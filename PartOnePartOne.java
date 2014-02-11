import lejos.robotics.navigation.DifferentialPilot
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Button;
import lejos.nxt.addon.OpticalDistanceSensor;

public class PartOnePartOne {

        private final int    wheelDiameter = 55;
        private final int    trackWidth    = 160;
        private final double maxDistance   = 900f;
        private final double minDistance   = 150f;

        private NXTRegulatedMotor leftMotor;
        private NXTRegulatedMotor rightMotor;

        private boolean           m_run;
        private DifferentialPilot dPilot;

public static void main(String[] args) {

        PartOnePartOne p1 = new PartOnePartOne();

        p1.m_run      = true;
        p1.leftMotor  = new NXTRegulatedMotor(MotorPort.A);
        p1.rightMotor = new NXTRegulatedMotor(MotorPort.B);
        p1.dPilot     = new DifferentialPilot(p1.wheelDiameter, p1.trackWidth, p1.leftMotor, p1.rightMotor);
		
		p1.run();

		Button.waitForAnyPress();

	}

private void run() {

        OpticalDistanceSensor ods = new OpticalDistanceSensor(SensorPort.S3);

        while(this.m_run) {

                travelWithProportionalControl(ods);

        }

}

private void travelWithProportionalControl(OpticalDistanceSensor ods) {

	int    distance = ods.getDistance();
	double speed    = distanceToSpeed(distance);

	System.out.println("Distance: " + distance);
	System.out.println("Speed: " + speed);

	this.dPilot.setTravelSpeed(speed);

	if (speed > 0) {

    	this.dPilot.forward();

	} else if (speed < 0) {

        this.dPilot.backward();

	} else {

        this.dPilot.stop();

    }

}

private double distanceToSpeed(int distance) {

        if (distance > this.maxDistance) {

                return 600f;

		} else if (distance >= (this.minDistance - 25) && distance <= (this.minDistance + 25)) {

        	return 0f;

		} else if (distance < this.minDistance) {

        	return -(600f * (distance / (this.maxDistance - this.minDistance))) * 5;

		} else {

	        return (600f * (distance / (this.maxDistance - this.minDistance)));

        }


	}

}

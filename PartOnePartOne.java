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

/**
 * Constructs a new OpticalDistanceSensor object constantly checks the distance from objects
*/
private void run() {

        OpticalDistanceSensor ods = new OpticalDistanceSensor(SensorPort.S3);

        while(this.m_run) {

                travelWithProportionalControl(ods);

        }

}

/**
 * Method changes movement of robot depending on speed 
 * speed is set by another method which requires distance
 * obtained by ods.getDistance();
 * @param ods Infrared sensor or Distance sensor
*/
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

/**
 * Sets the speed of the robot depending on the distance input into the method
 * @param distance The distance the robot is to an onject
*/
private double distanceToSpeed(int distance) {

        if (distance > this.maxDistance) {

                return 600f; //max speed if distance detected > maxDistance

		} else if (distance >= (this.minDistance - 25) && distance <= (this.minDistance + 25)) {

        	return 0f; //0 speed or stop if within configurable range

		} else if (distance < this.minDistance) {

        	return -(600f * (distance / (this.maxDistance - this.minDistance))) * 5;
        	//reverse proportional control so it goes backwards if it's too close to an object

		} else {

	        return (600f * (distance / (this.maxDistance - this.minDistance)));
	        //otherwise travel with proportional control

        }


	}

}

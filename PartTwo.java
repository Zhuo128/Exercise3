package Exercise3;

import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;
import lejos.robotics.Color;
import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import java.util.Random;

public class PartTwo {

	private final int wheelDiameter = 55;
	private final int trackWidth    = 160;
	private       int threshold;
	
	private LightSensor lSens;
	private LightSensor rSens;
	
	private NXTRegulatedMotor lMotor;
	private NXTRegulatedMotor rMotor;
	private DifferentialPilot dPilot;
	
	private Random rand;
	
	private boolean atJunction;
	
	public static void main(String[] args) {
		
		PartTwo p2 = new PartTwo();
		
		p2.lMotor = new NXTRegulatedMotor(MotorPort.A);
		p2.rMotor = new NXTRegulatedMotor(MotorPort.B);
		p2.dPilot = new DifferentialPilot(p2.wheelDiameter, p2.trackWidth, p2.lMotor, p2.rMotor);
		
		p2.lSens = new LightSensor(SensorPort.S1);
		p2.rSens = new LightSensor(SensorPort.S2);
		
		p2.rand = new Random();
		
		p2.atJunction = false;
		
		p2.calibrate();
		
		Button.waitForAnyPress();
		
		Delay.msDelay(500);
		
		while(!Button.ENTER.isDown()) {
			
			p2.run();
			
		}
		
	}
	
	private void run() {
		
		this.dPilot.setTravelSpeed(150);
		
		if (this.lSens.getLightValue() > this.threshold && this.rSens.getLightValue() > this.threshold && this.atJunction == false) {
			
			System.out.println("Straight");
			System.out.println(lSens.getLightValue());
			System.out.println(rSens.getLightValue());
			this.dPilot.forward();
			//this.dPilot.steer(0);
			
		} else if (this.lSens.getLightValue() <= this.threshold && this.rSens.getLightValue() > this.threshold && this.atJunction == false) {
			
			System.out.println("Left");
			//this.dPilot.stop();
			System.out.println(lSens.getLightValue());
			System.out.println(rSens.getLightValue());
			this.dPilot.rotate(10);
			//this.dPilot.steer(200, 15);
			
		} else if (this.lSens.getLightValue() > this.threshold && this.rSens.getLightValue() <= this.threshold && this.atJunction == false) {
			
			System.out.println("Right");
			//this.dPilot.stop();
			System.out.println(lSens.getLightValue());
			System.out.println(rSens.getLightValue());
			this.dPilot.rotate(-10);
			//this.dPilot.steer(-200, 15);
			
		} else {
			
			System.out.println("Junction hit");
			this.atJunction = true;
			this.dPilot.stop();
			this.dPilot.travel(70);
			
			if (this.rand.nextDouble() > 0.5f) {
				
				this.dPilot.rotate(100);
				//this.dPilot.steer(200, 90);
				
			} else if (this.rand.nextDouble() < 0.5f) {
				
				this.dPilot.rotate(-100);
				//this.dPilot.steer(-200, 90);
				
			} else {
				
				System.out.println("Do a barrel roll!");
				this.dPilot.rotate(200);
				this.dPilot.rotate(200);
				
			}
			
			this.atJunction = false;
			
			/*
			this.dPilot.stop();			
			this.dPilot.rotate(30);
			
			run();
			
			System.out.println("Backwards");
			this.dPilot.stop();
			System.out.println(lSens.getLightValue());
			System.out.println(rSens.getLightValue());
			this.dPilot.travel(-100);
			*/
			
		}
		
	}
	
	private void calibrate() {
		
		System.out.println("Threshold reading 1");
		Button.waitForAnyPress();
		int takeOne = this.lSens.getLightValue();
		System.out.println("Reading 1 complete");
		
		System.out.println("Threshold reading 2");
		Button.waitForAnyPress();
		int takeTwo = this.rSens.getLightValue();
		System.out.println("Reading 2 complete");
		
		this.threshold = (takeOne + takeTwo) / 2 + 2;
		System.out.println("Threshold: " + this.threshold);
	}

}

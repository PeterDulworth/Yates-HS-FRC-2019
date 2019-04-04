/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.UsbCamera;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive rbt;
  private Joystick logitechCtlr;
  private Joystick logitechCtlr2;
  private Spark ballCollectorCtrl;
  private Spark diskCollectorCtrl;

  @Override
  public void robotInit() {
    System.out.println("[robotInit()]");

    // UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(1);
    CameraServer.getInstance().startAutomaticCapture();

    // initialize drive speed controllers
    Spark leftDrive1 = new Spark(0);
    Spark leftDrive2 = new Spark(1);
    Spark rightDrive1 = new Spark(2);
    Spark rightDrive2 = new Spark(3);
    
    // enable dead band protection on all drive motors
    // leftDrive1.enableDeadbandElimination(true);
    // leftDrive2.enableDeadbandElimination(true);
    // rightDrive1.enableDeadbandElimination(true);
    // rightDrive2.enableDeadbandElimination(true);
    
    // create a group for the left side
    SpeedControllerGroup leftDriveCtrls = new SpeedControllerGroup(leftDrive1, leftDrive2);
    // create a group for the right side
    SpeedControllerGroup rightDriveCtrls = new SpeedControllerGroup(rightDrive1, rightDrive2);

    diskCollectorCtrl = new Spark(4);
    ballCollectorCtrl = new Spark(5);
    
    // drive train
    rbt = new DifferentialDrive(leftDriveCtrls, rightDriveCtrls);
    
    // logitech remote control 1
    logitechCtlr = new Joystick(0);
    logitechCtlr2 = new Joystick(1);
    
  }

  @Override
  public void autonomousInit() {
    System.out.println("Autonomous On");
  }

  @Override
  public void autonomousPeriodic() {
    System.out.println("[teleopPeriodic] L: " + logitechCtlr.getRawAxis(1) + " R: " + logitechCtlr.getRawAxis(5));
    rbt.tankDrive(-0.65 * logitechCtlr.getRawAxis(1), -0.65 * logitechCtlr.getRawAxis(5));
    // System.out.println("1" + logitechCtlr.getRawButton(1)); // a
    // System.out.println("2" + logitechCtlr.getRawButton(2)); // b
    // System.out.println("3" + logitechCtlr.getRawButton(3)); // x
    // System.out.println("4" + logitechCtlr.getRawButton(4)); // y

    ballCollector(logitechCtlr2.getRawButton(3), logitechCtlr2.getRawButton(1));
    hatchCollector(logitechCtlr2.getRawButton(4), logitechCtlr2.getRawButton(2));
  }

  @Override
  public void teleopPeriodic() {
    System.out.println("[teleopPeriodic] L: " + logitechCtlr.getRawAxis(1) + " R: " + logitechCtlr.getRawAxis(5));
    rbt.tankDrive(-0.75 * logitechCtlr.getRawAxis(1), -0.75 * logitechCtlr.getRawAxis(5));

    // raw btn 1 -> A
    // raw btn 2 -> B
    // raw btn 3 -> X
    // raw btn 4 -> Y
    // raw btn 5 -> top left trigger
    // raw btn 6 -> top right trigger
    // raw btn 7 -> back
    // raw btn 8 -> start
    // axis 2 -> bottom left trigger
    // axis 3 -> bottom right trigger

    ballCollector(logitechCtlr2.getRawAxis(3) > 0.5 ? true : false, logitechCtlr2.getRawAxis(2) > 0.5 ? true : false);
    hatchCollector(logitechCtlr2.getRawButton(5), logitechCtlr2.getRawButton(6));
  }

  private void ballCollector(boolean in, boolean out) {
    // if X is pressed, run the ball collector forwards
    if (in) {
      ballCollectorCtrl.set(-0.92); // PWM value between -1.0, 1.0
      System.out.println("[button X] ball collector in");
    }

    // if A is pressed, run the ball collector backwardss
    else if (out) {
      ballCollectorCtrl.set(0.92); // PWM value between -1.0, 1.0
      System.out.println("[button A] ball collector out");
    }

    else {
      ballCollectorCtrl.set(0.0); // PWM value between -1.0, 1.0
    }
  };

  private void hatchCollector(boolean up, boolean down) {
    // if X is pressed, run the ball collector forwards
    if (up) {
      diskCollectorCtrl.set(1.0); // PWM value between -1.0, 1.0
      System.out.println("[button Y] ball collector in");
    }

    // if A is pressed, run the ball collector backwardss
    else if (down) {
      diskCollectorCtrl.set(-1.0); // PWM value between -1.0, 1.0
      System.out.println("[button B] ball collector out");
    }

    else {
      diskCollectorCtrl.set(0.0); // PWM value between -1.0, 1.0
    }
  };
}

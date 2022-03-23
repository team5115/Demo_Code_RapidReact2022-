package frc.team5115.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Robot.RobotContainer;
import static frc.team5115.Constants.kD;
import static java.lang.Math.tan;
import static java.lang.Math.toRadians;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

import static frc.team5115.Constants.*;

public class Drivetrain extends SubsystemBase{

    private TalonSRX frontLeft;
    private TalonSRX frontRight;
    private TalonSRX backLeft;
    private TalonSRX backRight;

    private double frontLeftSpeed;
    private double frontRightSpeed;
    private double backLeftSpeed;
    private double backRightSpeed;

    private double rightSpd;
    private double leftSpd;

    public double throttle;
    
    public Drivetrain() {
        frontLeft = new TalonSRX(FRONT_LEFT_MOTOR_ID);
        frontRight = new TalonSRX(FRONT_RIGHT_MOTOR_ID);
        backLeft = new TalonSRX(BACK_LEFT_MOTOR_ID);
        backRight = new TalonSRX(BACK_RIGHT_MOTOR_ID);

        throttle = .25;
    }

    public void stop() {
        plugAndChugDrive(0, 0, 0, 0);
    }

    public void TankDrive(double x, double y, double throttle) { 
        leftSpd = (x-y) * throttle;
        rightSpd = (x+y) * throttle;
        plugAndChugDrive(leftSpd, rightSpd, leftSpd, rightSpd);
    }

    public void MecanumSimpleDrive(double y, double x, double z) {
    
        frontLeftSpeed = (-x + y + z);
        backLeftSpeed = (-x + y - z);
        frontRightSpeed = (x +  y + z);
        backRightSpeed = (x + y - z);

        plugAndChugDrive(frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
    }

    public void FieldOrientedDrive(double strafe, double fwd, double rotate){
        double x;
        double y;
        double pi = 3.1415926;
       // float gyro_degrees = gyro.getYaw();
        double gyro_degrees = 0.5123;
        double gyro_radians = gyro_degrees * pi/180; 

        x = strafe*Math.cos(gyro_radians) + fwd*Math.sin(gyro_radians);
        y = strafe*Math.sin(gyro_radians) - fwd*Math.cos(gyro_radians);

        frontLeftSpeed = (y + x + rotate);
        backLeftSpeed = (y - x + rotate);
        frontRightSpeed = (-y + x + rotate);
        backRightSpeed = (-y - x + rotate);

        plugAndChugDrive(frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
    }

    public void plugAndChugDrive(double frontleftspeed, double frontrightspeed, double backleftspeed, double backrightspeed){
        frontLeft.set(ControlMode.PercentOutput, frontleftspeed*throttle);
        frontRight.set(ControlMode.PercentOutput, frontrightspeed*throttle);
        backLeft.set(ControlMode.PercentOutput, backleftspeed*throttle);
        backRight.set(ControlMode.PercentOutput, backrightspeed*throttle);
    }
 
}

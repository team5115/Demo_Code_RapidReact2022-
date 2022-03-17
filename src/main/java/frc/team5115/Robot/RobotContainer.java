package frc.team5115.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team5115.Subsystems.*;
import static frc.team5115.Constants.*;

import frc.team5115.Commands.Subsystems.ReverseFeeder;
import frc.team5115.Commands.Subsystems.Climber.LeftForwardClimb;
import frc.team5115.Commands.Subsystems.Climber.LeftReverseClimb;
import frc.team5115.Commands.Subsystems.Climber.RightForwardClimb;
import frc.team5115.Commands.Subsystems.Climber.RightReverseClimb;
import frc.team5115.Commands.Subsystems.Limelight.Update;
import frc.team5115.Commands.Subsystems.Shooter.DelayShootGroup;
import frc.team5115.Commands.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team5115.Commands.NewAuto.AutoCommandGroup;

public class RobotContainer {

    public Drivetrain drivetrain;
    public Intake intake;
    public Shooter shooter;
    public Feeder feeder;
    public Climber climber;
    public final Joystick joy = new Joystick(0);
    public final Limelight limelight = new Limelight();
    public Camera camera; 
    public Timer timer;
    public AutoCommandGroup autocommandgroup;

    public RobotContainer() {
        drivetrain = new Drivetrain();
        intake = new Intake();
        shooter = new Shooter();
        feeder = new Feeder();
        climber = new Climber();
        camera = new Camera();

        autocommandgroup = new AutoCommandGroup(intake, feeder, shooter, drivetrain, camera);
        timer = new Timer();
        timer.reset();
        configureButtonBindings();
    }

    public void configureButtonBindings() {
        new JoystickButton( joy, INTAKE_BUTTON_ID).whileHeld(new InstantCommand(intake::forwardIntake)).whenReleased(new InstantCommand(intake::stop));
        new JoystickButton(joy, SHOOTER_BUTTON_ID).whileHeld(new DelayShootGroup(intake, feeder, shooter)).whenReleased(new Stopeverything(intake, feeder, shooter));

        new JoystickButton(joy, LEFT_CLIMBER_UP_BUTTON_ID).whileHeld(new LeftForwardClimb(climber)).whenReleased(new InstantCommand(climber::leftStop));
        new JoystickButton(joy, RIGHT_CLIMBER_UP_BUTTON_ID).whileHeld(new RightForwardClimb(climber)).whenReleased(new InstantCommand(climber::rightStop));
        new JoystickButton(joy, LEFT_CLIMBER_DOWN_BUTTON_ID).whileHeld(new LeftReverseClimb(climber)).whenReleased(new InstantCommand(climber::leftStop));
        new JoystickButton(joy, RIGHT_CLIMBER_DOWN_BUTTON_ID).whileHeld(new RightReverseClimb(climber)).whenReleased(new InstantCommand(climber::rightStop));
        
      //  new JoystickButton(joy, 7).whileHeld(new InstantCommand(intake::reverseIntake(-.25)).alongWith(new InstantCommand(feeder::reverseFeeder))).whenReleased(new Stopeverything(intake, feeder, shooter));
        new JoystickButton(joy, 8).whenPressed(new ReverseFeeder(intake, feeder));

        new JoystickButton(joy, 9).whileHeld(new InstantCommand(drivetrain::kidMode)).whenReleased(new InstantCommand(drivetrain::adultMode));
   
    }

    public void configureLimelight(){
      drivetrain.setDefaultCommand(new Update(limelight).perpetually());}

    public void setDriveDefault(){
        drivetrain.setDefaultCommand(new driveDefaultCommand(drivetrain, joy).perpetually());
    }

   static class driveDefaultCommand extends CommandBase {
        Drivetrain drivetrain;
        Joystick joy;
        Camera camera;

        public driveDefaultCommand(Drivetrain drivetrain, Joystick joystick) {
            addRequirements(drivetrain);
            this.drivetrain = drivetrain;
            joy = joystick;
          
        }

        @Override
        public void execute() {
           drivetrain.MecanumSimpleDrive(joy.getRawAxis(4), joy.getRawAxis(1), joy.getRawAxis(0));
           drivetrain.DistanceDetectionAverage();
     
        }    
    }


    public void startTeleop(){
        System.out.println("Starting teleop");
        new Stopeverything(intake, feeder, shooter);
        //NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);
        drivetrain.resetEncoder();
        if (autocommandgroup != null) {
            autocommandgroup.cancel();
          }
  
    }

    public void startAuto(){
        //NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);
        drivetrain.resetEncoder();
        if (autocommandgroup != null) {
            autocommandgroup.schedule();
          }
        
    }

    public void autoPeriod(){
      //  drivetrain.printEncoderDistance();
       // drivetrain.DistanceDetectionAverage();

        }
    public void teleopPeriodic(){
        //climber.print();
    }

    }
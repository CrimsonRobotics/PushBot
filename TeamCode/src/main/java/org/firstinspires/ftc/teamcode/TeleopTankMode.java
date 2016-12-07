package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TeleopTankMode", group = "Sensor")

public class TeleopTankMode extends OpMode {

    DcMotor leftMotor;// Initialize variables to represent motors
    DcMotor rightMotor;
    DcMotor loaderMotor;
    DcMotor winchMotor;

    Servo rightServo;// Initialize variables to represent servos
    Servo leftServo;
    Servo rightButtonServo;
    Servo leftButtonServo;

    // Initialize variables
    float loaderPower = 0;

    float leftY = 0;
    float rightY = 0;

    boolean loaderToggle = false;
    boolean prevLeftTrigger = false;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("left_drive"); //Get references to the motors from the hardware map
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        winchMotor = hardwareMap.dcMotor.get("winch");
        loaderMotor = hardwareMap.dcMotor.get("shooter");

        rightServo = hardwareMap.servo.get("rightServo"); // Get references to the servos from the hardware map
        leftServo = hardwareMap.servo.get("leftServo");
        rightButtonServo = hardwareMap.servo.get("leftButtonServo");
        leftButtonServo = hardwareMap.servo.get("rightButtonServo");

        winchMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);// Reset the encoder for the winch

        rightMotor.setDirection(DcMotor.Direction.REVERSE);// Reverse the right motor

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        if (gamepad1.right_stick_y != 0){ // Allow triggers to determine the power of the drive motors
            leftY = -gamepad1.right_stick_y;
        }else{
            leftY = -gamepad2.right_stick_y;
        }
        if (gamepad1.left_stick_y != 0){
            rightY = -gamepad1.left_stick_y;
        }else{
            rightY = -gamepad2.left_stick_y;
        }

        if ((gamepad1.left_trigger > .1 || gamepad2.left_trigger > .1) && !prevLeftTrigger){ // Toggle variable only when the left trigger is pressed, not held
            loaderToggle = !loaderToggle;
        }
        if (gamepad1.left_trigger > .1 || gamepad2.left_trigger > .1){ // Record previous position of the trigger
            prevLeftTrigger = true;
        } else{
            prevLeftTrigger = false;
        }

        if (gamepad1.right_trigger > .1 || gamepad2.right_trigger > .1){ // Reverse direction of loaderMotor only when the right trigger is held
            loaderMotor.setDirection(DcMotor.Direction.FORWARD);
        }else{
            loaderMotor.setDirection(DcMotor.Direction.REVERSE);
        }

        if (loaderToggle){ // Convert the boolean loaderToggle to the int loaderPower
            loaderPower = 1;
        }else{
            loaderPower = 0;
        }


        if (gamepad1.right_bumper || gamepad2.right_bumper){ // Extend right button pusher
            rightButtonServo.setPosition(1);
        }else{
            rightButtonServo.setPosition(0);
        }
        if (gamepad1.left_bumper || gamepad2.left_bumper){ // Extend left button pusher
            leftButtonServo.setPosition(0);
        }else{
            leftButtonServo.setPosition(1);
        }


        if(gamepad1.dpad_up || gamepad2.dpad_up) {
            telemetry.addData("Winch", "Up");
            telemetry.update();

            winchMotor.setTargetPosition(66000); // Prepare winchMotor to about 46 times, because this encoder will send 1440 pulses per rotation

            winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            winchMotor.setPower(1);

        }else if(gamepad1.dpad_down || gamepad2.dpad_down){
            telemetry.addData("Winch", "Down");
            telemetry.update();

            winchMotor.setTargetPosition(0); // Prepare winchMotor to return to its initial position

            winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            winchMotor.setPower(.5);

        }else if (gamepad1.dpad_left || gamepad2.dpad_left){
            winchMotor.setPower(0); // Pause the movement of the winch
        }

        telemetry.addData("Winch", winchMotor.getCurrentPosition());
        telemetry.update();


        if(gamepad1.x || gamepad2.x) { // Extend arms to grab the cap ball
            rightServo.setPosition(1);
            leftServo.setPosition(0);
        }else if(gamepad1.b || gamepad2.b) { // Retract arms
            rightServo.setPosition(0);
            leftServo.setPosition(1);
        }


        leftMotor.setPower(leftY); // Apply power variables
        rightMotor.setPower(rightY);
        loaderMotor.setPower(loaderPower);

        telemetry.addData("Left Direction", leftMotor.getDirection());
        telemetry.update();

    }
}


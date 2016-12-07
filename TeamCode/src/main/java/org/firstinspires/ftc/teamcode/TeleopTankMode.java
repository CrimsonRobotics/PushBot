package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TeleopTankMode", group = "Sensor")

public class TeleopTankMode extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor loaderMotor;
    //DcMotor shooterMotor;

    DcMotor winchMotor;

    Servo upLeftServo;
    Servo dnLeftServo;
    Servo upRightServo;
    Servo dnRightServo;

    Servo rightServo;
    Servo leftServo;

    Servo rightButtonServo;
    Servo leftButtonServo;

    double closePosition = 0.2;
    double openPosition = 0.4;



    float loaderPower = 0;
    //float shooterPower = 0;

    float leftY = 0;
    float rightY = 0;

    boolean loaderToggle = false;
    boolean prevLeftTrigger = false;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        winchMotor = hardwareMap.dcMotor.get("winch");

        winchMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //loaderMotor = hardwareMap.dcMotor.get("loader");
        loaderMotor = hardwareMap.dcMotor.get("shooter");

        //upLeftServo = hardwareMap.servo.get("upLeftServo");
        //dnLeftServo = hardwareMap.servo.get("dnLeftServo");
        //upRightServo = hardwareMap.servo.get("upRightServo");
        //dnRightServo = hardwareMap.servo.get("dnRightServo");


        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");

        rightButtonServo = hardwareMap.servo.get("leftButtonServo");
        leftButtonServo = hardwareMap.servo.get("rightButtonServo");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        //loaderMotor.setDirection(DcMotor.Direction.REVERSE);



        telemetry.addData("Status", "Resetting Winch Encoder");
        telemetry.update();

        //winchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        //telemetry.addData("Path0",  "Starting at %7d :%7d", winchMotor.getCurrentPosition());
        telemetry.update();

    }

    @Override
    public void loop() {

        if (gamepad1.right_stick_y != 0){
            leftY = -gamepad1.right_stick_y;
        }else{
            leftY = -gamepad2.right_stick_y;
        }
        if (gamepad1.left_stick_y != 0){
            rightY = -gamepad1.left_stick_y;
        }else{
            rightY = -gamepad2.left_stick_y;
        }


        if ((gamepad1.left_trigger > .1 || gamepad2.left_trigger > .1) && !prevLeftTrigger){
            loaderToggle = !loaderToggle;
        }

        if (gamepad1.left_trigger > .1 || gamepad2.left_trigger > .1){
            prevLeftTrigger = true;
        }else{
            prevLeftTrigger = false;
        }

        if (!(gamepad1.left_trigger > .1 || gamepad2.left_trigger > .1) && !(gamepad1.right_trigger > .1 || gamepad2.right_trigger > .1)){
            //loaderToggle = false;
        }

        if (gamepad1.right_trigger > .1 || gamepad2.right_trigger > .1){
            loaderMotor.setDirection(DcMotor.Direction.FORWARD);
            //loaderPower = 1;
        }else{
            loaderMotor.setDirection(DcMotor.Direction.REVERSE);
        }

        if (loaderToggle){
            loaderPower = 1;
        }else{
            loaderPower = 0;
        }


        if (gamepad1.right_bumper || gamepad2.right_bumper){
            rightButtonServo.setPosition(1);
        }else{
            rightButtonServo.setPosition(0);
        }
        if (gamepad1.left_bumper || gamepad2.left_bumper){
            leftButtonServo.setPosition(0);
        }else{
            leftButtonServo.setPosition(1);
        }


        if(gamepad1.dpad_up || gamepad2.dpad_up) {
            telemetry.addData("Winch", "Up");
            telemetry.update();

            winchMotor.setTargetPosition(66000);

            winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            winchMotor.setPower(1);

        }else if(gamepad1.dpad_down || gamepad2.dpad_down){
            telemetry.addData("Winch", "Down");
            telemetry.update();

            winchMotor.setTargetPosition(0);

            winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            winchMotor.setPower(.5);

        }else if (gamepad1.dpad_left || gamepad2.dpad_left){

            winchMotor.setPower(0);
        }

        telemetry.addData("Winch", winchMotor.getCurrentPosition());
        telemetry.update();




        if(gamepad1.x || gamepad2.x) {
            rightServo.setPosition(1);
            leftServo.setPosition(0);
        }else if(gamepad1.b || gamepad2.b) {
            rightServo.setPosition(0);
            leftServo.setPosition(1);
        }


        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
        loaderMotor.setPower(loaderPower);

        telemetry.addData("Left Direction", leftMotor.getDirection());
        telemetry.update();

    }
}


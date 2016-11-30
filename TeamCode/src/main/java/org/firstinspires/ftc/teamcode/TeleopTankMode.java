package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TeleopTankMode", group = "Sensor")

public class TeleopTankMode extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;
    //DcMotor loaderMotor;
    DcMotor shooterMotor;

    DcMotor winchMotor;

    Servo upLeftServo;
    Servo dnLeftServo;
    Servo upRightServo;
    Servo dnRightServo;

    Servo rightServo;
    Servo leftServo;

    double closePosition = 0.2;
    double openPosition = 0.4;



    float loaderPower = 0;
    float shooterPower = 0;




    final static int ENCODER_CPR = 1440;
    final static double GEAR_RATIO = 1;
    final static int WHEEL_DIAMETER = 1;
    final static int DISTANCE = 5;

    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    final static double ROTATIONS = DISTANCE / CIRCUMFERENCE;
    static final double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;




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
        shooterMotor = hardwareMap.dcMotor.get("shooter");

        //upLeftServo = hardwareMap.servo.get("upLeftServo");
        //dnLeftServo = hardwareMap.servo.get("dnLeftServo");
        //upRightServo = hardwareMap.servo.get("upRightServo");
        //dnRightServo = hardwareMap.servo.get("dnRightServo");


        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        shooterMotor.setDirection(DcMotor.Direction.REVERSE);



        telemetry.addData("Status", "Resetting Winch Encoder");
        telemetry.update();

        //winchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        //telemetry.addData("Path0",  "Starting at %7d :%7d", winchMotor.getCurrentPosition());
        telemetry.update();

    }

    @Override
    public void loop() {

        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        if (gamepad1.left_bumper){
            loaderPower = 1;
        }else{
            loaderPower = 0;
        }

        if (gamepad1.right_bumper){
            shooterPower = 1;
        }else{
            shooterPower = 0;
        }


        if(gamepad1.y) {
            telemetry.addData("Winch", "Up");
            telemetry.update();

            winchMotor.setTargetPosition(66000);

            winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            winchMotor.setPower(.5);



        }else if(gamepad1.a){
            telemetry.addData("Winch", "Down");
            telemetry.update();

            winchMotor.setTargetPosition(0);

            winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            winchMotor.setPower(.5);

        }else if (gamepad1.left_trigger > 0.1){

            winchMotor.setPower(0);
        }

        telemetry.addData("Winch", winchMotor.getCurrentPosition());
        telemetry.update();




        if(gamepad1.x) {
            //upLeftServo.setPosition(openPosition);
            //upRightServo.setPosition(openPosition);
            //dnLeftServo.setPosition(openPosition);
            //dnRightServo.setPosition(openPosition);
            rightServo.setPosition(1);
            leftServo.setPosition(0);
        }
        //Move servo 1 to the down position when a button is pressed
        if(gamepad1.b) {
            //upLeftServo.setPosition(closePosition);
            //upRightServo.setPosition(closePosition);
            //dnLeftServo.setPosition(closePosition);
            //dnRightServo.setPosition(closePosition);
            rightServo.setPosition(0);
            leftServo.setPosition(1);
        }


        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
        //loaderMotor.setPower(loaderPower);
        shooterMotor.setPower(shooterPower);


    }
}


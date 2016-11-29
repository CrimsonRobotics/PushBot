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
    //DcMotor shooterMotor;

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


    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder







    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        //loaderMotor = hardwareMap.dcMotor.get("loader");
        //shooterMotor = hardwareMap.dcMotor.get("shooter");

        //upLeftServo = hardwareMap.servo.get("upLeftServo");
        //dnLeftServo = hardwareMap.servo.get("dnLeftServo");
        //upRightServo = hardwareMap.servo.get("upRightServo");
        //dnRightServo = hardwareMap.servo.get("dnRightServo");


        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");

        //reverse the right motor
        rightMotor.setDirection(DcMotor.Direction.REVERSE);



        //winchMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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


        /*if(gamepad1.y) {


            leftMotor.setTargetPosition(3);

            winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            winchMotor.setPower(.5);



        }*/





        if(gamepad1.x) {
            //upLeftServo.setPosition(openPosition);
            //upRightServo.setPosition(openPosition);
            //dnLeftServo.setPosition(openPosition);
            //dnRightServo.setPosition(openPosition);
            rightServo.setPosition(openPosition);
            leftServo.setPosition(openPosition);
        }
        //Move servo 1 to the down position when a button is pressed
        if(gamepad1.b) {
            //upLeftServo.setPosition(closePosition);
            //upRightServo.setPosition(closePosition);
            //dnLeftServo.setPosition(closePosition);
            //dnRightServo.setPosition(closePosition);
            rightServo.setPosition(closePosition);
            leftServo.setPosition(closePosition);
        }


        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
        //loaderMotor.setPower(loaderPower);
        //shooterMotor.setPower(shooterPower);


    }
}


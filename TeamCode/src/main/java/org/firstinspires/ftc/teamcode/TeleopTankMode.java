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
    DcMotor loaderMotor;
    DcMotor shooterMotor;

    Servo upLeftServo;
    Servo dnLeftServo;
    Servo upRightServo;
    Servo dnRightServo;

    double closePosition = 0.2;
    double openPosition = 0.4;



    float loaderPower = 0;
    float shooterPower = 0;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        loaderMotor = hardwareMap.dcMotor.get("loader");
        shooterMotor = hardwareMap.dcMotor.get("shooter");

        upLeftServo = hardwareMap.servo.get("upLeftServo");
        dnLeftServo = hardwareMap.servo.get("dnLeftServo");
        upRightServo = hardwareMap.servo.get("upRightServo");
        dnRightServo = hardwareMap.servo.get("dnRightServo");


        //reverse the right motor
        rightMotor.setDirection(DcMotor.Direction.REVERSE);




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


        if(gamepad1.x) {
            upLeftServo.setPosition(openPosition);
            upRightServo.setPosition(openPosition);
            dnLeftServo.setPosition(openPosition);
            dnRightServo.setPosition(openPosition);
        }
        //Move servo 1 to the down position when a button is pressed
        if(gamepad1.b) {
            upLeftServo.setPosition(closePosition);
            upRightServo.setPosition(closePosition);
            dnLeftServo.setPosition(closePosition);
            dnRightServo.setPosition(closePosition);
        }


        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
        loaderMotor.setPower(loaderPower);
        shooterMotor.setPower(shooterPower);


    }
}


package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorController;

@TeleOp(name = "TeleopTankMode", group = "Sensor")

public class TeleopTankMode extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor loaderMotor;


    float loaderPower = 0;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        //get references to the motors from the hardware map
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        loaderMotor = hardwareMap.dcMotor.get("loader");

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





        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);
        loaderMotor.setPower(loaderPower);

    }
}


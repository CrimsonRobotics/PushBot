package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Sensor: MR ODS", group = "Sensor")

public class SensorOpticalDistance extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;

    OpticalDistanceSensor odsSensor;


    @Override
    public void init() {

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);



        odsSensor = hardwareMap.opticalDistanceSensor.get("sensor_ods");


        

    }

    @Override
    public void loop() {

        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        leftMotor.setPower(leftY);
        rightMotor.setPower(rightY);


        telemetry.addData("Raw",    odsSensor.getRawLightDetected());
        telemetry.addData("Normal", odsSensor.getLightDetected());

        telemetry.update();

    }
}


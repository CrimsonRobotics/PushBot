package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Sensor: ODS", group = "Sensor")

public class SensorOpticalDistance extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor winchMotor;

    OpticalDistanceSensor leftODS;
    OpticalDistanceSensor rightODS;

    boolean leftLine;
    boolean rightLine;

    boolean prevLeftLine = false;
    boolean prevRightLine = false;

    double leftP = 0;
    double rightP = 0;

    int state = 1;

    String action = "";

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        rightODS = hardwareMap.opticalDistanceSensor.get("sensor_right_ods");
        leftODS = hardwareMap.opticalDistanceSensor.get("sensor_left_ods");

    }

    @Override
    public void loop() {


        if (state == 3) {


            // it should stop following the line once the color sensor detects either red or blue

            rightLine = (rightODS.getLightDetected() > .21); // Check if line is underneath right sensor
            leftLine = (leftODS.getLightDetected() > .21); // Check if line is underneath left sensor

            if (!leftLine && !rightLine && !prevLeftLine && !prevRightLine) { // No line
                action = "Forward";
                leftP = -1;
                rightP = -1;

            } else if (leftLine && !rightLine && !prevLeftLine && !prevRightLine) { // Entering line towards right
                action = "Forward Right";
                leftP = -1;
                rightP = -0.05;

            } else if (!leftLine && rightLine && !prevLeftLine && !prevRightLine) { // Entering line towards left
                action = "Forward Left";
                leftP = -0.05;
                rightP = -1;

            } else if (leftLine && !rightLine && prevLeftLine && prevRightLine) { // Leaving line towards right
                action = "Back Right";
                leftP = -1;
                rightP = .05;

            } else if (!leftLine && rightLine && prevLeftLine && prevRightLine) { // Leaving line towards left
                action = "Back Left";
                leftP = .05;
                rightP = -1;

            } else if (leftLine && rightLine && prevLeftLine && prevRightLine) { // On line
                action = "Forward";
                leftP = -1;
                rightP = -1;

            }


            prevLeftLine = leftLine;
            prevRightLine = rightLine;
        }
        leftMotor.setPower(leftP);
        rightMotor.setPower(rightP);



        telemetry.addData("Right Raw",    rightODS.getRawLightDetected());
        telemetry.addData("Right Normal", rightODS.getLightDetected());
        telemetry.addData("Left Raw",    rightODS.getRawLightDetected());
        telemetry.addData("Left Normal", rightODS.getLightDetected());
        telemetry.addData("Move ", action);

        telemetry.update();

    }
}


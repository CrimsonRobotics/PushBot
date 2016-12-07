package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

@TeleOp(name = "Sensor: ODS", group = "Sensor")

public class SensorOpticalDistance extends OpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;

    OpticalDistanceSensor leftODS;
    OpticalDistanceSensor rightODS;

    ColorSensor colorSensor;

    boolean leftLine;
    boolean rightLine;

    Servo rightButtonServo;
    Servo leftButtonServo;

    Servo rightServo;

    boolean prevLeftLine = false;
    boolean prevRightLine = false;

    double leftP = 0;
    double rightP = 0;

    int state = 3;

    int beacon1 = 0;
    String action = "";

    int turnRight = 0;
    int turnLeft = 0;

    float angle = 90;// 0 is -->, 180 is <--
    int xpos = 0;
    int ypos = 0;

    int prevLeftEncoder = 0;
    int prevRightEncoder = 0;

    int buttonTimer = 0;
    int setTimer = 1;

    int color = 3;

    int beacon1done = 0;

    Sensor gyro;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        rightODS = hardwareMap.opticalDistanceSensor.get("sensor_right_ods");
        leftODS = hardwareMap.opticalDistanceSensor.get("sensor_left_ods");

        rightButtonServo = hardwareMap.servo.get("leftButtonServo");
        leftButtonServo = hardwareMap.servo.get("rightButtonServo");

        colorSensor = hardwareMap.colorSensor.get("sensor_color");

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        colorSensor.enableLed(false);

        //rightServo = hardwareMap.servo.get("rightServo");
        //rightServo.setPosition(1);




        SensorManager sensorManager;
        sensorManager = (SensorManager)hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


    }

    @Override
    public void loop() {


        if(gamepad1.y){// Easily Force a State Change For Testing
            state = 1;
        }else if(gamepad1.b){
            state = 2;
        }else if(gamepad1.a){
            state = 3;
        }else if(gamepad1.x){
            state = 4;
        }

        if (state == 1){
            leftP = 0;
            rightP = 0;
        }else if (state == 2){



            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            telemetry.update();


            if (colorSensor.red() >= 3 && colorSensor.alpha() <= 2 && colorSensor.blue() < 2){ // If Color Sensor Detects Red
                rightButtonServo.setPosition(1);// Extend Right Button Pusher
                leftButtonServo.setPosition(1);
            }


            if (colorSensor.blue() >= 2 && colorSensor.alpha() <= 2 && colorSensor.red() < 3){ // If Color Sensor Detects Blue
                rightButtonServo.setPosition(0);
                leftButtonServo.setPosition(0);// Extend Left Button Pusher
            }

            if (colorSensor.red() < 3){ // Retract Button Pushers
                rightButtonServo.setPosition(0);
            }
            if (colorSensor.blue() < 2){
                leftButtonServo.setPosition(1);
            }



        }else if (state == 3 && beacon1 == 0) {
            // it should stop following the line once the color sensor detects either red or blue
            leftLine = (leftODS.getLightDetected() > .6); // Check if line is underneath left sensor
            rightLine = (rightODS.getLightDetected() > .6); // Check if line is underneath right sensor

            if (!leftLine && !rightLine) { // No line
                action = "Forward Off Line";
                leftP = -.8;
                rightP = -.8;

                prevLeftLine = leftLine;
                prevRightLine = rightLine;

                if (turnRight == 1){ // Find Line Again By Turning Right
                    action = "Find Line To The Right";
                    leftP = -.3;
                    rightP = .3;
                }
                if (turnLeft == 1){ // Find Line Again By Turning Left
                    action = "Find Line To The Left";
                    leftP = .3;
                    rightP = -.3;
                }

            }
            if (leftLine && !rightLine && prevLeftLine && prevRightLine && beacon1 == 0) { // Leaving line towards right
                action = "Turn Right";

                leftP = -.3;
                rightP = 0.3;

                turnRight = 1;// Allows the program to understand that it has passed a line
            }
            if (!leftLine && rightLine && prevLeftLine && prevRightLine && beacon1 == 0) { // Leaving line towards left
                action = "Turn Left";
                leftP = .3;
                rightP = -.3;

                turnLeft = 1;// Allows the program to understand that it has passed a line
            }
            if (leftLine && rightLine && beacon1 == 0) { // On line
                action = "Forward On Line";
                leftP = -.4;
                rightP = -.4;

                turnRight = 0;
                turnLeft = 0;

                prevLeftLine = leftLine;
                prevRightLine = rightLine;
            }

            if (colorSensor.red() >= 3 && colorSensor.blue() < 2){ // If Color Sensor Detects Red
                rightButtonServo.setPosition(0);// Extend Right Button Pusher
                leftButtonServo.setPosition(0);
                leftP = -.2;
                rightP = -.2;
                color = 0;

                if (beacon1 == 0){
                    beacon1 = 2000;
                }
            }
            if (colorSensor.red() < 3){
                rightButtonServo.setPosition(0);
            }

            if (colorSensor.blue() >= 2 && colorSensor.red() < 3){ // If Color Sensor Detects Blue

                rightButtonServo.setPosition(1);
                leftButtonServo.setPosition(1);// Extend Left Button Pusher

                leftP = -.2;
                rightP = -.2;

                if (beacon1 == 0){
                    beacon1 = 2000;
                }


                color = 1;
            }
            if (colorSensor.blue() < 2){
                leftButtonServo.setPosition(1);
            }


        }else if (state == 4){



        }


        leftMotor.setTargetPosition((int)(800000*leftP));
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setTargetPosition((int)(800000*rightP));
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(leftP);
        rightMotor.setPower(rightP);

        if (beacon1 >= 1860){
            beacon1 --;
            beacon1done = 1;
        }

        if (beacon1 >= 1850 && beacon1 <= 1880){
            //leftP = .3;
            //rightP = .3;
            leftP = 0;
            rightP = 0;
        }
        if (beacon1 >= 00 && beacon1 <= 100){
            //leftP = -.7;
            //rightP = .7;
        }

        int leftDifference = prevLeftEncoder - leftMotor.getCurrentPosition();
        int rightDifference = prevRightEncoder - rightMotor.getCurrentPosition();



        prevLeftEncoder = leftMotor.getCurrentPosition();
        prevRightEncoder = rightMotor.getCurrentPosition();

        // Send info to Driver Station
        telemetry.addData("beacon1", beacon1);
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("Color", color);
        telemetry.addData("Right Raw",    rightODS.getRawLightDetected());
        telemetry.addData("Right Normal", rightODS.getLightDetected());
        telemetry.addData("Left Raw",    leftODS.getRawLightDetected());
        telemetry.addData("Left Normal", leftODS.getLightDetected());
        telemetry.addData("Move ", action);

        telemetry.update();

    }
}


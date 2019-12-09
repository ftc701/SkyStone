package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Hardware {

    public DcMotor LT;
    public DcMotor LB;
    public DcMotor RT;
    public DcMotor RB;

    public DcMotor lift1;
    public DcMotor lift2;

    public Servo servo1;
    public Servo servo2;
    public Servo servo3;
    public Servo servo4;

    HardwareMap hardwareMap;

    public Hardware(OpMode opMode){
        hardwareMap = opMode.hardwareMap;
        init();
    }

    public void init() {

        /*
        LT = hardwareMap.get(DcMotor.class, "LT");
        LB = hardwareMap.get(DcMotor.class, "LB");
        RT = hardwareMap.get(DcMotor.class, "RT");
        RB = hardwareMap.get(DcMotor.class, "RB");
         */

        lift1 = hardwareMap.get(DcMotor.class, "lift1");
        lift2 = hardwareMap.get(DcMotor.class, "lift2");

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");
        servo4 = hardwareMap.get(Servo.class, "servo4");

        /*
        LT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RT.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RT.setDirection(DcMotor.Direction.REVERSE);
        LT.setDirection(DcMotor.Direction.REVERSE);
         */

        lift1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lift1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lift2.setDirection(DcMotor.Direction.REVERSE);
    }
}
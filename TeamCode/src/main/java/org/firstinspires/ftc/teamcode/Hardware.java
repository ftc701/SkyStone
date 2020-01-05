package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Hardware {

    public DcMotor LTMotor;
    public DcMotor LBMotor;
    public DcMotor RTMotor;
    public DcMotor RBMotor;

    public DcMotor lift1;

    public Servo servo1;
    public Servo servo2;
    public Servo servo3;
    public Servo servo4;

    public Servo servo5;
    public Servo servo6;


    HardwareMap hardwareMap;

    public Hardware(OpMode opMode){
        hardwareMap = opMode.hardwareMap;
        init();
    }

    public void init() {


        LTMotor = hardwareMap.get(DcMotor.class, "LTMotor");
        LBMotor = hardwareMap.get(DcMotor.class, "LBMotor");
        RTMotor = hardwareMap.get(DcMotor.class, "RTMotor");
        RBMotor = hardwareMap.get(DcMotor.class, "RBMotor");


        lift1 = hardwareMap.get(DcMotor.class, "lift1");

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");
        servo4 = hardwareMap.get(Servo.class, "servo4");

        servo5 = hardwareMap.get(Servo.class, "servo5");
        servo6 = hardwareMap.get(Servo.class, "servo6");


        LTMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RTMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RTMotor.setDirection(DcMotor.Direction.REVERSE);
        LTMotor.setDirection(DcMotor.Direction.REVERSE);


        lift1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lift1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void OpenPower(double p1, double p2, double p3, double p4) {
        LTMotor.setPower(p1);
        LBMotor.setPower(p2);
        RTMotor.setPower(p3);
        RBMotor.setPower(p4);
    }

}
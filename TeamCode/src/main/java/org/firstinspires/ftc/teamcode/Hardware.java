package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;


public class Hardware {

    public DcMotor LTMotor;
    public DcMotor LBMotor;
    public DcMotor RTMotor;
    public DcMotor RBMotor;

    public DcMotor lift1;

    public CRServo servo1;
    public CRServo servo2;
    public Servo servo3;
    public Servo servo4;

    public Servo servo5;
    public Servo servo6;

    public AnalogInput ArmAngle;

    VisionPipeline vision = new VisionPipeline();

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

        servo1 = hardwareMap.get(CRServo.class, "servo1");
        servo2 = hardwareMap.get(CRServo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");
        servo4 = hardwareMap.get(Servo.class, "servo4");

        servo5 = hardwareMap.get(Servo.class, "servo5");
        servo6 = hardwareMap.get(Servo.class, "servo6");

        ArmAngle = hardwareMap.get(AnalogInput.class, "poten");


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

    public double mapFunction(double xVal, double[] coord1, double[] coord2)  {

        double x1 = coord1[0];
        double x2 = coord2[0];

        double y1 = coord1[1];
        double y2 = coord2[1];

        //0.75, 2.112
        //0, 0.045
        //y  = ((0.75 - 0)/(2.112 - 0.045) * (x - 0.045)
        //y  = (y1 - y2)/(x1 - x2) * (xVal - x2) + y2
        //y = mx + b
        //b = (y1) - (y1 - y2)/(x1 - x2) * x1
        //y = ((y1 - y2)/(x1 - x2)) * r.ArmAngle.getVoltage + ((y1) - (y1 - y2)/(x1 - x2) * x1)

        return ((y1 - y2)/(x1 - x2)) * xVal + ((y1) - (y1 - y2)/(x1 - x2) * x1);
    }

    public int SkyStoneLocation(){
        int location = 1; //Default

        int L = vision.getMassL();
        int C = vision.getMassC();
        int R = vision.getMassR();

        if (L > C && L > R){
            location = 1;
        } else if (C > L && C > R){
            location = 2;
        } else if (R > C && R > L){
            location = 3;
        }

        return location;
    }

    public void startVision(){
        OpenCvCamera phoneCam;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        phoneCam.setPipeline(vision);

        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

}
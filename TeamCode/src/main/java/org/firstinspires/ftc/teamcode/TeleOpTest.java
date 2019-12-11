package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOp_Test", group="Hardware Test")
public class TeleOpTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Hardware r = new Hardware(this);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            float LTMotorDC = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
            float LBMotorDC = -gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
            float RTMotorDC = -gamepad1.left_stick_y + gamepad1.left_stick_x +gamepad1.right_stick_x;
            float RBMotorDC = -gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;

            float largestVal = 1.0f;

            double radius = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;

            double rightX = gamepad1.right_stick_x;
            double v1 = radius * Math.cos(robotAngle) + rightX;
            double v2 = radius * Math.sin(robotAngle) - rightX;
            double v3 = radius * Math.sin(robotAngle) + rightX;
            double v4 = radius * Math.cos(robotAngle) - rightX;
            telemetry.addData("v1: ", v1);
            telemetry.addData("v2: ", v2);
            telemetry.addData("v3: ", v3);
            telemetry.addData("v4: ", v4);
            telemetry.update();

            largestVal = Math.max(largestVal, Math.abs(LTMotorDC));
            largestVal = Math.max(largestVal, Math.abs(LBMotorDC));
            largestVal = Math.max(largestVal, Math.abs(RTMotorDC));
            largestVal = Math.max(largestVal, Math.abs(RBMotorDC));

            LTMotorDC = LTMotorDC/largestVal;
            LBMotorDC = LBMotorDC/largestVal;
            RTMotorDC = RTMotorDC/largestVal;
            RBMotorDC = RBMotorDC/largestVal;


            r.OpenPower(LTMotorDC, LBMotorDC, RTMotorDC, RBMotorDC);


        }
    }
}

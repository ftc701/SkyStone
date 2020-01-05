package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Hardware TestOLD", group="Hardware Test")
public class BasicOpMode_LinearOLD extends LinearOpMode {

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

        if (gamepad1.dpad_up && runtime.milliseconds() > 40) {
            double newPosition = r.servo1.getPosition() + 0.05;
            newPosition = Range.clip(newPosition, 0, 1);
            r.servo1.setPosition(newPosition);
            r.servo2.setPosition(newPosition);
            runtime.reset();
        }

        if (gamepad1.dpad_down && runtime.milliseconds() > 40) {
            double newPosition = r.servo1.getPosition() - 0.05;
            newPosition = Range.clip(newPosition, 0, 1);
            r.servo1.setPosition(newPosition);
            r.servo2.setPosition(newPosition);
            runtime.reset();
            runtime.reset();
        }

            if (gamepad2.x) {
                r.lift1.setTargetPosition(-2000);
                r.lift2.setTargetPosition(-2000);
                r.lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                r.lift2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while(r.lift1.isBusy()) {
                    r.lift1.setPower(1);
                    r.lift2.setPower(1);
                    if (gamepad1.dpad_up && runtime.milliseconds() > 40) {
                        double newPosition = r.servo1.getPosition() + 0.05;
                        newPosition = Range.clip(newPosition, 0, 1);
                        r.servo1.setPosition(newPosition);
                        r.servo2.setPosition(newPosition);
                        runtime.reset();
                    }

                    if (gamepad1.dpad_down && runtime.milliseconds() > 40) {
                        double newPosition = r.servo1.getPosition() - 0.05;
                        newPosition = Range.clip(newPosition, 0, 1);
                        r.servo1.setPosition(newPosition);
                        r.servo2.setPosition(newPosition);
                        runtime.reset();
                        runtime.reset();
                    }
                }
            }
 {
                        double newPosition = r.servo1.getPosition() + 0.05;
                        newPosition = Range.clip(newPosition, 0, 1);
                        r.servo1.setPosition(newPosition);
                        r.servo2.setPosition(newPosition);
                        runtime.reset();
                    }
                    if (gamepad2.y) {
                        r.lift1.setTargetPosition(-10);
                        r.lift2.setTargetPosition(-10);
                        r.lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        r.lift2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        while(r.lift1.isBusy()) {
                            r.lift1.setPower(1);
                            r.lift2.setPower(1);
                            if (gamepad1.dpad_up && runtime.milliseconds() > 40)

                    if (gamepad1.dpad_down && runtime.milliseconds() > 40) {
                        double newPosition = r.servo1.getPosition() - 0.05;
                        newPosition = Range.clip(newPosition, 0, 1);
                        r.servo1.setPosition(newPosition);
                        r.servo2.setPosition(newPosition);
                        runtime.reset();
                        runtime.reset();
                    }
                }
            }

        r.servo3.setPosition(r.servo1.getPosition());

        r.lift1.setPower(gamepad1.left_stick_y);
        r.lift2.setPower(gamepad1.left_stick_y);

        telemetry.addData("servo1: ", r.servo1.getPosition());
        telemetry.addData("servo2: ", r.servo2.getPosition());
        telemetry.addData("lift1: ", r.lift1.getCurrentPosition());
        telemetry.addData("lift2: ", r.lift2.getCurrentPosition());
        telemetry.update();
        //

        }
    }
}

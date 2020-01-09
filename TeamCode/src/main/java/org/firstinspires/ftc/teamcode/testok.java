package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="testok", group="Hardware Test")
public class testok extends LinearOpMode {

    private DcMotor LTMotor;
    private DcMotor LBMotor;
    private DcMotor RTMotor;
    private DcMotor RBMotor;

    private DcMotor intake1;
    private DcMotor intake2;

    private DcMotor lift1;

    boolean setOnce;
    double stopPos;
    double target;

    int level = 0;


    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Hardware r = new Hardware(this);

        LTMotor = hardwareMap.dcMotor.get("LTMotor");
        LBMotor = hardwareMap.dcMotor.get("LBMotor");
        RTMotor = hardwareMap.dcMotor.get("RTMotor");
        RBMotor = hardwareMap.dcMotor.get("RBMotor");

        intake1 = hardwareMap.dcMotor.get("intake1");
        intake2 = hardwareMap.dcMotor.get("intake2");

        lift1 = hardwareMap.dcMotor.get("lift1");

        RTMotor.setDirection(DcMotor.Direction.REVERSE);
        RBMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        runtime.reset();
        target = r.ArmAngle.getVoltage();


        while (opModeIsActive()) {
/*
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
*/
        /*
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

        //r.servo3.setPosition(r.servo1.getPosition());
*/

            // r.servo1.setPosition(gamepad2.left_stick_y * 0.5 + 0.5);
            // r.servo2.setPosition(gamepad2.left_stick_y  * 0.5 + 0.5);

            r.servo1.setPower(gamepad2.left_stick_y);
            r.servo2.setPower(gamepad2.left_stick_y);

/*
            if (runtime.milliseconds() > 10) {
                target = target + (gamepad2.left_stick_y * 0.01);
                target = Range.clip(target, 0.004, 2.448);
                runtime.reset();
            }
                    //2.448, 0.004
                    //
                double error = target - r.ArmAngle.getVoltage();
                double P = error * 0.5;
                P = Range.clip(P, -0.4, 0.4);
            telemetry.addData("target: ", target);
            telemetry.addData("error: ", error);
                telemetry.addData("power: ", P);

                r.servo1.setPower(P);
                r.servo2.setPower(P);
*/

//0.75, 2.112
            //0, 0.045
            //y  = ((0.75 - 0)/(2.112 - 0.045) * (x - 0.045)

          //   lift1.setPower(gamepad2.right_trigger - gamepad2.left_trigger);

            if (gamepad2.dpad_down && runtime.milliseconds() > 10) {
                double newPosition = r.servo3.getPosition() - 0.01;
                newPosition = Range.clip(newPosition, 0, 1);
                r.servo3.setPosition(newPosition);
                runtime.reset();
            }

            if (gamepad2.dpad_up && runtime.milliseconds() > 10) {
                double newPosition = r.servo3.getPosition() + 0.01;
                newPosition = Range.clip(newPosition, 0, 1);
                r.servo3.setPosition(newPosition);
                runtime.reset();
            }

            r.servo3.setPosition(0.289360 * r.ArmAngle.getVoltage() - 0.188);
            // r.servo3.setPosition(((0.75 - 0)/(2.112 - 0.045) * (r.ArmAngle.getVoltage() - 0.045)));

            //(2.157,

            if (gamepad2.a) {
                r.servo4.setPosition(1.0);
            } else if (gamepad2.b) {
                r.servo4.setPosition(0.0);
            }

            if (gamepad2.x) {
                r.servo5.setPosition(1.0);
                r.servo6.setPosition(0.0);
            } else if (gamepad2.y) {
                r.servo5.setPosition(0.3);
                r.servo6.setPosition(0.7);
            }

            //  y\ =\ 0.287360\cdot x\ -0.188
            //   y = 3.4799 * x + 0.566

            LTMotor.setPower((gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x) * 1);
            LBMotor.setPower((gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x) * 1);
            RTMotor.setPower((gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x) * 1);
            RBMotor.setPower((gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x) * 1);

            intake1.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
            intake2.setPower(gamepad1.right_trigger - gamepad1.left_trigger);


            if (gamepad2.x && runtime.milliseconds() > 120) {
                if (level < 10) {
                    level++;
                }
                lift1.setTargetPosition(-200 * level);
                lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift1.setPower(1);
                runtime.reset();
            } else if (gamepad2.y && runtime.milliseconds() > 120) {
                if (level > 0) {
                    level--;
                }
                lift1.setTargetPosition(-200 * level);
                lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift1.setPower(1);
                runtime.reset();
            }


            if (gamepad2.dpad_up && runtime.milliseconds() > 40) {
                lift1.setTargetPosition(lift1.getCurrentPosition() - 40);
                lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift1.setPower(1);
                runtime.reset();
            } else if (gamepad2.dpad_down && runtime.milliseconds() > 40) {
                lift1.setTargetPosition(lift1.getCurrentPosition() + 40);
                lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift1.setPower(1);
                runtime.reset();
            }


            telemetry.addData("Potem", r.ArmAngle.getVoltage());
            //   telemetry.addData("servo1: ", r.servo1.getPower());
            //   telemetry.addData("servo2: ", r.servo2.getPower());
            telemetry.addData("servo3: ", r.servo3.getPosition());
            telemetry.addData("servo4: ", r.servo4.getPosition());
            //  telemetry.addData("lift1: ", r.lift1.getCurrentPosition());
            //  telemetry.addData("lift2: ", r.lift2.getCurrentPosition());
            telemetry.addData("level: ", level);
            telemetry.addData("Lift Pos: ", lift1.getCurrentPosition());
            telemetry.addData("Lift Target: ", lift1.getTargetPosition());
            telemetry.update();

        }
    }
}
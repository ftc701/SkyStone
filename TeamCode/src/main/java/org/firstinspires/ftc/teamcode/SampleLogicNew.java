package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

@TeleOp(name="visionTest", group="Hardware Test")
public class SampleLogicNew extends LinearOpMode
{
    OpenCvCamera phoneCam;


    int massL = 0;
    int massC = 0;
    int massR = 0;
    boolean frameNeeded = false;
    String TAG = "Stuff";
    
    //JavaCameraView view = getCameraView();
    /*
    int RectX = 160;
    int RectY = 640;
    int RectW = 240;
    int RectH = 213;
    */
    int RectP1X = 100;
    int RectP1Y = 10;
    int RectP2X = 200;
    int RectP2Y = 200;

    int massOfRectangleFrame = 2814135;
    int massOfTextFrame = 2085;

    @Override
    public void runOpMode()
    {
        /*
         * Instantiate an OpenCvCamera object for the camera we'll be using.
         * In this sample, we're using the phone's internal camera. We pass it a
         * CameraDirection enum indicating whether to use the front or back facing
         * camera, as well as the view that we wish to use for camera monitor (on
         * the RC phone). If no camera monitor is desired, use the alternate
         * single-parameter constructor instead (commented out below)
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View
        //phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK);

        /*
         * Open the connection to the camera device
         */
        phoneCam.openCameraDevice();

        /*
         * Specify the image processing pipeline we wish to invoke upon receipt
         * of a frame from the camera. Note that switching pipelines on-the-fly
         * (while a streaming session is in flight) *IS* supported.
         */
        phoneCam.setPipeline(new SamplePipeline());

        /*
         * Tell the camera to start streaming images to us! Note that you must make sure
         * the resolution you specify is supported by the camera. If it is not, an exception
         * will be thrown.
         *
         * Also, we specify the rotation that the camera is used in. This is so that the image
         * from the camera sensor can be rotated such that it is always displayed with the image upright.
         * For a front facing camera, rotation is defined assuming the user is looking at the screen.
         * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
         * away from the user.
         */
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        /*
         * Wait for the user to press start on the Driver Station
         */
        waitForStart();

        while (opModeIsActive())
        {
            /*
             * Send some stats to the telemetry
             */
            telemetry.addData("Frame Count", phoneCam.getFrameCount());
            telemetry.addData("FPS", String.format("%.2f", phoneCam.getFps()));
            telemetry.addData("Total frame time ms", phoneCam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", phoneCam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", phoneCam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", phoneCam.getCurrentPipelineMaxFps());
            telemetry.update();

            /*
             * NOTE: stopping the stream from the camera early (before the end of the OpMode
             * when it will be automatically stopped for you) *IS* supported. The "if" statement
             * below will stop streaming from the camera when the "A" button on gamepad 1 is pressed.
             */
            if(gamepad1.a)
            {
                /*
                 * IMPORTANT NOTE: calling stopStreaming() will indeed stop the stream of images
                 * from the camera (and, by extension, stop calling your vision pipeline). HOWEVER,
                 * if the reason you wish to stop the stream early is to switch use of the camera
                 * over to, say, Vuforia or TFOD, you will also need to call closeCameraDevice()
                 * (commented out below), because according to the Android Camera API documentation:
                 *         "Your application should only have one Camera object active at a time for
                 *          a particular hardware camera."
                 *
                 * NB: calling closeCameraDevice() will internally call stopStreaming() if applicable,
                 * but it doesn't hurt to call it anyway, if for no other reason than clarity.
                 *
                 * NB2: if you are stopping the camera stream to simply save some processing power
                 * (or battery power) for a short while when you do not need your vision pipeline,
                 * it is recommended to NOT call closeCameraDevice() as you will then need to re-open
                 * it the next time you wish to activate your vision pipeline, which can take a bit of
                 * time. Of course, this comment is irrelevant in light of the use case described in
                 * the above "important note".
                 */
                phoneCam.stopStreaming();
                //webcam.closeCameraDevice();
            }

            /*
             * The viewport (if one was specified in the constructor) can also be dynamically "paused"
             * and "resumed". The primary use case of this is to reduce CPU, memory, and power load
             * when you need your vision pipeline running, but do not require a live preview on the
             * robot controller screen. For instance, this could be useful if you wish to see the live
             * camera preview as you are initializing your robot, but you no longer require the live
             * preview after you have finished your initialization process; pausing the viewport does
             * not stop running your pipeline.
             *
             * The "if" statements below will pause the viewport if the "X" button on gamepad1 is pressed,
             * and resume the viewport if the "Y" button on gamepad1 is pressed.
             */
            else if(gamepad1.x)
            {
                phoneCam.pauseViewport();
            }
            else if(gamepad1.y)
            {
                phoneCam.resumeViewport();
            }

            /*
             * For the purposes of this sample, throttle ourselves to 10Hz loop to avoid burning
             * excess CPU cycles for no reason. (By default, telemetry is only sent to the DS at 4Hz
             * anyway). Of course in a real OpMode you will likely not want to do this.
             */
            sleep(100);
        }
    }

    /*
     * An example image processing pipeline to be run upon receipt of each frame from the camera.
     * Note that the processFrame() method is called serially from the frame worker thread -
     * that is, a new camera frame will not come in while you're still processing a previous one.
     * In other words, the processFrame() method will never be called multiple times simultaneously.
     *
     * However, the rendering of your processed image to the viewport is done in parallel to the
     * frame worker thread. That is, the amount of time it takes to render the image to the
     * viewport does NOT impact the amount of frames per second that your pipeline can process.
     *
     * IMPORTANT NOTE: this pipeline is NOT invoked on your OpMode thread. It is invoked on the
     * frame worker thread. This should not be a problem in the vast majority of cases. However,
     * if you're doing something weird where you do need it synchronized with your OpMode thread,
     * then you will need to account for that accordingly.
     */
    class SamplePipeline extends OpenCvPipeline
    {
        /*
         * NOTE: if you wish to use additional Mat objects in your processing pipeline, it is
         * highly recommended to declare them here as instance variables and re-use them for
         * each invocation of processFrame(), rather than declaring them as new local variables
         * each time through processFrame(). This removes the danger of causing a memory leak
         * by forgetting to call mat.release(), and it also reduces memory pressure by not
         * constantly allocating and freeing large chunks of memory.
         */

        @Override
        public Mat processFrame(Mat input)
        {

            Mat hsvThresholdInput = new Mat();
            Mat cvErodeSrc = new Mat();
            Mat cvDilateSrc = new Mat();
//2814135
            hsvThresholdInput = input;
            double[] hsvThresholdHue = {0.0, 45.0};
            double[] hsvThresholdSaturation = {173.0, 245.0};
            double[] hsvThresholdValue = {170.0, 253.0};
            hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdInput);

            // Step CV_erode0:
            cvErodeSrc = hsvThresholdInput;
            Mat cvErodeKernel = new Mat();
            Point cvErodeAnchor = new Point(-1, -1);
            double cvErodeIterations = 0;
            int cvErodeBordertype = Core.BORDER_DEFAULT;
            Scalar cvErodeBordervalue = new Scalar(-1);
            cvErode(cvErodeSrc, cvErodeKernel, cvErodeAnchor, cvErodeIterations, cvErodeBordertype, cvErodeBordervalue, cvErodeSrc);

            // Step CV_dilate0:
            cvDilateSrc = cvErodeSrc;
            Mat cvDilateKernel = new Mat();
            Point cvDilateAnchor = new Point(-1, -1);
            double cvDilateIterations = 7.0;
            int cvDilateBordertype = Core.BORDER_DEFAULT;
            Scalar cvDilateBordervalue = new Scalar(-1);
            cvDilate(cvDilateSrc, cvDilateKernel, cvDilateAnchor, cvDilateIterations, cvDilateBordertype, cvDilateBordervalue, cvDilateSrc);

            hsvThresholdInput.release();
            cvErodeSrc.release();
            cvErodeKernel.release();
            cvDilateKernel.release();
            cvDilateSrc.release();
            return cvDilateSrc; // display the image seen by the camera

            /*
             * IMPORTANT NOTE: the input Mat that is passed in as a parameter to this method
             * will only dereference to the same image for the duration of this particular
             * invocation of this method. That is, if for some reason you'd like to save a copy
             * of this particular frame for later use, you will need to either clone it or copy
             * it to another Mat.
             */

            /*
             * Draw a simple box around the middle 1/2 of the entire frame
             *//*
            Imgproc.rectangle(
                    input,
                    new Point(
                            input.cols()/4,
                            input.rows()/4),
                    new Point(
                            input.cols()*(3f/4f),
                            input.rows()*(3f/4f)),
                    new Scalar(0, 255, 0), 4);
            */



            /**
             * NOTE: to see how to get data from your pipeline to your OpMode as well as how
             * to change which stage of the pipeline is rendered to the viewport when it is
             * tapped, please see {@link PipelineStageSwitchingExample}
             */

        }
    }
    /*
    public void colSumMassLocation(Mat frameIntial, Mat input, Mat output) {


        // frameNeeded = false;
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();


    }
    */

    public int returnTotalMass(){
        return massL;
        //- massOfRectangleFrame - massOfTextFrame;
    }

    public Mat colSumOutput() {
        return colSumOutput;
    }

    public MatOfKeyPoint returnBlobList(){
        return blobList;
    }

    public KeyPoint returnLargestBlob(){
        return largestBlob;
    }

    /**
     * Segment an image based on hue, saturation, and value ranges.
     *
     * @param input The image on which to perform the HSL threshold.
     * @param hue The min and max hue
     * @param sat The min and max saturation
     * @param val The min and max value
     * @param out The image in which to store the output.
     */
    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]), new Scalar(hue[1], sat[1], val[1]), out);
    }

    /**
     * Expands area of lower value in an image.
     * @param src the Image to erode.
     * @param kernel the kernel for erosion.
     * @param anchor the center of the kernel.
     * @param iterations the number of times to perform the erosion.
     * @param borderType pixel extrapolation method.
     * @param borderValue value to be used for a constant border.
     * @param dst Output Image.
     */
    private void cvErode(Mat src, Mat kernel, Point anchor, double iterations,
                         int borderType, Scalar borderValue, Mat dst) {
        if (kernel == null) {
            kernel = new Mat();
        }
        if (anchor == null) {
            anchor = new Point(-1,-1);
        }
        if (borderValue == null) {
            borderValue = new Scalar(-1);
        }
        Imgproc.erode(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
    }

    /**
     * Expands area of higher value in an image.
     * @param src the Image to dilate.
     * @param kernel the kernel for dilation.
     * @param anchor the center of the kernel.
     * @param iterations the number of times to perform the dilation.
     * @param borderType pixel extrapolation method.
     * @param borderValue value to be used for a constant border.
     * @param dst Output Image.
     */
    private void cvDilate(Mat src, Mat kernel, Point anchor, double iterations,
                          int borderType, Scalar borderValue, Mat dst) {
        if (kernel == null) {
            kernel = new Mat();
        }
        if (anchor == null) {
            anchor = new Point(-1,-1);
        }
        if (borderValue == null){
            borderValue = new Scalar(-1);
        }
        Imgproc.dilate(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
    }

    public void changeRectP1X(int size) {
        RectP1X = size;
    }
    public void changeRectP1Y(int size) {
        RectP1Y = size;
    }
    public void changeRectP2X(int size) {
        RectP2X = size;
    }
    public void changeRectP2Y(int size) {
        RectP2Y = size;
    }

    public int getRectP1X() {
        return RectP1X;
    }
    public int getRectP1Y() {
        return RectP1Y;
    }
    public int getRectP2X() {
        return RectP2X;
    }
    public int getRectP2Y() {
        return RectP2Y;
    }

    public void changeRect(int RecP1X, int RecP1Y, int RecP2X, int RecP2Y) {
        changeRectP1X(RecP1X);
        changeRectP1Y(RecP1Y);
        changeRectP2X(RecP2X);
        changeRectP2Y(RecP2Y);
    }
/*
    Runnable scheduler = new Runnable() {

        @Override
        public void run() {

            massL = 0;
            Rect rectCrop = new Rect(new Point(RectP1X + 2, RectP1Y + 2),
                    new Point(RectP2X - 2, RectP2Y - 2));
            //RectX,RectY,RectW,RectH
            //new Point(RectX,  RectH), new Point(RectW, RectY)
            //new Point(0,  213), new Point(480, 427)
            final Mat in = cvDilateSrc.submat(rectCrop);

            massL = 0;
            massC = 0;
            massR = 0;
            Core.reduce(in, colSumOutput, 0, Core.REDUCE_SUM, 4);
            for (int x = 0; x <= frameFinal.cols(); x++) {
                int[] data = new int[3];
                colSumOutput.get(0, x, data);
                massL += data[0];
            }

            in.release();
            colSumOutput.release();
            frameNeeded = false;

        }
    };
    */

}


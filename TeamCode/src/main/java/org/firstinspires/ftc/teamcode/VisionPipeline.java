package org.firstinspires.ftc.teamcode;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

class VisionPipeline extends OpenCvPipeline {

    int massL;
    int massC;
    int massR;

    static int width = 320;
    static int height = 240;

    /*
     * NOTE: if you wish to use additional Mat objects in your processing pipeline, it is
     * highly recommended to declare them here as instance variables and re-use them for
     * each invocation of processFrame(), rather than declaring them as new local variables
     * each time through processFrame(). This removes the danger of causing a memory leak
     * by forgetting to call mat.release(), and it also reduces memory pressure by not
     * constantly allocating and freeing large chunks of memory.
     */
    Mat hsvThresholdInput = new Mat();
    Mat cvErodeSrc = new Mat();
    Mat cvDilateSrc = new Mat();
    Mat cvErodeKernel = new Mat();
    Mat cvDilateKernel = new Mat();
    Mat display = new Mat();

    Mat maskL = new Mat();
    Mat maskC = new Mat();
    Mat maskR = new Mat();

    Mat cropL = new Mat();
    Mat cropC = new Mat();
    Mat cropR = new Mat();

    Rect rectL = new Rect(new Point(0, 0), new Point(height/2, width*(1f/3f)));
    Rect rectC = new Rect(new Point(0, width * (1f/3f)), new Point(height/2, width * (2f/3f)));
    Rect rectR = new Rect(new Point(0, width * (2f/3f)), new Point(height/2, width));

    @Override
    public Mat processFrame(Mat input)
    {
        /*
         * IMPORTANT NOTE: the input Mat that is passed in as a parameter to this method
         * will only dereference to the same image for the duration of this particular
         * invocation of this method. That is, if for some reason you'd like to save a copy
         * of this particular frame for later use, you will need to either clone it or copy
         * it to another Mat.
         */
        hsvThresholdInput = input;
        double[] hsvThresholdHue = {0.0, 45.0};
        double[] hsvThresholdSaturation = {173.0, 245.0};
        double[] hsvThresholdValue = {170.0, 253.0};
        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdInput);

        // Step CV_erode0:
        cvErodeSrc = hsvThresholdInput;
        Point cvErodeAnchor = new Point(-1, -1);
        double cvErodeIterations = 0;
        int cvErodeBordertype = Core.BORDER_DEFAULT;
        Scalar cvErodeBordervalue = new Scalar(-1);
        cvErode(cvErodeSrc, cvErodeKernel, cvErodeAnchor, cvErodeIterations, cvErodeBordertype, cvErodeBordervalue, cvErodeSrc);

        // Step CV_dilate0:
        cvDilateSrc = cvErodeSrc;
        Point cvDilateAnchor = new Point(-1, -1);
        double cvDilateIterations = 7.0;
        int cvDilateBordertype = Core.BORDER_DEFAULT;
        Scalar cvDilateBordervalue = new Scalar(-1);
        cvDilate(cvDilateSrc, cvDilateKernel, cvDilateAnchor, cvDilateIterations, cvDilateBordertype, cvDilateBordervalue, cvDilateSrc);
        display = cvDilateSrc;

        //For the Sake of Debugging
        Imgproc.rectangle(
                display, rectL,
                new Scalar(255, 255, 255), 1);

        Imgproc.rectangle(
                display, rectC,
                new Scalar(255, 255, 255), 1);

        Imgproc.rectangle(
                display, rectR,
                new Scalar(255, 255, 255), 1);


        //Coord 1 = (279, 0)
        //Coord 2 = (12800, 100)

        double[] coord1 = {997, 0};
        double[] coord2 = {12495, 100};

        massL = (int) mapFunction(Core.countNonZero(cvDilateSrc.submat(rectL)), coord1, coord2);
        massC = (int) mapFunction(Core.countNonZero(cvDilateSrc.submat(rectC)), coord1, coord2);
        massR = (int) mapFunction(Core.countNonZero(cvDilateSrc.submat(rectR)), coord1, coord2);


        /**
         * NOTE: to see how to get data from your pipeline to your OpMode as well as how
         * to change which stage of the pipeline is rendered to the viewport when it is
         * tapped, please see {@link PipelineStageSwitchingExample}
         */

        return display;
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

    public int getMassL(){
        return massL;
    }

    public int getMassC(){
        return massC;
    }

    public int getMassR(){
        return massR;
    }
}
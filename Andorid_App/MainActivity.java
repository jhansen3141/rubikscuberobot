// Josh Hansen
// Ball Finder Application
// April 2016

package com.example.josh.rremote;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private JavaCameraView mOpenCvCameraView0;
    private int numCameras = 0;
    private TcpClient CB;
    private String TAG = "TCP";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ConnectTask().execute("");
        //Log.i(TAG,"CONNECTED");


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mOpenCvCameraView0 = (JavaCameraView) findViewById(R.id.java_surface_view0);
        mOpenCvCameraView0.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView0.setCvCameraViewListener(this);
        mOpenCvCameraView0.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mOpenCvCameraView0.enableView();

    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {
            //we create a TCPClient object and
            CB = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    //publishProgress(message);
                    Log.i("Debug","Input message: " + message);
                }
            });
            CB.run();

            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // CB.sendMessage("DS Testing \r");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView0 != null) {
            mOpenCvCameraView0.disableView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView0 != null) {
            mOpenCvCameraView0.disableView();
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {
        //releaseMats();
    }

    public void releaseMats () {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {

        Mat hsvImage = new Mat();
        Mat mask = new Mat();
        Mat output = new Mat();
        Mat morphOutput = new Mat();
        boolean foundObject = false;
        boolean reachedTarget = false;
        double objectPos = 0;
        int turnAmount = 0;

        inputFrame.copyTo(output);
        Imgproc.cvtColor(inputFrame,hsvImage, Imgproc.COLOR_RGB2HSV, 3);

        Core.inRange(hsvImage, new Scalar(30,100,100), new Scalar(50,200,255), mask);

        hsvImage.release();

        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)); // 24 24
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));

        Imgproc.erode(mask, morphOutput, erodeElement);
        Imgproc.erode(mask, morphOutput, erodeElement);
        Imgproc.dilate(mask, morphOutput, dilateElement);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(morphOutput, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        for(int i=0;i<contours.size();i++) {
            if((Imgproc.contourArea(contours.get(i)) > 3000)) {
                if(Imgproc.contourArea(contours.get(i)) > 200000) {
                    reachedTarget = true;
                   // Log.i(TAG,"Found");
                    Log.i(TAG,Double.toString(Imgproc.contourArea(contours.get(i))));
                    break;
                }
                //Log.i(TAG,Double.toString(Imgproc.contourArea(contours.get(i))));
                Rect rect = Imgproc.boundingRect(contours.get(i));
                objectPos = rect.x;
                Imgproc.rectangle(output,rect.tl(),rect.br(),new Scalar(255,0,0),3);
                foundObject = true;

                //Log.i(TAG, Integer.toString(rect.x) + "  " + Integer.toString(rect.y));
                break;
            }
        }
        if(!reachedTarget) {
            if (!foundObject) {
                CB.sendMessage("RM 80 \r");
                CB.sendMessage("LM -80\r");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CB.sendMessage("RM 0\r");
                CB.sendMessage("LM 0\r");
            }
            else {
                if(objectPos >= 500 && objectPos <= 700) {
                    CB.sendMessage("RM 60 \r");
                    CB.sendMessage("LM 60\r");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if(objectPos < 500) {
                    CB.sendMessage("RM -50 \r");
                    CB.sendMessage("LM 50 \r");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CB.sendMessage("RM 0 \r");
                    CB.sendMessage("LM 0 \r");
                }
                else if(objectPos > 700) {
                    CB.sendMessage("RM 50 \r");
                    CB.sendMessage("LM -50 \r");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CB.sendMessage("RM 0 \r");
                    CB.sendMessage("LM 0 \r");
                }


            }
        }
        else {
            CB.sendMessage("DS Found \r");
            CB.sendMessage("RM 0 \r");
            CB.sendMessage("LM 0\r");
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return output;
    }
}

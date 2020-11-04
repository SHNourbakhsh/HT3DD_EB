
package com.ht3dd_eb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.lang.Object;

//import org.opencv.android.Utils;
//import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
//import org.opencv.highgui.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs; // imread, imwrite, etc
import org.opencv.videoio.VideoCapture;   // VideoCapture
import org.opencv.objdetect.CascadeClassifier;

import android.content.Context;
import android.hardware.Camera;
//import android.graphics.Bitmap;
import android.util.Log;
//import android.view.SurfaceHolder;

class FdCvBase extends Thread {
    private static final String TAG = "Face detection: ";
    private Mat                 mRgba;
    private Mat                 mGray;
    private CascadeClassifier   mCascade;
    private VideoCapture        mCamera;
    private int 				mNrCameras = -1;
    //private static int 		count = 0;

    public FdCvBase(Context context) {
        Log.i(TAG, "CvBase started");
        mNrCameras = Camera.getNumberOfCameras();
        if (mNrCameras == 0)
        	Log.i(TAG, "Camrea is not available on this device");
        else if (mNrCameras == 1)
        {
        	Log.i(TAG, "Will use rear camrea (away from face)");
        	mCamera = new VideoCapture( 0);  // 0 for rear camera - Highgui.CV_CAP_ANDROID + 0
       	}
        else if (mNrCameras == 2)
        {
        	Log.i(TAG, "Will use front camrea (face direction)");
         	mCamera = new VideoCapture(1);  // 1 for front (face) camera - Highgui.CV_CAP_ANDROID + 1
        }
        else
        	Log.e(TAG, "Too many camreas, do not know how to handle the camera on this device");
   
        
        if (mCamera.isOpened()) {
        	Log.i(TAG, "Camera started");
            (new Thread(this)).start();
        } else {
            mCamera.release();
            mCamera = null;
            Log.e(TAG, "Failed to open native camera");
        }
        
        if (mCamera != null && mCamera.isOpened()) {
            Log.i(TAG, "before mCamera.getSupportedPreviewSizes()");
            //List<Size> sizes = mCamera.getSupportedPreviewSizes();
            Log.i(TAG, "after mCamera.getSupportedPreviewSizes()");
            int mFrameWidth = HT3DD_Activity.mScrSizeX;
            int mFrameHeight = HT3DD_Activity.mScrSizeY;
            int height = 480;
           
            Log.i(TAG, "mScrSizeX; " + mFrameWidth +  "; mScrSizeY; " + mFrameHeight );
            //Log.i(TAG, "mCamera.getSupportedPreviewSizes(); " + mCamera.getSupportedPreviewSizes() );

            // selecting optimal camera preview size
//            {
//                double minDiff = Double.MAX_VALUE;
//                for (Size size : sizes) {
//                    if (Math.abs(size.height - height) < minDiff) {
//                        mFrameWidth = (int) size.width;
//                        mFrameHeight = (int) size.height;
//                        minDiff = Math.abs(size.height - height);
//                    }
//                }
//            }
            
            
            // possible values:
            // 176x144, 320x240, 352x288, 640x480, 720x576, 768x432, 1280x720
//            Log.i(TAG, "Will set Camera Property to, Width; " + mFrameWidth +  "; and Height; " + mFrameHeight );
            //mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 640 );
            //mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 480 );
//            if (! mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, mFrameWidth))
//            	Log.i(TAG, "could not set Camera Property to, Width; ");
//            if (! mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, mFrameHeight))
//            	Log.i(TAG, "could not set Camera Property to, Width; ");
            
        }
        
        try {
            //InputStream is = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
            InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_default);
            
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            
            //File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            File cascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");
			
            FileOutputStream os = new FileOutputStream(cascadeFile);
            

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            mCascade = new CascadeClassifier(cascadeFile.getAbsolutePath());
            if (mCascade.empty()) {
                Log.e(TAG, "Failed to load cascade classifier");
                mCascade = null;
            } else
                Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());

            cascadeFile.delete();
            cascadeDir.delete();
            
            mGray = new Mat();
            mRgba = new Mat();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
        } // try - catch
    } //constructor
    
    public void releaseHT(){
    
    	if (mCamera != null)
        	mCamera.release();
         mCamera = null;
         this.stop();
    }

    @Override
    public void run() {
    	
        Log.i(TAG, "Starting processing thread");
        //mFps.init();
 
        //Infinite Loop to capture an image form Camera and display it.
        while (true) 
        {

            synchronized (this) 
            {
               /*  */
                if (mCamera == null) 
                {
                    Log.e(TAG, "mCamera.grab() is missing");
                    break;
                }

                if (!mCamera.grab()) 
                {
                    Log.e(TAG, "mCamera.grab() failed");
                    break;
                }
            } // synchronized
            
            if (!HT3DD_Activity.mRendererReady)						// wait until the openGL is up and running
            	continue;
            
            mCamera.retrieve(mRgba);  //Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA
            mCamera.retrieve(mGray); //Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA
                        
            if (mCascade != null) {
                int height = mGray.rows();
                int faceSize = Math.round(height * HT3DD_Activity.minFaceSize);
                //List<Rect> faces = new LinkedList<Rect>();
                MatOfRect faces = new MatOfRect();
                mCascade.detectMultiScale(mGray, faces, 1.1, 2, 2, new Size(faceSize, faceSize));
                Rect[] rects = faces.toArray();
               
                //Draw a rectangle on the detected face
                for (int i = 0; i < rects.length; i++)
                {
                	if ( (rects[i].x != 0) || (rects[i].y != 0 ) || (rects[i].width != 0)) {
                		GlobalVar.x_g = rects[i].x + (rects[i].x + rects[i].width)/2 - 10;
                		GlobalVar.y_g = rects[i].y + (rects[i].y + rects[i].height)/2 - 62;
                		GlobalVar.z_g = rects[i].width - 80;
                		GlobalVar.new_flag = !GlobalVar.new_flag;
                		//Log.i(TAG, "y, y, z = " + r.x + ", " + r.y + ", " + r.width);
                	}
                }        
            } //if
             
        } //while

        Log.i(TAG, "Finishing processing thread");
        
        synchronized (this) {
            // Explicitly deallocate Mats
            if (mRgba != null)
                mRgba.release();
            if (mGray != null)
                mGray.release();

            mRgba = null;
            mGray = null;
        } // synchronized
    } //run()
} // FdCvBase extends Thread

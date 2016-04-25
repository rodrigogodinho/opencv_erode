package br.com.emplacavel.applications.testeopencv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2{
	private static final String TAG = "MYAPP::OPENCV";
	private CameraBridgeViewBase mOpenCvCameraView;

	BaseLoaderCallback mCallBack = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status){
				case BaseLoaderCallback.SUCCESS:
				{
					Log.i(TAG, "OpenCV loaded successfully");
					mOpenCvCameraView.enableView();
					break;
				}
				default:
				{
					super.onManagerConnected(status);
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_main);

		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.openCVView);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0,this,mCallBack);
	}

	/**
	 * This method is invoked when camera preview has started. After this method is invoked
	 * the frames will start to be delivered to client via the onCameraFrame() callback.
	 *
	 * @param width  -  the width of the frames that will be delivered
	 * @param height - the height of the frames that will be delivered
	 */
	@Override
	public void onCameraViewStarted(int width, int height) {

	}

	/**
	 * This method is invoked when camera preview has been stopped for some reason.
	 * No frames will be delivered via onCameraFrame() callback after this method is called.
	 */
	@Override
	public void onCameraViewStopped() {

	}

	/**
	 * This method is invoked when delivery of the frame needs to be done.
	 * The returned values - is a modified frame which needs to be displayed on the screen.
	 * TODO: pass the parameters specifying the format of the frame (BPP, YUV or RGB and etc)
	 *
	 * @param inputFrame
	 */
	@Override
	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
		Mat src = inputFrame.rgba();
		Mat cannyEdges = new Mat();
		Point point = new Point(1,1);

		//Imgproc.Canny(src, cannyEdges,10, 100);
		Imgproc.erode(src, cannyEdges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)), point, 20);

		return cannyEdges;
	}

}

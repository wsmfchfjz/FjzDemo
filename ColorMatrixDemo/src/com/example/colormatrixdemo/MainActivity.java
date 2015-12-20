package com.example.colormatrixdemo;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private Bitmap srcBitmap;

	private ImageView dstimage = null;
	private SeekBar SaturationseekBar = null;
	private SeekBar BrightnessseekBar = null;
	private SeekBar ContrastseekBar = null;
	private int imgHeight, imgWidth;

	public static final int PICTURE = 0;
	public static final int MAX_WIDTH = 240;
	public static final int MAX_HEIGHT = 240;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SaturationseekBar = (SeekBar) findViewById(R.id.Saturationseekbar);
		BrightnessseekBar = (SeekBar) findViewById(R.id.Brightnessseekbar);
		ContrastseekBar = (SeekBar) findViewById(R.id.Contrastseekbar);

		srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
		dstimage = (ImageView) findViewById(R.id.dstImageView);
		dstimage.setImageBitmap(srcBitmap);
		
		imgHeight = srcBitmap.getHeight();
		imgWidth = srcBitmap.getWidth();

		SaturationseekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			// ���϶����Ļ���λ�÷����ı�ʱ�����÷���
			public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
				Log.i("test", ""+progress);
				// ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
				Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight, Config.ARGB_8888);
				ColorMatrix cMatrix = new ColorMatrix();
				// ���ñ��Ͷ�
				cMatrix.setSaturation((float) (progress / 100.0));

				Paint paint = new Paint();
				paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

				Canvas canvas = new Canvas(bmp);
				// ��Canvas�ϻ���һ���Ѿ����ڵ�Bitmap��������dstBitmap�ͺ�srcBitmapһ��һ����
				canvas.drawBitmap(srcBitmap, 0, 0, paint);

				dstimage.setImageBitmap(bmp);

			}

			public void onStartTrackingTouch(SeekBar bar) {
			}

			public void onStopTrackingTouch(SeekBar bar) {
			}
		});

		BrightnessseekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			// ���϶����Ļ���λ�÷����ı�ʱ�����÷���
			public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
				Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight, Config.ARGB_8888);
				int brightness = progress - 127;
				ColorMatrix cMatrix = new ColorMatrix();
				cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, // �ı�����
						0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

				Paint paint = new Paint();
				paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

				Canvas canvas = new Canvas(bmp);
				// ��Canvas�ϻ���һ���Ѿ����ڵ�Bitmap��������dstBitmap�ͺ�srcBitmapһ��һ����
				canvas.drawBitmap(srcBitmap, 0, 0, paint);
				dstimage.setImageBitmap(bmp);

			}

			public void onStartTrackingTouch(SeekBar bar) {
			}

			public void onStopTrackingTouch(SeekBar bar) {
			}
		});

		ContrastseekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			// ���϶����Ļ���λ�÷����ı�ʱ�����÷���
			public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
				Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight, Config.ARGB_8888);
				// int brightness = progress - 127;
				float contrast = (float) ((progress + 64) / 128.0);
				ColorMatrix cMatrix = new ColorMatrix();
				cMatrix.set(new float[] { contrast, 0, 0, 0, 0, 0, contrast, 0, 0, 0, // �ı�Աȶ�
						0, 0, contrast, 0, 0, 0, 0, 0, 1, 0 });

				Paint paint = new Paint();
				paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

				Canvas canvas = new Canvas(bmp);
				// ��Canvas�ϻ���һ���Ѿ����ڵ�Bitmap��������dstBitmap�ͺ�srcBitmapһ��һ����
				canvas.drawBitmap(srcBitmap, 0, 0, paint);

				dstimage.setImageBitmap(bmp);
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * ��Ҫ���ص�ͼƬ�����Ǵ�ͼ��������Ҫ������к��ʵ���С����
	 * 
	 * @param imageUri
	 */
	private Bitmap getSrcImage(Uri imageUri) {
		try {
			BitmapFactory.Options ops = new BitmapFactory.Options();
			ops.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri), null, ops);
			int wRatio = (int) Math.ceil(ops.outWidth / (float) MAX_WIDTH);
			int hRatio = (int) Math.ceil(ops.outHeight / (float) MAX_HEIGHT);

			if (wRatio > 1 && hRatio > 1) {
				if (wRatio > hRatio) {
					ops.inSampleSize = wRatio;
				} else {
					ops.inSampleSize = hRatio;
				}
			}

			ops.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri), null, ops);

			return bmp;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(this.getClass().getName(), e.getMessage());
		}

		return null;
	}
}

package com.loveplusplus.demo.fileupload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String mCurrentPhotoPath;// 图片路径
	private String mCurrentVideoPath;// 视频路径
	private static final int REQUEST_TAKE_PHOTO = 0;
	private static final int REQUEST_TAKE_VIDEO = 1;
	private static final String TAG = "MainActivity";
	public ProgressDialog progressDialog;
	private ImageView mImageView;
	private String path_sd = Environment.getExternalStorageDirectory().toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImageView = (ImageView) findViewById(R.id.imageView1);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {

				// 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
				PictureUtil.galleryAddPic(this, mCurrentPhotoPath);

				// 创建Options对象
				Options opts = new Options();
				// Bitmap不需要全部将原图片读取后再压缩.它可以只读取图片的边界信息属性直接压缩即可
				opts.inJustDecodeBounds = true;
				Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath, opts);
				// 获得原图片的长宽(需要先加载读取一次图片才能获得原图片的参数)
				int w = opts.outWidth;
				int h = opts.outHeight;
				// 设置压缩比,进行压缩
				// 在Option中，属性值inSampleSize表示缩略图大小为原始图片大小的几分之一，即如果这个值为2，则取出的缩略图的宽和高都是原始图片的1/2，图片大小就为原始大小的1/4。
				opts.inSampleSize = (w / 320) > (h / 240) ? (w / 320) : (h / 240);
				Log.e("testLog", "inSampleSize:" + opts.inSampleSize);
				// 按照Options属性来解析压缩后的图片,先重新把inJustDecodeBounds属性设置为false,再次加载一次
				opts.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(mCurrentPhotoPath, opts);

				mImageView.setImageBitmap(bm);

				String tempPath = path_sd + "/myAppFile/imgTemp.jpg";// 保存压缩后的图片的路径
				// 加载成bitmap，并压缩到200k以下
				try {
					ImageUtils.saveImage(tempPath, ImageUtils.compressImage2Bytes(bm));

					AsyncHttpClient as = new AsyncHttpClient();
					RequestParams params = new RequestParams();
					params.put("pic", new File(tempPath));
					as.post(getApplicationContext(), "上传服务器路径", null, params, "image/jpeg", new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						}
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				// 取消照相后，删除已经创建的临时文件。
				PictureUtil.deleteTempFile(mCurrentPhotoPath);
			}
		} else if (requestCode == REQUEST_TAKE_VIDEO) {
			if (resultCode == Activity.RESULT_OK) {

				Uri uri = intent.getData();
				mCurrentVideoPath = getRealPathFromURI(uri);

				Log.d(TAG, mCurrentVideoPath);
			}
		}

	}

	public int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
			return bitmap.getAllocationByteCount();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API
																			// 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_photo:
			takePhoto();
			return true;
		case R.id.menu_video:
			takeVideo();
			return true;
		case R.id.menu_upload_picture:
			// upload();

			save();
			return true;
		case R.id.menu_upload_video:
			uploadVideo();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void save() {

		if (mCurrentPhotoPath != null) {

			try {
				File f = new File(mCurrentPhotoPath);

				Bitmap bm = PictureUtil.getSmallBitmap(mCurrentPhotoPath);

				FileOutputStream fos = new FileOutputStream(new File(PictureUtil.getAlbumDir(), "small_" + f.getName()));

				bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);

				Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				Log.e(TAG, "error", e);
			}

		} else {
			Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
		}
	}

	private void uploadVideo() {
		if (mCurrentVideoPath != null) {
			FileUploadTask task = new FileUploadTask();
			task.execute(mCurrentVideoPath, "1");
		} else {
			Toast.makeText(this, "请先点击录像按钮拍摄视频", Toast.LENGTH_SHORT).show();
		}
	}

	private void takeVideo() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
	}

	/**
	 * 拍照
	 */
	private void takePhoto() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// 指定存放拍摄照片的位置
			File f = createImageFile();
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
	 * 照片的命名规则为：sheqing_20130125_173729.jpg
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "sheqing_" + timeStamp + ".jpg";

		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	/**
	 * 上传到服务器
	 */
	private void upload() {

		if (mCurrentPhotoPath != null) {
			FileUploadTask task = new FileUploadTask();
			task.execute(mCurrentPhotoPath, "0");
		} else {
			Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
		}
	}

	private class FileUploadTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String filePath = params[0];
			FileBean bean = new FileBean();

			String type = params[1];// 上传图片还是视频，图片需要压缩

			String content;
			if (null != type && "1".equals(type)) {
				content = VideoUtil.videoToString(filePath);
			} else {
				content = PictureUtil.bitmapToString(filePath);
			}
			bean.setFileContent(content);

			File f = new File(filePath);
			String fileName = f.getName();
			bean.setFileName(fileName);

			Gson gson = new Gson();
			String json = gson.toJson(bean);

			MessageHelper helper = new MessageHelper(MainActivity.this);
			// return helper.sendMsg(json);//使用webservice
			return helper.sendPost(json);// 使用http post
		}

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("正在提交,请稍候...");
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
		}

	}

}

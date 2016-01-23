package com.loveplusplus.demo.fileupload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class ImageUtils {
	private static final String SDCARD_CACHE_IMG_PATH = Environment 
            .getExternalStorageDirectory().getPath() + "/itcast/images/"; 
	
	 /**
     * 保存图片到SD卡
     * @param imagePath
     * @param buffer
     * @throws IOException
     */ 
    public static void saveImage(String imagePath, byte[] buffer) 
            throws IOException { 
        File f = new File(imagePath); 
        if (f.exists()) { 
            return; 
        } else {
            File parentFile = f.getParentFile(); 
            if (!parentFile.exists()) { 
                parentFile.mkdirs(); 
            } 
            f.createNewFile(); 
            FileOutputStream fos = new FileOutputStream(imagePath); 
            fos.write(buffer); 
            fos.flush(); 
            fos.close(); 
        } 
    } 
       
    /**
     * 从SD卡加载图片
     * @param imagePath
     * @return
     */ 
    public static Bitmap getImageFromLocal(String imagePath){ 
        File file = new File(imagePath); 
        if(file.exists()){ 
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath); 
            file.setLastModified(System.currentTimeMillis()); 
            return bitmap; 
        } 
            return null; 
    } 
       
    /**
     * Bitmap转换到Byte[]
     * @param bm
     * @return
     */ 
    public static byte[] bitmap2Bytes(Bitmap bm){    
    		if(bm == null){
    			return new byte[]{};
    		}
        ByteArrayOutputStream bas = new ByteArrayOutputStream();      
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bas);      
        return bas.toByteArray();    
       }   
       
    /**
     * 压缩bitmap
     * @param image
     * @return
     */
    public static byte[] compressImage2Bytes(Bitmap image) {  
    	  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        float imgSize = (float)baos.toByteArray().length / 1024;
        Log.i("testLog", "压缩前:"+(int)imgSize);
        int imgMaxSiz = 200;
        if(imgSize>imgMaxSiz){
        	baos.reset();//重置baos即清空baos  
        	int compressNum = (int)((1- imgMaxSiz / imgSize) * 100);//要压缩的百分比
        	image.compress(Bitmap.CompressFormat.JPEG, 100 - compressNum, baos);
        	Log.i("testLog", "压缩后:"+baos.toByteArray().length / 1024 + " -compressNum" + compressNum);
        }
        return baos.toByteArray();  //经测试，当baos.toByteArray().length / 1024为247时，服务器返回图片太大上传失败
    } 
    
    /**
     * 压缩bitmap
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {  
    	  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
        		Log.d("postData","length = "+baos.toByteArray().length / 1024);
        		baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            if(options < 0){
            		options = 1;
            }else{
            		options -= 10;//每次都减少10
            }
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
    
    /**
     * 从本地或者服务端加载图片
     * @return
     * @throws IOException 
     */ 
    public static Bitmap loadImage(final String imagePath) { 
        Bitmap bitmap = getImageFromLocal(imagePath); 
        if(bitmap != null){ 
        		bitmap = compressImage(bitmap);
            return bitmap; 
        } 
        return null; 
    } 
   
    // 返回图片存到sd卡的路径 
    public static String getCacheImgPath() { 
        return SDCARD_CACHE_IMG_PATH; 
    } 
   
    public static String md5(String paramString) { 
        String returnStr; 
        try { 
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5"); 
            localMessageDigest.update(paramString.getBytes()); 
            returnStr = byteToHexString(localMessageDigest.digest()); 
            return returnStr; 
        } catch (Exception e) { 
            return paramString; 
        } 
    } 
   
    /**
     * 将指定byte数组转换成16进制字符串
     * 
     * @param b
     * @return
     */ 
    public static String byteToHexString(byte[] b) { 
        StringBuffer hexString = new StringBuffer(); 
        for (int i = 0; i < b.length; i++) { 
            String hex = Integer.toHexString(b[i] & 0xFF); 
            if (hex.length() == 1) { 
                hex = '0' + hex; 
            } 
            hexString.append(hex.toUpperCase()); 
        } 
        return hexString.toString(); 
    } 
    
    
    public static byte[] getBytesFromRes(Context context, int drawable){
    		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawable);
    		return bitmap2Bytes(bm);
    }
    
    
    
       
}

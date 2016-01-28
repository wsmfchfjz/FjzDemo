package com.example.utildemo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private static String filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/myAppName/";
	
	private static boolean isShowLog = true;
	private static boolean isSaveLog = true;
	private static String justSaveTag = "";
	
    public static void v(String tag, String msg) {
        if (isShowLog) {
            android.util.Log.v(tag, msg);
        }
        saveLogToFile(tag, msg);
    }
    
    public static void d(String tag, String msg) {
        if (isShowLog) {
            android.util.Log.d(tag, msg);
        }
        saveLogToFile(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isShowLog) {
            android.util.Log.i(tag, msg);
        }
        saveLogToFile(tag, msg);
    }
    
    public static void w(String tag, String msg) {
        if (isShowLog) {
            android.util.Log.w(tag, msg);
        }
        saveLogToFile(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isShowLog) {
            android.util.Log.e(tag, msg);
        }
        saveLogToFile(tag, msg);
    }
    
    private static void saveLogToFile(String tag, String msg){
    	 if(isSaveLog && (justSaveTag.equals("") || justSaveTag.equals(tag))){
         	String time = new SimpleDateFormat("MMddHH:mm:ss:SSS").format(new Date());
         	String savePath = filePath + time.substring(0, 4) + "log.txt";
         	FileUtils.getInstance().writeTxtFile(time.substring(4) + " - " +msg + "\n", savePath);
         }
    }

}

package com.example.utildemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class FileUtils {

	private static FileUtils instance = null;
	
	public static FileUtils getInstance() {
		if (instance == null) {
			synchronized (FileUtils.class) {
				if (instance == null)
					instance = new FileUtils();
			}
		}
		return instance;
	}
	
//	String filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/000WangHeApp0106/myFile.txt";
	public String readTxtFile(String filePath) {
		StringBuffer sb = new StringBuffer();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt + "\n");
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 写数据到文件中
	 */
	public void writeTxtFile(String data, String fileName) {
		try {
			
			initDirectory(fileName.substring(0,fileName.lastIndexOf("/")+1));

			File tmpFile = new File(fileName);
			if (!tmpFile.exists())
				createNewFile(fileName);

			FileWriter file = new FileWriter(fileName, true);

			file.write(data);
			file.flush();
			file.close();
			file = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		//FileWriter是写字符，比如写txt文本，FileOutputStream是写字节，比如写视频。
		//单写文本的话，用FileWriter快，如果写视频的话只能用FileOutputStream，用FileWriter写的话会无法播放。
		//filereader和fileinputstream同理
	}
	
	/**
	 * 只删除文件，不删除目录
	 */
	public void deleteFile(String filePath){
		File f = new File(filePath); 
		if(f.exists())
		    f.delete();
	}

	/**
	 * 初始化文件目录
	 */
	private void initDirectory(String directoryName) {
		
		File directory = new File(directoryName);
		if (!directory.exists())
			directory.mkdirs();
		directory = null;
	}
	
	/**
	 * 创建新文件
	 */
	private void createNewFile(String filePath) throws Exception {
		FileOutputStream fos = new FileOutputStream(filePath);
		fos.write("".getBytes());
		fos.flush();
		fos.close();
	}

	/**
	 * 释放本实例
	 */
	public static void recycle() {
		instance = null;
	}

}

package com.example.utildemo;

import java.net.HttpURLConnection;

import org.json.JSONObject;

import com.example.utildemo.HttpUtil.RequestType;

public class HttpService {
	
	public static final int SERVER_TIMEOUT_DEFAULT = 8000;

	private HttpUtil httpUtil;
	
	private static HttpService httpService;
	public static HttpService getInstance() {
		if (httpService == null) {
			httpService = new HttpService();
		}
		return httpService;
	}
	
	private HttpService(){
		httpUtil = HttpUtil.getInstance();
	}

	public String getCaptcha() {
		String result = null;
		try {
			JSONObject tokenJson = new JSONObject(getTokenJson("https://api.gizwits.com/app/request_token"));
			String token = tokenJson.getString("token");
			System.out.println("token:" + token);

			JSONObject captchaUrlJson = new JSONObject(getCaptchaUrlJson("http://api.gizwits.com/app/verify/codes", token));
			result = captchaUrlJson.getString("captcha_url");
			System.out.println("captcha_url:" + result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	private String getCaptchaUrlJson(String urlStr,String token){
		String captchaUrlJson = null;
		try {
			HttpURLConnection connection = httpUtil.getConnection(RequestType.GET, urlStr);
			
			connection.setRequestProperty("Accept-Charset", "utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-Gizwits-Application-Id", "dcea1850ec144673904b8adc6c326281");
			connection.setRequestProperty("X-Gizwits-Application-Token", token);
			
			connection.setConnectTimeout(SERVER_TIMEOUT_DEFAULT);
			connection.setReadTimeout(SERVER_TIMEOUT_DEFAULT);
			connection.connect();
			
			captchaUrlJson = httpUtil.request(RequestType.GET, urlStr, "", connection);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return captchaUrlJson;
	}
	
	private String getTokenJson(String urlStr){
		String tokenJson = null;
		try {
			HttpURLConnection connection = httpUtil.getConnection(RequestType.POST, urlStr);
			
			connection.setRequestProperty("Accept-Charset", "utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-Gizwits-Application-Id", "dcea1850ec144673904b8adc6c326281");
			connection.setRequestProperty("X-Gizwits-Application-Auth", "944513b65523f420bdddbe33210fbf6a");
			
			connection.setConnectTimeout(SERVER_TIMEOUT_DEFAULT);
			connection.setReadTimeout(SERVER_TIMEOUT_DEFAULT);
			connection.connect();
			
			tokenJson = httpUtil.request(RequestType.POST, urlStr, "", connection);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tokenJson;
	}
	
}

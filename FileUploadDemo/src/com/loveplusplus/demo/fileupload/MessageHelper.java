package com.loveplusplus.demo.fileupload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

public class MessageHelper {

	private static final String TAG = null;
	private static String NAMESPACE = null;
	private static String URL = null;
	private static String METHOD_NAME = null;
	private static String SOAP_ACTION = null;
	private static String POST_URL = null;

	public MessageHelper(Context context) {
		try {
			Properties p = new Properties();
			p.load(context.getResources().openRawResource(R.raw.system));
			NAMESPACE = p.getProperty("NAMESPACE");
			URL = p.getProperty("URL");
			METHOD_NAME = p.getProperty("METHOD_NAME");
			SOAP_ACTION = NAMESPACE + METHOD_NAME;
			POST_URL=p.getProperty("POST_URL");
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调用webservice
	 * 
	 * @param json
	 * @return
	 */
	public String sendMsg(String json) {
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("arg0", json);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = false;
			envelope.encodingStyle = "UTF-8";
			envelope.setOutputSoapObject(rpc);
			new MarshalBase64().register(envelope);
			HttpTransportSE aht = new HttpTransportSE(URL, 60 * 1000);

			aht.call(SOAP_ACTION, envelope);
			Object result = (Object) envelope.getResponse();
			Log.d(TAG, result.toString());
			return String.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String sendPost(String json) {
		try {
			HttpURLConnection httpcon = (HttpURLConnection) ((new URL(POST_URL)
					.openConnection()));
			httpcon.setDoOutput(true);
			httpcon.setRequestProperty("Content-Type", "application/json");
			httpcon.setRequestProperty("Accept", "application/json");
			httpcon.setRequestMethod("POST");
			httpcon.connect();

			byte[] outputBytes = json.getBytes("UTF-8");
			OutputStream os = httpcon.getOutputStream();
			os.write(outputBytes);
			os.close();
			
			int status = httpcon.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
          
            return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

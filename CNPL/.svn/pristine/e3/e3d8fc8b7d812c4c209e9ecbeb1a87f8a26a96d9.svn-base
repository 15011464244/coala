package com.cn.net.cnpl.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Message;
import android.util.Log;

import com.cn.net.cnpl.model.Head;

public class NetHelper {

	HttpPost request = null;

	public void Create(String url) {
		request = new HttpPost(url);
	}

	public boolean ServerVerification() {

		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 10000);

			httpClient.execute(request);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public List<JSONObject> executeEx(List<NameValuePair> params)
			throws Exception {

		JSONObject obj = execute(params);
		List<JSONObject> body = ResolveBody(obj, "traces");
		return body;
	}

	public List<JSONObject> execute(List<NameValuePair> params, Head info)
			throws Exception {

		HttpResponse httpResponse = null;
		JSONObject jsonObject = null;
		List<JSONObject> result = null;

		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);

		if (params != null) {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}

		httpResponse = httpClient.execute(request);
		String retSrc = EntityUtils.toString(httpResponse.getEntity());

		JSONTokener jsonParser = new JSONTokener(retSrc);
		if (jsonParser != null)
			jsonObject = (JSONObject) jsonParser.nextValue();

		if (jsonObject != null) {
			JSONObject header = jsonObject.getJSONObject("header");
			if (header != null) {
				if (header.getString("ret") != null
						&& "0".equals(header.getString("ret"))) {
					result = NetHelper.ResolveBody(jsonObject, "body");
				}
				info.setErrorMsg(header.getString("errorMsg"));
				info.setRet(header.getString("ret"));
			}
		}

		return result;
	}

	public JSONObject execute(List<NameValuePair> params) throws Exception {
		try {
			HttpResponse httpResponse = null;

			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 10000);

			if (params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}

			httpResponse = httpClient.execute(request);
			String sb = EntityUtils.toString(httpResponse.getEntity(),
					HTTP.UTF_8);
			Log.e("result", sb);
			JSONTokener jsonParser = new JSONTokener(sb);
			JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
			return jsonObject;
		} catch (Exception e) {
			return null;

		}
	}
	

	
	public JSONObject executeCnpl(JSONObject jsonObject) throws Exception {
		JSONObject result=new JSONObject();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONArray requestjsonArray=new JSONArray();
		requestjsonArray.put(jsonObject);
		params.add(new BasicNameValuePair("para", requestjsonArray.toString()));
		  JSONObject jsonObject2 = execute(params);
		if(!jsonObject2.getBoolean("success")){
			result=jsonObject2.getJSONArray("sysinfos").getJSONObject(0);
			result.put("success", jsonObject2.getBoolean("success"));
			return result;
		}else{
			try{
				result=jsonObject2.getJSONObject("data");
				result.put("success", jsonObject2.getBoolean("success"));
				return result;
			}catch (Exception e) {
				return jsonObject2;
			}
		
		
			
		}
	
	}

	public JSONObject executeGet(String url) throws Exception {
		BufferedReader in = null;
		try {
			// 定义HttpClient
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					10000);

			// 实例化HTTP方法
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			// if ("1".equals(sb.toString().split("\t")[0])) {
			String res = sb.toString().split("\t")[1];
			JSONTokener jsonParser = new JSONTokener(res);
			JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
			return jsonObject;
			// }
			// if ("0".equals(sb.toString().split("\t")[0])) {
			// String res = sb.toString().split("\t")[1];
			// JSONTokener jsonParser = new JSONTokener(res);
			// JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
			// return jsonObject;
			// }

		} catch (Exception e) {
			return null;

		}
	}

	public static List<JSONObject> ResolveBody(JSONObject obj, String key) {

		if (obj == null)
			return null;

		List<JSONObject> rList = new ArrayList<JSONObject>();
		try {
			JSONArray objArray = obj.getJSONArray(key);
			for (int i = 0; i < objArray.length(); i++) {
				rList.add(objArray.getJSONObject(i));
			}
		} catch (JSONException e) {
			return null;
		}

		return rList;
	}

	public static boolean isNetworkAvailable(Context ctx) {
		try {
			ConnectivityManager cm = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			boolean wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.isConnectedOrConnecting();
			boolean internet = cm.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
			return (wifi || internet);
		} catch (Exception e) {
			return false;
		}
	}

	public   List<JSONObject> exeRequest(String sendData, String urlStr)
			throws Exception {
		try {
			URL url = new URL(urlStr.toString());
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(1000 * 4);
			httpConn.setReadTimeout(1000 * 6);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			// httpConn.setRequestProperty("Content-Type",
			// "application/json; charset=UTF-8");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setInstanceFollowRedirects(false);
			// byte[] gZip = BaseCommand.gZip(sendData.getBytes());
			httpConn.getOutputStream().write(sendData.getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), "UTF-8"));
			// InputStreamReader inss = new InputStreamReader(
			// httpConn.getInputStream(), "UTF-8");
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			if ("1".equals(sb.toString().split("\t")[0])) {
				String res = sb.toString().split("\t")[1];
				// 解码
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] bytesrc = decoder.decodeBuffer(res);
				// 解压
				byte[] resu = BaseCommand.unGZip(bytesrc);
				JSONTokener jsonParser = new JSONTokener(new String(resu));

				JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
				List<JSONObject> result = NetHelper.ResolveBody(jsonObject,
						"dataList");
				return result;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public   JSONObject exeRequestForJsonObj(String sendData, String urlStr)
			throws Exception {
		try {
			URL url = new URL(urlStr.toString());
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(1000 * 40);
			httpConn.setReadTimeout(1000 * 60);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			// httpConn.setRequestProperty("Content-Type",
			// "application/json; charset=UTF-8");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setInstanceFollowRedirects(false);
			// byte[] gZip = BaseCommand.gZip(sendData.getBytes());
			httpConn.getOutputStream().write(sendData.getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), "UTF-8"));
			// InputStreamReader inss = new InputStreamReader(
			// httpConn.getInputStream(), "UTF-8");
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			if ("1".equals(sb.toString().split("\t")[0])) {
				String res = sb.toString().split("\t")[1];
				// 解码
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] bytesrc = decoder.decodeBuffer(res);
				// 解压
				byte[] resu = BaseCommand.unGZip(bytesrc);
				JSONTokener jsonParser = new JSONTokener(new String(resu));

				JSONObject result = (JSONObject) jsonParser.nextValue();
				// JSONObject result = NetHelper.ResolveBody(jsonObject,
				// "dataList");
				return result;

			}
			if ("0".equals(sb.toString().split("\t")[0])) {
				String res = sb.toString().split("\t")[1];
				JSONTokener jsonParser = new JSONTokener(res);
				JSONObject result = (JSONObject) jsonParser.nextValue();
				return result;
			}
			return null;
		} catch (Exception e) {
			Log.e("aasdasd", e.getMessage());
			return null;
		}
	}

	public  JSONObject exeRequestGzip(String sendData, String urlStr)
			throws Exception {
		try {
			URL url = new URL(urlStr.toString());
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(1000 * 4);
			httpConn.setReadTimeout(1000 * 6);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type",
					"text/html;charset=UTF-8");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setInstanceFollowRedirects(false);
			// 压缩
			byte[] gZip = BaseCommand.gZip(sendData.getBytes("UTF-8"));
			httpConn.getOutputStream().write(gZip);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			Log.e("", sb.toString());
			JSONObject obj = new JSONObject(sb.toString());
			if (obj.getBoolean("success")) {
				return obj;
			}
			return null;
		} catch (Exception e) {
			return null;
		}

	}

}

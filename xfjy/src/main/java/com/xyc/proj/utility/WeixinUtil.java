package com.xyc.proj.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONException;
import com.xyc.proj.entity.AccessToken;



/**
 * ����ƽ̨ͨ�ýӿڹ�����
 * 
 * @author liuyq
 * @date 2013-08-09
 */
public class WeixinUtil {
	private static Logger log = Logger.getLogger(WeixinUtil.class);
	
	// ��ȡaccess_token�Ľӿڵ�ַ��GET�� ��200����/�죩
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * ��ȡaccess_token
	 * 
	 * @param appid ƾ֤
	 * @param appsecret ��Կ
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		com.alibaba.fastjson.JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// �������ɹ�
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// ��ȡtokenʧ��
				log.error("��ȡtokenʧ�� errcode:{} errmsg:{}="+jsonObject.getIntValue("errcode")+"    "+ jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * ����https���󲢻�ȡ���
	 * 
	 * @param requestUrl �����ַ
	 * @param requestMethod ����ʽ��GET��POST��
	 * @param outputStr �ύ�����
	 * @return JSONObject(ͨ��JSONObject.get(key)�ķ�ʽ��ȡjson���������ֵ)
	 */
	public static com.alibaba.fastjson.JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		com.alibaba.fastjson.JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ������SSLContext�����еõ�SSLSocketFactory����
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// ��������ʽ��GET/POST��
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// ���������Ҫ�ύʱ
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// ע������ʽ����ֹ��������
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// �����ص�������ת�����ַ�
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// �ͷ���Դ
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = com.alibaba.fastjson.JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}
}
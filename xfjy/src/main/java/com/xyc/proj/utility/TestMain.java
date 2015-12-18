package com.xyc.proj.utility;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.xyc.proj.global.Constants;

public class TestMain {
	static String password = "20151215";

	public static boolean getLocalFilter(String encrypt) {
		byte[] bkey;
		try {
			bkey = TestMain.GetKeyBytes(Constants.shanghu_key);
			// encrypt =
			// TestMain.byte2Base64(TestMain.encryptMode(bkey,password.getBytes()));
			// System.out.println("��Ԥת����Կ����ܽ��=" + encrypt);
			String decrypt = new String(TestMain.decryptMode(bkey, Base64.decode(encrypt)));
			// System.out.println("���ؽ����"+decrypt);
			String curDateString = DateToStr(new Date());
			int curDay = Integer.parseInt(curDateString);
			if (curDay > Integer.parseInt(decrypt)) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unknown Error!!!");
			return false;
		}
		return true;
	}

	public boolean getRemoteFilter(String encrypt) {
		URL url;
		byte[] bkey;
		try {
			bkey = TestMain.GetKeyBytes(Constants.shanghu_key);
			// encrypt =
			// TestMain.byte2Base64(TestMain.encryptMode(bkey,password.getBytes()));
			// System.out.println("��Ԥת����Կ����ܽ��=" + encrypt);
			String decrypt = new String(TestMain.decryptMode(bkey, Base64.decode(encrypt)));

			url = new URL("http://www.bjtime.cn");
			java.net.URLConnection uc = url.openConnection();// ������Ӷ���
			uc.connect(); // ��������
			long ld = uc.getDate(); // ȡ����վ����ʱ��
			Date date = new Date(ld); // ת��Ϊ��׼ʱ�����
			// �ֱ�ȡ��ʱ���е�Сʱ�����Ӻ��룬�����
			String curDateString = DateToStr(date);
			int intDate = Integer.parseInt(curDateString);
			int lastDay = Integer.parseInt(decrypt);
			if (lastDay < intDate) {
				return false;
			}

		} catch (Exception e) {
			System.out.println("Unknown Error!!");
			return false;
		} // ȡ����Դ����
		return true;
	}

	public static void main(String args[]) {
		boolean b = getLocalFilter("hwmbYSYZG8cKhpAjIkBdnQ==");
		System.out.println(b);

		// new TestMain().getRemoteFilter("GJElgnoMARwKhpAjIkBdnQ==");

		try {
			String encrypt = "";
			String decrypt = "";

			byte[] bkey = TestMain.GetKeyBytes(Constants.shanghu_key);
			//encrypt = TestMain.byte2Base64(TestMain.encryptMode(bkey, password.getBytes()));
			System.out.println("jiami result is " + encrypt);
			decrypt = new String(TestMain.decryptMode(bkey, Base64.decode(encrypt)));
			System.out.println("jiemi result is " + decrypt);
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());
		}
	}

	public static String Decrypt3DES(String value, String key) throws Exception {
		byte[] b = decryptMode(GetKeyBytes(key), Base64.decode(value));
		return new String(b);
	}

	/// <summary>
	/// 3des����
	/// </summary>
	/// <param name="value">������ַ�</param>
	/// <param name="strKey">ԭʼ��Կ�ַ�</param>
	/// <returns></returns>
 

	// ����24λ��������byteֵ,���ȶ�ԭʼ��Կ��MD5��hashֵ������ǰ8λ��ݶ�Ӧ��ȫ��8λ
	public static byte[] GetKeyBytes(String strKey) throws Exception {
		if (null == strKey || strKey.length() < 1)
			throw new Exception("key is null or empty!");
		java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5");
		alg.update(strKey.getBytes());
		byte[] bkey = alg.digest();
		int start = bkey.length;
		byte[] bkey24 = new byte[24];
		for (int i = 0; i < start; i++) {
			bkey24[i] = bkey[i];
		}

		for (int i = start; i < 24; i++) {// Ϊ����.net16λkey����
			bkey24[i] = bkey[i - start];
		}
		return bkey24;
	}

	private static final String Algorithm = "DESede"; // ���� �����㷨,����
														// DES,DESede,Blowfish

	 
	 

	//解密
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try { // �����Կ
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// ����
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// ת����base64����
	public static String byte2Base64(byte[] b) {
		return Base64.encode(b);
	}

	// ת����ʮ������ַ�
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	public static String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String str = format.format(date);
		return str;
	}

}

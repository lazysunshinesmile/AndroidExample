/****************************************************************************
 *
 * FILENAME:        com.learn.urlpic.HttpUtil.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-1-30
 *
 * DESCRIPTION:     The class encapsulates the music ring tone operations.
 *
 * vi: set ts=4:
 *
 * Copyright (c) 2009-2011 by Grandstream Networks, Inc.
 * All rights reserved.
 *
 * This material is proprietary to Grandstream Networks, Inc. and,
 * in addition to the above mentioned Copyright, may be
 * subject to protection under other intellectual property
 * regimes, including patents, trade secrets, designs and/or
 * trademarks.
 *
 * Any use of this material for any purpose, except with an
 * express license from Grandstream Networks, Inc. is strictly
 * prohibited.
 *
 ***************************************************************************/
package com.base.module.screensaver.utils;

import com.base.module.screensaver.ScreenSaverManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * ClassName:HttpUtil
 * @Description: 
 */

public class HttpUtil {
	private static String TAG = HttpUtil.class.getSimpleName();
	
	public static int ERROR_CODE = ScreenSaverManager.NO_ERROR;
	
	public static InputStream httpGetUrlPicture(String strUrl) throws Exception {
		Log.d(TAG, "url:" + strUrl);
		URL url = new URL(strUrl);
		// open Connection URL
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(60 * 1000);
		// get picture stream
		InputStream inStream = conn.getInputStream();
		return inStream;
	}
	
	public static Bitmap fetchBitmapFromUrl(String strUrl) throws Exception {
	    Log.d(TAG, "url:" + strUrl);
        URL url = new URL(strUrl);
        // open Connection URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(60 * 1000);
        // get picture stream
        //InputStream inStream = conn.getInputStream();
        InputStream inStream = httpGet(strUrl);
        try{
            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
            return bitmap;
        } catch (OutOfMemoryError err) {
            inStream = null;
            err.printStackTrace();
        }
        return null;
	}
	
    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = is.read(b, 0, 1024)) != -1) {
            baos.write(b, 0, len);
            baos.flush();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }
	
    public static Bitmap getBitmapFromUrl(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is); // bitmap =
                                                                   // BitmapFactory.decodeStream(bis);
            byte[] b = getBytes(is);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

	/**
	 * http for Delete
	 */
	public static InputStream httpDel(String url) {
		HttpDelete httpdelete = new HttpDelete(url);
		httpdelete.addHeader("Content-Type", "text/xml");
		return httpRequest(httpdelete);
	}

	/**
	 * httpGet
	 */
	public static InputStream httpGet(String url) {
		MyLog.d(TAG, url);
		HttpGet request = new HttpGet(url);
		return httpRequest(request);
	}

	/**
	 * httpPost
	 */
	public static InputStream httpPost(String url, List<NameValuePair> params) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		return httpRequest(httpPost);
	}

	/**
	 * httpPost
	 */
	public static InputStream httpPost(String url, String content) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "text/xml");
		StringEntity myEntity = null;
		try {
			myEntity = new StringEntity(content, HTTP.UTF_8);
			httpPost.setEntity(myEntity);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		return httpRequest(httpPost);
	}

	/**
	 * httpPut
	 */
	public static InputStream httpPut(String url, String content) {
		HttpPut httpput = new HttpPut(url);
		httpput.addHeader("Content-Type", "text/xml");
		try {
			StringEntity myEntity = new StringEntity(content, HTTP.UTF_8);
			httpput.setEntity(myEntity);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return httpRequest(httpput);
	}

	/**
	 * request
	 */
	private static InputStream httpRequest(HttpUriRequest httpUriRequest) {
		InputStream result = null;
		HttpResponse response = null;
		HttpClient httpClient = initHttp();
		try {
			response = httpClient.execute(httpUriRequest);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (response == null)
			MyLog.d(TAG, "httpRequest null");
		else
			MyLog.d(TAG, response.getStatusLine().getStatusCode());
		if (response != null && response.getStatusLine().getStatusCode() == 200) {
			try {
				HttpEntity httpentity = response.getEntity();
				StringBuffer buffer = new StringBuffer();
				buffer.append("name=");
				buffer.append(httpentity.getContentType().getName());
				buffer.append(", value ="+httpentity.getContentType().getValue());
				//httpentity.getContentType().getElements();
				Log.i(TAG, buffer.toString());
				result = httpentity.getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * init httpClient
	 */
	private static HttpClient initHttp() {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				60000);
		client.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 60000);
		return client;
	}
}


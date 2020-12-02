package com.base.module.screensaver.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOStreamUtil {
	public static String convertStreamToString(final InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * finally { try{ is.close(); } catch(IOException e){
		 * e.printStackTrace(); } }
		 */
		return sb.toString();
	}

	/**
	 * 
	 * @Description: Transfer the InputStream to bytes
	 * @param inStream
	 * @return
	 * @throws Exception
	 * @Return_Type :byte[]
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
	    System.gc();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		byte[] array = new byte[1024*1024*5];
		int len = 0;
		int count=0;
		while ((len = inStream.read(buffer)) >= 0) {
			outStream.write(buffer, 0, len);
			System.arraycopy(buffer, 0, array, count, len);
			count+=len;
		}
		inStream.close();
		
		byte[] bytes=new byte[count];
		System.arraycopy(array, 0, bytes, 0, count);
		buffer=null;
		array=null;
		System.gc();
		return bytes;
	}

	/**
	 * 
	 * @Description:
	 * @param filePath
	 * @param data
	 * @Return_Type :void
	 */
	public static void saveByte2File(String filePath, byte[] data) {
		FileOutputStream outStream;
		try {
			File imageFile = new File(filePath);
			outStream = new FileOutputStream(imageFile);
			outStream.write(data);
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

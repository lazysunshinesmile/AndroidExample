package com.base.module.screensaver.utils;

import android.util.Log;

public class MyLog {
	public static boolean DEBUG = false;

	public static void w(String tag, Object msg) {
		log(tag, msg.toString(), 'w');
	}

	public static void e(String tag, Object msg) {
		log(tag, msg.toString(), 'e');
	}

	public static void d(String tag, Object msg) {
		log(tag, msg.toString(), 'd');
	}

	public static void i(String tag, Object msg) {
		log(tag, msg.toString(), 'i');
	}

	public static void v(String tag, Object msg) {
		log(tag, msg.toString(), 'v');
	}

	/**
	 * @param tag
	 * @param msg
	 * @param level
	 * @return void
	 * @since v 1.0
	 */
	private static void log(String tag, String msg, char level) {
		if (DEBUG) {
			if ('e' == level) {
				Log.e(tag, msg);
			} else if ('w' == level) {
				Log.w(tag, msg);
			} else if ('d' == level) {
				Log.d(tag, msg);
			} else if ('i' == level) {
				Log.i(tag, msg);
			} else {
				Log.v(tag, msg);
			}
		}
	}
}

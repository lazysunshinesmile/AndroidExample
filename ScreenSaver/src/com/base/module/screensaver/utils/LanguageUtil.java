/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.utils.LanguageUtil.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-2-2
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

import com.gs.core.language.LanguageManager;

import android.content.Context;
import android.preference.Preference;
import android.widget.TextView;

/**
 * ClassName:LanguageUtil
 * 
 * @Description:
 */

public final class LanguageUtil {
    private static LanguageManager mLanguageManager = null;

    public static void setText(Context context, TextView textView, int id) {
        textView.setText(getValue(id));
    }

    public static void setTitle(Context context, Preference preference, int id) {
        preference.setTitle(getValue(id));
    }

    public static void setSummary(Context context, Preference preference, int id) {
        preference.setSummary(getValue(id));
    }

    public static String getValue(int index) {
        if (mLanguageManager == null) {
            mLanguageManager = LanguageManager.instance();
        }
        String result = mLanguageManager.getValue(index);
        if (result == null) {
            result = "";
        }
        return result;
    }

    public static final int SCREEN_SAVER = 1200;
    public static final int SCREEN_SAVER_SETTING = 1202;
    public static final int SET_SCREEN_SAVER_PICTURES_PATH = 1203;
    public static final int PLEASE_INPUT_ADDRESS = 1204;
    public static final int APPLY_INTERNET_PICTURES = 1205;
    public static final int SET_INTERNET_PICTURES_URL = 1206;
    public static final int INTERNET_PICTURES = 1207;
    public static final int LOCAL_PATH_TITLE = 1209;
    public static final int TIMEOUT_SETTING = 1210;
    public static final int SCREEN_SAVER_TIMEOUT_SUMMARY = 1211;
    public static final int SCREEN_SAVER_TIMEOUT_TITLE = 1212;
    public static final int ANIM_IDLE_TIME_SUMMARY = 1213;
    public static final int ANIM_IDLE_TIME_TITLE = 1214;
    public static final int TOAST_INTERNET_OR_ADDRESS_ERROR = 1215;
    public static final int TOAST_FOLDER_NO_PICTURE = 1216;
    public static final int PREVIEW = 68;
    public static final int PREVIEW_SCREEN_SAVER = 1218;
    public static final int SECOND = 116;
    public static final int TOAST_4_LARGE_PICTURE = 1220;
    public static final int TOAST_SPACE_NOT_ENOUGH = 1221;
    public static final int TOAST_NOT_SUPPORTED_FILE = 1222;
    
    public static final int MINUTE = 112;
    public static final int NEVER = 42;
}

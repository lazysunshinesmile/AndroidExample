/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.ui.SettingPreference.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-1-9
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
package com.base.module.screensaver.ui;

import com.base.module.screensaver.R;
import com.base.module.screensaver.utils.LanguageUtil;
import com.base.module.screensaver.utils.MyLog;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;

/**
 * ClassName:SettingPreference
 * 
 * @Description:
 */

public class SettingPreference extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private static final String TAG = "DisplaySettings";
    public static final String PREFERENCES = "settings";
    public static final String DEFAULT_LOCAL_PATH = "/system/media/screensaver/";

    //
    private static final String KEY_USE_HTTP = "use_http";
    public static final String USE_HTTP = "use_http";

    //
    private static final String KEY_HTTP_URL = "http_url";
    public static final String HTTP_URL = "http_url";
    EditTextPreference mHttpUrlPreference;
    //
    private static final String CHOOSE_FILE_ACTION = "com.base.module.filemanager.CHOOSE_FILE";
    private static final String KEY_PICK_FOLDER = "pick_folder";
    private static final String KEY_ACTION_TYPE = "actionType";
    private static final int REQ_SET_LOCAL_FILE = 60;
    private static final int REQ_SET_LOCAL_PATH = 61;
    private static final String KEY_LOCAL_PATH = "local_path";
    public static final String LOCAL_PATH = "local_path";
    Preference mLocalPathPreference;

    // preview screen saver
    private static final String KEY_PREVIEW = "preview";
    private static final String PREVIEW = "preview";
    Preference mPreviewPreference;

    /** If there is no setting in the provider, use this. */
    /*
    private static final int FALLBACK_SCREEN_SAVER_TIMEOUT_VALUE = 120000;
    private static final String KEY_SCREEN_SAVER_TIMEOUT = "screen_saver_timeout";
    public static final String SCREEN_SAVER_TIMEOUT = "screen_saver_timeout";
    private static final CharSequence[] SCREEN_SAVER_TIMEOUT_ENTRY_VALUES = { "60000", "120000", "300000", "600000", "1200000", "1800000", "-1" };
    private static final int[] MINUTES = {1, 2, 5, 10, 20, 30, -1};
    private static final int NEVER = -1;
    */

    /** If there is no setting in the provider, use this. */
    public static final int FALLBACK_ANIM_IDLE_TIME_VALUE = 3000;
    private static final String KEY_ANIM_IDLE_TIME = "anim_idle_time";
    public static final String ANIM_IDLE_TIME = "anim_idle_time";
    private static final CharSequence[] ANIM_IDLE_TIME_ENTRY_VALUES = { "3000", "5000", "8000", "10000" };
    private static final int[] SECONDS = {3, 5, 8, 10};

    /** split string of entry values. */
    private static final String ARRAY_SPLIT = "\\|";

    private Context mContext;

    /**
     * @Description:
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.settings);

        mContext = this;
        final Context context = mContext;
        LanguageUtil.setTitle(context, findPreference("screen_saver_setting_title"), LanguageUtil.SCREEN_SAVER_SETTING);
        LanguageUtil.setTitle(context, findPreference("picture_path_setting"), LanguageUtil.SET_SCREEN_SAVER_PICTURES_PATH);
        LanguageUtil.setTitle(context, findPreference("screen_saver_time_setting"), LanguageUtil.TIMEOUT_SETTING);

        initPreference();
    }

    private void initPreference() {
        final Context context = this;

        ContentResolver resolver = getContentResolver();
        SharedPreferences pref = getSharedPreferences(PREFERENCES, 0);
        // is Use Http
        SwitchPreference useHttpPreference = (SwitchPreference) findPreference(KEY_USE_HTTP);
        useHttpPreference.setChecked(pref.getBoolean(USE_HTTP, false));
        useHttpPreference.setOnPreferenceChangeListener(this);
        LanguageUtil.setTitle(context, useHttpPreference, LanguageUtil.APPLY_INTERNET_PICTURES);

        // Http Url
        mHttpUrlPreference = (EditTextPreference) findPreference(KEY_HTTP_URL);
        mHttpUrlPreference.setOnPreferenceChangeListener(this);
        String httpUrl = pref.getString(HTTP_URL, "");
        mHttpUrlPreference.setText(httpUrl);
        if (httpUrl.toString().equals("")) {
            LanguageUtil.setSummary(context, mHttpUrlPreference, LanguageUtil.SET_INTERNET_PICTURES_URL);
        } else {
            mHttpUrlPreference.setSummary(httpUrl);
        }
        mHttpUrlPreference.setDialogTitle(LanguageUtil.getValue(LanguageUtil.INTERNET_PICTURES));
        mHttpUrlPreference.getEditText().setHint(LanguageUtil.getValue(LanguageUtil.PLEASE_INPUT_ADDRESS));
        LanguageUtil.setTitle(this, mHttpUrlPreference, LanguageUtil.INTERNET_PICTURES);
        mHttpUrlPreference.getEditText().setSingleLine(true);

        // Local Path
        mLocalPathPreference = (Preference) findPreference(KEY_LOCAL_PATH);
        mLocalPathPreference.setOnPreferenceClickListener(this);
        String localPath = pref.getString(LOCAL_PATH, "");
        if (localPath.toString().equals("")) {
            localPath = DEFAULT_LOCAL_PATH;
            pref.edit().putString(LOCAL_PATH, localPath).commit();
        }
        if (!localPath.endsWith("/"))
            localPath += "/";

        LanguageUtil.setTitle(context, mLocalPathPreference, LanguageUtil.LOCAL_PATH_TITLE);
        mLocalPathPreference.setSummary(localPath);

        // set Enable
        if (useHttpPreference.isChecked()) {
            mHttpUrlPreference.setEnabled(true);
            mLocalPathPreference.setEnabled(false);
        } else {
            mHttpUrlPreference.setEnabled(false);
            mLocalPathPreference.setEnabled(true);
        }

        // Preview Screen Saver
        mPreviewPreference = (Preference) findPreference(KEY_PREVIEW);
        LanguageUtil.setTitle(context, mPreviewPreference, LanguageUtil.PREVIEW);
        LanguageUtil.setSummary(context, mPreviewPreference, LanguageUtil.PREVIEW_SCREEN_SAVER);

        /*
        // Screen Saver Timeout
        ListPreference screenSaverTimeoutPreference = (ListPreference) findPreference(KEY_SCREEN_SAVER_TIMEOUT);
        screenSaverTimeoutPreference.setOnPreferenceChangeListener(this);
        screenSaverTimeoutPreference.setDialogTitle(LanguageUtil.getValue(LanguageUtil.SCREEN_SAVER_TIMEOUT_TITLE));
        LanguageUtil.setTitle(context, screenSaverTimeoutPreference, LanguageUtil.SCREEN_SAVER_TIMEOUT_TITLE);
        LanguageUtil.setSummary(context, screenSaverTimeoutPreference, LanguageUtil.SCREEN_SAVER_TIMEOUT_SUMMARY);

        String never = LanguageUtil.getValue(LanguageUtil.NEVER);
        String minute = LanguageUtil.getValue(LanguageUtil.MINUTE);
        CharSequence[] entries = new CharSequence[MINUTES.length];
        for (int i = 0; i < entries.length; i++) {
            if (MINUTES[i] != NEVER) {
                entries[i] = MINUTES[i] + " " + minute;
            } else { // never
                entries[i] = never;
            }
        }
        screenSaverTimeoutPreference.setEntries(entries);
        final CharSequence[] entryValues = SCREEN_SAVER_TIMEOUT_ENTRY_VALUES;
        screenSaverTimeoutPreference.setEntryValues(entryValues);
        screenSaverTimeoutPreference.setValue(String.valueOf(Settings.System.getInt(resolver, SCREEN_SAVER_TIMEOUT, FALLBACK_SCREEN_SAVER_TIMEOUT_VALUE)));
        // hide set screen saver timeout.
        getPreferenceScreen().removePreference(screenSaverTimeoutPreference);
        */

        // Animation Idle Time
        ListPreference animIdleTimePreference = (ListPreference) findPreference(KEY_ANIM_IDLE_TIME);
        animIdleTimePreference.setOnPreferenceChangeListener(this);
        animIdleTimePreference.setDialogTitle(LanguageUtil.getValue(LanguageUtil.ANIM_IDLE_TIME_TITLE));
        LanguageUtil.setTitle(context, animIdleTimePreference, LanguageUtil.ANIM_IDLE_TIME_TITLE);
        LanguageUtil.setSummary(context, animIdleTimePreference, LanguageUtil.ANIM_IDLE_TIME_SUMMARY);

        String second = LanguageUtil.getValue(LanguageUtil.SECOND);
        CharSequence[] idleEntries = new CharSequence[SECONDS.length];
        for (int i = 0; i < idleEntries.length; i++) {
            idleEntries[i] = SECONDS[i] + " " + second;
        }
        animIdleTimePreference.setEntries(idleEntries);
        CharSequence[] idleEntryValues = ANIM_IDLE_TIME_ENTRY_VALUES;
        animIdleTimePreference.setEntryValues(idleEntryValues);
        int idleTime = pref.getInt(ANIM_IDLE_TIME, FALLBACK_ANIM_IDLE_TIME_VALUE);
        animIdleTimePreference.setValue(String.valueOf(idleTime));
    }

    /**
     * @Description:
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_CANCELED == resultCode) {
            Log.i(TAG, "cancel");
            return;
        }

        if (REQ_SET_LOCAL_PATH != requestCode && REQ_SET_LOCAL_FILE != requestCode) {
            return;
        }

        // If request Code is Set Local Path
        Uri uri = (Uri) data.getData();
        String localPath = uri.getPath();
        MyLog.i(TAG, localPath);

        if (localPath.equals("")) {
            localPath = DEFAULT_LOCAL_PATH;
        }

        if (!localPath.endsWith("/")) {
            localPath += "/";
        }

        SharedPreferences pref = getSharedPreferences(PREFERENCES, 0);
        pref.edit().putString(LOCAL_PATH, localPath).commit();
        mLocalPathPreference.setSummary(localPath);
    }

    /**
     * @Description:
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * @Description:
     * @param preference
     * @param newValue
     * @return
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        Log.i(TAG, key + "," + newValue.toString());

        if (KEY_USE_HTTP.equals(key)) {
            if (newValue.toString().equals("true")) {
                mHttpUrlPreference.setEnabled(true);
                mLocalPathPreference.setEnabled(false);
            } else {
                mHttpUrlPreference.setEnabled(false);
                mLocalPathPreference.setEnabled(true);
            }

            SharedPreferences pref = getSharedPreferences(PREFERENCES, 0);
            pref.edit().putBoolean(USE_HTTP, newValue.toString().equals("true")).commit();
        }

        if (KEY_HTTP_URL.equals(key)) {
            SharedPreferences pref = getSharedPreferences(PREFERENCES, 0);
            pref.edit().putString(HTTP_URL, newValue.toString()).commit();

            String httpUrl = newValue.toString();
            if (newValue.toString().equals("")) {
                LanguageUtil.setSummary(this, mHttpUrlPreference, LanguageUtil.SET_INTERNET_PICTURES_URL);
            } else {
                mHttpUrlPreference.setSummary(httpUrl);
            }
        }

        /*
        if (KEY_SCREEN_SAVER_TIMEOUT.equals(key)) {
            int value = Integer.parseInt((String) newValue);
            try {
                Settings.System.putInt(getContentResolver(), SCREEN_SAVER_TIMEOUT, value);
            } catch (NumberFormatException e) {
                Log.e(TAG, "could not persist screen saver timeout setting", e);
            }
        }
        */

        if (KEY_ANIM_IDLE_TIME.equals(key)) {
            int value = Integer.parseInt((String) newValue);
            SharedPreferences pref = getSharedPreferences(PREFERENCES, 0);
            pref.edit().putInt(ANIM_IDLE_TIME, value).commit();
        }

        return true;
    }

    /**
     * @Description:
     * @param preference
     * @return
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        // Open FileManager and Select a Path
        Intent intent = new Intent();
        intent.putExtra(KEY_ACTION_TYPE, REQ_SET_LOCAL_PATH);
        intent.putExtra(KEY_PICK_FOLDER, true);
        intent.setAction(CHOOSE_FILE_ACTION);
        try {
            startActivityForResult(intent, REQ_SET_LOCAL_PATH);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}

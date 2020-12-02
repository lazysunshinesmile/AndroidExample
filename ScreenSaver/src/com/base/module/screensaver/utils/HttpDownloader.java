/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.utils.HttpDownloader.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-9-4
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

/**
 * ClassName:HttpDownloader
 * @Description: 
 */

import com.base.module.screensaver.ScreenSaverManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.gs.nvram.NvramManager;
import com.gs.nvram.NvramKey;
import android.text.TextUtils;
import android.util.Log;

public class HttpDownloader {
    private static URL url = null;
    private static long size = 0;

    public static String downStr(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;

        try {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * -1: download error 0: download success 1: file already exist
     */
    public static int downFile(String urlStr, String path, String fileName) {
        InputStream inputStream = null;
        try {
            if (FileUtils.isFileExist(path + fileName)) {
                return 1;
            } else {
                inputStream = getInputStreamFromUrl(urlStr);
                File resultFile = FileUtils.write2SDFromInput(path, fileName, inputStream, size);
                if (resultFile == null) {
                    return -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int downFileOverWrite(String urlStr, String path, String fileName) {
        InputStream inputStream = null;
        try {
            if (!FileUtils.isFileExist(path)) {
                FileUtils.creatDir(path);
            }
            if (FileUtils.isFileExist(path + fileName)) {
                FileUtils.deleteFile(path + fileName);
            }
            inputStream = getInputStreamFromUrl(urlStr);
            if (size > 5 * 1024 * 1024) {
                return ScreenSaverManager.ERROR_PIC_TOO_BIG;
            }else if (size >= FileUtils.getSpaceSize(ScreenSaverManager.GXP2200_USER_DATA)) {
                return ScreenSaverManager.ERROR_NOT_ENOUGH_SPACE;
            }
            File resultFile = FileUtils.write2SDFromInput(path, fileName, inputStream, size);
            if (resultFile == null) {
                return ScreenSaverManager.ERROR_OTHER;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ScreenSaverManager.ERROR_OTHER;
        } finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ScreenSaverManager.NO_ERROR;
    }

    /**
     * get input stream from url
     */
    public static InputStream getInputStreamFromUrl(String urlStr) throws MalformedURLException, IOException {
        url = new URL(urlStr);
        // create http connection
        HttpURLConnection urlConn = null;

        if (url.getProtocol().toLowerCase().equals("https")) {
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            trustAllHosts(https);
            https.setConnectTimeout(20000);
            https.setReadTimeout(50000);
            https.setHostnameVerifier(DO_NOT_VERIFY);
            urlConn = https;
        } else {
            urlConn = (HttpURLConnection) url.openConnection();
            String userAgent = NvramManager.instance().get(NvramKey.Generic.HTTP_OR_HTTPS_USER_AGENT);
            String tmpUserAgent = NvramManager.instance().get(":user_agent");
            if (userAgent == null || userAgent.length() == 0) {
                userAgent = tmpUserAgent;
            }

            if (userAgent != null && userAgent.length() > 0) {
                urlConn.setRequestProperty("User-Agent", userAgent);
            }
        }

        InputStream inputStream= urlConn.getInputStream();
        size = urlConn.getContentLength();
        return inputStream;
    }
    /** 
     * add by yyzhu
     * @param uripath
     * @return
     */

    public static boolean containsFile(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Pattern p = Pattern.compile("(\\/+\\w+\\.\\w+)$");
        Matcher m = p.matcher(url);
        return m.find();
    }

    public static boolean getImageSrc(String uripath, List<String> imageSrcList) {
        try {
            InputStream inputStream = getInputStreamFromUrl(uripath);

            String htmlCode="";

            htmlCode =getContentFromIn(inputStream);
            // getImgStr(htmlCode);
            String patternString =  "(?i)href\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+"
                    + "(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg"
                    + "|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>";
            Pattern p = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(htmlCode);
            String quote = null;
            String src = null;
            while (m.find()) {
                quote = m.group(1);
                src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
                if(src.startsWith("http")||src.startsWith("https")){
                    imageSrcList.add(src);
                }else{
                    src = uripath+src;
                    imageSrcList.add(src);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getContentFromIn(InputStream in) {
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();
        try {
            if (null != in) {
                br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
        return content.toString();
    }

    //add by yyzhu end


    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust all hosts - do not check for any certificate
     */
    private static void trustAllHosts(HttpsURLConnection https) {
        // Create a trust manager that does not validate certificate chains
        // Android uses the certificate information mechanism X509
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                    }

                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            https.setSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * add by yyzhu
     */
    public static String getFullUrl(String url) {
        //Handle prefix.
        StringBuilder apBuilder =null;
        if(isIPv6(url)){
            if(!url.startsWith("[") || !url.endsWith("]")){
                String pattern = "([-+*/^()\\]\\[])";
                url = url.replaceAll(pattern, "");
                apBuilder = new StringBuilder();
                apBuilder.append("[");
                apBuilder.append(url);
                apBuilder.append("]");
            }
        }
        if(apBuilder!=null){
            url = apBuilder.toString();
        }
        if (!containsProtocol(url)) {
            url = addProtocolIfNeeded(url);
        }
        if(!containsFile(url)&& !url.endsWith("/")){
            url = url+"/";  
        }
        return url;
    }

    public static boolean isIPv6(String address){
        String pattern = "([-+*/^()\\]\\[])";
        if(address.startsWith("[") && address.endsWith("]")){
            address = address.replaceAll(pattern, "");
        }
        boolean result = false;
        String regHex = "(\\p{XDigit}{1,4})";
        String regIPv6Full = "^(" + regHex + ":){7}" + regHex + "$";
        String regIPv6AbWithColon = "^(" + regHex + "(:|::)){0,6}" + regHex
                + "$";
        String regIPv6AbStartWithDoubleColon = "^(" + "::(" + regHex
                + ":){0,5}" + regHex + ")$";
        String regIPv6 = "^(" + regIPv6Full + ")|("
                + regIPv6AbStartWithDoubleColon + ")|(" + regIPv6AbWithColon
                + ")$";
        if (address.indexOf(":") != -1)
        {
            if (address.length() <= 39)
            {
                String addressTemp = address;
                int doubleColon = 0;
                while (addressTemp.indexOf("::") != -1)
                {
                    addressTemp = addressTemp.substring(addressTemp
                            .indexOf("::") + 2, addressTemp.length());
                    doubleColon++;
                }
                if (doubleColon <= 1)
                {
                    result = address.matches(regIPv6);
                }
            }
        }
        return result;
    }

    public static boolean containsProtocol(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        Pattern p = Pattern.compile("^\\w+://");
        Matcher m = p.matcher(url);
        return m.find();
    }

    public static String addProtocolIfNeeded(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        return "http://" + url;
    }
    //add by yyzhu end
}

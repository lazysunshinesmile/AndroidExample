/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.utils.FileUtils.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-9-3
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

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ClassName:FileUtils
 * 
 * @Description:
 */

public class FileUtils {
    /**
     * judge whether there is enough block for down load.
     * 
     * @param downloadSize
     * @return true means enough, else don't enough.
     */
    public static boolean isEnoughForDownload(long downloadSize) {
        // SDcard space left
        long spaceLeft = getSpaceSize(Environment.getExternalStorageDirectory().getAbsolutePath());

        MyLog.d("ray", "downloadSize = " + downloadSize);

        if (spaceLeft < downloadSize) {
            return false;
        }

        return true;
    }

    public static long getSpaceSize(String path) {
        StatFs statFs = new StatFs(path);

        // the path block count
        int blockCounts = statFs.getBlockCount();
        MyLog.d("ray", "blockCounts = " + blockCounts);

        // the path Available block count
        int avCounts = statFs.getAvailableBlocks();
        MyLog.d("ray", "avCounts = " + avCounts);

        // block size
        long blockSize = statFs.getBlockSize();
        MyLog.d("ray", "blockSize = " + blockSize);

        // the path space left
        long spaceLeft = avCounts * blockSize;
        MyLog.d("ray", "spaceLeft = " + spaceLeft);

        return spaceLeft;
    }

    public static boolean isSupportImage(String filePath) {
        FileType fileType = null;
        try {
            fileType = FileTypeJudge.getType(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (fileType) {
        case BMP:
        case JPEG:
        case PNG:
        case GIF:
            return true;
        case UNKNOWN:
            return false;
        default:
            break;
        }
        return false;
    }

    public static File creatFile(String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        return file;
    }

    public static File creatDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdir();
        return dir;
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static File write2SDFromInput(String path, String fileName, InputStream input, long size) {
        File file = null;
        OutputStream output = null;
        try {
            // write InputStream
            MyLog.i("FileUtils", "size = " + size);
            creatDir(path);
            file = creatFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            long curLen = 0;
            int len = 0;
            while ((len = input.read(buffer)) != -1 && curLen < size) {
                curLen += len;
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @Description:
     * @param string
     * @Return_Type :void
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }
}

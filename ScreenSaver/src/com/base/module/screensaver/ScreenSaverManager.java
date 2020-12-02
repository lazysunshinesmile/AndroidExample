/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.ScreenSaverManager.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-9-5
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
package com.base.module.screensaver;
/**
 * ClassName:ScreenSaverManager
 * @Description: 
 */

public final class ScreenSaverManager {
    public static final int ERROR_OTHER = -1;
    public static final int NO_ERROR = 0;
    public static final int ERROR_PIC_TOO_BIG = 1;
    public static final int ERROR_NOT_A_PIC = 2;
    public static final int ERROR_NOT_SUPPORT = 3;
    public static final int ERROR_IO_EXCEPTION = 4;
    public static final int ERROR_CONN_EXCEPTION = 5;
    public static final int ERROR_NOT_ENOUGH_SPACE = 6;
    
    public static final int ERROR_NO_IMAGE_IN_FOLDER = 101;
    
    public static final String GXP2200_USER_DATA = "/sdcard/";
    public static final String TMP_PATH = "/sdcard/screensaver/";
    public static final String TMP_NAME = "tmp_image";
}


/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.receiver.BootReceiver.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-1-10
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
package com.base.module.screensaver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * ClassName:BootReceiver
 * @Description: 
 */

public class BootReceiver extends BroadcastReceiver {

	/**
	 * @Description:
	 * @param context
	 * @param intent
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent1 = new Intent();
		intent1.setAction("com.base.module.action.SCREENSAVER");
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		Bundle extras = new Bundle();
		extras.putBoolean("isafterboot", true);
		intent1.putExtras(extras);
	//	context.startActivity(intent1);
	}
}


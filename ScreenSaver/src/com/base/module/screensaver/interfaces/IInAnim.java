/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.interfaces.IInAnim.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-1-5
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
package com.base.module.screensaver.interfaces;

import android.view.animation.Animation;

/**
 * ClassName:IInAnim
 * 
 * @Description:
 */

public interface IInAnim {
	String[] mMethods = { "inAlpha", "inAlphaRotate", "inAlphaScale1",
			"inAlphaScale2", "inAlphaScaleRotate", "inAlphaScaleTanslate",
			"inAlphaScaleTanslateRotate", "inAlphaTanslate",
			"inAlphaTanslateRotate", "inAlphaScaleToBottom",
			"inAlphaScaleToLeft", "inAlphaScaleToRight", "inAlphaScaleToTop",
			"inAlphaScaleToLeftBottom", "inAlphaScaleToLeftTop",
			"inAlphaScaleToRightTop", "inAlphaScaleToRightBottom",
			"inAlphaSlideToBottom", "inAlphaSlideToTop", "inAlphaSlideToLeft",
			"inAlphaSlideToRight", "inAlphaSlideToLeftBottom",
			"inAlphaSlideToLeftTop", "inAlphaSlideToRightBottom",
			"inAlphaSlideToRightTop", "inShake", "inWaveScale" };

	public Animation inAlpha();

	public Animation inAlphaRotate();

	public Animation inAlphaScale1();

	public Animation inAlphaScale2();

	public Animation inAlphaScaleRotate();

	public Animation inAlphaScaleTanslate();

	public Animation inAlphaScaleTanslateRotate();

	public Animation inAlphaTanslate();

	public Animation inAlphaTanslateRotate();

	public Animation inAlphaScaleToBottom();

	public Animation inAlphaScaleToLeft();

	public Animation inAlphaScaleToRight();

	public Animation inAlphaScaleToTop();

	public Animation inAlphaScaleToLeftBottom();

	public Animation inAlphaScaleToLeftTop();

	public Animation inAlphaScaleToRightTop();

	public Animation inAlphaScaleToRightBottom();

	public Animation inAlphaSlideToBottom();

	public Animation inAlphaSlideToTop();

	public Animation inAlphaSlideToLeft();

	public Animation inAlphaSlideToRight();

	public Animation inAlphaSlideToLeftBottom();

	public Animation inAlphaSlideToLeftTop();

	public Animation inAlphaSlideToRightBottom();

	public Animation inAlphaSlideToRightTop();

	public Animation inShake();

	public Animation inWaveScale();
}

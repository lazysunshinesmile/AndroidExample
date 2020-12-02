/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.interfaces.IOutAnim.java
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
 * ClassName:IOutAnim
 * 
 * @Description:
 */

public interface IOutAnim {
	String[] mMethods = { "outAlpha", "outAlphaRotate", "outAlphaScale1",
			"outAlphaScale2", "outAlphaScaleRotate", "outAlphaScaleTanslate",
			"outAlphaScaleTanslateRotate", "outAlphaTanslate",
			"outAlphaTanslateRotate", "outAlphaScaleToBottom",
			"outAlphaScaleToLeft", "outAlphaScaleToRight",
			"outAlphaScaleToTop", "outAlphaScaleToLeftBottom",
			"outAlphaScaleToLeftTop", "outAlphaScaleToRightTop",
			"outAlphaScaleToRightBottom", "outAlphaSlideToBottom",
			"outAlphaSlideToTop", "outAlphaSlideToLeft",
			"outAlphaSlideToRight", "outAlphaSlideToLeftBottom",
			"outAlphaSlideToLeftTop", "outAlphaSlideToRightBottom",
			"outAlphaSlideToRightTop", "outHyperspace" };

	public Animation outAlpha();

	public Animation outAlphaRotate();

	public Animation outAlphaScale1();

	public Animation outAlphaScale2();

	public Animation outAlphaScaleRotate();

	public Animation outAlphaScaleTanslate();

	public Animation outAlphaScaleTanslateRotate();

	public Animation outAlphaTanslate();

	public Animation outAlphaTanslateRotate();

	public Animation outAlphaScaleToBottom();

	public Animation outAlphaScaleToLeft();

	public Animation outAlphaScaleToRight();

	public Animation outAlphaScaleToTop();

	public Animation outAlphaScaleToLeftBottom();

	public Animation outAlphaScaleToLeftTop();

	public Animation outAlphaScaleToRightTop();

	public Animation outAlphaScaleToRightBottom();

	public Animation outAlphaSlideToBottom();

	public Animation outAlphaSlideToTop();

	public Animation outAlphaSlideToLeft();

	public Animation outAlphaSlideToRight();

	public Animation outAlphaSlideToLeftBottom();

	public Animation outAlphaSlideToLeftTop();

	public Animation outAlphaSlideToRightBottom();

	public Animation outAlphaSlideToRightTop();

	public Animation outHyperspace();
}

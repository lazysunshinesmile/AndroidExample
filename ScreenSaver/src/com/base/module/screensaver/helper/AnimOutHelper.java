/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.helper.AnimOutHelper.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-1-4
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
package com.base.module.screensaver.helper;

import com.base.module.screensaver.interfaces.IOutAnim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ClassName:AnimOutHelper
 * 
 * @Description:
 */

public class AnimOutHelper extends AnimHelper implements IOutAnim {
	/**
	 * @Description:
	 * @param context
	 */
	public AnimOutHelper(Context context, long outDuration, long outDuration1,
			long outDuration2) {
		super(context);
		mOutDuration = outDuration;
		mOutDuration1 = outDuration1;
		mOutDuration2 = outDuration2;
	}

	public Animation getRandAnim() {
		int index = getRandIndex(mMethods.length);
		Method method = null;
		try {
			method = getClass().getDeclaredMethod(mMethods[index],
					(Class<?>[]) null);
			return (Animation) method.invoke(this, (Object[]) null);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * R.anim.out_alpha, R.anim.out_alpha_rotate, R.anim.out_alpha_scale,
	 * R.anim.out_alpha_scale_rotate, R.anim.out_alpha_scale_translate,
	 * R.anim.out_alpha_scale_translate_rotate, R.anim.out_alpha_translate,
	 * R.anim.out_alpha_translate_rotate, R.anim.out_rotate_scale2rotate_scale,
	 */
	public Animation outAlpha() {
		Animation anim = getAlphaOutAnim(mOutDuration, true);
		return anim;
	}

	public Animation outAlphaRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getRotateAnim(mOutDuration, 0, -360, 0.5f, 0.5f,
				true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScale1() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 2.0f,
				0.5f, 0.5f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScale2() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 2.0f, 0.0f,
				0.5f, 0.5f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 0.0f,
				0.5f, 0.5f, true));
		animSet.addAnimation(getRotateAnim(mOutDuration, 0, -360, 0.5f, 0.5f,
				true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleTanslate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 0.0f,
				0.5f, 0.5f, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 0.35f, 0.10f,
				true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleTanslateRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 0.0f,
				0.5f, 0.5f, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 0.35f, 0.10f,
				true));
		animSet.addAnimation(getRotateAnim(mOutDuration, 0, -360, 0.5f, 0.5f,
				true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaTanslate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 0.35f, 0.10f,
				true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaTanslateRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 0.35f, 0.10f,
				true));
		animSet.addAnimation(getRotateAnim(mOutDuration, 0, -360, 0.5f, 0.5f,
				true));
		animSet.setFillAfter(true);
		return animSet;
	}

	/*
	 * 
	 * R.anim.out_scale_to_bottom, R.anim.out_scale_to_left,
	 * R.anim.out_scale_to_right, R.anim.out_scale_to_top,
	 * R.anim.out_scale_to_left_bottom, R.anim.out_scale_to_left_top,
	 * R.anim.out_scale_to_right_bottom, R.anim.out_scale_to_right_top,
	 */
	public Animation outAlphaScaleToBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 1.0f, 0.0f,
				0.5f, 1.0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleToLeft() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 0.5f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleToRight() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 0.5f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleToTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 1.0f, 0.0f,
				0.5f, 0.0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleToLeftBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 0.0f,
				0.0f, 1.0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleToLeftTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleToRightTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaScaleToRightBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration, 1.0f, 1.0f, 0.0f, 0.0f,
				1.0f, 1.0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	/*
	 * R.anim.out_slide_to_bottom, R.anim.out_slide_to_left,
	 * R.anim.out_slide_to_right, R.anim.out_slide_to_top,
	 * R.anim.out_slide_to_left_bottom, R.anim.out_slide_to_left_top,
	 * R.anim.out_slide_to_right_bottom, R.anim.out_slide_to_right_top
	 */

	public Animation outAlphaSlideToBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 0f, 1f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaSlideToTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 0f, -1f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaSlideToLeft() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, -1f, 0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaSlideToRight() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 1f, 0f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaSlideToLeftBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, -1f, 1f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaSlideToLeftTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, -1f, -1f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaSlideToRightBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 1f, 1f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outAlphaSlideToRightTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getTransAnim(mOutDuration, 0f, 0f, 1f, -1f, true));
		animSet.setFillAfter(true);
		return animSet;
	}

	public Animation outHyperspace() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaOutAnim(mOutDuration, true));
		animSet.addAnimation(getScaleAnim(mOutDuration1, 1.0f, 1.0f, 0.5f,
				0.5f, 0.5f, 0.5f, true));

		Animation scaleAnim2 = getScaleAnim(mOutDuration2, 1.0f, 1.0f, 0.5f,
				0.5f, 0.5f, 0.5f, true);
		scaleAnim2.setStartOffset(mOutDuration1);
		animSet.addAnimation(scaleAnim2);
		Animation rotateAnim = getRotateAnim(mOutDuration2, 0, -360, 0.5f,
				0.5f, true);
		rotateAnim.setStartOffset(mOutDuration1);
		animSet.addAnimation(rotateAnim);

		animSet.setFillAfter(true);
		return animSet;
	}
}

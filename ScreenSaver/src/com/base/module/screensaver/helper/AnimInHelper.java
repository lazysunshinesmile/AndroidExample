/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.helper.AnimInHelper.java
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

import com.base.module.screensaver.interfaces.IInAnim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ClassName:AnimInHelper
 * 
 * @Description:
 */

public class AnimInHelper extends AnimHelper implements IInAnim {
	/**
	 * @Description:
	 * @param context
	 */
	public AnimInHelper(Context context, long inDuration, long inDuration1,
			long inDuration2) {
		super(context);
		mInDuration = inDuration;
		mInDuration1 = inDuration1;
		mInDuration2 = inDuration2;
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
	 * R.anim.in_alpha, R.anim.in_alpha_rotate, R.anim.in_alpha_scale,
	 * R.anim.in_alpha_scale_rotate, R.anim.in_alpha_scale_translate,
	 * R.anim.in_alpha_scale_translate_rotate, R.anim.in_alpha_translate,
	 * R.anim.in_alpha_translate_rotate, R.anim.in_rotate_scale2rotate_scale,
	 */
	public Animation inAlpha() {
		return getAlphaInAnim(mInDuration, false);
	}

	public Animation inAlphaRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getRotateAnim(mInDuration, 0, 360, 0.5f, 0.5f,
				false));
		return animSet;
	}

	public Animation inAlphaScale1() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration1, 1.0f, 1.0f, 2.0f, 0.5f,
				0.5f, 0.5f, false));
		Animation anim2 = getScaleAnim(mInDuration2, 1.0f, 1.0f, 0.5f, 2.0f,
				0.5f, 0.5f, false);
		anim2.setStartOffset(mInDuration1);
		animSet.addAnimation(anim2);
		return animSet;
	}

	public Animation inAlphaScale2() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration1, 1.0f, 1.0f, 0.5f, 2.0f,
				0.5f, 0.5f, false));
		Animation anim2 = getScaleAnim(mInDuration2, 1.0f, 1.0f, 2.0f, 0.5f,
				0.5f, 0.5f, false);
		anim2.setStartOffset(mInDuration1);
		animSet.addAnimation(anim2);
		return animSet;
	}

	public Animation inAlphaScaleRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 0.0f, 1.0f, 1.0f,
				0.5f, 0.5f, false));
		animSet.addAnimation(getRotateAnim(mInDuration, 0, 360, 0.5f, 0.5f,
				false));
		return animSet;
	}

	public Animation inAlphaScaleTanslate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 0.0f, 1.0f, 1.0f,
				0.5f, 0.5f, false));
		animSet.addAnimation(getTransAnim(mInDuration, 0.35f, 0.10f, 0f, 0f,
				false));
		return animSet;
	}

	public Animation inAlphaScaleTanslateRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 0.0f, 1.0f, 1.0f,
				0.5f, 0.5f, false));
		animSet.addAnimation(getTransAnim(mInDuration, 0.35f, 0.10f, 0f, 0f,
				false));
		animSet.addAnimation(getRotateAnim(mInDuration, 0, 360, 0.5f, 0.5f,
				false));
		return animSet;
	}

	public Animation inAlphaTanslate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, 0.35f, 0.10f, 0f, 0f,
				false));
		return animSet;
	}

	public Animation inAlphaTanslateRotate() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, 0.35f, 0.10f, 0f, 0f,
				false));
		animSet.addAnimation(getRotateAnim(mInDuration, 0, 360, 0.5f, 0.5f,
				false));
		return animSet;
	}

	/*
	 * R.anim.in_scale_from_bottom, R.anim.in_scale_from_left,
	 * R.anim.in_scale_from_right, R.anim.in_scale_from_top,
	 * R.anim.in_scale_from_left_bottom, R.anim.in_scale_from_left_top,
	 * R.anim.in_scale_from_right_bottom, R.anim.in_scale_from_right_top,
	 */
	public Animation inAlphaScaleToBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 1.0f, 0.0f, 1.0f, 1.0f,
				0.5f, 1.0f, false));
		return animSet;
	}

	public Animation inAlphaScaleToLeft() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 1.0f, 1.0f, 1.0f,
				0.0f, 0.5f, false));
		return animSet;
	}

	public Animation inAlphaScaleToRight() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 0.5f, false));
		return animSet;
	}

	public Animation inAlphaScaleToTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 1.0f, 0.0f, 1.0f, 1.0f,
				0.5f, 0.0f, false));
		return animSet;
	}

	public Animation inAlphaScaleToLeftBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, false));
		return animSet;
	}

	public Animation inAlphaScaleToLeftTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, false));
		return animSet;
	}

	public Animation inAlphaScaleToRightTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 0.0f, false));
		return animSet;
	}

	public Animation inAlphaScaleToRightBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration, 0.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 1.0f, false));
		return animSet;
	}

	/*
	 * R.anim.in_slide_from_bottom, R.anim.in_slide_from_left,
	 * R.anim.in_slide_from_right, R.anim.in_slide_from_top,
	 * R.anim.in_slide_from_left_bottom, R.anim.in_slide_from_left_top,
	 * R.anim.in_slide_from_right_bottom, R.anim.in_slide_from_right_top,
	 */
	public Animation inAlphaSlideToBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, 0f, 1f, 0f, 0f, false));
		return animSet;
	}

	public Animation inAlphaSlideToTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, 0f, -1f, 0f, 0f, false));
		return animSet;
	}

	public Animation inAlphaSlideToLeft() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, -1f, 0f, 0f, 0f, false));
		return animSet;
	}

	public Animation inAlphaSlideToRight() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, 1f, 0f, 0f, 0f, false));
		return animSet;
	}

	public Animation inAlphaSlideToLeftBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, -1f, 1f, 0f, 0f, false));
		return animSet;
	}

	public Animation inAlphaSlideToLeftTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, -1f, -1f, 0f, 0f, false));
		return animSet;
	}

	public Animation inAlphaSlideToRightBottom() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, 1f, 1f, 0f, 0f, false));
		return animSet;
	}

	public Animation inAlphaSlideToRightTop() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getTransAnim(mInDuration, 1f, -1f, 0f, 0f, false));
		return animSet;
	}

	/*
	 * R.anim.shake, R.anim.wave_scale
	 */

	public Animation inShake() {
		Animation anim = getTransAnim(mInDuration, 0f, 0f, 1f, 0f, false);
		Interpolator inter = new CycleInterpolator(1f);
		anim.setInterpolator(inter);
		return anim;
	}

	public Animation inWaveScale() {
		AnimationSet animSet = new AnimationSet(mContext, null);
		animSet.addAnimation(getAlphaInAnim(mInDuration, false));
		animSet.addAnimation(getScaleAnim(mInDuration1, 0.0f, 0.0f, 2.0f, 2.0f,
				0.5f, 0.5f, false));
		Animation anim2 = getScaleAnim(mInDuration2, 1.0f, 1.0f, 0.5f, 0.5f,
				0.5f, 0.5f, false);
		anim2.setStartOffset(mInDuration1);
		animSet.addAnimation(anim2);
		return animSet;
	}

}

/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.helper.AnimHelper.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2011-12-31
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

import com.base.module.screensaver.R;

import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * The Class ScreenGuardAnimHelper.
 * 
 */
public abstract class AnimHelper {
	protected Context mContext;
	protected long mOutDuration, mOutDuration1, mOutDuration2;
	protected long mInDuration, mInDuration1, mInDuration2;

	public AnimHelper(Context context) {
		mContext = context;
	}

	/**
	 * Gets the rand anim.
	 * 
	 * @param context
	 *            the context
	 * @return the rand anim
	 */
	@SuppressWarnings("unused")
	private Animation getRandInAnim(Context context) {
		int index;
		int[] resIds = new int[] { R.anim.in_alpha, R.anim.in_alpha_rotate,
				R.anim.in_alpha_scale, R.anim.in_alpha_scale_rotate,
				R.anim.in_alpha_scale_translate,
				R.anim.in_alpha_scale_translate_rotate,
				R.anim.in_alpha_translate, R.anim.in_alpha_translate_rotate,
				R.anim.in_rotate_scale2rotate_scale,

				R.anim.in_alpha, R.anim.in_alpha_rotate, R.anim.in_alpha_scale,
				R.anim.in_alpha_scale_rotate, R.anim.in_alpha_scale_translate,
				R.anim.in_alpha_scale_translate_rotate,
				R.anim.in_alpha_translate, R.anim.in_alpha_translate_rotate,
				R.anim.in_rotate_scale2rotate_scale,

				R.anim.in_scale_from_bottom, R.anim.in_scale_from_left,
				R.anim.in_scale_from_right, R.anim.in_scale_from_top,
				R.anim.in_scale_from_left_bottom,
				R.anim.in_scale_from_left_top,
				R.anim.in_scale_from_right_bottom,
				R.anim.in_scale_from_right_top,

				R.anim.in_slide_from_bottom, R.anim.in_slide_from_left,
				R.anim.in_slide_from_right, R.anim.in_slide_from_top,
				R.anim.in_slide_from_left_bottom,
				R.anim.in_slide_from_left_top,
				R.anim.in_slide_from_right_bottom,
				R.anim.in_slide_from_right_top,

				R.anim.shake, R.anim.wave_scale };
		index = getRandIndex(resIds.length);
		// index = 19;

		Log.i("tag", resIds[index] + ", " + index);
		int res = resIds[index];

		// res = R.anim.shake;

		Animation anim = AnimationUtils.loadAnimation(context, res);

		return anim;
	}

	/**
	 * Gets the rand out anim.
	 * 
	 * @param context
	 *            the context
	 * @return the rand out anim
	 */
	@SuppressWarnings("unused")
	private Animation getRandOutAnim(Context context) {
		int index;
		int[] resIds = new int[] { R.anim.out_alpha, R.anim.out_alpha_rotate,
				R.anim.out_alpha_scale, R.anim.out_alpha_scale_rotate,
				R.anim.out_alpha_scale_translate,
				R.anim.out_alpha_scale_translate_rotate,
				R.anim.out_alpha_translate, R.anim.out_alpha_translate_rotate,

				R.anim.out_alpha, R.anim.out_alpha_rotate,
				R.anim.out_alpha_scale, R.anim.out_alpha_scale_rotate,
				R.anim.out_alpha_scale_translate,
				R.anim.out_alpha_scale_translate_rotate,
				R.anim.out_alpha_translate, R.anim.out_alpha_translate_rotate,

				R.anim.out_scale_to_bottom, R.anim.out_scale_to_left,
				R.anim.out_scale_to_right, R.anim.out_scale_to_top,
				R.anim.out_scale_to_left_bottom, R.anim.out_scale_to_left_top,
				R.anim.out_scale_to_right_bottom,
				R.anim.out_scale_to_right_top,

				R.anim.out_slide_to_bottom, R.anim.out_slide_to_left,
				R.anim.out_slide_to_right, R.anim.out_slide_to_top,
				R.anim.out_slide_to_left_bottom, R.anim.out_slide_to_left_top,
				R.anim.out_slide_to_right_bottom,
				R.anim.out_slide_to_right_top, };
		index = getRandIndex(resIds.length);
		// index = 19;

		Log.i("tag", resIds[index] + ", " + index);
		int res = resIds[index];

		// res = R.anim.wave_scale;

		Animation anim = AnimationUtils.loadAnimation(context, res);
		return anim;
	}

	/**
	 * Gets the rand index.
	 * 
	 * @param maxIndex
	 *            the max index
	 * @return the rand index
	 */
	public static int getRandIndex(int maxIndex) {
		return (int) (Math.random() * maxIndex);
	}

	public Animation getAlphaOutAnim(long duration, boolean fillAfter) {
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.out_alpha);
		anim.setDuration(duration);
		anim.setFillAfter(fillAfter);
		return anim;
	}

	public Animation getAlphaInAnim(long duration, boolean fillAfter) {
		Animation anim = AnimationUtils
				.loadAnimation(mContext, R.anim.in_alpha);
		anim.setDuration(duration);
		anim.setFillAfter(fillAfter);
		return anim;
	}

	public Animation getScaleAnim(long duration, float fromX, float fromY,
			float toX, float toY, float pivotX, float pivotY, boolean fillAfter) {
		Animation anim = new ScaleAnimation(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF,
				pivotY);
		anim.setDuration(duration);
		anim.setFillAfter(fillAfter);
		return anim;
	}

	public Animation getTransAnim(long duration, float fromXDelta,
			float fromYDelta, float toXDelta, float toYDelta, boolean fillAfter) {
		Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				fromXDelta, Animation.RELATIVE_TO_SELF, toXDelta,
				Animation.RELATIVE_TO_SELF, fromYDelta,
				Animation.RELATIVE_TO_SELF, toYDelta);
		anim.setDuration(duration);
		anim.setFillAfter(fillAfter);
		return anim;
	}

	public Animation getRotateAnim(long duration, float fromDegree,
			float toDegree, float pivotX, float pivotY, boolean fillAfter) {
		Animation anim = new RotateAnimation(fromDegree, toDegree,
				Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF,
				pivotY);
		anim.setDuration(duration);
		anim.setFillAfter(fillAfter);
		return anim;
	}

}

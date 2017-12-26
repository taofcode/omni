package com.mobilebanking.core.uiutils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

/**
 * <b>Author</b>:
 * <p>
 * Created by <b>Sasa Ilic</b> on Nov 16, 2015
 * </p>
 */
public class AnimationUtils {

	/**
	 * Animates view either in or out of screen
	 * 
	 * @param activity
	 * @param view
	 * @param animationIn
	 *            true for animation in, otherwise false
	 * @return
	 */
	public static Animator createAnimation(Activity activity, final ViewGroup view,
			boolean animationIn, final boolean hideOnAnimationEng,
			final boolean showOnAnimationStart) {

		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		final int width = size.x;

		ObjectAnimator layoutAnimator = null;

		if (animationIn) {
			layoutAnimator = ObjectAnimator.ofFloat(view, "translationX", width, 0);
		} else {
			layoutAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, -width);
		}
		layoutAnimator.setDuration(300);

		Animator animation = new AnimatorSet();
		((AnimatorSet) animation).playTogether(layoutAnimator);

		animation.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				if (showOnAnimationStart) {
					view.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (hideOnAnimationEng) {
					view.setVisibility(View.GONE);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		return animation;
	}
}

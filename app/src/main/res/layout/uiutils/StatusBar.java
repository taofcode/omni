/**
 * 
 * MobileBanking-core StatusBar.java
 * 
 * Created by Sasa Ilic on Sep 2, 2015
 * 
 * Email: sassa.ilic@gmail.com
 * 
 */
package com.mobilebanking.core.uiutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 * <b>Author</b>:
 * <p>
 * Created by <b>Sasa Ilic</b> on Sep 2, 2015
 * </p>
 */
public class StatusBar {

	@SuppressLint("InlinedApi")
	public static void changeColour(Activity activity, int colourResource) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			Window window = activity.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(activity.getResources().getColor(colourResource));
		}
	}

}

/**
 * 
 * mobile-banking-core HiddenEditText.java
 * 
 * Created by Sasa Ilic on Nov 6, 2015
 * 
 * Email: sassa.ilic@gmail.com
 * 
 */
package com.mobilebanking.core.uiutils;

import com.mobilebanking.core.activity.CoreLogin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * 
 * <b>Author</b>:
 * <p>
 * Created by <b>Sasa Ilic</b> on Nov 6, 2015
 * </p>
 */
public class CustomEditText extends EditText {
	/*
	 * Must use this constructor in order for the layout files to instantiate the class
	 * properly
	 */
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				CoreLogin.clearFocus();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.onKeyPreIme(keyCode, event);
	}

}
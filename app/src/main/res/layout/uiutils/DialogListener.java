package com.mobilebanking.core.uiutils;

import android.content.Intent;

public interface DialogListener {
	public void onDialogResult(int requestCode, int resultCode, Intent data);
}

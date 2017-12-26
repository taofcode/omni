package sbs.thundercraft.co.zw.sbsfreelancer.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import sbs.thundercraft.co.zw.sbsfreelancer.R;


public class WaitDialog extends DialogFragment {

	private static final String TAG = "WaitDialog";
	View view;
	boolean cancelButtonEnabled;
	String message;

	public WaitDialog() {
		cancelButtonEnabled = false;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.custom_wait_dialog, null);

		if (savedInstanceState != null) {
			cancelButtonEnabled = savedInstanceState.getBoolean("cancelButtonEnabled", cancelButtonEnabled);
			message = savedInstanceState.getString("message", null);
			if (message != null) {
				setMessage(message);
			}
		}

		setupCancelButton();

		builder.setView(view);
		this.setCancelable(false);
		return builder.create();
	}


	private void setupCancelButton() {
		if (cancelButtonEnabled) {
			Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

			if (cancelButton != null) {
				cancelButton.setVisibility(View.VISIBLE);
				cancelButton.setOnClickListener(new View.OnClickListener() {
													@Override
													public void onClick(View v) {
														 dismiss();
													}
												}
				);
			}
		}
	}

	public void setMessage(String message) {
		this.message = message;
		if (view != null) {
			TextView messageView = (TextView) view.findViewById(R.id.dialog_message);
			if (messageView != null) {
				messageView.setText(message);
			}
		}
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("cancelButtonEnabled", cancelButtonEnabled);
		outState.putString("message", message);
		super.onSaveInstanceState(outState);

	}
}
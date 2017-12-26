package com.mobilebanking.core.uiutils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobilebanking.core.R;

public class WarningDialog extends DialogFragment implements android.view.View.OnClickListener {

	public Button cancel;
	public String message, title;

	public interface DialogDismissListener {
		public void onDismiss();
	}

	public static final int REQUEST_DISMISS = 101;
	private DialogDismissListener listener;
	private boolean cancelable;
	private boolean isOk;

	public WarningDialog() {
	};

	public WarningDialog(String title, String message) {
		this.message = message;
		this.title = title;
	}

	public WarningDialog(String title, String message, boolean cancelable,
			DialogDismissListener listener) {
		this.message = message;
		this.title = title;
		this.listener = listener;
		this.cancelable = cancelable;
	}
	public WarningDialog(String title, String message, boolean cancelable,boolean isOk,
							  DialogDismissListener listener) {
		this.message = message;
		this.title = title;
		this.listener = listener;
		this.cancelable = cancelable;
		this.isOk = isOk;
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.custom_warning_dialog, null);
		builder.setView(view);

		if (savedInstanceState != null) {
			title = savedInstanceState.getString("title");
			message = savedInstanceState.getString("message");
		}
		setTitle(view, title);
		setMessage(view, message);
		cancel = (Button) view.findViewById(R.id.warning_cancel_button);
		if(isOk) {

			cancel.setText("OK");

		}
		cancel.setOnClickListener(this);
		if (cancelable) {
			setCancelable(false);
		}

		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void onSaveInstanceState(Bundle stateBundle) {
		stateBundle.putString("title", title);
		stateBundle.putString("message", message);
		super.onSaveInstanceState(stateBundle);
	}

	public void setTitle(View view, String newTitle) {
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		title.setText(newTitle);
	}

	public void setMessage(View view, String newMessage) {
		TextView message = (TextView) view.findViewById(R.id.dialog_message);
		message.setText(newMessage);
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onDismiss();
		}
		dismiss();
	}
}

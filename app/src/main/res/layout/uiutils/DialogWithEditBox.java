package com.mobilebanking.core.uiutils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobilebanking.core.R;
import com.mobilebanking.core.service.SmsReceiver;


public class DialogWithEditBox extends DialogFragment implements android.view.View.OnClickListener {
	
	private DialogListener parent;
	
	private boolean manualEntry;
	
	private Button confirm, cancel;
	private String message, title, messageAlternative;
	private EditText numberEditText;
	private ProgressBar progressBar;

	private View view;
	
	public static final int RESULT_CONFRIM = 1;
	public static final int RESULT_CANCEL = 0;	
	public static final int REQUEST_CONFRIM = 110;	
	public static final String VALUE = "value";

	private static final String TAG = "Dialog With edit box";
	
	private int targetCode;
	SmsReceiver smsReceiver;
	public DialogWithEditBox() {};
	
	public DialogWithEditBox(String title, String message, String messageAltrenative, boolean manualEntry) {
		this.message = message;
		this.messageAlternative = messageAltrenative;
		this.title = title;
		this.manualEntry = manualEntry;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.custom_dialog_edit_text, null);
		builder.setView(view);
		
		numberEditText = (EditText)view.findViewById(R.id.dialog_edit_text);
		confirm = (Button) view.findViewById(R.id.dialog_confirm_button);
		cancel = (Button) view.findViewById(R.id.dialog_cancel_button);
		progressBar = (ProgressBar) view.findViewById(R.id.dialog_progress);

		targetCode = getTargetRequestCode();
		
		if(savedInstanceState!=null){
			title = savedInstanceState.getString("title");
			message = savedInstanceState.getString("message");
			numberEditText.setText(savedInstanceState.getString("value"));
			manualEntry = savedInstanceState.getBoolean("manualEntry");
			messageAlternative = savedInstanceState.getString("messageAlternative");
			targetCode = savedInstanceState.getInt("targetCode", 0);

			if(manualEntry){
				progressBar.setVisibility(View.GONE);
				confirm.setText(getString(R.string.dialog_confirm));
				setMessage(view, messageAlternative);
				numberEditText.setVisibility(View.VISIBLE);
			} else {
				setMessage(view, message);
			}
			
		} else {
			setMessage(view, message);
		}
		
		setTitle(view, title);
		
		confirm.setOnClickListener(this);		
		cancel.setOnClickListener(this);
				
		this.setCancelable(false);
		// Create the AlertDialog object and return it
		return builder.create();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "Registering sms receiver...");
		if(smsReceiver==null){
			smsReceiver = new SmsReceiver();
		}
		getActivity().registerReceiver(smsReceiver , new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}
	
	public void onSaveInstanceState(Bundle stateBundle){
		stateBundle.putString("title", title);
		stateBundle.putString("message", message);
		stateBundle.putString("messageAlternative", messageAlternative);
		stateBundle.putString("value",numberEditText.getText().toString());
		stateBundle.putBoolean("manualEntry", manualEntry);
		stateBundle.putInt("targetCode", targetCode);
	}

	public void setTitle(View view, String newTitle) {
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		title.setText(newTitle);
	}

	public void setMessage(View view, String newMessage) {
		TextView message = (TextView) view.findViewById(R.id.dialog_message);
		message.setText(newMessage);
	}
	
	public void setManual(View view){

	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.dialog_confirm_button) {
			if(!manualEntry){
				manualEntry = true;
				progressBar.setVisibility(View.GONE);
				confirm.setText(getString(R.string.dialog_confirm));
				setMessage(view, messageAlternative);
				numberEditText.setVisibility(View.VISIBLE);
			} else {
				Intent intent = new Intent();
				intent.putExtra(VALUE, numberEditText.getText().toString());
				if(parent!=null){
					parent.onDialogResult(getTargetRequestCode(),RESULT_CONFRIM,intent);
				} else {
					((DialogListener) getActivity()).onDialogResult(targetCode,RESULT_CONFRIM,intent);
				}
				dismiss();
			}
		} else if (id == R.id.dialog_cancel_button) {
			if(parent!=null){
				parent.onDialogResult(targetCode,RESULT_CANCEL,null);
			} else {
				((DialogListener) getActivity()).onDialogResult(targetCode,RESULT_CANCEL,null);
			}
			dismiss();
		} else {
			
		}
	}

	public DialogListener getParent() {
		return parent;
	}

	public void setParent(DialogListener parent, int requestConfrim) {
		setTargetFragment(null, requestConfrim);
		this.parent = parent;
	}
	
	public void onPause(){
		super.onPause();
		if(smsReceiver!=null){
			try {
				Log.e(TAG, "Trying to unregister sms receiver...");
				getActivity().unregisterReceiver(smsReceiver);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Sms receiver is already unregistred!");
			}
		}
	}

}

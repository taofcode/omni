package com.mobilebanking.core.uiutils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobilebanking.core.R;
import com.mobilebanking.core.service.RequestType;
import com.mobilebanking.core.service.TransactionResponse;
import com.mobilebanking.core.service.TransactionResponse.ResponseCode;

import de.greenrobot.event.EventBus;

public class WaitDialog extends DialogFragment {
	
	private static final String TAG = "WaitDialog";
	View view;
	boolean cancelButtonEnabled;
	String message;
	
	public WaitDialog() {			
		cancelButtonEnabled = false;
	}

	public WaitDialog(boolean cancelButtonEnabled) {	
		this.cancelButtonEnabled = cancelButtonEnabled;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.custom_wait_dialog, null);
		
		if(savedInstanceState!=null){
			cancelButtonEnabled = savedInstanceState.getBoolean("cancelButtonEnabled", cancelButtonEnabled);
			message = savedInstanceState.getString("message", null);
			if(message!=null){
				setMessage(message);
			}
		}
		
		setupCancelButton();
		
		builder.setView(view);
		this.setCancelable(false);
		return builder.create();		
	}

	
	private void setupCancelButton(){
		if(cancelButtonEnabled){
			Button cancelButton = (Button)view.findViewById(R.id.cancel_button);
			
			if(cancelButton!=null){
				cancelButton.setVisibility(View.VISIBLE);
				cancelButton.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						EventBus.getDefault().post(new TransactionResponse(RequestType.CARD_READ_CANCEL));
					}
				} 
			);				
			}
		}
	}
	
	public void setMessage(String message){
		this.message = message;
		if(view!=null){
			TextView messageView = (TextView) view.findViewById(R.id.dialog_message);		
			if(messageView!=null){
				messageView.setText(message);
			}
		}
	}
	
	
	public boolean isCancelButtonEnabled() {
		return cancelButtonEnabled;
	}


	public void setCancelButtonEnabled(boolean cancelButtonEnabled) {
		this.cancelButtonEnabled = cancelButtonEnabled;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("cancelButtonEnabled", cancelButtonEnabled);
		outState.putString("message", message);
		super.onSaveInstanceState(outState);
	}
}

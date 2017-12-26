package com.mobilebanking.core.uiutils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilebanking.core.R;

import java.util.List;


public class ConfirmDialog extends DialogFragment implements android.view.View.OnClickListener {

    public Button confirm, cancel;
    public String message, title;
    private List<Pair<String, String>> confirmationData;

    public static final int RESULT_CONFRIM = 1;
    public static final int RESULT_CANCEL = 0;

    public static final int REQUEST_CONFRIM = 100;
    public static final int REQUEST_CONFRIM_STEP_TWO = 101;
    private DialogListener dialogListener;

    public ConfirmDialog() {
    }

    public ConfirmDialog(String title, String message) {
        this.message = message;
        this.title = title;
    }

    public ConfirmDialog(String title, String message, DialogListener dialogListener) {
        this(title, message);
        this.dialogListener = dialogListener;
    }

    public ConfirmDialog(String title, String message, List<Pair<String, String>> data) {
        this.message = message;
        this.title = title;
        this.confirmationData = data;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        int result = -1;

        if (viewId == R.id.dialog_confirm_button) {
            result = RESULT_CONFRIM;
        } else if (viewId == R.id.dialog_cancel_button) {
            result = RESULT_CANCEL;
        } else {
            //DO nothing
        }

        if (result != -1) {
            /**
             * If we have a dialog listener attached, retrun the results to that as this means
             * the confirmation was launched from an activity, otherwise get the target fragment
             * and return results there
             */

            if (dialogListener != null) {
                dialogListener.onDialogResult(getTargetRequestCode(), result, null);
            } else {
                getTargetFragment().onActivityResult(getTargetRequestCode(), result, null);
            }
            dismiss();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(view);

        if (savedInstanceState != null) {
            title = savedInstanceState.getString("title");
            message = savedInstanceState.getString("message");
        }
        setTitle(view, title);
        setMessage(view, message);

        confirm = (Button) view.findViewById(R.id.dialog_confirm_button);
        confirm.setOnClickListener(this);

        cancel = (Button) view.findViewById(R.id.dialog_cancel_button);
        cancel.setOnClickListener(this);


        if (confirmationData != null) {
            ViewGroup dataContainer = (ViewGroup) view.findViewById(R.id.result_data);

            for (Pair<String, String> pair : confirmationData) {
                LinearLayout dataValueView = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.completition_screen_data_item, (ViewGroup) dataContainer, false);
                TextView dataCaption = (TextView) dataValueView.findViewById(R.id.caption);
                TextView dataValue = (TextView) dataValueView.findViewById(R.id.value);

                dataCaption.setText(pair.first);
                dataValue.setText(pair.second);
                dataContainer.addView(dataValueView);
            }
        }
        // Create the AlertDialog object and return it

        this.setCancelable(false);
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

}
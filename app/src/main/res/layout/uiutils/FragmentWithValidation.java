package com.mobilebanking.core.uiutils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobilebanking.core.R;
import com.mobilebanking.core.fragment.CompletitionScreenCore;
import com.mobilebanking.core.service.InfoVault;
import com.mobilebanking.core.service.TransactionResponse;
import com.mobilebanking.core.service.entity.Receipt;
import com.mobilebanking.core.activity.CoreHome;
import de.greenrobot.event.EventBus;

public class FragmentWithValidation extends Fragment {

	protected static final String WAIT_DIALOG = "wait_dialog";
	protected boolean allowsBackPress;
	private boolean stickyEvent = false;

	public boolean checkEditTextIsNotNumeric(EditText text) {
		return text.getText().toString().matches("")
				|| text.getText().toString().matches("[a-zA-Z.? ]*");
	}

	public boolean checkOnlyEditTextIsNotNumeric(EditText text) {
		if (!text.getText().toString().matches("")) {
			return text.getText().toString().matches("[a-zA-Z.? ]*");
		}
		return false;
	}

	public boolean checkPinContainChange(EditText pin) {
		return checkEditTextIsNotNumeric(pin) || pin.getText().toString().length() < 4;
	}

	public boolean checkEditTextIsNotEmail(EditText email) {
		if (!email.getText().toString().matches("")) {
			return !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString())
					.matches();
		}
		return false;
	}
	public boolean checkStringName(String name) {
		char[] chars = name.toCharArray();

		for (char c : chars) {
			if(!Character.isLetter(c)) {
				return false;
			}
		}

		return true;
	}

	public boolean checkEditTextAirtimeNumber(EditText text) {
		return (text.getText().toString().startsWith("07")
				|| text.getText().toString().startsWith("2637")
				|| text.getText().toString().startsWith("+2637"))
				&& text.getText().toString().length() >= 10;
	}

	public boolean validateNotEmpty(Uri uri) {
		if (uri == null  ||  uri.equals(Uri.EMPTY))
			return false;

		if ((uri.getPath() == null) || (uri.getPath().toString() == null) || ("".equals(uri.getPath().toString().trim())))
		{

			return false;
		}


		return true;
	}
	public boolean validateNotEmpty(TextView editText)
	{
		if (editText == null)
			return false;

		if ((editText.getText() == null) || (editText.getText().toString() == null) || ("".equals(editText.getText().toString().trim())))
		{
			editText.setError(editText.getResources().getString(R.string.validation_error_empty_text));
			return false;
		}

		editText.setError(null);
		return true;
	}

	public boolean validateEmailAddress(TextView editText)
	{
		if (editText == null)
			return false;

		// This method validates if a valid email address has been entered, not whether the field
		// has been populated. Empty fields pass this validation check, to make this email field
		// required, call validateNotEmpty as well
		if ((editText.getText() == null) || (editText.getText().toString() == null) || ("".equals(editText.getText().toString().trim())))
			return true;

		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches())
		{
			editText.setError(editText.getResources().getString(R.string.validation_error_invalid_email));
			return false;
		}

		editText.setError(null);
		return true;
	}

	protected EditText editText(int id) {
		return (EditText) findViewById(id);
	}

	protected EditText editText(int id, View view) {
		return (EditText) view.findViewById(id);
	}

	protected TextView textView(int id) {
		return (TextView) findViewById(id);
	}

	protected TextView textView(int id, View view) {
		return (TextView) view.findViewById(id);
	}

	public boolean validateNotEmpty(Spinner spinner)
	{
		if (spinner == null)
			return false;

		if (!(spinner.getSelectedView() instanceof TextView))
			return false;

		return validateNotEmpty((TextView)spinner.getSelectedView());
	}


	protected View findViewById(int id) {
		return getActivity().findViewById(id);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isStickyEvent()) {
			EventBus.getDefault().registerSticky(this);
		} else {
			EventBus.getDefault().register(this);
		}
		if (!InfoVault.getInstance(getActivity()).isTransactionInProgress()
				&& InfoVault.getInstance(getActivity()).isApiCall()) {
			closeWaitingDialog();
		}
		allowsBackPress = false;
	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(TransactionResponse response) {
		// Toast.makeText(getActivity(), "Called onEvent!",Toast.LENGTH_SHORT).show();
	}

	protected boolean showSelectAccountOrCardDialog() {
		if (!InfoVault.getInstance(getActivity()).isSelectedCredentialValid()) {
			new WarningDialog(getString(R.string.warning),
					getString(R.string.warning_account_or_card_not_select)).show(
					getFragmentManager(), null);
			return true;
		}
		return false;
	}

	protected void closeWaitingDialog() {
		FragmentManager fm = getFragmentManager();
		Fragment dialog = fm.findFragmentByTag(WAIT_DIALOG);
		if (dialog != null) {
			fm.beginTransaction().remove(dialog).commit();
		}
	}

	protected void showCompletitionScreen(String caption, String message) {
		showCompletitionScreen(caption, message, "");
	}

	protected void showCompletitionScreen(String caption, String message, String reference) {

		Receipt receipt = new Receipt(reference, null, null);
		showCompletitionScreen(caption, message, receipt);
	}

	protected void showCompletitionScreen(String caption, String message, Receipt receipt) {
		closeWaitingDialog();
		CompletitionScreenCore fragment = new CompletitionScreenCore();
		Bundle messageBundle = new Bundle();
		messageBundle.putString(CompletitionScreenCore.MESSAGE_KEY, message);
		messageBundle.putString(CompletitionScreenCore.CAPTION_KEY, caption);

		fragment.setReceipt(receipt);

		fragment.setArguments(messageBundle);
		((CoreHome) getActivity()).setFragment(fragment);
	}

	public boolean allowBackPressed() {
		return allowsBackPress;
	}

	public String correctAmoutValue(String amount) {
		if (amount.startsWith(".")) {
			amount = "0" + amount;
			return amount;
		} else {
			return amount;
		}
	}

	/**
	 * Standard getter
	 *
	 * @return the stickyEvent
	 */
	public boolean isStickyEvent() {
		return stickyEvent;
	}

	/**
	 * Standard setter
	 *
	 * @param stickyEvent
	 *            the stickyEvent to set
	 */
	public void setStickyEvent(boolean stickyEvent) {
		this.stickyEvent = stickyEvent;
	}
}

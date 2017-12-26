package com.mobilebanking.core.uiutils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static final String WAIT_DIALOG = "wait_dialog";
    private static String dateTimeNow;

    public static TextView findTextView(View view, int resId) {

        return (TextView) view.findViewById(resId);

    }

    public static EditText findEditText(View view, int resId) {
        return (EditText) view.findViewById(resId);
    }

    public static ListView findListView(View view, int resId) {

        return (ListView) view.findViewById(resId);

    }

    public static ImageView findImageView(View view, int resId) {
        return (ImageView) view.findViewById(resId);
    }

    public static boolean isEmptyEditText(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public static String formatDate(long longDate) {

        Date date = new Date(longDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return sdf.format(date);

    }

    public static void showWaitDialog(Fragment fragment) {
        showWaitDialog(fragment.getFragmentManager());
    }

    public static void showWaitDialog(FragmentManager fragmentManager) {
        new WaitDialog().show(fragmentManager, WAIT_DIALOG);
    }

    public static void dismissWaitDialog(FragmentManager fragmentManager) {

        Fragment dialog = fragmentManager.findFragmentByTag(WAIT_DIALOG);
        if (fragmentManager != null && dialog != null) {
            fragmentManager.beginTransaction().remove(dialog).commit();
        }
    }

    public static void showWarnDialog(Fragment fragment, String title, String message) {
        showWarnDialog(fragment.getFragmentManager(), title, message);
    }

    public static void showWarnDialog(FragmentManager fragmentManager, String title, String message) {

        new WarningDialog(title, message).show(fragmentManager, "WARN_TAG");
    }

    public static void dismissWaitDialog(Fragment fragment) {
        dismissWaitDialog(fragment.getFragmentManager());
    }

    public static boolean isDateOlderMinutes(long datemillis, long period) {

        long now = System.currentTimeMillis();
        long diff = now - datemillis;

        return diff >= (period * 60 * 1000);
    }

    public static Button findButton(View rootView, int resId) {

        return (Button) rootView.findViewById(resId);
    }

    public static String formatAmount(double amount) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        return format.format(amount);
    }

    public static String formatMoneyAmount(double amount) {
        DecimalFormat format = new DecimalFormat("$#,##0.00;-$#,##0.00");
        return format.format(amount);
    }

    public static String formatMoneyAmount(BigDecimal amount) {
        if (amount == null) {
            return formatMoneyAmount(0);
        }
        return formatMoneyAmount(amount.doubleValue());
    }

    public static String formatAmount(BigDecimal amount) {

        if (amount == null) {
            return formatAmount(0);
        }

        return formatAmount(amount.doubleValue());
    }

    public static BigDecimal getDecimalFromEditText(EditText editText) {

        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        String value = editText.getText().toString();
        return BigDecimal.valueOf(Double.parseDouble(value));
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String formatMsisdnFromContacts(String number) {

        if (number == null) {
            return number;
        }

        String formattedNumber = number;

        //Replace all the possible friendly display chars
        formattedNumber = formattedNumber
                .replace("(", "").replace(")", "")
                .replace("-", "")
                .replace("+", "")
                .replace(" ", "").trim();

        if (formattedNumber.startsWith("0")) {
            formattedNumber = "263" + formattedNumber.substring(1);
        }
        return formattedNumber;
    }

    public static String getDateTimeNow() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date());
    }
}

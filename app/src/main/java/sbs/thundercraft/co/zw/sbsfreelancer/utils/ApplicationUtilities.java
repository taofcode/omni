package sbs.thundercraft.co.zw.sbsfreelancer.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import sbs.thundercraft.co.zw.sbsfreelancer.R;


public class ApplicationUtilities {

    public static String WAIT_DIALOG = "wait_dialog";



    public static void dismisDialog(FragmentManager fragmentManager) {

        Fragment dialog = fragmentManager.findFragmentByTag(WAIT_DIALOG);
        if (fragmentManager != null && dialog != null) {
            fragmentManager.beginTransaction().remove(dialog).commit();
        }
    }
    public static  boolean validateNotEmpty(TextView editText)
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
    public static String generateDeviceId() {

        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10)
                + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10)
                + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {

            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {

            serial = "serial";
        }
        return "";


    }

    public static View findViewById(Activity activity, int id) {
        return activity.findViewById(id);
    }
    public static void showDialog(FragmentManager fragmentManager, String title, String message) {

        new WarningDialog(title, message).show(fragmentManager, "WARN_TAG");
    }
}

package sbs.thundercraft.co.zw.sbsfreelancer;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import sbs.thundercraft.co.zw.sbsfreelancer.inteface.Api;
import sbs.thundercraft.co.zw.sbsfreelancer.models.Devices;
import sbs.thundercraft.co.zw.sbsfreelancer.models.User;
import sbs.thundercraft.co.zw.sbsfreelancer.service.ApiUtils;
import sbs.thundercraft.co.zw.sbsfreelancer.service.RetrofitClient;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.ApplicationUtilities;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.WarningDialog;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

     private static final int REQUEST_READ_CONTACTS = 0;
    private Api mAPIService;

    private EditText pinEditView;
    private  Button loginButton;
    private EditText userEditView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAPIService = ApiUtils.getAPIService();
           userEditView = findViewById(R.id.user_edit_text);
          pinEditView =  findViewById(R.id.pin_edit_text);
          loginButton = findViewById(R.id.login_button);

          loginButton.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (!isPasswordValid(userEditView.getText().toString())) {

                      new WarningDialog("Error","Please enter valid User Number");
                  }
                  if (!isPasswordValid(pinEditView.getText().toString())) {

                      new WarningDialog("Error","Please enter valid Pin");
                  }




                  User user = new User();
                  String deviceId = ApplicationUtilities.generateDeviceId();
                  user.setUsername(userEditView.getText().toString());
                  user.setPassword(pinEditView.getText().toString());
                  user.setDeviceId(deviceId);
                  attemptLogin(user);

              }
          });





    }



    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    public void showResponse(String response) {
        ApplicationUtilities.showDialog(getFragmentManager(),"Response",response);
    }
    private void attemptLogin(User user) {

        mAPIService.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ApplicationUtilities.dismisDialog(getFragmentManager());
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Devices device = new Devices();
                    device.setAppVersion("1.0");
                    device.setDeviceId(ApplicationUtilities.generateDeviceId());
                    int sdkVersion = android.os.Build.VERSION.SDK_INT;
                    String name = Build.MANUFACTURER;
                    device.setName(name);
                    device.setOs(Build.VERSION.BASE_OS);
                    device.setOsVersion(String.valueOf(sdkVersion));
                    mAPIService.registerDevice(device);

                    goHome();



                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ApplicationUtilities.dismisDialog(getFragmentManager());
                new WarningDialog("Failure","Transaction was unsuccessful");

                //JUST FIRCE Login irreguardless
                goHome();
            }

        });
    }

    private void goHome() {
        Intent startIntent = new Intent(LoginActivity.this,MainActivity.class);
        startIntent.putExtra("username",userEditView.getText().toString());
        startActivity(startIntent);
    }


    private boolean isPasswordValid(String password) {
       if(password.length() == 4){
           return true;
       }else {
           return false;
       }
    }





}


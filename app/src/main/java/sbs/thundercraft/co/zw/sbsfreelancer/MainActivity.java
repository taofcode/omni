package sbs.thundercraft.co.zw.sbsfreelancer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import sbs.thundercraft.co.zw.sbsfreelancer.fragments.HeadShotFragment;
import sbs.thundercraft.co.zw.sbsfreelancer.fragments.OmniAccountOpenning;
import sbs.thundercraft.co.zw.sbsfreelancer.inteface.Api;
import sbs.thundercraft.co.zw.sbsfreelancer.models.DeviceLogins;
import sbs.thundercraft.co.zw.sbsfreelancer.service.ApiUtils;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.ApplicationUtilities;

public class MainActivity extends Activity {

    private ImageView logoutView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Api mAPIService;
    private ImageView openAccountImage;
   public String  username;
    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAPIService = ApiUtils.getAPIService();
        setContentView(R.layout.activity_main);
        openAccountImage = findViewById(R.id.openAccountImage);
        logoutView = findViewById(R.id.logOut);
        username = getIntent().getStringExtra("username");
        openAccountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestNavigation(new OmniAccountOpenning());



            }
        });

        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceLogins deviceLogins = new DeviceLogins();
                deviceLogins.setDeviceId(ApplicationUtilities.generateDeviceId());
                deviceLogins.setUserId(username);
                deviceLogins.setUserId("LoggedIn");
                mAPIService.logout(deviceLogins);

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    public void onBackPressed() {
        int fragments =  getFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
            return;
        }

            super.onBackPressed();

    }



    private void requestNavigation(Fragment fragment) {

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(TAG).commit();
    }

}

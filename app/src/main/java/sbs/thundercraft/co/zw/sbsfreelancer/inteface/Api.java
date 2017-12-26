package sbs.thundercraft.co.zw.sbsfreelancer.inteface;





import android.bluetooth.BluetoothClass;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import sbs.thundercraft.co.zw.sbsfreelancer.models.Customers;
import sbs.thundercraft.co.zw.sbsfreelancer.models.DeviceLogins;
import sbs.thundercraft.co.zw.sbsfreelancer.models.Devices;
import sbs.thundercraft.co.zw.sbsfreelancer.models.User;


/**
 * Created by shelton on 12/19/17.
 */


    public interface Api {
    @POST("api/registrations")
    Call<Customers> saveCustomer(@Body Customers customer,
                                 @Part MultipartBody.Part idImage,
                                 @Part MultipartBody.Part headshotImage,
                                 @Part MultipartBody.Part residenceImage);

    @POST("token")
    Call<User> login(@Body User user);

    @POST("/registerDevice")
    Call<Devices> registerDevice(@Body  Devices devices);

    @POST("/logout")
    Call<DeviceLogins> logout(@Body  DeviceLogins deviceLogins);

}
package sbs.thundercraft.co.zw.sbsfreelancer.service;

import sbs.thundercraft.co.zw.sbsfreelancer.inteface.Api;

/**
 * Created by shelton on 12/19/17.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://localhost:83/";

    public static Api getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(Api.class);
    }
}

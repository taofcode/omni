package sbs.thundercraft.co.zw.sbsfreelancer.service;

/**
 * Created by shelton on 12/19/17.
 */

public class ErrorResponse {
  private  String responseCode;
   private String description;
    public String getDescription() {

        return description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

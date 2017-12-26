package sbs.thundercraft.co.zw.sbsfreelancer.models;


import java.time.ZonedDateTime;
  public class Devices {


    private String deviceId;
    private String name;
    private String os;
    private  String appVersion;
    private String osVersion;
    private ZonedDateTime dateCreated;
    private ZonedDateTime dateUpdated;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public ZonedDateTime getZonedDateTimeCreated() {
        return dateCreated;
    }

    public void setZonedDateTimeCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getZonedDateTimeUpdated() {
        return dateUpdated;
    }

    public void setZonedDateTimeUpdated(ZonedDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}

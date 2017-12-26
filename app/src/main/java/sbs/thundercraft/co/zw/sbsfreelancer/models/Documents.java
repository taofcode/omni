package sbs.thundercraft.co.zw.sbsfreelancer.models;



import android.net.ProxyInfo;
import android.net.Uri;

import java.net.URL;
import java.time.ZonedDateTime;

import java.util.Map;


public class Documents {

    private Integer id;
    private Uri fileUrl;
    private String documentType;
    private ZonedDateTime dateCreated;
    private ZonedDateTime dateUpdated;
    private ZonedDateTime dateDeleted;
    private String idNumber;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Uri getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(Uri fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public ZonedDateTime getZonedDateTimeDeleted() {
        return dateDeleted;
    }

    public void setZonedDateTimeDeleted(ZonedDateTime dateDeleted) {
        this.dateDeleted = dateDeleted;
    }


    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}

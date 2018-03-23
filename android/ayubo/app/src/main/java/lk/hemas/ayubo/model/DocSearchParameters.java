package lk.hemas.ayubo.model;

import java.io.Serializable;
import java.util.HashMap;

import lk.hemas.ayubo.config.AppConfig;

/**
 * Created by Sabri on 3/16/2018. Search parameter model
 */

public class DocSearchParameters implements Serializable {

    //constants
    private static final String PARAM_SEARCH_NAME = "docName";
    private static final String PARAM_SEARCH_DATE = "toDate";
    private static final String PARAM_SEARCH_SPECIALTY = "speciality";
    private static final String PARAM_SEARCH_HOSPITAL = "hospitalid";
    private static final String PARAM_SEARCH_TOKEN = "gettoken";

    //instances
    private String docName;
    private String date;
    private String specialty;
    private String hospitalId;

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public HashMap<String, String> getSearchParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put(PARAM_SEARCH_TOKEN, AppConfig.DOC_SERVER_REQUEST_TOKEN);
        params.put(PARAM_SEARCH_NAME, docName != null ? docName : "");
        params.put(PARAM_SEARCH_DATE, date != null ? date : "");
        params.put(PARAM_SEARCH_SPECIALTY, specialty != null ? specialty : "");
        params.put(PARAM_SEARCH_HOSPITAL, hospitalId != null ? hospitalId : "");

        return params;
    }

    public String getDocName() {
        return docName;
    }

    public String getSpecialty() {
        return specialty;
    }
}

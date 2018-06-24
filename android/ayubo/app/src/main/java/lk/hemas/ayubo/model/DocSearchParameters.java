package lk.hemas.ayubo.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Sabri on 3/16/2018. Search parameter model
 */

public class DocSearchParameters extends SoapBasicParams implements Serializable {

    private String doctorId;
    private String locationId;
    private String specializationId;
    private String date;

//    protected DocSearchParameters(String doctorId, String locationId, String specializationId, String date) {
//        this.doctorId = doctorId;
//        this.locationId = locationId;
//        this.specializationId = specializationId;
//        this.date = date;
//    }

    public String getSearchParams() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("f", user_id);
            jsonObject.put("d", doctorId);
            jsonObject.put("e", locationId);
            jsonObject.put("b", specializationId);
            jsonObject.put("c", date);
            jsonObject.put("a", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

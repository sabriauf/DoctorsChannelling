package lk.hemas.ayubo.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;

import lk.hemas.ayubo.config.AppConfig;

/**
 * Created by Sabri on 3/17/2018. model for Ayubo search request params
 */

public class AyuboSearchParameters implements Serializable {

    private String user_id = AppConfig.HEMAS_USER_ID;
    private String token_key = AppConfig.HEMAS_SERVER_REQUEST_TOKEN;
    private String hospital_id = "";
    private String doc_id = "";
    private String specialization_id = "";

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public void setSpecialization_id(String specialization_id) {
        this.specialization_id = specialization_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public String getSpecialization_id() {
        return specialization_id;
    }

    public String getSearchParams() {
//        return new Gson().toJson(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("doc_id", doc_id);
            jsonObject.put("specialization_id", specialization_id);
            jsonObject.put("hospital_id", hospital_id);
            jsonObject.put("token_key", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}

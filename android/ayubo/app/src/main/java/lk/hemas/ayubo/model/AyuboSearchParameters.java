package lk.hemas.ayubo.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Sabri on 3/17/2018. model for Ayubo search request params
 */

public class AyuboSearchParameters extends SoapBasicParams implements Serializable {

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
            jsonObject.put("d", user_id);
            jsonObject.put("e", "");
            jsonObject.put("b", "");
            jsonObject.put("c", "");
            jsonObject.put("a", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}

package lk.hemas.ayubo.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class SessionSearchParams extends SoapBasicParams implements Serializable {

    private String doctorId;
    private String locationId;
    private String specializationId;
    private String source[];
    private String direct;

    public String getSearchParams() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("f", user_id);
            jsonObject.put("g", doctorId);
            jsonObject.put("d", locationId);
            jsonObject.put("e", specializationId);
            jsonObject.put("b", editSourceList());
            jsonObject.put("c", direct);
            jsonObject.put("a", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private JSONObject editSourceList() {
        HashMap<String, String> values = new HashMap<>();
        int pos = 0;
        for (String temp : source) {
            values.put(String.valueOf(pos++), temp);
        }
        try {
            return new JSONObject(new Gson().toJson(values));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
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

    public String[] getSource() {
        return source;
    }

    public void setSource(String[] source) {
        this.source = source;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }
}

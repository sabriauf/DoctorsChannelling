package lk.hemas.ayubo.model;

import org.json.JSONException;
import org.json.JSONObject;

import lk.hemas.ayubo.config.AppConfig;

/**
 * Created by Sabri on 3/30/2018. Soap request basic params
 */

public class SoapBasicParams {

    protected String user_id = AppConfig.HEMAS_USER_ID;
    protected String token_key = AppConfig.HEMAS_SERVER_REQUEST_TOKEN;

    public String getSearchParams() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("token_key", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getSearchParams(String query) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("q", query);
            jsonObject.put("token_key", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}

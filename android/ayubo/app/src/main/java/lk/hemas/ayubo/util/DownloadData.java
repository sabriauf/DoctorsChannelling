package lk.hemas.ayubo.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import lk.hemas.ayubo.model.DownloadDataBuilder;

/**
 * Created by Sabri on 3/16/2018. download data
 */

public class DownloadData extends AsyncTask<Void, Void, String> {

    //constants
    private static final String URL_PARAMS_ADAPTER = "?";
    private static final String URL_PARAMS_SEPARATOR = "&";
    private static final String JSON_STATUS_OBJECT = "Status";
    private static final String TITLE_XML_TAG_PATTERN = "<\\w+\\s.+?(\\>)";

    //instances
    private Context context;
    private HashMap<String, DownloadListener> listeners;
    private DownloadDataBuilder builder;

    public DownloadData(Context context, DownloadDataBuilder builder) {
        this.context = context;
        this.builder = builder;
        listeners = new HashMap<>();
    }

    @Override
    protected String doInBackground(Void... params) {
        String url = builder.getUrl();
        DownloadManager.DownloadBuilder mangerBuilder = new DownloadManager.DownloadBuilder();

        if (builder.getUrlParams() != null)
            url = getUrlParamString(url, builder.getUrlParams());

        mangerBuilder.init(context, url, builder.getRequestMethod());
//                .setHeaders(AppHandler.getHeaders(context));

        if (builder.getParams() != null)
            mangerBuilder.setParams(builder.getParams());

        if (!builder.getRaw().equals(""))
            mangerBuilder.setRawData(builder.getRaw());

        if (builder.getHeaders() != null)
            mangerBuilder.setHeaders(builder.getHeaders());

        if (builder.getType() != null)
            mangerBuilder.setContentType(builder.getType());

        mangerBuilder.setTimeout(builder.getTimeout());

        return mangerBuilder.startDownload();
    }

    public DownloadData setOnDownloadListener(String name, DownloadListener listener) {
        listeners.put(name, listener);
        return this;
    }

    public void removeDownloadListener(String key) {
        listeners.remove(key);
    }

    private String getUrlParamString(String url, HashMap<String, String> params) {
        String separator = "";
        url += URL_PARAMS_ADAPTER;

        for (String key : params.keySet()) {
            url += separator + key + "=" + params.get(key);
            separator = URL_PARAMS_SEPARATOR;
        }

        return url;
    }

    private Void parseJsonString(String jsonString) {

        try {
            JSONObject respond;
            if (jsonString.contains(">")) {
//                jsonString = removeUnwantedXMLTags(jsonString);
//                respond = XML.toJSONObject(jsonString);
                XmlToJson xmlToJson = new XmlToJson.Builder(jsonString).build();
                respond = xmlToJson.toJson();
            } else
                respond = new JSONObject(jsonString);

            if (respond != null)
                notifySuccess(respond.toString(), 200);
            else
                notifyFailure("Server Error", 401);

        } catch (JSONException e) {
            e.printStackTrace();
            notifyFailure(jsonString, 0);
        }
        return null;
    }

//    private String removeUnwantedXMLTags(String value) {
//        value = value.substring(value.indexOf(">") + 1);
//        Pattern pattern = Pattern.compile(TITLE_XML_TAG_PATTERN);
//        Matcher matcher = pattern.matcher(value);
//        while (matcher.find()) {
//            String temp = value.substring(matcher.start(), matcher.end());
//            String title = temp.substring(temp.indexOf("<"), temp.indexOf(" "));
//            value = value.replace(temp, title + ">");
//        }
//        return value;
//    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (response != null)
            parseJsonString(cleanResponse(response));
        else
            notifyFailure("", 0);
    }

    private String cleanResponse(String rawString) {
        rawString = rawString.trim();
        rawString = rawString.replaceAll("\n", "");
        return rawString;
    }

    private void notifyFailure(String message, int code) {
        Log.d(DownloadData.class.getSimpleName(), "Failed Message = " + message);
        for (String key : listeners.keySet()) {
            listeners.get(key).onDownloadFailed(message, builder.getWhat(), code);
        }
    }

    private void notifySuccess(String message, int code) {
        for (String key : listeners.keySet()) {
            listeners.get(key).onDownloadSuccess(message, builder.getWhat(), code);
        }
    }

    public interface DownloadListener {

        void onDownloadSuccess(String response, int what, int code);

        void onDownloadFailed(String errorMessage, int what, int code);
    }

    private class JsonStatus {
        private boolean success;
        private int code;
        private String description;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

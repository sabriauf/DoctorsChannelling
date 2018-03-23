package lk.hemas.ayubo.model;

import java.util.HashMap;

/**
 * Created by Sabri on 3/16/2018. Data download model
 */

public class DownloadDataBuilder {
    private int what;
    private HashMap<String, String> params;
    private String url;
    private HashMap<String, String> urlParams;
    private HashMap<String, String> headers;
    private String raw = "";
    private String requestMethod;
    private int timeout = 10000;
    private String type;

    public int getWhat() {
        return what;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getUrlParams() {
        return urlParams;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRaw() {
        return raw;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public int getTimeout() {
        return timeout;
    }

    public DownloadDataBuilder init(String url, int what, String requestMethod) {
        this.what = what;
        this.url = url;
        this.requestMethod = requestMethod;
        return this;
    }

    public DownloadDataBuilder setUrlParams(HashMap<String, String> urlParams) {
        this.urlParams = urlParams;
        return this;
    }

    public DownloadDataBuilder setParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    public DownloadDataBuilder setHeaders(HashMap<String, String> params) {
        this.headers = params;
        return this;
    }

    public DownloadDataBuilder setRawData(String rawData) {
        this.raw = rawData;
        return this;
    }

    public DownloadDataBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getType() {
        return type;
    }

    public DownloadDataBuilder setType(String type) {
        this.type = type;
        return this;
    }
}

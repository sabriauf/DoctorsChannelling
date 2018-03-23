package lk.hemas.ayubo.model;

import java.io.Serializable;
import java.util.HashMap;

import lk.hemas.ayubo.config.AppConfig;

/**
 * Created by Sabri on 3/22/2018. Doc Session Parameters
 */

public class DocSessionParameters implements Serializable {

    //constants
    private static final String PARAM_SEARCH_ID = "DocID";
    private static final String PARAM_SEARCH_FROM_DATE = "FromDate";
    private static final String PARAM_SEARCH_TO_DATE = "toDate";
    private static final String PARAM_SEARCH_TOKEN = "gettoken";

    //instances
    private String docId;
    private String fromDate;
    private String toDate;

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public HashMap<String, String> getSearchParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put(PARAM_SEARCH_TOKEN, AppConfig.DOC_SERVER_REQUEST_TOKEN);
        params.put(PARAM_SEARCH_ID, docId);
        params.put(PARAM_SEARCH_FROM_DATE, fromDate != null ? fromDate : "");
        params.put(PARAM_SEARCH_TO_DATE, toDate != null ? toDate : "");

        return params;
    }
}

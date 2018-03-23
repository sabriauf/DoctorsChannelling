package lk.hemas.ayubo.util;

import java.util.HashMap;

/**
 * Created by Sabri on 3/12/2018. App common methods
 */

public class AppHandler {

    private static final String SOAP_REQUEST_METHOD_KEY = "method";
    private static final String SOAP_REQUEST_INPUT_KEY = "input_type";
    private static final String SOAP_REQUEST_RESPONSE_KEY = "response_type";
    private static final String SOAP_REQUEST_DATA_KEY = "rest_data";
    private static final String SOAP_REQUEST_TYPE_VALUE = "JSON";

    public static HashMap<String, String> getSoapRequestParams(String methodName, String params) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(SOAP_REQUEST_METHOD_KEY, methodName);
        hashMap.put(SOAP_REQUEST_INPUT_KEY, SOAP_REQUEST_TYPE_VALUE);
        hashMap.put(SOAP_REQUEST_RESPONSE_KEY, SOAP_REQUEST_TYPE_VALUE);
        hashMap.put(SOAP_REQUEST_DATA_KEY, params);

        return hashMap;
    }
}

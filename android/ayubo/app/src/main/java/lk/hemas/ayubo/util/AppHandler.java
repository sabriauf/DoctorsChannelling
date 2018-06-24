package lk.hemas.ayubo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Patterns;

import java.net.InetAddress;
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

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNotValidateNIC(String value) {
        if (!(value.length() == 10 || value.length() == 12)) {
            return true;
        } else if (value.length() == 10 && !(value.toUpperCase().toCharArray()[9] == 'V' || value.toUpperCase().toCharArray()[9] == 'X')) {
            return true;
        }
        return false;
    }

//    public static boolean isNotValidatePassport(String value) {
//        if (!(value.length() == 10 || value.length() == 12)) {
//            return true;
//        } else if (value.length() == 10 && !(value.toUpperCase().toCharArray()[9] == 'V' || value.toUpperCase().toCharArray()[9] == 'X')) {
//            return true;
//        }
//        return false;
//    }

    public static boolean isNotValidatePhone(String value) {
        if (value.length() != 10) {
            return true;
        }
        return false;
    }

    public static boolean isNotValidateEmail(String value) {
        if ((TextUtils.isEmpty(value) || !Patterns.EMAIL_ADDRESS.matcher(value).matches())) {
            return true;
        }
        return false;
    }
}

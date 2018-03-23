package lk.hemas.ayubo.config;

import lk.hemas.ayubo.BuildConfig;

/**
 * Created by Sabri on 3/16/2018. Application configurations
 */

public class AppConfig {

    //tokens
    public static final String DOC_SERVER_REQUEST_TOKEN = "Hms@1234";
    public static final String HEMAS_SERVER_REQUEST_TOKEN = "70564a326677614f6b575a4955744558356d393633644f2f4d454f6742342b474b696171704d6f6a5768343d";
    public static final String HEMAS_USER_ID = "be0a5cbb-a2d5-2061-c4af-595e0345ebed";

    public static final String SERVER_REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final int SERVER_REQUEST_TIMEOUT = 50000;
    public static final String AMOUNT_VIEW = "LKR %.2f";

    private static final String DOC_BASIC_URL = "http://203.115.18.14/990.asmx";
    private static final String AYUBO_BASIC_URL = BuildConfig.DEFAULT_URL;

    public static final String URL_DOCTOR_LIST = DOC_BASIC_URL + "/DOC990DoctorListAvailability";
    public static final String URL_SESSION_LIST = DOC_BASIC_URL + "/DOC990DoctorListChanelling";
    public static final String URL_AYUBO_SOAP_REQUEST = AYUBO_BASIC_URL + "custom/service/v4_1_custom/rest.php";

    public static final String METHOD_SOAP_DOCTORS_SEARCH = "getHemasAppDocMaster";
    public static final String METHOD_SOAP_SPECIALTY_SEARCH = "getHemasAppSpecialityMaster";
    public static final String METHOD_SOAP_LOCATION_SEARCH = "getHemasAppDocMaster";
}

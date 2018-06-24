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
    public static final String AMOUNT_VIEW = "LKR %,.2f";
    public static final String LOCATION_VIDEO_ID = "b2c1e932-7155-f733-1de4-5886ec9ec7c5";

    private static final String DOC_BASIC_URL = "http://203.115.18.14/990.asmx";
    public static final String AYUBO_BASIC_URL = BuildConfig.DEFAULT_URL;

    public static final String URL_DOCTOR_LIST = DOC_BASIC_URL + "/DOC990DoctorListAvailability";
    public static final String URL_SESSION_LIST = DOC_BASIC_URL + "/DOC990DoctorListChanelling";
    public static final String URL_AYUBO_SOAP_REQUEST = AYUBO_BASIC_URL + "custom/service/v4_1_custom/rest.php";

    public static final String METHOD_SOAP_DOCTORS_SEARCH = "chanellingDoctor";
    public static final String METHOD_SOAP_SPECIALTY_SEARCH = "chanellingSpecialization";
    public static final String METHOD_SOAP_LOCATION_SEARCH = "chanellingLocation";
    public static final String METHOD_SOAP_GET_FAVORITES = "getAllFavorites";
    public static final String METHOD_SOAP_VIDEO_DOCTORS = "getMedicalExpertsNewExperts";
    public static final String METHOD_SOAP_VIDEO_EXPERTS = "VideoCallSearch";
    public static final String METHOD_SOAP_GET_SESSION = "getSessionEvents";
    public static final String METHOD_SOAP_GET_CHANNELLING_SESSION = "chanellingSessions";
    public static final String METHOD_SOAP_GET_CHANNELLING_SEARCH = "chanellingSearch";
    public static final String METHOD_SOAP_GET_NEW_FAVORITES = "chanellingCachedDoctorList";
    public static final String METHOD_SOAP_REMOVE_BOOKING = "chanellingReverseDoc990";
    public static final String METHOD_SOAP_ADD_FAVORITE = "makeFavorite";
    public static final String METHOD_SOAP_REMOVE_FAVORITE = "removeFavorites";
    public static final String METHOD_SOAP_ADD_APPOINTMENT = "chanellingBooking";
    public static final String METHOD_SOAP_DAILOG_PAY = "chanellingDialogAddtoBill";
    public static final String METHOD_SOAP_DAILOG_PIN = "chanellingDialogPayment";
    public static final String METHOD_SOAP_UPLOAD_IMAGE = "videoCallUploadPhotoSave";
    public static final String METHOD_SOAP_VIDEO_APPIONTMENT = "VideoCallTentativeBooking";
    public static final String METHOD_SOAP_GET_HISTORY = "videoCallAppointmentHistory";
    public static final String METHOD_SOAP_CANCEL_APPOINTMENT = "videoCallCancelAppointment";

    public static final String DOCTOR_IMAGE_BASIC = "resize.php?w=100&img=/datadrive/wellness_upload/upload/user_img/";
    public static final String DOCTOR_IMAGE_DEAFULT = "default_user_picture.php";
}

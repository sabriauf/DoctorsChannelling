package lk.hemas.ayubo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import lk.hemas.ayubo.activity.DetailActivity;
import lk.hemas.ayubo.activity.PayActivity;
import lk.hemas.ayubo.activity.SearchActivity;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.config.AyuboApplication;
import lk.hemas.ayubo.model.Appointment;
import lk.hemas.ayubo.model.Booking;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.model.SessionParent;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.model.User;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;
import lk.hemas.ayubo.util.TimeFormatter;

public class DetailSessionView implements DetailActivity.DetailAction, Serializable {


    //instances
    private Appointment appointment;
    private Booking booking;

    public DetailSessionView(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public String getDocName() {
        return appointment.getSessionParent().getDoctor_name();
    }

    @Override
    public String getSpecialisation() {
        return appointment.getSessionParent().getSpecialization_name();
    }

    @Override
    public Double getTotalPrice() {
        return booking.getPrice().getTotal() * 1.0;
    }

    @Override
    public String getType() {
        return appointment.getSource();
    }

    @Override
    public Date getDate() {
        return TimeFormatter.stringToDate(appointment.getSession().getShow_date(), TimeFormatter.DATE_FORMAT_VIDEO);
    }

    @Override
    public String getLocation() {
        return appointment.getSessionParent().getHospital_name();
    }

    @Override
    public String getAppointmentNo() {
        return String.valueOf(appointment.getSession().getNext_appointment_no());
    }

    @Override
    public Double getDoctorFee() {
        return booking.getPrice().getDoctor_fee();
    }

    @Override
    public Double getHospitalFee() {
        return booking.getPrice().getHospital_fee();
    }

    @Override
    public Double getPlatformFee() {
        return booking.getPrice().getPlatform_fee();
    }

    @Override
    public Double getVatFee() {
        return booking.getPrice().getVat();
    }

    @Override
    public Double getBookingFee() {
        return booking.getPrice().getBooking_charge();
    }

    @Override
    public DownloadDataBuilder getAddDownloadBuilder() {
        return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_ADD_APPOINTMENT, new BookingParams(appointment)
                        .getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public DownloadDataBuilder getRemoveDownloadBuilder(Activity activity) {
        return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_REMOVE_BOOKING, new DetailSessionView.
                        RemoveParams(activity)
                        .getSearchParams())).setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public boolean readData(Activity activity, String response) {
        Log.d(DetailActivity.class.getSimpleName(), "Server Respond = " + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            booking = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), Booking.class);

            ((AyuboApplication) activity.getApplication()).setBooking(booking);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return booking != null;
    }

    @Override
    public void onFinish(Activity activity) {
        if (booking != null) {
            Intent intent = new Intent(activity, PayActivity.class);
            intent.putExtra(PayActivity.EXTRA_BOOKING_OBJECT, booking);
            activity.startActivity(intent);
        } else
            Toast.makeText(activity, "Please wait until retrieve Data...", Toast.LENGTH_LONG).show();
    }

    @Override
    public String getDoctorNote() {
        return appointment.getSessionParent().getSpecial_notes();
    }

    @Override
    public boolean hasData(Activity activity) {
        return ((AyuboApplication) activity.getApplication()).getBooking() != null;
    }

    @Override
    public String getTime() {
        return appointment.getSession().getTime();
    }

    class BookingParams extends SoapBasicParams {
        Patient patient;
        ParamSession session;

        BookingParams(Appointment appointment) {
            patient = new Patient(appointment.getUser());
            session = new ParamSession(appointment.getSessionParent(), appointment.getSession(),
                    appointment.getSource());
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("b", user_id);
//                jsonObject.put("patient", patient.getMap().toString().replaceAll("=", "\"=\""));
//                jsonObject.put("session", session.getMap().toString().replaceAll("=", "\"=\""));
                jsonObject.put("c", new JSONObject(new Gson().toJson(patient)));
                jsonObject.put("d", new JSONObject(new Gson().toJson(session)));
                jsonObject.put("a", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            return jsonObject.toString().replace("\\", "");
            return jsonObject.toString();
        }
    }

    class RemoveParams extends SoapBasicParams {
        String trId;

        RemoveParams(Activity activity) {
            this.trId = String.valueOf(((AyuboApplication) activity.getApplication()).getBooking().getAppointment().getRef());
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("trId", trId);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    class Patient {
        private String member;
        private String needSMS;
        private String nsr;
        private String foreign;
        private String teleNo;
        private String title;
        private String patientName;
        private String nid;
        private String passport;
        private String email;
        private String clientRefNumber;

        Patient(User user) {
            patientName = String.format("%s %s", user.getfName(), user.getlName());
            teleNo = user.getPhone();
            if (AppHandler.isNotValidateNIC(user.getIdentification()))
                passport = user.getIdentification();
            else
                nid = user.getIdentification();
            title = "Mr";
            email = user.getEmail();
            clientRefNumber = "";
            member = "";
            needSMS = "";
            nsr = "";
            foreign = "";
        }

//        JSONArray getMap() {
//            JSONArray patientArray = new JSONArray();
//            HashMap<String, String> map = new HashMap<>();
//            map.put("patientName", patientName);
//            map.put("teleNo", teleNo);
//            map.put("passport", passport);
//            map.put("nid", nid);
//            map.put("title", title);
//            map.put("email", email);
//            map.put("clientRefNumber", clientRefNumber);
//            map.put("member", member);
//            map.put("needSMS", needSMS);
//            map.put("nsr", nsr);
//            map.put("foreign", foreign);
//
//            for (Object o : map.entrySet()) {
//                patientArray.put(o);
//            }
//
//            return patientArray;
//        }
    }

    class ParamSession {
        private String id;
        private String hosId;
        private String docId;
        private String theDay;
        private String startTime;
        private String theDate;
        private String from;

        ParamSession(SessionParent parent, Session session, String source) {
            id = session.getBooking_info()[0].getId();
            hosId = String.valueOf(parent.getHospital_id());
            docId = String.valueOf(parent.getDoctor_code());
            theDay = session.getDay();
            startTime = session.getTime();
            theDate = session.getShow_date();
            from = source;
        }

//        JSONArray getMap() {
//            JSONArray sessionArray = new JSONArray();
//            HashMap<String, String> map = new HashMap<>();
//            map.put("id", id);
//            map.put("hosId", hosId);
//            map.put("docId", docId);
//            map.put("theDay", theDay);
//            map.put("startTime", startTime);
//            map.put("theDate", theDate);
//            map.put("from", from);
//
//            for (Object o : map.entrySet()) {
//                sessionArray.put(o);
//            }
//
//            return sessionArray;
//        }
    }
}

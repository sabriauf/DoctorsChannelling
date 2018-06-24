package lk.hemas.ayubo.view;

import android.app.Activity;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import lk.hemas.ayubo.activity.DetailActivity;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Expert;
import lk.hemas.ayubo.model.VideoSession;

public class DetailVideoView implements DetailActivity.DetailAction, Serializable {

    //constants
    private static final String DOCTOR_FULL_NAME_WITH_TITLE = "%s. %s";
    private static final String DOCTOR_FULL_NAME_WITHOUT_TITLE = "%s";

    //instances
    Expert expert;
    VideoSession session;

    public DetailVideoView(Expert expert, VideoSession session) {
        this.expert = expert;
        this.session = session;
    }

    @Override
    public String getDocName() {
        if (expert.getTitle() != null && expert.getTitle().equals("null"))
            return String.format(Locale.getDefault(), DOCTOR_FULL_NAME_WITH_TITLE, expert.getTitle(), expert.getName());
        else
            return String.format(Locale.getDefault(), DOCTOR_FULL_NAME_WITHOUT_TITLE, expert.getName());
    }

    @Override
    public String getSpecialisation() {
        return expert.getSpeciality();
    }

    @Override
    public Double getTotalPrice() {
        return 0.0d;
    }

    @Override
    public String getType() {
        return "Video";
    }

    @Override
    public Date getDate() {
        return session.getStart();
    }

    @Override
    public String getLocation() {
        return "Video";
    }

    @Override
    public String getAppointmentNo() {
        return "";
    }

    @Override
    public Double getDoctorFee() {
        return 0.0d;
    }

    @Override
    public Double getHospitalFee() {
        return 0.0d;
    }

    @Override
    public Double getPlatformFee() {
        return 0.0;
    }

    @Override
    public Double getVatFee() {
        return 0.0d;
    }

    @Override
    public Double getBookingFee() {
        return null;
    }

    @Override
    public DownloadDataBuilder getAddDownloadBuilder() {
        return null;
    }

    @Override
    public DownloadDataBuilder getRemoveDownloadBuilder(Activity activity) {
        return null;
    }

    @Override
    public boolean readData(Activity activity, String response) {
        return false;
    }

    @Override
    public void onFinish(Activity activity) {
        activity.finish();
    }

    @Override
    public String getDoctorNote() {
        return null;
    }

    @Override
    public boolean hasData(Activity activity) {
        return false;
    }

    @Override
    public String getTime() {
        return null;
    }
}

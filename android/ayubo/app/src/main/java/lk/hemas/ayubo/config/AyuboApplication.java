package lk.hemas.ayubo.config;

import android.app.Application;

import java.util.List;

import lk.hemas.ayubo.model.Booking;
import lk.hemas.ayubo.model.ChannelDoctor;
import lk.hemas.ayubo.model.Session;

/**
 * Created by Sabri on 3/19/2018. Application class for Ayubo app
 */

public class AyuboApplication extends Application {

    List<Session> channelDoctors;
    private Booking booking;

    public List<Session> getChannelDoctors() {
        return channelDoctors;
    }

    public void setChannelDoctors(List<Session> channelDoctors) {
        this.channelDoctors = channelDoctors;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}

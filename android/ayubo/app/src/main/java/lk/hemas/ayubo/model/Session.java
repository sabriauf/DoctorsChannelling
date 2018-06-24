package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/19/2018. model for Sessions
 */

public class Session implements Serializable {
    private String status;
    private String day;
    private String time;
    private String month;
    private String year;
    private String show_date;
    private int next_appointment_no;
    private Info[] booking_info;

    public class Info implements Serializable {
        private String id;
        private double amount_local;
        private double amount_foreign;
        private String from;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getAmount_local() {
            return amount_local;
        }

        public void setAmount_local(double amount_local) {
            this.amount_local = amount_local;
        }

        public double getAmount_foreign() {
            return amount_foreign;
        }

        public void setAmount_foreign(double amount_foreign) {
            this.amount_foreign = amount_foreign;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getShow_date() {
        return show_date;
    }

    public void setShow_date(String show_date) {
        this.show_date = show_date;
    }

    public int getNext_appointment_no() {
        return next_appointment_no;
    }

    public void setNext_appointment_no(int next_appointment_no) {
        this.next_appointment_no = next_appointment_no;
    }

    public Info[] getBooking_info() {
        return booking_info;
    }

    public void setBooking_info(Info[] booking_info) {
        this.booking_info = booking_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package lk.hemas.ayubo.model;

import java.io.Serializable;

public class Booking implements Serializable{

    private BookingAppointment appointment;
    private BookingSession session;
    private Price price;
    private Payment payment;

    public BookingAppointment getAppointment() {
        return appointment;
    }

    public void setAppointment(BookingAppointment appointment) {
        this.appointment = appointment;
    }

    public BookingSession getBookingSession() {
        return session;
    }

    public void setBookingSession(BookingSession bookingSession) {
        this.session = bookingSession;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public class BookingAppointment implements Serializable{
        private int ref;
        private int appointment_no;
        private String doctor_name;
        private String doctor_code;
        private String hospital_name;
        private String hospital_id;
        private String specialization_name;
        private String specialization_id;
        private String from;

        public int getRef() {
            return ref;
        }

        public void setRef(int ref) {
            this.ref = ref;
        }

        public int getAppointment_no() {
            return appointment_no;
        }

        public void setAppointment_no(int appointment_no) {
            this.appointment_no = appointment_no;
        }

        public String getDoctor_name() {
            return doctor_name;
        }

        public void setDoctor_name(String doctor_name) {
            this.doctor_name = doctor_name;
        }

        public String getDoctor_code() {
            return doctor_code;
        }

        public void setDoctor_code(String doctor_code) {
            this.doctor_code = doctor_code;
        }

        public String getHospital_name() {
            return hospital_name;
        }

        public void setHospital_name(String hospital_name) {
            this.hospital_name = hospital_name;
        }

        public String getHospital_id() {
            return hospital_id;
        }

        public void setHospital_id(String hospital_id) {
            this.hospital_id = hospital_id;
        }

        public String getSpecialization_name() {
            return specialization_name;
        }

        public void setSpecialization_name(String specialization_name) {
            this.specialization_name = specialization_name;
        }

        public String getSpecialization_id() {
            return specialization_id;
        }

        public void setSpecialization_id(String specialization_id) {
            this.specialization_id = specialization_id;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }

    public class BookingSession implements Serializable{
        private String date;
        private String time;
        private String interval;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }
    }

    public class Price implements Serializable{
        private int total;
        private double doctor_fee;
        private double hospital_fee;
        private double platform_fee;
        private double vat;
        private double booking_charge;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public double getDoctor_fee() {
            return doctor_fee;
        }

        public void setDoctor_fee(double doctor_fee) {
            this.doctor_fee = doctor_fee;
        }

        public double getHospital_fee() {
            return hospital_fee;
        }

        public void setHospital_fee(double hospital_fee) {
            this.hospital_fee = hospital_fee;
        }

        public double getPlatform_fee() {
            return platform_fee;
        }

        public void setPlatform_fee(double platform_fee) {
            this.platform_fee = platform_fee;
        }

        public double getVat() {
            return vat;
        }

        public void setVat(double vat) {
            this.vat = vat;
        }

        public double getBooking_charge() {
            return booking_charge;
        }

        public void setBooking_charge(double booking_charge) {
            this.booking_charge = booking_charge;
        }
    }

    public class Payment implements Serializable{
        private String hnb_payment_link;

        public String getHnb_payment_link() {
            return hnb_payment_link;
        }

        public void setHnb_payment_link(String hnb_payment_link) {
            this.hnb_payment_link = hnb_payment_link;
        }
    }
}

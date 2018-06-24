package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.model.Booking;

public class ResultActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_BOOKING_OBJECT = "booking_object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Booking booking = readData(getIntent());
        if (booking != null)
            initViews(booking);
        else
            finish();
    }

    private Booking readData(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_BOOKING_OBJECT))
            return  (Booking) intent.getExtras().getSerializable(EXTRA_BOOKING_OBJECT);
        return null;
    }

    private void initViews(Booking booking) {
        View doctorView = findViewById(R.id.layout_result_doctor);
        View dateView = findViewById(R.id.layout_result_date);
        View timeView = findViewById(R.id.layout_result_time);
        View appointmentView = findViewById(R.id.layout_result_appointment);
        View locationView = findViewById(R.id.layout_result_location);

        setButton(doctorView, getString(R.string.doctor_result), booking.getAppointment().getDoctor_name());
        setButton(dateView, getString(R.string.date_result), booking.getBookingSession().getDate());
        setButton(timeView, getString(R.string.time_result), booking.getBookingSession().getTime());
        setButton(appointmentView, getString(R.string.appointment_result), String.valueOf(booking.getAppointment().getAppointment_no()));
        setButton(locationView, getString(R.string.location_result), booking.getAppointment().getHospital_name());

        findViewById(R.id.txt_result_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setButton(View view, String title, String value) {
        TextView txtTitle = view.findViewById(R.id.txt_tm_row_title);
        TextView txtValue = view.findViewById(R.id.txt_tm_row_value);

        txtTitle.setText(title);
        txtValue.setText(value);
    }
}

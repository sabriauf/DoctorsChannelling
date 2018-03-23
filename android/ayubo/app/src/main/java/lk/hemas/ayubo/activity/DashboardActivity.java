package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lk.hemas.ayubo.adapter.DoctorsAdapter;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.config.Constants;
import lk.hemas.ayubo.model.Doctor;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setView();
    }

    private void setView() {
        View view_video = findViewById(R.id.layout_video_call_button);
        setButton(view_video, getString(R.string.video_call_title), ContextCompat.getDrawable(this, R.drawable.video));
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_channel = findViewById(R.id.layout_channel_doctor_button);
        setButton(view_channel, getString(R.string.channel_doctor_title), ContextCompat.getDrawable(this, R.drawable.doctor));
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, VisitDoctorActivity.class));
            }
        });
        View view_report = findViewById(R.id.layout_report_button);
        setButton(view_report, getString(R.string.view_report_title), ContextCompat.getDrawable(this, R.drawable.clipboard));
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_ask = findViewById(R.id.layout_ask_button);
        setButton(view_ask, getString(R.string.ask_title), ContextCompat.getDrawable(this, R.drawable.question));
        view_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setRecyclerView();
    }

    private void setRecyclerView() {
        RecyclerView recycler_doctors = findViewById(R.id.recycler_doctors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_doctors.setLayoutManager(linearLayoutManager);
        DoctorsAdapter adapter = new DoctorsAdapter(this, new ArrayList<Doctor>());
        adapter.setOnDoctorClickListener(new DoctorsAdapter.OnDoctorClickListener() {
            @Override
            public void onDoctorClick(Doctor doctor) {
                Intent intent = new Intent(DashboardActivity.this, DoctorActivity.class);
                intent.putExtra(Constants.EXTRA_DOCTOR_OBJECT, doctor);
                startActivity(intent);
            }
        });
        recycler_doctors.setAdapter(adapter);
    }

    private void setButton(View view, String name, Drawable drawable) {
        ((ImageView) view.findViewById(R.id.img_icon_button)).setImageDrawable(drawable);
        ((TextView) view.findViewById(R.id.txt_name_button)).setText(name);
    }
}

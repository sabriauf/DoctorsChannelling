package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.ChannelDoctorAdapter;
import lk.hemas.ayubo.config.AyuboApplication;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.util.DatePickerFragment;
import lk.hemas.ayubo.util.TimeFormatter;

public class ScheduleActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_LOCATION_NAME = "location_name";

    //instances
    private List<Session> sessions;
    private ChannelDoctorAdapter adapter;

    //views
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            String location = getIntent().getExtras().getString(EXTRA_LOCATION_NAME);
            List<Session> channelDoctors = ((AyuboApplication) getApplication()).getChannelDoctors();
            if (channelDoctors != null && channelDoctors.size() > 0) {
                setView(location, channelDoctors.get(0).getDocName(), channelDoctors.get(0).getSpecialisation());
                readSessions(channelDoctors, location);
            }
        }
    }

    private void setView(String location, String name, String specialty) {
        progressBar = findViewById(R.id.progress_loading_schedule);
        ((TextView) findViewById(R.id.txt_doctor_name_schedule)).setText(name);
        ((TextView) findViewById(R.id.txt_specialty_schedule)).setText(specialty);
        ((TextView) findViewById(R.id.txt_location_schedule)).setText(location);
        findViewById(R.id.btn_calendar_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                    @Override
                    public void onSelected(Calendar calendar) {
                        filterByDate(TimeFormatter.millisecondsToString(calendar.getTimeInMillis(), TimeFormatter.SCHEDULE_DATE_FORMAT));
                    }
                });
            }
        });
    }

    private void readSessions(final List<Session> dataArray, final String location) {

        progressBar.setVisibility(View.VISIBLE);
//        errorView.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                sessions = new ArrayList<>();

                if (dataArray != null && dataArray.size() > 0) {
                    for (Session session : dataArray) {
                        if (session.getHospital().equals(location))
                            sessions.add(session);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        setRecyclerView();
                    }
                });
            }
        }).run();
    }

    private void setRecyclerView() {
        RecyclerView recycler_schedules = findViewById(R.id.recycler_schedule);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_schedules.setLayoutManager(linearLayoutManager);
        adapter = new ChannelDoctorAdapter(this, sessions, ChannelDoctorAdapter.VIEW_TYPE_SESSION);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Session channelDoctor) {
                Intent intent = new Intent(ScheduleActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_SESSION, channelDoctor);
                startActivity(intent);
            }

            @Override
            public void onGetMoreSessionsClicked(String location) {
            }
        });
        recycler_schedules.setAdapter(adapter);
    }

    private void filterByDate(String date) {
        List<Session> channelDoctors = ((AyuboApplication) getApplication()).getChannelDoctors();
        List<Session> filteredList = new ArrayList<>();

        for(Session session: channelDoctors) {
            if(session.getDate().equals(date))
                filteredList.add(session);
        }

        sessions.clear();
        sessions.addAll(filteredList);
        adapter.notifyDataSetChanged();

    }
}

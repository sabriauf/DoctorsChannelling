package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.ChannelDoctorAdapter;
import lk.hemas.ayubo.adapter.DoctorsAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.config.Constants;
import lk.hemas.ayubo.model.DocSessionParameters;
import lk.hemas.ayubo.model.Doctor;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.util.DatePickerFragment;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;

public class VideoRequestActivity extends AppCompatActivity {

    //instances
    private ChannelDoctorAdapter sessionAdapter;

    //views
    private TextView txtDoctorName, txtDoctorSpecialty;
    private EditText edtSearch;
    private ProgressBar progressDoctors, progressSessions;
    private RecyclerView recyclerDoctors, recyclerSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_reqest);

        initView();
        setButtons();
        setDoctorDetails("", "");
        setSessionRecyclerView();
        setDoctorsRecyclerView();
        getData(new DocSessionParameters());
    }

    private void initView() {
        txtDoctorName = findViewById(R.id.txt_name_doctor_request);
        txtDoctorSpecialty = findViewById(R.id.txt_specialty_doctor_request);
        progressDoctors = findViewById(R.id.progress_loading_doctors_request);
        progressSessions = findViewById(R.id.progress_loading_sessions_request);
        edtSearch = findViewById(R.id.edt_search_doctor_request);
        edtSearch.setFocusable(false);
        findViewById(R.id.btn_calendar_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                    @Override
                    public void onSelected(Calendar calendar) {
//                        filterByDate(TimeFormatter.millisecondsToString(calendar.getTimeInMillis(), TimeFormatter.SCHEDULE_DATE_FORMAT));
                    }
                });
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerDoctors = findViewById(R.id.recycler_doctors_request);
        recyclerSchedules = findViewById(R.id.recycler_schedules_request);
    }

    private void setButtons() {
        CircleImageView view_video = findViewById(R.id.img_video_button);
        view_video.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.video));
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        CircleImageView view_channel = findViewById(R.id.img_channel_button);
        view_channel.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doctor));
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        CircleImageView view_report = findViewById(R.id.img_review_button);
        view_report.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.clipboard));
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setDoctorDetails(String name, String specialty) {
        txtDoctorName.setText(name);
        txtDoctorSpecialty.setText(specialty);
    }

    private void setSessionRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerSchedules.setLayoutManager(linearLayoutManager);
        sessionAdapter = new ChannelDoctorAdapter(this, new ArrayList<Session>(), ChannelDoctorAdapter.VIEW_TYPE_SESSION);
        sessionAdapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Session channelDoctor) {
                Intent intent = new Intent(VideoRequestActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_SESSION, channelDoctor);
                startActivity(intent);
            }

            @Override
            public void onGetMoreSessionsClicked(String location) {
            }
        });
        recyclerSchedules.setAdapter(sessionAdapter);
    }

    private void setDoctorsRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerDoctors.setLayoutManager(linearLayoutManager);
        DoctorsAdapter adapter = new DoctorsAdapter(this, new ArrayList<Doctor>());
        adapter.setOnDoctorClickListener(new DoctorsAdapter.OnDoctorClickListener() {
            @Override
            public void onDoctorClick(Doctor doctor) {
                Intent intent = new Intent(VideoRequestActivity.this, DoctorActivity.class);
                intent.putExtra(Constants.EXTRA_DOCTOR_OBJECT, doctor);
                startActivity(intent);
            }
        });
        recyclerDoctors.setAdapter(adapter);
    }

    private void getData(DocSessionParameters parameters) {
        progressSessions.setVisibility(View.VISIBLE);
        parameters.setToDate("");
        parameters.setDocId("1164");
        parameters.setFromDate(""); //TODO - for testing carded values

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_SESSION_LIST, 0, DownloadManager.POST_REQUEST).
                setParams(parameters.getSearchParams()).setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(VisitDoctorActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressSessions.setVisibility(View.GONE);
//                        List<Session> doctors = readSessions(response);
//                        if (doctors != null && doctors.size() > 0) {
//                            ((AyuboApplication) getApplication()).setChannelDoctors(doctors);
//                            readLocations(doctors);
//                        } else
//                            showErrorView(getString(R.string.no_appointment));
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressSessions.setVisibility(View.GONE);
//                        showErrorView(getString(R.string.server_error));
                    }
                }).execute();
    }
}

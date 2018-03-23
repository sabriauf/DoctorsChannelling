package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.ChannelDoctorAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.config.AyuboApplication;
import lk.hemas.ayubo.model.DocSessionParameters;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;

public class DoctorActivity extends AppCompatActivity {

    //constant
    public static final String EXTRA_APPOINTMENTS = "extra_appointments";

    //instances
    private DocSessionParameters params;

    //views
    private ProgressBar progressBar;
    private View errorView;
    private TextView txtDocName, txtDocSpecialty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        readExtras();
    }

    private void readExtras() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRA_APPOINTMENTS)) {
            params = (DocSessionParameters) getIntent().getExtras().getSerializable(EXTRA_APPOINTMENTS);
            setView();
            getData(params);
        }
    }

    private void setView() {
        progressBar = findViewById(R.id.progress_loading_doctor);
        errorView = findViewById(R.id.layout_error);
        errorView.findViewById(R.id.btn_try_again_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(params);
            }
        });

        txtDocName = findViewById(R.id.txt_name_doctor);
        txtDocSpecialty = findViewById(R.id.txt_specialty_doctor);

        txtDocName.setText("");
        txtDocSpecialty.setText("");

        setButtons();
    }

    private void setButtons() {
        View view_video = findViewById(R.id.layout_video_button);
        setButton(view_video, getString(R.string.video_short_title), ContextCompat.getDrawable(this, R.drawable.video));
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_channel = findViewById(R.id.layout_channel_button);
        setButton(view_channel, getString(R.string.channel), ContextCompat.getDrawable(this, R.drawable.doctor));
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_report = findViewById(R.id.layout_review_button);
        setButton(view_report, getString(R.string.review), ContextCompat.getDrawable(this, R.drawable.clipboard));
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_ask = findViewById(R.id.layout_ask_button);
        setButton(view_ask, getString(R.string.ask), ContextCompat.getDrawable(this, R.drawable.question));
        view_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setRecyclerView(List<Session> channelDoctorList) {
        RecyclerView recycler_schedules = findViewById(R.id.recycler_available_schedules);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_schedules.setLayoutManager(linearLayoutManager);
        ChannelDoctorAdapter adapter = new ChannelDoctorAdapter(this, channelDoctorList,  ChannelDoctorAdapter.VIEW_TYPE_CHANNEL);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Session channelDoctor) {
                Intent intent = new Intent(DoctorActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_SESSION, channelDoctor);
                startActivity(intent);
            }

            @Override
            public void onGetMoreSessionsClicked(String location) {
                Intent intent = new Intent(DoctorActivity.this, ScheduleActivity.class);
                intent.putExtra(ScheduleActivity.EXTRA_LOCATION_NAME, location);
                startActivity(intent);
            }
        });
        recycler_schedules.setAdapter(adapter);
    }

    private void setButton(View view, String name, Drawable drawable) {
        ((ImageView) view.findViewById(R.id.img_profile_doctor_row)).setImageDrawable(drawable);
        ((TextView) view.findViewById(R.id.txt_name_doctor_row)).setText(name);
    }

    private void readLocations(final List<Session> dataArray) {

        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Session> locations = new ArrayList<>();

                if (dataArray != null && dataArray.size() > 0) {
                    mainLoop:
                    for (Session channelDoctor : dataArray) {
                        for (Session temp : locations) {
                            if (temp.getHospital().equals(channelDoctor.getHospital()))
                                continue mainLoop;
                        }
                        locations.add(channelDoctor);
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            txtDocName.setText(locations.get(0).getDocName());
                            txtDocSpecialty.setText(locations.get(0).getSpecialisation());
                            setRecyclerView(locations);
                        }
                    });
                }
            }
        }).run();
    }

    private void getData(DocSessionParameters parameters) {
        parameters.setToDate("");
        parameters.setDocId("1164");
        parameters.setFromDate(""); //TODO - for testing carded values

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_SESSION_LIST, 0, DownloadManager.POST_REQUEST).
                setParams(parameters.getSearchParams()).setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(VisitDoctorActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        List<Session> doctors = readSessions(response);
                        if (doctors != null && doctors.size() > 0) {
                            ((AyuboApplication) getApplication()).setChannelDoctors(doctors);
                            readLocations(doctors);
                        } else
                            showErrorView(getString(R.string.no_appointment));
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        showErrorView(getString(R.string.server_error));
                    }
                }).execute();
    }

    private void showErrorView(String message) {
        errorView.setVisibility(View.VISIBLE);
        ((TextView) errorView.findViewById(R.id.txt_message_error)).setText(message);
        errorView.startAnimation(AnimationUtils.loadAnimation(DoctorActivity.this, R.anim.anim_shake));
    }

    private List<Session> readSessions(String respond) {
        List<Session> dataArray = null;
        try {
            JSONObject jsonObject = new JSONObject(respond);

            Type messageType = new TypeToken<List<Session>>() {
            }.getType();

            dataArray = new Gson().fromJson(jsonObject.getJSONObject("ArrayOfChanelling").getJSONArray("Chanelling")
                    .toString(), messageType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataArray;
    }
}

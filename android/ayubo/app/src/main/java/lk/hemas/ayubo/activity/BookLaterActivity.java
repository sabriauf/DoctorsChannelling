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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.VideoSessionAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Expert;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DatePickerFragment;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;
import lk.hemas.ayubo.util.TimeFormatter;

public class BookLaterActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_EXPERT_OBJECT = "expert_object";

    //instances
    private List<Expert.Location> locations;
    private VideoSessionAdapter adapter;
    private Expert expert;

    //views
    private TextView txtName, txtSpeciality;
    private CircleImageView imgProfile;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_later);

        initViews();
        setButtons();
        readExtra(getIntent());
    }

    private void initViews() {
        txtName = findViewById(R.id.txt_later_name);
        txtSpeciality = findViewById(R.id.txt_later_specialty);
        imgProfile = findViewById(R.id.img_later_doctor);
        recyclerView = findViewById(R.id.recycler_later_schedules);
        progressBar = findViewById(R.id.prg_later_progress);

        findViewById(R.id.img_later_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
                newFragment.setMinDate(Calendar.getInstance());
                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                    @Override
                    public void onSelected(Calendar calendar) {
                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        filterByDate(calendar.getTimeInMillis());
                    }
                });
            }
        });
    }

    private void readExtra(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_EXPERT_OBJECT))
            setExpertDetails((Expert) intent.getExtras().getSerializable(EXTRA_EXPERT_OBJECT));
    }

    private void setExpertDetails(Expert expert) {
        this.expert = expert;

        txtName.setText(String.format("%s %s", expert.getTitle(), expert.getName()));
        txtSpeciality.setText(expert.getSpeciality());
        Glide.with(this).load(expert.getPicture()).into(imgProfile);

        locations = new ArrayList<>(expert.getLocations());

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new VideoSessionAdapter(this, locations);
        recyclerView.setAdapter(adapter);
        adapter.setOnVideoSessionListener(new VideoSessionAdapter.OnVideoSessionListener() {
            @Override
            public void onVideoSessionClicked(Expert.Location location) {
                if (progressBar.getVisibility() == View.GONE)
                    createAnAppointment(location);
            }
        });
    }

    private void setButtons() {
        View view_video = findViewById(R.id.layout_video_button);
        setButton(view_video, getString(R.string.video_short_title), ContextCompat.getDrawable(this, R.drawable.video_call_small), false);
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_channel = findViewById(R.id.layout_channel_button);
        setButton(view_channel, getString(R.string.channel), ContextCompat.getDrawable(this, R.drawable.channel_small_selected), true);
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_report = findViewById(R.id.layout_review_button);
        setButton(view_report, getString(R.string.review), ContextCompat.getDrawable(this, R.drawable.review_small), false);
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_ask = findViewById(R.id.layout_ask_button);
        setButton(view_ask, getString(R.string.ask), ContextCompat.getDrawable(this, R.drawable.ask_small), false);
        view_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setButton(View view, String name, Drawable drawable, boolean isSelected) {
        ((ImageView) view.findViewById(R.id.img_profile_doctor_row)).setImageDrawable(drawable);
        TextView txtTitle = view.findViewById(R.id.txt_name_doctor_row);
        txtTitle.setTextSize(12);
        txtTitle.setText(name);
        txtTitle.setTextColor(ContextCompat.getColor(this, isSelected ? R.color.text_color_secondary : R.color.text_color_primary));
    }

    private void filterByDate(final long time) {
        locations.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Expert.Location location : expert.getLocations()) {
                    long available = TimeFormatter.stringToDate(location.getNext_available(), "yyyy-MM-dd hh:mm:ss").getTime();
                    if (available > time) {
                        locations.add(location);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void createAnAppointment(Expert.Location location) {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_VIDEO_APPIONTMENT, new AppointmentParams(location).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("result") == 0) {
                                //TODO - send to payment
                                return;
                            } else {
                                Toast.makeText(BookLaterActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(BookLaterActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(BookLaterActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    class AppointmentParams extends SoapBasicParams {

        private String locationID;
        private String doctorID;
        private String start;
        private String patientID;

        AppointmentParams(Expert.Location location) {
            locationID = location.getId();
            doctorID = expert.getId();
            start = location.getNext_available();
            patientID = "";
        }

        public String getSearchParams(String query) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("locationID", locationID);
                jsonObject.put("doctorID", doctorID);
                jsonObject.put("start", start);
                jsonObject.put("patientID", patientID);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}

package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lk.hemas.ayubo.adapter.DoctorsAdapter;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.DocSearchParameters;
import lk.hemas.ayubo.model.Doctor;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;
import lk.hemas.ayubo.view.SelectDoctorAction;

public class DashboardActivity extends AppCompatActivity {

    //views
    private ShimmerRecyclerView recycler_doctors;
    private View errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setView();
    }

    private void setView() {
        recycler_doctors = findViewById(R.id.recycler_doctors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_doctors.setLayoutManager(linearLayoutManager);
        recycler_doctors.showShimmerAdapter();

        View view_video = findViewById(R.id.layout_video_call_button);
        setButton(view_video, getString(R.string.video_call_title), ContextCompat.getDrawable(this, R.drawable.video_call),
                getString(R.string.video_call_message));
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, VideoRequestActivity.class));
            }
        });
        View view_channel = findViewById(R.id.layout_channel_doctor_button);
        setButton(view_channel, getString(R.string.channel_doctor_title), ContextCompat.getDrawable(this, R.drawable.channel_a_doctor),
                getString(R.string.channelling_message));
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, VisitDoctorActivity.class));
            }
        });
        View view_report = findViewById(R.id.layout_report_button);
        setButton(view_report, getString(R.string.view_report_title), ContextCompat.getDrawable(this, R.drawable.review),
                getString(R.string.review_message));
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        View view_ask = findViewById(R.id.layout_ask_button);
        setButton(view_ask, getString(R.string.ask_title), ContextCompat.getDrawable(this, R.drawable.ask),
                getString(R.string.ask_message));
        view_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        errorView = findViewById(R.id.layout_error);
        errorView.findViewById(R.id.btn_try_again_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavorites();
            }
        });


//        Spinner spnUsers = findViewById(R.id.spn_user_dashboard);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnUsers.setAdapter(dataAdapter);

        findViewById(R.id.txt_dashboard_bookings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
            }
        });

        getFavorites();
    }

    private void getFavorites() {
        errorView.setVisibility(View.GONE);
        recycler_doctors.showShimmerAdapter();

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_FAVORITES, new SoapBasicParams().getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        recycler_doctors.hideShimmerAdapter();
                        setDoctorsAdapter(readFavoriteDoctors(response));
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        recycler_doctors.hideShimmerAdapter();
//                        errorView.setVisibility(View.VISIBLE);
//                        errorView.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.anim_shake));
                    }
                }).execute();
    }

    private List<Doctor> readFavoriteDoctors(String response) {
        List<Doctor> dataArray = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            Type messageType = new TypeToken<List<Doctor>>() {
            }.getType();

            dataArray = new Gson().fromJson(jsonObject.getJSONArray("data")
                    .toString(), messageType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (dataArray == null)
            dataArray = new ArrayList<>();
        dataArray.add(new Doctor());

        return dataArray;
    }

    private void setDoctorsAdapter(List<Doctor> doctors) {
        DoctorsAdapter adapter = new DoctorsAdapter(this, null, new ArrayList<Object>(doctors));
        adapter.setOnDoctorClickListener(new DoctorsAdapter.OnDoctorClickListener() {
            @Override
            public void onDoctorClick(Object object) {
                Doctor doctor = (Doctor) object;

                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);

                DocSearchParameters params;

                if (doctor.getId() != 0) {
                    params = new DocSearchParameters();
//                    params.setDate(TimeFormatter.millisecondsToString(System.currentTimeMillis(), TimeFormatter.DATE_FORMAT_VIDEO));
                    params.setDate("");
                    params.setSpecializationId(doctor.getSpecialization_id());
                    params.setDoctorId(String.valueOf(doctor.getId()));
                    params.setLocationId(doctor.getHospital_id());
                } else {
                    params = null;
                }

                intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(params));
                startActivity(intent);
            }
        });
        recycler_doctors.setAdapter(adapter);
    }

    private void setButton(View view, String name, Drawable drawable, String message) {
        ((ImageView) view.findViewById(R.id.img_icon_button)).setImageDrawable(drawable);
        ((TextView) view.findViewById(R.id.txt_name_button)).setText(name);
        ((TextView) view.findViewById(R.id.txt_desc_button)).setText(message);
    }
}

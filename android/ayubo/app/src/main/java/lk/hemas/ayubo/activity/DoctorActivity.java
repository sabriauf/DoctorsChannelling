package lk.hemas.ayubo.activity;

import android.animation.ValueAnimator;
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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.ChannelDoctorAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.ChannelDoctor;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Hospital;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;

public class DoctorActivity extends AppCompatActivity {

    //constant
    public static final String EXTRA_DOCTOR_DETAILS = "doctor_details";

    //instances
    private ChannelDoctor doctor;

    //views
//    private ProgressBar progressBar;
    private View errorView;
    private LottieAnimationView imgFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        readExtras();
    }

    private void readExtras() {
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(EXTRA_DOCTOR_DETAILS)) {
                Object object = getIntent().getExtras().getSerializable(EXTRA_DOCTOR_DETAILS);
                if (object != null)
                    if (object instanceof ChannelDoctor) {
                        doctor = (ChannelDoctor) object;
                        setView(doctor.getDoctor_name(), doctor.getSpecialization(), doctor.getDoc_image());
                        setRecyclerView(new ArrayList<>(doctor.getHospitals().values()));
                    }
            }
    }


    private void setView(String name, String specialty, String imgUrl) {
//        progressBar = findViewById(R.id.progress_loading_doctor);
        errorView = findViewById(R.id.layout_error);
        errorView.findViewById(R.id.btn_try_again_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getData();
            }
        });

        TextView txtDocName = findViewById(R.id.txt_name_doctor);
        TextView txtDocSpecialty = findViewById(R.id.txt_specialty_doctor);
        imgFavorite = findViewById(R.id.img_favorite_doctor);
        CircleImageView imgDoctor = findViewById(R.id.img_profile_doctor);

        txtDocName.setText(name);
        txtDocSpecialty.setText(specialty);
        Glide.with(this).load(imgUrl).into(imgDoctor);

        if (!doctor.getFavourite().equals("no")) startCheckAnimation();
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCheckAnimation();
                if (doctor.getFavourite().equals("no")) {
                    addFavorites();
                    doctor.setFavourite("yes");
                } else {
                    removeFavorites();
                    doctor.setFavourite("no");
                }
            }
        });

        setButtons();
    }


    private void startCheckAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                imgFavorite.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        if (imgFavorite.getProgress() == 0f) {
            animator.start();
        } else {
            imgFavorite.setProgress(0f);
        }
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

    private void setRecyclerView(final List<Hospital> locations) {
        RecyclerView recycler_schedules = findViewById(R.id.recycler_available_schedules);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_schedules.setLayoutManager(linearLayoutManager);
        ChannelDoctorAdapter adapter = new ChannelDoctorAdapter(this, new ArrayList<Object>(locations), ChannelDoctorAdapter.VIEW_TYPE_HOSPITAL);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Object channelDoctor) {
                Hospital hospital = (Hospital) channelDoctor;
                Intent intent = new Intent(DoctorActivity.this, ScheduleActivity.class);
                intent.putExtra(ScheduleActivity.EXTRA_LOCATION_DETAILS, hospital);
                intent.putExtra(ScheduleActivity.EXTRA_LOCATION_LIST, new ArrayList<>(locations));
                intent.putExtra(ScheduleActivity.EXTRA_DOCTOR_DETAILS, doctor);
                startActivity(intent);
            }

//            @Override
//            public void onGetMoreSessionsClicked(String location) {
////                Intent intent = new Intent(DoctorActivity.this, ScheduleActivity.class);
////                intent.putExtra(ScheduleActivity.EXTRA_LOCATION_NAME, location);
////                startActivity(intent);
//            }
        });
        recycler_schedules.setAdapter(adapter);
    }

    private void setButton(View view, String name, Drawable drawable, boolean isSelected) {
        ((ImageView) view.findViewById(R.id.img_profile_doctor_row)).setImageDrawable(drawable);
        TextView txtTitle = view.findViewById(R.id.txt_name_doctor_row);
        txtTitle.setText(name);
        txtTitle.setTextColor(ContextCompat.getColor(this, isSelected ? R.color.text_color_secondary : R.color.text_color_primary));
    }

    private void addFavorites() {
        errorView.setVisibility(View.GONE);

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_ADD_FAVORITE, new FavoriteParams(
                        String.valueOf(doctor.getDoctor_code()), String.valueOf(doctor.getSpecialization_id())).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        Toast.makeText(DoctorActivity.this, String.format(Locale.getDefault(),
                                getString(R.string.added_favourite), doctor.getDoctor_name()), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(DoctorActivity.this, R.string.failed_add_favourite, Toast.LENGTH_LONG).show();
                        startCheckAnimation();
                    }
                }).execute();
    }

    private void removeFavorites() {
        errorView.setVisibility(View.GONE);

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_REMOVE_FAVORITE, new FavoriteParams(
                        String.valueOf(doctor.getDoctor_code()), String.valueOf(doctor.getSpecialization_id())).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        Toast.makeText(DoctorActivity.this, String.format(Locale.getDefault(),
                                getString(R.string.remove_favourite), doctor.getDoctor_name()), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(DoctorActivity.this, R.string.failed_remove_favourite, Toast.LENGTH_LONG).show();
                        startCheckAnimation();
                    }
                }).execute();
    }

    class FavoriteParams extends SoapBasicParams {
        private String doctorId;
        private String specialization_id;
//        private String description;

        FavoriteParams(String docId, String specId) {
            this.doctorId = docId;
//            this.description = desc;
            this.specialization_id = specId;
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userId", user_id);
                jsonObject.put("doctorId", doctorId);
                jsonObject.put("specialization_id", specialization_id);
                jsonObject.put("hospital_id", "");
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

//    class RemoveFavoriteParams extends SoapBasicParams {
//        private String doctorId;
//        private String specialization_id;
//
//        RemoveFavoriteParams(String docId, String specialization) {
//            this.doctorId = docId;
//            this.specialization_id = specialization;
//        }
//
//        public String getSearchParams() {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("user_id", user_id);
//                jsonObject.put("doctorId", doctorId);
//                jsonObject.put("specialization_id", specialization_id);
//                jsonObject.put("token_key", token_key);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return jsonObject.toString();
//        }
//    }
}

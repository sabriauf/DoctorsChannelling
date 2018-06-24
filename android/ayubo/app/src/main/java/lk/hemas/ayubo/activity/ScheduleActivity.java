package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.ChannelDoctorAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.config.AyuboApplication;
import lk.hemas.ayubo.model.Appointment;
import lk.hemas.ayubo.model.ChannelDoctor;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Hospital;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.model.SessionParent;
import lk.hemas.ayubo.model.SessionSearchParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DatePickerFragment;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;
import lk.hemas.ayubo.util.TimeFormatter;

public class ScheduleActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_LOCATION_LIST = "location_lists";
    public static final String EXTRA_DOCTOR_DETAILS = "doctor_details";
    public static final String EXTRA_LOCATION_DETAILS = "location_details";

    //instances
    private List<Session> sessions;
    private SessionParent parent;
    private ChannelDoctor doctor;
    private List<Hospital> locations;

    //primary data
    private boolean isInitial = true;

    //views
    private ProgressBar progressBar;
    private TextView txtNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            doctor = (ChannelDoctor) getIntent().getExtras().getSerializable(EXTRA_DOCTOR_DETAILS);
            Hospital hospital = (Hospital) getIntent().getExtras().getSerializable(EXTRA_LOCATION_DETAILS);
            locations = (List<Hospital>) getIntent().getExtras().getSerializable(EXTRA_LOCATION_LIST);
            if (doctor != null) {
                setView(hospital, doctor.getDoctor_name(), doctor.getSpecialization(), doctor.getDoc_image());
            }
        }
    }

    private void setView(final Hospital hospital, String name, String specialty, String imgUrl) {
        progressBar = findViewById(R.id.progress_loading_schedule);

        setBasicView(name, specialty, "", imgUrl);

        ImageView imgPlus = findViewById(R.id.img_calendar_schedule);
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
                newFragment.setMinDate(Calendar.getInstance());
                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                    @Override
                    public void onSelected(Calendar calendar) {
                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                        filterByDate(calendar.getTimeInMillis());
                    }
                });
            }
        });

        Spinner spnUsers = findViewById(R.id.spn_spinner_custom_view);
        spnUsers.setAdapter(new SpinnerAdapter(R.layout.custom_location_spinner_row, locations));
        spnUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isInitial) {
                    isInitial = false;
                    getData(doctor, hospital);
                } else
                    getData(doctor, locations.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnUsers.setSelection(getPosition(hospital));

        txtNoData = findViewById(R.id.txt_schedule_no_data);

    }

    private int getPosition(Hospital hospital) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getHospital_id().equals(hospital.getHospital_id()))
                return i;
        }
        return 0;
    }

    private void setBasicView(String name, String specialty, String note, String imgUrl) {
        CircleImageView imageView = findViewById(R.id.img_doc_doctor_info);
        try {
            Glide.with(this).load(imgUrl).into(imageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ((TextView) findViewById(R.id.txt_doctor_name_doctor_info)).setText(name);
        ((TextView) findViewById(R.id.txt_specialty_doctor_info)).setText(specialty);
        if (note == null || note.equals(""))
            ((TextView) findViewById(R.id.txt_note_doctor_info)).setText(R.string.special_note);
        else
            ((TextView) findViewById(R.id.txt_note_doctor_info)).setText(String.format(Locale.getDefault(),
                    "%session %session", getString(R.string.special_note), note));
    }

    private void readSessions(final List<Session> dataArray) {

        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                sessions = new ArrayList<>();

                if (dataArray != null && dataArray.size() > 0) {
                    parentThread:
                    for (Session session : dataArray) {
                        for (int i = 0; i < sessions.size(); i++) {
                            Session temp = sessions.get(i);
                            if (session.getShow_date().compareTo(temp.getShow_date()) < 0) {
                                sessions.add(i, session);
                                continue parentThread;
                            }
                        }
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

    private List<Session> readSessions(String respond, int docId) {
        List<Session> dataArray = null;
        try {
            JSONObject jsonObject = new JSONObject(respond);

            parent = new Gson().fromJson(jsonObject.getJSONObject("data").getJSONObject(String.valueOf(docId))
                    .toString(), SessionParent.class);

            setBasicView(parent.getDoctor_name(), parent.getSpecialization_name(), parent.getSpecial_notes(), parent.getDoc_image());

            if (parent.getSessions() != null)
                dataArray = new ArrayList<>(parent.getSessions().values());
            else
                dataArray = new ArrayList<>();

            if(parent.getSessions() == null || parent.getSessions().size() == 0)
                txtNoData.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataArray;
    }

    private void setRecyclerView() {
        RecyclerView recycler_schedules = findViewById(R.id.recycler_schedule);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_schedules.setLayoutManager(linearLayoutManager);
        ChannelDoctorAdapter adapter = new ChannelDoctorAdapter(this, new ArrayList<Object>(sessions), ChannelDoctorAdapter.VIEW_TYPE_SESSION);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Object channelDoctor) {
                Intent intent;
                Appointment appointment = new Appointment();
                appointment.setSession((Session) channelDoctor);
                appointment.setSessionParent(parent);

                intent = new Intent(ScheduleActivity.this, SourceActivity.class);
                intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
                startActivity(intent);
            }

//            @Override
//            public void onGetMoreSessionsClicked(String location) {
//            }
        });
        recycler_schedules.setAdapter(adapter);
    }

    private void filterByDate(Long date) {
        List<Session> channelDoctors = ((AyuboApplication) getApplication()).getChannelDoctors();
        List<Session> filteredList = new ArrayList<>();

        if (channelDoctors != null) {
            for (Session session : channelDoctors) {
                Long sessionDate = TimeFormatter.stringToDate(session.getShow_date(), TimeFormatter.DATE_FORMAT_VIDEO).getTime();
                if (sessionDate > date)
                    filteredList.add(session);
            }
        }

        sessions.clear();
        sessions.addAll(filteredList);
        setRecyclerView();

    }

    class SpinnerAdapter extends ArrayAdapter<Hospital> {

        private LayoutInflater mInflater;
        private List<Hospital> items;
        private int mRes;

        private SpinnerAdapter(int resource, @NonNull List<Hospital> objects) {
            super(ScheduleActivity.this, resource, objects);
            this.mInflater = LayoutInflater.from(ScheduleActivity.this);
            this.items = objects;
            this.mRes = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        private View createItemView(int position, View convertView, ViewGroup parent) {
            final View view = mInflater.inflate(mRes, parent, false);

            ImageView imgLocation = view.findViewById(R.id.img_location_spinner_row);
            TextView txtLocation = view.findViewById(R.id.txt_location_spinner_row);

            txtLocation.setText(items.get(position).getHospital_name());
            Glide.with(ScheduleActivity.this).load(items.get(position).getHospital_image()).into(imgLocation);

            return view;
        }
    }

    private void getData(final ChannelDoctor doctor, Hospital location) {

        progressBar.setVisibility(View.VISIBLE);
        txtNoData.setVisibility(View.GONE);

        if (sessions != null) {
            sessions.clear();
            setRecyclerView();
        }

        SessionSearchParams sessionParams = new SessionSearchParams();
        sessionParams.setDoctorId(String.valueOf(doctor.getDoctor_code()));
        sessionParams.setSpecializationId(String.valueOf(doctor.getSpecialization_id()));
        sessionParams.setLocationId(location.getHospital_id());
        sessionParams.setDirect(location.getDirect());
        sessionParams.setSource(location.getSource());

//        sessionParams.setDoctorId("966");
//        sessionParams.setSpecializationId("32");
//        sessionParams.setLocationId("150");
//        sessionParams.setDirect("");
//        sessionParams.setSource(new String[]{"echannelling"});

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_CHANNELLING_SESSION, sessionParams.getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        List<Session> sessions = readSessions(response, doctor.getDoctor_code());
                        if (sessions != null && sessions.size() > 0) {
                            ((AyuboApplication) getApplication()).setChannelDoctors(sessions);
                            readSessions(sessions);
                        }
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        showErrorView(getString(R.string.server_error));
                    }
                }).execute();
    }

    private void showErrorView(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

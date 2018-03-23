package lk.hemas.ayubo.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.SearchAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.util.TimeFormatter;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_SESSION = "extra_session";
    //Detail params
    private static final String TITLE_TYPE = "Type";
    private static final String TITLE_DATE = "Date";
    private static final String TITLE_TIME = "Time";
    private static final String TITLE_LOCATION = "Location";
    private static final String TITLE_APPOINTMENT = "Appointment Number";
    private static final String TITLE_DOCTOR_FEE = "Doctor Fee";
    private static final String TITLE_HOSPITAL_FEE = "Hospital Fee";
    private static final String TITLE_PLATFORM_FEE = "Platform Fee";

    //instances
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        readExtras();
        initView();
    }

    private void readExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(EXTRA_SESSION)) {
            session = (Session) bundle.getSerializable(EXTRA_SESSION);
        }
    }

    private void initView() {
        TextView txtDocName = findViewById(R.id.txt_doctor_name_details);
        TextView txtSpecialty = findViewById(R.id.txt_specialty_details);
        TextView txtPrice = findViewById(R.id.txt_price_details);
        TextView txtConfirm = findViewById(R.id.txt_confirm_details);

        if (session != null) {
            txtDocName.setText(session.getDocName());
            txtSpecialty.setText(session.getSpecialisation());
            Double totalPrice = session.getDoctorFee() + session.getHospitalFee() + session.getVATFee();
            txtPrice.setText(String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, totalPrice));

            txtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            RecyclerView recycler_search = findViewById(R.id.recycler_session_details);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_search.setLayoutManager(linearLayoutManager);
            SearchAdapter adapter = new SearchAdapter(this, getDetailList(), new DetailsActions());
            adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Object object) {
                }
            });
            recycler_search.setAdapter(adapter);
        }
    }

    private List<Object> getDetailList() {
        List<Object> objects = new ArrayList<>();

        objects.add(new DetailRow(TITLE_TYPE, "Channeling")); //TODO - set by object
        Date sessionDate = TimeFormatter.stringToDate(session.getDate(), TimeFormatter.SCHEDULE_DATE_FORMAT);
        objects.add(new DetailRow(TITLE_DATE, TimeFormatter.millisecondsToString(sessionDate.getTime(), TimeFormatter.TIME_FORMAT_APPOINTMENT_DATE)));
        objects.add(new DetailRow(TITLE_TIME, TimeFormatter.millisecondsToString(sessionDate.getTime(), TimeFormatter.TIME_FORMAT)));
        objects.add(new DetailRow(TITLE_LOCATION, session.getHospital()));
        objects.add(new DetailRow(TITLE_APPOINTMENT, String.valueOf(session.getCurrentAppNumber())));
        objects.add(new DetailRow(TITLE_DOCTOR_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, session.getDoctorFee())));
        objects.add(new DetailRow(TITLE_HOSPITAL_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, session.getHospitalFee())));
        objects.add(new DetailRow(TITLE_PLATFORM_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, session.getVATFee())));

        return objects;
    }

    class DetailsActions implements SearchActivity.SearchActions {

        @Override
        public String getTitle(Context context) {
            return null;
        }

        @Override
        public boolean isValueConsists(Object object, String value) {
            return true;
        }

        @Override
        public boolean onFinish(Activity activity, Object object) {
            return false;
        }

        @Override
        public List<Object> readObject(JSONObject jsonObject) {
            return null;
        }

        @Override
        public String getName(Object object) {
            return ((DetailRow) object).title;
        }

        @Override
        public String getValue(Object object) {
            return ((DetailRow) object).value;
        }

        @Override
        public DownloadDataBuilder getDownloadBuilder() {
            return null;
        }
    }

    public class DetailRow {
        private String title;
        private String value;

        DetailRow(String title, String value) {
            this.title = title;
            this.value = value;
        }

    }
}

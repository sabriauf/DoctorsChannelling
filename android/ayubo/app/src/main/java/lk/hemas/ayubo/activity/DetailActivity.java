package lk.hemas.ayubo.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.SearchAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.config.AyuboApplication;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.TimeFormatter;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL_ACTION_OBJECT = "detail_action_object";
    //Detail params
    private static final String TITLE_TYPE = "Type";
    private static final String TITLE_DATE = "Date";
    private static final String TITLE_TIME = "Time";
    private static final String TITLE_LOCATION = "Location";
    private static final String TITLE_APPOINTMENT = "Appointment Number";
    private static final String TITLE_DOCTOR_FEE = "Doctor Fee";
    private static final String TITLE_HOSPITAL_FEE = "Hospital Fee";
    private static final String TITLE_PLATFORM_FEE = "Platform Fee";
    private static final String TITLE_VAT_FEE = "VAT";
    private static final String TITLE_BOOKING_FEE = "Booking Fee";

    //instances
    private DetailAction detailAction;
    private List<Object> objects;
    private SearchAdapter adapter;

    //views
    private TextView txtPrice;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        readExtras();
        initView();
        getData();
    }

    private void readExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(EXTRA_DETAIL_ACTION_OBJECT)) {
            detailAction = (DetailAction) bundle.getSerializable(EXTRA_DETAIL_ACTION_OBJECT);
        }
    }

    private void initView() {
        TextView txtDocName = findViewById(R.id.txt_doctor_name_doctor_info);
        TextView txtSpecialty = findViewById(R.id.txt_specialty_doctor_info);
        TextView txtNote = findViewById(R.id.txt_detail_note);
        txtPrice = findViewById(R.id.txt_price_details);
        TextView txtConfirm = findViewById(R.id.txt_confirm_details);
        findViewById(R.id.txt_note_doctor_info).setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.prg_details);

        if (detailAction != null) {
            txtDocName.setText(detailAction.getDocName());
            txtSpecialty.setText(detailAction.getSpecialisation());
            txtNote.setText(detailAction.getDoctorNote());

            txtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailAction.onFinish(DetailActivity.this);
                }
            });

            RecyclerView recycler_search = findViewById(R.id.recycler_session_details);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_search.setLayoutManager(linearLayoutManager);
            adapter = new SearchAdapter(this, getDetailList(), new DetailsSearchActions());
            adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Object object) {
                }
            });
            recycler_search.setAdapter(adapter);
        }
    }

    private List<Object> getDetailList() {
        objects = new ArrayList<>();

        objects.add(new DetailRow(TITLE_TYPE, detailAction.getType(), R.drawable.channeling));
        Date sessionDate = detailAction.getDate();
        objects.add(new DetailRow(TITLE_DATE, TimeFormatter.millisecondsToString(sessionDate.getTime(),
                TimeFormatter.TIME_FORMAT_APPOINTMENT_DATE), R.drawable.date));
        objects.add(new DetailRow(TITLE_TIME, detailAction.getTime(), R.drawable.time));
        objects.add(new DetailRow(TITLE_LOCATION, detailAction.getLocation(), R.drawable.hospital));
        objects.add(new DetailRow(TITLE_APPOINTMENT, detailAction.getAppointmentNo(), R.drawable.appointment));

        return objects;
    }

    private void getData() {
        if (detailAction != null && detailAction.getAddDownloadBuilder() != null) {
            if (detailAction.hasData(this)) {
                removeBooking();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            new DownloadData(this, detailAction.getAddDownloadBuilder()).
                    setOnDownloadListener(SearchActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                        @Override
                        public void onDownloadSuccess(final String response, int what, int code) {
                            progressBar.setVisibility(View.GONE);
                            if (detailAction.readData(DetailActivity.this, response)) {
                                objects.add(new DetailRow(TITLE_DOCTOR_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                                        detailAction.getDoctorFee()), R.drawable.doctor_fee));
                                objects.add(new DetailRow(TITLE_HOSPITAL_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                                        detailAction.getHospitalFee()), R.drawable.hospital_fee));
                                objects.add(new DetailRow(TITLE_PLATFORM_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                                        detailAction.getPlatformFee()), R.drawable.platform_fee));
                                objects.add(new DetailRow(TITLE_BOOKING_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                                        detailAction.getBookingFee()), R.drawable.dc_booking_fee_icon));
                                objects.add(new DetailRow(TITLE_VAT_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                                        detailAction.getVatFee()), R.drawable.vat));
                                adapter.notifyDataSetChanged();

                                Double totalPrice = detailAction.getTotalPrice();
                                txtPrice.setText(String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, totalPrice));
                            } else
                                txtPrice.setText(R.string.invalid);
                        }

                        @Override
                        public void onDownloadFailed(String errorMessage, int what, int code) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//                            progressBar.setVisibility(View.GONE);
//                            errorView.setVisibility(View.VISIBLE);
//                            errorView.startAnimation(AnimationUtils.loadAnimation(SearchActivity.this, R.anim.anim_shake));
                        }
                    }).execute();
        }
    }

    private void removeBooking() {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, detailAction.getRemoveDownloadBuilder(this)).
                setOnDownloadListener(SearchActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        ((AyuboApplication) getApplication()).setBooking(null);
                        getData();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    class DetailsSearchActions implements SearchActivity.SearchActions {

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
        public String getImageUrl(Object object) {
            return String.valueOf(((DetailRow) object).icon);
        }

        @Override
        public DownloadDataBuilder getDownloadBuilder() {
            return null;
        }

        @Override
        public int getViewType() {
            return SearchAdapter.SUMMARY_TYPE;
        }
    }

    public class DetailRow {
        private String title;
        private String value;
        private int icon;

        DetailRow(String title, String value, int icon) {
            this.title = title;
            this.value = value;
            this.icon = icon;
        }
    }

    public interface DetailAction {
        String getDocName();

        String getSpecialisation();

        Double getTotalPrice();

        String getType();

        Date getDate();

        String getLocation();

        String getAppointmentNo();

        Double getDoctorFee();

        Double getHospitalFee();

        Double getPlatformFee();

        Double getVatFee();

        Double getBookingFee();

        DownloadDataBuilder getAddDownloadBuilder();

        DownloadDataBuilder getRemoveDownloadBuilder(Activity activity);

        boolean readData(Activity activity, String response);

        void onFinish(Activity activity);

        String getDoctorNote();

        boolean hasData(Activity activity);

        String getTime();
    }
}

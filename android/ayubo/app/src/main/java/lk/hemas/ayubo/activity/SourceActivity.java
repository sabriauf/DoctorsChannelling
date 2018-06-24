package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.ChannelDoctorAdapter;
import lk.hemas.ayubo.model.Appointment;
import lk.hemas.ayubo.model.Session;

public class SourceActivity extends AppCompatActivity {

    //instances
    public Appointment appointment;

//    //primary data
//    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        readData(getIntent());
//        initView();
        setRecyclerView();
    }

    private void readData(Intent intent) {
        if (intent.getExtras() != null)
            appointment = (Appointment) intent.getExtras().getSerializable(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT);
    }

//    private void initView() {
//        View doc990PayView = findViewById(R.id.layout_dialog_source);
//        View eChannellingPayView = findViewById(R.id.layout_card_source);
//        View alternative = findViewById(R.id.layout_alternative_source);
//        boolean isEchannellingSet = false;
//        boolean isDoc990 = false;
//        boolean isAlternative = false;
//
//        final Intent intent = new Intent(SourceActivity.this, UserDetailsActivity.class);
//        if (appointment != null) {
//            for (Session.Info info : appointment.getSession().getBooking_info()) {
//                if (info.getFrom().equals("echannelling")) {
//                    setButton(eChannellingPayView, ContextCompat.getDrawable(this, R.drawable.echannelling),
//                            getAmountString(info.getAmount_local()), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    appointment.setSource("echannelling");
//                                    intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });
//                    isEchannellingSet = true;
//                } else if (info.getFrom().equals("doc990")) {
//                    setButton(doc990PayView, ContextCompat.getDrawable(this, R.drawable.doc990),
//                            getAmountString(info.getAmount_local()), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    appointment.setSource("doc990");
//                                    intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });
//                    isDoc990 = true;
//                } else {
//                    source = info.getFrom();
//                    setButton(alternative, appointment.getSessionParent().getHospital_image(),
//                            getAmountString(info.getAmount_local()), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    appointment.setSource(source);
//                                    intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });
//                    isAlternative = true;
//                }
//            }
//        }
//
//        if (!isEchannellingSet)
//            eChannellingPayView.setVisibility(View.GONE);
//        if (!isDoc990)
//            doc990PayView.setVisibility(View.GONE);
//        if (!isAlternative)
//            alternative.setVisibility(View.GONE);
//
//    }

    private void setRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.recycler_source);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Session.Info> sources = Arrays.asList(appointment.getSession().getBooking_info());
        ChannelDoctorAdapter adapter = new ChannelDoctorAdapter(this, new ArrayList<Object>(sources),
                ChannelDoctorAdapter.VIEW_TYPE_SOURCE);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Object channelDoctor) {
                Session.Info source = (Session.Info) channelDoctor;
                Intent intent = new Intent(SourceActivity.this, UserDetailsActivity.class);
                appointment.setSource(source.getFrom());
                intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }

//    private String getAmountString(double amount) {
//        return String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, amount);
//    }

//    private void setButton(View view, Drawable image, String title, View.OnClickListener listener) {
//        ImageView imgIcon = view.findViewById(R.id.img_source_row);
//        TextView txtPrice = view.findViewById(R.id.txt_price_source_row);
//
//        imgIcon.setImageDrawable(image);
//        txtPrice.setText(title);
//
//        view.setOnClickListener(listener);
//    }
//
//    private void setButton(View view, String image, String title, View.OnClickListener listener) {
//        ImageView imgIcon = view.findViewById(R.id.img_source_row);
//        TextView txtPrice = view.findViewById(R.id.txt_price_source_row);
//
//        Glide.with(SourceActivity.this).load(image).into(imgIcon);
//        txtPrice.setText(title);
//
//        view.setOnClickListener(listener);
//    }
}

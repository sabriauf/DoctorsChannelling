package lk.hemas.ayubo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.config.AyuboApplication;
import lk.hemas.ayubo.model.Booking;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;
import lk.hemas.ayubo.util.PhoneMask;
import lk.hemas.ayubo.view.PinEntryDialogActionView;

public class PayActivity extends AppCompatActivity {

    //constants
    private static final int REQUEST_CODE_DIALOG = 101;
    public static final String EXTRA_BOOKING_OBJECT = "booking_object";

    //instances
    private RadioButton selectedButton;
    private Booking booking;
    private MobileNumberView numberView;

    //primary data
    private String trId;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        readData(getIntent());
        initView();
    }

    private void readData(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_BOOKING_OBJECT))
            booking = (Booking) intent.getExtras().getSerializable(EXTRA_BOOKING_OBJECT);
    }

    private void initView() {
        final View dialogPayView = findViewById(R.id.layout_dialog_payment);
        View cardPayView = findViewById(R.id.layout_card_payment);

        TextView txtPrice = findViewById(R.id.txt_price_proceed);
        if (booking != null)
            txtPrice.setText(String.format(Locale.getDefault(), "AMOUNT : %,.2f LKR", (booking.getPrice().getTotal() * 1.0)));

        numberView = new MobileNumberView();
        numberView.setVisibility(View.GONE);

        setRadioButton(dialogPayView, ContextCompat.getDrawable(this, R.drawable.dialog),
                getString(R.string.add_to_dialog_bill), true);
        setRadioButton(cardPayView, ContextCompat.getDrawable(this, R.drawable.visa),
                getString(R.string.online_payment), false);

        TextView btnPay = findViewById(R.id.txt_proceed_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isProcessing) {
                    isProcessing = true;
                    if (selectedButton != null) {
                        if (((RadioButton) dialogPayView.findViewById(R.id.radio_select_payment_row)).isChecked()) {
                            String value = numberView.getText();
                            if (!AppHandler.isNotValidatePhone(value) && (value.startsWith("077") || value.startsWith("076")))
                                sendDialogPaymentRequest(value);
                            else {
                                Toast.makeText(PayActivity.this, getString(R.string.invalid_phone), Toast.LENGTH_LONG).show();
                                isProcessing = false;
                            }
                        } else {
                            if (booking != null) {
                                Log.d(PayActivity.class.getSimpleName(), "Payment Link - " + booking.getPayment().getHnb_payment_link());
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(booking.getPayment().getHnb_payment_link()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ((AyuboApplication) getApplication()).setBooking(null);
                                startActivity(browserIntent);
                                finish();
                            } else {
                                isProcessing = false;
                                Toast.makeText(PayActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        isProcessing = false;
                        Toast.makeText(PayActivity.this, R.string.error_no_payment_method, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void setRadioButton(View view, Drawable image, String title, final boolean showNumberView) {
        ImageView imgIcon = view.findViewById(R.id.img_payment);
        TextView txtMethodName = view.findViewById(R.id.txt_title_payment);
        final RadioButton radioButton = view.findViewById(R.id.radio_select_payment_row);

        imgIcon.setImageDrawable(image);
        txtMethodName.setText(title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(radioButton, showNumberView);
            }
        });

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(radioButton, showNumberView);
            }
        });
    }

    private void onRadioButtonClicked(RadioButton radioButton, boolean showNumberView) {
        if (selectedButton != null)
            selectedButton.setChecked(false);
        radioButton.setChecked(true);
        selectedButton = radioButton;
        numberView.setVisibility(showNumberView ? View.VISIBLE : View.GONE);
    }

    class MobileNumberView {
        EditText edtNumber;
        TextView txtNumber;

        MobileNumberView() {
            edtNumber = findViewById(R.id.edt_proceed_number);
            txtNumber = findViewById(R.id.txt_proceed_mobile);

            edtNumber.addTextChangedListener(new PhoneMask());
        }

        void setVisibility(int visibility) {
            edtNumber.setVisibility(visibility);
            txtNumber.setVisibility(visibility);
        }

        String getText() {
            String value = edtNumber.getText().toString();
            value = value.replace(" ", "");
            value = value.replace("(", "");
            value = value.replace(")", "");

            return value;
        }
    }

    private void sendDialogPaymentRequest(String number) {
        trId = "2110";
        if (booking != null)
            trId = String.valueOf(booking.getAppointment().getRef());
        number = "94" + number.substring(1);

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_DAILOG_PAY, new DialogPayParams(trId, number).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        readResponse(response);
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        isProcessing = false;
                        Toast.makeText(PayActivity.this, "Error - " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    private void readResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("result") == 0) {
                String correlator = jsonObject.getJSONObject("data").getString("clientcorrelator");
                Intent intent = new Intent(PayActivity.this, CustomDigitEntryDialogActivity.class);
                intent.putExtra(CustomDigitEntryDialogActivity.EXTRA_ACTION_OBJECT, new PinEntryDialogActionView(correlator, trId));
                startActivityForResult(intent, REQUEST_CODE_DIALOG);
            } else {
                isProcessing = false;
                Toast.makeText(PayActivity.this, "Error - " + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            isProcessing = false;
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DIALOG)
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(PayActivity.this, ResultActivity.class);
                intent.putExtra(ResultActivity.EXTRA_BOOKING_OBJECT, booking);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ((AyuboApplication) getApplication()).setBooking(null);
                finish();
            } else if (resultCode == RESULT_FIRST_USER) {
                sendDialogPaymentRequest(numberView.getText());
            } else {
                isProcessing = false;
            }

    }

    class DialogPayParams extends SoapBasicParams {
        private String trId;
        private String dialogNo;

        DialogPayParams(String trId, String dialogNo) {
            this.trId = trId;
            this.dialogNo = dialogNo;
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("b", user_id);
                jsonObject.put("c", trId);
                jsonObject.put("d", dialogNo);
                jsonObject.put("a", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}

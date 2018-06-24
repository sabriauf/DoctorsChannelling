package lk.hemas.ayubo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.Calendar;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.config.Constants;
import lk.hemas.ayubo.model.Appointment;
import lk.hemas.ayubo.model.User;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DatePickerFragment;
import lk.hemas.ayubo.util.TimeFormatter;
import lk.hemas.ayubo.view.DetailSessionView;

public class UserDetailsActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_APPOINTMENT_OBJECT = "appointment_object";

    //instances
    private User user;
    private Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        final ViewHolder holder = new ViewHolder();

        readData(getIntent(), holder);

        findViewById(R.id.txt_next_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.validate()) {
                    if (appointment == null)
                        appointment = new Appointment();
                    appointment.setUser(user);
                    Intent intent = new Intent(UserDetailsActivity.this, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_DETAIL_ACTION_OBJECT, new DetailSessionView(appointment));
                    startActivity(intent);
                }
            }
        });
    }

    private void readData(Intent intent, ViewHolder holder) {
        if (intent.getExtras() != null)
            appointment = (Appointment) intent.getExtras().getSerializable(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        user = new Gson().fromJson(sharedPreferences.getString(Constants.PREFERENCE_USER_OBJECT, ""), User.class);
        if (user != null)
            holder.setUser(user);
        else
            user = new User();
    }

    class ViewHolder {
        EditText edtTitle, edtFName, edtLName, edtPhone, edtEmail, edtIdentity, edtDOB;

        ViewHolder() {
            edtTitle = findViewById(R.id.edt_title_user);
            edtFName = findViewById(R.id.edt_name_user);
            edtLName = findViewById(R.id.edt_l_name_user);
            edtPhone = findViewById(R.id.edt_phone_user);
            edtEmail = findViewById(R.id.edt_email_user);
            edtIdentity = findViewById(R.id.edt_identification_user);
            edtDOB = findViewById(R.id.edt_dob_user);

            edtDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        hideKeyboard();
                        showDateSelector();
                    }
                }
            });

            edtDOB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    showDateSelector();
                }
            });
        }

        private void showDateSelector() {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 20);
            newFragment.setDefaultDate(calendar);
            newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                @Override
                public void onSelected(Calendar calendar) {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                    user.setDob(calendar.getTime());
                    edtDOB.setText(TimeFormatter.millisecondsToString(calendar.getTimeInMillis(), TimeFormatter.DATE_FORMAT_DOB));
                }
            });
        }

        private void hideKeyboard() {
            View view = UserDetailsActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        boolean validate() {
            boolean isValid = true;

            if (edtTitle.getText().toString().equals("")) {
                isValid = false;
                edtTitle.setError(getString(R.string.enter_title));
            } else {
                user.setTitle(edtTitle.getText().toString());
                edtTitle.setError(null);
            }

            if (edtFName.getText().toString().equals("")) {
                isValid = false;
                edtFName.setError(getString(R.string.enter_name));
            } else {
                user.setfName(edtFName.getText().toString());
                edtFName.setError(null);
            }

            if (edtLName.getText().toString().equals("")) {
                isValid = false;
                edtLName.setError(getString(R.string.enter_name));
            } else {
                user.setlName(edtLName.getText().toString());
                edtLName.setError(null);
            }

            if (edtPhone.getText().toString().equals("")) {
                isValid = false;
                edtPhone.setError(getString(R.string.enter_phone));
            } else if (AppHandler.isNotValidatePhone(edtPhone.getText().toString())) {
                edtPhone.setError(getString(R.string.invalid_phone));
            } else {
                user.setPhone(edtPhone.getText().toString());
                edtPhone.setError(null);
            }

            if (edtEmail.getText().toString().equals("")) {
                isValid = false;
                edtEmail.setError(getString(R.string.enter_email));
            } else if (AppHandler.isNotValidateEmail(edtEmail.getText().toString())) {
                edtEmail.setError(getString(R.string.invalid_email));
            } else {
                user.setEmail(edtEmail.getText().toString());
                edtEmail.setError(null);
            }

            if (edtIdentity.getText().toString().equals("")) {
                isValid = false;
                edtIdentity.setError(getString(R.string.enter_identification));
            } else if (AppHandler.isNotValidateNIC(edtIdentity.getText().toString())) {
                edtIdentity.setError(getString(R.string.invalid_identification));
            } else {
                user.setIdentification(edtIdentity.getText().toString());
                edtIdentity.setError(null);
            }

            if (edtDOB.getText().toString().equals("")) {
                isValid = false;
                edtDOB.setError("Please select the Date of birth");
            } else {
                edtDOB.setError(null);
            }

            if (isValid) {
                getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE).edit().putString(Constants.
                        PREFERENCE_USER_OBJECT, new Gson().toJson(user)).apply();
            }

            return isValid;
        }

        private void setUser(User user) {
            edtTitle.setText(user.getTitle());
            edtFName.setText(user.getfName());
            edtLName.setText(user.getlName());
            edtPhone.setText(user.getPhone());
            edtEmail.setText(user.getEmail());
            edtIdentity.setText(user.getIdentification());
            edtDOB.setText(TimeFormatter.millisecondsToString(user.getDob().getTime(), TimeFormatter.DATE_FORMAT_DOB));
        }
    }

}

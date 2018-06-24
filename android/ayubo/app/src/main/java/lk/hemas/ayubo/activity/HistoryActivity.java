package lk.hemas.ayubo.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.HistoryAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.History;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;

public class HistoryActivity extends AppCompatActivity {

    //views
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Spinner spnUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        spnUsers = findViewById(R.id.spn_spinner_custom_view);
        progressBar = findViewById(R.id.prg_history_progress);

        recyclerView = findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadHistory();
    }

    class SpinnerAdapter extends ArrayAdapter<String> {

        private LayoutInflater mInflater;
        private List<String> items;
        private int mRes;

        private SpinnerAdapter(int resource, @NonNull List<String> objects) {
            super(HistoryActivity.this, resource, objects);
            this.mInflater = LayoutInflater.from(HistoryActivity.this);
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

        private View createItemView(int position, @SuppressWarnings("unused") View convertView, ViewGroup parent) {
            final View view = mInflater.inflate(mRes, parent, false);
            TextView txtView = view.findViewById(R.id.txt_text_spinner_row);
            txtView.setText(items.get(position));
            return view;
        }
    }

    private void loadHistory() {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_HISTORY, new SoapBasicParams().getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("result") == 0) {
                                readResponse(jsonObject);
                                return;
                            } else {
                                Toast.makeText(HistoryActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(HistoryActivity.this, "Failed to load History", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(HistoryActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    private void readResponse(JSONObject jsonObject) {
        List<Object> items = new ArrayList<>();
        try {
            JSONObject object = jsonObject.getJSONObject("data").getJSONObject("appointments");
            Type historyListType = new TypeToken<List<History>>() {
            }.getType();

            List<History> todayArray = new Gson().fromJson(object.getJSONArray("today").toString(), historyListType);
            List<History> upcomingArray = new Gson().fromJson(object.getJSONArray("upcoming").toString(), historyListType);
            List<History> previousArray = new Gson().fromJson(object.getJSONArray("previous").toString(), historyListType);

            if (todayArray.size() > 0 || upcomingArray.size() > 0)
                items.add(getString(R.string.upcoming_appointments));
            items.addAll(todayArray);
            items.addAll(upcomingArray);

            if (previousArray.size() > 0)
                items.add(getString(R.string.previous_appointment));
            items.addAll(previousArray);

            HistoryAdapter adapter = new HistoryAdapter(this, items);
            recyclerView.setAdapter(adapter);
            adapter.setOnAppointmentCancelListener(new HistoryAdapter.OnCancelAppointmentListener() {
                @Override
                public void onCancel(History history) {
                    if (progressBar.getVisibility() == View.GONE)
                        showAlertToCancel(history.getId());
                }
            });

            readUsers(items);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readUsers(final List<Object> items) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> users = new ArrayList<>();

                parent:
                for (Object obj : items) {
                    if(obj instanceof History) {
                        History history = (History) obj;

                        for (String name: users) {
                            if(history.getPatient().equals(name))
                                continue parent;
                        }

                        users.add(history.getPatient());
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setSpinner(users);
                    }
                });
            }
        }).start();
    }

    private void setSpinner(List<String> users) {
        spnUsers.setAdapter(new SpinnerAdapter(R.layout.custom_spinner_text_row, users));
        spnUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void showAlertToCancel(final String id) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_cancel)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelAppointment(id);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void cancelAppointment(String appointmentId) {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_CANCEL_APPOINTMENT, new CancelAppointmentParam(appointmentId).
                        getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("result") == 0) {
                                Toast.makeText(HistoryActivity.this, "Appointment is canceled", Toast.LENGTH_LONG).show();
                                loadHistory();
                            } else {
                                Toast.makeText(HistoryActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HistoryActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(HistoryActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    class CancelAppointmentParam extends SoapBasicParams {
        private String appointmentId;

        CancelAppointmentParam(String appointmentId) {
            this.appointmentId = appointmentId;
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("appointmentId", appointmentId);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}

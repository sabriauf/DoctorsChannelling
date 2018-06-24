package lk.hemas.ayubo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.SearchAdapter;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.util.DownloadData;

public class SearchActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_SEARCH_OBJECT = "search_object";
    public static final String EXTRA_RESULT_OBJECT = "result_object";
    public static final String EXTRA_SEARCH_VALUE = "search_value";
    public static final String EXTRA_SEARCH_ID = "search_id";
    public static final String EXTRA_TO_DATE = "search_to_date";

    //instances
    private SearchAdapter adapter;
    private List<Object> source;
    private List<Object> objects;
    private SearchActions searchActions;

    //views
    private ProgressBar progressBar;
    private View errorView;
    private TextView txtNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        progressBar = findViewById(R.id.progress_loading);
        txtNoResult = findViewById(R.id.txt_result_search);

        errorView = findViewById(R.id.layout_error);
        errorView.findViewById(R.id.btn_try_again_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        EditText editText = findViewById(R.id.edt_search_value);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(SearchActivity.EXTRA_SEARCH_VALUE, textView.getText().toString());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        readExtras();
    }

    private void readExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_SEARCH_OBJECT)) {
            searchActions = (SearchActions) extras.get(EXTRA_SEARCH_OBJECT);
            ((TextView) findViewById(R.id.txt_title_search)).setText(searchActions.getTitle(this));
            getData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (searchActions != null)
            searchActions.onFinish(this, null);
    }

    private void getData() {
        if (searchActions != null) {
            progressBar.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            new DownloadData(this, searchActions.getDownloadBuilder()).
                    setOnDownloadListener(SearchActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                        @Override
                        public void onDownloadSuccess(final String response, int what, int code) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final List<Object> data = readVisitDoctors(response);

                                    txtNoResult.setVisibility(data != null && data.size() > 0 ? View.GONE : View.VISIBLE);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            if (data != null && data.size() == 1 && searchActions != null)
                                                searchActions.onFinish(SearchActivity.this, data.get(0));
                                            else
                                                setRecyclerData(data);
                                        }
                                    });
                                }
                            }).run();
                        }

                        @Override
                        public void onDownloadFailed(String errorMessage, int what, int code) {
                            progressBar.setVisibility(View.GONE);
                            errorView.setVisibility(View.VISIBLE);
                            errorView.startAnimation(AnimationUtils.loadAnimation(SearchActivity.this, R.anim.anim_shake));
                        }
                    }).execute();
        }
    }

    private List<Object> readVisitDoctors(String respond) {
        List<Object> dataArray = null;
        try {
            JSONObject jsonObject = new JSONObject(respond);
            dataArray = searchActions.readObject(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataArray;
    }

    private void setRecyclerData(List<Object> data) {
        if (data != null)
            if (adapter == null) {
                source = new ArrayList<>(data);
                objects = data;
                setRecyclerView();
            } else {
                source.clear();
                objects.clear();
                objects.addAll(data);
                source.addAll(data);
                adapter.notifyDataSetChanged();
            }
    }

    private void setRecyclerView() {
        RecyclerView recycler_search = findViewById(R.id.recycler_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_search.setLayoutManager(linearLayoutManager);
        adapter = new SearchAdapter(this, objects, searchActions);
        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {
                if (searchActions != null)
                    searchActions.onFinish(SearchActivity.this, object);
            }
        });
        recycler_search.setAdapter(adapter);
    }

    private void filterData(final String name) {
        if (objects == null)
            return;

        if (name.length() > 2)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (source != null && source.size() > 0 && searchActions != null) {
                        objects.clear();
                        for (Object object : source) {

                            if (searchActions.isValueConsists(object, name))
                                objects.add(object);
                        }

                        txtNoResult.setVisibility(objects.size() > 0 ? View.GONE : View.VISIBLE);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                }
            }).run();
        else if (name.length() == 2) {

            objects.clear();
            objects.addAll(source);
            adapter.notifyDataSetChanged();
            txtNoResult.setVisibility(objects.size() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    public interface SearchActions {

        String getTitle(Context context);

        boolean isValueConsists(Object object, String value);

        boolean onFinish(Activity activity, Object object);

        List<Object> readObject(JSONObject jsonObject);

        String getName(Object object);

        String getValue(Object object);

        String getImageUrl(Object object);

        DownloadDataBuilder getDownloadBuilder();

        int getViewType();
    }
}

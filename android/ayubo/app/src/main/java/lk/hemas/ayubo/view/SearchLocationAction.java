package lk.hemas.ayubo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.activity.SearchActivity;
import lk.hemas.ayubo.adapter.SearchAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.AyuboSearchParameters;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.VisitLocation;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadManager;

/**
 * Created by Sabri on 3/17/2018. Search Action implemented for Hospitals search
 */

public class SearchLocationAction implements SearchActivity.SearchActions, Serializable {

    private AyuboSearchParameters params;

    public SearchLocationAction(AyuboSearchParameters params) {
        this.params = params;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.search_location);
    }

    @Override
    public boolean isValueConsists(Object object, String value) {
        VisitLocation location = (VisitLocation) object;
        return location.getName().toLowerCase().contains(value.toLowerCase());
    }

    @Override
    public boolean onFinish(Activity activity, Object object) {
        Intent result = new Intent();
        if(object != null) {
            result.putExtra(SearchActivity.EXTRA_SEARCH_VALUE, ((VisitLocation) object).getName());
            result.putExtra(SearchActivity.EXTRA_SEARCH_ID, ((VisitLocation) object).getId());
            result.putExtra(SearchActivity.EXTRA_RESULT_OBJECT, (VisitLocation) object);
            activity.setResult(Activity.RESULT_OK, result);
        }
        activity.finish();
        return true;
    }

    @Override
    public List<Object> readObject(JSONObject jsonObject) {
        Type messageType = new TypeToken<List<VisitLocation>>() {
        }.getType();

        try {
            return new Gson().fromJson(jsonObject.getJSONArray("data").toString(), messageType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName(Object object) {
        return ((VisitLocation) object).getName();
    }

    @Override
    public String getValue(Object object) {
        return "";
    }

    @Override
    public String getImageUrl(Object object) {
        return "";
    }

    @Override
    public DownloadDataBuilder getDownloadBuilder() {
        return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_LOCATION_SEARCH, params.getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public int getViewType() {
        return SearchAdapter.SINGLE_TYPE;
    }
}

package lk.hemas.ayubo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.activity.DoctorActivity;
import lk.hemas.ayubo.activity.SearchActivity;
import lk.hemas.ayubo.adapter.SearchAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.ChannelDoctor;
import lk.hemas.ayubo.model.DocSearchParameters;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadManager;

/**
 * Created by Sabri on 3/19/2018. View for Select doctor
 */

public class SelectDoctorAction implements SearchActivity.SearchActions, Serializable {

    private DocSearchParameters params;

    public SelectDoctorAction(DocSearchParameters params) {
        this.params = params;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.visit_a_doctor_two);
    }

    @Override
    public boolean isValueConsists(Object object, String value) {
        return object instanceof ChannelDoctor && ((ChannelDoctor) object).getDoctor_name().toLowerCase().contains(value.toLowerCase());
    }

    @Override
    public boolean onFinish(Activity activity, Object object) {
        if (object != null && object instanceof ChannelDoctor) {
//            DocSessionParameters docSessionParameters = new DocSessionParameters();
//            docSessionParameters.setDocId(String.valueOf(((ChannelDoctor) object).getDoctor_code()));
//            docSessionParameters.setFromDate(TimeFormatter.millisecondsToString(Calendar.getInstance().getTimeInMillis(),
//                    TimeFormatter.DATE_FORMAT_SHORT));
//            docSessionParameters.setToDate(activity.getIntent().getExtras() != null ? activity.getIntent()
//                    .getExtras().getString(SearchActivity.EXTRA_TO_DATE) : "");

            Intent intent = new Intent(activity, DoctorActivity.class);
            intent.putExtra(DoctorActivity.EXTRA_DOCTOR_DETAILS, (ChannelDoctor) object);
            activity.startActivity(intent);
            activity.finish();
            return true;
        }
        activity.finish();
        return false;
    }

    @Override
    public List<Object> readObject(JSONObject jsonObject) {
        Type messageType = new TypeToken<HashMap<String, ChannelDoctor>>() {
        }.getType();

        try {
            HashMap<String, ChannelDoctor> dataArray = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), messageType);

            return new ArrayList<Object>(dataArray.values());
        } catch (JSONException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

//    private List<Object> sortTheList(List<ChannelDoctor> originalList) {
//        List<Object> sortedList = new ArrayList<>();
//        List<ChannelDoctor> temp = new ArrayList<>();
//        String location = "";
//        while (originalList.size() > 0) {
//            for (ChannelDoctor channelDoctor : originalList) {
//                if (location.equals("")) {
//                    location = channelDoctor.getHospital();
//                    addToSortedOrder(sortedList, location);
//                    addToSortedOrder(sortedList, channelDoctor);
//                    continue;
//                }
//                if (location.equals(channelDoctor.getHospital()))
//                    addToSortedOrder(sortedList, channelDoctor);
//                else
//                    temp.add(channelDoctor);
//            }
//            location = "";
//            originalList = new ArrayList<>(temp);
//            temp = new ArrayList<>();
//        }
//        return sortedList;
//    }

//    private void addToSortedOrder(List<Object> list, Object item) {
//        String location;
//        String name = "";
//        if (item instanceof ChannelDoctor) {
//            location = ((ChannelDoctor) item).getHospital();
//            name = ((ChannelDoctor) item).getDocName();
//        } else
//            location = (String) item;
//
//        for (int i = 0; i < list.size(); i++) {
//            Object obj = list.get(i);
//            String localLocation;
//            String localName = "";
//            if (obj instanceof String)
//                localLocation = (String) obj;
//            else {
//                localLocation = ((ChannelDoctor) obj).getHospital();
//                localName = ((ChannelDoctor) obj).getDocName();
//            }
//
//            if ((localLocation.compareTo(location) > 0 && name.equals("")) || (localLocation.compareTo(location) == 0
//                    && localName.compareTo(name) > 0)) {
//                list.add(i, item);
//                return;
//            }
//        }
//        list.add(item);
//    }

    @Override
    public String getName(Object object) {
        if (object instanceof ChannelDoctor)
            return ((ChannelDoctor) object).getDoctor_name();
        else
            return (String) object;
    }

    @Override
    public String getValue(Object object) {
        if (object instanceof ChannelDoctor)
            return ((ChannelDoctor) object).getSpecialization();
        else
            return "";
    }

    @Override
    public String getImageUrl(Object object) {
        if (object instanceof ChannelDoctor)
            return ((ChannelDoctor) object).getDoc_image();
        else
            return "";
    }

    @Override
    public DownloadDataBuilder getDownloadBuilder() {
        if(params != null)
        return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_CHANNELLING_SEARCH, params.getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
        else
            return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                    setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_NEW_FAVORITES, new SoapBasicParams().getSearchParams())).
                    setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public int getViewType() {
        return SearchAdapter.DETAIL_TYPE;
    }

}

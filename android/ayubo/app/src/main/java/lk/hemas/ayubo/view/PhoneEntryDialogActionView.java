package lk.hemas.ayubo.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.io.Serializable;

import lk.hemas.ayubo.activity.CustomDigitEntryDialogActivity;

public class PhoneEntryDialogActionView implements CustomDigitEntryDialogActivity.EntryDialogActionView, Serializable {


    @Override
    public String getTitle(Context context) {
        return null;
    }

    @Override
    public View.OnClickListener setOnClickListener(Activity activity, PinEntryEditText value) {
        return null;
    }

    @Override
    public void initEntryValue(Activity activity, PinEntryEditText value) {

    }

    @Override
    public void readResponse(Activity activity, PinEntryEditText value, String response) {

    }

    @Override
    public String getParams(PinEntryEditText value) {
        return null;
    }
}

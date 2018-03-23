package lk.hemas.ayubo.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Sabri on 3/22/2018. Date Picker dialog
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private OnDateSelected onDateSelected;

    public void setOnDateSelectedListener(OnDateSelected onDateSelected) {
        this.onDateSelected = onDateSelected;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Context context = getContext();

        if (context != null) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            return datePickerDialog;
        } else
            return null;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month + 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        if (onDateSelected != null)
            onDateSelected.onSelected(c);
    }

    public interface OnDateSelected {
        void onSelected(Calendar calendar);
    }
}

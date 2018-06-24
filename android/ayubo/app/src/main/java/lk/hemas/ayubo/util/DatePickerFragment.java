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
    private Calendar minDate;
    private Calendar defaultDate;

    public void setOnDateSelectedListener(OnDateSelected onDateSelected) {
        this.onDateSelected = onDateSelected;
    }

    public void setMinDate(Calendar minDate) {
        this.minDate = minDate;
    }

    public void setDefaultDate(Calendar defaultDate) {
        this.defaultDate = defaultDate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(defaultDate == null)
            defaultDate = Calendar.getInstance();
        int year = defaultDate.get(Calendar.YEAR);
        int month = defaultDate.get(Calendar.MONTH);
        int day = defaultDate.get(Calendar.DAY_OF_MONTH);

        Context context = getContext();

        if (context != null) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, this, year, month, day);
            if (minDate != null)
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            return datePickerDialog;
        } else
            return null;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        if (onDateSelected != null)
            onDateSelected.onSelected(c);
    }

    public interface OnDateSelected {
        void onSelected(Calendar calendar);
    }
}

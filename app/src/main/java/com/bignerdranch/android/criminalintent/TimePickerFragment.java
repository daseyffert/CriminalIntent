package com.bignerdranch.android.criminalintent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Daniel on 11/30/2015.
 */
public class TimePickerFragment extends PickerDialogFragment {

    private TimePicker mTimePicker;

    /**
     * Creates a new Instance of TimePickerFragment by creating a Bundle of Arguments
     * for the fragment, creating a fragment TimePickerFragment and then assigning the
     * arguments to the newly created fragment
     * @param date on which the new Instance is created: mCrime.getDate()
     * @return new DatePickerFragment created
     */
    public static TimePickerFragment newInstance(Date date) {
        Bundle args = getArgs(date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a View to return to PickerDialogFragment to initialize it in the AlertDialog
     * @return View inflated with a TimePicker as well as the day, month, year options
     */
    @Override
    protected View initLayout() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
       //get time
        mTimePicker.setIs24HourView(false);
        mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));

        return v;
    }

    /**
     * Retrieve Date from TimePicker in AlertDialog
     * @return the date user entered
     */
    @Override
    protected Date getDate() {
        //TimePicker only sets the time. The date remains the same
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        //Get the time from the timePicker
        int hour = mTimePicker.getCurrentHour();
        int minute = mTimePicker.getCurrentMinute();

        return new GregorianCalendar(year, month, day, hour, minute).getTime();
    }
}

package com.bignerdranch.android.criminalintent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Daniel on 11/28/2015.
 * DatePickerFragment creates DatePicker Widget inside AlertDialog
 * created by abstract class PickerDialogFragment
 */
public class DatePickerFragment extends PickerDialogFragment {
    private DatePicker mDatePicker;

    /**
     * Creates a new Instance of DatePickerFragment by creating a Bundle of Arguments
     * for the fragment, creating a fragment DatePickerFragment and then assigning the
     * arguments to the newly created fragment
     * @param date on which the new Instance is created: mCrime.getDate()
     * @return new DatePickerFragment created
     */
    public static DatePickerFragment newInstance(Date date) {
        //get Arguments from extends class
        Bundle args = getArgs(date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a View to return to PickerDialogFragment to initialize it in the AlertDialog
     * @return View inflated with a DatePicker as well as the day, month, year options
     */
    @Override
    protected View initLayout() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        //get date
        mDatePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), null);

        return v;
    }

    /**
     * Retrieve Date from DatePicker in AlertDialog
     * @return the date user entered
     */
    @Override
    protected Date getDate() {
        //Get the date from the DatePicker
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();

        //The time remains the same, so pull it from the calendar
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        return new GregorianCalendar(year, month, day, hour, minute).getTime();
    }


    /**
     * Create a View with Customized Alert Dialog
     * @param date date shown in DAtePicker
     * @return Customized View of Alert Dialog
     * REMAARK: commented out due to preference of standard Alert Dialog
     */
//    @Override
//    protected View initLayout(Date date) {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        final int hour = calendar.get(Calendar.HOUR);
//        final int minute = calendar.get(Calendar.MINUTE);
//
//
//        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
//        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
//        //get date
//        mDatePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), null);
//
//
//        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
//
//        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
//        mDatePicker.init(year, month, day, null);
//
//
//        Button mDatePickerOK = (Button) v.findViewById(R.id.dialog_date_button);
//        mDatePickerOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                view.setVisibility(View.GONE);
//                int year = mDatePicker.getYear();
//                int month = mDatePicker.getMonth();
//                int day = mDatePicker.getDayOfMonth();
//                Date date = new GregorianCalendar(year, month, day).getTime();
//                sendResult(Activity.RESULT_OK, date);
//            }
//        });
//
//        return v;
//    }
}

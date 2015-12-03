package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 12/1/2015.
 *
 * PickerDialogFragment is an abstract class used tp set up the necessary components for
 * DatePickerFragment and TimePickerFragment that will utilize AlertDialog and communicate
 * back with CrimeFrament anu changes made to date or time.
 */
public abstract class PickerDialogFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.bignerdranch.android.criminalIntent.date";

    protected Calendar mCalendar;

    protected abstract View initLayout();
    protected abstract Date getDate();

    /**
     * Creates and Returns argument Bundles for fragment
     * @param date passed from child class newInstance(date)
     * @return Bundle of Arguments creates
     */
    protected static Bundle getArgs(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        return args;
    }

    /**
     * Upon creation of the Dialog we give appropriate dialog (DatePicker or TimePicker)
     * our current data, inflate the layout, and set up the AlertDialog.
     * We will also return a Result.
     */
    @Override
    public Dialog onCreateDialog(Bundle onSavedInstanceState) {
        //get DatePicker date from Argument Bundle
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        //create and inflate a View with appropriate Widget to add into AlertDialog
        View v = initLayout();
        //Instantiate AlertDialog with View we just created
        return  new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date date = getDate();
                //return results of date set
                sendResult(Activity.RESULT_OK, date);
            }
        }).create();
    }

    /**
     * Puts date in intent with extras then sends results back to TargetFragement (this case CrimeFragment)
     * @param resultCode checks to see if there was a change in date
     * @param date the new date
     */
    private void sendResult(int resultCode, Date date) {
        if(getTargetFragment() == null)
            return;
        //creates intent to put extras in
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        //send results back to CrimeFragment
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}

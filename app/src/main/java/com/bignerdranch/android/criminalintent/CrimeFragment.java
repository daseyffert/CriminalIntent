package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Daniel on 10/29/2015.
 * CrimeFraqment Class is a controller that interacts with model, Crime Class,
 * and its View Objects, fragment_crime XML.
 * PURPOSE: present details of specific crime and update those details as
 * user updates/changes them
 */
public class CrimeFragment extends Fragment{

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Crime mCrime;
    private TextView mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;


    //ATTACHING ARGUMENTS: create fragment Instance and Bundle up arguments
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        //NOTICE: that Override methods in Fragments must
        //be public because called by their hosting Activity
        super.onCreate(savedInstanceState);

        //RETRIEVING ARGUMENTS: getArguments() retrieves Bundle
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    //Explicitly inflate Fragment's View and get Reference to widgets
    //PURPOSE: In fragment Lifecycle, this method is called, when adding fragment to Fragment Manager,
    //to inflate fragment_crime, add textChange listener to textView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Explicitly inflate Fragment View with .inflate(resID, container, boolean);
        //resID = fragment layout resource, container = fragment view's parent
        //boolean = whether to add inflated view to view's parent(false because i will add the view in Activity's code)
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        //Wire up crime_title to respond to user text
        mTitleField = (TextView) v.findViewById(R.id.fragment_crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Intentionally left blank
            }
        });

        //Get reference to mDateButton and set date of crime
        mDateButton = (Button) v.findViewById(R.id.fragment_crime_date);
        updateDate();

        mDateButton.setText(mCrime.getDate().toString());
        final String date = DateFormat.getLongDateFormat(getActivity()).format(mCrime.getDate());
        mDateButton.setText(date);
        //set clicklistener for date widget
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //accepts fragment that will be the target
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                //to get dialogFragment added to fragment manager
                dialog.show(manager, DIALOG_DATE);
            }
        });

        //Get reference to mTimeButton and set date of crime
        mTimeButton = (Button) v.findViewById(R.id.fragment_crime_time);
        updateTime();

        mTimeButton.setText(DateFormat.format("h:mm a", mCrime.getDate()));
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                //to get dialogFragment added to fragment manager
                dialog.show(manager, DIALOG_TIME);
            }
        });




        //Get reference to mSolvedCheckBox and set up listener
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.fragment_crime_solved_crime);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set Crime's solved property in Model to isChecked
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK)
            return;

        Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        mCrime.setDate(date);

        switch(requestCode) {
            case REQUEST_DATE:
                updateDate();
                break;
            case REQUEST_TIME:
                updateTime();
                break;
        }

    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMMM d, yyyyy", mCrime.getDate()));
    }

    private void updateTime() {
        mTimeButton.setText(DateFormat.format("h:mm a", mCrime.getDate()));
    }
}

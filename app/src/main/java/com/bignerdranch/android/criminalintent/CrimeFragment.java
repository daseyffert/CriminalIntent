package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Daniel on 10/29/2015.
 * CrimeFraqment Class is a controller that interacts with model, Crime Class,
 * and its View Objects, fragment_crime XML.
 * PURPOSE: present details of specific crime and update those details as
 * user updates/changes them
 */
public class CrimeFragment extends Fragment{

    private Crime mCrime;
    private TextView mTitleField;

    @Override
    public void onCreate(Bundle savedInstanceState){
        //NOTICE: that Override methods in Fragments must
        //be public because called by their hosting Activity
        super.onCreate(savedInstanceState);

        mCrime = new Crime();
    }

    /**
     * Explicitly inflate Fragment's View and get Reference to widgets
     * PURPOSE: In fragment Lifecycle, this method is called, when adding fragment to Fragment Manager,
     * to inflate fragment_crime, add textChange listener to textView
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Explicitly inflate Fragment View with .inflate(resID, container, boolean);
        //resID = fragment layout resource, container = fragment view's parent
        //boolean = whether to add inflated view to view's parent(false because i will add the view in Activity's code)
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        //Wire up crime_title to respond to user text
        mTitleField = (TextView) v.findViewById(R.id.fragment_crime_crime_title);
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

        return v;
    }
}

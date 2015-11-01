package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daniel on 10/29/2015.
 * CrimeFraqment Class is a controller that interacts with model, Crime Class,
 * and its View Objects, fragment_crime XML.
 * PURPOSE: present details of specific crime and update those details as
 * user updates/changes them
 */
public class CrimeFragment extends Fragment{

    private Crime mCrime;

    @Override
    public void onCreate(Bundle savedInstanceState){
        //NOTICE: that Override methods in Fragments must
        //be public because called by their hosting Activity
        super.onCreate(savedInstanceState);

        mCrime = new Crime();
    }

    /**
     * Explicitly inflate Fragment's View
     * PURPOSE: In fragment Lifecycle this method is called during setContentView()
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Explicitly inflate Fragment View with .inflate(resID, container, boolean);
        //resID = fragment layout resource, container = fragment view's parent
        //boolean = whether to add inflated view to view's parent(false because i will add the view in Activity's code)
        View v = inflater.inflate(R.layout.fragment_crime,container,false);
        return v;
    }
}

package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class CrimeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        //Get Fragment Manager first so that can get fragment class
        FragmentManager fm = getSupportFragmentManager();
        //Ask fragment manager if fragment_container is in list
        Fragment fragment = fm.findFragmentById(R.id.activity_crime_fragment_container);
        //Checks if fragment manager found and returned fragment_container
        if(fragment == null){
            fragment = new CrimeFragment();
            //.beginTransaction() = creates and returns instance of FRAGMENT TRANSACTION
            //.add().commit() = create and commit FRAGMENT TRANSACTION
            fm.beginTransaction().add(R.id.activity_crime_fragment_container, fragment).commit();
        }
    }


}

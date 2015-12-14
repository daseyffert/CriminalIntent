package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Daniel on 11/8/2015.
 * Abstract Fragment Activity is created to allow re-usability of code when
 * creating more fragments to use
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_fragment);

        //Get Fragment Manager first so that can get fragment class
        FragmentManager fm = getSupportFragmentManager();
        //Ask fragment manager if fragment_container is in list
        Fragment fragment = fm.findFragmentById(R.id.activity_crime_fragment_container);
        //Checks if fragment manager found and returned fragment_container
        if(fragment == null){
            fragment = createFragment();
            //.beginTransaction() = creates and returns instance of FRAGMENT TRANSACTION
            //.add().commit() = create and commit FRAGMENT TRANSACTION
            fm.beginTransaction().add(R.id.activity_crime_fragment_container, fragment).commit();
        }
    }

}


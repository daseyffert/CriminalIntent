package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 11/26/2015.
 */
public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public Intent getParentActivityIntent() {
        Intent intent = new Intent(this,CrimeListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        //ViewPager Adapter requires fragment managers
        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentStatePagerAdapter is agent managing conversation with ViewPager
        //what does the agent do?
        //Add fragment you return and help ViewPager identify fragment's View
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            /**
             * fetches crime instance for given position then uses crime's id
             *  to create and return proper CrimeFragment
             * */
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }
            /**
             * returns number of items in array list
             * */
            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //set ViewPager current item to index of selected crime
        for(int i = 0; i < mCrimes.size(); i++)
            if(mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
    }
}

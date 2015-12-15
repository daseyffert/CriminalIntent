package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 11/8/2015.
 * The CrimeLab Class is a singleton used to store only one
 * instance of each crime allowing for one owner of crime data and
 * for ease of passing data between controller classes
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //private Constructor class that only get made through the get() method
    //if applicable
    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void delCrime(Crime crime) {
        mCrimes.remove(crime);
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime : mCrimes){
            if(crime.getId().equals(id))
                return crime;
        }
        return null;
    }
}

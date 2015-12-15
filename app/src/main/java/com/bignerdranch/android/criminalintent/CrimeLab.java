package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bignerdranch.android.criminalintent.CrimeDbSchema.CrimeTable;


/**
 * Created by Daniel on 11/8/2015.
 * The CrimeLab Class is a singleton used to store only one
 * instance of each crime allowing for one owner of crime data and
 * for ease of passing data between controller classes
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //private Constructor class that only get made through the get() method
    //if applicable
    private CrimeLab(Context context){
        //open sandbox creating new Database if there isn't one already
        //create a new database if this is the first
        //else check version of database
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);

        mDatabase.insert(CrimeTable.NAME, null, values);

    }

    public void delCrime(UUID crimeId) {
        String uuidString = crimeId.toString();

        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] {id.toString()});

        try {
            if(cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }

    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1:0);

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new CrimeCursorWrapper(cursor);
    }
}

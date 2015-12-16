package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Date;
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
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;
    private TextView mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;


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

        //Enable menu options
        setHasOptionsMenu(true);

        //RETRIEVING ARGUMENTS: getArguments() retrieves Bundle
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
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

        //set on click listener for date widget
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

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                //accepts fragment that will be Target
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

        //Get reference to Button that sends report ans set up listener then implement implicit intent
        mReportButton = (Button) v.findViewById(R.id.fragment_crime_crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //W/o using ShareCompatIntentBuilder
                ////Send a string must be sent using ACTION_SEND as a plain/text
                //Intent intent = new Intent(Intent.ACTION_SEND);
                //intent.setType("text/plain");
                ////Extras are put the same way but using arbitrary text
                //intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                //intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                ////createChooser allows for you to choose the application from which to open intent
                //intent = Intent.createChooser(intent, getString(R.string.send_report));
                //startActivity(intent);
                //*Chapter 15 Challenge 1 ShareCompat*
                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .startChooser();
            }
        });

        //get a reference to suspect button and set up listener then implement implicit intent
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.fragment_crime_crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        //once suspect is assigned show their name
        if(mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());

        //PackageManager knows all devices' components installed
        //If application doesn't have contact app disable suspect button
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
            mSuspectButton.setEnabled(false);

        //Get reference to CallSuspect button and set up listener
        mCallSuspectButton = (Button) v.findViewById(R.id.fragment_crime_call_suspect);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                String selectClause = ContactsContract.CommonDataKinds.Phone._ID + " = ?";

                String[] fields = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                String[] selectParams = {Long.toString(mCrime.getContact())};

                Cursor cursor = getActivity().getContentResolver().query(contentUri, fields, selectClause, selectParams, null);

                if(cursor != null && cursor.getCount() > 0) {
                    try {
                        cursor.moveToFirst();
                        String number = cursor.getString(0);
                        Uri phoneNumber = Uri.parse("tel:" + number);
                        Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
                        startActivity(intent);
                    } finally {
                        cursor.close();
                    }
                }
            }
        });

        return v;
    }

    /**
     * Create menu besed off menu resource for crime_fragment
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.fragment_crime_menu_item_delete_crime:
                //delete the crime
                CrimeLab.get(getActivity()).delCrime(mCrime.getId());
                //return to parent activity
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Updates the appropriate button (date vs. time) if resultCode indicated something
     * was changed and requestCode indicating which has changed.
     * @param requestCode identifies whether to change Date or Time
     * @param resultCode returns whether date was changed or not
     * @param data intent that has date in the extras
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK)
            return;
        //To check for the set Time and date
        if(requestCode == REQUEST_DATE || requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);

            switch (requestCode) {
                case REQUEST_DATE:
                    updateDate();
                    break;
                case REQUEST_TIME:
                    updateTime();
                    break;
            }
        } else if(requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            //Specify which fields you want to query to return values for
            String[] queryFields = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
            //Perform your query - the contactUri is like a "where" clause here
            Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                //Double check you actually got results
                if(cursor.getCount() == 0)
                    return;

                //Pull out the first column of the frist row of data = that is your suspects' name
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                long contact = cursor.getLong(1);

                mCrime.setSuspect(suspect);
                mCrime.setContact(contact);

                mSuspectButton.setText(suspect);
                //enables button and changes text
                //updateCallSuspectButton();
            } finally {
                cursor.close();
            }
        }


    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, MMMM d, yyyyy", mCrime.getDate()));
    }

    private void updateTime() {
        mTimeButton.setText(DateFormat.format("h:mm a", mCrime.getDate()));
    }

    /**
     * Creates four strings and pieces them together making the Crime Report
     * @return the Crime Report
     */
    private String getCrimeReport() {
        String solvedString = null;

        //String 1 Check whether crime has been solved to add string needed
        if(mCrime.isSolved())
            solvedString = getString(R.string.crime_report_solved);
        else
            solvedString = getString(R.string.crime_report_unsolved);

        //String 2 format the way the date is displayed
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        //String 3 check if there is a suspect to set correct string resource
        String suspect = mCrime.getSuspect();
        if(suspect == null)
            suspect = getString(R.string.crime_report_no_suspect);
        else
            suspect = getString(R.string.crime_report_suspect);

        //join all the strings into sorrect assigned value
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);


        return report;
    }
}

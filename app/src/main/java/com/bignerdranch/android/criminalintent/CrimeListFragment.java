package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Daniel on 11/8/2015.
 * This class creates the Recycler views inside a different Fragment, from abstract Fragment, created for this Activity.
 *
 * RecyclerView only job is recycling and positioning Views. To attain RecyclerView must work with: ViewHolder and Adapter.
 * ViewHolder has one job; hold on to the View. Meaning, initialize the Views, then findViewById() to attach Views to the Layout.
 * Adapter has 2 jobs: creating necessary ViewHolders, binding ViewHolders to data from model layer.
 */
public class CrimeListFragment extends Fragment{

    private static final String EXTRA_SUBTITLE_VISIBLE = "com.bignerdranch.android.criminalintent.subtitle_visibility";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private int mPreviousAdapterSelected = -1;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        //tell FragmentManger that your fragment must make call to onCreateOptionsMenu()
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        //RV does not position Views by ITSELF, it is aided by LayoutManager
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_crime_list_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(EXTRA_SUBTITLE_VISIBLE);

        //update interface
        updateUI();
        return view;
    }

    //When returning back to CrimeList Activity we must update changes if any on onResume()
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    /**
     * Populate the menu with the menu created in the resource files
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        //Dealing with displaying number of crimes as a subtitle
        MenuItem subtitleItem = menu.findItem(R.id.fragment_crime_list_menu_item_show_subtitle);
        //depending on whether visible or not change the name
        if(mSubtitleVisible)
            subtitleItem.setTitle(R.string.hide_subtitles);
        else
            subtitleItem.setTitle(R.string.show_subtitle);
    }

    /**
     * Check to see if item from menu was selected then proceeds properly depending on item selected.
     * @param item item from the menu options
     * @return if the actions were performed properly
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //to add a new crime
            case R.id.fragment_crime_list_menu_item_new_crime:
                //Create and add new crime
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                //Create intent to start a new instance of CrimePagerActivity
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;

            //to show subtitles and hide "Show Subtitle" option
            case R.id.fragment_crime_list_menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                //make option invisible/visible
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * update the subtitle of the crime when called from onOptionsItemSelected()
     */
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        //when subtitle is set to invisible clear the subtitle
        if(!mSubtitleVisible)
            subtitle = null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    /**
     * Updates the Interface with all the crimes in recycler views
     */
    private void updateUI() {
        CrimeLab crimelab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimelab.getCrimes();


        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            if (mPreviousAdapterSelected < 0)
                mAdapter.notifyDataSetChanged();
            else {
                mAdapter.notifyItemChanged(mPreviousAdapterSelected);
                mPreviousAdapterSelected = -1;
            }
        }

        //make sure number of crimes stays up to date
        updateSubtitle();
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        //SECOND:RecyclerView creates ViewHolder to Display new VIEW
        //In this method you create a view and wrap it with a ViewHolder
        //*Remark: This method is only called until suffecient number of ViewHOlders are made
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        //THIRD: Adapter will look up model data for position and bind to ViewHolder's VIEW
        //To BIND, use position to find the right model data then update the View
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        //FIRST: RecyclerView finds out how many items in the list
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_checkbox);
        }

        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //Starting a new CrimeActivity by calling newIntent from CrimeActivity
        @Override
        public void onClick(View v) {
            mPreviousAdapterSelected = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);

        }
    }

    //Save visiblilty state of
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}



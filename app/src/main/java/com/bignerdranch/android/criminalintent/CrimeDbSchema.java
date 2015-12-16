package com.bignerdranch.android.criminalintent;

/**
 * Created by Daniel on 12/15/2015
 * This class will implement the Database Schema required for the project
 */
public class CrimeDbSchema {

    /**
     * CrimeTable class only exists to define the String constant needed
     * to describe the moving pieces of your table
     */
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
            public static final String CONTACT = "contact";
        }
    }
}

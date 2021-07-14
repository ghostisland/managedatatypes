package com.example.managedatatypes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userdetails.db";
    public static int DATABASE_VERSION = 1;
    private SQLiteDatabase dbUsers;
    public static String grabStringHolder;

    // THESE VALUES ARE TO INSERT INTO THE DATABASE WHEN THE APP IS FIRST INSTALLED

    long timeStampLong = System.currentTimeMillis()/1000;

    public String[] dateArray = {
            Long.toString(timeStampLong-86400),
            Long.toString(timeStampLong-172800),
            Long.toString(timeStampLong-259200),
            Long.toString(timeStampLong-345600)};

    public String[] nameArray = {
            "mary",
            "john",
            "paul",
            "jane"};

    public String[] emaiArray = {
            "mary@email.com",
            "john@email.com",
            "paul@email.com",
            "jane@email.com"};

    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // THIS METHOD WILL BE CALLED THE FIRST TIME WE ACCESS THIS DATABASE
    // THIS IS WHERE WE CREATE THE INITIAL DATABASE

    @Override
    public void onCreate(SQLiteDatabase dbDeclare) {
        this.dbUsers = dbDeclare;

        // THE MoodContract CLASS VARIABLES ARE PASSED IN HERE
        // THIS onCreate METHOD WILL ONLY BE CALLED THE FIRST TIME WE ACCESS THIS DATABASE

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                SQLiteContract.SaveUsersTable.TABLE_USERS + " ( " +
                SQLiteContract.SaveUsersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SQLiteContract.SaveUsersTable.COLUMN_DATE + " TEXT, " +
                SQLiteContract.SaveUsersTable.COLUMN_NAME + " TEXT, " +
                SQLiteContract.SaveUsersTable.COLUMN_EMAI + " TEXT, " +
                SQLiteContract.SaveUsersTable.COLUMN_PASS + " TEXT " +
                ")";
        dbUsers.execSQL(SQL_CREATE_USERS_TABLE);

        // WE ALSO WANT TO FILL THE DATABASE WITH QUESTION RIGHT AWAY WITH THE FOLLOWING METHOD

        fillUsersTable();
    }

    // IF WE EVER DECIDE TO MAKE CHANGES TO THIS DATABASE I.E. ADD ANOTHER COLUMN WE CAN'T JUST ADD TO onCreate
    // WE NEED TO TELL THE APP TO DO SO USING onUpgrade METHOD - CHANGE "private static final int DATABASE_VERSION = 2;"
    // WE ALSO DEFINE IN onUpgrade HOW WE WANT TO UPGRADE THE DATABASE - DELETE OLD TABLE AND CALL onCreate AGAIN
    // WHEN THE DATABASE HAS BEEN CHANGED IT WILL DROP EXISTING TABLE AND CREATE A NEW ONE
    // OR YOU CAN JUST DELETE THE APP FROM YOUR PHONE AND REINSTALL
    // THIS PROCESS IS ONLY DONE IF THE APP IS ALREADY IN PRODUCTION

    @Override
    public void onUpgrade(SQLiteDatabase dbUpgrade, int oldVersion, int newVersion) {
        dbUpgrade.execSQL("DROP TABLE IF EXISTS " + SQLiteContract.SaveUsersTable.TABLE_USERS);
        onCreate(dbUpgrade);
    }

    // IF ALL ENTRIES IN THE DATABASE HAVE BEEN REMOVED IN THE CALENDAR AND THE APP HAS BEEN REINSTALLED
    // THE DATABASE VERSION IS REVERTED TO VERSION NUMBER 1
    @Override
    public void onDowngrade(SQLiteDatabase dbDowngrade, int oldVersion, int newVersion) {
        dbDowngrade.setVersion(oldVersion);
    }

    // THESE ARE REFERENCED IN THE GETTER AND SETTER METHODS IN THE CLASS "MoodChanges.java"

    public void insertLogFill(String date, String name, String emai, String pass) throws SQLException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_DATE, date);
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_NAME, name);
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_EMAI, emai);
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_PASS, pass);
        database.insert(SQLiteContract.SaveUsersTable.TABLE_USERS, null, cv);
        database.close();
    }

    // THESE ARE REFERENCED IN THE GETTER AND SETTER METHODS IN THE CLASS "SQLiteVariables.java"
    // FILL THE DATABASE WITH DUMMY DATA THE FIRST TIME THE APP IS INSTALLED

    public void fillUsersTable() {
        for (int x = 0; x <emaiArray.length; x ++) {
            SQLiteVariables usersFill = new SQLiteVariables(dateArray[x], nameArray[x], emaiArray[x], "123abc");
            addInitialUsers(usersFill);
        }
    }

    private void addInitialUsers(SQLiteVariables sqliteVariables) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_DATE, sqliteVariables.getDateusers());
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_NAME, sqliteVariables.getNameusers());
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_EMAI, sqliteVariables.getEmaiusers());
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_PASS, sqliteVariables.getPassusers());
        dbUsers.insert(SQLiteContract.SaveUsersTable.TABLE_USERS, null, cv);
    }

    // WE HAVE ALREADY SAVED THE DATA IN OUR DATABASE BUT WE HAVE NO WAY TO RETRIEVE THE DATA YET
    // WE WILL MAKE THE FOLLOWING METHOD PUBLIC AS WE WANT TO ACCESS IT FROM ANOTHER CLASS
    // THE RETURN TYPE IS LIST OF "SQLiteVariables" OBJECT

    public List<SQLiteVariables> getAllUsers() {
        List<SQLiteVariables> usersList = new ArrayList<>();
        dbUsers = getReadableDatabase();

        //  WE NEED A CURSOR TO QUERY OUR DATABASE AND GET ALL ENTRIES FROM THE DATABASE TABLE

        Cursor dbCursor = dbUsers.rawQuery("SELECT * FROM " + SQLiteContract.SaveUsersTable.TABLE_USERS, null);

        // THE FOLLOWING WILL MOVE THE CURSOR TO THE FIRST ENTRY AND RETURN FALSE IF THERE IS NO ENTRY
        // WE WILL MOVE TO THE FIRST ENTRY IF IT EXISTS, THEN WE WILL QUERY THE ENTRY IN "DO"
        // AND FILL A QUESTION OBJECT WITH ITS DATA, THEN MOVE TO THE NEXT ENTRY BUT ONLY IF IT EXISTS
        // THESE ARE REFERENCED IN THE GETTER AND SETTER METHODS IN THE CLASS "SQLiteVariables.java"

        if (dbCursor.moveToFirst()) {
            do {
                SQLiteVariables usersArray = new SQLiteVariables();
                usersArray.setDateusers(dbCursor.getString(dbCursor.getColumnIndex(SQLiteContract.SaveUsersTable.COLUMN_DATE)));
                usersArray.setNameusers(dbCursor.getString(dbCursor.getColumnIndex(SQLiteContract.SaveUsersTable.COLUMN_NAME)));
                usersArray.setEmaiusers(dbCursor.getString(dbCursor.getColumnIndex(SQLiteContract.SaveUsersTable.COLUMN_EMAI)));
                usersArray.setPassusers(dbCursor.getString(dbCursor.getColumnIndex(SQLiteContract.SaveUsersTable.COLUMN_PASS)));

                // ALL THE RETRIEVED QUESTIONS ARE ADDED TO "usersList" ARRAY LIST

                usersList.add(usersArray);
            } while (dbCursor.moveToNext());
        }

        // WE NEED TO CLOSE THE CURSOR AFTER IT IS USED AND RETURN "usersList" ARRAY LIST

        dbCursor.close();
        dbUsers.close();
        return usersList;
    }

    public void deleteUser(Integer id) throws SQLException {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(SQLiteContract.SaveUsersTable.TABLE_USERS,
                SQLiteContract.SaveUsersTable._ID + "=" + id, null);
        database.close();
    }

    public String grabEntry(String email, String entry) {
        dbUsers = getReadableDatabase();
        Cursor result = dbUsers.query(
                // ACCESS THE TABLE WHERE WE WANT TO GET AN ENTRY
                SQLiteContract.SaveUsersTable.TABLE_USERS,
                // DEFINE THE COLUMN WHERE THE ENTRY IS FOUND
                new String[] { entry },
                // DECLARE THE ID COLUMN FOR THE ENTRY
                SQLiteContract.SaveUsersTable.COLUMN_EMAI + "=?",
                // ADD THE ID FOR THE ENTRY WE ARE LOOKING FOR
                new String[] { email },
                // THIS PARAMETER IS FOR GROUPING RESULTS - NOT NEEDED
                null,
                null,
                // WE ARE ONLY ACCESSING ONE ENTRY - NOT NEEDED
                null
        );
        if (result.moveToFirst()) {
            // GET THE QUERIED ENTRY AND ADD IT TO A STRING
            grabStringHolder = result.getString(result.getColumnIndex(entry));
            result.close();
        }
        return grabStringHolder;
    }

    public void deleteDb() throws SQLException {
        SQLiteDatabase database = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + SQLiteContract.SaveUsersTable.TABLE_USERS;
        database.execSQL(clearDBQuery);
        database.close();
    }

    public void updateUser(String name, String pass, String emai) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_NAME, name);
        cv.put(SQLiteContract.SaveUsersTable.COLUMN_PASS, pass);
        database.update(SQLiteContract.SaveUsersTable.TABLE_USERS,cv,
                SQLiteContract.SaveUsersTable.COLUMN_EMAI + "='" + emai + "'",null);
        database.close();
    }
}


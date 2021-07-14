package com.example.managedatatypes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLiteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static SQLiteDbHelper myDBAll;
    public static List<SQLiteVariables> usersList;
    public static int usersCountTotal;
    public static final String TAG = "SQLite";

    public static String jsonDataString = "[" +
            "{\"date\":\"1625564252\",\"name\":\"Henry\",\"email\":\"henry@email.com\",\"password\":\"123abc\"}," +
            "{\"date\":\"1625564252\",\"name\":\"Marta\",\"email\":\"marta@email.com\",\"password\":\"123abc\"}," +
            "{\"date\":\"1625564252\",\"name\":\"Katie\",\"email\":\"katie@email.com\",\"password\":\"123abc\"}," +
            "{\"date\":\"1625564252\",\"name\":\"Teddy\",\"email\":\"teddy@email.com\",\"password\":\"123abc\"}," +
            "{\"date\":\"1625564252\",\"name\":\"Wayne\",\"email\":\"wayne@email.com\",\"password\":\"123abc\"}," +
            "{\"date\":\"1625564252\",\"name\":\"Fredy\",\"email\":\"fredy@email.com\",\"password\":\"123abc\"}" +
            "]";

    public static RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        // INITIALIZE TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbarSQLite);
        setSupportActionBar(toolbar);

        // SET UP A TAG TO REFERENCE THE ACTIVITY FROM THE RECYCLER ADAPTER
        MainActivity.recycleDelete = getResources().getString(R.string.SQLiteActivityRecycle);

        mRecyclerView = findViewById(R.id.my_recycler_view_sqlite);

        myDBAll = new SQLiteDbHelper(this);

        // SET UP THE SPINNER, OPTIONS CAN BE FOUND IN STRING RESOURCES
        Spinner spinnerSQLite = findViewById(R.id.spinnerSQLite);
        ArrayAdapter<CharSequence> adapterSpinnerSQLIte = ArrayAdapter.createFromResource(this,
                R.array.spinnerItemsSQLite, android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerSQLIte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSQLite.setAdapter(adapterSpinnerSQLIte);
        spinnerSQLite.setOnItemSelectedListener(this);
    }

    // SET UP THE ONCLICKLISTENER FOR THE SPINNER
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        findViewById(R.id.btnSQLiteSpinner).setOnClickListener(view1 -> {
            //String text = parent.getItemAtPosition(position).toString();
            //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

            if (position == 0) {
                handleSQLiteSelectAllDialog();
            }
            if (position == 1) {
                handleSQLiteSelectUserDialog();
            }
            if (position == 2) {
                handleSQLiteInsertDialog();
            }
            if (position == 3) {
                handleSQLiteDeleteAllDialog();
            }
            if (position == 4) {
                try {
                    handleSQLiteExportXLS();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // THIS METHOD IS CALLED FROM THE RECYCLER VIEW WHEN CLICKED TO UPDATE ENTRY
    public static void handleSQLiteUpdateDialog(View view, Context context, String email ) {

        Button btnClick = view.findViewById(R.id.btnUpdateDial);
        final EditText nameEdit = view.findViewById(R.id.nameUpdateDial);
        final EditText passwordEdit = view.findViewById(R.id.passwordUpdateDial);
        final TextView titleEdit = view.findViewById(R.id.titleUpdateDial);

        titleEdit.setText(email);

        SQLiteDbHelper myDBAll;
        myDBAll = new SQLiteDbHelper(context);

        btnClick.setOnClickListener(view1 -> {
            myDBAll.updateUser(nameEdit.getText().toString(),
                               passwordEdit.getText().toString(),
                               email);

            Toast.makeText(context, context.getResources().getString(R.string.entryupdated),
                    Toast.LENGTH_LONG).show();

            // UPDATE THE RECYCLER VIEW WITH THE CHANGES
            RecyclerAdapter.nameViewUpdate.setText(nameEdit.getText().toString());
            RecyclerAdapter.passwordViewUpdate.setText(passwordEdit.getText().toString());
        });
    }

    // THIS METHOD IS CALLED FROM THE SPINNER TO POPULATE THE RECYCLERVIEW WITH ALL DB ENTRIES
    private void handleSQLiteSelectAllDialog() {

        // DO NOT CHANGE THE ORDER OF THESE
        SQLiteDbHelper dbHelperMoodMon = new SQLiteDbHelper(mRecyclerView.getContext());
        myDBAll = new SQLiteDbHelper(mRecyclerView.getContext());
        usersList = dbHelperMoodMon.getAllUsers();
        usersCountTotal = usersList.size();

        // GET THE LIST OF ALL DB ENTRIES AND ADD TO STRING BUILDER
        StringBuilder allUsersBuild = new StringBuilder();
        allUsersBuild.append("[");
        for(int tb = 0; tb < usersCountTotal; tb++){
            allUsersBuild
                    .append("{\"date\":\"")
                    .append(usersList.get(tb).getDateusers())
                    .append("\",\"name\":\"")
                    .append(usersList.get(tb).getNameusers())
                    .append("\",\"email\":\"")
                    .append(usersList.get(tb).getEmaiusers())
                    .append("\",\"password\":\"")
                    .append(usersList.get(tb).getPassusers())
                    .append("\"},");
        }
        allUsersBuild.setLength(allUsersBuild.length() - 1);
        allUsersBuild.append("]");

        //System.out.println("StringBuilder All SQLite Entries = " + allUsersBuild);

        jsonDataString = allUsersBuild.toString();
        addItemsFromJSON();
    }

    // THIS METHOD SELECTS AN ENTRY BASED ON THE DETAILS ENTERED IN THE ALERT DIALOG
    private void handleSQLiteSelectUserDialog() {

        View view = getLayoutInflater().inflate(R.layout.dialog_select_user_activity, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button btnClick = view.findViewById(R.id.btnSelectUserDial);
        final EditText emailEdit = view.findViewById(R.id.emailSelectUserDial);
        final EditText passwordEdit = view.findViewById(R.id.passwordSelectUserDial);

        btnClick.setOnClickListener(view1 -> {

            if (emailEdit.getText().toString().equals("") || passwordEdit.getText().toString().equals("")){
                Toast.makeText(SQLiteActivity.this,
                        getResources().getString(R.string.fillallfields), Toast.LENGTH_LONG).show();
            } else {

                String dateGrab = myDBAll.grabEntry(emailEdit.getText().toString(), SQLiteContract.SaveUsersTable.COLUMN_DATE);
                String nameGrab = myDBAll.grabEntry(emailEdit.getText().toString(), SQLiteContract.SaveUsersTable.COLUMN_NAME);
                String emaiGrab = myDBAll.grabEntry(emailEdit.getText().toString(), SQLiteContract.SaveUsersTable.COLUMN_EMAI);
                String passGrab = myDBAll.grabEntry(emailEdit.getText().toString(), SQLiteContract.SaveUsersTable.COLUMN_PASS);

                if (passwordEdit.getText().toString().equals(passGrab)) {

                    // GET THE LIST OF DB ENTRIES AND ADD TO STRING BUILDER
                    StringBuilder selectEntry = new StringBuilder();
                    selectEntry.append("[");
                    selectEntry
                            .append("{\"date\":\"")
                            .append(dateGrab)
                            .append("\",\"name\":\"")
                            .append(nameGrab)
                            .append("\",\"email\":\"")
                            .append(emaiGrab)
                            .append("\",\"password\":\"")
                            .append(passGrab)
                            .append("\"},");
                    selectEntry.setLength(selectEntry.length() - 1);
                    selectEntry.append("]");

                    //System.out.println("StringBuilder All SQLite Entries = " + selectEntry);

                    Toast.makeText(SQLiteActivity.this, getResources().getString(R.string.entryfound),
                            Toast.LENGTH_LONG).show();

                    jsonDataString = selectEntry.toString();
                    addItemsFromJSON();
                } else {
                    Toast.makeText(SQLiteActivity.this,
                            getResources().getString(R.string.wrongcred), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // THIS METHOD REFRESHES THE RECYCLERVIEW TO DISPLAY AN ENTRY WHEN IT IS INSERTED
    public static void selectRefeshSQLite(String email, String password){

        String dateGrab = myDBAll.grabEntry(email, SQLiteContract.SaveUsersTable.COLUMN_DATE);
        String nameGrab = myDBAll.grabEntry(email, SQLiteContract.SaveUsersTable.COLUMN_NAME);
        String emaiGrab = myDBAll.grabEntry(email, SQLiteContract.SaveUsersTable.COLUMN_EMAI);
        String passGrab = myDBAll.grabEntry(email, SQLiteContract.SaveUsersTable.COLUMN_PASS);

        if (password.equals(passGrab)) {

            // GET THE LIST OF DB ENTRIES AND ADD TO STRING BUILDER
            StringBuilder selectEntry = new StringBuilder();
            selectEntry.append("[");
            selectEntry
                    .append("{\"date\":\"")
                    .append(dateGrab)
                    .append("\",\"name\":\"")
                    .append(nameGrab)
                    .append("\",\"email\":\"")
                    .append(emaiGrab)
                    .append("\",\"password\":\"")
                    .append(passGrab)
                    .append("\"},");
            selectEntry.setLength(selectEntry.length() - 1);
            selectEntry.append("]");

            jsonDataString = selectEntry.toString();
            addItemsFromJSON();
        }
    }

    // THIS METHOD INSERTS AN INDIVIDUAL ENTRY
    private void handleSQLiteInsertDialog() {

        // DO NOT CHANGE THE ORDER OF THESE

        SQLiteDbHelper dbHelperMoodMon = new SQLiteDbHelper(this);
        myDBAll = new SQLiteDbHelper(this);
        usersList = dbHelperMoodMon.getAllUsers();
        usersCountTotal = usersList.size();

        View view = getLayoutInflater().inflate(R.layout.dialog_insert_activity, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button btnClick = view.findViewById(R.id.btnInsertDial);
        final EditText nameEdit = view.findViewById(R.id.nameInsertDial);
        final EditText emailEdit = view.findViewById(R.id.emailInsertDial);
        final EditText passwordEdit = view.findViewById(R.id.passwordInsertDial);

        btnClick.setOnClickListener(view1 -> {

            if (nameEdit.getText().toString().equals("") ||
                    emailEdit.getText().toString().equals("") ||
                    passwordEdit.getText().toString().equals("")) {
                Toast.makeText(SQLiteActivity.this,
                        getResources().getString(R.string.fillallfields), Toast.LENGTH_LONG).show();
            } else {

                String emaiGrab = myDBAll.grabEntry(emailEdit.getText().toString(), SQLiteContract.SaveUsersTable.COLUMN_EMAI);

                if (emailEdit.getText().toString().equals(emaiGrab)) {

                    Toast.makeText(SQLiteActivity.this,
                            getResources().getString(R.string.alreadyexist), Toast.LENGTH_LONG).show();
                } else {

                    long timeStampLong = System.currentTimeMillis()/1000;
                    String timeStamp = Long.toString(timeStampLong);

                    myDBAll.insertLogFill(
                            timeStamp,
                            nameEdit.getText().toString(),
                            emailEdit.getText().toString(),
                            passwordEdit.getText().toString());

                    Toast.makeText(SQLiteActivity.this,
                            getResources().getString(R.string.entryinsert), Toast.LENGTH_LONG).show();

                    // UPDATE THE RECYCLER VIEW WITH THE CHANGES
                    selectRefeshSQLite(emailEdit.getText().toString(),passwordEdit.getText().toString());

                }
            }
        });
    }

    // THIS METHOD DELETES ALL ENTRIES WITH AN ALERT DIALOG WARNING
    private void handleSQLiteDeleteAllDialog() {

        new android.app.AlertDialog.Builder(SQLiteActivity.this)
                .setMessage("Delete all entries?" + "\n\n" + "Long click on list items to delete individual users")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> {
                    myDBAll.deleteDb();
                    Toast.makeText(SQLiteActivity.this, "All entries Deleted",
                            Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    // IF ANY CHANGES HAVE BEEN MADE UPDATE THE JSON CONTAINING THE DATA AND ADD TO THE RECYCLERVIEW
    public static void addItemsFromJSON() {
        try {
            JSONArray jsonArray = new JSONArray(jsonDataString);

            // SET UP THE RECYCLER VIEW TO HOLD THE DATA
            List<Object> viewItems = new ArrayList<>();
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter mAdapter = new RecyclerAdapter(mRecyclerView.getContext(), viewItems);

            for (int i=0; i<jsonArray.length(); ++i) {
                JSONObject itemObj = jsonArray.getJSONObject(i);
                String date = itemObj.getString("date");
                String name = itemObj.getString("name");
                String email = itemObj.getString("email");
                String password = itemObj.getString("password");
                RecyclerVariables variables = new RecyclerVariables(
                        MainActivity.formatDate(date,"yyyy/MM/dd' ('HH:mm:ss)"),
                                                name,
                                                email,
                                                password);
                viewItems.add(variables);
                mRecyclerView.setAdapter(mAdapter);
            }

        } catch (JSONException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }
    }

    // THIS METHOD SAVES CODE TO CREATE NAME, EMAIL, AND PASSWORD ARRAYS IN THE EXPORT XLS METHOD
    private List<String> jsonBuilder(String column) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonDataString);
        StringBuilder usersBuild = new StringBuilder();
        for (int ub = 0; ub < jsonArray.length(); ub++) {
            JSONObject itemObj = jsonArray.getJSONObject(ub);
            usersBuild
                    .append(itemObj.getString(column))
                    .append(",");
        }
        usersBuild.setLength(usersBuild.length() - 1);
        List<String> nameValues = new ArrayList<>(Arrays.asList(usersBuild.toString().split(",")));
        return nameValues;
    }

    // EXPORT ALL DATA AS XLS FILE

    public void handleSQLiteExportXLS() throws JSONException {

        Workbook exWorkBook = new HSSFWorkbook();
        Sheet exSheet = exWorkBook.createSheet("Export");

        // SET SOME CELL FORMATTING STYLES
        CellStyle cellYellow = exWorkBook.createCellStyle();
        cellYellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        cellYellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellYellow.setBorderTop(BorderStyle.THIN);
        cellYellow.setBorderBottom(BorderStyle.THIN);
        cellYellow.setBorderLeft(BorderStyle.THIN);
        cellYellow.setBorderRight(BorderStyle.THIN);
        //cellYellow.setAlignment(HorizontalAlignment.CENTER);

        CellStyle borderThin = exWorkBook.createCellStyle();
        borderThin.setBorderTop(BorderStyle.THIN);
        borderThin.setBorderBottom(BorderStyle.THIN);
        borderThin.setBorderLeft(BorderStyle.THIN);
        borderThin.setBorderRight(BorderStyle.THIN);

        CellStyle centerBorder = exWorkBook.createCellStyle();
        centerBorder.setBorderTop(BorderStyle.THIN);
        centerBorder.setBorderBottom(BorderStyle.THIN);
        centerBorder.setBorderLeft(BorderStyle.THIN);
        centerBorder.setBorderRight(BorderStyle.THIN);

        // GET THE CURRENT TIMESTAMP CONVERT IT TO HUMAN READABLE AND ADD TO FILE NAME AND TITLE
        long timeStampLong = System.currentTimeMillis();
        String timeStamp = Long.toString(timeStampLong);
        String fileName = TAG + "_" + MainActivity.formatDate(timeStamp,"yyyy-MM-dd'_'HH.mm.ss");
        String fileString = fileName + ".xls";

        // SET WIDTH OF FIRST FIVE COLUMNS
        exSheet.setColumnWidth(0,(10*500));
        exSheet.setColumnWidth(1,(10*500));
        exSheet.setColumnWidth(2,(10*500));
        exSheet.setColumnWidth(3,(10*500));
        exSheet.setColumnWidth(4,(10*500));

        // DECLARE FIRST ROWS FOR STUDENT INFO
        Row row01 = exSheet.createRow(0);

        // SET TITLE FOR STUDENT INFO
        Cell row01coll01 = row01.createCell(0);
        row01coll01.setCellValue("Name");
        row01coll01.setCellStyle(cellYellow);
        Cell row01coll02 = row01.createCell(1);
        row01coll02.setCellValue("Email");
        row01coll02.setCellStyle(cellYellow);
        Cell row01coll03 = row01.createCell(2);
        row01coll03.setCellValue("Password");
        row01coll03.setCellStyle(cellYellow);
        Cell row01coll04 = row01.createCell(3);
        row01coll04.setCellValue("Date");
        row01coll04.setCellStyle(cellYellow);
        Cell row01coll05 = row01.createCell(4);
        row01coll05.setCellValue("Time");
        row01coll05.setCellStyle(cellYellow);

        File file = new File(getFilesDir(), fileString);
        FileOutputStream outputStream =null;

        JSONArray jsonArray = new JSONArray(jsonDataString);

        // GET THE LIST OF ALL DATE ENTRIES AND ADD TO STRING BUILDER
        StringBuilder dateUsersBuild = new StringBuilder();
        for(int dj = 0; dj < jsonArray.length(); dj++){
            JSONObject dateitemObj = jsonArray.getJSONObject(dj);
            dateUsersBuild
                    .append(MainActivity.formatDate(dateitemObj.getString("date"),"dd/MM/yyyy"))
                    .append(",");
        }
        dateUsersBuild.setLength(dateUsersBuild.length() - 1);

        // GET THE LIST OF ALL TIME ENTRIES AND ADD TO STRING BUILDER
        StringBuilder timeUsersBuild = new StringBuilder();
        for(int dj = 0; dj < jsonArray.length(); dj++){
            JSONObject timeitemObj = jsonArray.getJSONObject(dj);
            timeUsersBuild
                    .append(MainActivity.formatDate(timeitemObj.getString("date"),"HH:mm:ss"))
                    .append(",");
        }
        timeUsersBuild.setLength(timeUsersBuild.length() - 1);

        List<String> nameValues = jsonBuilder("name");
        List<String> emaiValues = jsonBuilder("email");
        List<String> passValues = jsonBuilder("password");
        List<String> dateValues = new ArrayList<>(Arrays.asList(dateUsersBuild.toString().split(",")));
        List<String> timeValues = new ArrayList<>(Arrays.asList(timeUsersBuild.toString().split(",")));

        // SET UP AND EXPORT THE DATA
        for (int av = 0; av < dateValues.size(); av ++) {

            Row rowAV = exSheet.createRow(1 + av);

            Cell cellNV = rowAV.createCell(0);
            cellNV.setCellValue(nameValues.get(av));
            cellNV.setCellStyle(borderThin);

            Cell cellEV = rowAV.createCell(1);
            cellEV.setCellValue(emaiValues.get(av));
            cellEV.setCellStyle(borderThin);

            Cell cellPV = rowAV.createCell(2);
            cellPV.setCellValue(passValues.get(av));
            cellPV.setCellStyle(centerBorder);

            Cell cellDV = rowAV.createCell(3);
            cellDV.setCellValue(dateValues.get(av));
            cellDV.setCellStyle(borderThin);

            Cell cellTV = rowAV.createCell(4);
            cellTV.setCellValue(timeValues.get(av));
            cellTV.setCellStyle(borderThin);
        }

        try {
            outputStream = new FileOutputStream(file);
            exWorkBook.write(outputStream);
            //Toast.makeText(getApplicationContext(),"Export Successful",Toast.LENGTH_LONG).show();
            Context context = getApplicationContext();
            Uri path = FileProvider.getUriForFile(context, MainActivity.PACKAGE_NAME + ".fileprovider", file);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/xls");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, fileName);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(fileIntent);

        } catch (java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(),"Export Error",Toast.LENGTH_LONG).show();
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
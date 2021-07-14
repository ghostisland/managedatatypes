package com.example.managedatatypes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static Retrofit retrofit;
    public static InterfaceRetrofit interfaceRetrofit;
    public static String BASE_URL;
    public static String recycleDelete = "";
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DEFINE THE BASE URL FROM STRING RESOURCES EITHER FOR NODE.JS OR PHP DEPENDING ON ACTIVITY
        BASE_URL = getResources().getString(R.string.mongonodeurl);

        // SET THE NAME OF THE PACKAGE ID FOR USE LATER
        PACKAGE_NAME = getApplicationContext().getPackageName();

        // INITIALIZE TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        // SET UP THE TOOLBAR TO DISPLAY THE APP NAME
        toolbar.setTitle(getResources().getString(R.string.app_name));

        // INSIDE ONCREATE INSTANTIATE THE RETROFIT OBJECT
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                // THIS "GsonConverterFactory" WILL CONVERT THE GSON DATA TO A JAVA OBJECT
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // INSTANTIATE "interfaceRetrofit" OBJECT. "retrofit.create" METHOD TAKES A PARAMETER OF CLASS NAME TO CREATE
        interfaceRetrofit = retrofit.create(InterfaceRetrofit.class);

        findViewById(R.id.sqliteMain).setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, SQLiteActivity.class)));

        findViewById(R.id.mongoMain).setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, MongoActivity.class)));

        findViewById(R.id.mysqlphpMain).setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, MySQLPHPActivity.class)));

        findViewById(R.id.calendarMain).setOnClickListener(v ->
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CalendarFragment()).commit());

        wakeUpMongoServer();
    }
    // END OF ONCREATE

    // CONVERT BETWEEN TIMESTAMP OR HUMAN TIME ACROSS THE APP
    static String formatDate(String milliseconds, String pattern) {
        //@SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliseconds)*1000);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }

    // CLOSE FRAGMENTS ON BACK PRESS
    @Override
    public void onBackPressed() {
        List<Fragment> all_frags = getSupportFragmentManager().getFragments();
        if (all_frags.size() > 0) {
            for (Fragment frag : all_frags) {
                getSupportFragmentManager().beginTransaction().remove(frag).commit();

                // INITIALIZE TOOLBAR
                Toolbar toolbar = findViewById(R.id.toolbarMain);
                setSupportActionBar(toolbar);

                // SET UP THE TOOLBAR TO DISPLAY THE APP NAME
                toolbar.setTitle(getResources().getString(R.string.app_name));
            }
        } else {
            onExit();
        }
    }

    // EXIT APP ON BACK PRESS
    public void onExit() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> ActivityCompat.finishAffinity(MainActivity.this))
                .setNegativeButton("No", null)
                .show();
    }

    // THIS METHOD IS ONLY FOR HEROKU TO WAKE IT UP WHEN IDLE JUST CHECKS IF THE DATABASE HAS COLLECTIONS
    private void wakeUpMongoServer() {
        InterfaceRetrofit InterfaceRetrofit = MainActivity.retrofit.create(InterfaceRetrofit.class);
        Call<List<MongoVariables>> call = InterfaceRetrofit.checkExistsMongo();
        call.enqueue(new Callback<List<MongoVariables>>() {
            @Override
            public void onResponse(Call<List<MongoVariables>> call, Response<List<MongoVariables>> response) {
            }
            @Override
            public void onFailure(Call<List<MongoVariables>> call, Throwable t) {
            }
        });
    }
}
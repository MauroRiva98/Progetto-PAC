package com.project.pac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import static com.project.pac.R.*;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedpreferences = getSharedPreferences(String.valueOf(string.SHARED_PREFS), Context.MODE_PRIVATE);

        Log.i("MAIN ACTIVITY", sharedpreferences.getString(String.valueOf(R.string.SESSION_TOKEN), String.valueOf(Context.MODE_PRIVATE)));

        if(!sharedpreferences.contains(String.valueOf(R.string.SESSION_TOKEN)) ||
                sharedpreferences.getString(String.valueOf(R.string.SESSION_TOKEN), String.valueOf(Context.MODE_PRIVATE)).equals("0")){
            // NOT LOGGED
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            Intent mainContent = new Intent(this, ContentActivity.class);
            startActivity(mainContent);
        }

    }

}
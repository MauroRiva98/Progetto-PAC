package com.project.pac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void login(View view) {

        // 172.16.132.43:8080
        // p.badoglio@remazel.com
        // Badoglio

        final EditText email = findViewById(R.id.txt_email);
        final EditText password = findViewById(R.id.txt_pwd);
        final EditText ipAddress = findViewById(R.id.ip_address);

        Map<String, String> map = new HashMap<String, String>();
        map.put("email", email.getText().toString());
        map.put("password", password.getText().toString());

        String address = "http://" + ipAddress.getText().toString() + "/api/login";

        sharedpreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        CodaRichieste coda_custom = CodaRichieste.getInstance(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject o = null;
                        try {
                            o = new JSONObject(response);

                            if(o != null && o.length() == 1){

                                editor.putString(String.valueOf(R.string.session_emailUtente), String.valueOf(map.get("email")));
                                editor.putString(String.valueOf(R.string.SESSION_TOKEN), o.getString("token"));
                                editor.putString(String.valueOf(R.string.SESSION_IP), ipAddress.getText().toString());
                                if(editor.commit()){

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }

                            }else{
                                makeToast("LOGIN ERRATO");
                            }

                        } catch (JSONException e) {
                            makeToast("LOGIN ERRATO");
                            Log.e("ERRORE RISPOSTA SERVER", e.getMessage());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SERVER ERROR", error.getMessage());
                makeToast("SERVER ERROR: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getParams() {

                return map;
            }
        };

        coda_custom.addToRequestQueue(stringRequest);

    }

    Toast t;
    private void makeToast(String s) {
        if(t != null) t.cancel();
        Toast.makeText(this, s,Toast.LENGTH_LONG).show();
    }

}
package com.project.pac;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;

public class ApiRequest {
    Map parameter;
    String url;
    Context ctx;

    static final String user = "postgres";
    static final String password = "a";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    JSONObject risposta;
    boolean boolRisposta;
    String stringResp;
    String tmp;
    CodaRichieste coda_custom;

    public ApiRequest(Map parameter, String url, Context ctx) {
        this.parameter = parameter;
        this.url = url;
        this.ctx = ctx;

        sharedpreferences= ctx.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        coda_custom = CodaRichieste.getInstance(ctx);
    }

//    public void authStringReq(){
//        CodaRichieste queue = CodaRichieste.getInstance(this.ctx);
//
//        StringWriter sw = new StringWriter();
//        JsonWriter w = Json.createWriter(sw);
//
//        w.writeObject((JsonObject) this.parameter);
//        w.close();
//
//        StringRequest sr = new StringRequest(Request.Method.POST, this.url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                makeToast("RISPO: " + response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                makeToast("ERROR: ");
//            }
//        }) {
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//
//                return sw.getBuffer();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//        };
//
//        queue.addToRequestQueue(sr);
//
//    }

    public void authRequest() { // NOT USED
        Map<String, String> map = this.parameter;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("RISPOSTA", response);

                        JSONObject o = null;
                        try {
                            o = new JSONObject(response);
                            if(o.length() == 1){
                                Log.d("INSIDE ON RESPONSE", "PRE SCRITTURA");
                                editor.putString(String.valueOf(R.string.session_emailUtente), String.valueOf(map.get("email")));
                                editor.putString(String.valueOf(R.string.SESSION_TOKEN), o.getString("token"));

                                editor.commit();

                            }
                        } catch (JSONException e) {
                            Log.e("ERRORE RISPOSTA SERVER", e.getMessage());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeToast("error.: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getParams() {

                return map;
            }
        };

        coda_custom.addToRequestQueue(stringRequest);

    }

    public boolean provaReq(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://172.16.132.74:8080/prova",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        makeToast("Risposta prova: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeToast("error prova: " + error.getMessage());
            }
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);

        return true;

    }

    public boolean malattiaRequest(){

        StringRequest malattia_req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(!response.equals("ERROR")) boolRisposta = true;
                        else boolRisposta = false;

                        makeToast("Risposta prova: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                makeToast("ERRORE RISPOSTA");

            }
        }
        ){
            @Override
            public Map<String, String> getParams() {

                return parameter;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(malattia_req);

        Log.d("RISPOSTA MALATTIA", "BOOL " + boolRisposta);
        return boolRisposta;

    }

    public String turniReq(){
        Map<String, String> map = this.parameter;

        JSONObject result;

        StringRequest turniRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                            stringResp = response;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeToast("error.: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getParams() {

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(turniRequest);

        return stringResp;
    }

    public boolean logoutReq(){

        StringRequest logout = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("RISPOSTA JSON", response);

                        editor.putString(String.valueOf(R.string.RESULT_LOGOUT), response);
                        editor.apply();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeToast("error.: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getParams() {

                return parameter;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(logout);

        Log.d("SHARED PREF _ LOGOUT", sharedpreferences.getString(String.valueOf(R.string.RESULT_LOGOUT), String.valueOf(Context.MODE_PRIVATE)));

        if(sharedpreferences.contains(String.valueOf(R.string.RESULT_LOGOUT)))
            return true;
        else
            return false;


    }

    Toast t;

    private void makeToast(String s) {
        if(t != null) t.cancel();
        Toast.makeText(ctx, s,Toast.LENGTH_LONG).show();
    }



}


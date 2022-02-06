package com.project.pac;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ListaTurniFragment extends Fragment {
    SharedPreferences sharedpreferences;

    EditText data_inizio;
    EditText data_fine;
    Button showList;
    ListView listItem;
    final Calendar calendar1 = Calendar.getInstance();
    final Calendar calendar2 = Calendar.getInstance();

    ArrayList<Turno> listaTurni;
    TurnoAdapter adapter;

    public ListaTurniFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_turni, container, false);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        String token = sharedpreferences.getString(String.valueOf(R.string.SESSION_TOKEN), String.valueOf(Context.MODE_PRIVATE));
        Log.d("TOKEN", token);
        String BASE_IP = sharedpreferences.getString(String.valueOf(R.string.SESSION_IP), String.valueOf(Context.MODE_PRIVATE));
        Log.d("IP", BASE_IP);

        data_inizio = getActivity().findViewById(R.id.periodoInizio);
        data_fine = getActivity().findViewById(R.id.periodoFine);
        listItem = getActivity().findViewById(R.id.list_item);
        showList = getActivity().findViewById(R.id.show_turni);

        listaTurni = new ArrayList<Turno>();

        adapter = new TurnoAdapter(getActivity().getApplicationContext(), listaTurni);
        listItem.setAdapter(adapter);

        DatePickerDialog.OnDateSetListener periodoInizio = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                calendar1.set(year, month, day);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                String dateString = dateFormat.format(calendar1.getTime());
                data_inizio.setText(dateString);
            }
        };

        data_inizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),
                        periodoInizio,
                        calendar1.get(Calendar.YEAR),
                        calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener periodoFine = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar2.set(year, month, day);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                String dateString = dateFormat.format(calendar2.getTime());

                data_fine.setText(dateString);
            }
        };

        data_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),
                        periodoFine,
                        calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String start = data_inizio.getText().toString();
                String end = data_fine.getText().toString();

                // TODO: CONTROLLO DEL PERIODO MASSIMO UN MESE

                if(start != null && !start.isEmpty() && end != null && !end.isEmpty()){

                    listaTurni.clear();
                    listItem.setAdapter(adapter);

                    Map<String, String> params = new HashMap<String, String>();


                    params.put("token", token);
                    params.put("data_inizio", start);
                    params.put("data_fine", end);

                    String BASE_IP = sharedpreferences.getString(String.valueOf(R.string.SESSION_IP), String.valueOf(Context.MODE_PRIVATE));

                    CodaRichieste queue = CodaRichieste.getInstance(getActivity().getApplicationContext());

                    StringRequest turniRequest = new StringRequest(Request.Method.POST, "http://" + BASE_IP + "/api/richiestaTurni",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject o = new JSONObject(response);

                                        JSONArray list_turni = o.getJSONArray("turni");
                                        // Log.d("RISPOSTA JSON", list_turni.toString());

                                        for (int i = 0; i < list_turni.length(); i++) {

                                            o = list_turni.getJSONObject(i);

                                            Turno t = new Turno(o.getString("data"), o.getInt("ora_inizio"), o.getInt("ora_fine"), o.getString("indirizzo"), o.getString("ruolo"), o.getBoolean("trasferta"), o.getBoolean("straordinario"));

                                            listaTurni.add(t);
                                            listItem.setAdapter(adapter);

                                        }



                                    } catch (JSONException e) {
                                        makeToast("CONTROLLARE LE DATE, SE CORRETTE NON SONO PRESENTI TURNI");
                                        e.printStackTrace();
                                    }




                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            makeToast("ERRORE RISPOSTA: " + error.getMessage());
                        }
                    }) {
                        @Override
                        public Map<String, String> getParams() {

                            return params;
                        }

                    };

                    queue.addToRequestQueue(turniRequest);
                    // Log.d("RICHIESTA TURNI", "FINE CHIAMATA...");

                }else{
                    makeToast("SCEGLIERE DATA INIZIO E FINE PERIODO PER PROSEGUIRE");

                }
            }
        });
    }

    Toast t;
    private void makeToast(String s) {
        if(t != null) t.cancel();
        Toast.makeText(getContext(), s,Toast.LENGTH_LONG).show();
    }

}
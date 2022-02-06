package com.project.pac;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TurniFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TurniFragment extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    final Calendar myCalendar= Calendar.getInstance();
    SharedPreferences sharedpreferences;

    public TurniFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TurniFragment.
     */
    public static TurniFragment newInstance(String param1, String param2) {
        TurniFragment fragment = new TurniFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        EditText editText = getView().findViewById(R.id.txt_data);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                StringBuilder sb = new StringBuilder();

                sb.append(day);
                sb.append("/");
                sb.append(month);
                sb.append("/");
                sb.append(year);

                editText.setText(sb.toString());
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button btnLista = getView().findViewById(R.id.btn_showTurni);

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();

                if(data != ""){
                    Bundle bundle = new Bundle();
                    bundle.putString("data_scelta", data);

                    ListaTurniFragment fragmentLista = new ListaTurniFragment();
                    fragmentLista.setArguments(bundle);
                    // https://developer.android.com/guide/fragments/communicate
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, fragmentLista)
                            .commit();

                    // fragmentManager = getFragmentManager();
                    // fragmentTransaction = fragmentManager.beginTransaction();
                    // fragmentTransaction.replace(R.id.container_fragment, new ListaTurniFragment());
                    // fragmentTransaction.commit();

                }else{
                    Toast.makeText(getActivity(),
                            (CharSequence) "Selezionare una data prima di proseguire",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_turni, container, false);
        return view;
    }
}
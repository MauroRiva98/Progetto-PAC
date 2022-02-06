package com.project.pac;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class TurnoAdapter extends ArrayAdapter<Turno> {

    ArrayList<Turno> list;
    Context context;

    public TurnoAdapter(@NonNull Context context, ArrayList<Turno> turniList) {
        super(context, R.layout.simple_turno, turniList);
        this.context = context;
        this.list = turniList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Log.d("GETVIEW - SETADAPTER", String.valueOf(position));

        if(convertView == null) { // per ogni riga della lista

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.simple_turno, null);
        }

            TextView data_turno = convertView.findViewById(R.id.data_turno);
            data_turno.setText(list.get(position).data);

            TextView start_turno = convertView.findViewById(R.id.start_turno);
            start_turno.setText(String.valueOf(list.get(position).ora_inizio));

            TextView end_turno = convertView.findViewById(R.id.end_turno);
            end_turno.setText(String.valueOf(list.get(position).ora_fine));

            TextView addr = convertView.findViewById(R.id.address_turno);
            addr.setText(list.get(position).indirizzo);

            TextView straordinario = convertView.findViewById(R.id.isStraordinario);
            if(list.get(position).isStraordinario)
                straordinario.setText("STRAORDINARIO");
            else
                straordinario.setText("");

            TextView trasf = convertView.findViewById(R.id.isTrasferta);
            if(list.get(position).isTrasferta)
                trasf.setText("TRASFERTA");
            else
                trasf.setText("");

            TextView role = convertView.findViewById(R.id.ruolo_turno);
            role.setText(list.get(position).ruolo);



        return convertView;

    }

}

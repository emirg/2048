package com.riosgavagnin.emiliano.a2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Clase adapter que sirve para actualizar dinamicamente los elementos de la lista en la actividad RankingActivity
 */

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DatosJugador> array;

    public ListAdapter(Context c, ArrayList<DatosJugador> array) {
        mContext = c;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.usuario_item,null);
            // Si no esta inicializado, inicializarlo con algunos atributos
            // textView = new TextView(mContext);
            // textView.setLayoutParams(new ViewGroup.LayoutParams(160, 160));
            // textView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // textView.setPadding(32, 32, 32, 32);
        }

        TextView usuarioTextView = convertView.findViewById(R.id.nombre_usuario_ranking);
        TextView puntajeTextView = convertView.findViewById(R.id.puntaje_usuario_ranking);
        usuarioTextView.setText(String.valueOf(array.get(position).getNombreUsuario()));
        puntajeTextView.setText(String.valueOf(String.valueOf(array.get(position).getPuntaje())));

        //  textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //  textView.setGravity(Gravity.CENTER);

        return convertView;
    }
}

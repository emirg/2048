package com.riosgavagnin.emiliano.a2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Math.sqrt;



/* Clase adapter entre la grilla lógica y grilla visual
*
* Esta clase se encargara de tomar los datos (numeros) de la grilla
* lógica y crear elementos del tipo TextView y colocarlos en un GridView
* de forma tal que formen la grilla con la que finalmente interactuara
* el usuario
*
*/
public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> grid; // La grilla (matriz) será recibida en forma de una ArrayList (tal y como se especifica en la super clase BaseAdapter)
    private int tamañoCuadrado ; // Variable utilizada para cambiar el tamaño del cuadrado del TextView para que se adapte a la dificultad
    private final int[] tamañosFuente = {24,20,16,12}; // Arreglo con distintos tamaños de fuente, de esta forma se cambiara acorde al tamaño del cuadrado y la longitud del número


    public GridAdapter(Context c, ArrayList<Integer> grid) {
        mContext = c;
        this.grid = grid;
        tamañoCuadrado = grid != null ? (int) sqrt(grid.size()) : 0;
    }

    public int getCount() {
        return grid.size();
    }

    public Object getItem(int position) {
        return grid.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // Crea un nuevo TextView por cada item referenciado por el adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_square,null);
        }

        TextView textView = convertView.findViewById(R.id.text_square_tv);
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        switch(tamañoCuadrado){ // Según la dificultad elegida, se determinara el tamaño del cuadrado (3x3 implica tamaños de cuadrados mas grandes que 8x8)
            case 3:
                //textView.setHeight(120);
                //textView.setWidth(120);
                params.width = mContext.getResources().getDimensionPixelSize(R.dimen.alturaTv3x3);
                params.height = params.width;
                textView.setLayoutParams(params);
                break;
            case 4:
                //textView.setHeight(80);
                //textView.setWidth(80);
                params.width = mContext.getResources().getDimensionPixelSize(R.dimen.alturaTv4x4);
                params.height = params.width;
                textView.setLayoutParams(params);
                break;

            case 8:
                //textView.setHeight(40);
                //textView.setWidth(40);
                params.width = mContext.getResources().getDimensionPixelSize(R.dimen.alturaTv8x8);
                params.height = params.width;
                textView.setLayoutParams(params);
                break;
        }

        if(grid.get(position) == 0){ // Si es un cuadrado con un "0", colocamos un espacio vacio, no mostramos el "0"
            textView.setText(" ");
            textView.setTextSize(tamañosFuente[0]);
        }else{
            int dato = grid.get(position);
            String cadenaDato = String.valueOf(dato);
            textView.setText(cadenaDato);
            switch (cadenaDato.length()){ // Según la longitud del número, será el tamaño de la fuente a utilizar
                case 1:
                    if(tamañoCuadrado != 8) {
                        textView.setTextSize(tamañosFuente[0]);
                    }else{
                        textView.setTextSize((float) (tamañosFuente[0]/1.5));
                    }
                    break;
                case 2:
                    if(tamañoCuadrado != 8) {
                        textView.setTextSize(tamañosFuente[1]);
                    }else{
                        textView.setTextSize((float) (tamañosFuente[1]/1.5));
                    }
                    break;
                case 3:
                    if(tamañoCuadrado != 8) {
                        textView.setTextSize(tamañosFuente[2]);
                    }else{
                        textView.setTextSize((float) (tamañosFuente[2]/1.5));
                    }
                    break;
                case 4:
                    if(tamañoCuadrado != 8) {
                        textView.setTextSize(tamañosFuente[3]);
                    }else{
                        textView.setTextSize((float) (tamañosFuente[3]/1.5));
                    }
                    break;
            }
        }

        //  textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //  textView.setGravity(Gravity.CENTER);

        return textView;
    }

    
}
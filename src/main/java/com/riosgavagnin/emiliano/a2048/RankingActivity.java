package com.riosgavagnin.emiliano.a2048;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.riosgavagnin.emiliano.a2048.data.UsuariosContractSQL;
import com.riosgavagnin.emiliano.a2048.data.UsuariosDbHelper;

import java.util.ArrayList;

/*
 * Actvidad encargada de buscar y mostrar una lista con todos los jugadores registrados y sus records
 */

public class RankingActivity extends AppCompatActivity {

    private SQLiteDatabase usuariosBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);


        ///Creación BD////
        UsuariosDbHelper dbHelper = new UsuariosDbHelper(this);
        usuariosBD = dbHelper.getWritableDatabase();
        /////////////

         ArrayList<DatosJugador> rank = buscarRanking();
        //Log.v("Ranking", rank);

        // Creo un ListView para mostrar todos los usuarios con sus records
        ListView listView = findViewById(R.id.ranking_lv);

        // Seteo su adapter con la lista de los usuarios previamente buscada
        listView.setAdapter(new ListAdapter(this,rank));
    }

    // Metodo encargado de devolver un ArrayList con todos los jugadores y sus puntajes record
    private ArrayList<DatosJugador> buscarRanking(){

        String[] columnas = {"_ID",UsuariosContractSQL.UsuariosEntry.COLUMN_USER, UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD, UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD};
        Cursor result = usuariosBD.query(
                UsuariosContractSQL.UsuariosEntry.TABLE_NAME,
                columnas,
                null,
                null,
                null,
                null,
                UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD + " DESC"
        );


        if (result != null) {

            ArrayList<DatosJugador> ranking = new ArrayList<>();

            // Muevo el puntero a la primer fila
            result.moveToFirst();

            // Itero sobre las filas
            for (int i = 0; i < result.getCount(); i++) {
                DatosJugador datosJugador = new DatosJugador();
                String usuarioNombre = result.getString(result.getColumnIndex(UsuariosContractSQL.UsuariosEntry.COLUMN_USER));
                int puntaje = result.getInt(result.getColumnIndex(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD));
                datosJugador.setNombreUsuario(usuarioNombre);
                datosJugador.setPuntaje(puntaje);
                ranking.add(datosJugador);

                // Muevo a la siguiente fila
                result.moveToNext();
            }
            // Cierro el puntero
            result.close();
            return ranking;
        }else{
            return null;
        }
    }
}

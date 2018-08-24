package com.riosgavagnin.emiliano.a2048;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.riosgavagnin.emiliano.a2048.data.UsuariosContractSQL;
import com.riosgavagnin.emiliano.a2048.data.UsuariosDbHelper;

/* Clase inicial - Encargada del Log in/Registro
* Esta clase sera utilizada para permitir al usuario iniciar sesion o registrarse.
* Se instancia la BD existente en el dispositivo.
*/
public class LoginActivity extends AppCompatActivity {

    private SQLiteDatabase usuariosBD; // Instancia de la BD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ////

        ///Creación BD////
        UsuariosDbHelper dbHelper = new UsuariosDbHelper(this);
        usuariosBD = dbHelper.getWritableDatabase();
        /////////////

        // Botones y campos de texto
        Button login = findViewById(R.id.iniciar_sesion_boton);
        Button register = findViewById(R.id.registrar_boton);
        final EditText usuarioEditView = findViewById(R.id.usuario_ev);
        final EditText contraseñaEditView = findViewById(R.id.contraseña_ev);


       // Creo la acción correspondiente para el botón de login acorde a los campos de texto
       login.setOnClickListener(view -> {
                   String usuario = usuarioEditView.getText().toString();
                   String contraseña = contraseñaEditView.getText().toString();
                    if(!usuario.isEmpty() && !contraseña.isEmpty()){
                        Cursor query = buscarUsuario(usuario, contraseña,true);
                        if(query != null && query.getCount() == 1){
                            Context context = LoginActivity.this;

                            Class destinationActivity = MainActivity.class;

                            Intent startMain= new Intent(context,destinationActivity);

                            query.moveToFirst();
                            int record = query.getInt(query.getColumnIndex(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD));

                            startMain.putExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_USER, usuario);

                            startActivity(startMain);
                        }else{
                            Toast.makeText(this,R.string.errorLogin,Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this,R.string.errorCampoIncompleto,Toast.LENGTH_LONG).show();
                    }

               });

       // Creo la acción correspondiente para el botón de registro acorde a los campos de texto
       register.setOnClickListener(view -> {


                    String usuario = usuarioEditView.getText().toString();
                    String contraseña = contraseñaEditView.getText().toString();
                    if(!usuario.isEmpty() && !contraseña.isEmpty()) {
                        Cursor query = buscarUsuario(usuario, contraseña,false);
                        if (query != null && query.getCount() == 0) {
                            registrarUsuario(usuario, contraseña);
                            Context context = LoginActivity.this;

                            Class destinationActivity = MainActivity.class;

                            Intent startMain = new Intent(context, destinationActivity);

                            startMain.putExtra(UsuariosContractSQL.UsuariosEntry.COLUMN_USER, usuario);

                            startActivity(startMain);

                        } else {
                            Toast.makeText(this,R.string.errorRegis,Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this,R.string.errorCampoIncompleto,Toast.LENGTH_SHORT).show();
                    }

                });

    }


    // Método utilizado para buscar a un usuario en la BD
    /*
       El parametro "usarContraseña" es un booleano que establece si se debera buscar a un usuario
       según su nombre de usuario y contraseña o simplemnete por su nombre de usuario. El primer caso
       es utilizado cuando se busca hacer un login donde debemos comprobar si existe el usuario e ingreso
       la contraseña correcto. El segundo caso se utiliza a la hora de registrar a un usuario para asegurarse
       que no hay otro usuario con el mismo nombre de usuario
     */
    public Cursor buscarUsuario(String nombreUsuario, String contraseña, boolean usarContraseña){

        //String sentenciaSQLBuscarUsuario =
        //        "SELECT " + UsuariosContractSQL.UsuariosEntry.COLUMN_USER +
        //        " FROM " + UsuariosContractSQL.UsuariosEntry.TABLE_NAME +
        //        " WHERE " + UsuariosContractSQL.UsuariosEntry.COLUMN_USER + " = " + nombreUsuario + " AND " + UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD + " = " +contraseña
        //        ;
        //usuariosBD.execSQL(sentenciaSQLBuscarUsuario);

        String[] columnas = {"_ID",UsuariosContractSQL.UsuariosEntry.COLUMN_USER, UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD, UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD};
        Cursor cursor;
        if(usarContraseña){ // Si necesito buscarlo según nombre de usuario y contraseña... (Log In)
             cursor = usuariosBD.query(
                    UsuariosContractSQL.UsuariosEntry.TABLE_NAME,
                    columnas,
                    UsuariosContractSQL.UsuariosEntry.COLUMN_USER + " = '" + nombreUsuario + "' AND " + UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD + " = '" +contraseña+ "'",
                    null,
                    null,
                    null,
                    null
            );
        }else{ // Si necesito buscarlo según nombre de usuario... (Registro)
             cursor = usuariosBD.query(
                    UsuariosContractSQL.UsuariosEntry.TABLE_NAME,
                    columnas,
                    UsuariosContractSQL.UsuariosEntry.COLUMN_USER + " = '" + nombreUsuario + "'",
                    null,
                    null,
                    null,
                    null
            );
        }

        return cursor;

    }

    // Método utilizado una vez que comprobamos que el usuario se esta registrando con un nombre de usuario valido
    public void registrarUsuario(String nombreUsuario, String contraseña){

        //String sentenciaSQLRegistrar = "INSERT INTO " + UsuariosContractSQL.UsuariosEntry.TABLE_NAME + " VALUES (" + nombreUsuario + "," + contraseña + ", 0);";
        //usuariosBD.execSQL(sentenciaSQLRegistrar);

        ContentValues cv  = new ContentValues(); // Creamos un ContentValue para almacenar toda la información que insertaremos en la BD

        // Insertamos los datos en el ContentValues
        cv.put(UsuariosContractSQL.UsuariosEntry.COLUMN_USER, nombreUsuario);
        cv.put(UsuariosContractSQL.UsuariosEntry.COLUMN_PASSWORD, contraseña);
        cv.put(UsuariosContractSQL.UsuariosEntry.COLUMN_RECORD, 0);

        usuariosBD.insert( UsuariosContractSQL.UsuariosEntry.TABLE_NAME,null,cv); // Insertamos en la BD

    }
}

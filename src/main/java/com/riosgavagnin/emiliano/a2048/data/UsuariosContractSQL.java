package com.riosgavagnin.emiliano.a2048.data;

import android.provider.BaseColumns;

public class UsuariosContractSQL {
    //
    // ID | Usuario | Contraseña | Record
    //
    private UsuariosContractSQL(){}

    public static class UsuariosEntry implements BaseColumns {
        public static final String TABLE_NAME = "listaUsuarios";

        public static final String COLUMN_USER = "nombreUsuario";
        public static final String COLUMN_PASSWORD = "contraseñaUsuario";
        public static final String COLUMN_RECORD = "recordUsuario";

    }
}

package com.riosgavagnin.emiliano.a2048;

import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

/* Clase contenedora de métodos utiles para el manejo de matrices
* Se implementan métodos principalmente para el juego "2048"
* Revisar el código antes de utilizar en otro proyecto
*/

public class ArrayUtils {

    ///METODOS SOBRE MATRICES////

    // Método encargado de hacer el shiftLeft (desplazar fila hacia la izq.) de todas las filas de la grilla
    public static int shiftArrayLeft(int[][] array) {
        int puntajeShift = 0;
        int[][] copyArray = copiarArray(array);
        for (int i = 0; i < array.length; i++) { // Por cada fila...
            shiftRowLeft(array, i); //Realizo un shift
            puntajeShift = puntajeShift + combineRowLeft(array, i); //Combino los números que quedaron juntos y son iguales
            shiftRowLeft(array, i); //Vuelvo hacer shift a la izquierda. Esto es necesario para casos como 2-2-2-2 -> 4-4-0-0. Sin el ultimo shift la fila seria 2-2-2-2 -> 4-0-4-0
        }
        if (!equalsArrays(array, copyArray)) { // Si el shift hizo un cambio en la grilla, agrego un numero en una posición aleatoria
            addNumber(array);
        }

        return puntajeShift;
    }

    // Analoga a shiftArrayLeft pero hacia la derecha.
    public static int shiftArrayRight(int[][] array) {
        int puntajeShift = 0;
        int[][] copyArray = copiarArray(array);
        for (int i = 0; i < array.length; i++) {
            shiftRowRight(array, i);
            puntajeShift = puntajeShift + combineRowRight(array, i);
            shiftRowRight(array, i);


        }
        if (!equalsArrays(array, copyArray)) {
            addNumber(array);
        }
        return puntajeShift;

    }

    // Analoga a shiftArrayLeft pero hacia arriba.
    public static int shiftArrayUp(int[][] array) {
        int puntajeShift = 0;
        int[][] copyArray = copiarArray(array);
        for (int i = 0; i < array[0].length; i++) {
            shiftColUp(array, i);
            puntajeShift = puntajeShift + combineColUp(array, i);
            shiftColUp(array, i);

        }
        if (!equalsArrays(array, copyArray)) {
            addNumber(array);
        }
        return puntajeShift;
    }

    // Analoga a shiftArrayLeft pero hacia abajo.
    public static int shiftArrayDown(int[][] array) {
        int puntajeShift = 0;
        int[][] copyArray = copiarArray(array);
        for (int i = 0; i < array.length; i++) {
            shiftColDown(array, i);
            puntajeShift = puntajeShift + combineColDown(array, i);
            shiftColDown(array, i);

        }
        if (!equalsArrays(array, copyArray)) {
            addNumber(array);
        }
        return puntajeShift;
    }


    ///METODOS SOBRE FILAS Y COLUMNAS///

    // Método encargado de hacer el desplazamiento hacia la izquierda  una fila dada
    private static void shiftRowLeft(int[][] grid, int fila) {
        int[] row = grid[fila]; // Creo una copia de la fila
        int i = 0, j = 0;
        while (j < grid.length) {
            if (i < row.length) {
                if (row[i] != 0) { // Si el elemento en la fila es distinto a 0, lo copio en la posición indicada del siguiente numero a insertar
                    grid[fila][j] = row[i];
                    i++;
                    j++;
                } else { // Si es 0 avanzó
                    i++;
                }
            } else { // Si ya termine de recorrer, completo la fila con 0
                grid[fila][j] = 0;
                j++;
            }
        }
    }

    // Método encargado de hacer el desplazamiento hacia la derecha de una fila dada
    // Analogo al metodo shiftRowLeft, solamente se cambia la dirección en la que se avanza en la fila
    private static void shiftRowRight(int[][] grid, int fila) {
        int[] row = grid[fila];
        int i = row.length - 1, j = grid.length - 1;
        while (j >= 0) {
            if (i >= 0) {
                if (row[i] != 0) {
                    grid[fila][j] = row[i];
                    i--;
                    j--;
                } else {
                    i--;
                }
            } else {
                grid[fila][j] = 0;
                j--;
            }
        }
    }

    // Método encargado de hacer el desplazamiento hacia arriba de una columna dada
    // Analogo al metodo shiftRowLeft, solamente se cambia la dirección en la que se avanza en la columna
    private static void shiftColUp(int[][] grid, int col) {
        int[] c = new int[grid[0].length];
        for (int i = 0; i < grid[col].length; i++) { // Trabajo la columna como una fila
            c[i] = grid[i][col];
        }

        // El resto de la operación es similar a shiftRowLeft
        int i = 0, j = 0;
        while (j < grid[col].length) {
            if (i < c.length) {
                if (c[i] != 0) {
                    grid[j][col] = c[i];
                    i++;
                    j++;
                } else {
                    i++;
                }
            } else {
                grid[j][col] = 0;
                j++;
            }
        }
    }

    // Método encargado de hacer el desplazamiento hacia abajo de una columna dada
    // Analogo al metodo shiftColUp, solamente se cambia la dirección en la que se avanza en la columna
    private static void shiftColDown(int[][] grid, int col) {
        int[] c = new int[grid[col].length];
        for (int i = 0; i < grid[col].length; i++) {
            c[i] = grid[i][col];
        }

        int i = c.length - 1, j = grid[col].length - 1;
        while (j >= 0) {
            if (i >= 0) {
                if (c[i] != 0) {
                    grid[j][col] = c[i];
                    i--;
                    j--;
                } else {
                    i--;
                }
            } else {
                grid[j][col] = 0;
                j--;
            }
        }

    }

    // Método encargado de combinar aquellos numeros de una fila que son iguales (hacia la izquierda)
    private static int combineRowLeft(int[][] grid, int fila) {
        int[] r = grid[fila];
        int puntajeFila = 0;
        for (int i = 1; i < r.length; i++) {
            int a = r[i];
            int b = r[i - 1];

            if (a == b) { // Si un numero i y el i-1 son iguales, los juntamos en i-1 y ponemos 0 en i
                r[i - 1] = a + b;
                r[i] = 0;
                puntajeFila = puntajeFila + a + b;

                //break;
            }
        }

        return puntajeFila;

    }

    // Analogo a combineRowLeft pero hacia la derecha
    private static int combineRowRight(int[][] grid, int fila) {
        int[] r = grid[fila];
        int puntajeFila = 0;
        for (int i = r.length - 1; i > 0; i--) {
            int a = r[i];
            int b = r[i - 1];

            if (a == b) {
                r[i] = a + b;
                r[i - 1] = 0;
                puntajeFila = puntajeFila + a + b;
                //break;
            }
        }

        return puntajeFila;

    }

    // Analogo a combineRowLeft pero hacia arriba
    private static int combineColUp(int[][] grid, int col) {
        int[] r = new int[grid[0].length];
        int puntajeCol = 0;
        for (int i = 0; i < r.length; i++) {
            r[i] = grid[i][col];
        }
        for (int i = 1; i < r.length; i++) {
            int a = r[i];
            int b = r[i - 1];

            if (a == b) {
                r[i - 1] = a + b;
                r[i] = 0;
                puntajeCol = puntajeCol + a + b;
            }
        }
        for (int i = 0; i < r.length; i++) {
            grid[i][col] = r[i];
        }

        return puntajeCol;

    }

    // Analogo a combineRowLeft pero hacia la abajo
    private static int combineColDown(int[][] grid, int col) {
        int[] r = new int[grid[0].length];
        int puntajeCol = 0;
        for (int i = 0; i < r.length; i++) {
            r[i] = grid[i][col];
        }
        for (int i = r.length - 1; i > 0; i--) {
            int a = r[i];
            int b = r[i - 1];

            if (a == b) {
                r[i] = a + b;
                r[i - 1] = 0;
                puntajeCol = puntajeCol + a + b;

            }
        }
        for (int i = 0; i < r.length; i++) {
            grid[i][col] = r[i];
        }

        return puntajeCol;

    }


    ///METODOS EXTRAS///

    // Método utilizado para transformar una matriz en una lista (utilizado por GridAdapter)
    public static ArrayList<Integer> arrayToList(int[][] grid) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                arrayList.add(grid[i][j]);
            }
        }
        return arrayList;
    }

    // Agrega dos números (2 ó 4) en posiciones aleatorias
    public static void inicializarArray(int[][] array) {
        addNumber(array);
        addNumber(array);
    }

    // Compara si dos matrices son iguales (utilizado para saber si hay que agregar numeros una vez hecho un swipe)
    public static boolean equalsArrays(int[][] a, int[][] b) {
        boolean iguales = true;
        if (a.length == b.length && a[0].length == b[0].length) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    int n = a[i][j], m = b[i][j];
                    if (n != m) {
                        iguales = false;
                        break;
                    }
                }
            }
        } else {
            iguales = false;
        }
        return iguales;
    }

    // Metodo utilizado para copiar una matriz
    public static int[][] copiarArray(int[][] a) {
        int[][] b = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                int n = a[i][j];
                b[i][j] = n;
            }
        }

        return b;
    }

    // Agrega un número (2 ó 4) en una posición aleatoria de la grilla
    private static void addNumber(int[][] grid) {

        Random r = new Random();
        float random = r.nextFloat();
        int randomX = r.nextInt(grid.length);
        int randomY = r.nextInt(grid.length);
        int numero = (random > 0.5) ? 2 : 4;
        if (grid[randomX][randomY] == 0) {
            grid[randomX][randomY] = numero;
        } else {
            boolean insertado = false;

            if (calcularEspaciosVacios(grid) != 0) {
                while (!insertado) {
                    if (grid[randomY][randomX] == 0) {
                        grid[randomY][randomX] = numero;
                        insertado = true;
                    } else {

                        randomX = r.nextInt(grid.length);
                        randomY = r.nextInt(grid.length);
                    }
                }
            }
        }


    }

    // Cuenta la cantidad de espacios vacios (ceros) de una grilla
    private static int calcularEspaciosVacios(int[][] grid){
        int espaciosVacios = 0;
        if(grid != null){
            for (int k = 0; k < grid.length; k++) {
                for (int l = 0; l < grid[0].length; l++) {
                    if(grid[k][l] == 0) espaciosVacios++;
                }
            }
        }
        return espaciosVacios;
    }

    // Comprueba si hay posibilidades de continuar el juego (si hay ceros o si no hay 2048)
    public static boolean juegoTermino(int[][] grid) {

        boolean termino = false;
        boolean hayCeros = false;
        boolean hay2048 = false;
        boolean vecinoIgual = false;
        int i=0,j=0,numeroActual;

        while(!hay2048 && !hayCeros && !vecinoIgual && i<grid.length && j<grid[0].length) {
            numeroActual = grid[i][j];
            hay2048 = numeroActual == 2048;
            hayCeros = numeroActual == 0;

            if(i+1 < grid.length){ //Derecha
                vecinoIgual = numeroActual == grid[i+1][j];
            }
            if(j+1 < grid[0].length && !vecinoIgual){ //Abajo
                vecinoIgual = numeroActual == grid[i][j+1];
            }
            if(i-1 > -1 && !vecinoIgual){ //Izquierda
                vecinoIgual = numeroActual == grid[i-1][j];
            }
            if(j-1 > -1 && !vecinoIgual){ //Arriba
                vecinoIgual = numeroActual == grid[i][j-1];
            }

            if(hay2048){
                termino = true;
            }else{
                if (hayCeros || vecinoIgual){
                    termino = false;
                }

                if(!hayCeros && !vecinoIgual){
                    termino = true;
                }
            }

            if(j+1 == grid[0].length){
                j = 0;
                i++;
            }else{
                j++;
            }


        }

        return termino;
    }

    // En desarrollo
    // Habria que conseguirle un mejor lugar a este método
    @Deprecated
    public static void actualizarFuente(int puntaje, TextView textView){

        int[] tamañosFuente = {18,14,12,10};
        String cadenaDato = String.valueOf(puntaje);
        textView.setText(cadenaDato);
        switch (cadenaDato.length()){
            case 1:
                textView.setTextSize(tamañosFuente[0]);
                break;
            case 2:
                textView.setTextSize(tamañosFuente[0]);
                break;
            case 3:
                textView.setTextSize(tamañosFuente[0]);
                break;
            case 4:
                textView.setTextSize(tamañosFuente[1]);
                break;
        }

    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    static BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        System.out.println("===== ÁRBOL B+ =====");

        // El orden es la cantidad máxima de hijos por nodo; se pide mayor o igual a 3
        int orden = leerEntero("Ingrese el orden del árbol (mínimo 3): ");
        while (orden < 3) {
            System.out.println("El orden debe ser al menos 3.");
            orden = leerEntero("Ingrese el orden del árbol (mínimo 3): ");
        }
        ArbolBMas arbol = new ArbolBMas(orden);

        menu(arbol);
    }

    // Menú principal: se repite hasta que el usuario elige salir
    public static void menu(ArbolBMas arbol) throws IOException {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Insertar una clave y su dato");
            System.out.println("2. Buscar una clave");
            System.out.println("3. Eliminar una clave");
            System.out.println("4. Recorrer un rango (clave inicial y cantidad)");
            System.out.println("5. Mostrar el árbol");
            System.out.println("6. Cargar datos de ejemplo");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1:
                    insertar(arbol);
                    break;
                case 2:
                    buscar(arbol);
                    break;
                case 3:
                    eliminar(arbol);
                    break;
                case 4:
                    recorrerRango(arbol);
                    break;
                case 5:
                    System.out.println();
                    arbol.imprimirArbol();
                    break;
                case 6:
                    cargarEjemplo(arbol);
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // Opción 1: pide clave y dato; no permite claves repetidas
    public static void insertar(ArbolBMas arbol) throws IOException {
        int clave = leerEntero("Clave (número entero): ");
        if (arbol.buscar(clave)) {
            System.out.println("La clave " + clave + " ya existe en el árbol.");
            return;
        }
        System.out.print("Dato asociado: ");
        String dato = lector.readLine().trim();
        arbol.insertar(clave, dato);
        System.out.println("Clave " + clave + " insertada.");
    }

    // Opción 2: indica si la clave existe
    public static void buscar(ArbolBMas arbol) throws IOException {
        int clave = leerEntero("Clave a buscar: ");
        if (arbol.buscar(clave)) {
            System.out.println("La clave " + clave + " existe en el árbol.");
        } else {
            System.out.println("La clave " + clave + " no existe en el árbol.");
        }
    }

    // Opción 3: elimina la clave si existe
    public static void eliminar(ArbolBMas arbol) throws IOException {
        int clave = leerEntero("Clave a eliminar: ");
        if (arbol.eliminar(clave)) {
            System.out.println("Clave " + clave + " eliminada.");
        } else {
            System.out.println("La clave " + clave + " no existe en el árbol.");
        }
    }

    // Opción 4: imprime los siguientes n elementos a partir de una clave
    public static void recorrerRango(ArbolBMas arbol) throws IOException {
        int clave = leerEntero("Clave inicial: ");
        int cantidad = leerEntero("Cantidad de elementos a mostrar: ");
        if (cantidad < 1) {
            System.out.println("La cantidad debe ser al menos 1.");
            return;
        }
        System.out.println();
        int impresos = arbol.recorrerRango(clave, cantidad);
        if (impresos == 0) {
            System.out.println("No hay elementos desde esa clave.");
        }
    }

    // Opción 6: carga claves de ejemplo para probar el árbol rápidamente
    public static void cargarEjemplo(ArbolBMas arbol) {
        int[] claves = {10, 20, 5, 6, 15, 30, 25, 40, 45, 50};
        int nuevas = 0;
        for (int clave : claves) {
            if (!arbol.buscar(clave)) {
                arbol.insertar(clave, "dato" + clave);
                nuevas++;
            }
        }
        System.out.println("Se cargaron " + nuevas + " claves de ejemplo.");
    }

    // Lee un número y vuelve a preguntar si el usuario escribe algo que no lo es
    public static int leerEntero(String mensaje) throws IOException {
        int numero = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            try {
                numero = Integer.parseInt(lector.readLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
        return numero;
    }
}
import java.util.ArrayList;
import java.util.List;

public class NodoArbolBMas {

    // Atributos
    private boolean esHoja;
    private List<Integer> claves;
    private List<NodoArbolBMas> hijos;      // Vacía si es una hoja
    private List<String> datos;             // Solo en hojas: datos.get(i) es el dato de claves.get(i). null en nodos internos.
    private NodoArbolBMas siguiente;        // Solo en hojas: la hoja que sigue. null en nodos internos

    // Métodos
    // Constructor (Debe modificarse para que el nodo sea capaz de almacenar los datos.)
    public NodoArbolBMas(boolean esHoja) {
        this.esHoja = esHoja;
        this.claves = new ArrayList<>();
        this.hijos = new ArrayList<>();

        // solo las hojas guardan datos. En un nodo interno = quedan null
        if (esHoja){
            this.datos = new ArrayList<>();
        }
    }

    // Getters
    public boolean esHoja() {
        return esHoja;
    }

    public List<Integer> getClaves() {
        return claves;
    }

    public List<NodoArbolBMas> getHijos() {
        return hijos;
    }

    public List<String> getDatos() {
        return datos;
    }

    public NodoArbolBMas getSiguiente() {
        return siguiente;
    }

    // Setters
    public void setClaves(List<Integer> claves) {
        this.claves = claves;
    }

    public void setHijos(List<NodoArbolBMas> hijos) {
        this.hijos = hijos;
    }

    public void setDatos(List<String> datos) {
        this.datos = datos;
    }

    public void setSiguiente(NodoArbolBMas siguiente) {
        this.siguiente = siguiente;
    }
}

import java.util.ArrayList;

public class ArbolBMas {

    // Atributos
    private final int orden;
    private NodoArbolBMas raiz;

    // Métodos
    // Constructor
    public ArbolBMas(int orden) {
        this.orden = orden;
        raiz = new NodoArbolBMas(true);
    }

    // Operaciones públicas

    // Inserción de una llave en el árbol B+ (junto con su dato)
    public void insertar(int clave, String dato) {
        // Se inserta primero de forma recursiva
        insertarRecursivo(this.raiz, clave, dato);

        // Se verifica si la raíz se desbordó
        if(this.raiz.getClaves().size() == orden) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(this.raiz);
            dividirHijo(nuevaRaiz, 0);
            this.raiz = nuevaRaiz;
        }
    }

    // Búsqueda de un nodo como parte de la interfaz pública del árbol
    // (la clave solo existe si está en la hoja que le corresponde)
    public boolean buscar(int clave) {
        return encontrarHoja(raiz, clave).getClaves().contains(clave);
    }

    // Eliminación
    // Elimina una clave. Devuelve false si la clave no existe.
    // Si el borrado deja algún nodo con muy pocas claves, el árbol se rebalancea solo.
    public boolean eliminar(int clave) {
        if (!buscar(clave)) {
            return false;
        }
        eliminarRecursivo(raiz, clave);

        // Si la raíz interna se quedó sin claves, su único hijo pasa a ser la nueva raíz
        if (!raiz.esHoja() && raiz.getClaves().isEmpty()) {
            raiz = raiz.getHijos().get(0);
        }
        return true;
    }

    // Recorrido por rango (Debe mostrar los datos correspondientes al rango recuperado.)
    // Busca la clave e imprime los siguientes n elementos con sus datos.
    // Devuelve la cantidad de elementos impresos.
    public int recorrerRango(int clave, int n) {
        // Se baja hasta la hoja donde está (o estaría) la clave
        NodoArbolBMas hoja = encontrarHoja(raiz, clave);

        // Se ubica la primera clave de la hoja que sea mayor o igual a la buscada
        int pos = 0;
        while (pos < hoja.getClaves().size() && hoja.getClaves().get(pos) < clave) {
            pos++;
        }

        // Se imprime hoja por hoja, saltando con siguiente, hasta completar n elementos
        int impresos = 0;
        while (hoja != null && impresos < n) {
            while (pos < hoja.getClaves().size() && impresos < n) {
                System.out.println(hoja.getClaves().get(pos) + " -> " + hoja.getDatos().get(pos));
                impresos++;
                pos++;
            }
            hoja = hoja.getSiguiente();
            pos = 0; // en la hoja nueva se empieza desde su primera clave
        }
        return impresos;
    }

    // Impresión del árbol como parte de la interfaz pública del árbol
    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    // Métodos auxiliares privados

    // Inserción recursiva de una llave
    // Permite que los nodos se desborden temporalmente y los divide al regresar de la recursión
    private void insertarRecursivo(NodoArbolBMas nodo, int llave, String dato) {
        int i = nodo.getClaves().size() - 1;

        if(nodo.esHoja()) {
            while(i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }
            nodo.getClaves().add(i + 1, llave);
            nodo.getDatos().add(i + 1, dato);
        } else {
            while(i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }
            i++;
            NodoArbolBMas hijo = nodo.getHijos().get(i);

            // Llamada recursiva para insertar la llave primero en el hijo correspondiente
            insertarRecursivo(hijo, llave, dato);

            // Evaluación de desbordamiento al retornar de la recursividad
            if(hijo.getClaves().size() == orden) {
                dividirHijo(nodo, i);
            }
        }
    }

    // Dividir un nodo que está lleno
    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas nodoLleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(nodoLleno.esHoja());
        int mitad = orden / 2;

        if (nodoLleno.esHoja()) {
            // En nodos hoja, se mantiene la llave media en ambos lados
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            // Los datos se reparten igual que las claves
            nuevoNodo.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(mitad, nodoLleno.getDatos().size())));
            nodoLleno.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(0, mitad)));

            // Se enlazan las hojas: primero la nueva toma el siguiente de la vieja, luego la vieja apunta a la nueva
            nuevoNodo.setSiguiente(nodoLleno.getSiguiente());
            nodoLleno.setSiguiente(nuevoNodo);

            // El padre recibe como separador la primera llave del nuevo nodo
            int llaveMedia = nuevoNodo.getClaves().getFirst();
            padre.getClaves().add(indice, llaveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
        else {
            // En nodos internos, la llave media sube y se elimina del hijo
            int claveMedia = nodoLleno.getClaves().get(mitad);
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad + 1, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            // Mover los hijos del nodo dividido
            nuevoNodo.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(mitad + 1, nodoLleno.getHijos().size())));
            nodoLleno.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(0, mitad + 1)));

            padre.getClaves().add(indice, claveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
    }

    // Baja desde un nodo hasta la hoja donde está (o estaría) la clave indicada
    private NodoArbolBMas encontrarHoja(NodoArbolBMas nodo, int clave) {
        // Caso base: si ya es una hoja, se devuelve
        if (nodo.esHoja()) {
            return nodo;
        }

        // Se avanza mientras la clave buscada sea mayor o igual que el separador.
        // Se usa >= porque un separador es la primera clave de la hoja derecha.
        int i = 0;
        while (i < nodo.getClaves().size() && clave >= nodo.getClaves().get(i)) {
            i++;
        }

        // Se baja al hijo que corresponde
        return encontrarHoja(nodo.getHijos().get(i), clave);
    }

    // Cantidad mínima de claves que debe tener un nodo (la raíz queda exenta)
    private int minimoClaves(NodoArbolBMas nodo) {
        if (nodo.esHoja()) {
            return orden / 2;
        }
        return (orden - 1) / 2;
    }

    // Eliminación recursiva: se borra en la hoja y se corrige el desbalance al regresar de la recursión
    private void eliminarRecursivo(NodoArbolBMas nodo, int clave) {
        if (nodo.esHoja()) {
            // La clave y su dato están en la misma posición
            int posicion = nodo.getClaves().indexOf(clave);
            nodo.getClaves().remove(posicion);
            nodo.getDatos().remove(posicion);
            return;
        }

        // Se busca el hijo por el que hay que bajar (mismo criterio que encontrarHoja)
        int i = 0;
        while (i < nodo.getClaves().size() && clave >= nodo.getClaves().get(i)) {
            i++;
        }
        NodoArbolBMas hijo = nodo.getHijos().get(i);

        eliminarRecursivo(hijo, clave);

        // Al regresar, se revisa si el hijo quedó con muy pocas claves
        if (hijo.getClaves().size() < minimoClaves(hijo)) {
            corregirDesbalance(nodo, i);
        }
    }

    // Arregla un hijo con pocas claves: primero intenta pedir prestado a un hermano y, si no se puede, fusiona
    private void corregirDesbalance(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas izquierdo = null;
        NodoArbolBMas derecho = null;
        if (i > 0) {
            izquierdo = padre.getHijos().get(i - 1);
        }
        if (i < padre.getHijos().size() - 1) {
            derecho = padre.getHijos().get(i + 1);
        }
        int minimo = minimoClaves(hijo);

        if (izquierdo != null && izquierdo.getClaves().size() > minimo) {
            prestarDeIzquierda(padre, i);
        } else if (derecho != null && derecho.getClaves().size() > minimo) {
            prestarDeDerecha(padre, i);
        } else if (izquierdo != null) {
            fusionar(padre, i - 1); // el hijo se une con su hermano izquierdo
        } else {
            fusionar(padre, i);     // no hay izquierdo: el hijo se une con su hermano derecho
        }
    }

    // El hijo recibe la última clave de su hermano izquierdo
    private void prestarDeIzquierda(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas izquierdo = padre.getHijos().get(i - 1);

        if (hijo.esHoja()) {
            // En hojas se mueve la clave con su dato y el separador pasa a ser la primera clave del hijo
            hijo.getClaves().add(0, izquierdo.getClaves().removeLast());
            hijo.getDatos().add(0, izquierdo.getDatos().removeLast());
            padre.getClaves().set(i - 1, hijo.getClaves().getFirst());
        } else {
            // En nodos internos, el separador baja al hijo y la última clave del hermano sube al padre
            hijo.getClaves().add(0, padre.getClaves().get(i - 1));
            padre.getClaves().set(i - 1, izquierdo.getClaves().removeLast());
            hijo.getHijos().add(0, izquierdo.getHijos().removeLast());
        }
    }

    // El hijo recibe la primera clave de su hermano derecho
    private void prestarDeDerecha(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas derecho = padre.getHijos().get(i + 1);

        if (hijo.esHoja()) {
            // En hojas se mueve la clave con su dato y el separador pasa a ser la nueva primera clave del hermano
            hijo.getClaves().add(derecho.getClaves().removeFirst());
            hijo.getDatos().add(derecho.getDatos().removeFirst());
            padre.getClaves().set(i, derecho.getClaves().getFirst());
        } else {
            // En nodos internos, el separador baja al hijo y la primera clave del hermano sube al padre
            hijo.getClaves().add(padre.getClaves().get(i));
            padre.getClaves().set(i, derecho.getClaves().removeFirst());
            hijo.getHijos().add(derecho.getHijos().removeFirst());
        }
    }

    // Une el hijo i con el hijo i + 1 en un solo nodo y elimina el separador del padre
    private void fusionar(NodoArbolBMas padre, int i) {
        NodoArbolBMas izquierdo = padre.getHijos().get(i);
        NodoArbolBMas derecho = padre.getHijos().get(i + 1);

        if (izquierdo.esHoja()) {
            // En hojas se juntan claves y datos, y se salta la hoja derecha en la cadena de enlaces
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getDatos().addAll(derecho.getDatos());
            izquierdo.setSiguiente(derecho.getSiguiente());
        } else {
            // En nodos internos, el separador del padre baja y se juntan claves e hijos
            izquierdo.getClaves().add(padre.getClaves().get(i));
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getHijos().addAll(derecho.getHijos());
        }

        // El padre pierde el separador y la referencia al hijo derecho
        padre.getClaves().remove(i);
        padre.getHijos().remove(i + 1);
    }

    // Impresión recursiva privada de los nodos a partir de uno inicial
    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        System.out.println(indentacion + "+- " + (nodo.esHoja() ? "Hoja > " : "Interno > ") + nodo.getClaves());
        indentacion += esUltimo ? "   " : "|  ";
        for(int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), indentacion, i == (nodo.getHijos().size() - 1));
        }
    }
}
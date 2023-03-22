package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            this.color = Color.NINGUNO;
            this.elemento = elemento;
            this.padre = this.izquierdo = this.derecho = null;

        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            // Aquí va su código.
            String r = "";
            String ching = "";

            if (color == Color.ROJO)
                ching = "R";
            else
                ching = "N";

            r = ching + "{" + elemento.toString() + "}";

            return r;

        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
                return this.color == vertice.color && super(vertice);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        VerticeRojinegro r = (VerticeRojinegro) vertice;
        return vertice.color;

    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeRojinegro temp = (VerticeRojinegro) getUltimoVerticeAgregado();
        temp.color = Color.ROJO;

        rebalancea(temp);

    }

    private void rebalancea(VerticeRojinegro r) {
        /** Este metódo se invoca inmediatamente después de agregar un vertice para rebalancear el arbol Roji-negro
         * 
         * Caso 0: Solo sucede si r es null o r no es rojo
         *      Si esto sucede, solamente salimos 
         * 
         * Caso 1: El padre de r es null
         *      Si esto sucede, lo único que hacemos es cambiar el color de r a NEGRO (No importa que color sea antes).
         * 
         * Caso 2: El padre de r es NEGRO:
         *      Si esto sucede, unicamente terminamos el proceso.
         *  
         * Caso 3: El tío de r es ROJO (El hermano de su padre o el otro hijo de su abuelo)
         *      Si esto sucede, pintamos de negro al padre y al tío. Al abuelo lo pintamos de rojo y hacemos recursión 
         *      sobre el abuelo.
         * 
         * Caso 4: r y su padre estan cruzados
         *          Def: Un vertice esta cruzado con su padre si la direccion del padre no es igual a la direccion del hijo
         *          por ejemplo, si el padre es hijo izquierdo del abuelo, y r es hijo derecho del padre, entonces r y su padre
         *          estan cruzados
         * 
         *      Si esto sucede, entonces tenemos que 
         * 
         * Caso 5: Esto es inmediatamente después del caso 4, si el caso 4 sucede o no no influye con que este caso suceda
         *      Si esto sucede, solamente hay que pintar al padre de negro, al abuelo de rojo, y giramos a la direccion opuesta
         *      de la dirección del padre sobre el abuelo. Por ejemplo, si el padre es hijo derecho del abuelo, entonces pintamos
         *      al abuelo de rojo y al padre de negro, y giramos sobre el abuelo hacia la izquierda. Tambien funciona si es la 
         *      dirección opuesta a r 
         * 
         * */

         VerticeRojinegro aux = r;

        if (r == null || r.color != Color.ROJO) //Caso 0
            return;

        else if (r.padre == null) { //Caso 1
            r.color = Color.NEGRO;
            return;
        }

        else if (r.padre.color == Color.NEGRO) //Caso 2
            return;

        else if (tioRojo(r)) {
            r.padre.color = Color.negro;

            if (left(r.padre))
                r.padre.padre.derecho.color = Color.NEGRO;
            else
                r.padre.padre.derecho.color = Color.ROJO;

            r.padre.padre.color = Color.ROJO;

            return rebalancea(r.padre.padre);
        }

        else if (left(r) ^ left(r.padre)) { //Caso 4
            if (left(r)) {
                giraDerecha(r.padre);

            }

            else {
                giraIzquierda(r.padre);
            }
        }

        aux.padre.color = Color.NEGRO;
        aux.padre.padre.color = Color.ROJO;

        if (left(aux))
            giraDerecha(aux.padre.padre);

        else
            giraIzquierda(aux.padre.padre);

    }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

    private void cambiaColor(VerticeRojinegro r) {
        if (r == null)
            return;

        if (r.color == Color.ROJO) {
            r.color = NEGRO;
            return;
        }

        r.color = Color.ROJO;
    }

    private boolean left(VerticeRojinegro v) {
        if (v == null || v.padre == null)
            return false;

        if (v.padre.izquierdo == v)
            return true;

        return false;
    }

    private boolean tioRojo(VerticeRojinegro v) {
        if (v == null || v.padre == null || v.padre.padre == null)
            return false;

        if (left(padre)) {
            if (v.padre.padre.derecho == null || v.padre.padre.derecho.color != Color.ROJO)
                return false;

            return true;
        }

        if (!left(padre)) {
            if (v.padre.padre.izquierdo == null || v.padre.padre.izquierdo.color != Color.ROJO)
                return false;

            return true;
        }

    }

}

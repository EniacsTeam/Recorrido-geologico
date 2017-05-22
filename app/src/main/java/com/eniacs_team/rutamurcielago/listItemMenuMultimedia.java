package com.eniacs_team.rutamurcielago;

/**
 * Clase que contiene la información de cada cardView del RecyclerView.
 */

public class listItemMenuMultimedia {
    private String titulo; //Titulo del menú.
    private String id; // Id del punto seleccionado.


    /**
     * Constructor de la lista de items en el menú.
     * @param titulo
     * @param id
     */
    public listItemMenuMultimedia(String titulo, String id) {
        this.titulo = titulo;
        this.id = id;
    }
    /**
     * getter del titulo
     * @return
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * getter del Id
     * @return
     */

    public String getId() {
        return id;
    }
}

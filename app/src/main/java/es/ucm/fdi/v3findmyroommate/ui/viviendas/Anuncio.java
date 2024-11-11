package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.net.Uri;

public class Anuncio {
    private String todoCompleto;

    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;
    private String descripcion;
    private Uri imagenUri;

    public Anuncio( Intent data) {


        this.titulo = data.getStringExtra("titulo");
        this.ubicacion = data.getStringExtra("ubicacion");
        this.metros = data.getStringExtra("metros");
        this.precio = data.getStringExtra("precio");
        this.descripcion = data.getStringExtra("descripcion");

        this.imagenUri = data.getParcelableExtra("imagenUri");
        this.todoCompleto =  "Ubicación: " + ubicacion +
                "\nMetros cuadrados: " + metros + "\nPrecio: " + precio;

        if(!this.descripcion.isEmpty()){
            this.todoCompleto += "\nDescripción: \n " + descripcion;
        }

    }


    public Uri getImagenUri() {
        return imagenUri;
    }


    public String getDetalle() {
        return todoCompleto;
    }


    public String getTitulo() {
        return titulo;
    }
    public String getUbicacion() {
        return ubicacion;
    }
    public String getMetros() {
        return metros;
    }
    public String getPrecio() {
        return precio;
    }
    public String getDescripcion() {
        return descripcion;
    }


    @Override
    public String toString() {
        return todoCompleto;
    }
}
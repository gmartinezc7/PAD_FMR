package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.net.Uri;

public class Anuncio {
    private int id;
    private String todoCompleto;

    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;

    private Uri imagenUri;
    private Intent data;
    public Anuncio(int id, Intent data) {

        this.id = id;
        this.data = data;

        this.titulo = data.getStringExtra("titulo");
        this.ubicacion = data.getStringExtra("ubicacion");
        this.metros = data.getStringExtra("metros");
        this.precio = data.getStringExtra("precio");
        this.imagenUri = data.getParcelableExtra("imagenUri");
        this.todoCompleto = "Título: " + titulo + "\nUbicación: " + ubicacion +
                "\nMetros cuadrados: " + metros + "\nPrecio: " + precio;


    }

    public Intent getData() {
        return data;
    }

    public Uri getImagenUri() {
        return imagenUri;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "Anuncio " + id + ": \n" + todoCompleto;
    }
}
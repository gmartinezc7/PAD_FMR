package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Anuncio {
    private String todoCompleto;

    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;
    private String descripcion;
    private List<Uri> imagenesUri = new ArrayList<>();

    public Anuncio( Intent data) {


        this.titulo = data.getStringExtra("titulo");
        this.ubicacion = data.getStringExtra("ubicacion");
        this.metros = data.getStringExtra("metros");
        this.precio = data.getStringExtra("precio");
        this.descripcion = data.getStringExtra("descripcion");
        this.imagenesUri = data.getParcelableArrayListExtra("imagenesUri");


        this.todoCompleto =  "Ubicación: " + ubicacion +
                "\n\nMetros cuadrados: " + metros + "\n\nPrecio: " + precio;

        if(!this.descripcion.isEmpty()){
            this.todoCompleto += "\n\nDescripción:\n" + descripcion;
        }

    }


    public  List<Uri> getImagenesUri() {
        return imagenesUri;
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
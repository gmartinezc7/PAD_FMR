package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Anuncio {

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

    }





    public  List<Uri> getImagenesUri() {
        return imagenesUri;
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


    public void setTitulo(String titulo){

        this.titulo = titulo;

    }

    public void setUbicacion(String ubicacion){

        this.ubicacion = ubicacion;

    }

    public void setMetros(String metros){

        this.metros = metros;

    }

    public void setPrecio(String precio){
        this.precio = precio;
    }

    public void setDescripcion(String descripcion){
      this.descripcion = descripcion;
    }


}
package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


//CLASE QUE NOS PERMITE MANEJAR ANUNCIOS, NOS GUSTARÍA QUE IMPLEMENTASE
// "PARCELABLE" PARA PODER PASARLA MEDIANTE INTENTS, PERO TRAS MUCHAS PRUEBAS
// Y ERROES QUE PERSISTÍAN, HEMOS DECIDIDO DEJARLA COMO UNA CLASE NORMAL, ES POR ESO QUE
//EN OTRAS PARTES DEL CÓDIGO HEMOS TENIDO QUE TRANSFERIR LOS DATOS ATRIBUTO POR ATRIBUTO,
//PERO COMO SON SOLO 6, EL PROBLEMA NO HA SIDO MUY GRANDE.
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




}
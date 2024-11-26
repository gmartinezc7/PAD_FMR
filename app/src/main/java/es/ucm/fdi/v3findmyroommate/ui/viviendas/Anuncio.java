package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.v3findmyroommate.R;


//CLASE QUE NOS PERMITE MANEJAR ANUNCIOS, NOS GUSTARÍA QUE IMPLEMENTASE
// "PARCELABLE" PARA PODER PASARLA MEDIANTE INTENTS, PERO TRAS MUCHAS PRUEBAS
// Y ERROES QUE PERSISTÍAN, HEMOS DECIDIDO DEJARLA COMO UNA CLASE NORMAL, ES POR ESO QUE
//EN OTRAS PARTES DEL CÓDIGO HEMOS TENIDO QUE TRANSFERIR LOS DATOS ATRIBUTO POR ATRIBUTO,
//PERO COMO SON SOLO 6, EL PROBLEMA NO HA SIDO MUY GRANDE.
public class Anuncio {

    public static int generadorId = 0;
    private String idAnuncio;
    private String titulo;
    private String ubicacion;
    private String metros;
    private String precio;
    private String descripcion;
    private List<Uri> imagenesUri = new ArrayList<>();


    /*
    // TEST TEST TEST
    {
        Uri exampleUri = Uri.parse("android.resource://es.ucm.fdi.v3findmyroommate/" + R.drawable.age_diversity_icon);
        this.imagenesUri.add(exampleUri);
    }
    // END OF TEST
*/

    //TAGS
    private String categoria;

    //Para la casa:
    private String tipoCasa;
    private String habitaciones;
    private String banos;
    private String exteriorInterior;

    //Para la habitación:
    private String companeros;
    private String genero;
    private String tipoBano;


    public Anuncio(Intent data) {

        this.idAnuncio = data.getStringExtra("id");

        // Si el Intent no tiene un campo ID (cuando se crea el anuncio, por ejemplo):
        if (this.idAnuncio == null) {
            // Genera un número aleatorio.
            Random rand = new Random();
            int randomIDNumber = rand.nextInt(Integer.MAX_VALUE);

            // Crea el ID como un string basado en el número aleatorio creado anteriormente.
            this.idAnuncio = "a" + randomIDNumber;
        }


        this.titulo = data.getStringExtra("titulo");
        this.ubicacion = data.getStringExtra("ubicacion");
        this.metros = data.getStringExtra("metros");
        this.precio = data.getStringExtra("precio");
        this.descripcion = data.getStringExtra("descripcion");


        // TEST TEST TEST
        this.imagenesUri = data.getParcelableArrayListExtra("imagenesUri");
        // END TEST


        //TAGS
        this.categoria = data.getStringExtra("categoria");

        if(this.categoria.equalsIgnoreCase("Casa")){

            this.tipoCasa = data.getStringExtra("tipoCasa");
            this.habitaciones = data.getStringExtra("habitaciones");
            this.banos = data.getStringExtra("banos");
            this.exteriorInterior = data.getStringExtra("exteriorInterior");

        }
        else if(categoria.equalsIgnoreCase("Habitación")){

            this.companeros = data.getStringExtra("companeros");
            this.genero = data.getStringExtra("genero");
            this.exteriorInterior = data.getStringExtra("exteriorInterior");
            this.tipoBano = data.getStringExtra("tipoBano");

        }
    }

    public List<Uri> getImagenesUri() {
        return imagenesUri;
    }
    public String getId() {
        return idAnuncio;
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


    public String getCategoria() {
        return categoria;
    }

    public String getTipoCasa() {
        return tipoCasa;
    }
    public String getHabitaciones() {
        return habitaciones;
    }
    public String getBanos() {
        return banos;
    }
    public String getExteriorInterior() {
        return exteriorInterior;
    }

    public String getCompaneros() {
        return companeros;
    }
    public String getGenero() {
        return genero;
    }
    public String getTipoBano() {
        return tipoBano;
    }

}
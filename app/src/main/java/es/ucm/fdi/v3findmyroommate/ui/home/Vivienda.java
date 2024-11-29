package es.ucm.fdi.v3findmyroommate.ui.home;

import java.io.Serializable;
import java.util.ArrayList;
import android.net.Uri;
import java.util.List;

public class Vivienda implements Serializable {

    private String id;
    private String title;
    private String location;
    private String metr;
    private String price;
    private String description;
    private List<Uri> imagenesUri = new ArrayList<>();

    // TAGS
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

    //Usuario dueño
    private String userId;
    private String ownerName;

    public Vivienda (){} // constructor que necesita Firebase
    public Vivienda (String id, String name, String price, String description, String address, String metr, List<Uri> imagenes,
                     String categoria, String tipoCasa, String habitaciones, String banos, String exteriorInterior,
                     String companeros, String genero, String tipoBano){
        this.id = id;
        this.title = name;
        this.location = address;
        this.metr = metr;
        this.price = price;
        this.description = description;
        this.imagenesUri = imagenes;
        this.categoria = categoria;
        this.tipoCasa = tipoCasa;
        this.habitaciones = habitaciones;
        this.banos = banos;
        this.exteriorInterior = exteriorInterior;
        this.companeros = companeros;
        this.genero = genero;
        this.tipoBano = tipoBano;

    }

    // SET
    public void setId(String id){ this.id = id; }
    public void setTitle(String name){ this.title = name; }
    public void setPrice(String  price){ this.price = price; }
    public void setDescription(String description){ this.description = description; }
    public void setLocation(String address){ this.location = address; }
    public void setMetr(String metr) {this.metr = metr; }
    public  void setImagenesUri(List<Uri> list){this.imagenesUri = list;}

    public void setCategoria(String categoria) { this.categoria = categoria; }

    public void setTipoCasa(String tipocasa) {
        this.tipoCasa = tipocasa;
    }
    public void setHabitaciones(String habs) {
        this.habitaciones = habs;
    }
    public void setBanos(String b) {
        this.banos = b;
    }
    public void setExteriorInterior(String ei) {
        this.exteriorInterior = ei;
    }

    public void setCompaneros(String c) {
        this.companeros = c;
    }
    public void setGenero(String g) {
        this.genero = g;
    }
    public void setTipoBano(String tb) {
        this.tipoBano = tb;
    }

    public void setUserId(String userId) {this.userId = userId;}
    public void setOwnerName(String ownerName) {this.ownerName = ownerName;}

    // GET
    public String getId () { return this.id;}
    public String getTitle() { return this.title;}
    public String getPrice() { return this.price;}
    public String getDescription() { return this.description;}
    public String getLocation() { return this.location;}
    public String getMetr() { return this.metr; }
    public  List<Uri> getImagenesUri() {
        return this.imagenesUri;
    }
    public String getCategoria() { return this.categoria;}

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

    public String getUserId() {return userId;}
    public String getOwnerName() {return ownerName;}

    public void printVivienda (){
        System.out.println("INFORMACIONVIVIENDA");
        System.out.println("Title: " + this.title);
        System.out.println("Price: " + this.price);
        System.out.println("Description: " + this.description);
        System.out.println("Location: " + this.location);
        System.out.println("Metr: " + this.metr);
        System.out.println("Categoria: " + this.categoria);
        System.out.println("TipoCasa: " + this.tipoCasa);
        System.out.println("Habitaciones: " + this.habitaciones);
        System.out.println("Baños: " + this.banos);
        System.out.println("Orientacion: " + this.exteriorInterior);
        System.out.println("Compañeros: " + this.companeros);
        System.out.println("Genero: " + this.genero);
        System.out.println("TipoBaño: " + this.tipoBano);

    }

}

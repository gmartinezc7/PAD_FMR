package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.net.Uri;

public class Anuncio {
    private int id;
    private String detalle;
    private Uri imagenUri;

    public Anuncio(int id, String detalle, Uri imagenUri) {
        this.id = id;
        this.detalle = detalle;
        this.imagenUri = imagenUri;
    }


    public Uri getImagenUri() {
        return imagenUri;
    }

    public int getId() {
        return id;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        return "Anuncio " + id + ": \n" + detalle;
    }
}
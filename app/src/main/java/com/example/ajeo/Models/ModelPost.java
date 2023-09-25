package com.example.ajeo.Models;

public class ModelPost {

//    ,UltimaPuja
    String Titulo,ruta,Precio,Raza,Descripcion,email,PId,uid,timestamp,image,PrecioInicial;
    Long UltimaPuja;

    public ModelPost(String titulo, String ruta, String precio, String raza, String descripcion, String email, String PId, String uid, String timestamp, String image, String precioInicial, Long ultimaPuja) {
        Titulo = titulo;
        this.ruta = ruta;
        Precio = precio;
        Raza = raza;
        Descripcion = descripcion;
        this.email = email;
        this.PId = PId;
        this.uid = uid;
        this.timestamp = timestamp;
        this.image = image;
        PrecioInicial = precioInicial;
        UltimaPuja = ultimaPuja;
    }

    public ModelPost(){}

    public String getImage() {
        return image;
    }

    public Long getUltimaPuja() {
        return UltimaPuja;
    }

    public String getTitulo() {
        return Titulo;
    }

    public String getRuta() {
        return ruta;
    }

    public String getPrecio() {
        return Precio;
    }

    public String getPrecioInicial() {
        return PrecioInicial;
    }

    public String getRaza() {
        return Raza;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getEmail() {
        return email;
    }

    public String getPId() {
        return PId;
    }

    public String getUid() {
        return uid;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

package com.example.ajitaawno;

import java.util.Date;

public class Offre  {
    String description ;
    String etat;
    Long NombrePiece;
    Date TimePub;
    String TitreOffre;
    String user;
    String Image_Offre;
    String ville;

    public Offre(String description, String etat, Long nombrePiece, Date timePub, String titreOffre, String user, String ImageOffre, String ville) {
        this.description = description;
        this.etat = etat;
        this.NombrePiece = nombrePiece;
        this.TimePub = timePub;
        this.TitreOffre = titreOffre;
        this.user = user;
        this.Image_Offre = ImageOffre;
        this.ville= ville;

    }
    public Offre(){

    }
    public  String getVille(){
        return  this.ville;
    }
    public void setVille(String ville){
        this.ville = ville;
    }

    public String getImage_Offre() {
        return Image_Offre;
    }

    public void setImage_Offre(String image_Offre) {
        Image_Offre = image_Offre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setNombrePiece(Long nombrePiece) {
        NombrePiece = nombrePiece;
    }

    public void setTimePub(Date timePub) {
        TimePub = timePub;
    }

    public void setTitreOffre(String titreOffre) {
        TitreOffre = titreOffre;
    }

    public void setUser(String user_id) {
        user = user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getEtat() {
        return etat;
    }

    public Long getNombrePiece() {
        return NombrePiece;
    }

    public Date getTimePub() {
        return TimePub;
    }

    public String getTitreOffre() {
        return TitreOffre;
    }

    public String getUser() {
        return user;
    }
}

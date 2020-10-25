package com.example.ajitaawno;




import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Offres implements Comparable<Offres>, Serializable {

    @Exclude private String id;
    private String Description;
    private String Image_Offre;
    private String TitreOffre;
    private String User_id;
    private String Etat;
    private int NombrePiece;
    private Date TimePub;
    private String Categorie;
    private String Ville;

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public String getCategorie() {
        return Categorie;
    }

    public String getVille() {
        return Ville;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Offres(){}

    public Offres(String titreOffre,String description,int nombrePiece, String image_Offre,  String user_id, String etat,String categorie,String ville) {
        Description = description;
        Image_Offre = image_Offre;
        TitreOffre = titreOffre;
        User_id = user_id;
        Etat = etat;
        NombrePiece = nombrePiece;

        Categorie=categorie;
        Ville=ville;
    }
    public Offres(String titreOffre,String description,int nombrePiece, String image_Offre,  String user_id, String etat,  Date timePub,String categorie,String ville) {
        Description = description;
        Image_Offre = image_Offre;
        TitreOffre = titreOffre;
        User_id = user_id;
        Etat = etat;
        NombrePiece = nombrePiece;
        TimePub = timePub;
        Categorie=categorie;
        Ville=ville;
    }



    public void setTimePub(Date timePub) {
        TimePub = timePub;
    }

    public Date getTimePub() {
        return TimePub;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImage_Offre(String image_Offre) {
        Image_Offre = image_Offre;
    }

    public void setTitreOffre(String titreOffre) {
        TitreOffre = titreOffre;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public void setEtat(String etat) {
        Etat = etat;
    }

    public void setNombrePiece(int nombrePiece) {
        NombrePiece = nombrePiece;
    }



    public String getDescription() {
        return Description;
    }

    public String getImage_Offre() {
        return Image_Offre;
    }

    public String getTitreOffre() {
        return TitreOffre;
    }

    public String getUser_id() {
        return User_id;
    }

    public String getEtat() {
        return Etat;
    }

    public int getNombrePiece() {
        return NombrePiece;
    }


    @Override
    public int compareTo(Offres o) {
        return o.getTimePub().compareTo(getTimePub());
    }

    @Override
    public String toString() {
        return "Offres{" +
                "Description='" + Description + '\'' +
                ", Image_Offre='" + Image_Offre + '\'' +
                ", TitreOffre='" + TitreOffre + '\'' +
                ", User_id='" + User_id + '\'' +
                ", Etat=" + Etat +
                ", NombrePiece=" + NombrePiece +
                ", TimePub=" + TimePub +
                ", Categorie_id=" + Categorie +
                ", Ville_id=" + Ville +
                '}';
    }
}

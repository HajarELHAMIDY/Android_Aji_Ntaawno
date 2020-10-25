package com.example.ajitaawno;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Demande implements Comparable<Demande>, Serializable {
    @Exclude private String id;

    String Reservation;
    int NombrePiece;
    Date TimePub;
    String TitreDemande;
    String user;
    String Image_Demande;
    private String Categorie;
    private String Ville;
    String Description ;
    public Demande(){}
    public Demande(String description, String reservation, int nombrePiece, Date timePub, String titreOffre, String user, String image_Offre, String Categorie, String Ville) {

        this.Description = description;
        this.Reservation = reservation;
        this.NombrePiece = nombrePiece;
        this.TimePub = timePub;
        this.TitreDemande = titreOffre;
        this.user = user;
        Image_Demande = image_Offre;
        this.Categorie = Categorie;
        this.Ville = Ville;
    }

    public Demande(String description, String reservation, int nombrePiece, String titreOffre, String user, String image_Offre, String Categorie, String Ville) {
        this.Description = description;
        this.Reservation = reservation;
        NombrePiece = nombrePiece;

        TitreDemande = titreOffre;
        this.user = user;
        Image_Demande = image_Offre;
        this.Categorie = Categorie;
        this.Ville = Ville;
    }

    public void setTitreDemande(String titreDemande) {
        TitreDemande = titreDemande;
    }

    public void setDescriptionD(String descriptionD) {
        this.Description = descriptionD;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public String getVille() {
        return Ville;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public String getUser() {
        return user;
    }


    public String getImage_Demande() {
        return Image_Demande;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

   public String getReservation() {
     return Reservation;
    }

    public void setReservation(String reservation) {
        this.Reservation = reservation;
    }

    public int getNombrePiece() {
        return NombrePiece;
    }

    public void setNombrePiece(int nombrePiece) {
        NombrePiece = nombrePiece;
    }

    public Date getTimePub() {
        return TimePub;
    }

    public void setTimePub(Date timePub) {
        TimePub = timePub;
    }

    public String getTitreDemande() {
        return TitreDemande;
    }


    public void setTitreOffre(String titreOffre) {
        TitreDemande = titreOffre;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setImage_Demande(String image_Demande) {
        Image_Demande = image_Demande;
    }

    @Override
    public int compareTo(Demande o) {
        return o.getTimePub().compareTo(getTimePub());
    }

    @Override
    public String toString() {
        return "Demandes{" +
                "Description='" + Description+ '\'' +
                ", Image_Offre='" + Image_Demande+ '\'' +
                ", TitreOffre='" + TitreDemande + '\'' +
                ", User_id='" + user + '\'' +
                ", Etat=" + Reservation +
                ", NombrePiece=" + NombrePiece +
                ", TimePub=" + TimePub +
                ", Categorie_id=" + Categorie +
                ", Ville_id=" + Ville +
                '}';
    }
}

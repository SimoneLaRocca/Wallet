package it.unisa.walletmanagement.Model.Entity;

import java.io.Serializable;
import java.util.List;

public class ListaCategorie implements Serializable {
    private List<String> categorie;

    public ListaCategorie(List<String> categorie) {
        this.categorie = categorie;
    }

    public ListaCategorie() {
    }

    public List<String> getCategorie() {
        return categorie;
    }

    public void setCategorie(List<String> categorie) {
        this.categorie = categorie;
    }
}

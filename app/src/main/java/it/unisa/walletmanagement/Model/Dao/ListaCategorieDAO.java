package it.unisa.walletmanagement.Model.Dao;

import android.content.Context;

import java.util.List;

import it.unisa.walletmanagement.Model.Entity.ListaCategorie;
import it.unisa.walletmanagement.Model.Storage.FileManager;

public class ListaCategorieDAO {

    private String fileName;
    private Context context;

    public ListaCategorieDAO(Context context) {
        this.context = context;
        this.fileName = "categorie.txt";
    }

    public ListaCategorieDAO(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
    }

    /**
     * Inserisci categoria nel file categorie.txt.
     * @param nome nome della categoria
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public boolean insertCategoria(String nome){
        ListaCategorie listaCategorie = doRetrieveListaCategorie();
        if(listaCategorie != null){
            if(listaCategorie.getCategorie().contains(nome)){
                return false;
            }
        }
        if(FileManager.writeRecordToFile(context, fileName, nome, true)){
            return true;
        }
        return false;
    }

    /**
     * Rimuove la categoria dal file categorie.txt.
     * @param nome nome della specifica categoria da eliminare
     */
    public void deleteCategoria(String nome){
        FileManager.deleteRecordFromFile(context, fileName, nome);
    }

    /**
     * Restituisce un semplice oggetto ListaCategorie che contiene la lista
     * di tutte le categorie salvate nel file categorie.txt.
     * @return oggetto entity ListaCategorie
     */
    public ListaCategorie doRetrieveListaCategorie(){
        ListaCategorie listaCategorie = new ListaCategorie();
        List<String> list = FileManager.readListFromFile(context, fileName);
        if(list == null || list.size() == 0 || list.get(0).equals("")){
            return null;
        }
        listaCategorie.setCategorie(list);
        return listaCategorie;
    }
}

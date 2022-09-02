package it.unisa.walletmanagement.Model.Dao;

import android.content.Context;

import java.util.List;

import it.unisa.walletmanagement.Model.Entity.ListaSpesa;
import it.unisa.walletmanagement.Model.Storage.FileManager;

public class ListaSpesaDAO {

    private String fileName;
    private Context context;

    public ListaSpesaDAO(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
    }

    public ListaSpesaDAO(Context context) {
        this.context = context;
        this.fileName = "lista_spesa.txt";
    }

    /**
     * Inserisce la voce della lista della spesa nel file lista_spesa.txt
     * @param voce voce della lista della spesa
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public boolean insertVoce(String voce){
        if(FileManager.writeRecordToFile(context, fileName, voce, true)){
            return true;
        }
        return false;
    }

    /**
     * Rimuove la voce dal file lista_spesa.txt.
     * @param voce voce da eliminare
     */
    public void deleteVoce(String voce){
        FileManager.deleteRecordFromFile(context, fileName, voce);
    }

    /**
     * Restituisce un semplice oggetto ListaSpesa che contiene la lista
     * di tutte le voci salvate nel file lista_spesa.txt.
     * @return oggetto entity ListaSpesa
     */
    public ListaSpesa doRetrieveListaSpesa(){
        ListaSpesa listaSpesa = new ListaSpesa();
        List<String> list = FileManager.readListFromFile(context, fileName);
        if(list == null || list.size() == 0 || list.get(0).equals("")){
            return null;
        }
        listaSpesa.setLista(list);
        return listaSpesa;
    }
}

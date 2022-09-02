package it.unisa.walletmanagement.Model.Storage;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {

    /**
     * Scrive su un dato file una stringa testuale (record) interponendo
     * a ogni scrittura un carattere newline (\n)
     * @param context contesto dell'applicazione
     * @param fileName nome del file privato su cui scrivere
     * @param content record da scrivere
     * @param mode modalità di scrittura (false = write / true = append)
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public static boolean writeRecordToFile(Context context, String fileName, String content, boolean mode){
        String[] list = context.fileList();
        File path = context.getFilesDir();
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(new File(path, fileName), mode);
            content += "\n";
            writer.write(content.getBytes());
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Scrive su un dato file una stringa testuale.
     * @param context contesto dell'applicazione
     * @param fileName nome del file privato su cui scrivere
     * @param content stringa di testo da scrivere
     * @param mode modalità di scrittura (false = write / true = append)
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public static boolean writeToFile(Context context, String fileName, String content, boolean mode){
        String[] list = context.fileList();
        File path = context.getFilesDir();
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(new File(path, fileName), mode);
            writer.write(content.getBytes());
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     Scrive su un dato file una serie di record (stringhe testuali) usando
     come separatore un carattere newline (\n).
     * @param context contesto dell'applicazione
     * @param fileName nome del file privato su cui scrivere
     * @param content lista di record testuali da scrivere
     * @param mode modalità di scrittura (false = write / true = append)
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public static boolean writeListToFile(Context context, String fileName, List<String> content, boolean mode){
        String[] list = context.fileList();
        File path = context.getFilesDir();
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(new File(path, fileName), mode);
            for (String item : content){
                item += '\n';
                writer.write(item.getBytes());
            }
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Legge tutto il contenuto di un dato file.
     * @param context contesto dell'applicazione
     * @param fileName nome del file privato da leggere
     * @return contenuto del file
     */
    public static String readFromFile(Context context, String fileName){
        File path = context.getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content =  new byte[(int) readFrom.length()];

        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            return new String(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Legge tutto il contenuto di un dato file e ne restituisce il contenuto
     * sotto forma di lista in cui ogni elemento è un record.
     * Il separatore utilizzato è il carattere newline (\n).
     * @param context contesto dell'applicazione
     * @param fileName nome del file privato
     * @return lista di record testuali
     */
    public static List<String> readListFromFile(Context context, String fileName){
        File path = context.getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content =  new byte[(int) readFrom.length()];

        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            List<String> list = Arrays.asList(new String(content).split("\n"));
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cancella un record da un dato file.
     * @param context contesto dell'applicazione
     * @param fileName nome del file privato
     * @param delete record da rimuovere
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public static boolean deleteRecordFromFile(Context context, String fileName, String delete){
        ArrayList<String> list = new ArrayList(readListFromFile(context, fileName));
        if(list.contains(delete)){
            list.remove(delete);
            writeListToFile(context, fileName, list, false);
            return true;
        }
        return false;
    }
}

package it.unisa.walletmanagement.Control.Grafici;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public interface Chart {
    /**
     * Metodo generico per la creazione di un grafico.
     * Presenta una configurazione di default.
     * @param activity corrente
     * @param id del grafico nel layout
     */
    void draw(AppCompatActivity activity, int id);

    /**
     * Metodo specifico per la creazione di grafici di tipo Bar.
     * @param activity corrente
     * @param id del grafico nel layout
     * @param labels lista di etichette associate all'asse x
     * @param values lista di valori per ogni specifico elemento sull'asse x
     * @param label descrizione del tipo di valore
     */
    void draw(AppCompatActivity activity, int id, String[] labels, float[] values, String label);

    /**
     * Metodo specifico per la creazione di grafici di tipo GroupedBar e Line.
     * @param activity corrente
     * @param id del grafico nel layout
     * @param labels lista di etichette associate all'asse x
     * @param group1 lista di valori per ogni specifico elemento sull'asse x del gruppo 1
     * @param group2 lista di valori per ogni specifico elemento sull'asse x del gruppo 2
     * @param label1 descrizione del tipo di valore del gruppo 1
     * @param label2 descrizione del tipo di valore del gruppo 2
     */
    void draw(AppCompatActivity activity, int id, String[] labels, float[] group1, float[] group2, String label1, String label2);

    /**
     * Metodo specifico per la creazione di grafici di tipo Pie.
     * @param activity corrente
     * @param id del grafico nel layout
     * @param map HashMap in cui le chiave sono le labels (lista etichette associate all'asse x)
     *            e i valori sono i values (lista di valori per ogni specifico elemento sull'asse x)
     * @param label descrizione del tipo di valore
     */
    void draw(AppCompatActivity activity, int id, HashMap<String, Integer> map, String label);
}

package it.unisa.walletmanagement.Model.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.Model.Storage.DatabaseHelper;
import it.unisa.walletmanagement.Model.Storage.SchemaDB;

public class MovimentoDAO {
    private DatabaseHelper databaseHelper;

    public MovimentoDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    /**
     * Inserisce il movimento nel db.
     * @param movimento oggetto da salvare
     * @param nome_conto conto in cui è contenuto il movimento
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public boolean insertMovimento(Movimento movimento, String nome_conto){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String text_date = simpleDateFormat.format(movimento.getData().getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put(SchemaDB.Movimento.COLUMN_NOME, movimento.getNome());
        contentValues.put(SchemaDB.Movimento.COLUMN_IMPORTO, movimento.getImporto());
        contentValues.put(SchemaDB.Movimento.COLUMN_TIPO, movimento.getTipo());
        contentValues.put(SchemaDB.Movimento.COLUMN_CATEGORIA, movimento.getCategoria());
        contentValues.put(SchemaDB.Movimento.COLUMN_DATA, text_date);
        contentValues.put(SchemaDB.Movimento.COLUMN_NOME_CONTO, nome_conto);

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.insert(SchemaDB.Movimento.TABLE_NAME, null, contentValues);

        return true;
    }

    /**
     * Aggiorna movimento nel db.
     * @param movimento oggetto da aggiornare
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public boolean updateMovimento(Movimento movimento){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String text_date = simpleDateFormat.format(movimento.getData().getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put(SchemaDB.Movimento.COLUMN_NOME, movimento.getNome());
        contentValues.put(SchemaDB.Movimento.COLUMN_IMPORTO, movimento.getImporto());
        contentValues.put(SchemaDB.Movimento.COLUMN_TIPO, movimento.getTipo());
        contentValues.put(SchemaDB.Movimento.COLUMN_CATEGORIA, movimento.getCategoria());
        contentValues.put(SchemaDB.Movimento.COLUMN_DATA, text_date);

        String where = SchemaDB.Movimento.COLUMN_ID + "=?";
        String whereArgs[]= {String.valueOf(movimento.getId())};

        sqLiteDatabase.update(SchemaDB.Movimento.TABLE_NAME, contentValues, where, whereArgs);
        return true;
    }

    /**
     * Rimuove il movimento dal db.
     * @param id del movimento da rimuovere
     * @return un valore booleano per segnalare il risultato dell'operazione
     */
    public boolean deleteMovimento(int id){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        int deleted_rows = sqLiteDatabase.delete(SchemaDB.Movimento.TABLE_NAME,
                SchemaDB.Movimento.COLUMN_ID+"=?",
                new String[]{String.valueOf(id)});
        sqLiteDatabase.close();

        if(deleted_rows > 0)
            return true;
        return false;
    }

    /**
     * Restituisce una lista di tutti i movimenti nel db.
     * @return una lista di tutti i movimenti
     */
    public List<Movimento> doRetrieveAll(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        ArrayList<Movimento> movimenti = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String query = "SELECT * FROM "+SchemaDB.Movimento.TABLE_NAME;

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});
        if(cursor.getCount() <= 0){
            sqLiteDatabase.close();
            return null;
        }
        while (cursor.moveToNext()){
            Movimento m = new Movimento();
            m.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_ID)));
            m.setNome(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_NOME)));
            m.setImporto(cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_IMPORTO)));
            m.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_TIPO)));
            m.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_CATEGORIA)));
            // data
            try {
                String text_date = cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_DATA));
                Date date = simpleDateFormat.parse(text_date);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                m.setData(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movimenti.add(m);
        }
        sqLiteDatabase.close();
        cursor.close();
        return movimenti;
    }

    /**
     * Restituisce una lista dei movimenti per tipo (entrata/uscita).
     * @param tipo valore intero che specifica il tipo di movimento (1: entrata, 0: uscita)
     * @return una lista dei movimenti per il tipo specificato
     */
    public List<Movimento> doRetrieveByType(int tipo){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        ArrayList<Movimento> movimenti = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String query = "SELECT * FROM "+SchemaDB.Movimento.TABLE_NAME+
                " WHERE "+SchemaDB.Movimento.COLUMN_TIPO+" = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(tipo)});
        if(cursor.getCount() <= 0){
            sqLiteDatabase.close();
            return null;
        }
        while (cursor.moveToNext()){
            Movimento m = new Movimento();
            m.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_ID)));
            m.setNome(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_NOME)));
            m.setImporto(cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_IMPORTO)));
            m.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_TIPO)));
            m.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_CATEGORIA)));
            // data
            try {
                String text_date = cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_DATA));
                Date date = simpleDateFormat.parse(text_date);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                m.setData(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movimenti.add(m);
        }
        sqLiteDatabase.close();
        cursor.close();
        return movimenti;
    }

    /**
     * Restituisce il saldo corrente di un dato conto.
     * @param nome_conto nome del conto
     * @return un valore float che specifica il saldo per il dato conto
     */
    public float doRetrieveCurrentBalance(String nome_conto){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String query = "SELECT " +
                "COALESCE((SELECT SUM(" + SchemaDB.Movimento.COLUMN_IMPORTO + ") " +
                "FROM " + SchemaDB.Movimento.TABLE_NAME + " " +
                "WHERE " + SchemaDB.Movimento.COLUMN_TIPO + " = 1 AND " + SchemaDB.Movimento.COLUMN_NOME_CONTO + " = ?), 0) - " +
                "COALESCE((SELECT SUM(" + SchemaDB.Movimento.COLUMN_IMPORTO + ") " +
                "FROM " + SchemaDB.Movimento.TABLE_NAME + " " +
                "WHERE " + SchemaDB.Movimento.COLUMN_TIPO + " = 0 AND " + SchemaDB.Movimento.COLUMN_NOME_CONTO + " = ?), 0) AS sum";
        float value = 0;

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{nome_conto, nome_conto});
        if(cursor.moveToNext()){
            value += cursor.getFloat(cursor.getColumnIndexOrThrow("sum"));
        }

        query = "SELECT " + SchemaDB.Conto.COLUMN_SALDO +
                " FROM " + SchemaDB.Conto.TABLE_NAME +
                " WHERE " + SchemaDB.Conto.COLUMN_NOME + " = ? ";
        cursor = sqLiteDatabase.rawQuery(query, new String[]{nome_conto});
        if(cursor.moveToNext()){
            value += cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.Conto.COLUMN_SALDO));
        }

        sqLiteDatabase.close();
        cursor.close();
        return value;
    }

    /**
     * Restituisce un array di 7 elementi, che specifica la somma
     * delle entrate/uscite per ogni giorno della settimana corrente.
     * La settimana inizia con domenica.
     * @param tipo del movimento (1: entrata, 0: uscita)
     * @return array di float di 7 elementi con gli indici che
     * si riferiscono ai giorni della settimana e i valori alla
     * somma delle entrate/uscite per lo specifico giorno
     */
    public float[] doRetrieveForCurrentWeek(int tipo){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        float[] list = new float[7];
        Arrays.fill(list, 0f);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(GregorianCalendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String first_day_of_week = simpleDateFormat.format(gregorianCalendar.getTime());

        String query = "SELECT * " +
                " FROM " + SchemaDB.Movimento.TABLE_NAME +
                " WHERE CAST((SUBSTR(" + SchemaDB.Movimento.COLUMN_DATA + ", 7)||" +
                    "SUBSTR(" + SchemaDB.Movimento.COLUMN_DATA + ", 4, 2)||" +
                    "SUBSTR(" + SchemaDB.Movimento.COLUMN_DATA + ", 1, 2)) AS INTEGER) " +
                    " >= CAST((SUBSTR( ?, 7)||SUBSTR( ?, 4, 2)||SUBSTR( ?, 1, 2)) AS  INTEGER)" +
                    " AND " + SchemaDB.Movimento.COLUMN_TIPO + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,
                new String[]{first_day_of_week, first_day_of_week,
                        first_day_of_week, String.valueOf(tipo)});

        if(cursor.getCount() <= 0){
            sqLiteDatabase.close();
            return list;
        }

        while (cursor.moveToNext()){
            Movimento m = new Movimento();
            m.setImporto(cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_IMPORTO)));
            try {
                String text_date = cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_DATA));
                Date date = simpleDateFormat.parse(text_date);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                m.setData(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int day = m.getData().get(GregorianCalendar.DAY_OF_WEEK);
            list[day-1] += m.getImporto();
        }

        sqLiteDatabase.close();
        cursor.close();

        return list;
    }

    /**
     * Restituisce il numero di movimenti per ogni categoria salvati nel db.
     * @return HashMap in cui le chiavi sono i nomi delle categorie e
     * i valori sono il numero totale dei movimenti
     */
    public HashMap<String, Integer> doCountMovimentiPerCategoria(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        HashMap<String, Integer> map = new HashMap<>();

        String query = "SELECT " + SchemaDB.Movimento.COLUMN_CATEGORIA + ", COUNT(*) AS tot " +
                "FROM " + SchemaDB.Movimento.TABLE_NAME +
                " GROUP BY " + SchemaDB.Movimento.COLUMN_CATEGORIA;

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});

        if(cursor.getCount() <= 0){
            sqLiteDatabase.close();
            return map;
        }

        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_CATEGORIA));
            int tot = cursor.getInt(cursor.getColumnIndexOrThrow("tot"));
            map.put(name, tot);
        }

        sqLiteDatabase.close();
        cursor.close();
        return map;
    }

    /**
     * Restituisce un array di 5 elementi, che specifica la somma
     * delle entrate/uscite per ogni settimana del mese corrente.
     * @param tipo del movimento (1: entrata, 0: uscita)
     * @return array di float di 5 elementi con gli indici che si riferiscono
     * alle settimane del mese corrente e i valori alla somma
     * delle entrate/uscite per la specifica settimana
     */
    public float[] doRetrieveForCurrentMonth(int tipo){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        float[] list = new float[5];
        Arrays.fill(list, 0f);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String first_day_of_month = simpleDateFormat.format(gregorianCalendar.getTime());

        String query = "SELECT * " +
                " FROM " + SchemaDB.Movimento.TABLE_NAME +
                " WHERE CAST((SUBSTR(" + SchemaDB.Movimento.COLUMN_DATA + ", 7)||" +
                "SUBSTR(" + SchemaDB.Movimento.COLUMN_DATA + ", 4, 2)||" +
                "SUBSTR(" + SchemaDB.Movimento.COLUMN_DATA + ", 1, 2)) AS INTEGER) " +
                " >= CAST((SUBSTR( ?, 7)||SUBSTR( ?, 4, 2)||SUBSTR( ?, 1, 2)) AS  INTEGER)" +
                " AND " + SchemaDB.Movimento.COLUMN_TIPO + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,
                new String[]{first_day_of_month, first_day_of_month,
                        first_day_of_month, String.valueOf(tipo)});

        if(cursor.getCount() <= 0){
            sqLiteDatabase.close();
            return list;
        }

        while (cursor.moveToNext()){
            Movimento m = new Movimento();
            m.setImporto(cursor.getFloat(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_IMPORTO)));
            try {
                String text_date = cursor.getString(cursor.getColumnIndexOrThrow(SchemaDB.Movimento.COLUMN_DATA));
                Date date = simpleDateFormat.parse(text_date);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                m.setData(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int week = m.getData().get(GregorianCalendar.WEEK_OF_MONTH);
            list[week-1] += m.getImporto();
        }

        sqLiteDatabase.close();
        cursor.close();

        return list;
    }
}

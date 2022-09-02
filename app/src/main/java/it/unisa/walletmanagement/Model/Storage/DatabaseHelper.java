package it.unisa.walletmanagement.Model.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "wallet_database";

    final private static String CREATE_CONTO =
            "CREATE TABLE "+SchemaDB.Conto.TABLE_NAME+" ("
                    + SchemaDB.Conto.COLUMN_NOME + " TEXT PRIMARY KEY, "
                    + SchemaDB.Conto.COLUMN_SALDO + " TEXT NOT NULL); ";

    final private static String CREATE_MOVIMENTO =
            "CREATE TABLE "+SchemaDB.Movimento.TABLE_NAME+" ("
                    + SchemaDB.Movimento.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SchemaDB.Movimento.COLUMN_NOME + " TEXT NOT NULL, "
                    + SchemaDB.Movimento.COLUMN_IMPORTO + " FLOAT NOT NULL, "
                    + SchemaDB.Movimento.COLUMN_TIPO + " INTEGER NOT NULL, "
                    + SchemaDB.Movimento.COLUMN_DATA + " TEXT NOT NULL, "
                    + SchemaDB.Movimento.COLUMN_CATEGORIA + " TEXT NOT NULL, "
                    + SchemaDB.Movimento.COLUMN_NOME_CONTO + " TEXT NOT NULL, "
                    + "FOREIGN KEY("+SchemaDB.Movimento.COLUMN_NOME_CONTO+") REFERENCES "
                    + SchemaDB.Conto.TABLE_NAME+"("+SchemaDB.Conto.COLUMN_NOME+") ON UPDATE CASCADE ON DELETE CASCADE);";

    final private static Integer VERSION = 2;
    final private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTO);
        sqLiteDatabase.execSQL(CREATE_MOVIMENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ SchemaDB.Conto.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ SchemaDB.Movimento.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {
        super.onOpen(sqLiteDatabase);
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
    }

    public void clearDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + SchemaDB.Conto.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM " + SchemaDB.Movimento.TABLE_NAME);
        sqLiteDatabase.close();
    }

    public void deleteDatabase() {
        context.deleteDatabase(DATABASE_NAME);
    }
}

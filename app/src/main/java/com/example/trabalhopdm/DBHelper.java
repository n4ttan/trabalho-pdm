package com.example.trabalhopdm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Nome do arquivo do banco e versão (usada para controlar atualizações de estrutura)
    private static final String DB_NAME = "chamados.db";
    private static final int DB_VERSION = 2;

    // Nome da tabela e das colunas
    public static final String TABLE_CHAMADOS = "chamados";
    public static final String COL_ID = "id";
    public static final String COL_TITULO = "titulo";
    public static final String COL_LOCAL = "local";
    public static final String COL_DESCRICAO = "descricao";
    public static final String COL_TIPO = "tipo";
    public static final String COL_STATUS = "status";
    public static final String COL_DATA = "data";
    public static final String COL_SOLUCAO = "solucao";
    public static final String COL_IMAGEM = "imagem";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Roda UMA VEZ, quando o banco é criado pela primeira vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_CHAMADOS + " (" +
                COL_ID + " TEXT PRIMARY KEY, " +
                COL_TITULO + " TEXT, " +
                COL_LOCAL + " TEXT, " +
                COL_DESCRICAO + " TEXT, " +
                COL_TIPO + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_DATA + " TEXT, " +
                COL_SOLUCAO + " TEXT, " +
                COL_IMAGEM + " TEXT)";
        db.execSQL(sql);
    }

    // Roda quando DB_VERSION aumenta — por enquanto só recria a tabela
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMADOS);
        onCreate(db);
    }

    // Insere um novo chamado
    public void inserirChamado(String id, String titulo, String local, String descricao,
                               String tipo, String status, String data, String solucao, String imagem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_TITULO, titulo);
        values.put(COL_LOCAL, local);
        values.put(COL_DESCRICAO, descricao);
        values.put(COL_TIPO, tipo);
        values.put(COL_STATUS, status);
        values.put(COL_DATA, data);
        values.put(COL_SOLUCAO, solucao);
        values.put(COL_IMAGEM, imagem);

        db.insert(TABLE_CHAMADOS, null, values);
        db.close();
    }

    // Retorna todos os chamados como um Cursor (similar a um "ponteiro" para os resultados)
    public Cursor listarChamados() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CHAMADOS, null);
    }

    // Atualiza solução e status de um chamado pelo ID
    public void atualizarChamado(String id, String solucao, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SOLUCAO, solucao);
        values.put(COL_STATUS, status);

        db.update(TABLE_CHAMADOS, values, COL_ID + " = ?", new String[]{id});
        db.close();
    }

    // Conta quantos chamados existem com um determinado status
    public int contarPorStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_CHAMADOS + " WHERE " + COL_STATUS + " = ?",
                new String[]{status}
        );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // Conta o total de chamados
    public int contarTotal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CHAMADOS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
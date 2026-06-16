package com.example.trabalhopdm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        recyclerView = findViewById(R.id.recyclerViewChamados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        carregarLista();
    }

    // Recarrega a lista toda vez que voltar para essa tela
    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    // Classe simples para representar um chamado em memória
    public static class Chamado {
        public String id, titulo, local, descricao, tipo, status, data, solucao, imagem;
    }

    private void carregarLista() {
        ArrayList<Chamado> chamados = new ArrayList<>();

        // Percorre o Cursor retornado pelo banco
        Cursor cursor = dbHelper.listarChamados();
        while (cursor.moveToNext()) {
            Chamado c = new Chamado();
            c.id = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_ID));
            c.titulo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITULO));
            c.local = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_LOCAL));
            c.descricao = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DESCRICAO));
            c.tipo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TIPO));
            c.status = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_STATUS));
            c.data = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DATA));
            c.solucao = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_SOLUCAO));
            c.imagem = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_IMAGEM));
            chamados.add(c);
        }
        cursor.close();

        // Cria o adapter passando a lista e o que fazer ao clicar num item
        ChamadoRecyclerAdapter adapter = new ChamadoRecyclerAdapter(chamados, chamado -> {
            Intent intent = new Intent(this, AtendimentoActivity.class);
            intent.putExtra("chamadoId", chamado.id);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
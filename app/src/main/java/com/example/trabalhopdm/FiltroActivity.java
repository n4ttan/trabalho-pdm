package com.example.trabalhopdm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FiltroActivity extends AppCompatActivity {

    Spinner spinnerFiltroStatus;
    EditText etFiltroData;
    Button btnFiltrar;
    RecyclerView recyclerView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        spinnerFiltroStatus = findViewById(R.id.spinnerFiltroStatus);
        etFiltroData = findViewById(R.id.etFiltroData);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        recyclerView = findViewById(R.id.recyclerViewFiltrado);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        // Opções do filtro de status — "Todos" mostra tudo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Todos", "Aberto", "Em Atendimento", "Concluído"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroStatus.setAdapter(adapter);

        // Carrega todos os chamados ao abrir a tela
        filtrar();

        btnFiltrar.setOnClickListener(v -> filtrar());
    }

    private void filtrar() {
        String statusFiltro = spinnerFiltroStatus.getSelectedItem().toString();
        String dataFiltro = etFiltroData.getText().toString().trim();

        ArrayList<ListaActivity.Chamado> resultado = new ArrayList<>();

        Cursor cursor = dbHelper.listarChamados();
        while (cursor.moveToNext()) {
            String status = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_STATUS));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DATA));

            // Verifica filtro de status
            boolean passouStatus = statusFiltro.equals("Todos") || status.equals(statusFiltro);

            // Verifica filtro de data (se preenchido)
            boolean passouData = dataFiltro.isEmpty() || data.equals(dataFiltro);

            if (passouStatus && passouData) {
                ListaActivity.Chamado c = new ListaActivity.Chamado();
                c.id = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_ID));
                c.titulo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITULO));
                c.local = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_LOCAL));
                c.tipo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TIPO));
                c.status = status;
                c.data = data;
                resultado.add(c);
            }
        }
        cursor.close();

        // Cria o adapter — mesmo adapter usado na ListaActivity
        ChamadoRecyclerAdapter recyclerAdapter = new ChamadoRecyclerAdapter(resultado, chamado -> {
            Intent intent = new Intent(this, AtendimentoActivity.class);
            intent.putExtra("chamadoId", chamado.id);
            startActivity(intent);
        });

        recyclerView.setAdapter(recyclerAdapter);
    }
}
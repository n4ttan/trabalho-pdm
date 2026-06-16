package com.example.trabalhopdm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conecta cada botão ao seu ID definido no XML
        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        Button btnLista = findViewById(R.id.btnLista);
        Button btnFiltro = findViewById(R.id.btnFiltro);
        Button btnEstatisticas = findViewById(R.id.btnEstatisticas);
        Button btnSobre = findViewById(R.id.btnSobre);

        // Quando clicar em "Novo Chamado", vai abrir a tela de cadastro
        btnCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadastroActivity.class);
            startActivity(intent);
        });

        // Quando clicar em "Ver Todos", vai abrir a lista
        btnLista.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaActivity.class);
            startActivity(intent);
        });

        // Quando clicar em "Filtrar", vai abrir a tela de filtro
        btnFiltro.setOnClickListener(v -> {
            Intent intent = new Intent(this, FiltroActivity.class);
            startActivity(intent);
        });

        btnEstatisticas.setOnClickListener(v -> {
            Intent intent = new Intent(this, EstatisticasActivity.class);
            startActivity(intent);
        });

        btnSobre.setOnClickListener(v -> {
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
        });

    }
}
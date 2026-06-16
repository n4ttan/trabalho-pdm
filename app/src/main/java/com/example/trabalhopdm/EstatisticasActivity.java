package com.example.trabalhopdm;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EstatisticasActivity extends AppCompatActivity {

    TextView tvTotal, tvAbertos, tvEmAtendimento, tvConcluidos;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        tvTotal = findViewById(R.id.tvTotal);
        tvAbertos = findViewById(R.id.tvAbertos);
        tvEmAtendimento = findViewById(R.id.tvEmAtendimento);
        tvConcluidos = findViewById(R.id.tvConcluidos);

        dbHelper = new DBHelper(this);
    }

    // Atualiza sempre que a tela aparece (caso novos chamados tenham sido criados). O onCreate não roda de novo (a tela já existe na memória, aí não atualiza)
    @Override
    protected void onResume() {
        super.onResume();
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        int total = dbHelper.contarTotal();
        int abertos = dbHelper.contarPorStatus("Aberto");
        int emAtendimento = dbHelper.contarPorStatus("Em Atendimento");
        int concluidos = dbHelper.contarPorStatus("Concluído");

        tvTotal.setText(String.valueOf(total));
        tvAbertos.setText(String.valueOf(abertos));
        tvEmAtendimento.setText(String.valueOf(emAtendimento));
        tvConcluidos.setText(String.valueOf(concluidos));
    }
}
package com.example.trabalhopdm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FiltroActivity extends AppCompatActivity {

    Spinner spinnerFiltroStatus;
    EditText etFiltroData;
    Button btnFiltrar;
    ListView listViewFiltrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        spinnerFiltroStatus = findViewById(R.id.spinnerFiltroStatus);
        etFiltroData = findViewById(R.id.etFiltroData);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        listViewFiltrado = findViewById(R.id.listViewFiltrado);

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
        try {
            SharedPreferences prefs = getSharedPreferences("chamados", MODE_PRIVATE);
            String json = prefs.getString("lista", "[]");
            JSONArray lista = new JSONArray(json);

            String statusFiltro = spinnerFiltroStatus.getSelectedItem().toString();
            String dataFiltro = etFiltroData.getText().toString().trim();

            ArrayList<JSONObject> resultado = new ArrayList<>();

            for (int i = 0; i < lista.length(); i++) {
                JSONObject chamado = lista.getJSONObject(i);
                String status = chamado.getString("status");
                String data = chamado.getString("data");

                // Verifica filtro de status
                boolean passouStatus = statusFiltro.equals("Todos") || status.equals(statusFiltro);

                // Verifica filtro de data (se preenchido)
                boolean passouData = dataFiltro.isEmpty() || data.equals(dataFiltro);

                if (passouStatus && passouData) {
                    resultado.add(chamado);
                }
            }

            // Reutiliza o mesmo adapter da ListaActivity
            FiltroAdapter filtroAdapter = new FiltroAdapter(this, resultado);
            listViewFiltrado.setAdapter(filtroAdapter);

            // Ao clicar abre atendimento
            listViewFiltrado.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    JSONObject chamado = resultado.get(position);
                    Intent intent = new Intent(this, AtendimentoActivity.class);
                    intent.putExtra("chamadoId", chamado.getString("id"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Adapter para a lista filtrada — igual ao da ListaActivity
    class FiltroAdapter extends ArrayAdapter<JSONObject> {

        ArrayList<JSONObject> itens;

        FiltroAdapter(Context context, ArrayList<JSONObject> itens) {
            super(context, 0, itens);
            this.itens = itens;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_chamado, parent, false);
            }

            try {
                JSONObject chamado = itens.get(position);

                TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
                TextView tvStatus = convertView.findViewById(R.id.tvStatus);
                TextView tvTipo = convertView.findViewById(R.id.tvTipo);
                TextView tvLocal = convertView.findViewById(R.id.tvLocal);
                TextView tvData = convertView.findViewById(R.id.tvData);

                tvTitulo.setText(chamado.getString("titulo"));
                tvTipo.setText("Tipo: " + chamado.getString("tipo"));
                tvLocal.setText("Local: " + chamado.getString("local"));
                tvData.setText("Data: " + chamado.getString("data"));

                String status = chamado.getString("status");
                tvStatus.setText(status);

                switch (status) {
                    case "Aberto":
                        tvStatus.setBackgroundColor(0xFF43A047);
                        break;
                    case "Em Atendimento":
                        tvStatus.setBackgroundColor(0xFFFB8C00);
                        break;
                    case "Concluído":
                        tvStatus.setBackgroundColor(0xFF757575);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }
}
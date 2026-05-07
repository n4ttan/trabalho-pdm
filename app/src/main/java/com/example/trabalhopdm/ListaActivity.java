package com.example.trabalhopdm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        listView = findViewById(R.id.listViewChamados);
        carregarLista();
    }

    // Recarrega a lista toda vez que voltar para essa tela
    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    private void carregarLista() {
        SharedPreferences prefs = getSharedPreferences("chamados", MODE_PRIVATE);
        String json = prefs.getString("lista", "[]");

        ArrayList<JSONObject> chamados = new ArrayList<>();

        try {
            JSONArray lista = new JSONArray(json);
            for (int i = 0; i < lista.length(); i++) {
                chamados.add(lista.getJSONObject(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Adapter customizado para usar nosso item_chamado.xml
        ChamadoAdapter adapter = new ChamadoAdapter(this, chamados);
        listView.setAdapter(adapter);

        // Ao clicar num chamado, abre a tela de atendimento
        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                JSONObject chamado = chamados.get(position);
                Intent intent = new Intent(this, AtendimentoActivity.class);
                // Passa o ID do chamado para a próxima tela
                intent.putExtra("chamadoId", chamado.getString("id"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Classe interna — adapter customizado para a lista
    class ChamadoAdapter extends ArrayAdapter<JSONObject> {

        ArrayList<JSONObject> itens;

        ChamadoAdapter(Context context, ArrayList<JSONObject> itens) {
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

                // Muda a cor do status conforme o valor
                switch (status) {
                    case "Aberto":
                        tvStatus.setBackgroundColor(0xFF43A047); // verde
                        break;
                    case "Em Atendimento":
                        tvStatus.setBackgroundColor(0xFFFB8C00); // laranja
                        break;
                    case "Concluído":
                        tvStatus.setBackgroundColor(0xFF757575); // cinza
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }
}
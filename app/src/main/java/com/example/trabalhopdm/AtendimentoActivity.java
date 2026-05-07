package com.example.trabalhopdm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class AtendimentoActivity extends AppCompatActivity {

    TextView tvTitulo, tvLocal, tvDescricao, tvTipo;
    EditText etSolucao;
    Spinner spinnerStatus;
    Button btnAtualizar;

    String chamadoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento);

        // Conecta componentes
        tvTitulo = findViewById(R.id.tvTitulo);
        tvLocal = findViewById(R.id.tvLocal);
        tvDescricao = findViewById(R.id.tvDescricao);
        tvTipo = findViewById(R.id.tvTipo);
        etSolucao = findViewById(R.id.etSolucao);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnAtualizar = findViewById(R.id.btnAtualizar);

        // Popula o Spinner de status
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Aberto", "Em Atendimento", "Concluído"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Recupera o ID passado pela tela anterior
        chamadoId = getIntent().getStringExtra("chamadoId");

        // Carrega os dados do chamado na tela
        carregarChamado();

        btnAtualizar.setOnClickListener(v -> atualizarChamado());
    }

    private void carregarChamado() {
        try {
            SharedPreferences prefs = getSharedPreferences("chamados", MODE_PRIVATE);
            String json = prefs.getString("lista", "[]");
            JSONArray lista = new JSONArray(json);

            // Procura o chamado pelo ID
            for (int i = 0; i < lista.length(); i++) {
                JSONObject chamado = lista.getJSONObject(i);
                if (chamado.getString("id").equals(chamadoId)) {

                    // Preenche os campos com os dados do chamado
                    tvTitulo.setText(chamado.getString("titulo"));
                    tvLocal.setText(chamado.getString("local"));
                    tvDescricao.setText(chamado.getString("descricao"));
                    tvTipo.setText(chamado.getString("tipo"));
                    etSolucao.setText(chamado.getString("solucao"));

                    // Seleciona o status atual no Spinner
                    String status = chamado.getString("status");
                    ArrayAdapter adapter = (ArrayAdapter) spinnerStatus.getAdapter();
                    int pos = adapter.getPosition(status);
                    spinnerStatus.setSelection(pos);

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizarChamado() {
        try {
            SharedPreferences prefs = getSharedPreferences("chamados", MODE_PRIVATE);
            String json = prefs.getString("lista", "[]");
            JSONArray lista = new JSONArray(json);

            // Procura e atualiza o chamado pelo ID
            for (int i = 0; i < lista.length(); i++) {
                JSONObject chamado = lista.getJSONObject(i);
                if (chamado.getString("id").equals(chamadoId)) {
                    chamado.put("solucao", etSolucao.getText().toString().trim());
                    chamado.put("status", spinnerStatus.getSelectedItem().toString());
                    lista.put(i, chamado);
                    break;
                }
            }

            // Salva a lista atualizada
            prefs.edit().putString("lista", lista.toString()).apply();

            Toast.makeText(this, "Chamado atualizado!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Erro ao atualizar!", Toast.LENGTH_SHORT).show();
        }
    }
}
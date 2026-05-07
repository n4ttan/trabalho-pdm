package com.example.trabalhopdm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CadastroActivity extends AppCompatActivity {

    // Declaração dos componentes da tela
    EditText etTitulo, etLocal, etDescricao;
    Spinner spinnerTipo;
    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Conecta as variáveis aos componentes do XML pelo ID
        etTitulo = findViewById(R.id.etTitulo);
        etLocal = findViewById(R.id.etLocal);
        etDescricao = findViewById(R.id.etDescricao);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Popula o Spinner com as opções de tipo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"TI", "Infraestrutura"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // Ação do botão salvar
        btnSalvar.setOnClickListener(v -> salvarChamado());
    }

    private void salvarChamado() {
        // Pega o texto digitado pelo usuário
        String titulo = etTitulo.getText().toString().trim();
        String local = etLocal.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();

        // Validação — não deixa salvar campos vazios
        if (titulo.isEmpty() || local.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gera data atual automaticamente
        String data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        // Cria o objeto JSON do chamado
        try {
            JSONObject chamado = new JSONObject();
            chamado.put("id", UUID.randomUUID().toString());
            chamado.put("titulo", titulo);
            chamado.put("local", local);
            chamado.put("descricao", descricao);
            chamado.put("tipo", tipo);
            chamado.put("status", "Aberto");
            chamado.put("data", data);
            chamado.put("solucao", "");

            // Recupera a lista existente do SharedPreferences
            SharedPreferences prefs = getSharedPreferences("chamados", MODE_PRIVATE);
            String json = prefs.getString("lista", "[]");
            JSONArray lista = new JSONArray(json);

            // Adiciona o novo chamado à lista
            lista.put(chamado);

            // Salva a lista atualizada
            prefs.edit().putString("lista", lista.toString()).apply();

            Toast.makeText(this, "Chamado salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Volta para a tela anterior

        } catch (Exception e) {
            Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
        }
    }
}
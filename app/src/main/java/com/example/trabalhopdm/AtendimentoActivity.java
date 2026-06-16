package com.example.trabalhopdm;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AtendimentoActivity extends AppCompatActivity {

    TextView tvTitulo, tvLocal, tvDescricao, tvTipo;
    EditText etSolucao;
    Spinner spinnerStatus;
    Button btnAtualizar;

    String chamadoId;
    DBHelper dbHelper;

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

        dbHelper = new DBHelper(this);

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
        Cursor cursor = dbHelper.listarChamados();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_ID));

            if (id.equals(chamadoId)) {
                tvTitulo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITULO)));
                tvLocal.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_LOCAL)));
                tvDescricao.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DESCRICAO)));
                tvTipo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TIPO)));
                etSolucao.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_SOLUCAO)));

                // Seleciona o status atual no Spinner
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_STATUS));
                ArrayAdapter spinnerAdapter = (ArrayAdapter) spinnerStatus.getAdapter();
                int pos = spinnerAdapter.getPosition(status);
                spinnerStatus.setSelection(pos);

                break;
            }
        }
        cursor.close();
    }

    private void atualizarChamado() {
        String solucao = etSolucao.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        dbHelper.atualizarChamado(chamadoId, solucao, status);

        Toast.makeText(this, "Chamado atualizado!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
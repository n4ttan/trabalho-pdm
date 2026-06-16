package com.example.trabalhopdm;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CadastroActivity extends AppCompatActivity {

    // Declaração dos componentes da tela
    EditText etTitulo, etLocal, etDescricao;
    Spinner spinnerTipo;
    Button btnSalvar, btnEscolherImagem;
    ImageView imgPreview;

    // Guarda o caminho da imagem escolhida
    Uri imagemSelecionada;

    // "Ouvinte" que espera o resultado da galeria
    ActivityResultLauncher<String> galeriaLauncher;

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
        btnEscolherImagem = findViewById(R.id.btnEscolherImagem);
        imgPreview = findViewById(R.id.imgPreview);

        // Popula o Spinner com as opções de tipo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"TI", "Infraestrutura"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // Configura o "ouvinte" da galeria — roda quando o usuário escolhe uma imagem
        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imagemSelecionada = uri;
                        imgPreview.setImageURI(uri);
                    }
                }
        );

        // Ao clicar, abre a galeria pedindo apenas imagens
        btnEscolherImagem.setOnClickListener(v -> galeriaLauncher.launch("image/*"));

        // Ação do botão salvar
        btnSalvar.setOnClickListener(v -> salvarChamado());
    }

    private void salvarChamado() {
        String titulo = etTitulo.getText().toString().trim();
        String local = etLocal.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();

        if (titulo.isEmpty() || local.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        String data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String id = UUID.randomUUID().toString();

        // Copia a imagem para o armazenamento interno do app
        String caminhoImagem = "";
        if (imagemSelecionada != null) {
            caminhoImagem = copiarImagem(imagemSelecionada, id);
        }

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.inserirChamado(id, titulo, local, descricao, tipo, "Aberto", data, "", caminhoImagem);

        // Envia para a nuvem
        salvarNaNuvem(id, titulo, local, descricao, "Aberto", data, caminhoImagem);

        Toast.makeText(this, "Chamado salvo com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Copia a imagem da galeria para o armazenamento interno do app
    private String copiarImagem(Uri uri, String id) {
        try {
            java.io.InputStream inputStream = getContentResolver().openInputStream(uri);
            java.io.File destino = new java.io.File(getFilesDir(), "img_" + id + ".jpg");
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(destino);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return destino.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private void salvarNaNuvem(String id, String titulo, String local, String descricao,
                               String status, String data, String imagem) {
        com.parse.ParseObject chamado = new com.parse.ParseObject("Chamado");
        chamado.put("objectId_local", id);
        chamado.put("titulo", titulo);
        chamado.put("local", local);
        chamado.put("descricao", descricao);
        chamado.put("status", status);
        chamado.put("dataCadastro", data);
        chamado.put("nomeImagem", imagem.isEmpty() ? "sem_imagem" : new java.io.File(imagem).getName());

        chamado.saveInBackground(e -> {
            if (e == null) {
                android.util.Log.d("Back4App", "Chamado salvo na nuvem com sucesso!");
            } else {
                android.util.Log.e("Back4App", "Erro ao salvar na nuvem: " + e.getMessage());
            }
        });
    }
}
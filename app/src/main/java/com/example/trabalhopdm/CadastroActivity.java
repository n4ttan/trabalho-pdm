package com.example.trabalhopdm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CadastroActivity extends AppCompatActivity {

    EditText etTitulo, etLocal, etDescricao;
    Spinner spinnerTipo;
    Button btnSalvar, btnEscolherImagem;
    ImageView imgPreview;

    String caminhoImagemAtual = "";
    ActivityResultLauncher<Uri> cameraLauncher;
    Uri fotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etTitulo = findViewById(R.id.etTitulo);
        etLocal = findViewById(R.id.etLocal);
        etDescricao = findViewById(R.id.etDescricao);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnEscolherImagem = findViewById(R.id.btnEscolherImagem);
        imgPreview = findViewById(R.id.imgPreview);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"TI", "Infraestrutura"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // Configura o launcher da câmera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                sucesso -> {
                    if (sucesso) {
                        // Mostra a foto tirada no preview
                        Bitmap bitmap = BitmapFactory.decodeFile(caminhoImagemAtual);
                        imgPreview.setImageBitmap(bitmap);
                    }
                }
        );

        btnEscolherImagem.setOnClickListener(v -> abrirCamera());
        btnSalvar.setOnClickListener(v -> salvarChamado());
    }

    private void abrirCamera() {
        try {
            // Cria um arquivo temporário para salvar a foto
            File fotoFile = criarArquivoFoto();
            caminhoImagemAtual = fotoFile.getAbsolutePath();

            // Converte o caminho do arquivo para Uri usando FileProvider
            fotoUri = FileProvider.getUriForFile(
                    this,
                    getApplicationContext().getPackageName() + ".provider",
                    fotoFile
            );

            cameraLauncher.launch(fotoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao abrir câmera!", Toast.LENGTH_SHORT).show();
        }
    }

    private File criarArquivoFoto() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nomeArquivo = "FOTO_" + timestamp;
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nomeArquivo, ".jpg", dir);
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

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.inserirChamado(id, titulo, local, descricao, tipo, "Aberto", data, "", caminhoImagemAtual);

        salvarNaNuvem(id, titulo, local, descricao, "Aberto", data, caminhoImagemAtual);

        Toast.makeText(this, "Chamado salvo com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
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
        chamado.put("nomeImagem", imagem.isEmpty() ? "sem_imagem" : new File(imagem).getName());

        chamado.saveInBackground(e -> {
            if (e == null) {
                android.util.Log.d("Back4App", "Chamado salvo na nuvem!");
            } else {
                android.util.Log.e("Back4App", "Erro: " + e.getMessage());
            }
        });
    }
}
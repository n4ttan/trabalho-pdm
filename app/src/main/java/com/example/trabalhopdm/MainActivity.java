package com.example.trabalhopdm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conecta componentes
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        // Define a Toolbar como ActionBar (aparece o ícone ☰)
        setSupportActionBar(toolbar);

        // Conecta o DrawerLayout à Toolbar — cria o botão hamburguer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Força o ícone ☰ para branco
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(android.graphics.Color.WHITE);
        }

        // Navegação pelo menu lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_estatisticas) {
                startActivity(new Intent(this, EstatisticasActivity.class));
            } else if (id == R.id.nav_sobre) {
                startActivity(new Intent(this, SobreActivity.class));
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Botões da tela inicial
        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        Button btnLista = findViewById(R.id.btnLista);
        Button btnFiltro = findViewById(R.id.btnFiltro);

        btnCadastrar.setOnClickListener(v -> startActivity(new Intent(this, CadastroActivity.class)));
        btnLista.setOnClickListener(v -> startActivity(new Intent(this, ListaActivity.class)));
        btnFiltro.setOnClickListener(v -> startActivity(new Intent(this, FiltroActivity.class)));
    }
}
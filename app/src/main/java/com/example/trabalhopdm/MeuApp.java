package com.example.trabalhopdm;

import android.app.Application;
import com.parse.Parse;

public class MeuApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializa o Parse com as chaves do Back4App
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("3kLFWrrddAQsiAvpb26RJqhw4G8SnRrnFUht4hYp")
                .clientKey("4smUUFANNt2jFpqW3OC2FMJAsAkdLCFTOl7Si1QV")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
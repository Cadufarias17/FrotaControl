package edu.ifsul.pdm.frotacontrolfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SecretariaLC_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretaria_lc);
    }

    public void AbrirTelaLoginSecretaria(View view) {
        Intent i = new Intent(this, LoginSecretariaActivity.class);
        startActivity(i);
    }

    public void AbrirTelaCadastroSecretaria(View view) {
        Intent i = new Intent(this, CadastroSecretariaActivity.class);
        startActivity(i);
    }

    public void Voltar(View view) {
        finish();
    }
}

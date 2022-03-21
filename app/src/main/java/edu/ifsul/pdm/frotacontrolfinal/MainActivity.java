package edu.ifsul.pdm.frotacontrolfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void AbrirTelaPassageiro(View view) {
        Intent i = new Intent(this, PassageiroLC_Activity.class);
        startActivity(i);
    }

    public void AbrirTelaSecretaria(View view) {
        Intent i = new Intent(this, SecretariaLC_Activity.class);
        startActivity(i);
    }

    public void AbrirTelaSobre(View view) {
        Intent i = new Intent(this, SobreActivity.class);
        startActivity(i);
    }

    public void Sair(View view) {
        finish();
    }
}

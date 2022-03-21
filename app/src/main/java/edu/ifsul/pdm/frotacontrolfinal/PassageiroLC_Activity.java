package edu.ifsul.pdm.frotacontrolfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PassageiroLC_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiro_lc);
    }

    public void AbrirTelaLoginPassageiro(View view) {
        Intent i = new Intent(this, LoginPassageiroActivity.class);
        startActivity(i);
    }

    public void AbrirTelaCadastroPassageiro(View view) {
        Intent i = new Intent(this, CadastroPassageiroActivity.class);
        startActivity(i);
    }

    public void Voltar(View view) {
        finish();
    }
}

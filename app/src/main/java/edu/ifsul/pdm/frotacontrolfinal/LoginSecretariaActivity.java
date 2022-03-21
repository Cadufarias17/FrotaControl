package edu.ifsul.pdm.frotacontrolfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.ifsul.pdm.frotacontrolfinal.veiculo.Veiculo;
import edu.ifsul.pdm.frotacontrolfinal.veiculo.VeiculoListActivity;

public class LoginSecretariaActivity extends AppCompatActivity {

    private EditText login, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_secretaria);

        login = findViewById(R.id.editTextLoginSecretaria);
        senha = findViewById(R.id.editTextSenhaSecretaria);

        SharedPreferences preferencias = getSharedPreferences(
                "user_preferences", // 1 - chave das preferencias buscadas
                MODE_PRIVATE);      // 2 - modo de acesso
        int idDoArigoh = preferencias.getInt("idSec",-1);
        if(idDoArigoh > 0){
            // abre outra activity
            Intent it = new Intent(getBaseContext(), VeiculoListActivity.class);
            startActivity(it);
        }
    }

    public void LogarSecretaria(View v) {
        String valor1=login.getText().toString();
        String valor2=senha.getText().toString();

        // Cria uma fila para envio de mensagens por Volley
        RequestQueue filaEnviadoraDeMensagens = Volley.newRequestQueue(this);

        //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig. Procurar por IPv4
        String urlServidor = "http:/192.168.107.11/cadu/ws_loginSecretaria.php";

        // parâmetros para enviar por POST usando um Map
        final Map<String, String> parametrosPOST = new HashMap<>();

        // adiciona-se os parâmetros por chave -> valor
        parametrosPOST.put("login",String.valueOf(valor1));
        parametrosPOST.put("senha",String.valueOf(valor2));

        // cria a requisição de mensagem e tratamento de resposta
        StringRequest requisicao = new StringRequest(

                Request.Method.POST, // 1 - Método usado para enviar mensagem
                urlServidor,         // 2 - Endereço do servidor
                new Response.Listener<String>() { // 3 - Objeto para tratar resposta
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG_LOGIN","[Res: "+response+"]");

                        try {
                            JSONObject o = new JSONObject(response);
                            if(o.getString("resposta").equals("OK")){
                                // exibe uma mensagem de bem vindo
                                Toast.makeText(
                                        getBaseContext(),
                                        "Login em " + o.getString("nome") + "!",
                                        Toast.LENGTH_LONG).show();
                                // coloca id do usuário na seção (SharedPreferences)
                                // obter um arquivo de preferências
                                SharedPreferences preferencias = getSharedPreferences(
                                        "user_preferences", // 1 - chave das preferencias buscadas
                                        MODE_PRIVATE);      // 2 - modo de acesso

                                // para adicionar algo nas preferências é necessário usar um editor
                                SharedPreferences.Editor editor = preferencias.edit();
                                // Valores a serem adicionados ou alterados
                                editor.putInt(
                                        "idSec", // 1 - Chave
                                        o.getInt("id"));          // 2 - Valor
                                        // comando para salvar os dados alterados
                                editor.apply();

                                // abre outra activity
                                Intent it = new Intent(getBaseContext(), VeiculoListActivity.class);
                                startActivity(it);

                            } else {
                                // converte a resposta em Inteiro
                                // int resultado = Integer.parseInt(response);
                                // exibe o resultado na tela usando Toast
                                Toast.makeText(
                                        getBaseContext(),
                                        "Dados Incorretos",
                                        Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG_EXEMPLO",
                                    "Erro encontrado ao tentar enviar mensagem",e);
                        }
                    }
                },
                new Response.ErrorListener() { // 4 - Objeto para tratar erro
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // exibe o erro na tela e nos Logs
                        Toast.makeText(
                                getBaseContext(),
                                "Erro: "+error,
                                Toast.LENGTH_LONG).show();
                        // log de erro no LogCat
                        Log.e("TAG_EXEMPLO",
                                "Erro encontrado ao tentar enviar mensagem",error);
                    }
                }){
            // sobrescrever método getParams para enviar dados por POST
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return parametrosPOST;
            }
        };

        // envia a mensagem ao servidor
        filaEnviadoraDeMensagens.add(requisicao);
    }
}

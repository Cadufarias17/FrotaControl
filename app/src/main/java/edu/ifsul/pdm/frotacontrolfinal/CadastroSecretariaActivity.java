package edu.ifsul.pdm.frotacontrolfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class CadastroSecretariaActivity extends AppCompatActivity {

    // declarar os campos que serão utilizados
    EditText nome, senha, confirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_secretaria);
        // relacionar as variaveis com os campos do xml
        nome = findViewById(R.id.editTextNomeSecretaria);
        senha = findViewById(R.id.editTextSenhaSecretaria);
        confirmarSenha = findViewById(R.id.editTextConfirmarSenhaSecretaria);
    }

    public void CadastrarSecretaria(View view) {

        String valor1 = nome.getText().toString();
        String valor2 = senha.getText().toString();
        String valor3 = confirmarSenha.getText().toString();

        // conferir se as senhas estão iguais
        if (valor2.equals(valor3)) {
            // Cria uma fila para envio de mensagens por Volley
            RequestQueue filaEnviadoraDeMensagens = Volley.newRequestQueue(this);

            //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig. Procurar por IPv4
            String urlServidor = "http:/192.168.107.11/cadu/ws_cadastroSecretaria.php";

            // parâmetros para enviar por POST usando um Map
            final Map<String, String> parametrosPOST = new HashMap<>();

            // adiciona-se os parâmetros por chave -> valor
            parametrosPOST.put("nome", String.valueOf(valor1));
            parametrosPOST.put("senha", String.valueOf(valor2));

            // cria a requisição de mensagem e tratamento de resposta
            StringRequest requisicao = new StringRequest(

                    Request.Method.POST, // 1 - Método usado para enviar mensagem
                    urlServidor,         // 2 - Endereço do servidor
                    new Response.Listener<String>() { // 3 - Objeto para tratar resposta
                        @Override
                        public void onResponse(String response) {
                            Log.d("DEBUG_CADASTRO","[Res: "+response+"]");

                            try {
                                JSONObject o = new JSONObject(response);
                                if(o.getString("resposta").equals("OK")){
                                    // exibe uma mensagem de bem vindo
                                    Toast.makeText(
                                            getBaseContext(),
                                            "Cadastrado o id: " + o.getInt("id") + "!",
                                            Toast.LENGTH_LONG).show();
                                    // abre a activity de login do passageiro
                                    Intent it = new Intent(getBaseContext(), LoginSecretariaActivity.class);
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
}

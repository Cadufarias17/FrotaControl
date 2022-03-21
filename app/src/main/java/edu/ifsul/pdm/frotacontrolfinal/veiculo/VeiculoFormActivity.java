package edu.ifsul.pdm.frotacontrolfinal.veiculo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import edu.ifsul.pdm.frotacontrolfinal.R;

public class VeiculoFormActivity extends AppCompatActivity {

    // objetos para interagir com a tela
    EditText editTextPlaca, editTextLugares;
    // objeto da pessoa
    private Veiculo veiculo = null;
    // Declarar objeto
    private RequestQueue filaEnviadoraDeMensagens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo_form);

        // adiciona botão de voltar (opcional)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão voltar (seta)
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão para voltar

        // vinculações
        editTextPlaca = findViewById(R.id.editTextPlaca);
        editTextLugares = findViewById(R.id.editTextLugares);

        // instanciar Fila para Envio de Mensagens
        filaEnviadoraDeMensagens = Volley.newRequestQueue(this);

        // métodos para Editar
        Intent it = getIntent();
        // pega o id para edição

        int idEdicao = it.getIntExtra("idEdicao",-1);

        // edição
        if(idEdicao > -1){
            // busca para edição
            try {
                buscarUm(idEdicao, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            veiculo = new Veiculo();
                            // atualiza valores
                            veiculo.setId(response.getInt("id_VEICULO"));
                            veiculo.setPlaca(response.getString("placa_VEICULO"));
                            veiculo.setNumLugares(response.getInt("numLugares_VEICULO"));
                            veiculo.setOcupado(response.getInt("ocupado_VEICULO"));
                            veiculo.setIdSec(response.getInt("id_SEC"));

                            // mostra na tela
                            // adiciona dados nas Views
                            editTextPlaca.setText(veiculo.getPlaca());
                            editTextLugares.setText(String.valueOf(veiculo.getNumLugares()));
                            //adicionar também 'ocupado' e 'idSec'

                        } catch (JSONException e) {
                            Log.e("TAG_DEBUG_EDITAR_CARRO","Erro ao converter",e);
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void salvar() throws JSONException {
        // pegar dados da tela
        String placa = editTextPlaca.getText().toString();
        String lugares = editTextLugares.getText().toString();
        // valida campos
        if(!validaCampos(placa,lugares)){
            return;
        }
        // edição e inclusão
        if(veiculo == null){
            veiculo = new Veiculo();
            veiculo.setPlaca(placa);
            veiculo.setNumLugares(Integer.parseInt(lugares));

            // cria um objeto  para colocar os dados do produto
            JSONObject dados = new JSONObject();
            try {
                dados.put("placa",veiculo.getPlaca());
                dados.put("lugares",veiculo.getNumLugares());
                // obter um arquivo de preferências
                SharedPreferences preferencias = getSharedPreferences(
                        "user_preferences", // 1 - chave das preferencias buscadas
                        MODE_PRIVATE);      // 2 - modo de acesso
                int resultado = preferencias.getInt(
                        "idSec",           // 1 - Chave
                        0);          // 2 - Valor padrão se chave não existir
                // acessar um valor nas Preferências
                Log.d("TESTANDO_PREFERENCES","IdSec: "+resultado);

                dados.put("idSec", resultado);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // endereço do arquivo webservice no servidor
            //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig. Procurar por IPv4
            String urlServidor = "http://192.168.107.11/cadu/veiculo/ws_inserir.php";
            // requisição
            JsonObjectRequest requisicao = new JsonObjectRequest(
                    Request.Method.POST, // método de envio
                    urlServidor,        // url do servidor
                    dados,               // dados a serem enviados
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("resposta").equals("OK")){
                                    // informa o usuário que salvou com sucesso
                                    Toast.makeText(
                                            getBaseContext(),    // 1 - Contexto
                                            "Carro incluído com sucesso!",// 2 - mensagem
                                            Toast.LENGTH_SHORT)     // 3 - duração
                                            .show();
                                } else {
                                    // informa o usuário que houve erro
                                    Toast.makeText(
                                            getBaseContext(),    // 1 - Contexto
                                            "Erro ao incluir carro!",    // 2 - mensagem
                                            Toast.LENGTH_SHORT)    // 3 - duração
                                            .show();
                                }
                                // deixa objeto null
                                veiculo = null;
                            } catch (JSONException e) {
                                Log.e("TAG_DEBUG","Erro ao converter",e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG_DEBUG","Erro de comunicação",error);
                            // informa o usuário que houve erro
                            Toast.makeText(
                                    getBaseContext(),    // 1 - Contexto
                                    "Erro de comunicação!",    // 2 - mensagem
                                    Toast.LENGTH_SHORT)    // 3 - duração
                                    .show();
                            // deixa objeto null
                            veiculo = null;
                        }
                    }
            );
            // envia a mensagem
            filaEnviadoraDeMensagens.add(requisicao);

        } else {
            // atualiza dados do produto
            veiculo.setPlaca(placa);
            veiculo.setNumLugares(Integer.parseInt(lugares));

            // cria um objeto  para colocar os dados do produto
            JSONObject dados = new JSONObject();

            dados.put("id", veiculo.getId());
            dados.put("placa", veiculo.getPlaca());
            dados.put("lugares", veiculo.getNumLugares());
            dados.put("ocupado", veiculo.getOcupado());
            dados.put("idSec", veiculo.getIdSec());

            // endereço do arquivo webservice no servidor
            //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig.
            //Procurar por IPv4
            String urlServidor = "http://192.168.107.11/cadu/veiculo/ws_editar.php";
            // requisição
            JsonObjectRequest requisicao = new JsonObjectRequest(
                    Request.Method.POST, // método de envio
                    urlServidor,        // url do servidor
                    dados,               // dados a serem enviados
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("resposta").equals("OK")){
                                    // informa o usuário que salvou com sucesso
                                    Toast.makeText(
                                            getBaseContext(),    // 1 - Contexto
                                            "Editado com sucesso!",// 2 - mensagem
                                            Toast.LENGTH_SHORT)    // 3 - duração
                                            .show();
                                } else {
                                    // informa o usuário que houve erro
                                    Toast.makeText(
                                            getBaseContext(),   // 1 - Contexto
                                            "Erro ao editar!",    // 2 - mensagem
                                            Toast.LENGTH_SHORT)   // 3 - duração
                                            .show();
                                }
                            } catch (JSONException e) {
                                Log.e("TAG_DEBUG","Erro ao converter",e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG_DEBUG","Erro de comunicação",error);
                            // informa o usuário que houve erro
                            Toast.makeText(
                                    getBaseContext(),     // 1 - Contexto
                                    "Erro de comunicação!", // 2 - mensagem
                                    Toast.LENGTH_SHORT)     // 3 - duração
                                    .show();
                        }
                    }
            );
            // envia a mensagem
            filaEnviadoraDeMensagens.add(requisicao);
        }
    }

    public void excluir(){
        // Se for inclusão não permite exclusão
        if (veiculo == null) {
            Toast.makeText(
                    getBaseContext(),
                    "O registro ainda não foi inserido!",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Cria mensagem de alerta
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    VeiculoFormActivity.this, // 1 - Activity que irá executar o Alerta
                    R.style.Theme_AppCompat_DayNight_Dialog); // 2 - Estilo do Alerta
            // Adiciona texto do Título
            builder.setTitle("Exclusão de registro");
            // Adiciona mensagem do Alerta
            builder.setMessage("Você realmente deseja excluir esse produto?");
            // Adiciona botão, se null apesas fecha o alerta
            builder.setNegativeButton("Não", null);
            // adiciona botão, evento onClick adicionado
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // excluir Registro no banco de dados
                    try {
                        excluir(veiculo.getId(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getString("resposta").equals("OK")){
                                        // informa o usuário que salvou com sucesso
                                        Toast.makeText(
                                                getBaseContext(),       // 1 - Contexto
                                                "Excluído com sucesso!",// 2 - mensagem
                                                Toast.LENGTH_SHORT)     // 3 - duração
                                                .show();
                                        // finalizar a Activity
                                        finish();
                                    } else {
                                        // informa o usuário que houve erro
                                        Toast.makeText(
                                                getBaseContext(),      // 1 - Contexto
                                                "Erro ao excluir!",    // 2 - mensagem
                                                Toast.LENGTH_SHORT)    // 3 - duração
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    Log.e("TAG_DEBUG","Erro ao converter",e);
                                }

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // exibe mensagem de sucesso
                    Toast.makeText(
                            getBaseContext(),
                            "Produto Excluída com Sucesso!",
                            Toast.LENGTH_SHORT);
                    // fecha a tela
                    finish();
                }
            });
            // exibe o Alerta na tela
            builder.show();
        }
    }

    public void excluir(int id, Response.Listener<JSONObject> trataResposta) throws JSONException {
        // cria um objeto  para colocar os dados do produto
        JSONObject dados = new JSONObject();
        dados.put("id",id);
        // endereço do arquivo webservice no servidor
        // Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig.
        // Procurar por IPv4
        String urlServidor = "http://192.168.107.11/cadu/veiculo/ws_excluir.php";
        // requisição
        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.POST, // método de envio
                urlServidor,         // url do servidor
                dados,               // dados a serem enviados
                trataResposta,       // tratador da resposta do servidor
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG_DEBUG","Erro de comunicação",error);
                        // informa o usuário que houve erro
                        Toast.makeText(
                                getBaseContext(),     // 1 - Contexto
                                "Erro de comunicação!", // 2 - mensagem
                                Toast.LENGTH_SHORT)     // 3 - duração
                                .show();
                    }
                }
        );
        // envia a mensagem
        filaEnviadoraDeMensagens.add(requisicao);
    }

    public boolean validaCampos(String nome, String preco){
        if(nome.isEmpty()){
            editTextPlaca.setError("Este campo é obrigatório!");
            editTextPlaca.requestFocus();
            return false;
        }
        if(preco.isEmpty()){
            editTextLugares.setError("Este campo é obrigatório");
            editTextLugares.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            case R.id.opcao_salvar:
                //salvar
                try {
                    salvar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.opcao_excluir:
                //salvar
                excluir();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buscarUm(int idEdicao, Response.Listener<JSONObject> trataResposta) throws JSONException {
        // cria um objeto  para colocar os dados do produto
        JSONObject dados = new JSONObject();
        dados.put("id",idEdicao);
        // endereço do arquivo webservice no servidor
        // Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig.
        // Procurar por IPv4
        String urlServidor = "http://192.168.107.11/cadu/veiculo/ws_busca_um.php";
        // requisição
        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.POST, // 1 - método de envio
                urlServidor,         // 2 - url do servidor
                dados,               // 3 - dados a serem enviados
                trataResposta,       // 4 - objeto para tratar resposta com sucesso
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG_DEBUG_BUSCA_UM","Erro de comunicação",error);
                        // informa o usuário que houve erro
                        Toast.makeText(
                                getBaseContext(),    // 1 - Contexto
                                "Erro de comunicação!",// 2 - mensagem
                                Toast.LENGTH_SHORT)    // 3 - duração
                                .show();
                    }
                }
        );
        // envia a mensagem
        filaEnviadoraDeMensagens.add(requisicao);
    }
}

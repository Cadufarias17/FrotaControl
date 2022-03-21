package edu.ifsul.pdm.frotacontrolfinal.veiculo;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ifsul.pdm.frotacontrolfinal.R;
import edu.ifsul.pdm.frotacontrolfinal.utils.OnClickItemDaLista;

public class VeiculoListActivity extends AppCompatActivity {

    // lista de produtos
    public ArrayList<Veiculo> veiculos;
    // adaptador
    public AdaptadorVeiculos adaptadorVeiculos;
    // recycler view
    public RecyclerView recyclerView;
    // searchView usado no filtro
    public SearchView filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // viculação com a tela
        recyclerView = findViewById(R.id.lista);
        filtro = findViewById(R.id.filtroTitulo);

        // adiciona botão de voltar (opcional)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão voltar (seta)
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão para voltar

        // Instanciar adaptador com evento de Click
        adaptadorVeiculos = new AdaptadorVeiculos(
                getBaseContext(),
                new OnClickItemDaLista<Veiculo>() {
                    @Override
                    public void onItemClick(Veiculo item) {
                        // cria uma intent para edição
                        Intent it = new Intent(getBaseContext(),VeiculoFormActivity.class);
                        // adiciona id do item
                        it.putExtra("idEdicao",item.getId());
                        // inicia Activity
                        startActivity(it);
                    }
                }
        );
        // vincula lista do Adaptador com lista da classe
        veiculos = adaptadorVeiculos.getItens();
        // vincula RecyclerView e adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adaptadorVeiculos);


        //Define um texto (placeholder) de dica para o SearchView
        filtro.setQueryHint("Digite um nome para filtrar");
        // Evento da Busca no ActionBar
        filtro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // executa o procedimento de onResume para atualizar tela
                onResume();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filtra quando exibe mais que dois caracteres
                //if(newText.length() > 2){
                onResume();
                //}
                return false;
            }
        });
        // botão para incluir um novo item

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // abre para iniciar processo de Incluir um item
                startActivity(new Intent(getBaseContext(),VeiculoFormActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // falta fazer
        String textoDoFiltro = filtro.getQuery().toString();
        try {
            listarTodosPorFiltro(textoDoFiltro);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void listarTodosPorFiltro(String texto) throws JSONException {
        // instanciar Fila para Envio de Mensagens
        RequestQueue filaEnviadoraDeMensagens = Volley.newRequestQueue(this);

        // parâmetros para enviar por POST usando um Map
        final Map<String, String> parametrosPOST = new HashMap<>();
        // adiciona-se os parâmetros por chave -> valor
        parametrosPOST.put("filtro",texto);


        //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig.
        //Procurar por IPv4
        String urlServidor="http://192.168.107.11/cadu/veiculo/ws_listar_com_filtro.php";

        // (Opcional) se quiser verificar se a url foi montada corretamente exibir o Log no LogCat
        Log.d("TAG_EXEMPLO",urlServidor);

        // cria a requisição de mensagem e tratamento de resposta
        StringRequest requisicao = new StringRequest(
                Request.Method.POST,
                urlServidor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG_LISTA",response);
                        try {
                            JSONArray array = new JSONArray(response);
                            // limpa tela
                            adaptadorVeiculos.getItens().clear();
                            // obtém a resposta
                            for(int i = 0; i < array.length(); i++){
                                // produto para adicionar no Array
                                Veiculo p = new Veiculo();
                                // pega a linha
                                JSONObject o = (JSONObject) array.get(i);
                                try{
                                    p.setId(o.getInt("id_VEICULO"));
                                    p.setPlaca(o.getString("placa_VEICULO"));
                                    p.setNumLugares(o.getInt("numLugares_VEICULO"));
                                    p.setOcupado(o.getInt("ocupado_VEICULO"));
                                    p.setIdSec(o.getInt("id_SEC"));
                                    // adiciona a produto na lista
                                    adaptadorVeiculos.getItens().add(p);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            // informa a tela que os dados mudaram
                            adaptadorVeiculos.notifyDataSetChanged();

                        } catch(org.json.JSONException e){
                            Log.d("TAG_EXEMPLO",
                                    "Erro ao converter resultado do Json: "+
                                            response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // exibe o erro na tela e nos Logs
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

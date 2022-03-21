package edu.ifsul.pdm.frotacontrolfinal.veiculo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ifsul.pdm.frotacontrolfinal.R;
import edu.ifsul.pdm.frotacontrolfinal.utils.OnClickItemDaLista;

public class AdaptadorVeiculos extends RecyclerView.Adapter<AdaptadorVeiculos.LinhaExibida> {
    // itens
    private ArrayList<Veiculo> itens;
    // contexto de execução (Activity responsável por gerar a Tela)
    private Context contexto;
    // evento para Click no Item da lista
    private OnClickItemDaLista<Veiculo> onClickItem;

    public AdaptadorVeiculos(Context contexto, OnClickItemDaLista<Veiculo> eventoDeClickNoItem) {
        this.contexto = contexto;
        this.itens = new ArrayList<>();
        this.onClickItem = eventoDeClickNoItem;
    }

    @NonNull
    @Override
    public LinhaExibida onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // instanciar uma linha com base a um Layout XML
        View linha = LayoutInflater.from(contexto)
                //        .inflate(R.layout.linha_simples_adaptador,bozo,false);
                .inflate(R.layout.linha_simples_somente_texto,parent,false);
        // retorna a linha com o ViewHolder
        return new LinhaExibida(linha);
    }

    @Override
    public void onBindViewHolder(@NonNull LinhaExibida holder, int position) {
        // pega o valor
        final Veiculo valorSelecionado = itens.get(position);
        // altera a linha exibida
        holder.texto.setText(valorSelecionado.getPlaca());
        Log.d("TESTE",valorSelecionado.getPlaca());
        // adicionar um evento na linha
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // executa código passado por parâmetro
                onClickItem.onItemClick(valorSelecionado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public ArrayList<Veiculo> getItens() {
        return itens;
    }

    class LinhaExibida extends RecyclerView.ViewHolder {

        // 1 - Componentes da linha
        public TextView texto;

        public LinhaExibida(View itemLinha){
            super(itemLinha);
            // vincula o elemento da linha com o Componente
            texto = itemLinha.findViewById(R.id.textView);
        }
    }
}

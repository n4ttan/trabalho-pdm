package com.example.trabalhopdm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.net.Uri;

import java.util.ArrayList;

public class ChamadoRecyclerAdapter extends RecyclerView.Adapter<ChamadoRecyclerAdapter.ChamadoViewHolder> {

    // Interface para "avisar" a Activity quando um item for clicado
    public interface OnItemClickListener {
        void onItemClick(ListaActivity.Chamado chamado);
    }

    private ArrayList<ListaActivity.Chamado> itens;
    private OnItemClickListener listener;

    public ChamadoRecyclerAdapter(ArrayList<ListaActivity.Chamado> itens, OnItemClickListener listener) {
        this.itens = itens;
        this.listener = listener;
    }

    // ViewHolder: guarda referências dos componentes visuais de UM item
    public static class ChamadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvStatus, tvTipo, tvLocal, tvData;
        ImageView imgMiniatura;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvLocal = itemView.findViewById(R.id.tvLocal);
            tvData = itemView.findViewById(R.id.tvData);
            imgMiniatura = itemView.findViewById(R.id.imgMiniatura);
        }
    }

    // Cria um novo ViewHolder inflando o layout do item
    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(view);
    }

    // Preenche o ViewHolder com os dados do item na posição "position"
    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        ListaActivity.Chamado c = itens.get(position);

        holder.tvTitulo.setText(c.titulo);
        holder.tvTipo.setText("Tipo: " + c.tipo);
        holder.tvLocal.setText("Local: " + c.local);
        holder.tvData.setText("Data: " + c.data);
        holder.tvStatus.setText(c.status);

        switch (c.status) {
            case "Aberto":
                holder.tvStatus.setBackgroundColor(0xFF43A047);
                break;
            case "Em Atendimento":
                holder.tvStatus.setBackgroundColor(0xFFFB8C00);
                break;
            case "Concluído":
                holder.tvStatus.setBackgroundColor(0xFF757575);
                break;
        }
        if (c.imagem != null && !c.imagem.isEmpty()) {
            holder.imgMiniatura.setImageBitmap(
                    android.graphics.BitmapFactory.decodeFile(c.imagem)
            );
        } else {
            holder.imgMiniatura.setImageDrawable(null);
            holder.imgMiniatura.setBackgroundColor(0xFFE0E0E0);
        }

        // Quando clicar no item inteiro, chama o listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(c));
    }

    // Retorna quantos itens existem na lista
    @Override
    public int getItemCount() {
        return itens.size();
    }
}
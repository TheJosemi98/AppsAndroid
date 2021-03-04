package com.example.app_dietas.logica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app_dietas.Dietas.Alimento;
import com.example.app_dietas.R;

import java.util.List;

public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.ViewHolder> implements View.OnClickListener {

    private List<Alimento> mAlimentos;
    private View.OnClickListener listener;
    private int position;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombreAlimento;
        public TextView cantidadNumerica;
        public TextView cantidadCualitativa;
        public TextView grupoAlimenticio;


        public ViewHolder(View itemView) {

            super(itemView);

            nombreAlimento = (TextView) itemView.findViewById(R.id.textViewNombreAlimento);
            cantidadNumerica = (TextView) itemView.findViewById(R.id.textViewCantidadN);
            cantidadCualitativa = (TextView) itemView.findViewById(R.id.textViewCantidadCual);
            grupoAlimenticio = (TextView) itemView.findViewById(R.id.textViewGrupoAlimenticio);

        }

    }
    public AlimentoAdapter(List<Alimento> Alimentos){
        mAlimentos = Alimentos;
    }

    @Override
    public  AlimentoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater  = LayoutInflater.from(context);

        View alimentoView = inflater.inflate(R.layout.recyclerview_alimento,parent,false);

        alimentoView.setOnClickListener(this);

        ViewHolder viewHolder = new ViewHolder(alimentoView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(AlimentoAdapter.ViewHolder viewHolder, int position){
        Alimento alimento = mAlimentos.get(position);
        this.position = position;

        TextView textViewNombre = viewHolder.nombreAlimento;
        textViewNombre.setText(alimento.getNombre());
        TextView textViewCantidadN = viewHolder.cantidadNumerica;
        textViewCantidadN.setText(String.valueOf(alimento.getCantidadNumerica()));
        TextView textViewCantidadC = viewHolder.cantidadCualitativa;
        textViewCantidadC.setText(alimento.getCantidadCualitativa());
        TextView textViewGrupoAlimenticio = viewHolder.grupoAlimenticio;
        textViewGrupoAlimenticio.setText(alimento.getGrupoAlimenticio());

    }


    @Override
    public  int getItemCount(){
        return mAlimentos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            listener.onClick(v);
    }


}

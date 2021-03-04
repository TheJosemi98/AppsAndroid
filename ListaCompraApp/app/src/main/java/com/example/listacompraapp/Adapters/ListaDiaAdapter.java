package com.example.listacompraapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listacompraapp.Listas.ListaDeUnDia;
import com.example.listacompraapp.R;

import java.util.Calendar;
import java.util.List;

public class ListaDiaAdapter extends RecyclerView.Adapter<ListaDiaAdapter.ViewHolder> implements View.OnClickListener {

    private List<ListaDeUnDia> mListaDeUnDia;
    private View.OnClickListener listener;
    private int position;

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView fechaT;
        private TextView gastoT;
        private TextView categoriaMasCompradaT;

        public ViewHolder(View itemView){
            super(itemView);

            fechaT = (TextView) itemView.findViewById(R.id.registro_fecha);
            gastoT = (TextView) itemView.findViewById(R.id.registro_gasto);
            categoriaMasCompradaT = (TextView) itemView.findViewById(R.id.registro_categoria_mas_comprada);

        }

    }

    public ListaDiaAdapter(List<ListaDeUnDia> Registros){mListaDeUnDia = Registros;}

    @Override
    public ListaDiaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View listaDiasView = inflater.inflate(R.layout.progreso_adapter,parent,false);
        listaDiasView.setOnClickListener(this);

        ViewHolder viewHolder = new ViewHolder(listaDiasView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListaDiaAdapter.ViewHolder viewHolder, int position){
        ListaDeUnDia listaDeUnDia = mListaDeUnDia.get(position);
        this.position = position;

        Calendar listaDiaC = Calendar.getInstance();
        listaDiaC.setTime(listaDeUnDia.getFecha_dia());

        String dia, mes;

        if(listaDiaC.get(Calendar.DAY_OF_MONTH) < 10)
            dia = "0"+listaDiaC.get(Calendar.DAY_OF_MONTH);
        else
            dia = ""+listaDiaC.get(Calendar.DAY_OF_MONTH);

        if(listaDiaC.get(Calendar.MONTH) < 10)
            mes = "0"+listaDiaC.get(Calendar.MONTH);
        else
            mes = ""+listaDiaC.get(Calendar.MONTH);

        TextView textViewFecha = viewHolder.fechaT;
        textViewFecha.setText(dia+"/"+mes+"/"+listaDiaC.get(Calendar.YEAR));
        TextView textViewGasto = viewHolder.gastoT;
        textViewGasto.setText("Gasto: "+String.format("%.2f €",listaDeUnDia.getGastoDia()));
        TextView textViewCategoria = viewHolder.categoriaMasCompradaT;
        textViewCategoria.setText("Categoria mas comprada: "+listaDeUnDia.getCategoriaMasComprada());

    }

    @Override
    public int getItemCount(){return mListaDeUnDia.size();}

    public void setOnClickListener(View.OnClickListener listener){this.listener = listener;}

    @Override
    public void onClick(View v){
        if(listener != null)
            listener.onClick(v);

    }




}

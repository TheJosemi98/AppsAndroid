package com.example.listacompraapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listacompraapp.Listas.Articulo;
import com.example.listacompraapp.R;

import java.util.List;

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ViewHolder> implements View.OnClickListener {

    private List<Articulo> mArticulos;
    private View.OnClickListener listener;
    private  int position;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreT;
        private TextView categoriaT;
        private TextView precioT;

        public ViewHolder(View itemView) {
            super(itemView);

            nombreT = (TextView) itemView.findViewById(R.id.articulo_text);
            categoriaT = (TextView) itemView.findViewById(R.id.categoria_registroList_text);
            precioT = (TextView) itemView.findViewById(R.id.precio_text);

        }

    }

    public ArticuloAdapter(List<Articulo> articulos) {
        mArticulos = articulos;
    }

    @Override
    public ArticuloAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articulosView = inflater.inflate(R.layout.articulo_lista_adapter,parent,false);
        articulosView.setOnClickListener(this);

        ViewHolder viewHolder = new ViewHolder(articulosView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ArticuloAdapter.ViewHolder viewHolder, int position){
        Articulo articulo_ = mArticulos.get(position);
        this.position = position;

        TextView textViewNombre = viewHolder.nombreT;
        textViewNombre.setText(articulo_.getNombre());
        TextView textViewPrecio = viewHolder.precioT;
        if(articulo_.isTienePrecio())
            textViewPrecio.setText("Precio: "+String.format("%.2f €",articulo_.getPrecio()));
        else
            textViewPrecio.setText("Precio: - €");
        TextView textViewCategoria = viewHolder.categoriaT;
        textViewCategoria.setText("Categoria: "+articulo_.getCategoria());

    }

    @Override
    public int getItemCount(){return mArticulos.size();}

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v){
        if(listener != null)
            listener.onClick(v);
    }

}

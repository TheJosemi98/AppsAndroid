package com.example.podometroapp.progreso;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.podometroapp.R;

import java.util.Calendar;
import java.util.List;

public class RegistroDiaAdapter extends RecyclerView.Adapter<RegistroDiaAdapter.ViewHolder> implements View.OnClickListener {

    private List<RegistrosDia> mRegistrosDia;
    private View.OnClickListener listener;
    private int position;


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView fechaRegistro;
        private TextView distanciaRecorrida;
        private TextView tiempoActivoMin;
        private TextView caloriasQuemadas;
        private TextView nPasos;
        private ImageView caloriasImage;
        private ImageView pasosImage;

        public  ViewHolder(View itemView){
            super(itemView);

            fechaRegistro = (TextView) itemView.findViewById(R.id.fecha_registro);
            distanciaRecorrida = (TextView) itemView.findViewById(R.id.distancia_registro);
            tiempoActivoMin = (TextView) itemView.findViewById(R.id.tiempo_registro);
            caloriasQuemadas = (TextView) itemView.findViewById(R.id.calorias_registros);
            nPasos = (TextView) itemView.findViewById(R.id.pasos_registros);
            caloriasImage = (ImageView) itemView.findViewById(R.id.imageCalorias);
            pasosImage = (ImageView) itemView.findViewById(R.id.imageMetas);

        }

    }

    public RegistroDiaAdapter(List<RegistrosDia> Registros){mRegistrosDia = Registros;}

    @Override
    public  RegistroDiaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View registrosDiasView = inflater.inflate(R.layout.registros_contenedor,parent,false);
        registrosDiasView.setOnClickListener(this);

        ViewHolder viewHolder = new ViewHolder(registrosDiasView);
        return viewHolder;
    }

    @Override
    public  void onBindViewHolder(RegistroDiaAdapter.ViewHolder viewHolder, int position){
        RegistrosDia registrosDia = mRegistrosDia.get(position);
        this.position = position;

        Calendar registroDiaC = Calendar.getInstance();
        registroDiaC.setTime(registrosDia.getFecha());
        Log.i("PasosObjetivo",""+registrosDia.isCumplidoPasos());

        if(registrosDia.isCumplidoCalorias())
            viewHolder.caloriasImage.setImageResource(R.drawable.circle_notificacion_done);
        else
            viewHolder.caloriasImage.setImageResource(R.drawable.circle_notificacion_no_done);

        if(registrosDia.isCumplidoPasos())
            viewHolder.pasosImage.setImageResource(R.drawable.circle_notificacion_done);
        else
            viewHolder.pasosImage.setImageResource(R.drawable.circle_notificacion_no_done);


        String dia, mes;
        if(registroDiaC.get(Calendar.DAY_OF_MONTH) < 10)
            dia = "0"+registroDiaC.get(Calendar.DAY_OF_MONTH);
        else
            dia = ""+registroDiaC.get(Calendar.DAY_OF_MONTH);

        if((registroDiaC.get(Calendar.MONTH)+1) < 10)
            mes = "0"+(registroDiaC.get(Calendar.MONTH)+1);
        else
            mes = ""+(registroDiaC.get(Calendar.MONTH)+1);




        TextView textViewFecha = viewHolder.fechaRegistro;
        textViewFecha.setText(dia+"/"+mes+"/"+registroDiaC.get(Calendar.YEAR));
        TextView textViewDist  = viewHolder.distanciaRecorrida;
        textViewDist.setText(String.format("%.3f",registrosDia.getDistanciaTotalRecorrida())+" Km");
        TextView textViewCalorias = viewHolder.caloriasQuemadas;
        textViewCalorias.setText(String.format("%.3f",registrosDia.getCaloriasQuemadas())+" Kcal");
        TextView textViewTiempoAct = viewHolder.tiempoActivoMin;
        textViewTiempoAct.setText(registrosDia.getTiempoEnMinutos()+" min");
        TextView textViewNPasos = viewHolder.nPasos;
        textViewNPasos.setText(registrosDia.getnPasosDados()+" pasos");

    }

    @Override
    public  int getItemCount(){
        return mRegistrosDia.size();
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

package com.example.podometroapp.ui.Metas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.podometroapp.R;

public class MetasFragment extends Fragment {

    private EditText pasosObjetivo;
    private EditText caloriasObjetivo;
    private EditText pesoText;
    private EditText edadText;
    private EditText alturaText;
    private RadioButton masculinoRadioButton;
    private RadioButton femeninoRadioButton;
    private Button aplicarCambios;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_metas, container, false);
        pasosObjetivo = root.findViewById(R.id.pasos_metas_edit);
        caloriasObjetivo = root.findViewById(R.id.calorias_meta_edit);
        pesoText = root.findViewById(R.id.input_peso);
        edadText = root.findViewById(R.id.input_edad);
        alturaText = root.findViewById(R.id.input_altura);
        masculinoRadioButton = root.findViewById(R.id.radioButtonMasculino);
        femeninoRadioButton = root.findViewById(R.id.radioButtonFemenino);
        aplicarCambios = root.findViewById(R.id.boton_aplicar_metas);

        return root;
    }


}

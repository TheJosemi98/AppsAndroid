package com.example.app_dietas.ui.CerrarSesion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app_dietas.R;

public class CerrarFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_option, container, false);

        SharedPreferences sharedPref_ = getContext().getSharedPreferences("SesionIniciada", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref_.edit();
        editor.putInt("SesionIniciada",2);
        editor.commit();

        getActivity().finish();

        return root;
    }
}

package com.adrian.realm.view;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.adrian.realm.R;
import com.adrian.realm.model.*;
import com.adrian.realm.viewmodel.TareasAppViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NuevaTareaFragment extends Fragment {

    private TareasAppViewModel tareasAppViewModel;
    private NavController navController;

    private EditText descripcionEditText;
    private EditText prioridadEditText;
    private Button addTarea;
    private TextView tareaTextView;

    private int idPrioridadSeleccionada;
    private Tarea tareaSelecionada;

    public NuevaTareaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nueva_tarea, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        tareasAppViewModel = ViewModelProviders.of(requireActivity()).get(TareasAppViewModel.class);


        descripcionEditText = view.findViewById(R.id.edittext_descripcion);
        prioridadEditText = view.findViewById(R.id.edittext_prioridad);

        addTarea = view.findViewById(R.id.button_crearTarea);
        tareaTextView = view.findViewById(R.id.textview_tareatitle);

        if (tareasAppViewModel.isUserEditing) {
            tareaSelecionada = tareasAppViewModel.obtenerTareaDetallePorId(tareasAppViewModel.userToEditId);
            descripcionEditText.setText(tareaSelecionada.descripcion);
            prioridadEditText.setText(tareaSelecionada.prioridad);
            addTarea.setText("Editar tarea");
            tareaTextView.setText("Editar tarea");
        }
        else {
            addTarea.setText("Añadir tarea");
            tareaTextView.setText("Añadir tarea");
        }

        view.findViewById(R.id.button_crearTarea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(descripcionEditText.getText().toString().isEmpty()){
                    descripcionEditText.setError("Introduzca la descripción");
                    return;
                }
                if (tareasAppViewModel.isUserEditing) {
                    Tarea tarea = new Tarea();
                    tarea.setDescripcion(descripcionEditText.getText().toString());
                    tarea.setPrioridad(prioridadEditText.getText().toString());
                    tareasAppViewModel.actualizarTarea(tareasAppViewModel.userToEditId, tarea);
                    tareaSelecionada = null;
                    tareasAppViewModel.isUserEditing = false;
                    tareasAppViewModel.userToEditId = 0;
                }
                else tareasAppViewModel.insertarTarea(new Tarea(descripcionEditText.getText().toString() + " " +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), prioridadEditText.getText().toString(), idPrioridadSeleccionada));
                navController.popBackStack();
            }
        });
    }
}

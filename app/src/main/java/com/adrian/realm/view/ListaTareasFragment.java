package com.adrian.realm.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.adrian.realm.R;
import com.adrian.realm.viewmodel.TareasAppViewModel;
import com.adrian.realm.model.*;

import io.realm.RealmResults;

public class ListaTareasFragment extends Fragment {

    private NavController navController;
    private TareasAppViewModel tareasAppViewModel;
    private TareasAdapter tareasAdapter;
    private SearchView searchView;
    private String criteria;
    private RecyclerView recyclerView;

    public ListaTareasFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_tareas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        tareasAppViewModel = ViewModelProviders.of(requireActivity()).get(TareasAppViewModel.class);


        view.findViewById(R.id.fab_nuevaTarea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nuevaTareaFragment);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_listaTareas);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        tareasAdapter = new TareasAdapter();

        tareasAdapter.establecerListaTareas(tareasAppViewModel.obtenerTareasDetalle());
        recyclerView.setAdapter(tareasAdapter);

        searchView = view.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RealmResults<Tarea> tareas = newText.equals("") ? tareasAppViewModel.obtenerTareasDetalle() : tareasAppViewModel.obtenerTareasDetallePorNombre(newText);
                tareasAdapter.establecerListaTareas(tareas);
                recyclerView.setAdapter(tareasAdapter);

                return true;
            }
        });


    }


    class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder>{

        RealmResults<Tarea> tareaDetalleList;



        @NonNull
        @Override
        public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TareaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_tarea, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
            final Tarea tarea = tareaDetalleList.get(position);
            Log.i("Logger", String.valueOf(tarea.getId()));
            holder.descripcionTextView.setText(tarea.getDescripcion());
            holder.prioridadTextView.setText(tarea.getPrioridad());
            //holder.prioridadTextView.setText(tarea.prioridadId);

            holder.eliminarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tareasAppViewModel.eliminarTarea(tarea.id);
                    navController.navigate(R.id.listaTareasFragment);

                }
            });

            holder.editarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tareasAppViewModel.editarTarea(tarea.id);
                    navController.navigate(R.id.nuevaTareaFragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tareaDetalleList != null ? tareaDetalleList.size() : 0;
        }

        void establecerListaTareas(RealmResults<Tarea> list){
            tareaDetalleList = list;
            notifyDataSetChanged();
        }

        class TareaViewHolder extends RecyclerView.ViewHolder {
            TextView descripcionTextView, prioridadTextView;
            ImageView editarImageView, eliminarImageView;

            TareaViewHolder(@NonNull View itemView) {
                super(itemView);
                descripcionTextView = itemView.findViewById(R.id.textview_descripcion);
                prioridadTextView = itemView.findViewById(R.id.textview_prioridad);
                prioridadTextView = itemView.findViewById(R.id.textview_prioridad);
                editarImageView = itemView.findViewById(R.id.imageview_editar);
                eliminarImageView = itemView.findViewById(R.id.imageview_eliminar);
            }
        }
    }
}

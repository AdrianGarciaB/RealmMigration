package com.adrian.realm.viewmodel;

import android.app.Application;



import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.realm.Realm;
import io.realm.RealmResults;
import com.adrian.realm.model.*;

public class TareasAppViewModel extends AndroidViewModel {

    private Realm realm;
    public boolean isUserEditing;
    public int userToEditId;

    public TareasAppViewModel(@NonNull Application application) {
        super(application);
        realm = Realm.getDefaultInstance();
    }

    public RealmResults<Tarea> obtenerTareasDetalle(){
        return realm.where(Tarea.class).findAll();
    }

    public RealmResults<Tarea> obtenerTareasDetallePorNombre(String criteria){
        return realm.where(Tarea.class).contains("descripcion", criteria).findAll();
    }

    public Tarea obtenerTareaDetallePorId(int id){
        return realm.where(Tarea.class).equalTo("id", id).findAll().first();
    }

    public void insertarTarea(final Tarea tarea){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxId = obtenerTareasDetalle().max("id");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                Tarea tareaTmp = realm.createObject(Tarea.class, nextId);
                tareaTmp.descripcion = tarea.descripcion;
                tareaTmp.prioridad = tarea.prioridad;
                realm.insertOrUpdate(tareaTmp);
            }
        });
    }

    public void actualizarTarea( final int id, final Tarea tarea){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Tarea tareaAEditar = obtenerTareaDetallePorId(id);
                tareaAEditar.setDescripcion(tarea.descripcion);
                tareaAEditar.setPrioridad(tarea.prioridad);
            }
        });
    }

    public void eliminarTarea(final int id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Tarea> result = realm.where(Tarea.class).equalTo("id",id).findAll();
                result.deleteAllFromRealm();

            }
        });
    }

    public void editarTarea(int userToEditId) {
        isUserEditing = true;
        this.userToEditId = userToEditId;
    }
}
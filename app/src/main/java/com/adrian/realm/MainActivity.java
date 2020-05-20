package com.adrian.realm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import com.adrian.realm.viewmodel.TareasAppViewModel;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class MainActivity extends AppCompatActivity implements RealmMigration {
    private TareasAppViewModel tareasAppViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("tareas.realm")
                .schemaVersion(1)
                .migration(this)
                .build();


        Realm.setDefaultConfiguration(config);

        setContentView(R.layout.activity_main);
        tareasAppViewModel = ViewModelProviders.of(this).get(TareasAppViewModel.class);
    }

    @Override
    public void onBackPressed() {
        tareasAppViewModel.isUserEditing = false;
        super.onBackPressed();
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if(oldVersion == 0) {
            Log.d("Migration", "actualizando a la version 1");
            RealmObjectSchema tareasSchema = schema.get("Tarea");


            tareasSchema.addField("prioridad", String.class);

            tareasSchema.transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("descripcion", obj.getString("descripcion") + " " + obj.getString("fecha"));
                        }
                    });
            tareasSchema.removeField("fecha");
            oldVersion++;
        }
    }
}

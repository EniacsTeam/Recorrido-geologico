package com.eniacs_team.rutamurcielago;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuMultimediaMapa extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<listItemMenuMultimedia> listItemns;
    private BaseDatos baseDatos;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_multimedia_mapa);
        
        recyclerView = (RecyclerView) findViewById(R.id.menumultimediamaparv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        listItemns = new ArrayList<>();
        baseDatos = new BaseDatos(this);
        baseDatos.cargarBase();

        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        if(extras != null){
            id = extras.getInt("id"); // id del punto.
        }
        
        loadRecyclerViewData();
        
        
    }

    private void loadRecyclerViewData() {

        //if(baseDatos.existenciaPunto(id, "Imagen") == 1){
        listItemMenuMultimedia item3 = new listItemMenuMultimedia(
                "Imagen", String.valueOf(id)
        );
        listItemns.add(item3);

        //}
        //if(baseDatos.existenciaPunto(id, "Video") == 1){
            listItemMenuMultimedia item1 = new listItemMenuMultimedia(
                    "Video", String.valueOf(id)
            );
            listItemns.add(item1);

        //}
        //if(baseDatos.existenciaPunto(id, "Audio") == 1){
            listItemMenuMultimedia item2 = new listItemMenuMultimedia(
                    "Audio", String.valueOf(id)
            );
            listItemns.add(item2);

        //}
        //if(baseDatos.existenciaPunto(id, "Animacion") == 1){
            listItemMenuMultimedia item4 = new listItemMenuMultimedia(
                    "Animacion", String.valueOf(id)
            );
            listItemns.add(item4);

        //}

        if(listItemns.size() == 0){
            Toast.makeText(getApplicationContext(), getString(R.string.No_multimedia), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else{
            adapter = new adapterMenuMultimedia(listItemns, this);
            recyclerView.setAdapter(adapter);
        }



    }
}

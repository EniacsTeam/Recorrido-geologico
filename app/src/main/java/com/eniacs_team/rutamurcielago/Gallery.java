package com.eniacs_team.rutamurcielago;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Gallery extends AppCompatActivity {

    private BaseDatos baseDatos;
    private int idPunto;
    private String nPunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        baseDatos = new BaseDatos(this);
        Bundle extras = getIntent().getExtras();
        idPunto = extras.getInt("id");
        nPunto = extras.getString("nombre");
        setTitle("Punto #" + idPunto + " - " + nPunto);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists, idPunto);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<CreateList> prepareData(){
        Map imagenes = baseDatos.selectImagen(idPunto);


        ArrayList<CreateList> theimage = new ArrayList<>();
        StringBuilder sb;
        Iterator<Map.Entry<Drawable, String>> it = imagenes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Drawable, String> par = it.next();
            CreateList createList = new CreateList();
            sb = new StringBuilder(par.getValue());
            if (sb.length() > 21)
            {
                sb.substring(0, 20);
                sb.append("...");
            }
            createList.setImage_title(sb.toString());
            createList.setImage_drawable(par.getKey());
            theimage.add(createList);
        }

        return theimage;
    }
}

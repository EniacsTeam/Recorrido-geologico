package com.eniacs_team.rutamurcielago;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    private final String image_titles[] = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras libero odio, venenatis eu auctor sit amet, dignissim a tortor.",
            "Morbi eu justo pellentesque, rutrum lacus nec, condimentum nulla. Curabitur vitae cursus elit. Morbi venenatis, arcu et molestie porttitor, sapien justo pellentesque dolor, eget mattis nisl tellus et massa.",
            "Vestibulum pulvinar, leo in rhoncus vulputate, tortor augue sodales sapien, sit amet vulputate libero nisi vitae magna. Nullam tempus felis ante, eget hendrerit dui placerat ac.",
    };

    private final Integer image_ids[] = {
            R.drawable.github_logo,
            R.drawable.chibi,
            R.drawable.character,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();
        StringBuilder sb;
        for(int i = 0; i< image_titles.length; i++){
            CreateList createList = new CreateList();
            String cutString = image_titles[i].substring(0, 20);
            sb = new StringBuilder(cutString);
            sb.append("...");
            createList.setImage_title(sb.toString());
            createList.setImage_ID(image_ids[i]);
            theimage.add(createList);
        }
        return theimage;
    }
}

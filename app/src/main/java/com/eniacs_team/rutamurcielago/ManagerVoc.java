package com.eniacs_team.rutamurcielago;

/**
 * Created by kenca on 04/06/2017.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.eniacs_team.rutamurcielago.R.mipmap.audio;

public class ManagerVoc {

    //final String MEDIA_PATH = new String("/sdcard/");
    private BaseDatos baseDatos;
    private ArrayList<HashMap<String, String>> vocList = new ArrayList<HashMap<String, String>>();
    Map<String,String[]> audios = new LinkedHashMap<String, String[]>();

    // Constructor
    public ManagerVoc(){
        baseDatos = BaseDatos.getInstancia();
        baseDatos.cargarBase();
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList(){

        audios = baseDatos.audiosExtraDisponibles();


        if(audios.size()!=0){
            for( Map.Entry<String,String[]> entry : audios.entrySet()){
                HashMap<String, String> voc = new HashMap<String, String>();
                voc.put("id", entry.getKey());
                String[] valores = entry.getValue();
                voc.put("Title", valores[0]);
                voc.put("Texto", valores[1]);
                vocList.add(voc);
            }
        }




        /*File home = new File(MEDIA_PATH);

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> voc = new HashMap<String, String>();
                voc.put("Title", file.getName().substring(0, (file.getName().length() - 4)));
                voc.put("Path", file.getPath());

                // Adding each song to SongList
                vocList.add(voc);
            }
        }*/
        // return songs list array
        return vocList;
    }


}

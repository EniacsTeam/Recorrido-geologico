package com.eniacs_team.rutamurcielago;

/**
 * Created by kenca on 04/06/2017.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerVoc {

    final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList<HashMap<String, String>> vocList = new ArrayList<HashMap<String, String>>();

    // Constructor
    public ManagerVoc(){

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList(){
        File home = new File(MEDIA_PATH);

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> voc = new HashMap<String, String>();
                voc.put("Title", file.getName().substring(0, (file.getName().length() - 4)));
                voc.put("Path", file.getPath());

                // Adding each song to SongList
                vocList.add(voc);
            }
        }
        // return songs list array
        return vocList;
    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }


}

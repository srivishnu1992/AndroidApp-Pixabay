package com.example.srivi.pixabay;

import java.util.ArrayList;

/**
 * Created by srivi on 09-04-2018.
 */

public class ImageInfo {
    @Override
    public String toString() {
        return "ImageInfo{" +
                "total=" + total +
                ", hits=" + hits +
                '}';
    }

    int total;
    ArrayList<hits> hits = new ArrayList<>(  );
    class hits {
        String largeImageURL;

        @Override
        public String toString() {
            return "hits{" +
                    "largeImageURL='" + largeImageURL + '\'' +
                    '}';
        }
    }


}

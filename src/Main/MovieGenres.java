package Main;

import java.util.Map;
import java.util.HashMap;

/**
 * Class Movie Genres
 */
public class MovieGenres {

    public Genres genres[] = new Genres[]{};
    public Map<Integer, String> genreMap = new HashMap<Integer, String>();

    public Map<Integer, String> getGenreMap() {
        return genreMap;
    }

    public void setGenreMap(Map<Integer, String> genreMap) {
        this.genreMap = genreMap;
    }

    public class Genres{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }



        // Overrides fuer toString-Methode
        @Override
        public String toString() {
            return name;
        }

        public String toString(int mapIndex) {
            return genreMap.get(mapIndex);
        }


    }

    public boolean fromListToHashmap(){

        if(this.genres != null)
        {
            for (Genres genre : this.genres) {
                genreMap.put(genre.getId(), genre.getName());
            }

            return true;
        }
        else
        {
            return false;
        }
    }



}

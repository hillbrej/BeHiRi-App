package Main;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jonas on 20.05.2017.
 */
public class Favorites extends Movies {

    private List<Results> favorites = new ArrayList<>();
    private List<Integer> favoriteIds = new ArrayList<>();

    // Getter und Setter Methoden
    public List<Results> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Results> favorites) {
        this.favorites = favorites;
    }

    public void moviesFromFile(String path){
        Reader fr = null;
        Results[] favoritesArray = new Results[]{};

        try
        {
            fr = new FileReader( path+"\\fileWriter.json" );

            Reader reader = fr;

            favoritesArray = new Gson().fromJson(fr, Favorites.Results[].class);
            if(favoritesArray != null) {
                favorites.addAll(Arrays.asList(favoritesArray));
                for(Results item : favorites)
                    favoriteIds.add(item.getId());
            }

            fr.close();
        }
        catch ( IOException e ) {
            System.err.println( "Konnte Datei nicht erstellen" );
        }
    }

    public void Movies2File(String path){
        Writer fw = null;
        try
        {
            fw = new FileWriter( path+"\\fileWriter.json" );
            String data = new Gson().toJson(favorites);
            fw.write( data );
            fw.close();

            // fw.append( System.getProperty("line.separator") ); // e.g. "\n"
            /*
            FileOutputStream fileOutputStream = new FileOutputStream(path+ "Movies.data");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(favoriteList);
            */
        }
        catch ( IOException e ) {
            System.err.println( "Konnte Datei nicht erstellen" );
        }
    }

    public List<String> FavMovies2List() {
        List<String> movies = new ArrayList<String>();
        for (Results item : favorites) {
            movies.add(item.getTitle());
        }

        return movies;
    }

    public boolean addFavorites(Results element){
        if(!favoriteIds.contains(element.getId())) {
            this.favorites.add(element);
            this.favoriteIds.add(element.getId());
            return true;
        }
        else {
            System.out.println("Film schon in Favoriten enthalten");
            return false;
        }
    }

}

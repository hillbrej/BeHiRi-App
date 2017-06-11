package Main;

import com.google.gson.Gson;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Jonas on 20.05.2017.
 */
public class Favorites extends Movies {

    public static ListProperty<Results> favList = new SimpleListProperty<>();
    private List<Results> favorites = new ArrayList<>();
    private List<Integer> favoriteIds = new ArrayList<>();

    // Getter und Setter Methoden
    public List<Results> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Results> favorites) {
        this.favorites = favorites;
    }

    public void moviesFromFile(String path) {
        Reader fr = null;
        Results[] favoritesArray = new Results[]{};

        try {
            fr = new FileReader(path + "\\fileWriter.json");

            Reader reader = fr;

            favoritesArray = new Gson().fromJson(fr, Favorites.Results[].class);
            if (favoritesArray != null) {
                favorites.addAll(Arrays.asList(favoritesArray));
                ObservableList<Movies.Results> obsMovieList = FXCollections.observableArrayList(this.favorites);
                favList.setValue(obsMovieList);

                for (Results item : favorites)
                    favoriteIds.add(item.getId());
            }

            fr.close();
        } catch (IOException e) {
            System.err.println("Konnte nicht aus Dateien lesen (evtl. existiert die Datei: " + path + " nicht).");
        }
    }

    public void Movies2File(String path) {
        Writer fw = null;

        try {
            fw = new FileWriter(path + "\\fileWriter.json");
            String data = new Gson().toJson(favorites);
            fw.write(data);
            fw.close();

        } catch (IOException e) {
            System.err.println("Konnte Datei nicht erstellen");
        }
    }

    public List<String> FavMovies2List() {
        List<String> movies = new ArrayList<String>();
        for (Results item : favorites) {
            movies.add(item.getTitle());
        }

        return movies;
    }

    /* public boolean addFavorites(Results element){
        if(!favoriteIds.contains(element.getId())) {
            this.favorites.add(element);
            this.favoriteIds.add(element.getId());
            return true;
        }
        else {
            System.out.println("Film schon in Favoriten enthalten");
            return false;
        }
    }*/

    public boolean addFavorites(Results element) {
        if (element != null) {
            ObservableList<Results> obsMovieList = FXCollections.observableArrayList(this.favorites);
            favList.setValue(obsMovieList);
            return true;
        } else {
            System.out.println("Film schon in Favoriten enthalten");
            return false;
        }
    }

    public boolean deleteFavorites(Results element){
        if(element != null) {
            // this.favList.remove(element);
            this.favorites.remove(element);
            ObservableList<Results> obsMovieList = FXCollections.observableArrayList(this.favorites);
            favList.setValue(obsMovieList);
            return true;
        }
        else {
            System.out.println("Film konnte nicht aus Favoriten geloescht werden");
            return false;
        }
    }

    public String showFavDetails(String title, MovieGenres genresIds){
        String output = "";
        String genres = "";
        System.out.println("Film Titel: " + title);

        for(Movies.Results item : favorites) {
            if (item.getTitle() == title) {
                output = "Titel:\t\t\t\t" + title + "\n\n";
                output = output + "Originaltitel:\t\t" + item.getOriginal_title() + "\n";
                output = output + "Erscheinungsjahr:\t" + item.getRelease_date() + "\n";

                // Genres abfragen
                for (int j = 0; j < item.getGenre_ids().length; j++) {
                    for (int k = 0; k < genresIds.genres.length; k++) {
                        if (genresIds.genres[k].getId() == item.getGenre_ids()[j]) {
                            genres = genres + genresIds.genres[k].getName() + ", ";
                            break;
                        }
                    }
                }

                output = output + "Genres:\t\t\t" + genres + "\n\n";
                output = output + "Beschreibung:\n" + item.getOverview() + "\n";

            }
        }
        return output;
    }

    public String getFavMovieUrl(String title){
        String url = "";
        for(Movies.Results item : favorites) {
            if (item.getTitle() == title)
                url = item.getPoster_path();
        }

        return url;
    }

    public int getFavRating(String title)
    {
        int rating = -1;
        for(Movies.Results item : this.favorites) {
            if (item.getTitle() == title)
                rating = item.getRating();
        }

        return rating;
    }

    public String showFavDetails(int index, MovieGenres genresIds){
        String output = "";
        String genres = "";

        if (index < favorites.size() && favorites != null) {
            output = "Titel:\t\t\t\t" + favorites.get(index).getTitle() + "\n\n";
            output = output + "Originaltitel:\t\t" + favorites.get(index).getOriginal_title() + "\n";
            output = output + "Erscheinungsjahr:\t" + favorites.get(index).getRelease_date() + "\n";

            // Genres abfragen
            for (int j = 0; j < favorites.get(index).getGenre_ids().length; j++) {
                for (int k = 0; k < genresIds.genres.length; k++) {
                    if (genresIds.genres[k].getId() == favorites.get(index).getGenre_ids()[j]) {
                        genres = genres + genresIds.genres[k].getName() + ", ";
                        break;
                    }
                }
            }

                output = output + "Genres:\t\t\t" + genres + "\n\n";
                output = output + "Beschreibung:\n" + favorites.get(index).getOverview() + "\n";

            }

        return output;
    }


}

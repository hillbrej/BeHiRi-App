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
/**
 * Created by Beier on 24.06.2017.
 */


public class Reminds extends Movies
{
    public static ListProperty<Results> remList = new SimpleListProperty<>(); //favList
    private List<Results> reminders = new ArrayList<>();//favorites
    private List<Integer> reminderIds = new ArrayList<>();//favoriteIds

    public List<Results> getReminders() { //getFavorites()
        return reminders;
    }

    public void setReminders(List<Results> reminders) {//setFavorites
        this.reminders = reminders;
    }

    public void moviesFromFile(String path) {
        Reader fr = null;
        Results[] reminderArray = new Results[]{};

        try {
            fr = new FileReader(path + "\\fileWriterRemind.json");

            Reader reader = fr;

            reminderArray = new Gson().fromJson(fr, Favorites.Results[].class);
            if (reminderArray != null) {
                reminders.addAll(Arrays.asList(reminderArray));
                ObservableList<Movies.Results> obsMovieList = FXCollections.observableArrayList(this.reminders);
                remList.setValue(obsMovieList);

                for (Results item : reminders)
                    reminderIds.add(item.getId());
            }

            fr.close();
        } catch (IOException e) {
            System.err.println("Konnte nicht aus Dateien lesen (evtl. existiert die Datei: " + path + " nicht).");
        }
    }

    public void Movies2File(String path) {
        Writer fw = null;

        try {
            fw = new FileWriter(path + "\\fileWriterRemind.json");
            String data = new Gson().toJson(reminders);
            fw.write(data);
            fw.close();

        } catch (IOException e) {
            System.err.println("Konnte Datei nicht erstellen");
        }
    }

    public List<String> RemMovies2List() {//FavMovies2List
        List<String> movies = new ArrayList<String>();
        for (Results item : reminders) {
            movies.add(item.getTitle());
        }

        return movies;
    }

    public boolean addReminders(Results element) {//addFavorites
        if (element != null) {
            ObservableList<Results> obsMovieList = FXCollections.observableArrayList(this.reminders);
            remList.setValue(obsMovieList);
            return true;
        } else {
            System.out.println("Film schon in Merkliste enthalten");
            return false;
        }
    }

    public boolean deleteReminders(Results element){//deleteFavorites

        if(element != null) {
            try {
                this.reminders.remove(element);
                ObservableList<Results> obsMovieList = FXCollections.observableArrayList(this.reminders);
                remList.setValue(obsMovieList);
                return true;
            }
            catch(Exception ex)
            {
                System.out.print("Fehler in der Klasse Reminds beim Loeschen des Objekts: " + ex.getMessage());
                return false;
            }
        }
        else {
            System.out.println("Film konnte nicht aus Merk geloescht werden");
            return false;
        }
    }

    public String showRemDetails(String title, MovieGenres genresIds){//showFavDetails
        String output = "";
        String genres = "";
        System.out.println("Film Titel: " + title);

        for(Movies.Results item : reminders) {
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

    public String getRemMovieUrl(String title){//getFavMovieUrl
        String url = "";
        for(Movies.Results item : reminders) {
            if (item.getTitle() == title)
                url = item.getPoster_path();
        }

        return url;
    }

    public int getRemRating(String title) {//getFavRating
        int rating = -1;
        for(Movies.Results item : this.reminders) {
            if (item.getTitle() == title)
                rating = item.getRating();
        }

        return rating;
    }

    public String showRemDetails(int index, MovieGenres genresIds){//showFavDetails
        String output = "";
        String genres = "";

        if (index < reminders.size() && reminders != null) {
            output = "Titel:\t\t\t\t" + reminders.get(index).getTitle() + "\n\n";
            output = output + "Originaltitel:\t\t" + reminders.get(index).getOriginal_title() + "\n";
            output = output + "Erscheinungsjahr:\t" + reminders.get(index).getRelease_date() + "\n";

            // Genres abfragen
            for (int j = 0; j < reminders.get(index).getGenre_ids().length; j++) {
                for (int k = 0; k < genresIds.genres.length; k++) {
                    if (genresIds.genres[k].getId() == reminders.get(index).getGenre_ids()[j]) {
                        genres = genres + genresIds.genres[k].getName() + ", ";
                        break;
                    }
                }
            }

                output = output + "Genres:\t\t\t" + genres + "\n\n";
                output = output + "Beschreibung:\n" + reminders.get(index).getOverview() + "\n";

            }

        return output;
    }

    public boolean isIdInReminders(int id) {//isIdInFavorites
        for(Results item : reminders) {
            if (item.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public void filterRemMoviesByTitle(String title, boolean noFilter) {
        if(!noFilter) {
            ObservableList<Movies.Results> obsMovieList = FXCollections.observableArrayList(FXCollections.observableArrayList(this.reminders).filtered(s -> (s.getTitle().toLowerCase().contains(title))));
            remList.setValue(obsMovieList);
        }
        else
        {
            ObservableList<Movies.Results> obsMovieList = FXCollections.observableArrayList(FXCollections.observableArrayList(this.reminders).filtered(s -> true));
            remList.setValue(obsMovieList);
        }
    }
}

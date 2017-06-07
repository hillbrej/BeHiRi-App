package Main;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

// Klasse MovieDb fuer den API-Zugriff auf themoviedb.org
public class MovieDb implements Runnable {
    public static int counter = 0;
    public static ListProperty<String> moviesList = new SimpleListProperty<>();

    private String host = "";
    private String pathMovieSearch = "";
    private String pathGenreSearch = "";
    private String searchString = "";
    private String apiKey = "";
    private String language = "";
    private String queryTail = "";
    private Movies movies = new Movies();
    private boolean dataAvailable = true;
    private boolean killThread = false;

    // Konstruktor
    MovieDb(){
        this.host = "api.themoviedb.org";
        this.pathMovieSearch = "/3/search/movie";
        this.pathGenreSearch = "/3/genre/movie/list";
        this.apiKey = "api_key=afedbc4bff50da07274b4bc896c217ce";
        // this.language = "&language=en-US";
        this.language = "&language=de";
        this.queryTail = "&page=1&include_adult=false";
    }

    // Getter und Setter
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPathMovieSearch() {
        return pathMovieSearch;
    }

    public void setPathMovieSearch(String pathMovieSearch) {
        this.pathMovieSearch = pathMovieSearch;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getQueryTail() {
        return queryTail;
    }

    public void setQueryTail(String queryTail) {
        this.queryTail = queryTail;
    }

    public Movies getMovies() {
        return movies;
    }

    public boolean isDataAvailable() {
        return dataAvailable;
    }

    public boolean isKillThread() {
        return killThread;
    }

    public void setKillThread(boolean killThread) {
        this.killThread = killThread;
    }

    // Methoden Aufrufe
    public Movies ApiQueryMovies(String searchString) {

        counter++;
        System.out.println("API-Anfragen: " + counter);
        searchString = "&query=" + searchString;
        String queryString = this.apiKey + this.language + searchString + this.queryTail;

        try {
            //String url = new URI("https", host, path, queryString, null).toASCIIString();
            // System.out.println("URL:" + url);

            URL url = new URI("https", this.host, this.pathMovieSearch, queryString, null).toURL();
            Reader reader = new InputStreamReader(url.openStream()); // Stream für den Inhalt an URL öffnen

            // Ausgabe vom reader - ! Nach Auslesen ist reader auf null !
            /*
            System.out.println("Ausgabe von reader:");
            int i = 0;
            while(i != -1)
            {
                i = reader.read();
                System.out.print((char) i);
            }
            */

            // Zuweisung über GSON
            Movies movies = new Gson().fromJson(reader, Movies.class);
            return  movies;

        } catch (URISyntaxException | IOException ex) {
            System.err.println("An error occurred while requesting data from API.");
            return null;
        }
    }

    public MovieGenres ApiQueryGenres() {

        counter++;
        System.out.println("API-Anfragen: " + counter);

        String queryString = this.apiKey + this.language;

        try {
            //String url = new URI("https", host, path, queryString, null).toASCIIString();
            // System.out.println("URL:" + url);

            URL url = new URI("https", this.host, this.pathGenreSearch, queryString, null).toURL();
            Reader reader = new InputStreamReader(url.openStream()); // Stream für den Inhalt an URL öffnen

            // Ausgabe vom reader - ! Nach Auslesen ist reader auf null !
            /*
            System.out.println("Ausgabe von reader:");
            int i = 0;
            while(i != -1)
            {
                i = reader.read();
                System.out.print((char) i);
            }
            */

            // Zuweisung über GSON
            MovieGenres genres = new Gson().fromJson(reader, MovieGenres.class);
            return  genres;

        } catch (URISyntaxException | IOException ex) {
            System.err.println("An error occurred while requesting data from API.");
            return null;
        }
    }

    public void Genres2File(){

    }

    // Methode Run für Thread zum Suchen
    @Override
    public void run() {
        this.movies = ApiQueryMovies(this.searchString);
        ObservableList<String> obsMovieList = FXCollections.observableArrayList(this.movies.Movies2List());
        Platform.runLater(() -> moviesList.setValue(obsMovieList));
    }

}

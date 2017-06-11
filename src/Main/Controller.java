package Main;

import java.awt.image.FilteredImageSource;
import java.io.*;

import java.util.*;

import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;

import com.sun.org.apache.xpath.internal.operations.Number;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;
import javafx.scene.Node;

import java.text.Collator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Controller {

    // Pfad in dem das Programm liegt (fuer Favoritenexport usw.)
    File f = new File("");
    String localPath = f.getAbsolutePath();

    public static String[] comboBoxValues;

    // Objekt für die Movies erzeugen und instanzieren
    MovieDb movieDb = new MovieDb();

    // Objekt für die Movies erzeugen
    Movies movies;

    // Objekt für die Genres erzeugen
    MovieGenres genres;

    // Objekt für die Favoriten erzeugen
    Favorites favoriteMovies;

    // Filter fuer Suchliste
    // FilteredList<Movies.Results> filteredMovies;

    // Thread erzeugen
    ExecutorService exeService;

    @FXML
    public ComboBox myCombobox;
    public ListView myListView;
    public TextField textfieldSearch;
    public ImageView imageViewStar1;
    public ImageView imageViewStar2;
    public ImageView imageViewStar3;
    public ImageView imageViewStar4;
    public ImageView imageViewStar5;
    public Button toFavouritelistButton;
    public Button toReminderlistButton;

    public ComboBox myComboboxFav;
    public ListView myListViewFav;
    public TextField textfieldSearchFav;
    public ImageView imageViewStarFav1;
    public ImageView imageViewStarFav2;
    public ImageView imageViewStarFav3;
    public ImageView imageViewStarFav4;
    public ImageView imageViewStarFav5;
    public Button deleteFavouritelistButtonFav;
    public Button toReminderlistButtonFav;

    // public TextArea textAreaDetail;
    public ImageView imageViewMovie;
    public ObservableList<String> favoriteList;
    public Label labelDetail;
    public Label labelFavDetail;
    public ComboBox comboBoxYearFrom;
    public ComboBox comboBoxYearTo;

    @FXML
    private void initialize() {
        myCombobox.getItems().addAll(comboBoxValues);
        myComboboxFav.getItems().addAll(comboBoxValues);
        ////
        textfieldSearch.setOnKeyTyped((event) -> searchMovies(event));
        textfieldSearch.setOnAction((event) -> searchMoviesEnter());

        // myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        myListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> showFilm());
        myListViewFav.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> showFavFilm());

        myComboboxFav.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                // System.out.println(ov);
                // System.out.println(t);
                System.out.println("Combobox-Auswahl: " + t1);
                if (t1 != null)
                    sortFavSearchList(t1);
            }
        });

        myCombobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                // System.out.println(ov);
                // System.out.println(t);
                System.out.println("Combobox-Auswahl: " + t1);
                if (t1 != null)
                    sortSearchList(t1);
            }
        });

        comboBoxYearTo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                // System.out.println(ov);
                // System.out.println(t);
                System.out.println("Combobox-Auswahl: " + t1);
                filterYears();
            }
        });

        comboBoxYearFrom.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                // System.out.println(ov);
                // System.out.println(t);
                System.out.println("Combobox-Auswahl: " + t1);
                filterYears();
            }
        });

        // Movie Objekte instanzieren
        movies = new Movies();
        favoriteMovies = new Favorites();

        // Genre Objekt instanzieren
        genres = new MovieGenres();
        genres = movieDb.ApiQueryGenres();

        // listView mit MovieListe aus Klasse verbinden
        myListView.itemsProperty().bind(MovieDb.moviesList);

        favoriteList = FXCollections.observableArrayList();
        myListViewFav.setItems(favoriteList);
        myListViewFav.itemsProperty().bind(Favorites.favList);

        // Suchliste mit Filter verbinden
        // filteredMovies = new FilteredList<>(movieDb.moviesList, s -> true);

        toFavouritelistButton.setVisible(false);
        toReminderlistButton.setVisible(false);

        imageViewStarFav1.setVisible(false);
        imageViewStarFav2.setVisible(false);
        imageViewStarFav3.setVisible(false);
        imageViewStarFav4.setVisible(false);
        imageViewStarFav5.setVisible(false);

        deleteFavouritelistButtonFav.setVisible(false);
        toReminderlistButtonFav.setVisible(false);

        // Bishergie Favoriten aus Lister importieren
        System.out.println("Local Path: " + localPath);
        favoriteMovies.moviesFromFile(localPath);
        favoriteList.addAll(favoriteMovies.FavMovies2List());
    }

    public void setData(String... comboBoxValues) {
        this.comboBoxValues = comboBoxValues;
    }

    public void showFilm() {
        Object selectedObj = myListView.getSelectionModel().getSelectedItem();

        if (selectedObj != null) {
            String selectedTitle = myListView.getSelectionModel().getSelectedItem().toString();
            System.out.println("Ausgewaehlter Film: " + selectedTitle);

            imageViewMovie.setImage(null);
            labelDetail.setText(null);

            try {
                Movies.Results selectedMovie = Movies.Results.class.cast(selectedObj);

                String movieDetail = movies.showDetails(selectedMovie, genres);
                String movieUrl = movies.getMovieUrl(selectedMovie);


                if(movieUrl != null && movieUrl.length() > 0)
                {
                    Image image = new Image("https://image.tmdb.org/t/p/w500" + movieUrl);
                    imageViewMovie.setFitHeight(image.getHeight() / 2.0);
                    imageViewMovie.setFitWidth(image.getWidth() / 2.0);
                    imageViewMovie.setImage(image);
                    imageViewMovie.setX(10);
                    imageViewMovie.setY(10);
                }

                labelDetail.setText(movieDetail);
                labelDetail.setMaxWidth(300);

                toFavouritelistButton.setVisible(true);
                toReminderlistButton.setVisible(true);

            } catch (Exception ex) {
                System.out.print("Fehler beim Anzeigen der Filmdetails: " + ex.getMessage());
            }
        } else
            System.out.print("Objekt konnte nicht aus Suchliste gelesen werden");
    }

    public void searchMovies(KeyEvent event) {
        List<String> years = new ArrayList<>();
        String search = textfieldSearch.getText();
        KeyCode code = event.getCode();
        if (code != KeyCode.SPACE) {
            if (search.length() >= 2) {
                System.out.println("Auto ver an");
                try {
                    // Neuen Executor Service starten
                    System.out.println("Suche nach: " + search);

                    exeService = Executors.newCachedThreadPool();

                    System.out.println("### ### ### Current Threads: " + Thread.activeCount());
                    System.out.println("### ### ### Current Thread Group: " + Thread.currentThread().getThreadGroup());

                    System.out.println("exeService: " + exeService.toString());
                    movieDb.setSearchString(search);
                    exeService.execute(movieDb);

                    // Executor Service beenden
                    exeService.shutdown();
                    exeService.awaitTermination(250, TimeUnit.MILLISECONDS);
                    System.out.println("exeService: " + exeService.toString());

                    // Vorhandene Jahre der Filme ermitteln und in Comboboxen schreiben
                    comboBoxYearFrom.setValue(null);
                    comboBoxYearTo.setValue(null);
                    years = movieDb.getMovies().getYearsofMovies();
                    years.sort(Comparator.naturalOrder());
                    for (String item : years) {
                        if (!comboBoxYearFrom.getItems().contains(item)) {
                            comboBoxYearFrom.getItems().add(item);
                            comboBoxYearTo.getItems().add(item);
                        }
                    }

                    // Erstes bzw. letztes Element auswaehlen
                    comboBoxYearFrom.getSelectionModel().select(0);
                    comboBoxYearTo.getSelectionModel().select(comboBoxYearTo.getItems().size() - 1);

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            } else {
                //System.out.println("auto aus");
            }
        }
    }

    public void searchMoviesFav() {
        if (textfieldSearchFav.getText().length() >= 3) {
            System.out.println("Auto ver an");
        } else {
            //System.out.println("auto aus");
        }
    }

    public void searchMoviesEnter() {
        //System.out.println("Search");
        String[] films = {"Spi", "Spi 2", "Endo"}; // hier methoden aufruf für suche
        fillListView(films);
    }

    public void fillListView(String... films) {
        myListView.getItems().clear();
        myListView.getItems().addAll(films);
    }

    /*
    public void changeImageStarToFull()
    {
        changeImageStarFull(1);
    }
    public void changeImageStarToFull2()
    {
        changeImageStarFull(2);
    }
    public void changeImageStarToFull3()
    {
        changeImageStarFull(3);
    }
    public void changeImageStarToFull4()
    {
        changeImageStarFull(4);
    }
    public void changeImageStarToFull5()
    {
        changeImageStarFull(5);
    }

    public void changeImageStarToEmpty()
    {
        changeImageStarEmpty(1);
    }
    public void changeImageStarToEmpty2()
    {
        changeImageStarEmpty(2);
    }
    public void changeImageStarToEmpty3()
    {
        changeImageStarEmpty(3);
    }
    public void changeImageStarToEmpty4()
    {
        changeImageStarEmpty(4);
    }
    public void changeImageStarToEmpty5()
    {
        changeImageStarEmpty(5);
    }
    */

    public void changeImageStarToFullFav() {
        changeImageStarFullFav(1);
    }

    public void changeImageStarToFullFav2() {
        changeImageStarFullFav(2);
    }

    public void changeImageStarToFullFav3() {
        changeImageStarFullFav(3);
    }

    public void changeImageStarToFullFav4() {
        changeImageStarFullFav(4);
    }

    public void changeImageStarToFullFav5() {
        changeImageStarFullFav(5);
    }

    public void changeImageStarToEmptyFav() {
        changeImageStarEmptyFav(1);
    }

    public void changeImageStarToEmptyFav2() {
        changeImageStarEmptyFav(2);
    }

    public void changeImageStarToEmptyFav3() {
        changeImageStarEmptyFav(3);
    }

    public void changeImageStarToEmptyFav4() {
        changeImageStarEmptyFav(4);
    }

    public void changeImageStarToEmptyFav5() {
        changeImageStarEmptyFav(5);
    }

    /*
    public void changeImageStarFull(int x)
    {
        Image myImage = new Image("SternVoll.png");
        Image myImage2 = new Image("SternLeer.png");

        switch(x)
        {
            case 1:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage2);
                imageViewStar3.setImage(myImage2);
                imageViewStar4.setImage(myImage2);
                imageViewStar5.setImage(myImage2);
                break;
            case 2:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                imageViewStar3.setImage(myImage2);
                imageViewStar4.setImage(myImage2);
                imageViewStar5.setImage(myImage2);
                break;
            case 3:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                imageViewStar3.setImage(myImage);
                imageViewStar4.setImage(myImage2);
                imageViewStar5.setImage(myImage2);
                break;
            case 4:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                imageViewStar3.setImage(myImage);
                imageViewStar4.setImage(myImage);
                imageViewStar5.setImage(myImage2);
                break;
            case 5:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                imageViewStar3.setImage(myImage);
                imageViewStar4.setImage(myImage);
                imageViewStar5.setImage(myImage);
                break;
        }
    }

    public void changeImageStarEmpty(int x)
    {
        Image myImage = new Image("SternLeer.png");

        switch(x)
        {
            case 1:
                imageViewStar1.setImage(myImage);
                break;
            case 2:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                break;
            case 3:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                imageViewStar3.setImage(myImage);
                break;
            case 4:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                imageViewStar3.setImage(myImage);
                imageViewStar4.setImage(myImage);
                break;
            case 5:
                imageViewStar1.setImage(myImage);
                imageViewStar2.setImage(myImage);
                imageViewStar3.setImage(myImage);
                imageViewStar4.setImage(myImage);
                imageViewStar5.setImage(myImage);
                break;
        }
    }
    */

    public void changeImageStarFullFav(int x) {
        Image myImage = new Image("SternVoll.png");
        Image myImage2 = new Image("SternLeer.png");

        switch(x)
        {
            case 1:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage2);
                imageViewStarFav3.setImage(myImage2);
                imageViewStarFav4.setImage(myImage2);
                imageViewStarFav5.setImage(myImage2);
                break;
            case 2:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                imageViewStarFav3.setImage(myImage2);
                imageViewStarFav4.setImage(myImage2);
                imageViewStarFav5.setImage(myImage2);
                break;
            case 3:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                imageViewStarFav3.setImage(myImage);
                imageViewStarFav4.setImage(myImage2);
                imageViewStarFav5.setImage(myImage2);
                break;
            case 4:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                imageViewStarFav3.setImage(myImage);
                imageViewStarFav4.setImage(myImage);
                imageViewStarFav5.setImage(myImage2);
                break;
            case 5:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                imageViewStarFav3.setImage(myImage);
                imageViewStarFav4.setImage(myImage);
                imageViewStarFav5.setImage(myImage);
                break;
        }
    }

    public void changeImageStarEmptyFav(int x) {
        Image myImage = new Image("SternLeer.png");

        switch(x)
        {
            case 1:
                imageViewStarFav1.setImage(myImage);
                break;
            case 2:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                break;
            case 3:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                imageViewStarFav3.setImage(myImage);
                break;
            case 4:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                imageViewStarFav3.setImage(myImage);
                imageViewStarFav4.setImage(myImage);
                break;
            case 5:
                imageViewStarFav1.setImage(myImage);
                imageViewStarFav2.setImage(myImage);
                imageViewStarFav3.setImage(myImage);
                imageViewStarFav4.setImage(myImage);
                imageViewStarFav5.setImage(myImage);
                break;
        }
    }

    public void addToFavorites() {
            Object selectedObj = myListView.getSelectionModel().getSelectedItem();

            if (selectedObj != null)
            {
                String title = myListView.getSelectionModel().getSelectedItem().toString();
                System.out.println("Film " + title + " wird den Favoriten hinzugefuegt");

                try
                {
                    Movies.Results selectedMovie = Movies.Results.class.cast(selectedObj);
                    if(!favoriteMovies.getFavorites().contains(selectedMovie)) {
                        favoriteMovies.getFavorites().add(selectedMovie);
                        favoriteMovies.addFavorites(selectedMovie);
                        favoriteMovies.Movies2File(localPath);
                    }
                    else
                        System.out.println("Ausgewaehlter Fiml schon Favoriten enthalten");
                }
                catch(Exception ex)
                {
                    System.out.println("Fehler beim Hinzufuegen der Favoriten: " + ex.getMessage());
                }
            }
            else
                System.out.println("Objekt konnte nicht aus Liste ermittelt werden");
    }

    public void delteFromFavorites() {
        try
        {
            Object selectedObj = myListViewFav.getSelectionModel().getSelectedItem();
            if (selectedObj != null) {
                String title = myListViewFav.getSelectionModel().getSelectedItem().toString();
                System.out.println("Film " + title + " wird aus den Favoriten geloescht");
                Movies.Results selectedMovie = Movies.Results.class.cast(selectedObj);
                if(favoriteMovies.getFavorites().contains(selectedMovie))
                {
                    try
                    {
                        favoriteMovies.deleteFavorites(selectedMovie);
                        favoriteMovies.Movies2File(localPath);
                    } catch (Exception ex) {
                        System.out.println("Fehler beim Loeschen aus Liste: " + ex.getMessage());
                    }
                }
                else
                    System.out.print("Film konnte nicht in Liste gefunden werden");
            }
            else
                System.out.println("Film konnte nicht ausgewaehlt werden.");
        }
        catch(Exception ex)
            {
                System.out.println(ex);
            }
    }


    public void showFavFilm() {
        int rating = -1;

        Object selectedObj = myListViewFav.getSelectionModel().getSelectedItem();

        if(selectedObj != null) {
            String selectedTitle = myListViewFav.getSelectionModel().getSelectedItem().toString();
            System.out.println("Ausgewaehlter Film: " + selectedTitle);

            labelFavDetail.setText(null);

            imageViewStarFav1.setVisible(true);
            imageViewStarFav2.setVisible(true);
            imageViewStarFav3.setVisible(true);
            imageViewStarFav4.setVisible(true);
            imageViewStarFav5.setVisible(true);

            deleteFavouritelistButtonFav.setVisible(true);
            toReminderlistButtonFav.setVisible(true);

            // Erstmal die Sterne wieder ablöschen
            changeImageStarToEmptyFav();
            changeImageStarToEmptyFav2();
            changeImageStarToEmptyFav3();
            changeImageStarToEmptyFav4();
            changeImageStarToEmptyFav5();

            Movies.Results selectedMovie = Movies.Results.class.cast(selectedObj);

            deleteFavouritelistButtonFav.setVisible(true);
            toReminderlistButtonFav.setVisible(true);

            try {
                String movieDetail = favoriteMovies.showDetails(selectedMovie, genres);
                String movieUrl = favoriteMovies.getMovieUrl(selectedMovie);

                if(movieUrl != null && movieUrl.length() > 0)
                {
                    Image image = new Image("https://image.tmdb.org/t/p/w500" + movieUrl);
                    imageViewMovie.setFitHeight(image.getHeight() / 2.0);
                    imageViewMovie.setFitWidth(image.getWidth() / 2.0);
                    labelFavDetail.setMaxWidth(image.getWidth());
                    labelFavDetail.setMinHeight(image.getHeight());
                }

                labelFavDetail.setText(movieDetail);
                labelFavDetail.setStyle("-fx-background-image: " + "url(https://image.tmdb.org/t/p/w500" + movieUrl + ");");

                // Rating (Sterne) der Filme anzeigen
                rating = favoriteMovies.getFavRating(selectedMovie);

                switch (rating) {
                    case 1:
                        changeImageStarToFullFav();
                        break;
                    case 2:
                        changeImageStarToFullFav();
                        changeImageStarToFullFav2();
                        break;
                    case 3:
                        changeImageStarToFullFav();
                        changeImageStarToFullFav2();
                        changeImageStarToFullFav3();
                        break;
                    case 4:
                        changeImageStarToFullFav();
                        changeImageStarToFullFav2();
                        changeImageStarToFullFav3();
                        changeImageStarToFullFav4();
                        break;
                    case 5:
                        changeImageStarToFullFav();
                        changeImageStarToFullFav2();
                        changeImageStarToFullFav3();
                        changeImageStarToFullFav4();
                        changeImageStarToFullFav5();
                        break;
                    default:
                }
            }
            catch(Exception ex)
            {
                System.out.println("Fehler beim Abfragen der Filme aus dem Movie Objekt: " + ex.getMessage());
            }
        }
        else
        {
            System.out.println("Fehler beim Einlesen des Objekts aus der Suchliste");
        }
    }

    public void changeImageStarToRating(int rating) {
        try {
            String title = myListViewFav.getSelectionModel().getSelectedItem().toString();

            for (Movies.Results item : favoriteMovies.getFavorites()){
                if (item.getTitle() == title){
                    item.setRating(rating);
                }
            }

            favoriteMovies.Movies2File(localPath);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
    public void changeImageStarToRatingOne() {
        changeImageStarToRating(1);
        changeImageStarToFullFav();
    }
    public void changeImageStarToRatingTwo() {
        changeImageStarToRating(2);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
    }
    public void changeImageStarToRatingThree() {
        changeImageStarToRating(3);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
    }
    public void changeImageStarToRatingFour() {
        changeImageStarToRating(4);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
        changeImageStarToFullFav4();
    }
    public void changeImageStarToRatingFive() {
        changeImageStarToRating(5);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
        changeImageStarToFullFav4();
        changeImageStarToFullFav5();
    }

    public void sortFavSearchList(String selection) {
        try
        {
            if (selection == "A bis Z")
                favoriteMovies.favList.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
            else
                favoriteMovies.favList.sort((o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
        }
        catch(Exception ex)
        {
            System.out.println("Fehler beim Sortieren der Filme");
        }

    }

    public void sortSearchList(String selection) {

        try
        {
            if (selection == "A bis Z")
                movieDb.moviesList.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
            else
                movieDb.moviesList.sort((o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
        }
        catch(Exception ex)
        {
            System.out.println("Fehler beim Sortieren der Filme");
        }
    }

    public void filterYears() {
        int yearFromInt = -1;
        int yearToInt = -1;

        try {
            yearFromInt = Integer.parseInt(comboBoxYearFrom.getSelectionModel().getSelectedItem().toString());
            yearToInt = Integer.parseInt(comboBoxYearTo.getSelectionModel().getSelectedItem().toString());
        }
        catch(Exception ex)
        {
            System.out.println("Fehler beim Lesen der Grenzen");
        }

        if(yearFromInt != -1 && yearToInt != -1) {
            try {
                System.out.println("Filter zwischen " + yearFromInt + " und " + yearToInt);
                movieDb.filterMovies(yearFromInt, yearToInt);
            } catch (Exception ex) {
                System.out.println("Exception beim Filtern der Filme: " + ex.getMessage());

            }
        }
        else
            System.out.println("Grenzen fuer Filterung konnten nicht eingelesen werden");
    }

}

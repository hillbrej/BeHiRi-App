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
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

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
    Reminds remindedMovies;

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
    public TabPane tabPane;

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
    public ObservableList<String> remindedList;
    public Label labelDetail;
    public Label labelFavDetail;
    public ComboBox comboBoxYearFrom;
    public ComboBox comboBoxYearTo;
    public Pane paneFavBackground;
    public Label labelOverview;
    public ListView listViewGenres;

    //rem
    public TextField textfieldSearchRem;
    public ComboBox myComboboxRem;
    public ListView myListViewRem;
    public Pane paneRemBackground;
    public Label labelRemDetail;
    public ImageView imageViewStarRem1;
    public ImageView imageViewStarRem2;
    public ImageView imageViewStarRem3;
    public ImageView imageViewStarRem4;
    public ImageView imageViewStarRem5;
    public Button deleteRemindlistButtonRem;
    public Button toFavoritelistButtonRem;

    public ComboBox stylesheetComboBox;
    public Label shortInfoLabel;

    public Label filterLabel;
    public Label listingLabel;
    public Label fromLabel;
    public Label toLabel;



    public String initCombo = "Klassisch";

    @FXML
    private void initialize() {

        myCombobox.setTooltip(new Tooltip("Sortieren der Sucheingabe"));
        myListView.setTooltip(new Tooltip("Eintrag wählen um anzuzeigen"));
        textfieldSearch.setTooltip(new Tooltip("Online suche mit THEMOVIE.db"));
        toFavouritelistButton.setTooltip(new Tooltip("Aktueller Eintrag in Favoritenliste aufnehmen"));
        toReminderlistButton.setTooltip(new Tooltip("Aktueller Eintrag in Merkliste aufnehemen"));
        myComboboxFav.setTooltip(new Tooltip("Ergebnisse sortieren"));
        myListViewFav.setTooltip(new Tooltip("Gefilterte Liste der Favoriten"));
        textfieldSearchFav.setTooltip(new Tooltip("Favoriteneintrag suchen"));

        deleteFavouritelistButtonFav.setTooltip(new Tooltip("Aktueller Eintrag aus Favoritenliste entfernen"));
        toReminderlistButtonFav.setTooltip(new Tooltip("Aktueller Eintrag in Merkliste aufnehmen"));
        comboBoxYearFrom.setTooltip(new Tooltip("Aktuelle Ergebnisse nach Erscheinungsdatum filtern"));
        comboBoxYearTo.setTooltip(new Tooltip("Aktuelle Ergebnisse nach Erscheinungsdatum filtern"));
        listViewGenres.setTooltip(new Tooltip("Aktuelle Ergebnisse nach Genres sortieren"));
        textfieldSearchRem.setTooltip(new Tooltip("Merklisteneintrag suchen"));
        myComboboxRem.setTooltip(new Tooltip("Ergebnisse sortieren"));
        myListViewRem.setTooltip(new Tooltip("Gefilterte Liste der Merkliste"));

        deleteRemindlistButtonRem.setTooltip(new Tooltip("Aktueller Eintrag aus Merkliste entfernen"));
        toFavoritelistButtonRem.setTooltip(new Tooltip("Aktueller Eintrag in Favoritenliste aufnehmen"));

        stylesheetComboBox.setTooltip(new Tooltip("Erscheinung der Öberfläche ändern"));

        /*button.setTooltip(new Tooltip("Tooltip for Button"));*/
        myCombobox.getItems().addAll(comboBoxValues);
        myComboboxFav.getItems().addAll(comboBoxValues);
        myComboboxRem.getItems().addAll(comboBoxValues);

        stylesheetComboBox.getItems().addAll("Klassisch", "Fanzy", "Crank");
        stylesheetComboBox.setValue(initCombo);
        ////
        textfieldSearch.setOnKeyTyped((event) -> searchMovies(event));
        textfieldSearch.setOnAction((event) -> searchMoviesEnter());

        // myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        myListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> showFilm());
        myListViewFav.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> showFavFilm());
        myListViewRem.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> showRemFilm());
        listViewGenres.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> filterGenres());
/*hier*/

        myComboboxRem.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                // System.out.println(ov);
                // System.out.println(t);
                System.out.println("Combobox-Auswahl: " + t1);
                if (t1 != null)
                    sortRemSearchList(t1);
            }
        });

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

        stylesheetComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                System.out.println("Combobox-Auswahl: " + t1);
                loadStylesheet(t1);
            }
        });

        // Movie Objekte instanzieren
        movies = new Movies();
        favoriteMovies = new Favorites();
        remindedMovies = new Reminds();

        // Genre Objekt instanzieren
        genres = new MovieGenres();
        genres = movieDb.ApiQueryGenres();

        // listView mit MovieListe aus Klasse verbinden
        myListView.itemsProperty().bind(MovieDb.moviesList);

        favoriteList = FXCollections.observableArrayList();
        remindedList = FXCollections.observableArrayList();

        myListViewRem.setItems(remindedList);
        myListViewRem.itemsProperty().bind(Reminds.remList);
        myListViewFav.setItems(favoriteList);
        myListViewFav.itemsProperty().bind(Favorites.favList);

        comboBoxYearFrom.itemsProperty().bind(movieDb.yearsFrom);
        comboBoxYearTo.itemsProperty().bind(movieDb.yearsTo);

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

        imageViewStarRem1.setVisible(false);
        imageViewStarRem2.setVisible(false);
        imageViewStarRem3.setVisible(false);
        imageViewStarRem4.setVisible(false);
        imageViewStarRem5.setVisible(false);

        deleteRemindlistButtonRem.setVisible(false);
        toFavoritelistButtonRem.setVisible(false);

        filterLabel.setVisible(false);
        listingLabel.setVisible(false);
        fromLabel.setVisible(false);
        toLabel.setVisible(false);

        comboBoxYearFrom.setVisible(false);
        comboBoxYearTo.setVisible(false);

        listViewGenres.setVisible(false);

        // Logos anzeigen
        paneFavBackground.setStyle("-fx-background-image: url(movieDbLogo.png);");
        paneFavBackground.setPrefWidth(312);
        paneFavBackground.setPrefHeight(276);
        paneFavBackground.setMinHeight(276);
        paneFavBackground.setMinWidth(312);

        paneRemBackground.setStyle("-fx-background-image: url(movieDbLogo.png);");
        paneRemBackground.setPrefWidth(312);
        paneRemBackground.setPrefHeight(276);
        paneRemBackground.setMinHeight(276);
        paneRemBackground.setMinWidth(312);

        Image imageStart = new Image("movieDbLogo.png");
        imageViewMovie.setImage(imageStart);

        // Bishergie Favoriten aus Lister importieren
        System.out.println("Local Path: " + localPath);

        remindedMovies.moviesFromFile(localPath);
        remindedList.addAll(remindedMovies.RemMovies2List());
        favoriteMovies.moviesFromFile(localPath);
        favoriteList.addAll(favoriteMovies.FavMovies2List());
    }

    public void searchMoviesRem(){}
    public void deleteFromReminder(){
        try
        {
            Object selectedObj = myListViewRem.getSelectionModel().getSelectedItem();

            if (selectedObj != null) {
                String title = myListViewRem.getSelectionModel().getSelectedItem().toString();
                System.out.println("Film " + title + " wird aus den Merkliste geloescht");
                Movies.Results selectedMovie = Movies.Results.class.cast(selectedObj);
                if(remindedMovies.getReminders().contains(selectedMovie))
                {
                    try
                    {
                        remindedMovies.deleteReminders(selectedMovie);
                        remindedMovies.Movies2File(localPath);
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
    public void addToReminded(){
        Object selectedObj = myListView.getSelectionModel().getSelectedItem();

        if (selectedObj != null)
        {
            String title = myListView.getSelectionModel().getSelectedItem().toString();
            System.out.println("Film " + title + " wird zur Merkliste hinzugefuegt");

            try
            {
                Movies.Results selectedMovie = Movies.Results.class.cast(selectedObj);
                if(!remindedMovies.isIdInReminders(selectedMovie.getId()))
                {
                    remindedMovies.getReminders().add(selectedMovie);
                    remindedMovies.addReminders(selectedMovie);
                    remindedMovies.Movies2File(localPath);
                }
                else
                    System.out.println("Ausgewaehlter Fiml schon in Merk enthalten");
            }
            catch(Exception ex)
            {
                System.out.println("Fehler beim Hinzufuegen der Favoriten: " + ex.getMessage());
            }
        }
        else
            System.out.println("Objekt konnte nicht aus Liste ermittelt werden");
    }

    public void addToReminded2(){
        Object selectedObj = myListViewFav.getSelectionModel().getSelectedItem();

        if (selectedObj != null)
        {
            String title = myListViewFav.getSelectionModel().getSelectedItem().toString();
            System.out.println("Film " + title + " wird zur Merkliste hinzugefuegt");

            try
            {
                Reminds.Results selectedMovie = Reminds.Results.class.cast(selectedObj);
                if(!remindedMovies.isIdInReminders(selectedMovie.getId()))
                {
                    remindedMovies.getReminders().add(selectedMovie);
                    remindedMovies.addReminders(selectedMovie);
                    remindedMovies.Movies2File(localPath);
                }
                else
                    System.out.println("Ausgewaehlter Fiml schon in Merk enthalten");
            }
            catch(Exception ex)
            {
                System.out.println("Fehler beim Hinzufuegen der Favoriten: " + ex.getMessage());
            }
        }
        else
            System.out.println("Objekt konnte nicht aus Liste ermittelt werden");
    }

    public void loadStylesheet(String sheet)
    {
        if(sheet.equals("Klassisch"))
        {
            tabPane.getStylesheets().clear();
            tabPane.getStylesheets().add("Main/stylesheet.css");
        }
        if(sheet.equals("Fanzy"))
        {
            tabPane.getStylesheets().clear();
            tabPane.getStylesheets().add("Main/stylesheet2.css");
        }
        if(sheet.equals("Crank"))
        {
            tabPane.getStylesheets().clear();
            tabPane.getStylesheets().add("Main/stylesheet3.css");
        }

        //tabPane.setStyle("@stylesheet.css");
        //shortInfoLabel.setStyle("-fx-font-family: sample; -fx-font-size: 80;");
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

                // String movieDetail = movies.showDetails(selectedMovie, genres);

                String movieDetail = movies.showDetailsWithoutOverview(selectedMovie,genres);
                String movieOverview = movies.showDetailsOverview(selectedMovie);

                String movieUrl = movies.getMovieUrl(selectedMovie);

                Image image;

                if(movieUrl != null && movieUrl.length() > 0)
                {
                    image = new Image("https://image.tmdb.org/t/p/w500" + movieUrl);
                }
                else
                {
                    image = new Image("movieDbLogo.png");
                }

                imageViewMovie.setFitHeight(image.getHeight() / 2.0);
                imageViewMovie.setFitWidth(image.getWidth() / 2.0);
                imageViewMovie.setImage(image);

                labelDetail.setText(movieDetail);
                labelOverview.setText(movieOverview);


                toFavouritelistButton.setVisible(true);
                toReminderlistButton.setVisible(true);
                /////////////////
                filterLabel.setVisible(true);
                listingLabel.setVisible(true);
                fromLabel.setVisible(true);
                toLabel.setVisible(true);

                comboBoxYearFrom.setVisible(true);
                comboBoxYearTo.setVisible(true);

                listViewGenres.setVisible(true);

            } catch (Exception ex) {
                System.out.print("Fehler beim Anzeigen der Filmdetails: " + ex.getMessage());
            }
        } else
            System.out.print("Objekt konnte nicht aus Suchliste gelesen werden");
    }

    public void searchMovies(KeyEvent event) {
        List<Integer> years = new ArrayList<>();
        List<String> yearsToList = new ArrayList<>();
        String search = textfieldSearch.getText();
        KeyCode code = event.getCode();
        if (code != KeyCode.SPACE) {
            if (search.length() >= 2) {
                System.out.println("Auto ver an");

                tabPane.cursorProperty().setValue(Cursor.WAIT);
                try {
                    comboBoxYearFrom.getSelectionModel().select(null);
                    comboBoxYearTo.getSelectionModel().select(null);

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
                    exeService.awaitTermination(200, TimeUnit.MILLISECONDS);
                    System.out.println("exeService: " + exeService.toString());

                    // Vorhandene Jahre der Filme ermitteln und in Comboboxen schreiben
                    /*
                    comboBoxYearFrom.setItems(null);

                    comboBoxYearFrom.setItems(null);

                    years = movieDb.getMovies().getYearsofMovies();
                    years.sort(Comparator.naturalOrder());

                    for(int item : years)
                    {
                        if(!years.contains(item) && item !=0)
                            yearsToList.add(Integer.toString(item));
                    }

                    // comboBoxYearFrom.setItems(FXCollections.observableArrayList(yearsToList));
                    // comboBoxYearTo.setItems(FXCollections.observableArrayList(yearsToList));

                    comboBoxYearFrom.getItems().addAll(yearsToList);
                    comboBoxYearTo.getItems().addAll(yearsToList);


                    /*for (String item : years) {
                        if (!comboBoxYearFrom.getItems().contains(item)) {
                            comboBoxYearFrom.getItems().add(item);
                            comboBoxYearTo.getItems().add(item);
                        }
                    }*/

                    // Erstes bzw. letztes Element auswaehlen
                    // if(comboBoxYearFrom.getItems().size() > 0)
                    //   comboBoxYearFrom.getSelectionModel().select(0);

                    // if(comboBoxYearTo.getItems().size() > 1)
                    //    comboBoxYearTo.getSelectionModel().select(comboBoxYearTo.getItems().size() - 1);

                    // Combobox mit Genres fuellen
                    for(MovieGenres.Genres item : genres.genres)
                    {
                        //choiceBoxGenres.getItems().add(item.getName());
                        listViewGenres.getItems().add(item.getName());
                    }

                }
                catch (Exception ex) {
                    System.out.println(ex.toString());
                } finally {
                    tabPane.cursorProperty().setValue(Cursor.DEFAULT);
                }
            }
            else {
                //System.out.println("auto aus");
            }
        }
    }
/*
*
* hiiiiiiiiiiiiiiiiieeeeeeeeeeeeeeeeerrrrrrrrrrrrrrrrrrrr
*
* */
    public void searchMoviesFav() {


        /*ObservableList<String> placeholder;
        placeholder = FXCollections.observableArrayList();

        placeholder = favoriteList;

        if(textfieldSearchFav.getText().length() != 0)
        {
            placeholder.clear();


            for (String title: favoriteList)
            {
                System.out.println(textfieldSearchFav.getText());
                if(title.contains(textfieldSearchFav.getText()))
                {
                    placeholder.add(title);
                    System.out.println(title);
                }
            }
            System.out.println(textfieldSearchFav.getText());
        }
        */
        
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
                    if(!favoriteMovies.isIdInFavorites(selectedMovie.getId()))
                    {
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

    public void addToFavorites2() {
        Object selectedObj = myListViewRem.getSelectionModel().getSelectedItem();

        if (selectedObj != null)
        {
            String title = myListViewRem.getSelectionModel().getSelectedItem().toString();
            System.out.println("Film " + title + " wird den Favoriten hinzugefuegt");

            try
            {
                Favorites.Results selectedMovie = Favorites.Results.class.cast(selectedObj);
                if(!favoriteMovies.isIdInFavorites(selectedMovie.getId()))
                {
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

                    paneFavBackground.setPrefHeight(image.getHeight() / 1.0);
                    paneFavBackground.setPrefWidth(image.getWidth() / 1.0);

                    labelFavDetail.setMaxWidth(image.getWidth());
                    labelFavDetail.setMinWidth(image.getWidth());
                    labelFavDetail.setMinHeight(image.getHeight());
                    labelFavDetail.setMaxHeight(image.getHeight());

                    paneFavBackground.setStyle("-fx-background-image: " + "url(https://image.tmdb.org/t/p/w500" + movieUrl + ");");
                }
                else
                {
                    paneFavBackground.setStyle("-fx-background-image: url(movieDbLogo.png);");
                    paneFavBackground.setPrefWidth(312);
                    paneFavBackground.setPrefHeight(276);
                    paneFavBackground.setMinHeight(276);
                    paneFavBackground.setMinWidth(312);
                }

                labelFavDetail.setText(movieDetail);
                labelFavDetail.setStyle("-fx-background-color: rgba(211, 211, 211, 0.6);");

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
/////////hier
    public void showRemFilm() {
        int rating = -1;

        Object selectedObj = myListViewRem.getSelectionModel().getSelectedItem();

        if(selectedObj != null) {
            String selectedTitle = myListViewRem.getSelectionModel().getSelectedItem().toString();
            System.out.println("Ausgewaehlter Film: " + selectedTitle);

            labelRemDetail.setText(null);

            imageViewStarRem1.setVisible(true);
            imageViewStarRem2.setVisible(true);
            imageViewStarRem3.setVisible(true);
            imageViewStarRem4.setVisible(true);
            imageViewStarRem5.setVisible(true);

            deleteRemindlistButtonRem.setVisible(true);
            toFavoritelistButtonRem.setVisible(true);

            // Erstmal die Sterne wieder ablöschen
            Image myImageStar = new Image("SternLeer.png");

            imageViewStarRem1.setImage(myImageStar);
            imageViewStarRem2.setImage(myImageStar);
            imageViewStarRem3.setImage(myImageStar);
            imageViewStarRem4.setImage(myImageStar);
            imageViewStarRem5.setImage(myImageStar);
            /*changeImageStarToEmptyRem();
            changeImageStarToEmptyRem2();
            changeImageStarToEmptyRem3();
            changeImageStarToEmptyRem4();
            changeImageStarToEmptyRem5();*/
///////hier
            Movies.Results selectedMovie = Movies.Results.class.cast(selectedObj);

            deleteFavouritelistButtonFav.setVisible(true);
            toReminderlistButtonFav.setVisible(true);

            try {
                String movieDetail = remindedMovies.showDetails(selectedMovie, genres);
                String movieUrl = remindedMovies.getMovieUrl(selectedMovie);

                if(movieUrl != null && movieUrl.length() > 0)
                {
                    Image image = new Image("https://image.tmdb.org/t/p/w500" + movieUrl);

                    paneRemBackground.setPrefHeight(image.getHeight() / 1.0);
                    paneRemBackground.setPrefWidth(image.getWidth() / 1.0);

                    labelRemDetail.setMaxWidth(image.getWidth());
                    labelRemDetail.setMinWidth(image.getWidth());
                    labelRemDetail.setMinHeight(image.getHeight());
                    labelRemDetail.setMaxHeight(image.getHeight());

                    paneRemBackground.setStyle("-fx-background-image: " + "url(https://image.tmdb.org/t/p/w500" + movieUrl + ");");
                }
                else
                {
                    paneRemBackground.setStyle("-fx-background-image: url(movieDbLogo.png);");
                    paneRemBackground.setPrefWidth(312);
                    paneRemBackground.setPrefHeight(276);
                    paneRemBackground.setMinHeight(276);
                    paneRemBackground.setMinWidth(312);
                }

                labelRemDetail.setText(movieDetail);
                labelRemDetail.setStyle("-fx-background-color: rgba(211, 211, 211, 0.6);");

                // Rating (Sterne) der Filme anzeigen
                rating = remindedMovies.getFavRating(selectedMovie);

                Image myImageStar2 = new Image("SternVoll.png");

                switch (rating) {
                    case 1:
                        imageViewStarRem1.setImage(myImageStar2);
                        //changeImageStarToFullFav();
                        break;
                    case 2:
                        imageViewStarRem1.setImage(myImageStar2);
                        imageViewStarRem2.setImage(myImageStar2);
                        //changeImageStarToFullFav();
                        //changeImageStarToFullFav2();
                        break;
                    case 3:
                        imageViewStarRem1.setImage(myImageStar2);
                        imageViewStarRem2.setImage(myImageStar2);
                        imageViewStarRem3.setImage(myImageStar2);
                        break;
                    case 4:
                        imageViewStarRem1.setImage(myImageStar2);
                        imageViewStarRem2.setImage(myImageStar2);
                        imageViewStarRem3.setImage(myImageStar2);
                        imageViewStarRem4.setImage(myImageStar2);
                        break;
                    case 5:
                        imageViewStarRem1.setImage(myImageStar2);
                        imageViewStarRem2.setImage(myImageStar2);
                        imageViewStarRem3.setImage(myImageStar2);
                        imageViewStarRem4.setImage(myImageStar2);
                        imageViewStarRem5.setImage(myImageStar2);
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

    public void changeImageStarToRating2(int rating) {
        try {
            String title = myListViewRem.getSelectionModel().getSelectedItem().toString();

            for (Movies.Results item : remindedMovies.getReminders()){
                if (item.getTitle() == title){
                    item.setRating(rating);
                }
            }

            remindedMovies.Movies2File(localPath);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void changeImageStarToRatingOne() {
        changeImageStarToRating(1);
        changeImageStarToFullFav();
    }
    public void changeImageStarToRatingOneRem() {
        changeImageStarToRating2(1);
        Image starfull = new Image("SternVoll.png");
        //changeImageStarToFullFav();
        imageViewStarRem1.setImage(starfull);
    }
    public void changeImageStarToRatingTwo() {
        changeImageStarToRating(2);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
    }
    public void changeImageStarToRatingTwoRem() {
        changeImageStarToRating2(2);
        Image starfull = new Image("SternVoll.png");
        //changeImageStarToFullFav();
        //changeImageStarToFullFav2();
        imageViewStarRem1.setImage(starfull);
        imageViewStarRem2.setImage(starfull);
    }
    public void changeImageStarToRatingThree() {
        changeImageStarToRating(3);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
    }
    public void changeImageStarToRatingThreeRem() {
        changeImageStarToRating2(3);
        /*changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();*/
        Image starfull = new Image("SternVoll.png");
        imageViewStarRem1.setImage(starfull);
        imageViewStarRem2.setImage(starfull);
        imageViewStarRem3.setImage(starfull);
    }
    public void changeImageStarToRatingFour() {
        changeImageStarToRating(4);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
        changeImageStarToFullFav4();
    }
    public void changeImageStarToRatingFourRem() {
        changeImageStarToRating2(4);
        /*changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
        changeImageStarToFullFav4();*/
        Image starfull = new Image("SternVoll.png");
        imageViewStarRem1.setImage(starfull);
        imageViewStarRem2.setImage(starfull);
        imageViewStarRem3.setImage(starfull);
        imageViewStarRem4.setImage(starfull);
    }
    public void changeImageStarToRatingFive() {
        changeImageStarToRating(5);
        changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
        changeImageStarToFullFav4();
        changeImageStarToFullFav5();
    }
    public void changeImageStarToRatingFiveRem() {
        changeImageStarToRating2(5);
        /*changeImageStarToFullFav();
        changeImageStarToFullFav2();
        changeImageStarToFullFav3();
        changeImageStarToFullFav4();
        changeImageStarToFullFav5();*/
        Image starfull = new Image("SternVoll.png");
        imageViewStarRem1.setImage(starfull);
        imageViewStarRem2.setImage(starfull);
        imageViewStarRem3.setImage(starfull);
        imageViewStarRem4.setImage(starfull);
        imageViewStarRem5.setImage(starfull);
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

    public void sortRemSearchList(String selection) {
        try
        {
            if (selection == "A bis Z")
                remindedMovies.remList.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
            else
                remindedMovies.remList.sort((o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
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

        if(comboBoxYearFrom.getSelectionModel().getSelectedItem() != null)
            yearFromInt = Integer.parseInt(comboBoxYearFrom.getSelectionModel().getSelectedItem().toString());
        else
            yearFromInt = 0;

        if(comboBoxYearTo.getSelectionModel().getSelectedItem() != null)
            yearToInt = Integer.parseInt(comboBoxYearTo.getSelectionModel().getSelectedItem().toString());
        else
            yearToInt = 9999;

        if (yearFromInt != -1 && yearToInt != -1)
        {
            try
            {
               System.out.println("Filter zwischen " + yearFromInt + " und " + yearToInt);
                movieDb.filterMovies(yearFromInt, yearToInt);
            }
            catch (Exception ex) {
                    System.out.println("Exception beim Filtern der Filme: " + ex.getMessage());
            }
        }
        else
                System.out.println("Grenzen fuer Filterung konnten nicht eingelesen werden");

    }

    public void filterGenres() {

        Object selectedObj = listViewGenres.getSelectionModel().getSelectedItem();

        if (selectedObj != null) {
            String selectedGenre = listViewGenres.getSelectionModel().getSelectedItem().toString();
            System.out.println("Ausgewaehltes Genre: " + selectedGenre);

        }
    }

}

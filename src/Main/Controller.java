package Main;

import java.io.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller{

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

    public TextArea textAreaDetail;
    public ImageView imageViewMovie;
    public ObservableList<String> favoriteList;

    @FXML
    private void initialize()
    {
        myCombobox.getItems().addAll(comboBoxValues);
        myComboboxFav.getItems().addAll(comboBoxValues);
        ////
        textfieldSearch.setOnKeyTyped((event) -> searchMovies(event));
        textfieldSearch.setOnAction((event) -> searchMoviesEnter());
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                showFilm();
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

        imageViewStar1.setVisible(false);
        imageViewStar2.setVisible(false);
        imageViewStar3.setVisible(false);
        imageViewStar4.setVisible(false);
        imageViewStar5.setVisible(false);

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

    public void setData(String... comboBoxValues)
    {
        this.comboBoxValues = comboBoxValues;
    }

    public void showFilm()
    {
        textAreaDetail.setText(null);

        String selectedMovie = myListView.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectedMovie);

        // Referenz auf Movie Liste holen
        movies = movieDb.getMovies();

        //check(myListView.getSelectionModel().getSelectedItem());
        //prüft ob bereits in 1. Favliste 2. Merkliste 3. bewertet...

        imageViewStar1.setVisible(true);
        imageViewStar2.setVisible(true);
        imageViewStar3.setVisible(true);
        imageViewStar4.setVisible(true);
        imageViewStar5.setVisible(true);

        toFavouritelistButton.setVisible(true);
        toReminderlistButton.setVisible(true);

        String movieDetail = movies.showDetails(selectedMovie, genres);
        String movieUrl = movies.getMovieUrl(selectedMovie);

        textAreaDetail.appendText(movieDetail);

        Image image = new Image("https://image.tmdb.org/t/p/w500/" + movieUrl);
        imageViewMovie.setImage(image);

    }

    public void searchMovies(KeyEvent event)
    {
        String search = textfieldSearch.getText();
        KeyCode code = event.getCode();
        if (code != KeyCode.SPACE)
        {
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
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            } else {
                //System.out.println("auto aus");
            }
        }
    }

    public void searchMoviesFav()
    {
        if (textfieldSearchFav.getText().length()>=3)
        {
            System.out.println("Auto ver an");
        }
        else
        {
            //System.out.println("auto aus");
        }
    }

    public void searchMoviesEnter()
    {
        //System.out.println("Search");
        String [] films = {"Spi", "Spi 2", "Endo"}; // hier methoden aufruf für suche
        fillListView(films);
    }

    public void fillListView(String... films)
    {
        myListView.getItems().clear();
        myListView.getItems().addAll(films);
    }

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

    public void changeImageStarToFullFav()
    {
        changeImageStarFullFav(1);
    }
    public void changeImageStarToFullFav2()
    {
        changeImageStarFullFav(2);
    }
    public void changeImageStarToFullFav3()
    {
        changeImageStarFullFav(3);
    }
    public void changeImageStarToFullFav4()
    {
        changeImageStarFullFav(4);
    }
    public void changeImageStarToFullFav5()
    {
        changeImageStarFullFav(5);
    }

    public void changeImageStarToEmptyFav()
    {
        changeImageStarEmptyFav(1);
    }
    public void changeImageStarToEmptyFav2()
    {
        changeImageStarEmptyFav(2);
    }
    public void changeImageStarToEmptyFav3()
    {
        changeImageStarEmptyFav(3);
    }
    public void changeImageStarToEmptyFav4()
    {
        changeImageStarEmptyFav(4);
    }
    public void changeImageStarToEmptyFav5()
    {
        changeImageStarEmptyFav(5);
    }

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

    public void changeImageStarFullFav(int x)
    {
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

    public void changeImageStarEmptyFav(int x)
    {
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

    public void addToFavorites()
    {
        try {
            String title = myListView.getSelectionModel().getSelectedItem().toString();

            for (Movies.Results item : movies.getResults()){
                if (item.getTitle() == title){

                    if(favoriteMovies.addFavorites(item)) {
                        favoriteList.add(title);
                        break;
                    }
                }
            }

            favoriteMovies.Movies2File(localPath);

        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }

}

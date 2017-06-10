package Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 12.05.2017.
 */
public class Movies {

    private List<Results> results = new ArrayList<>();

    public class Results {

        // Member der Movies Klasse
        private String poster_path;
        private boolean adult;
        private String overview;
        private String release_date;
        private int[] genre_ids = new int[]{};
        private int id;
        private String original_title;
        private String original_language;
        private String title;
        private String backdrop_path;
        private double popularity;
        private int vote_count;
        private boolean video;
        private double vote_average;
        private int rating;

        // GETTER-Methoden für Movies Klasse
        public int getId() {
            return id;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public boolean isAdult() {
            return adult;
        }

        public String getOverview() {
            return overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public int[] getGenre_ids() {
            return genre_ids;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public String getTitle() {
            return title;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public double getPopularity() {
            return popularity;
        }

        public long getVote_count() {
            return vote_count;
        }

        public boolean isVideo() {
            return video;
        }

        public double getVote_average() {
            return vote_average;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        // Overrides fuer toString-Methode
        @Override
        public String toString() {
            return original_title;
        }
    }

    // Getter und Setter Methoden
    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<String> Movies2List(){
        List<String> movies = new ArrayList<String>();

        for(Results item : results)
            movies.add(item.getTitle());

        return movies;
    }

    public String showDetails(String title, MovieGenres genresIds){
        String output = "";
        String genres = "";
        System.out.println("Film Titel: " + title);

        for(Movies.Results item : this.getResults()) {
            if (item.getTitle() == title) {
                output = "Titel:\t\t\t\t" + title + "\n\n";
                output = output + "Originaltitel:\t\t" + item.getOriginal_title() + "\n";
                output = output + "Erscheinungsjahr:\t" + item.getRelease_date() + "\n";

                // Genres abfragen
                for (int j = 0; j < item.getGenre_ids().length; j++) {
                    for (int k = 0; k < genresIds.genres.length; k++) {
                        if (genresIds.genres[k].getId() == item.getGenre_ids()[j]) {
                            genres = genres + genresIds.genres[k].getName() + ", ";
                        }
                    }
                }

            output = output + "Genres:\t\t\t" + genres + "\n\n";
            output = output + "Beschreibung:\n" + item.getOverview() + "\n";

            }
        }
        return output;
    }

    public String showDetails(int index, MovieGenres genresIds){
        String output = "";
        String genres = "";

        if (index < results.size() && results != null) {
            output = "Titel:\t\t\t\t" + results.get(index) + "\n\n";
            output = output + "Originaltitel:\t\t" + results.get(index).getOriginal_title() + "\n";
            output = output + "Erscheinungsjahr:\t" + results.get(index).getRelease_date() + "\n";

            // Genres abfragen
            for (int j = 0; j < results.get(index).getGenre_ids().length; j++) {
                for (int k = 0; k < genresIds.genres.length; k++) {
                    if (genresIds.genres[k].getId() == results.get(index).getGenre_ids()[j]) {
                        genres = genres + genresIds.genres[k].getName() + ", ";
                    }
                }
            }

            output = output + "Genres:\t\t\t" + genres + "\n\n";
            output = output + "Beschreibung:\n" + results.get(index).getOverview() + "\n";

            }

        return output;
    }


    public String getMovieUrl(String title){
        String url = "";
        for(Movies.Results item : this.getResults()) {
            if (item.getTitle() == title)
                url = item.getPoster_path();
        }

        return url;
    }

    public List<String> getYearsofMovies()
    {
        List<String> listYears = new ArrayList<>();
        String tempString;

        for(Results item : results)
        {
            tempString = item.getRelease_date();
            tempString = tempString.substring(0,tempString.indexOf("-"));
            listYears.add(tempString);
        }

        return listYears;


    }

}
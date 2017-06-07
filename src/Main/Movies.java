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
                output = "Titel:\t\t\t\t" + title + "\n";
                output = output + "Originaltitel:\t\t" + item.getOriginal_title() + "\n";
                output = output + "Erscheinungsjahr:\t" + item.getRelease_date() + "\n\n";
                output = output + "Beschreibung:\n" + item.getOverview() + "\n";

                for (int j = 0; j < item.getGenre_ids().length; j++) {
                    for (int k = 0; k < genresIds.genres.length; k++) {
                        if (genresIds.genres[k].getId() == item.getGenre_ids()[j]) {
                            genres = genres + genresIds.genres[k].getName() + ", ";
                        }
                    }
                }

            }
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

}
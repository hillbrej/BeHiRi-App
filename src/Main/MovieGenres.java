package Main;

/**
 * Class Movie Genres das ist ein test
 */
public class MovieGenres {

    public Genres genres[] = new Genres[]{};

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
    }



}

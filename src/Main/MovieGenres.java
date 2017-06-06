package Main;

/**
 * Created by Jonas on 15.05.2017.
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

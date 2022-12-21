package io.welldev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieBoxApplication.class, args);

//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("io.welldev.config");

//        GenreDAO genreDAO = context.getBean(GenreDAO.class);
//        MovieService movieService = context.getBean(MovieService.class);
//        DirectorDAO directorDAO = context.getBean(DirectorDAO.class);
//        CountryDAO countryDAO = context.getBean(CountryDAO.class);
//        MovieDAO movieDAO = context.getBean(MovieDAO.class);

//        insertData(actorDAO, directorDAO, genreDAO, movieDAO, countryDAO);
//        insertData2(actorDAO, directorDAO, genreDAO, movieDAO, countryDAO);

//        manyToManyUnidirectional(actorDAO, directorDAO, genreDAO, movieDAO, countryDAO);
//        manyToManyBidirectional(actorDAO, directorDAO, genreDAO, movieDAO, countryDAO);

//        ActorRepo actorRepo = new ActorRepo() {
//        }
//        Actor actor = actorDAO.actorRepo.findById(1L).get();
//        Actor actor = actorDAO.get(1L);
//        System.out.println(actor.getName());

//        List<Actor> actor = actorService.findActorByName("Abhishek Bachchan");
//        for (Actor actor1:
//             actor) {
//            System.out.println(actor1.getId());
//        }

//        List<Movie> movies = movieService.findAll();
//        for (Movie movie:
//                movies) {
//            System.out.println(movie.getTitle());
//        }


    }

//    private static void manyToManyUnidirectional(ActorDAO actorDAO, DirectorDAO directorDAO, GenreDAO genreDAO, MovieDAO movieDAO, CountryDAO countryDAO) {
//        Movie movie = movieDAO.get(26L);
//        Set<Genre> genreSet = movie.getGenres();
//        List<Genre> genreList = new ArrayList<>(genreSet);
//        System.out.println("--------------------------------------------------------");
//        System.out.println("Genres of movie: " + movie.getTitle());
//        for (Genre genre:
//             genreList) {
//            System.out.println(genre.getName());
//        }
//        System.out.println("--------------------------------------------------------");
//    }
//    private static void manyToManyBidirectional(ActorDAO actorDAO, DirectorDAO directorDAO, GenreDAO genreDAO, MovieDAO movieDAO, CountryDAO countryDAO) {
//        Movie movie = movieDAO.get(26L);
//        Set<Actor> actorSet = movie.getActors();
//        List<Actor> actorList = new ArrayList<>(actorSet);
//        System.out.println("--------------------------------------------------------");
//        System.out.println("Actors of movie: " + movie.getTitle());
//        for (Actor actorLocal:
//                actorList) {
//            System.out.println(actorLocal.getName());
//        }
//        System.out.println("--------------------------------------------------------");
//
////        Actor actor = actorDAO.get(1L);
////        Set<Movie> movieSet = actor.getMovies();
////        List<Movie> movieList = new ArrayList<>(movieSet);
////        System.out.println("--------------------------------------------------------");
////        System.out.println("Movies of actor: " + actor.getName());
////        for (Movie movieLocal:
////                movieList) {
////            System.out.println(movieLocal.getTitle());
////        }
////        System.out.println("--------------------------------------------------------");
//    }
//
//
//    private static void insertData(ActorDAO actorDAO, DirectorDAO directorDAO, GenreDAO genreDAO, MovieDAO movieDAO, CountryDAO countryDAO) {
//
//        Genre genreAction = new Genre("Action");
//        Genre genreCrime = new Genre("Crime");
//        Genre genreThriller = new Genre("Thriller");
//        Genre genreDrama = new Genre("Drama");
//
//        Actor actorAbhishekBachchan = new Actor("Abhishek Bachchan");
//        Actor actorUdayChopra = new Actor("Uday Chopra");
//        Actor actorHrithikRoshan = new Actor("Hrithik Roshan");
//        Actor actorAamirKhan = new Actor("Aamir Khan");
//
//        Director directorSanjayGadhvi = new Director("Sanjay Gadhvi");
//        Director directorVijayKrishnaAcharya = new Director("Vijay Krishna Acharya");
//
//        Country countryIndia = new Country("India");
//
//        ///////////////////////////////////////////////
//
//        // Movie 1
//
//        Movie movieDhoom2 = new Movie();
//        movieDhoom2.setTitle("Dhoom 2");
//        movieDhoom2.setYear(2006);
//
//        movieDhoom2.getActors().add(actorAbhishekBachchan);
//        movieDhoom2.getActors().add(actorUdayChopra);
//        movieDhoom2.getActors().add(actorHrithikRoshan);
//
//        movieDhoom2.setDirector(directorSanjayGadhvi);
//
//        movieDhoom2.getGenres().add(genreAction);
//        movieDhoom2.getGenres().add(genreCrime);
//        movieDhoom2.getGenres().add(genreThriller);
//
//        movieDhoom2.setCountry(countryIndia);
//        movieDhoom2.setRating("6.5/10");
//
//        ///////////////////////////////////////////////
//
//        // Movie 2
//
//        Movie movieDhoom3 = new Movie();
//        movieDhoom3.setTitle("Dhoom 3");
//        movieDhoom3.setYear(2013);
//
//        movieDhoom3.getActors().add(actorAbhishekBachchan);
//        movieDhoom3.getActors().add(actorUdayChopra);
//        movieDhoom3.getActors().add(actorAamirKhan);
//
//        movieDhoom3.setDirector(directorVijayKrishnaAcharya);
//
//        movieDhoom3.getGenres().add(genreAction);
//        movieDhoom3.getGenres().add(genreCrime);
//        movieDhoom3.getGenres().add(genreDrama);
//
//        movieDhoom3.setCountry(countryIndia);
//        movieDhoom3.setRating("5.4/10");
//
//        ///////////////////////////////////////////////
//
//        genreDAO.add(genreAction);
//        genreDAO.add(genreCrime);
//        genreDAO.add(genreThriller);
//        genreDAO.add(genreDrama);
//
////        actorDAO.add(actorAbhishekBachchan);
////        actorDAO.add(actorUdayChopra);
////        actorDAO.add(actorHrithikRoshan);
////        actorDAO.add(actorAamirKhan);
//
//        directorDAO.add(directorSanjayGadhvi);
//        directorDAO.add(directorVijayKrishnaAcharya);
//
//        countryDAO.add(countryIndia);
//
//        movieDAO.add(movieDhoom2);
//        movieDAO.add(movieDhoom3);
//    }
//
//    private static void insertData2(ActorDAO actorDAO, DirectorDAO directorDAO, GenreDAO genreDAO, MovieDAO movieDAO, CountryDAO countryDAO) {
//
//        Genre genreAction = new Genre("Action");
//        Genre genreCrime = new Genre("Crime");
//        Genre genreThriller = new Genre("Thriller");
//        Genre genreDrama = new Genre("Drama");
//
//        Actor actorAbhishekBachchan = new Actor("Abhishek Bachchan");
//        Actor actorUdayChopra = new Actor("Uday Chopra");
//        Actor actorHrithikRoshan = new Actor("Hrithik Roshan");
//        Actor actorAamirKhan = new Actor("Aamir Khan");
//
//        Director directorSanjayGadhvi = new Director("Sanjay Gadhvi");
//        Director directorVijayKrishnaAcharya = new Director("Vijay Krishna Acharya");
//
//        Country countryIndia = new Country("India");
//
//        List<Genre> allGenres = Arrays.asList(
//                genreAction,
//                genreCrime,
//                genreThriller,
//                genreDrama
//        );
//
//        List<Genre> genresOfDhoom2 = Arrays.asList(
//                genreAction,
//                genreCrime,
//                genreThriller
//        );
//
//        List<Genre> genresOfDhoom3 = Arrays.asList(
//                genreAction,
//                genreCrime,
//                genreDrama
//        );
//
//        List<Actor> allActors = Arrays.asList(
//                actorAbhishekBachchan,
//                actorUdayChopra,
//                actorHrithikRoshan,
//                actorAamirKhan
//        );
//
//        List<Actor> actorsOfDhoom2 = Arrays.asList(
//                actorAbhishekBachchan,
//                actorUdayChopra,
//                actorHrithikRoshan
//        );
//
//        List<Actor> actorsOfDhoom3 = Arrays.asList(
//                actorAbhishekBachchan,
//                actorUdayChopra,
//                actorAamirKhan
//        );
//
//        Director directorOfDhoom2 = new Director("Sanjay Gadhvi");
//        Director directorOfDhoom3 = new Director("Vijay Krishna Acharya");
//        Country country = new Country("India");
//        Movie dhoom2 = new Movie("Dhoom 2", new HashSet<Genre>(genresOfDhoom2),
//                "6.5/10", directorOfDhoom2, new HashSet<Actor>(actorsOfDhoom2), 2006, country);
//        Movie dhoom3 = new Movie("Dhoom 3", new HashSet<Genre>(genresOfDhoom3),
//                "5.4/10", directorOfDhoom3, new HashSet<Actor>(actorsOfDhoom3), 2013, country);
//
//        genreDAO.addAll(allGenres);
////        actorDAO.addAll(allActors);
//        directorDAO.add(directorOfDhoom2);
//        directorDAO.add(directorOfDhoom3);
//        countryDAO.add(country);“
//        movieDAO.add(dhoom2);
//        movieDAO.add(dhoom3);
//    }
}

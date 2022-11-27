package io.welldev;

import io.welldev.dao.*;

import io.welldev.model.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class illuminatiHibernateMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("io.welldev.config");

        ActorDAO actorDAO = context.getBean(ActorDAO.class);
        DirectorDAO directorDAO = context.getBean(DirectorDAO.class);
        ExtendedDirectorDAO extendedDirectorDAO = context.getBean(ExtendedDirectorDAO.class);
        GenreDAO genreDAO = context.getBean(GenreDAO.class);
        MovieDAO movieDAO = context.getBean(MovieDAO.class);
        ReviewDAO reviewDAO = context.getBean(ReviewDAO.class);


//
//        List<Genre> genreList = Arrays.asList(
//                new Genre("Romance"),
//                new Genre("Drama")
//        );
//        List<Actor> actorList = Arrays.asList(
//                new Actor("Ananta Jalil"),
//                new Actor("Barsha Jalil")
//        );
//
//        actorDAO.addAll(actorList);
//        genreDAO.addAll(genreList);
//
//        Director director = new Director("Ananta Jalil");
//        directorDAO.add(director);
//
//        List<Review> reviewList = Arrays.asList(
//                new Review("My Love was Selfish but now its Selfless. Thank you Jalil bhai"),
//                new Review("When Jalil Bhai ripped his heart with his picture in it out of his body I finally realized Love")
//        );
//        reviewDAO.addAll(reviewList);
//
//        Movie movie = new Movie("Nirshartho Bhalobasa : What is LOve?", new HashSet<Genre>(genreList),
//                "M", director, new HashSet<Actor>(actorList), 2013, reviewList);
//
//        movieDAO.add(movie);




//        Director director = new Director("David Fincher");
//        Director director1 = new Director("Hero Alam");
//
//        extendedDirectorDAO.addWithoutTransaction(director);
//        extendedDirectorDAO.add(director1);

        Movie movie = movieDAO.get(88L);
        //        for (Actor actor:
//             movie.getActors()) {
//            System.out.println(actor.getName());
//        }















    }
}

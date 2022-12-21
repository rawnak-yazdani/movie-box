package io.welldev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieBoxApplication.class, args);
    }
}

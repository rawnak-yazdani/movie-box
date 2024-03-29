package io.welldev.model.utils;

import io.welldev.model.entity.Movie;
import io.welldev.model.exception.ItemNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Calendar;
import java.util.Optional;

@Component
public class ImageUtils {

    public static String USER_UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/app-photos/users";

    public static String MOVIE_UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/app-photos/movies";

    public static String encodeImageToBase64(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    public static String writeMovieImageFile(Movie movie, MultipartFile imgFile) throws IOException {
        Path fileNameAndPath = Paths.get(MOVIE_UPLOAD_DIRECTORY,
                movie.getTitle() + movie.getYear()
                        + Calendar.getInstance().getTime() + imgFile.getOriginalFilename());
        Files.write(fileNameAndPath, imgFile.getBytes());
        return fileNameAndPath.toString();
    }
    public static String writeUserImageFile(String username, MultipartFile imgFile) throws IOException {
        Path fileNameAndPath = Paths.get(USER_UPLOAD_DIRECTORY,
                username + "_" + Calendar.getInstance().getTime() + imgFile.getOriginalFilename());
        Files.write(fileNameAndPath, imgFile.getBytes());
        return fileNameAndPath.toString();
    }

    public static void deleteFile(String srcPath) {
        File file = new File(srcPath);
        if (file.exists()) {
         file.delete();
        }
    }
}

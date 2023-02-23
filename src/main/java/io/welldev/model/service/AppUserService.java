package io.welldev.model.service;

import io.welldev.model.constants.Constants;
import io.welldev.model.datainputobject.AppUserInput;
import io.welldev.model.datainputobject.AppUserUpdateInput;
import io.welldev.model.datainputobject.ChangePasswordInput;
import io.welldev.model.datainputobject.UserMovieInput;
import io.welldev.model.dataoutputobject.AppUserOutput;
import io.welldev.model.dataoutputobject.MovieOutput;
import io.welldev.model.entity.AppUser;
import io.welldev.model.entity.Movie;
import io.welldev.model.exception.ItemNotFoundException;
import io.welldev.model.repository.AppUserRepo;
import io.welldev.model.repository.MovieRepo;
import io.welldev.model.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {
    private final AppUserRepo appUserRepo;

    private  final MovieRepo movieRepo;

    private final MovieService movieService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/app-photos/users";

    public List<AppUser> findAll() {
        return appUserRepo.findAll();
    }

    public AppUser save(AppUser user, String role) {
        Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(user.getUsername()));

        if (appUser.isPresent()) {
            throw new IllegalArgumentException();
        } else {
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return appUserRepo.save(user);
        }
    }

    public AppUserOutput signUp(AppUserInput appUserInput, String role) throws IOException {
        AppUser appUser = new AppUser();
        mapper.map(appUserInput, appUser);

        try {
            AppUserOutput appUserOutput = mapAppUser(save(appUser, role));
            return appUserOutput;
        } catch (IllegalArgumentException argumentException) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
    }

    public AppUserOutput userSignUp(AppUserInput appUserInput) throws IOException {
        return signUp(appUserInput, Constants.AppStrings.USER_ROLE);
    }

    public AppUserOutput adminSignUp(AppUserInput appUserInput) throws IOException {
        return signUp(appUserInput, Constants.AppStrings.ADMIN_ROLE);
    }

    public AppUserOutput updateUserInfo(String username, AppUserUpdateInput userUpdateInput) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals(username)) {
            Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(username));

            if (appUser.isPresent()) {
//                appUser.get().setName(userUpdateInput.getName());
//                appUser.get().setUsername(username);
//                appUser.get().setPassword(passwordEncoder.encode(userUpdateInput.getPassword()));
                System.out.println(userUpdateInput);
                mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                mapper.map(userUpdateInput, appUser.get());
                AppUserOutput appUserOutput = mapAppUser(appUserRepo.save(appUser.get()));
                return appUserOutput;
            } else {
                throw new ItemNotFoundException("User does not exist!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

    }

    public AppUserOutput updateUserImage(String username, MultipartFile imageFile) throws IOException {
        AppUser appUser = findAppUserByUsername(username);
        Optional<File> oldImageFile = Optional.of(new File(appUser.getImgSrc()));
        oldImageFile.ifPresent(File::delete);
        appUser.setImgSrc(ImageUtils.writeUserImageFile(username, imageFile));
        AppUserOutput appUserOutput = mapAppUser(appUserRepo.save(appUser));
        return appUserOutput;
    }

    public void changePassword(String username, ChangePasswordInput changePasswordInput) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(username)) {
            Optional<AppUser> reqUsername = Optional.ofNullable(appUserRepo.findByUsername(username));

            if (passwordEncoder.matches(changePasswordInput.getCurrentPassword(),
                    reqUsername.get().getPassword())) {
                String newEncodedPassword = passwordEncoder.encode(changePasswordInput.getNewPassword());
                reqUsername.get().setPassword(newEncodedPassword);
                appUserRepo.save(reqUsername.get());
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }
    }

    public AppUserOutput updateWatchlist(String reqUsername,
                                         List<UserMovieInput> userMovieInputs) throws IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            Optional<AppUser> createdUser = Optional.ofNullable(appUserRepo.findByUsername(reqUsername));

            List<Movie> updatedList = new LinkedList<>();

            for (UserMovieInput input :
                    userMovieInputs) {
                updatedList.add(movieRepo.findById(input.getId()).get());
            }

            if (createdUser.isPresent()) {

                createdUser.get().getWatchlist().addAll(updatedList);
                AppUserOutput appUserOutput = mapAppUser(appUserRepo.save(createdUser.get()));
                return appUserOutput;
            } else {
                throw new ItemNotFoundException("User does not exist!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

    }

    public AppUserOutput deleteFromWatchlist(String reqUsername,
                                             List<UserMovieInput> userMovieInputs) throws IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(reqUsername)) {
            Optional<AppUser> createdUser = Optional.ofNullable(appUserRepo.findByUsername(reqUsername));

            if (createdUser.isPresent()) {
                for (UserMovieInput input :
                        userMovieInputs) {
                    createdUser.get().getWatchlist().remove(movieRepo.findById(input.getId()).get());
                }

                AppUserOutput appUserOutput = mapAppUser(appUserRepo.save(createdUser.get()));
                return appUserOutput;
            } else {
                throw new ItemNotFoundException("User does not exist!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }


    }

    public AppUser findAppUserByUsername(String username) {
        Optional<AppUser> requestedAppUser = Optional.ofNullable(appUserRepo.findByUsername(username));

        if (requestedAppUser.isPresent())
            return appUserRepo.findByUsername(username);
        else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
//            throw new UsernameNotFoundException("Username or Password incorrect");
            throw new ItemNotFoundException("Username or Password is Wrong. Otherwise the user is needed to sign up.");
        }
    }

    public AppUserOutput showAUser(String username) throws IOException {
        Optional<AppUser> requestedAppUser = Optional.ofNullable(appUserRepo.findByUsername(username));

        if (requestedAppUser.isPresent()) {
            Set<MovieOutput> movieOutputs = new HashSet<>();
            for ( Movie movie : requestedAppUser.get().getWatchlist()) {
                movieOutputs.add(movieService.mapMovie(movie));
            }
            AppUserOutput appUserOutput = mapAppUser(requestedAppUser.get());
            appUserOutput.setWatchlist(movieOutputs);
            return appUserOutput;
        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not Exist");
            throw new ItemNotFoundException("User does not exist!");
        }
    }

    public List<AppUserOutput> showAllUsers() throws IOException{
        Optional<List<AppUser>> appUsers = Optional.of(appUserRepo.findAll());
        List<AppUserOutput> appUserOutputs = new LinkedList<>();

        for (AppUser user :
                appUsers.get()) {
            appUserOutputs.add(mapAppUser(user));
        }

        return appUserOutputs;
    }

    public void deleteUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(username)) {
            Optional<AppUser> appUser = Optional.ofNullable(appUserRepo.findByUsername(username));

            if (appUser.isPresent())
                appUserRepo.delete(appUser.get());
            else
                throw new ItemNotFoundException("User does not exist!");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

    }

    public AppUserOutput mapAppUser(AppUser appUser) throws IOException {
        AppUserOutput appUserOutput = new AppUserOutput();
        if (appUser.getImgSrc() != null) {
            File file = new File(appUser.getImgSrc());
            String imgSrcB64 = ImageUtils.encodeImageToBase64(file);
            mapper.map(appUser, appUserOutput);
            appUserOutput.setImgSrc(imgSrcB64);
        } else {
            mapper.map(appUser, appUserOutput);
        }

        return appUserOutput;
    }
}

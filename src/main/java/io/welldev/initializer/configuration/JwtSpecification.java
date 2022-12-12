package io.welldev.initializer.configuration;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Getter
@Setter
public class JwtSpecification {
    public static String secretKey = "jumanjiScaresTheSoulOUTTAME_PLZ_LET_MET_OUT_YOU_HAVE_TO_WIn_THE_Game_OR_YOU_ARE_TRAPPED";
    public static String tokenPrefix = "Bearer ";
    public static Integer tokenExpirationAfterDays = 14;
    public static SecretKey secretKeyHashed = Keys.hmacShaKeyFor(secretKey.getBytes());
    public static String authorizationHeader = HttpHeaders.AUTHORIZATION;
    public static String currentUsername = "";
}

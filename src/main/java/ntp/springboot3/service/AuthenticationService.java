package ntp.springboot3.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import ntp.springboot3.dto.request.AuthenticationRequest;
import ntp.springboot3.dto.request.IntrospectRequest;
import ntp.springboot3.dto.request.LogoutRequest;
import ntp.springboot3.dto.request.RefreshRequest;
import ntp.springboot3.dto.request.response.AuthenticationResponse;
import ntp.springboot3.dto.request.response.IntrospectResponse;
import ntp.springboot3.entity.InvalidatedToken;
import ntp.springboot3.entity.User;
import ntp.springboot3.exception.AppException;
import ntp.springboot3.exception.ErrorCode;
import ntp.springboot3.repo.InvalidatedTokenRepo;
import ntp.springboot3.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepo userRepo;
    InvalidatedTokenRepo invalidatedTokenRepo;

    @NonFinal //chan khong cho import vao constuctor
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY; //chu ky rat quan trong

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        }catch (AppException e ){
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHENTICATION_FAILED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.AUTHENTICATION);

//        var token = generateToken(request.getUsername());
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepo.save(invalidatedToken);
    }

    public AuthenticationResponse refeshToken(RefreshRequest request) throws ParseException, JOSEException {
        //kiem tra hieu luc token
         var signedJWT = verifyToken(request.getToken());

         var jit = signedJWT.getJWTClaimsSet().getJWTID();
         var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepo.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepo.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.AUTHENTICATION)
        );

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expityTime.after(new Date())))
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);


        if (invalidatedTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);

        return  signedJWT;
    }


    String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("devteria.com")//dinh danh
                .issueTime(new Date())// lay thoi gian hien tai
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()//het han sau 1 gio
                ))// xac dinh thoi han
                .jwtID(UUID.randomUUID().toString()) //logout
                .claim("scope", buildScope(user))
                .build();

        //sau khi cau hinh xong thi payload
        Payload payLoad = new Payload(jwtClaimSet.toJSONObject());

        //co header va body thi signaute
        JWSObject jwsObject = new JWSObject(header , payLoad);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner("");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                        .forEach(permission -> stringJoiner.add(permission.getName()));
            });

            return stringJoiner.toString();
    }
}


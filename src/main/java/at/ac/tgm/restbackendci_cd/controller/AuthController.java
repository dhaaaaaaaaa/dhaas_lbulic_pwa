package at.ac.tgm.restbackendci_cd.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Liest das Passwort aus der .env / application.properties
    @Value("${app.admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Wir prüfen nur, ob das Passwort stimmt (Username "admin" ist optional)
        if (adminPassword.equals(password)) {
            return ResponseEntity.ok().body(Map.of("message", "Login erfolgreich"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Falsches Passwort"));
        }
    }
}
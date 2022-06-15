package licenta.andreibalinth.backend.controller;

import licenta.andreibalinth.backend.entities.dto.UserDto;
import licenta.andreibalinth.backend.entities.dto.login.LoginRequestDto;
import licenta.andreibalinth.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/authentication")
public class AuthenticationController {
    private final UserService service;
    private static final String incorrectCredentialsMessage = "Your email or password is incorrect";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto){
        Optional<UserDto> response = service.checkUser(requestDto);
        if(response.isPresent()) return ResponseEntity.ok().body(response.get());
        return new ResponseEntity<>(incorrectCredentialsMessage, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        service.add(userDto);
        return ResponseEntity.ok().build();
    }
}

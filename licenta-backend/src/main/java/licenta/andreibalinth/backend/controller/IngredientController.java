package licenta.andreibalinth.backend.controller;

import licenta.andreibalinth.backend.entities.dto.IngredientEntityDto;
import licenta.andreibalinth.backend.service.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/ingredients")
public class IngredientController {
    private final IngredientService service;
    private final static String noIngredientWithGivenIdMessage = "There is no ingredient with the given id";

    @GetMapping("/")
    public ResponseEntity<?> getAllIngredients(){
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable("id") Long id){
        Optional<IngredientEntityDto> dto = service.getById(id);
        if(dto.isPresent()) return ResponseEntity.ok().body(dto.get());
        return new ResponseEntity<>(noIngredientWithGivenIdMessage, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public void create(@RequestBody IngredientEntityDto dto){
        service.add(dto);
    }

    @PostMapping("/update")
    public void update(@RequestBody IngredientEntityDto dto){
        service.update(dto);
    }
}

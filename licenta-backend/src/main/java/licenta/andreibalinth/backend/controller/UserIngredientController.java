package licenta.andreibalinth.backend.controller;

import licenta.andreibalinth.backend.entities.dto.UserIngredientQuantityDto;
import licenta.andreibalinth.backend.service.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RequestMapping("/ingredients")
public class UserIngredientController {
    private final IngredientService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllForUser(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(service.getAllIngredientsOfUser(id));
    }

    @GetMapping("/absent/{id}")
    public ResponseEntity<?> getAllAbsentFromUser(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(service.getAllAbsentFromUser(id));
    }

    @GetMapping("/{userId}/{ingredientId}")
    public ResponseEntity<?> getOneByUserIdAndIngredientId(@PathVariable("userId") Long userId, @PathVariable("ingredientId") Long ingredientId){
        return ResponseEntity.ok().body(service.getOneByUserIdAndIngredientId(userId, ingredientId));
    }

    @PostMapping("/create/{id}")
    public void create(@RequestBody UserIngredientQuantityDto dto, @PathVariable("id") Long id){
        service.addUserIngredientQuantity(dto, id);
    }

    @PostMapping("/update/{id}")
    public void update(@RequestBody UserIngredientQuantityDto dto, @PathVariable("id") Long id){
        service.updateUserIngredient(dto, id);
    }
}

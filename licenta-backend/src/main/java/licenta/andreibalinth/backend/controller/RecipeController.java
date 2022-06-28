package licenta.andreibalinth.backend.controller;

import licenta.andreibalinth.backend.entities.dto.RecipeEntityDto;
import licenta.andreibalinth.backend.entities.dto.UserToRecipeDto;
import licenta.andreibalinth.backend.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService service;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok().body(service.getAllForUser(userId));
    }

    @GetMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> getOneForUser(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId){
        return ResponseEntity.ok().body(service.getByIdForUser(userId, recipeId));
    }

    @PostMapping("/add/{userId}")
    public void addRecipeToUser(@PathVariable("userId") Long userId, @RequestBody UserToRecipeDto dto){
        service.addForUser(userId, dto);
    }

    @DeleteMapping("/delete/{userId}/{recipeId}")
    public void deleteRecipeFromUser(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId){
        service.deleteForUser(userId, recipeId);
    }

    @PutMapping("/update")
    public void updateRecipeOfUser(@RequestBody RecipeEntityDto dto){
        service.update(dto);
    }
}

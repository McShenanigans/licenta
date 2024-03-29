package licenta.andreibalinth.backend.controller;

import licenta.andreibalinth.backend.entities.dto.RecipeEntityDto;
import licenta.andreibalinth.backend.entities.dto.RecipeStoreFilterDto;
import licenta.andreibalinth.backend.entities.dto.UserToRecipeDto;
import licenta.andreibalinth.backend.service.RecipeService;
import licenta.andreibalinth.backend.service.RecipeTagService;
import licenta.andreibalinth.backend.service.TimeTagService;
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
    private final RecipeTagService tagService;
    private final TimeTagService timeTagService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok().body(service.getAllForUser(userId));
    }

    @GetMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> getOneForUser(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId){
        return ResponseEntity.ok().body(service.getByIdForUser(userId, recipeId));
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getAllRecipeTags(){
        return ResponseEntity.ok().body(tagService.getAll());
    }

    @GetMapping("/timeTags")
    public ResponseEntity<?> getAllRecipeTimeTags() {
        return ResponseEntity.ok().body(timeTagService.getAll());
    }

    @GetMapping("/tags/{recipeId}")
    public ResponseEntity<?> getAllRecipeTagsForRecipe(@PathVariable("recipeId") Long recipeId){
        return ResponseEntity.ok().body(tagService.getAllByRecipeId(recipeId));
    }

    @PostMapping("/store/{userId}")
    public ResponseEntity<?> getAllPublicRecipesFromOtherUsers(@PathVariable("userId") Long userId, @RequestBody RecipeStoreFilterDto filter){
        return ResponseEntity.ok().body(service.getAllPublicRecipesFromOtherUsers(userId, filter));
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addRecipeToUser(@PathVariable("userId") Long userId, @RequestBody UserToRecipeDto dto){
        service.addForUser(userId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/connect/{userId}/{recipeId}")
    public ResponseEntity<?> createConnectionFromUserToRecipe(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId){
        service.createConnectionBetweenUserAndRecipe(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{userId}/{recipeId}")
    public ResponseEntity<?> deleteRecipeFromUser(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId){
        service.deleteForUser(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRecipeOfUser(@RequestBody RecipeEntityDto dto){
        service.update(dto);
        return ResponseEntity.ok().build();
    }
}

package licenta.andreibalinth.backend.controller;

import licenta.andreibalinth.backend.service.IngredientCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ingredientCategory")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IngredientCategoryController {

    private final IngredientCategoryService service;

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }
}

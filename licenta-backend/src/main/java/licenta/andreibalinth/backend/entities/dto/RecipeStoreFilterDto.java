package licenta.andreibalinth.backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStoreFilterDto {
    private List<RecipeTagEntityDto> tags;
    private List<IngredientEntityDto> ingredients;
}

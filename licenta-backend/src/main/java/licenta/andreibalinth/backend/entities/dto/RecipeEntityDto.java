package licenta.andreibalinth.backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntityDto {
    private Long id;
    private String name;
    private String description;
    private List<RecipeTagEntityDto> tags;
    private Set<RecipeIngredientQuantityDto> quantities;
    private Boolean isPublic;
    private RecipeTimeTagDto timeTag;
}

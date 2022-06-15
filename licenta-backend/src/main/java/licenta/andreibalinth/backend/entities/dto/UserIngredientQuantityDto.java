package licenta.andreibalinth.backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIngredientQuantityDto {
    private IngredientEntityDto ingredient;
    private Double quantity;
}

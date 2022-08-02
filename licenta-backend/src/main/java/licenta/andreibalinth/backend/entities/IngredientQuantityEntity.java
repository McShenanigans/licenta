package licenta.andreibalinth.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientQuantityEntity {
    private IngredientEntity ingredient;
    private Double quantity;
}

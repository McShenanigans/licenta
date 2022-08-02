package licenta.andreibalinth.backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplexScheduleEntryDto {
    private ScheduleEntryDto entry;
    private boolean allIngredientsAvailable;
    private List<IngredientQuantityDto> missingIngredients;
}

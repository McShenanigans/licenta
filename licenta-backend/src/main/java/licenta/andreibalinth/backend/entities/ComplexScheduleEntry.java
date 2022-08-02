package licenta.andreibalinth.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplexScheduleEntry {
    private ScheduleEntryEntity entry;
    private boolean allIngredientsAvailable;
    private List<IngredientQuantityEntity> missingIngredients;
}

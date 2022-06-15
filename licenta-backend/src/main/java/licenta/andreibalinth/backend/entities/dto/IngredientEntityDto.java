package licenta.andreibalinth.backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientEntityDto {
    private Long id;
    private String name;
    private MeasurementUnitEntityDto measurementUnit;
    private List<IngredientCategoryDto> categories;
}

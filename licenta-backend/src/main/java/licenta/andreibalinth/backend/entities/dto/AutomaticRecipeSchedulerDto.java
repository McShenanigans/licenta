package licenta.andreibalinth.backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutomaticRecipeSchedulerDto {
    private UserDto user;
    private Integer numberOfDays;
    private List<RecipeTimeTagDto> timeTags;
}

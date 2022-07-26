package licenta.andreibalinth.backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEntryDto {
    private Long id;
    private RecipeEntityDto recipe;
    private LocalDateTime date;
}

package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.RecipeTimeTagDto;

import java.util.List;

public interface TimeTagService {
    List<RecipeTimeTagDto> getAll();
}

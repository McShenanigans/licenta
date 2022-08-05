package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.RecipeTimeTagDto;
import licenta.andreibalinth.backend.mappers.RecipeTimeTagMapper;
import licenta.andreibalinth.backend.repository.RecipeTimeTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TimeTagServiceImpl implements TimeTagService{
    RecipeTimeTagMapper mapper;
    RecipeTimeTagRepository repository;

    @Override
    public List<RecipeTimeTagDto> getAll() {
        return mapper.toDtos(repository.findAll());
    }
}

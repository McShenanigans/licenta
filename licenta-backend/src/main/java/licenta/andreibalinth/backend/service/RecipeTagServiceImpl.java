package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.RecipeTagEntity;
import licenta.andreibalinth.backend.repository.RecipeTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeTagServiceImpl implements RecipeTagService{
    private final RecipeTagRepository repository;

    @Override
    public List<RecipeTagEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<RecipeTagEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void add(RecipeTagEntity tag) {
        repository.save(tag);
    }

    @Override
    @Transactional
    public void update(RecipeTagEntity tag) {
        Optional<RecipeTagEntity> tagOpt = repository.findById(tag.getId());
        if(tagOpt.isEmpty()) return;
        RecipeTagEntity savedTag = tagOpt.get();
        savedTag.setName(tag.getName());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

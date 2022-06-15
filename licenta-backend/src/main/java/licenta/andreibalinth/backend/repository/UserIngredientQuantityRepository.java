package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.IngredientEntity;
import licenta.andreibalinth.backend.entities.UserEntity;
import licenta.andreibalinth.backend.entities.UserIngredientQuantity;
import licenta.andreibalinth.backend.entities.embeddingKeys.UserIngredientQuantityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserIngredientQuantityRepository extends JpaRepository<UserIngredientQuantity, UserIngredientQuantityKey> {
    Optional<UserIngredientQuantity> findByUserAndIngredient(UserEntity user, IngredientEntity ingredient);
}

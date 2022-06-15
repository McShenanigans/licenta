package licenta.andreibalinth.backend.entities.embeddingKeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIngredientQuantityKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIngredientQuantityKey that = (UserIngredientQuantityKey) o;
        return userId.equals(that.userId) && ingredientId.equals(that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, ingredientId);
    }
}

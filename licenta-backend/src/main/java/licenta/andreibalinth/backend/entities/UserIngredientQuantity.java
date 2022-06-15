package licenta.andreibalinth.backend.entities;

import licenta.andreibalinth.backend.entities.embeddingKeys.UserIngredientQuantityKey;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "users_to_ingredients")
public class UserIngredientQuantity {
    @EmbeddedId
    private UserIngredientQuantityKey id;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "quantity")
    private Double quantity;

    public UserIngredientQuantity(UserEntity user, IngredientEntity ingredient, Double quantity){
        this.user = user;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.id = new UserIngredientQuantityKey();
        this.id.setIngredientId(ingredient.getId());
        this.id.setUserId(user.getId());
    }

    public Double getQuantity() {
        return quantity;
    }

    public IngredientEntity getIngredient() {
        return ingredient;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserIngredientQuantityKey getId() {
        return id;
    }

    public void setId(UserIngredientQuantityKey id) {
        this.id = id;
    }

    public void setIngredient(IngredientEntity ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}

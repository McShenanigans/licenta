package licenta.andreibalinth.backend.entities;

import licenta.andreibalinth.backend.entities.embeddingKeys.RecipeQuantityKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "recipes_to_ingredients")
public class RecipeIngredientQuantity {
    @EmbeddedId
    private RecipeQuantityKey id;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(name = "quantity")
    private Double quantity;

//    public RecipeEntity getRecipe() {
//        return recipe;
//    }
//
//    public IngredientEntity getIngredient() {
//        return ingredient;
//    }
//
//
//    public Double getQuantity() {
//        return quantity;
//    }
//
//    public RecipeQuantityKey getId() {
//        return id;
//    }
//
//    public void setRecipe(RecipeEntity recipe) {
//        this.recipe = recipe;
//    }
//
//    public void setQuantity(Double quantity) {
//        this.quantity = quantity;
//    }
//
//    public void setIngredient(IngredientEntity ingredient) {
//        this.ingredient = ingredient;
//    }
//
//    public void setId(RecipeQuantityKey id) {
//        this.id = id;
//    }
}

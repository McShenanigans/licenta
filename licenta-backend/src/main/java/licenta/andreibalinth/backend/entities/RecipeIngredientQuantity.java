package licenta.andreibalinth.backend.entities;

import licenta.andreibalinth.backend.entities.embeddingKeys.RecipeQuantityKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}

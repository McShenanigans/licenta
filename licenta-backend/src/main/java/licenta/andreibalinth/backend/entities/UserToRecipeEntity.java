package licenta.andreibalinth.backend.entities;

import licenta.andreibalinth.backend.entities.embeddingKeys.UserToRecipeKey;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name="users_to_recipes")
public class UserToRecipeEntity {
    @EmbeddedId
    private UserToRecipeKey key;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name="recipe_id")
    private RecipeEntity recipe;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "owner")
    private boolean isOwner;

    public UserToRecipeEntity(UserEntity user, RecipeEntity recipe, boolean isOwner){
        this.user = user;
        this.recipe = recipe;
        this.isOwner = isOwner;
        this.key = new UserToRecipeKey();
        this.key.setRecipeId(recipe.getId());
        this.key.setUserId(user.getId());
    }

    public UserEntity getUser() {
        return user;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public UserToRecipeKey getKey() {
        return key;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setKey(UserToRecipeKey key) {
        this.key = key;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }
}

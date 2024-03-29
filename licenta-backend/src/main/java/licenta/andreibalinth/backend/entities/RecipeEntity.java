package licenta.andreibalinth.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipe")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "public")
    private Boolean isPublic;

    @ManyToMany( cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "recipes_to_tags",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<RecipeTagEntity> tags;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredientQuantity> quantities;

    @OneToMany(mappedBy = "recipe")
    private Set<UserToRecipeEntity> users;

    @ManyToOne
    @JoinColumn(name = "time_tag_id")
    private RecipeTimeTagEntity timeTag;
}

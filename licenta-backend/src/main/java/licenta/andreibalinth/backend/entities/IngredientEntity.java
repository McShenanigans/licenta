package licenta.andreibalinth.backend.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "measurement_unit_id", nullable = false)
    private MeasurementUnitEntity measurementUnit;

    @OneToMany(mappedBy = "ingredient")
    private Set<UserIngredientQuantity> usersIngredientQuantities;

    @ManyToMany
    @JoinTable(
            name="ingredients_to_ingredient_categories",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_category_id")
    )
    private List<IngredientCategoryEntity> categories;
}

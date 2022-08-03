import { Label } from "reactstrap";

const RecipeDetailsModal = props => {
    if(!props.show) return null;

    const ingredientsList = props.recipe.quantities.map(ingredientQuantity => {
        return (
            <div key={props.recipe.id}>
                <Label>
                    {ingredientQuantity.ingredient.name} <br/> {ingredientQuantity.quantity} {ingredientQuantity.ingredient.measurementUnit.name}
                </Label>
                <br/>
            </div>
        )
    });

    return (
        <div className="modal" onClick={props.onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h4 className="modal-title">{props.recipe.name}</h4>
                    <button onClick={() => props.onClose()}>X</button>
                </div>
                <div className="modal-body">
                    <div>
                        <Label>{props.recipe.description}</Label>
                    </div>
                    <div>
                        <Label>Ingredients list</Label>
                        <br/>
                        {ingredientsList}
                    </div>
                </div>
                <div className="modal-footer">
                </div>
            </div>
        </div>
    )
}

export default RecipeDetailsModal;
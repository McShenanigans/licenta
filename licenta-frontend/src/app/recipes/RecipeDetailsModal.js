import { Label, Button } from "reactstrap";

import "../../styles/general.css";

const RecipeDetailsModal = props => {
    if(!props.show) return null;

    const ingredientsList = props.recipe.quantities.map(ingredientQuantity => {
        return (
            <div key={props.recipe.id} className="card-xs">
                <Label>
                    {ingredientQuantity.ingredient.name} <br/> {ingredientQuantity.quantity} {ingredientQuantity.ingredient.measurementUnit.name}
                </Label>
                <br/>
            </div>
        )
    });

    return (
        <div className="modal font-general" onClick={props.onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h4 className="modal-title">{props.recipe.name}</h4>
                    <Button className="button-danger" onClick={() => props.onClose()}>X</Button>
                </div>
                <div className="modal-body">
                    <div>
                        <h5>Description</h5>
                        <Label>{props.recipe.description}</Label>
                    </div>
                    <div>
                        <h5>Ingredients list</h5>
                        <div className="cards-container-xs">
                            {ingredientsList}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default RecipeDetailsModal;
import { useEffect, useState } from "react";
import { Label } from "reactstrap";

const MissingIngredients = props => {
    const [missingIngredientsList, setMissingIngredientsList] = useState([]);

    useEffect(() => {
        setMissingIngredientsList(props.missingIngredients);
    }, []);
    
    const list = missingIngredientsList.map(ingredientQuantity => {
        return (
            <div>
                <Label>{ingredientQuantity.ingredient.name}</Label><br/>
                <Label>{ingredientQuantity.quantity} {ingredientQuantity.ingredient.measurementUnit.name}</Label>
            </div>
        )        
    });

    if(!props.show) return null;
    return (
        <div className="modal" onClick={props.onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h4 className="modal-title">Missing ingredients</h4>
                    <button onClick={() => props.onClose()}>X</button>
                </div>
                <div className="modal-body">
                    {list}
                </div>
            </div>
        </div>
    )
}

export default MissingIngredients;
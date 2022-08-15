import { useEffect, useState } from "react";
import { Label, Button } from "reactstrap";

import "../../styles/general.css";

const MissingIngredients = props => {
    const [missingIngredientsList, setMissingIngredientsList] = useState([]);

    useEffect(() => {
        setMissingIngredientsList(props.missingIngredients);
    }, []);
    
    const list = missingIngredientsList.map(ingredientQuantity => {
        return (
            <div className="card-xs">
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
                    <Button className="button-danger" onClick={() => props.onClose()}>X</Button>
                </div>
                <div className="modal-body">
                    <div className="cards-container-xs">
                        {list}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MissingIngredients;
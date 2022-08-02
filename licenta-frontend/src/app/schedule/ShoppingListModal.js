import axios from "axios";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { Label } from "reactstrap";

const ShoppingListModal = props => {
    const [shoppingList, setShoppingList] = useState([]);
    const [cookies, setCookies] = useCookies();

    const doGetAll = () => {
        axios.get('http://localhost:8080/schedule/shoppingList/' + cookies.user.id)
        .then(response => {
            setShoppingList(response.data);
        })
    }

    useEffect(() => {
        doGetAll();
    }, [])

    const list = shoppingList.map(ingredientQuantity => {
        return (
            <div key={ingredientQuantity.ingredient.id}>
                <Label>{ingredientQuantity.ingredient.name}</Label><br/>
                <Label>{ingredientQuantity.quantity} {ingredientQuantity.ingredient.measurementUnit.name}</Label>
            </div>
        )
    })

    if(!props.show) return null;

    return (
        <div className="modal" onClick={props.onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h4 className="modal-title">Shopping list</h4>
                    <button onClick={() => props.onClose()}>X</button>
                </div>
                <div className="modal-body">
                    {list}
                </div>
            </div>
        </div>
    )
}

export default ShoppingListModal;
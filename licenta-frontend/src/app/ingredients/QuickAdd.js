import axios from "axios";
import { useState } from "react";
import { useCookies } from "react-cookie";
import {Button, Input} from 'reactstrap';

import "../../styles/general.css";
import "../../styles/ingredients.css";

const QuickAdd = props => {

    const [quickAddValue, setQuickAddValue] = useState(null);
    const [cookies, setCookies] = useCookies();

    const handleQuickAddChange = (event) => {
        setQuickAddValue(parseInt(event.target.value));
    }

    const handleQuickAdd = () => {
        axios.post('http://ec2-34-207-124-110.compute-1.amazonaws.com:8080/ingredients/update/' + cookies.user.id, {
            ingredient: props.ingredient,
            quantity: props.quantity + quickAddValue
        })
        .then(() => {
            props.triggerRender();
        });
    }

    return (
        <div className="quick-add-container">
            <Input type='number' placeholder='Input a quantity to add...' value={quickAddValue} onChange={handleQuickAddChange}/>
            <Button className="button-primary" type='button' color='primary' onClick={() => handleQuickAdd()}>Add</Button>
        </div>
    )
}

export default QuickAdd;
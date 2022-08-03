import axios from "axios";
import { useState } from "react";
import { useCookies } from "react-cookie";
import {Button, Input} from 'reactstrap';

const QuickAdd = props => {

    const [quickAddValue, setQuickAddValue] = useState(null);
    const [cookies, setCookies] = useCookies();

    const handleQuickAddChange = (event) => {
        setQuickAddValue(parseInt(event.target.value));
    }

    const handleQuickAdd = () => {
        axios.post('http://localhost:8080/ingredients/update/' + cookies.user.id, {
            ingredient: props.ingredient,
            quantity: props.quantity + quickAddValue
        })
        .then(() => {
            props.triggerRender();
        });
    }

    return (
        <div>
            <Input type='number' placeholder='Input a quantity to add...' value={quickAddValue} onChange={handleQuickAddChange}/>
            <Button type='button' color='primary' onClick={() => handleQuickAdd()}>Add</Button>
        </div>
    )
}

export default QuickAdd;
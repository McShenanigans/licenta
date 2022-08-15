import {useCookies} from 'react-cookie';
import {useEffect, useState} from 'react'
import {useNavigate, Link} from 'react-router-dom';
import {Button, Container} from 'reactstrap';
import axios from 'axios';
import QuickAdd from './QuickAdd';

import "../../styles/general.css";
import "../../styles/ingredients.css";

function UserIngredients() {
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();
    const [ingredients, setIngredients] = useState([]);
    const [renderFlag, setRenderFlag] = useState(false);

    const doGetAll = () => {
        axios.get('http://localhost:8080/ingredients/'+cookies.user.id)
        .then((response) => {
            setIngredients(response.data);
        });
    }

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        doGetAll();
    }, [renderFlag]);

    const doRemove = (id) => {
        axios.delete('http://localhost:8080/ingredients/delete/' + cookies.user.id + '/' + id);
        flipRenderFlag();
    }

    const flipRenderFlag = () => {
        setRenderFlag(!renderFlag);
    }

    const list = ingredients.map(ingredient => {
        return <div className='card-container' key={ingredient.ingredient.id}>
            <div className='card'>
                <div className='card-content'>
                    <div className='card-content-item'>{ingredient.ingredient.name}</div>
                    <div className='card-ingredient-quantity'>{ingredient.quantity} {ingredient.ingredient.measurementUnit.name}</div>
                </div>
                <div className='card-content'>
                    <Button className='button-secondary' tag={Link} to={'/ingredients/'+ingredient.ingredient.id}>Edit</Button>
                    <Button className='button-danger' onClick={() => {doRemove(ingredient.ingredient.id)}}>Delete</Button>
                </div>
            </div>
            <div className='card-extension'>
                <QuickAdd ingredient={ingredient.ingredient} quantity={ingredient.quantity} triggerRender={() => flipRenderFlag()}/>
            </div>
        </div>
    })

    return (<Container fluid className='font-general'>
        <div className='title-container'>
            <h3>Your ingredients</h3>
            <Button className='button-primary' tag={Link} to='/ingredients/create'>Add new ingredient</Button>
        </div>
        <div className='cards-container'>
            {list}
        </div>
    </Container>);
}

export default UserIngredients;
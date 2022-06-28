import {useCookies} from 'react-cookie';
import {useEffect, useState} from 'react'
import {useNavigate, Link} from 'react-router-dom';
import {Button, ButtonGroup, Container} from 'reactstrap';
import axios from 'axios';

function UserIngredients() {
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();
    const [ingredients, setIngredients] = useState([]);

    const doGetAll = () => {
        axios.get('http://localhost:8080/ingredients/'+cookies.user.id)
        .then((response) => {
            setIngredients(response.data);
        });
    }

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        doGetAll();
    }, [ingredients.length]);

    const doRemove = (id) => {
        axios.delete('http://localhost:8080/ingredients/delete/' + cookies.user.id + '/' + id);
        window.location.reload();
    }

    const list = ingredients.map(ingredient => {
        return <div className='col-md-10' key={ingredient.ingredient.id}>
            <div className='col-md-8'>
                <div className='col-md-12'>{ingredient.ingredient.name}</div>
                <div className='col-md-12'>{ingredient.quantity} {ingredient.ingredient.measurementUnit.name}</div>
            </div>
            <div className='col-md-4'>
                <ButtonGroup>
                    <Button size='sm' color='primary' tag={Link} to={'/ingredients/'+ingredient.ingredient.id}>Edit</Button>
                    <Button size='sm' color='danger' onClick={() => {doRemove(ingredient.ingredient.id)}}>Delete</Button>
                </ButtonGroup>
            </div>
        </div>
    })

    return (<Container fluid>
        <div>
            <ButtonGroup>
                <Button color='primary' tag={Link} to='/ingredients/create'>Add Ingredient</Button>
                <Button color='secondary' tag={Link} to='/home'>Go back</Button>
            </ButtonGroup>
        </div>
        <h3>
            Your ingredients
        </h3>
        <div>
            {list}
        </div>
    </Container>);
}

export default UserIngredients;
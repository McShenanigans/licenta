import {useEffect, useState} from 'react';
import axios from 'axios'
import {Button, ButtonGroup, Container} from 'reactstrap';
import { Link } from 'react-router-dom';

function Ingredients() {
    const [ingredients, setIngredients] = useState([])

    const doGetAll = () => {
        axios.get('http://ec2-34-207-124-110.compute-1.amazonaws.com:8080/admin/ingredients/')
        .then((response) => {
            setIngredients(response.data);
        });
    }

    useEffect(() => {
        doGetAll();
    });

    const list = ingredients.map(ingredient => {
        return <div className='col-md-10'>
            <div className='col-md-8'>
                <div className='col-md-12'>{ingredient.name}</div>
                <div className='col-md-12'>{ingredient.measurementUnit.name}</div>
            </div>
            <div className='col-md-4'>
                <ButtonGroup>
                    <Button size='sm' color='primary' tag={Link} to={'/admin/ingredients/' + ingredient.id}>Edit</Button>
                </ButtonGroup>
            </div>
        </div>
    })
    return (
        <Container fluid>
            <div>
                <Button color='success' tag={Link} to="/admin/ingredients/create">Create ingredient</Button>
            </div>
            <h3>
                Ingredients
            </h3>
            <div>{list}</div>
        </Container>
    );
}

export default Ingredients;
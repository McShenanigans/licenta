import {useCookies} from 'react-cookie';
import {useEffect, useState} from 'react';
import {useNavigate, Link} from 'react-router-dom';
import {Button, ButtonGroup, Container} from 'reactstrap';
import axios from 'axios';

function UserRecipes() {
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();
    const [recipes, setRecipes] = useState([]);

    const doGetAll = () => {
        axios.get('http://localhost:8080/recipe/' + cookies.user.id)
        .then((response) => {
            setRecipes(response.data);
        });
    }

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        doGetAll();
    }, [recipes.length]);

    const handleDelete = (recipeId) => {
        axios.delete('http://localhost:8080/recipe/delete/' + cookies.user.id + '/' + recipeId)
        .then(
            navigate('/recipes')
        );
    }

    const list = recipes.map(userToRecipe => {
        return <div key={userToRecipe.recipe.id}>
            <div>{userToRecipe.recipe.name}</div>
            <div>
                <ButtonGroup>
                    <Button size='sm' color='primary' hidden={userToRecipe.owner === false} tag={Link} to={'/recipes/'+userToRecipe.recipe.id}>Edit</Button>
                    <Button size='sm' color='danger' onClick={() => handleDelete(userToRecipe.recipe.id)}>Delete</Button>
                </ButtonGroup>
            </div>
        </div>
    })

    return (<Container fluid>
        <div>
            <Button color='success' tag={Link} to='/recipes/write'>Write recipe</Button>
        </div>
        <h3>
            Saved recipes
        </h3>
        <div>
            {list}
        </div>
    </Container>)
}

export default UserRecipes;
import {useCookies} from 'react-cookie';
import {useEffect, useState} from 'react';
import {useNavigate, Link} from 'react-router-dom';
import {Button, ButtonGroup, Container, Label} from 'reactstrap';
import axios from 'axios';

function UserRecipes() {
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();
    const [recipes, setRecipes] = useState([]);
    const activeVisibilityButtonColor = 'primary';
    const inactiveVisibilityButtonColor = 'secondary';
    const [visibilityChangeFlag, setVisibilityChangeFlag] = useState(false);

    const doGetAll = () => {
        axios.get('http://localhost:8080/recipe/' + cookies.user.id)
        .then((response) => {
            setRecipes(response.data);
        });
    }

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        doGetAll();
    }, [recipes.length, visibilityChangeFlag]);

    const handleDelete = (recipeId) => {
        axios.delete('http://localhost:8080/recipe/delete/' + cookies.user.id + '/' + recipeId)
        .then(
            window.location.reload()
        );
    };

    const handleVisibilityChange = (recipe, isPublic) => {
        axios.put('http://localhost:8080/recipe/update', {
            id: recipe.id,
            name: recipe.name,
            description: recipe.description,
            isPublic: isPublic,
            tags: recipe.tags,
            quantities: recipe.quantities
        }).then(
            setVisibilityChangeFlag(!visibilityChangeFlag)
        );
    };

    const list = recipes.map(userToRecipe => {
        return <div key={userToRecipe.recipe.id}>
            <Label>{userToRecipe.recipe.name}</Label>
            <div>
                <ButtonGroup>
                    <Button size='sm' color='primary' hidden={userToRecipe.owner === false} tag={Link} to={'/recipes/'+userToRecipe.recipe.id}>Edit</Button>
                    <Button size='sm' color='danger' onClick={() => handleDelete(userToRecipe.recipe.id)}>Delete</Button>
                </ButtonGroup>
                <div hidden={userToRecipe.owner === false}>
                    <Label>Visibility</Label>
                    <ButtonGroup>
                        <Button size='sm' color={userToRecipe.recipe.isPublic ? activeVisibilityButtonColor : inactiveVisibilityButtonColor} onClick={() => handleVisibilityChange(userToRecipe.recipe, true)}>Public</Button>
                        <Button size='sm' color={userToRecipe.recipe.isPublic ? inactiveVisibilityButtonColor : activeVisibilityButtonColor} onClick={() => handleVisibilityChange(userToRecipe.recipe, false)}>Private</Button>
                    </ButtonGroup>
                </div>
            </div>
        </div>
    })

    return (<Container fluid>
        <div>
            <ButtonGroup>
                <Button color='primary' tag={Link} to='/recipes/write'>Write recipe</Button>
                <Button color='secondary' tag={Link} to='/home'>Go back</Button>
            </ButtonGroup>
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
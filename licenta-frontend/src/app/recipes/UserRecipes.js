import {useCookies} from 'react-cookie';
import {useEffect, useState} from 'react';
import {useNavigate, Link} from 'react-router-dom';
import {Button, Container} from 'reactstrap';
import axios from 'axios';
import RecipeDetailsModal from './RecipeDetailsModal.js';

import "../../styles/general.css";
import "../../styles/recipes.css";

function UserRecipes() {
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();
    const [recipes, setRecipes] = useState([]);
    const [visibilityChangeFlag, setVisibilityChangeFlag] = useState(false);
    const [recipeIDForDetails, setRecipeIdForDetails] = useState(null);

    const doGetAll = () => {
        axios.get('http://ec2-34-207-124-110.compute-1.amazonaws.com:8080/recipe/' + cookies.user.id)
        .then((response) => {
            setRecipes(response.data);
        });
    }

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        doGetAll();
    }, [recipes.length, visibilityChangeFlag]);

    const handleDelete = (recipeId) => {
        axios.delete('http://ec2-34-207-124-110.compute-1.amazonaws.com:8080/recipe/delete/' + cookies.user.id + '/' + recipeId)
        .then(
            window.location.reload()
        );
    };

    const handleVisibilityChange = (recipe, isPublic) => {
        axios.put('http://ec2-34-207-124-110.compute-1.amazonaws.com:8080/recipe/update', {
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
        return <div className='card-container' key={userToRecipe.recipe.id}>
            <div className='card'>
                <div className='card-content'>
                    <div className='card-content-item-full'>{userToRecipe.recipe.name}</div>
                </div>
                <div className='card-content'>
                    <Button className='button-secondary' hidden={userToRecipe.owner === false} tag={Link} to={'/recipes/'+userToRecipe.recipe.id}>Edit</Button>
                    <Button className={userToRecipe.owner === false ? 'button-danger-full' : 'button-danger'} onClick={() => handleDelete(userToRecipe.recipe.id)}>Delete</Button>
                </div>
            </div>
            <div className='card-extension' hidden={userToRecipe.owner === false}>
                <Button className={userToRecipe.recipe.isPublic ? 'button-primary' : 'button-secondary'} onClick={() => handleVisibilityChange(userToRecipe.recipe, true)}>Public</Button>
                <Button className={userToRecipe.recipe.isPublic ? 'button-secondary' : 'button-primary'} onClick={() => handleVisibilityChange(userToRecipe.recipe, false)}>Private</Button>
            </div>
            <div className='card-extension' hidden={userToRecipe.owner === true}>
                <Button className='button-primary-full' onClick={() => setRecipeIdForDetails(userToRecipe.recipe.id)}>Details</Button>
                <RecipeDetailsModal recipe={userToRecipe.recipe} show={recipeIDForDetails === userToRecipe.recipe.id} onClose={() => setRecipeIdForDetails(null)}/>
            </div>
        </div>
    })

    return (<Container fluid className='font-general'>
        <div className='title-container'>
            <h3>Saved recipes</h3>
            <Button className='button-primary' tag={Link} to='/recipes/write'>Write recipe</Button>
        </div>
        <div className='cards-container'>
            {list}
        </div>
    </Container>)
}

export default UserRecipes;
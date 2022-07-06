import axios from "axios";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { useNavigate, Link } from "react-router-dom";
import { Button, ButtonGroup, Container, Label } from "reactstrap";

function RecipeStore() {
    const [availableRecipes, setAvailableRecipes] = useState([]);
    const [renderFlag, setRenderFlag] = useState(false);
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();

    const getAll = () => {
        axios.get('http://localhost:8080/recipe/store/' + cookies.user.id)
        .then((response) => {
            setAvailableRecipes(response.data);
        });
    };

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        getAll();
    }, [renderFlag]);

    const handleAddToCookbook = (recipeId) => {
        axios.post('http://localhost:8080/recipe/connect/' + cookies.user.id + '/' + recipeId)
        .then(() => {setRenderFlag(!renderFlag)});
    }

    const recipeRenderList = availableRecipes.map(recipe => {
        return (
            <div key={recipe.id}>
                <Label>{recipe.name}</Label>
                <div>
                    <ButtonGroup>
                        <Button size='sm' color='primary'>Details</Button>
                        <Button size='sm' color='success' onClick={() => handleAddToCookbook(recipe.id)}>Add To Cookbook</Button>
                    </ButtonGroup>
                </div>
            </div>
        );
    });

    return <Container>
        <Button tag={Link} to='/home'>Go Back</Button>
        <div>
            {recipeRenderList}
        </div>
    </Container>;
}

export default RecipeStore;
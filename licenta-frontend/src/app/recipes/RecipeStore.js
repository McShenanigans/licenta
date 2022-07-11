import axios from "axios";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { useNavigate, Link } from "react-router-dom";
import { Button, ButtonGroup, Container, Label, Tag } from "reactstrap";
import Select from 'react-select'

function RecipeStore() {
    const [availableRecipes, setAvailableRecipes] = useState([]);
    const [renderFlag, setRenderFlag] = useState(false);
    const [availableTags, setAvailableTags] = useState([]);
    const [selectedTags, setSelectedTags] = useState([]);
    const [availableIngredients, setAvailableIngredients] = useState([]);
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();

    const getAll = () => {
        if(availableTags.length === 0 && selectedTags.length === 0){
            axios.get('http://localhost:8080/recipe/tags').then((response) => {
                let newTags = [];
                response.data.forEach(tag => {
                   newTags.push({value: tag, label: tag.name}) 
                });
                setAvailableTags(newTags);
            })
        }
        if(availableIngredients.length === 0 && selectedIngredients.length === 0){
            axios.get('http://localhost:8080/admin/ingredients/').then((response) => {
                let newIngredients = [];
                response.data.forEach(ingredient => {
                    newIngredients.push({value: ingredient, label: ingredient.name})
                });
                setAvailableIngredients(newIngredients);
            })
        }
        axios.post('http://localhost:8080/recipe/store/' + cookies.user.id, {
            tags: selectedTags,
            ingredients: selectedIngredients
        })
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
        .then(() => {triggerRender()});
    }

    const triggerRender = () => {
        setRenderFlag(!renderFlag);
    }

    const handleTagSelect = (event) => {
        let tag = event.value;
        
        let newSelectedTags = [tag];
        selectedTags.forEach(selectedTag => newSelectedTags.push(selectedTag));
        setSelectedTags(newSelectedTags);

        let newAvailableTags = [];
        availableTags.forEach(availableTag => {
            if(availableTag.value.id !== tag.id) newAvailableTags.push(availableTag)
        });
        setAvailableTags(newAvailableTags);

        triggerRender();
    }

    const handleIngredientSelect = (event) => {
        let ingredient = event.value;

        let newSelectedIngredients = [ingredient];
        selectedIngredients.forEach(selectedIngredient => newSelectedIngredients.push(selectedIngredient));
        setSelectedIngredients(newSelectedIngredients);

        let newAvailableIngredients = [];
        availableIngredients.forEach(avaialbleIngredient => {
            if(avaialbleIngredient.value.id !== ingredient.id) newAvailableIngredients.push(avaialbleIngredient);
        });
        setAvailableIngredients(newAvailableIngredients);

        triggerRender()
    }

    const unselectTag = (tag) => {
        let newSelectedTags = [];
        selectedTags.forEach(selectedTag => {
            if(selectedTag.id !== tag.id) newSelectedTags.push(selectedTag);
        });
        setSelectedTags(newSelectedTags);

        let newAvailableTags = [{value: tag, label: tag.name}];
        availableTags.forEach(availableTag => newAvailableTags.push(availableTag));
        setAvailableTags(newAvailableTags);

        triggerRender();
    }

    const unselectIngredient = (ingredient) => {
        let newSelectedIngredients = [];
        selectedIngredients.forEach(selectedIngredient => {
            if(selectedIngredient.id !== ingredient.id) newSelectedIngredients.push(selectedIngredient);
        });
        setSelectedIngredients(newSelectedIngredients);

        let newAvailableIngredients = [{value: ingredient, label: ingredient.name}];
        availableIngredients.forEach(availableIngredient => newAvailableIngredients.push(availableIngredient));
        setAvailableIngredients(newAvailableIngredients);

        triggerRender();
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

    const tagFilterList = selectedTags.map(tag => {
        return (
            <div key={tag.id}>
                <Label>{tag.name}</Label>
                <Button size='sm' color="danger" onClick={() => unselectTag(tag)}>X</Button>
            </div>
        );
    })

    const ingredientFilterList = selectedIngredients.map(ingredient => {
        return (
            <div key={ingredient.id}>
                <Label>{ingredient.name}</Label>
                <Button size="sm" color="danger" onClick={() => unselectIngredient(ingredient)}>X</Button>
            </div>
        )
    })

    return (<Container>
        <Button tag={Link} to='/home'>Go Back</Button>
        <div>
            <Label>Filter by tag</Label>
            <Select value={null} options={availableTags} onChange={handleTagSelect}/>
            <div>
                {tagFilterList}
            </div>
        </div>
        <div>
            <Label>Filter by ingredients</Label>
            <Select value={null} options={availableIngredients} onChange={handleIngredientSelect}/>
            <div>
                {ingredientFilterList}
            </div>
        </div>
        <div>
            {recipeRenderList}
        </div>
    </Container>);
}

export default RecipeStore;
import {useCookies} from 'react-cookie';
import {useEffect, useState} from 'react';
import {useNavigate, Link, useParams} from 'react-router-dom';
import Select from 'react-select';
import {Button, ButtonGroup, Container, Input, Form, FormGroup, Label} from 'reactstrap';
import axios from 'axios'

function WriteRecipe(){
    const [cookies, setCookies] = useCookies();
    const [availableIngredients, setAvailalbleIngredients] = useState([]);
    const [selectedIngredientsAndQuantities, setSelectedIngredientsAndQuantities] = useState([]);
    const [selectedIngredient, setSelectedIngredient] = useState(null);
    const [description, setDescription] = useState("");
    const [name, setName] = useState("");
    const [recipeId, setRecipeId] = useState(null)
    const params = useParams();

    const navigate = useNavigate();

    const doGetAllIngredients = () => {
        if(typeof cookies.user.id === 'undefined') navigate('/');
        axios.get('http://localhost:8080/admin/ingredients/')
        .then((response) => {
            let options = [];
            response.data.forEach(ingredient => {
                options.push({value: ingredient, label: ingredient.name});
            });
            if(params.id !== 'write'){
                axios.get('http://localhost:8080/recipe/' + cookies.user.id + '/' + params.id).then(response => {
                    if(response.data === null) navigate('/404');
                    setRecipeId(response.data.recipe.id);
                    setName(response.data.recipe.name);
                    setDescription(response.data.recipe.description);
                    setSelectedIngredientsAndQuantities(response.data.recipe.quantities);
                    response.data.recipe.quantities.forEach(quantity => {
                        let index = -1;
                        for (let i = 0; i < options.length; i++){
                            if(options[i].value.id === quantity.ingredient.id) {
                                index = i;
                                break;
                            }
                        }
                        if(index > -1) options.splice(index, 1);
                    })
                    setAvailalbleIngredients(options);
                })
            }
            else setAvailalbleIngredients(options);
        })
    }

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        if(availableIngredients.length === 0 && selectedIngredientsAndQuantities.length === 0) {
            doGetAllIngredients();
        }
    }, [availableIngredients.length]);

    const handleSelectChange = (event) => {
        let ingredient = event.value;

        let newSelectedIngredients = [];
        newSelectedIngredients.push({ingredient: ingredient, quantity: 0});
        selectedIngredientsAndQuantities.forEach(ingred => {
            newSelectedIngredients.push(ingred)
        });
        setSelectedIngredientsAndQuantities(newSelectedIngredients);

        let newAvailableIngredients = [];
        availableIngredients.forEach(ingred => {
            if(ingred.value.id !== ingredient.id) {
                newAvailableIngredients.push(ingred);
            }
        });
        setAvailalbleIngredients(newAvailableIngredients);

        setSelectedIngredient(null);
    }

    const handleQuantityChange = (event) => {
        if(event.target.value === "") return;
        console.log(event.target.value);
        let newIngredientsAndQuantities = [];
        selectedIngredientsAndQuantities.forEach(ingredientQuantity => {
            if(ingredientQuantity.ingredient.id == event.target.parentElement.id){
                ingredientQuantity.quantity = parseFloat(event.target.value);
            }
            newIngredientsAndQuantities.push(ingredientQuantity);
        });
        setSelectedIngredientsAndQuantities(newIngredientsAndQuantities);
    }

    const unselectIngredient = (ingredient) => {
        let newSelectedIngredients = [];
        selectedIngredientsAndQuantities.forEach(ingred => {
            if(ingred.ingredient.id !== ingredient.id) newSelectedIngredients.push(ingred);
        });
        setSelectedIngredientsAndQuantities(newSelectedIngredients);
        let newAvailableIngredients = [];
        newAvailableIngredients.push({value: ingredient, label: ingredient.name});
        availableIngredients.forEach(ingred => {
                newAvailableIngredients.push(ingred);
        });
        setAvailalbleIngredients(newAvailableIngredients);
    }

    const quantitiesListForRender = selectedIngredientsAndQuantities.map((ingredientQuantity) => {
        return (<div key={ingredientQuantity.ingredient.id} id={ingredientQuantity.ingredient.id}>
            <div>{ingredientQuantity.ingredient.name}</div>
            <Input type='number' step="0.01" defaultValue={ingredientQuantity.quantity} onChange={handleQuantityChange}/>
            <div>{ingredientQuantity.ingredient.measurementUnit.name}</div>
            <Button color='danger' type='button' onClick={() => unselectIngredient(ingredientQuantity.ingredient)}>Remove</Button>
        </div>
        )
    });

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    const allQuantitiesAreSet = () => {
        for(let i = 0; i < selectedIngredientsAndQuantities.length; i++){
            if(selectedIngredientsAndQuantities[i].quantity < 0.01) return false;
        }
        return true;
    }

    const textIsValid = (text) => {
        return text !== "" && text !== null && typeof text !== 'undefined';
    }

    const isSubmitAvailable = () => {
        return !(
            selectedIngredientsAndQuantities.length > 0 && 
            textIsValid(description) &&
            textIsValid(name) &&
            allQuantitiesAreSet()
            );
    }

    const handleNameChange = (event) => {
        setName(event.target.value);
    }

    const handleSubmit = () => {
        if (params.id === 'write') doAddRecipe();
        else doUpdateRecipe();
    }

    const doAddRecipe = () => {
        let submitUrl = 'http://localhost:8080/recipe/add/' + cookies.user.id;
        axios.post(submitUrl, {
            recipe: {
                id: recipeId,
                name: name,
                description: description,
                tags: [],
                quantities: selectedIngredientsAndQuantities
            },
            owner: true
        }).then(
            navigate('/recipes')
        );
    }

    const doUpdateRecipe = () => {
        let submitUrl = 'http://localhost:8080/recipe/update';
        axios.put(submitUrl, {
            id: recipeId,
            name: name,
            description: description,
            tags: [],
            quantities: selectedIngredientsAndQuantities
        }).then(
            navigate('/recipes')
        );
    }

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Input type='text' placeholder='Name your recipe...' defaultValue={name} onChange={handleNameChange}/>
                </FormGroup>
                <FormGroup>
                    <Label for='ingredientSelect'>Add a new ingredient from here</Label>
                    <Select id='ingredientSelect' value={selectedIngredient} options={availableIngredients} onChange={handleSelectChange}/>
                </FormGroup>
                <FormGroup>
                    <Label>Add the quantities for each ingredient</Label>
                    {quantitiesListForRender}
                </FormGroup>
                <FormGroup>
                    <Label>Describe how to cook the recipe here</Label>
                    <Input id='description' type='textarea' onChange={handleDescriptionChange} rows={10} defaultValue={description}/>
                </FormGroup>
                <ButtonGroup>
                    <Button type='submit' color='success' hidden={isSubmitAvailable()}>Submit</Button>
                    <Button type='button' color='secondary' tag={Link} to='/recipes'>Go back</Button>
                </ButtonGroup>
            </Form>
        </Container>
    )
}

export default WriteRecipe;
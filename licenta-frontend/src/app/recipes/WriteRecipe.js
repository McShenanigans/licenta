import {useCookies} from 'react-cookie';
import {useEffect, useState} from 'react';
import {useNavigate, Link, useParams} from 'react-router-dom';
import Select from 'react-select';
import {Button, ButtonGroup, Container, Input, Form, FormGroup, Label} from 'reactstrap';
import axios from 'axios'

import "../../styles/general.css";

function WriteRecipe(){
    const activeRecipeVisibilityButtonColor = 'button-primary';
    const inactiveRecipeVisibilityButtonColor = 'button-secondary';
    const [privateButtonColor, setPrivateButtonColor] = useState(activeRecipeVisibilityButtonColor);
    const [publicButtonColor, setPublicButtonColor] = useState(inactiveRecipeVisibilityButtonColor);
    const [cookies, setCookies] = useCookies();
    const [availableIngredients, setAvailalbleIngredients] = useState([]);
    const [selectedIngredientsAndQuantities, setSelectedIngredientsAndQuantities] = useState([]);
    const [selectedIngredient, setSelectedIngredient] = useState(null);
    const [availableTags, setAvailableTags] = useState([]);
    const [selectedTags, setSelectedTags] = useState([]);
    const [selectedTag, setSelectedTag] = useState(null);
    const [description, setDescription] = useState("");
    const [name, setName] = useState("");
    const [recipeId, setRecipeId] = useState(null);
    const [isPublic, setIsPublic] = useState(false);
    const [timeTagSelectList, setTimeTagSelectList] = useState([]);
    const [selectedTimeTag, setSelectedTimeTag] = useState(null);
    const params = useParams();
    const ingredientIdPrefix = 'ingredient';
    const tagIdPrefix = 'tag';

    const navigate = useNavigate();

    const doGetAllIngredients = () => {
        if(typeof cookies.user.id === 'undefined') navigate('/');
        axios.get('http://localhost:8080/admin/ingredients/')
        .then((response) => {
            let options = [];
            response.data.forEach(ingredient => {
                options.push({value: ingredient, label: ingredient.name});
            });
            axios.get('http://localhost:8080/recipe/tags').then(responseTags => {
                let tagOptions = [];
                responseTags.data.forEach(tag => {
                    tagOptions.push({value: tag, label: tag.name});
                });
                axios.get('http://localhost:8080/recipe/timeTags').then(responseTimeTags => {
                    setTimeTagSelectList(responseTimeTags.data);
                    if(params.id !== 'write'){
                        axios.get('http://localhost:8080/recipe/' + cookies.user.id + '/' + params.id).then(response => {
                            if(response.data === null) navigate('/404');

                            setRecipeId(response.data.recipe.id);
                            setName(response.data.recipe.name);
                            setDescription(response.data.recipe.description);
                            setSelectedIngredientsAndQuantities(response.data.recipe.quantities);
                            setSelectedTags(response.data.recipe.tags);
                            setIsPublic(response.data.recipe.public);
                            setSelectedTimeTag(response.data.recipe.timeTag);
    
                            response.data.recipe.quantities.forEach(quantity => {
                                let index = -1;
                                for (let i = 0; i < options.length; i++){
                                    if(options[i].value.id === quantity.ingredient.id) {
                                        index = i;
                                        break;
                                    }
                                }
                                if(index > -1) options.splice(index, 1);
                            });
                            setAvailalbleIngredients(options);
    
                            response.data.recipe.tags.forEach(tag => {
                                let index = -1;
                                for (let i = 0; i < tagOptions.length; i++){
                                    if(tagOptions[i].value.id === tag.id){
                                        index = i;
                                        break;
                                    }
                                }
                                if(index > -1) tagOptions.splice(index, 1);
                            });
                            setAvailableTags(tagOptions);
    
                            if(response.data.recipe.isPublic === true){
                                setPublicButtonColor(activeRecipeVisibilityButtonColor);
                                setPrivateButtonColor(inactiveRecipeVisibilityButtonColor);
                            }
                        })
                    }
                    else {
                        setAvailalbleIngredients(options);
                        setAvailableTags(tagOptions);
                    }
                })
            })
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

    const handleTagChange = (event) => {
        let tag = event.value;

        let newSelectedTags = [];
        newSelectedTags.push(tag);
        selectedTags.forEach(sTag => {
            newSelectedTags.push(sTag);
        })
        setSelectedTags(newSelectedTags);

        let newAvailableTags = [];
        availableTags.forEach(aTag => {
            if(aTag.value.id !== tag.id) {
                newAvailableTags.push(aTag);
            }
        });
        setAvailableTags(newAvailableTags);

        setSelectedTag(null)
    }

    const handleQuantityChange = (event) => {
        if(event.target.value === "") return;
        let newIngredientsAndQuantities = [];
        selectedIngredientsAndQuantities.forEach(ingredientQuantity => {
            let idToHandle = parseInt(event.target.parentElement.id.split(ingredientIdPrefix)[1]);
            if(ingredientQuantity.ingredient.id === idToHandle){
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

    const unselectTag = (tag) => {
        let newSelectedTags = [];
        selectedTags.forEach(sTag => {
            if(sTag.id !== tag.id) newSelectedTags.push(sTag);
        })
        setSelectedTags(newSelectedTags);

        let newAvailableTags = [];
        newAvailableTags.push({value: tag, label: tag.name});
        availableTags.forEach(aTag => {
            newAvailableTags.push(aTag);
        })
        setAvailableTags(newAvailableTags);
    }

    const quantitiesListForRender = selectedIngredientsAndQuantities.map((ingredientQuantity) => {
        return (
            <div className='card-container'>
                <div className='card' key={ingredientIdPrefix + ingredientQuantity.ingredient.id} id={ingredientIdPrefix + ingredientQuantity.ingredient.id}>
                    <div className='card-content'>
                        <div className='card-content-item-small'>{ingredientQuantity.ingredient.name} ({ingredientQuantity.ingredient.measurementUnit.name})</div>
                    </div>
                    <Input type='number' step="0.01" defaultValue={ingredientQuantity.quantity} onChange={handleQuantityChange}/>
                    <Button className='button-danger-full' type='button' onClick={() => unselectIngredient(ingredientQuantity.ingredient)}>Remove</Button>
                </div>
            </div>
        )
    });

    const tagListForRender = selectedTags.map(tag => {
        return (
        <div key={tagIdPrefix + tag.id} id={tagIdPrefix + tag.id} className='card-small'>
            <div className='card-content'>
                <div className='card-content-item-small'>{tag.name}</div>
                <Button className='button-danger' type='button' onClick={() => unselectTag(tag)}>X</Button>
            </div>
        </div>
        )
    });

    const timeTagListForRender = timeTagSelectList.map(timeTag => {
        return <Button className={selectedTimeTag !== null && selectedTimeTag.id === timeTag.id ? activeRecipeVisibilityButtonColor : inactiveRecipeVisibilityButtonColor} type='button' onClick={() => setSelectedTimeTag(timeTag)}>{timeTag.name}</Button>
    })

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
            allQuantitiesAreSet() &&
            selectedTimeTag !== null
            );
    }

    const handleNameChange = (event) => {
        setName(event.target.value);
    }

    const handleSubmit = () => {
        if (params.id === 'write') doAddRecipe();
        else doUpdateRecipe();
    }

    const handleIsPublicChange = (value) => {
        setIsPublic(value);
        if(value) {
            setPublicButtonColor(activeRecipeVisibilityButtonColor);
            setPrivateButtonColor(inactiveRecipeVisibilityButtonColor);
            return;
        }
        setPublicButtonColor(inactiveRecipeVisibilityButtonColor);
        setPrivateButtonColor(activeRecipeVisibilityButtonColor);
    }

    const doAddRecipe = () => {
        let submitUrl = 'http://localhost:8080/recipe/add/' + cookies.user.id;
        axios.post(submitUrl, {
            recipe: {
                id: recipeId,
                name: name,
                description: description,
                isPublic: isPublic,
                tags: selectedTags,
                quantities: selectedIngredientsAndQuantities,
                timeTag: selectedTimeTag
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
            isPublic: isPublic,
            tags: selectedTags,
            quantities: selectedIngredientsAndQuantities,
            timeTag: selectedTimeTag
        }).then(
            navigate('/recipes')
        );
    }

    return (
        <Container className='font-general'>
            <Form onSubmit={handleSubmit}>
                <div className='form-body'>
                    <FormGroup>
                        <Label>Name your recipe *</Label>
                        <Input type='text' defaultValue={name} onChange={handleNameChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for='tagSelect'>Assign tags to your recipe *</Label>
                        <Select id='tagSelect' value={selectedTag} options={availableTags} onChange={handleTagChange}/>
                        <div className='cards-container-small'>{tagListForRender}</div>
                    </FormGroup>
                    <FormGroup>
                        <Label>At what time should this recipe be eaten? *</Label><br/>
                        <ButtonGroup>{timeTagListForRender}</ButtonGroup>
                    </FormGroup>
                    <FormGroup>
                        <Label for='ingredientSelect'>Add a new ingredient from here *</Label>
                        <Select id='ingredientSelect' value={selectedIngredient} options={availableIngredients} onChange={handleSelectChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label>Add the quantities for each ingredient *</Label>
                        <div className='cards-container-small'>{quantitiesListForRender}</div>
                    </FormGroup>
                    <FormGroup>
                        <Label>Describe how to cook the recipe here *</Label>
                        <Input id='description' type='textarea' onChange={handleDescriptionChange} rows={10} defaultValue={description}/>
                    </FormGroup>
                    <FormGroup>
                        <Label>Set your recipe's visibility</Label>
                        <ButtonGroup>
                            <Button type='button' className={publicButtonColor} onClick={() => handleIsPublicChange(true)}>Public</Button>
                            <Button type='button' className={privateButtonColor} onClick={() => handleIsPublicChange(false)}>Private</Button>
                        </ButtonGroup>
                    </FormGroup>
                </div>
                <div className='form-button-area'>
                    <Button type='submit' className='button-primary' hidden={isSubmitAvailable()}>Submit</Button>
                    <Button type='button' className='button-warning' tag={Link} to='/recipes'>Go back</Button>
                </div>
            </Form>
        </Container>
    )
}

export default WriteRecipe;
import {useState, useEffect} from 'react';
import {useCookies} from 'react-cookie';
import axios from 'axios';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';

function CreateUserIngredient() {
    const [ingredient, setIngredient] = useState({id: -1, name: ""});
    const [quantity, setQuantity] = useState(0);
    const [ingredients, setIngredients] = useState([]);
    const [cookies, setCookies] = useCookies();
    const navigate = useNavigate();
    const params = useParams();
    const [isIngredientNameEditable, setIsIngredientNameEditable] = useState(true);
    const [submitUrl, setSubmitUrl] = useState('http://localhost:8080/ingredients/create/'+cookies.user.id);
    const [submitText, setSubmitText] = useState('Add');

    useEffect(() => {
        if(typeof cookies.user === 'undefined') navigate('/');
        if(params.id !== 'create'){
            setIsIngredientNameEditable(false);
            axios.get('http://localhost:8080/ingredients/'+cookies.user.id+'/'+params.id)
            .then((response) => {
                setIngredient(response.data.ingredient);
                setIngredients([response.data.ingredient]);
                setQuantity(response.data.quantity);
                setSubmitUrl('http://localhost:8080/ingredients/update/'+cookies.user.id);
                setSubmitText('Update');
            })
        }
        else {
            axios.get('http://localhost:8080/ingredients/absent/' + cookies.user.id).then(response => {
                setIngredients(response.data);
                if(response.data.length > 0) setIngredient(response.data[0]);
            })
        }
    }, []);

    const handleChange = (event) => {
        const targetName = event.target.name;
        const targetValue = event.target.value;

        if(targetName === 'quantity'){
            setQuantity(targetValue);
        } else if (targetName === 'ingredient'){
            setIngredient(ingredients[targetValue]);
        }
    }

    const handleSubmit = () => {
        axios.post(submitUrl, {
            user: null,
            ingredient: ingredient,
            quantity: quantity
        }).then(
            navigate('/ingredients')
        );
    }

    const ingredientSelectList = ingredients.map((ingred, index) => {
        return <option key={index} value={index}>{ingred.name} ({ingred.measurementUnit.name})</option>
    })

    return(<Container>
        <Form onSubmit={handleSubmit}>
            <FormGroup>
                <Label for='ingredient'>Select an ingredient to add to your pantry</Label>
                <Input type='select' name='ingredient' id='ingredient' onChange={handleChange} defaultValue={ingredient.name} readOnly={!isIngredientNameEditable}>
                    {ingredientSelectList}
                </Input>
            </FormGroup>
            <FormGroup>
                <Label>What is the amount of this ingredient you have left?</Label>
                <Input type='number' name='quantity' id='quantity' value={quantity} onChange={handleChange} />
            </FormGroup>
            <FormGroup>
                <Button color='primary' type='submit' hidden={ingredients.length === 0}>{submitText}</Button>
                <Button color='secondary' type='button' tag={Link} to='/ingredients'>Cancel</Button>
            </FormGroup>
        </Form>
    </Container>
    );
}

export default CreateUserIngredient;
import {useState, useEffect} from 'react';
import axios from 'axios';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label, InputGroup} from 'reactstrap';

function CreateIngredient() {
    const params = useParams();
    const [name, setName] = useState("");
    const [measurementUnit, setMeasurementUnit] = useState({id: -1, name: ""});
    const [measurementUnits, setMeasurementUnits] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [categories, setCategories] = useState([]);
    const [ingredientId, setIngredientId] = useState(null);
    const [submitUrl, setSubmitUrl] = useState('http://localhost:8080/admin/ingredients/create');
    const [submitText, setSubmitText] = useState('Create');
    
    useEffect(() => {
        if(params.id !== 'create'){
            axios.get('http://localhost:8080/admin/ingredients/'+params.id).then(response => {
                setName(response.data.name);
                setMeasurementUnit(response.data.measurementUnit);
                setSelectedCategories(response.data.categories);
                setIngredientId(response.data.id);
                setSubmitUrl('http://localhost:8080/admin/ingredients/update');
                setSubmitText('Update');
            })
        }
        axios.get('http://localhost:8080/measurementUnits/').then((response => {
            setMeasurementUnits(response.data);
            if(measurementUnit.id === -1) setMeasurementUnit(response.data[0]);
        }));
        axios.get('http://localhost:8080/admin/ingredientCategory/').then((response => {
            setCategories(response.data);
        }));
    }, []);

    const navigate = useNavigate();

    const handleSubmit = () => {
        axios.post(submitUrl, {
            id: ingredientId,
            name: name,
            measurementUnit: measurementUnit,
            categories: selectedCategories
        }).then(
            navigate("/admin/ingredients")
        );
    };

    const handleChange = (event) => {
        const targetName = event.target.name;
        const targetValue = event.target.value;
        const targetChecked = event.target.checked;

        if(targetName === 'name') {
            setName(targetValue);
        }
        else if (targetName === 'measurementUnit') {
            setMeasurementUnit(measurementUnits[targetValue]);
        }
        else if (targetName === 'categories') {
            if (targetChecked === true && selectedCategories.indexOf(categories[targetValue]) < 0){
                selectedCategories.push(categories[targetValue]);
            } else if (targetChecked === false && selectedCategories.indexOf(categories[targetValue]) > -1){
                selectedCategories.splice(selectedCategories.indexOf(categories[targetValue]), 1);
            }
            setSelectedCategories(selectedCategories);
        }
    }

    const categoryIsSelected = (category) => {
        for(let i = 0; i < selectedCategories.length; i++){
            if(selectedCategories[i].id === category.id) {
                return true;
            }
        }
        return false;
    }

    const measurementUnitSelectList = measurementUnits.map((unit, index) => {
        return <option key={index} value={index} selected={measurementUnit.id === unit.id}>{unit.name}</option>;
    });

    const categoryCheckboxList = categories.map((category, index) => {
        return (<InputGroup key={index}>
            <Input value={index} type='checkbox' onChange={handleChange} name='categories' defaultChecked={categoryIsSelected(category)}/>
            <span>{category.name}</span>
        </InputGroup>);
    });

    return (
        <Container>
            <h3>Create Ingredient</h3>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for='name'>Name: </Label>
                    <Input type='text' name='name' id='name' value={name} onChange={handleChange}/>
                </FormGroup>
                <FormGroup>
                    <Label for='measurementUnit'>Measurement unit: </Label>
                    <Input type='select' name='measurementUnit' id='measurementUnit' onChange={handleChange} defaultValue={measurementUnit.name}>
                        {measurementUnitSelectList}
                    </Input>
                </FormGroup>
                <FormGroup>
                    <Label for='categories'>Select the categories of the ingredient</Label>
                    <InputGroup id='categories'>
                        {categoryCheckboxList}
                    </InputGroup>
                </FormGroup>
                <FormGroup>
                    <Button color='primary' type='submit'>{submitText}</Button>
                    <Button color='secondary' tag={Link} to='/admin/ingredients'>Cancel</Button>
                </FormGroup>
            </Form>
        </Container>
    )
}

export default CreateIngredient;
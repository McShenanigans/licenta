import { Button, Label } from "reactstrap";
import Select from "react-select";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import axios from "axios";
import DateTimePicker from "react-datetime-picker";

const ScheduleModal = props => {

    const [selectedRecipe, setSelectedRecipe] = useState(null);
    const [recipes, setRecipes] = useState([]);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [cookies, setCookies] = useCookies();

    const handleSelectChange = (event) => {
        setSelectedRecipe(event.value);
    };

    const doGetAll = () => {
        axios.get("http://localhost:8080/recipe/"+cookies.user.id)
        .then((response) => {
            let newRecipes = []
            response.data.forEach(entry => {
                newRecipes.push({value: entry.recipe, label: entry.recipe.name});
            });
            setRecipes(newRecipes);
        });
    }

    useEffect(() => {
        doGetAll();
    }, []);

    const handleSubmit = () => {
        axios.post('http://localhost:8080/schedule/add/'+cookies.user.id, {
            id: null,
            recipe: selectedRecipe,
            date: selectedDate
        }).then(() => {
            props.onClose();
        });
    };

    if(!props.show) return null;
    return(
        <div className="modal font-general" onClick={props.onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h4 className="modal-title">Schedule a recipe for a date</h4>
                    <Button className="button-danger" onClick={() => props.onClose()}>X</Button>
                </div>
                <div className="modal-body">
                    <div className="modal-body-section">
                        <h5>Choose a recipe</h5>
                        <Select options={recipes} onChange={handleSelectChange}/>
                    </div>
                    <div className="modal-body-section">
                        <h5>Choose a time</h5>
                        <DateTimePicker onChange={setSelectedDate} value={selectedDate}/>
                    </div>
                </div>
                <div className="modal-footer">
                    <Button className="button-primary" onClick={() => handleSubmit()}>Create</Button>
                </div>
            </div>
        </div>
    )
}

export default ScheduleModal;
import axios from "axios";
import { Button } from "reactstrap";

import "../../styles/general.css";
import "../../styles/schedule.css";


const ConfirmRecipeCompletionDialog = props => {
    if(!props.show) return null;

    const handleAnswer = (answer) => {
        axios.delete('http://localhost:8080/schedule/delete/' + props.entry.id + '/' + answer)
        .then(() => {
            props.onClose();
        })
    }

    return (
        <div className="modal">
            <div className="modal-content">
                <div className="modal-header">
                    <h4 className="modal-title">Did you manage to cook this recipe?</h4>
                </div>
                <div className="modal-body">
                    <div>
                        <h5>{props.entry.recipe.name}</h5>
                        <h5>Date: {props.date}</h5>
                        <h5>Ingredients for this recipe will be automatically subtracted from your pantry</h5>
                    </div>
                </div>
                <div className="modal-footer">
                    <Button className="button-primary" onClick={() => handleAnswer(true)}>Yes</Button>
                    <Button className="button-warning" onClick={() => handleAnswer(false)}>No</Button>
                </div>
            </div>
        </div>
    )
}

export default ConfirmRecipeCompletionDialog;
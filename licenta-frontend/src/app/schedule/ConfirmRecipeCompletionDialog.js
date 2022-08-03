import axios from "axios";
import { Label, Button } from "reactstrap";

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
                        <Label>{props.entry.recipe.name}</Label>
                        <Label>Date: {props.date}</Label>
                    </div>
                </div>
                <div className="modal-footer">
                    <Button color="success" onClick={() => handleAnswer(true)}>Yes</Button>
                    <Button color="danger" onClick={() => handleAnswer(false)}>No</Button>
                </div>
            </div>
        </div>
    )
}

export default ConfirmRecipeCompletionDialog;
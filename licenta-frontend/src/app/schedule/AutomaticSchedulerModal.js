import { useEffect, useState } from "react";
import axios from 'axios';
import { useCookies } from "react-cookie";
import { ButtonGroup, Input, Label, Button } from "reactstrap";

import "../../styles/general.css";

const AutomaticSchedulerModal = props => {
    const [cookies, setCookies] = useCookies();
    const [numberOfDays, setNumberOfDays] = useState(0);
    const [timeTags, setTimeTags] = useState([]);
    const [selectedTimeTags, setSelectedTimeTags] = useState([]);

    const getAll = () => {
        axios.get('http://localhost:8080/recipe/timeTags').then((response) =>  {
            setTimeTags(response.data);
        })
    }

    useEffect(() => {getAll()}, []);

    const selectTag = (tag) => {
        let newSelectedTags = [tag];
        selectedTimeTags.forEach(timeTag => newSelectedTags.push(timeTag));
        setSelectedTimeTags(newSelectedTags);
    }

    const unselectTag = (tag) => {
        let newSelectedTags = [];
        selectedTimeTags.forEach(timeTag => {
            if(tag.id !== timeTag.id) newSelectedTags.push(timeTag);
        });
        setSelectedTimeTags(newSelectedTags);
    }

    const isTagSelected = (tag) => {
        for( let i = 0; i < selectedTimeTags.length; i++)
            if(selectedTimeTags[i].id === tag.id) return true;
        return false;
    }

    const flipTagSelection = (tag) => {
        if(isTagSelected(tag)) unselectTag(tag);
        else selectTag(tag);
    }

    const list = timeTags.map(tag => {
        return <Button className={isTagSelected(tag) ? 'button-primary' : 'button-warning'} onClick={() => flipTagSelection(tag)}>{tag.name}</Button>
    });

    const handleDaysChange = (event) => {
        setNumberOfDays(parseInt(event.target.value));
    }

    const handleSubmit = () => {
        axios.post('http://localhost:8080/schedule/auto', {
            user: cookies.user,
            numberOfDays: numberOfDays,
            timeTags: selectedTimeTags,
            timeZoneDifferenceInHours: new Date().getTimezoneOffset()/60
        }).then(() => {
            window.location.reload();
        });
    }

    if(!props.show) return null;
    return (<div className="modal" onClick={props.onClose}>
                <div className="modal-content" onClick={e => e.stopPropagation()}>
                    <div className="modal-header">
                        <h4 className="modal-title">Automatic recipe scheduler</h4>
                        <Button className="button-danger" onClick={() => props.onClose()}>X</Button>
                    </div>
                    <div className="modal-body">
                        <div>
                            <Label>For how many days, starting tommorow, should your scheduled be filled in?</Label>
                            <Input type="number" onChange={handleDaysChange} value={numberOfDays}/>
                        </div>
                        <div>
                            <Label>At what times of day do you want to eat?</Label>
                            <ButtonGroup>{list}</ButtonGroup>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <Button className="button-primary" onClick={() => handleSubmit()}>Start</Button>
                    </div>
                </div>
            </div>)
}

export default AutomaticSchedulerModal;
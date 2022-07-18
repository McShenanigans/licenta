import {useCookies} from 'react-cookie';
import {useNavigate} from 'react-router-dom';
import {useEffect, useState} from 'react';
import { Button, Label } from 'reactstrap';
import axios from 'axios';
import ScheduleModal from './AddScheduleEntryModal';

function Schedule(){
    const [cookie, setCookies] = useCookies();
    const [currentEntries, setCurrentEntries] = useState([]);
    const [showAddEntryModal, setShowAddEntryModal] = useState(false);
    const [renderFlag, setRenderFlag] = useState(false);
    const navigate = useNavigate();

    const doGetAll = () => {
        axios.get("http://localhost:8080/schedule/"+cookie.user.id)
        .then((response) => {
            setCurrentEntries(response.data);
        });
    };

    useEffect(() => {
        if(typeof cookie.user === 'undefined') navigate('/');
        doGetAll();
    }, [renderFlag]);

    const flipRenderFlag = () => {
        setRenderFlag(!renderFlag);
    };

    const entriesRenderList = currentEntries.map(entry => {
        return (<div key={entry.id}>
            <Button color='danger' onClick={() => handleEntryDelete(entry.id)}>X</Button>
            <Label>{entry.recipe.name}</Label>
            <Label>{entry.date}</Label>
        </div>)
    });

    const handleModalOnClose = () => {
        setShowAddEntryModal(false);
        flipRenderFlag();
    };

    const handleEntryDelete = (entryId) => {
        axios.delete('http://localhost:8080/schedule/delete/' + entryId)
        .then(flipRenderFlag());
    };

    return (
        <div>
            <div>
                <Button color='primary' onClick={() => setShowAddEntryModal(true)}>Schedule a recipe</Button>
            </div>
            <div>
                {entriesRenderList}
            </div>
            <ScheduleModal show={showAddEntryModal} onClose={() => handleModalOnClose()}/>
        </div>
    )
}

export default Schedule;
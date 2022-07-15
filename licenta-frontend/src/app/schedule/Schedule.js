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
    }

    useEffect(() => {
        if(typeof cookie.user === 'undefined') navigate('/');
        doGetAll();
    }, [renderFlag]);

    const entriesRenderList = currentEntries.map(entry => {
        return (<div key={entry.id}>
            <Button color='danger'>X</Button>
            <Label>{entry.recipe.name}</Label>
            <Label>{entry.data}</Label>
        </div>)
    })

    return (
        <div>
            <div>
                <Button color='primary' onClick={() => setShowAddEntryModal(true)}>Schedule a recipe</Button>
            </div>
            <div>
                {entriesRenderList}
            </div>
            <ScheduleModal show={showAddEntryModal} onClose={() => {
                    setShowAddEntryModal(false);
                    setRenderFlag(!renderFlag);
                }}/>
        </div>
    )
}

export default Schedule;
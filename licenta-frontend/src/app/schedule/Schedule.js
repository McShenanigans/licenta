import {useCookies} from 'react-cookie';
import {useNavigate} from 'react-router-dom';
import {useEffect, useState} from 'react';
import { Button, Label } from 'reactstrap';
import axios from 'axios';
import ScheduleModal from './AddScheduleEntryModal';
import { type } from '@testing-library/user-event/dist/type';

function Schedule(){
    const [cookie, setCookies] = useCookies();
    const [currentEntries, setCurrentEntries] = useState([]);
    const [showAddEntryModal, setShowAddEntryModal] = useState(false);
    const [renderFlag, setRenderFlag] = useState(false);
    const navigate = useNavigate();

    const doGetAll = () => {
        const client = axios.create();

        client.interceptors.response.use((response) => {
            handleDates(response.data);
            return response;
        });

        client.get("http://localhost:8080/schedule/"+cookie.user.id)
        .then((response) => {
            setCurrentEntries(response.data);
        });
    };

    const isoDateFormat = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d*)?(?:[-+]\d{2}:?\d{2}|Z)?$/;

    const isIsoDateString = (value) => {
        return value && typeof value === 'string' && isoDateFormat.test(value+'Z');
    }

    const handleDates = (data) => {
        if(data === null || data === undefined || typeof data !== 'object') return data;

        for(const key of Object.keys(data)){
            const value = data[key];
            if(isIsoDateString(value)) data[key] = new Date(value + 'Z');
            else if (typeof value === 'object') handleDates(value);
        }
    };

    useEffect(() => {
        if(typeof cookie.user === 'undefined') navigate('/');
        doGetAll();
    }, [renderFlag]);

    const flipRenderFlag = () => {
        setRenderFlag(!renderFlag);
    };

    const getHourMinutesString = (date) => {
        let hour = date.getHours();
        let minutes = date.getMinutes();
        if(minutes < 10) minutes = '0' + minutes;
        return hour.toString() + ':' + minutes;
    }

    const entriesRenderList = currentEntries.map(entry => {
        return (<div key={entry.id}>
            <Button color='danger' onClick={() => handleEntryDelete(entry.id)}>X</Button>
            <Label>{entry.recipe.name}</Label>
            <br/>
            <Label>{'Scheduled for ' + entry.date.toDateString() + ', at ' + getHourMinutesString(entry.date)}</Label>
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
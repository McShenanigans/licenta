import {useCookies} from 'react-cookie';
import {useNavigate} from 'react-router-dom';
import {useEffect, useState} from 'react';
import { Button, ButtonGroup, Label } from 'reactstrap';
import axios from 'axios';
import ScheduleModal from './AddScheduleEntryModal';
import MissingIngredients from './ViewMissingIngredientsModal';
import ShoppingListModal from './ShoppingListModal';

function Schedule(){
    const [cookie, setCookies] = useCookies();
    const [currentEntries, setCurrentEntries] = useState([]);
    const [showAddEntryModal, setShowAddEntryModal] = useState(false);
    const [showShoppingListModal, setShowShoppingListModal] = useState(false);
    const [missingIngredientsModalToShow, setMissingIngredientsModalToShow] = useState(null);
    const [renderFlag, setRenderFlag] = useState(false);
    const navigate = useNavigate();

    const doGetAll = () => {
        const client = axios.create();

        client.interceptors.response.use((response) => {
            handleDates(response.data);
            return response;
        });

        client.get("http://localhost:8080/schedule/" + cookie.user.id)
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

    const handleModalOnClose = () => {
        setShowAddEntryModal(false);
        setMissingIngredientsModalToShow(null);
        setShowShoppingListModal(false);
        flipRenderFlag();
    };

    const handleEntryDelete = (entryId) => {
        axios.delete('http://localhost:8080/schedule/delete/' + entryId)
        .then(flipRenderFlag());
    };

    const enableMissingIngredientsModal = (id) => {
        setMissingIngredientsModalToShow(id);
        flipRenderFlag();
    }

    const entriesRenderList = currentEntries.map(complexEntry => {
        return (<div key={complexEntry.entry.id}>
            <Label>{complexEntry.entry.recipe.name}</Label>
            <br/>
            <Label>{'Scheduled for ' + complexEntry.entry.date.toDateString() + ', at ' + getHourMinutesString(complexEntry.entry.date)}</Label>
            <ButtonGroup>
                <Button color='danger' onClick={() => handleEntryDelete(complexEntry.entry.id)}>X</Button>
                <Button color='primary' hidden={complexEntry.allIngredientsAvailable === true} 
                    onClick={() => enableMissingIngredientsModal(complexEntry.entry.id)}>
                    See the ingredients you are missing
                </Button>
            </ButtonGroup>
            <MissingIngredients show={missingIngredientsModalToShow === complexEntry.entry.id} onClose={() => handleModalOnClose()} missingIngredients={complexEntry.missingIngredients}/>
        </div>)
    });

    return (
        <div>
            <div>
                <ButtonGroup>
                    <Button color='primary' onClick={() => setShowAddEntryModal(true)}>Schedule a recipe</Button>
                    <Button color='secondary' onClick={() => setShowShoppingListModal(true)}>See shopping list</Button>
                </ButtonGroup>
            </div>
            <div>
                {entriesRenderList}
            </div>
            <ScheduleModal show={showAddEntryModal} onClose={() => handleModalOnClose()}/>
            <ShoppingListModal  show={showShoppingListModal} onClose={() => handleModalOnClose()}/>
        </div>
    )
}

export default Schedule;
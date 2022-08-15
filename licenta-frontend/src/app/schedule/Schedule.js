import {useCookies} from 'react-cookie';
import {useNavigate} from 'react-router-dom';
import {useEffect, useState} from 'react';
import { Button, ButtonGroup, Label } from 'reactstrap';
import axios from 'axios';
import ScheduleModal from './AddScheduleEntryModal';
import MissingIngredients from './ViewMissingIngredientsModal';
import ShoppingListModal from './ShoppingListModal';
import ConfirmRecipeCompletionDialog from './ConfirmRecipeCompletionDialog';
import AutomaticSchedulerModal from './AutomaticSchedulerModal';

import "../../styles/general.css";
import "../../styles/schedule.css";
import RecipeDetailsModal from '../recipes/RecipeDetailsModal';

function Schedule(){
    const [cookie, setCookies] = useCookies();
    const [currentEntries, setCurrentEntries] = useState([]);
    const [idsOfPastEntries, setIdsOfPastEntries] = useState([]);
    const [showAddEntryModal, setShowAddEntryModal] = useState(false);
    const [showShoppingListModal, setShowShoppingListModal] = useState(false);
    const [showAutomaticSchedulerModal, setShowAutomaticSchedulerModal] = useState(false);
    const [missingIngredientsModalToShow, setMissingIngredientsModalToShow] = useState(null);
    const [recipeDetailsModalToShow, setRecipeDetailsModalToShow] = useState(null);
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

            if(idsOfPastEntries.length > 0) return;
            
            let newIdsOfPastEntries = [];
            let date = new Date();
            response.data.forEach(complexEntry => {
                if(complexEntry.entry.date.getTime() < date.getTime()) newIdsOfPastEntries.push(complexEntry.entry.id);
            });
            setIdsOfPastEntries(newIdsOfPastEntries);
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
        setShowAutomaticSchedulerModal(false);
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

    const removeEntry = (entryId) => {
        let newIdsForPastEntries = [];
        idsOfPastEntries.forEach(entry => {
            if(entryId !== entry) newIdsForPastEntries.push(entry);
        });
        setIdsOfPastEntries(newIdsForPastEntries);
        flipRenderFlag();
    }

    const entriesRenderList = currentEntries.map(complexEntry => {
        return (
        <div key={complexEntry.entry.id} className="card-container">
            <div className='card'>
                <h5 className='card-content-item-small'>{complexEntry.entry.recipe.name}</h5>
                <h5 className='card-content-item-small'>{'Scheduled for ' + complexEntry.entry.date.toDateString() + ', at ' + getHourMinutesString(complexEntry.entry.date)}</h5>
            </div>
            <div className='card-extension'>
                <Button className='button-primary-full' hidden={complexEntry.allIngredientsAvailable === true} 
                    onClick={() => enableMissingIngredientsModal(complexEntry.entry.id)}>
                    Missing ingredients
                </Button>
                <Button className='button-warning-full' onClick={() => {setRecipeDetailsModalToShow(complexEntry.entry.recipe.id)}}>Details</Button>
                <Button className='button-danger-full' onClick={() => handleEntryDelete(complexEntry.entry.id)}>Cancel</Button>
            </div>
            <MissingIngredients show={missingIngredientsModalToShow === complexEntry.entry.id} onClose={() => handleModalOnClose()} missingIngredients={complexEntry.missingIngredients}/>
            <ConfirmRecipeCompletionDialog
                show={idsOfPastEntries.indexOf(complexEntry.entry.id) !== -1} 
                entry={complexEntry.entry} date={complexEntry.entry.date.toDateString() + ', ' + getHourMinutesString(complexEntry.entry.date)}
                onClose={() => removeEntry(complexEntry.entry.id)}
            />
            <RecipeDetailsModal recipe={complexEntry.entry.recipe} show={recipeDetailsModalToShow === complexEntry.entry.recipe.id} onClose={() => {
                setRecipeDetailsModalToShow(null);
                flipRenderFlag();
            }}/>
        </div>)
    });

    return (
        <div className='font-general'>
            <div className='functions-button-row'>
                <Button className='button-primary' onClick={() => setShowAddEntryModal(true)}>Schedule a recipe</Button>
                <Button className='button-ternary' onClick={() => setShowShoppingListModal(true)}>See shopping list</Button>
                <Button className='button-caution' onClick={() => setShowAutomaticSchedulerModal(true)}>Automatic scheduler</Button>
            </div>
            <div className='cards-container'>
                {entriesRenderList}
            </div>
            <ScheduleModal show={showAddEntryModal} onClose={() => handleModalOnClose()}/>
            <ShoppingListModal  show={showShoppingListModal} onClose={() => handleModalOnClose()}/>
            <AutomaticSchedulerModal show={showAutomaticSchedulerModal} onClose={() => handleModalOnClose()}/>
        </div>
    )
}

export default Schedule;
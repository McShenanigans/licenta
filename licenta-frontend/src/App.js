import './App.css';
import React from 'react';
import Login from './authentication/Login';
import Register from './authentication/Register';
import Schedule from './app/schedule/Schedule';
import UserIngredients from './app/ingredients/UserIngredients';
import CreateUserIngredient from './app/ingredients/CreateUserIngredient';
import Ingredients from './admin/ingredients/Ingredients';
import CreateIngredient from './admin/ingredients/CreateIngredient';
import UserRecipes from './app/recipes/UserRecipes';
import WriteRecipe from './app/recipes/WriteRecipe'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {CookiesProvider} from 'react-cookie';
import Page404 from './404';
import RecipeStore from './app/recipes/RecipeStore';
import Navigation from './app/Navigation';

function App() {
    return (        
        <CookiesProvider>
            <Router>
                <Navigation></Navigation>
                <Routes>
                    <Route path='/' element={<Login/>}/>
                    <Route path='/register' element={<Register/>}/>
                    <Route path='/schedule' element={<Schedule/>}/>
                    <Route path='/ingredients' element={<UserIngredients/>}/>
                    <Route path='/ingredients/:id' element={<CreateUserIngredient/>}/>
                    <Route path='/admin/ingredients' element={<Ingredients/>}/>
                    <Route path='/admin/ingredients/:id' element={<CreateIngredient/>}/>
                    <Route path='/recipes' element={<UserRecipes/>}/>
                    <Route path='/recipes/:id' element={<WriteRecipe/>}/>
                    <Route path='/store' element={<RecipeStore/>}/>
                    <Route path='*' element={<Page404/>}/>
                </Routes>
            </Router>
        </CookiesProvider>
    );
}
export default App;
import './App.css';
import React from 'react';
import Login from './authentication/Login';
import Register from './authentication/Register';
import Home from './app/Home';
import UserIngredients from './app/ingredients/UserIngredients';
import CreateUserIngredient from './app/ingredients/CreateUserIngredient';
import Ingredients from './admin/ingredients/Ingredients';
import CreateIngredient from './admin/ingredients/CreateIngredient';
import UserRecipes from './app/recipes/UserRecipes';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {CookiesProvider} from 'react-cookie';

function App() {
    return (        
        <CookiesProvider>
            <Router>
                <Routes>
                    <Route path='/' element={<Login/>}/>
                    <Route path='/register' element={<Register/>}/>
                    <Route path='/home' element={<Home/>}/>
                    <Route path='/ingredients' element={<UserIngredients/>}/>
                    <Route path='/ingredients/:id' element={<CreateUserIngredient/>}/>
                    <Route path='/admin/ingredients' element={<Ingredients/>}/>
                    <Route path='/admin/ingredients/:id' element={<CreateIngredient/>}/>
                    <Route path='/recipes' element={<UserRecipes/>}/>
                </Routes>
            </Router>
        </CookiesProvider>
    );
}
export default App;
import '../App.css';
import React, { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import {useCookies} from 'react-cookie';

import "../styles/login.css"

function Login() {
    const [isSubmitted, setIsSubmitted] = useState(false);
    let [errorMessage, setErrorMessage] = useState("");

    const navigate = useNavigate();

    const [cookies, setCookies] = useCookies(['user']);

    const handleSubmit = (event) => {
        event.preventDefault();

        var [userEmail, userPassword] = document.forms[0];

        axios.post('http://localhost:8080/authentication/login', {
            email: userEmail.value,
            password: userPassword.value
        }).then((response) => {
            setCookies('user', response.data);
            setIsSubmitted(true);
        }).catch((error) => {
            setErrorMessage(error.response.data);
        })
    };

        const renderForm = (
            <div className = 'form' >
                <form onSubmit = { handleSubmit } >
                    <div className = 'input-container' >
                        <label> Email </label>   
                        <input type = "email" name = 'email'required />
                    </div>  
                    <div className = 'input-container' >
                        <label > Password: </label> 
                        <input type = "password" name = 'password' required />
                    </div>
                    <div>
                        { errorMessage }
                    </div>
                    <div className = 'button-container' >
                        <input type = "submit" />
                    </div>
                </form >
            </div>
        )

        return (
            <div className = "app" >
            <div className = 'login-form' >
            <div className = 'title' > Log In </div> 
                {isSubmitted ? navigate("/home") : renderForm} </div>
                <div>
                    <Link to="/register">Don't have an account? Register here!</Link>
                </div>
            </div>
        );
    }
    export default Login;
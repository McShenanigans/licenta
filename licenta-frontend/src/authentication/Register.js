import '../App.css';
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

import "../styles/login.css"

function Register() {
    const [isSubmitted, setIsSubmitted] = useState(false);
    let [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();

        var [userFirstName, userLastName, userUsername, userEmail, userPassword] = document.forms[0];

        axios.post('http://localhost:8080/authentication/register', {
            id: null,
            username: userUsername.value,
            email: userEmail.value,
            password: userPassword.value,
            firstName: userFirstName.value,
            lastName: userLastName.value
        }).then((response) => {
            setIsSubmitted(true);
        }).catch((error) => {
            setErrorMessage(error.response.data);
        })
    };

        const renderForm = (
            <div className = 'form' >
                <form onSubmit = { handleSubmit } >
                    <div className = 'input-container' >
                        <label> First name </label>   
                        <input type = "text" name = 'firstName'required />
                    </div>  
                    <div className = 'input-container' >
                        <label> Last name </label>   
                        <input type = "text" name = 'lastName'required />
                    </div>  
                    <div className = 'input-container' >
                        <label> Username </label>   
                        <input type = "text" name = 'username'required />
                    </div>
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
            <div className = 'register-form' >
            <div className = 'title' > Register </div> 
                {isSubmitted ? navigate("/") : renderForm} </div>
            </div>  
        );
    }
    export default Register;
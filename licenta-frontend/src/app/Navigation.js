import { useState } from "react";
import { useCookies } from "react-cookie"
import { useLocation } from "react-router-dom";
import { Button, Collapse, Nav, Navbar, NavbarToggler, NavItem, NavLink } from "reactstrap"

import "../styles/general.css";
import "../styles/navigation.css"

function Navigation() {
    const [cookies, setCookies, removeCookies] = useCookies();
    const [isOpen, setIsOpen] = useState(true);
    const location = useLocation();

    if(typeof cookies.user === 'undefined') return null;

    const validRoutes = ['schedule', 'ingredients', 'recipes', 'store'];
    if (validRoutes.indexOf(location.pathname.split('/')[1]) === -1) return null;

    const handleToggle = () => {
        setIsOpen(!isOpen);
    }

    const handleLogOut = () => {
        removeCookies(['user']);
        window.location.reload();
    }

    return (
        <div className="font-general">
            <Navbar expand='md' className="navbar">
                <NavbarToggler onClick={() => handleToggle()}/>
                <Collapse isOpen={isOpen} navbar>
                    <Nav>
                        <NavItem className="navbar-item">
                            <NavLink className="navlink" href='http://localhost:3000/schedule'>Schedule</NavLink>
                        </NavItem>
                        <NavItem className="navbar-item">
                            <NavLink className="navlink" href='http://localhost:3000/ingredients'>Ingredients</NavLink>
                        </NavItem>
                        <NavItem className="navbar-item">
                            <NavLink className="navlink" href='http://localhost:3000/recipes'>Recipes</NavLink>
                        </NavItem>
                        <NavItem className="navbar-item">
                            <NavLink className="navlink" href='http://localhost:3000/store'>Store</NavLink>
                        </NavItem>
                    </Nav>
                    <Button className="logout-btn" type='button' onClick={() => handleLogOut()}>Log out</Button>
                </Collapse>
            </Navbar>
        </div>
    )
}

export default Navigation
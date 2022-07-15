import { useState } from "react";
import { useCookies } from "react-cookie"
import { Collapse, Nav, Navbar, NavbarToggler, NavItem, NavLink } from "reactstrap"

function Navigation() {
    const [cookies, setCookies] = useCookies();
    const [isOpen, setIsOpen] = useState(true);

    if(typeof cookies.user === 'undefined') return <div></div>;

    const handleToggle = () => {
        setIsOpen(!isOpen);
    }

    return (
        <div>
            <Navbar color="light" light expand='md'>
                <NavbarToggler onClick={() => handleToggle()}/>
                <Collapse isOpen={isOpen} navbar>
                    <Nav pills>
                        <NavItem>
                            <NavLink href='http://localhost:3000/home'>Calendar</NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink href='http://localhost:3000/ingredients'>Ingredients</NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink href='http://localhost:3000/recipes'>Recipes</NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink href='http://localhost:3000/store'>Store</NavLink>
                        </NavItem>
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    )
}

export default Navigation
import {useCookies} from 'react-cookie';
import {useNavigate, Link} from 'react-router-dom';
import {useEffect} from 'react';
import { Button } from 'reactstrap';

function Home(){
    const [cookie, setCookies] = useCookies();
    const navigate = useNavigate();

    useEffect(() => {
        if(typeof cookie.user === 'undefined') navigate('/');
    });

    return (
        <div>
            <div>Welcome home!</div>
            <Button size='md' color='primary' tag={Link} to='/ingredients'>Ingredients</Button>
            <Button size='md' color='primary' tag={Link} to='/recipes'>Recipes</Button>
            <Button size='md' color='primary' tag={Link} to='/store'>Recipe Store</Button>
        </div>
    )
}

export default Home;
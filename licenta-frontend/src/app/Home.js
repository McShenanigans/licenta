import {useCookies} from 'react-cookie';
import {useNavigate, Link} from 'react-router-dom';
import {useEffect} from 'react';

function Home(){
    const [cookie, setCookies] = useCookies();
    const navigate = useNavigate();

    useEffect(() => {
        if(typeof cookie.user === 'undefined') navigate('/');
    });

    return (
        <div>
            <div>Welcome home!</div>
        </div>
    )
}

export default Home;
import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import NotFound from './components/NotFound';
import StartPage from './components/StartPage';
import Login from './components/Login';
import Reg from './components/Registration';


import InfoLesser from './components/Info';
import RegMotoroller from './components/RegMotoroller';
import Lesser from "./components/Lesser";
import Logout from "./components/Logout";


ReactDOM.render(
    <div>
        <BrowserRouter>
            <Switch>
                <Route exact path="/"
                       render={() => <StartPage/>}
                />
                <Route exact path="/login"
                       render={() => <Login/>}
                />
                <Route exact path="/reg"
                       render={() => <Reg/>}
                />
                <Route exact path="/info_lesser"
                       render={() => <InfoLesser/>}
                />
                <Route exact path="/regmoto"
                       render={() => <RegMotoroller/>}
                />}
                <Route exact path="/lesser"
                       render={() => <Lesser/>}
                />
                <Route exact path="/logout"
                       render={() => <Logout/>}
                />
                <Route component={NotFound}/>
            </Switch>
        </BrowserRouter>
    </div>,
    document.getElementById('root')
);
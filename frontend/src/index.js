import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import NotFound from './components/NotFound';
import StartPage from './components/StartPage';
import Login from './components/Login';
import Reg from './components/Registration';

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
                <Route component={NotFound}/>
            </Switch>
        </BrowserRouter>
    </div>,
    document.getElementById('root')
);
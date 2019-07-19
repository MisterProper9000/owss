import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import NotFound from './components/NotFound';
import Client from './components/Client'

ReactDOM.render(
    <div>
        <BrowserRouter>
            <Switch>
                <Route exact path="/"
                       render={() => <Client/>}
                />
                <Route component={NotFound}/>
            </Switch>
        </BrowserRouter>
    </div>,
    document.getElementById('root')
);
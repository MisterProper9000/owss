import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import NotFound from './components/NotFound';
import StartPage from './components/StartPage';
import Login from './components/Login';
import Reg from './components/Registration';


import InfoLesser from './components/Info';
import RegScooter from './components/RegScooter';
import Lesser from "./components/Lesser";
import Logout from "./components/Logout";
import DepositMoney from "./components/DepositMoney";
import PaymentInfo from "./components/PaymentInfo";
import ScooterInfo from "./components/ScooterInfo";

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
                       render={() => <RegScooter/>}
                />}
                <Route exact path="/lesser"
                       render={() => <Lesser/>}
                />
                <Route exact path="/logout"
                       render={() => <Logout/>}
                />
                <Route exact path="/deposit_money"
                       render={() => <DepositMoney/>}
                />
                <Route exact path="/payment_info"
                       render={() => <PaymentInfo/>}
                />
                <Route exact path="/scooter_info"
                       render={() => <ScooterInfo/>}
                />
                <Route component={NotFound}/>
            </Switch>
        </BrowserRouter>
    </div>,
    document.getElementById('root')
);
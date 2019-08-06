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

import Cookies from "js-cookie";
import Statistic from "./components/Statistic";


if (Cookies.get('token')) {
    ReactDOM.render(
        <div>
            <BrowserRouter>
                <Switch>
                    <Route exact path="/"
                           render={() => <StartPage/>}
                    />
                    <Route path="/info_lesser"
                           render={() => <InfoLesser/>}
                    />
                    <Route path="/regmoto"
                           render={() => <RegScooter/>}
                    />}
                    <Route path="/lesser"
                           render={() => <Lesser/>}
                    />
                    <Route path="/logout"
                           render={() => <Logout/>}
                    />
                    <Route path="/deposit_money"
                           render={() => <DepositMoney/>}
                    />
                    <Route path="/payment_info"
                           render={() => <PaymentInfo/>}
                    />
                    <Route path="/scooter_info"
                           render={() => <ScooterInfo/>}
                    />
                    <Route path="/stat"
                           render={() => <Statistic/>}
                    />
                    <Route component={NotFound}/>
                </Switch>
            </BrowserRouter>
        </div>
        ,
        document.getElementById('root')
    );
} else {
    ReactDOM.render(
        <div>
            <BrowserRouter>
                <Switch>
                    <Route exact path="/"
                           render={() => <StartPage/>}
                    />
                    <Route path="/login"
                           render={() => <Login/>}
                    />
                    <Route path="/reg"
                           render={() => <Reg/>}
                    />

                    <Route component={NotFound}/>
                </Switch>
            </BrowserRouter>
        </div>
        ,
        document.getElementById('root')
    );
}


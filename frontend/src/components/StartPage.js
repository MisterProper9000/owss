import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import scooter from "./scooter.png";
import '../css/StartPage.css';

class StartPage extends Component {
    render() {
        return (
            <div>
                <NavbarComp/>
                <h1 className="title">ScooShar</h1>
                <h2 className="title2">Бери и катайся!</h2>
                <img src={scooter} className="img_scooter"/>
                <img src={scooter} className="img_scooter"/>
            </div>
        );
    }
}

export default StartPage;
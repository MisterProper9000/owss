import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import '../css/StartPage.css';

class StartPage extends Component {
    render() {
        return (
            <div className="bg" >
                <NavbarComp/>
                <h2 className="title1">ScooShar</h2>
                <h1 className="title2">Fast. Easy. Comfortable.</h1>
            </div>
        );
    }
}

export default StartPage;
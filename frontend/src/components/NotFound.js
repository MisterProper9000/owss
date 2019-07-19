import React, {Component} from 'react';
import '../css/NotFound.css';
import notFoundLogo from "./notFound.png";

class NotFoundComponent extends Component {
    render() {
        return (
            <div className="notFound">
                <img src={notFoundLogo} className="notFoundLogo"/>
            </div>
        )
    }
}

export default NotFoundComponent;
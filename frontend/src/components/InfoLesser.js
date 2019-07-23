import React, {Component} from 'react';
import NavbarComp from "./NavbarComp"

class InfoLesser extends Component {

    constructor(props) {
        super(props);
        this.state = {

        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    render() {
        return(
            <div>
                <NavbarComp/>
            </div>
        );
    }
}

export default InfoLesser;
import React, {Component, useState} from 'react';
import '../css/RegScooter.css';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";

class RegScooter extends Component {

    constructor(props) {
        super(props);
        this.state = {
            auto_number: '',
            model: '',
            insurance: 'false',
            status: 'false',

        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    goBack(){
        window.location = "/lesser";
    }

    handleSubmit(event) {
        event.preventDefault();
        const {auto_number, model, insurance} = this.state;
        const id_owner = Cookies.get('token');

        if(this.state.auto_number === "" || this.state.model === "" || this.state.insurance === "")
        {
            this.setState({errorMsg: 'All fields must be filled'});
            return;
        }

        fetch('http://10.101.177.21:9091/addmoto', {
            method: 'POST',
            body: JSON.stringify({
                auto_number,
                model,
                id_owner,
                insurance
            })
        }).then((resp) => {
            return resp.json()
        }).then(response => {
            if (response === true) {
                this.setState({errorMsg: ''});
                alert("Your motoroller is successfully registered");
                window.location = "/lesser";

            } else {
                this.setState({errorMsg: 'Error with saving data'});
            }
        });
    }

    componentDidMount() {
    }

    render() {
        const {auto_number, model, insurance} = this.state;

        return (
            <div>
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <h1 className="titleDop">Add motorollers</h1>
                <form className="formScooter" onSubmit={this.handleSubmit}>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <div>
                        <select className="option" name="insurance" value={insurance} onChange={this.handleChange}>
                            <option value={true}>Insured</option>
                            <option value={false}>Order insurance</option>
                        </select>
                    </div>
                    <input className="input" type="text" placeholder="motoroller number" name="auto_number"
                           value={auto_number}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="model" name="model"
                           value={model}
                           onChange={this.handleChange}/><br/>
                    <input type="submit" name="buttonLogin" className="buttonSubmit"
                           value="Ok"/>
                </form>
            </div>
        );
    }
}

export default RegScooter;
import React, {Component, useState} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";

import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";

class RegMotoroller extends Component {

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

    handleSubmit(event) {
        event.preventDefault();
        const {auto_number, model, insurance,status} = this.state;
        const id_owner = Cookies.get('token');

        fetch(JuliaLocalIpOW + '/addmoto', {
            method: 'POST',
            body: JSON.stringify({
                auto_number,
                model,
                id_owner,
                insurance,
                status
            })
        }).then((resp) => {
            return resp.json()
        }).then(response => {
            if (response === true) {
                this.setState({errorMsg: ''});
                alert("Your motoroller is successfully registered");
                window.location = "/";

            } else {
                this.setState({errorMsg: 'Error with saving data'});
            }
        });
    }

    componentDidMount() {
    }

    render() {
        const {auto_number, model, id_owner, insurance} = this.state;

        return (
            <div>
                <NavbarComp/>
                <form className="formLogin" onSubmit={this.handleSubmit}>
                    <h1 className="title">Add motorollers</h1>
                    <div>
                        <select className="option" name="insurance" value={insurance} onChange={this.handleChange}>
                            <option value={true}>Insured</option>
                            <option value={false}>Order insurance</option>
                        </select>
                    </div>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <input className="input" type="text" placeholder="auto_number" name="auto_number"
                           value={auto_number}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="model" name="model"
                           value={model}
                           onChange={this.handleChange}/><br/>
                    <br/>


                    <input type="submit" name="buttonLogin" className="input btn btn-outline-danger"
                           value="Ok"/>
                </form>
            </div>
        );
    }
}

export default RegMotoroller;
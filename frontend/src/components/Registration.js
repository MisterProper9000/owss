import React, {Component, useState} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";

class Registration extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            first_name: '',
            last_name: '',
            type: "Personal owner",
            email: '',
            address: '',
            phone: '',
            bank_account: '',
            password: '',
            errorMsg: '',
            data: [],
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const {
            first_name, last_name, type, email, phone, address,
            bank_account, password
        } = this.state;

        fetch('http://10.101.177.21:9091/reg', {
            method: 'POST',
            body: JSON.stringify({
                first_name,
                last_name,
                type,
                email,
                password,
                phone,
                address,
                bank_account
            })
        }).then((resp) => {
            return resp.json()
        }).then(response => {
            if (response === true) {
                this.setState({errorMsg: 'You are registered'});
                alert("You are successfully registered");
                window.location = "/login";
            } else {
                this.setState({errorMsg: 'Enter correct data!'});
            }
        })
    }

    componentDidMount() {
    }

    render() {
        const {first_name, last_name, type, email, phone, address, bank_account, password} = this.state;

        return (
            <div>
                <NavbarComp/>
                <form className="formLogin" onSubmit={this.handleSubmit}>
                    <h1 className="title">Sign up</h1>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <div>
                        <select className="option" name="type" value={type} onChange={this.handleChange}>
                            <option value="Personal Owner">Personal Owner</option>
                            <option value="Organization">Organization</option>
                        </select>
                    </div>
                    <input className="input" type="text" placeholder="first name" name="first_name"
                           value={first_name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="last name" name="last_name"
                           value={last_name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="bank account" name="bank_account"
                           value={bank_account}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="email" name="email"
                           value={email}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="phone" name="phone"
                           value={phone}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="address" name="address"
                           value={address}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="password" placeholder="password" name="password"
                           value={password}
                           onChange={this.handleChange}/><br/>

                    <input type="submit" name="buttonLogin" className="input btn btn-outline-danger"
                           value="Ok"/>
                </form>
            </div>
        );
    }
}

export default Registration;
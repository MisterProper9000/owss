import React, {Component} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";


import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";


class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            email: '',
            password: '',
            data: [],
            errorMsg: '',
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.goToSignUp = this.goToSignUp.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const {email, password} = this.state;

        if (this.state.email === "" || this.state.password === "") {
            this.setState({errorMsg: 'All fields must be filled'});
            return;
        }
        if (!this.state.email.includes("@") || !this.state.email.includes(".")) {
            this.setState({errorMsg: 'Wrong email'});
            return;
        }

        fetch(JuliaLocalIpOW + '/login', {
        //fetch('http://10.101.177.21:9091/login', {
            method: 'POST',
            body: JSON.stringify({
                    email,
                    password
                }
            )
        }).then((resp) => {
            return resp.json()
        }).then(response => {
            if (response != false) {
                Cookies.set('token', response);
                this.setState({errorMsg: ''});
                alert("You are successfully logged in");
                window.location = "/lesser";
            } else {
                this.setState({errorMsg: 'Error with login or password'});
            }
        });
    }

    goToSignUp() {
        window.location = "/reg";
    }

    componentDidMount() {
    }

    render() {
        const {email, password} = this.state;

        return (
            <div>
                <NavbarComp/>
                <div className="row">
                    <div className="column">
                        <div className="bglogo"></div>
                    </div>

                    <div className="column">
                        <form className="formLogin" onSubmit={this.handleSubmit}>
                            <h1 className="title">Account Login</h1>
                            <div className="errorMsg">{this.state.errorMsg}</div>
                            <input className="input" type="text" placeholder="email" name="email"
                                   value={email}
                                   onChange={this.handleChange}/><br/>
                            <input className="input" type="password" placeholder="password" name="password"
                                   value={password}
                                   onChange={this.handleChange}/><br/>
                            <input type="submit" name="buttonLogin" className="buttonSubmit"
                                   value="Ok"/>
                        </form>
                        <div className="titleWords">Don't have an account</div>
                        <button className="input btn-info" className="titleSignUp" onClick={this.goToSignUp}>SIGN UP
                            NOW
                        </button>
                    </div>
                </div>
            </div>
        );
    }
}

export default Login;
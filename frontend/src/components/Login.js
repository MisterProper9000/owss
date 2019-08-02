import React, {Component} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";


class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            email: '',
            password: '',
            data: [],
            errorMsg: '',
            way4: '',
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const {email, password} = this.state;

        fetch('http://10.101.177.21:9091/login', {
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
                Cookies.set('token',response);
                this.setState({errorMsg: ''});
                alert("You are successfully logged in");
                window.location = "/lesser";
            } else {
                this.setState({errorMsg: 'Error with login or password'});
            }
        });
    }

    componentDidMount() {
    }

    render() {
        const {email, password} = this.state;

        return (
            <div>
                <NavbarComp/>
                <form className="formLogin" onSubmit={this.handleSubmit}>
                    <h1 className="title">Sign in</h1>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <input className="input" type="text" placeholder="email" name="email"
                           value={email}
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

export default Login;
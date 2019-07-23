import React, {Component} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            email: '',
            password: '',
            data: [],
            errorMsg: ''
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
        }).then(response => response.json()).then(response => {
            if (response == true) {
                this.setState({errorMsg: ''});
                window.location = "/info_lesser"
            } else {
                this.setState({errorMsg: 'Wrong login or password!'});
            }
        })
    }

    componentDidMount() {
    }

    render() {
        const {email, password} = this.state;

        return (
            <div>
                <NavbarComp/>
                <form className="formLogin" onSubmit={this.handleSubmit}>
                    <h1>Sign in</h1>
                    <input className="input" type="text" placeholder="email" name="email"
                           value={email}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="password" placeholder="password" name="password"
                           value={password}
                           onChange={this.handleChange}/><br/>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <input type="submit" name="buttonLogin" className="input btn btn-secondary"
                           value="Ok"/>
                </form>
            </div>
        );
    }
}

export default Login;
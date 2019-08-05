import React, {Component} from 'react';
import Cookies from "js-cookie";
import {Redirect} from "react-router-dom"
class Login extends Component {

    constructor(props) {
        super(props);
        Cookies.remove('token');
    }

    render() {
        return (<Redirect to="/"/>);
    }
}

export default Login;
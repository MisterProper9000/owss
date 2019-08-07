import React, {Component} from 'react';
import '../css/DepositMoney.css';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";

import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";
import Cookies from "js-cookie";

class DepositMoney extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: Cookies.get('token'),
            card_number: '',
            security_code: '',
            name_on_card: '',
            expiration: '',
            depositMoney: '',
            errorMsg: '',
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();

        const {card_number, security_code, expiration, depositMoney, id} = this.state;

        if (this.state.card_number === "" || this.state.security_code === "" || this.state.expiration === "" || this.state.depositMoney === "") {
            this.setState({errorMsg: 'All fields must be filled'});
            return;
        }

        fetch(JuliaLocalIpOW + '/depositmoney', {
            //fetch('http://10.101.177.21:9091/depositmoney', {

            method: 'POST',
            body: JSON.stringify({
                    card_number,
                    security_code,
                    expiration,
                    depositMoney,
                    id
                }
            )
        })
            .then(resp => {
                return resp.json();

                // if (resp) {
                //     //alert("You have successfully replenished your wallet in the amount of " + depositMoney)
                // } else {
                //     this.setState({errorMsg: 'Replenishment error'});
                // }
            })
            .then(resp =>{
                console.log(resp);
            })
    }

    goBack() {
        window.location = "/lesser";
    }

    componentDidMount() {
    }

    render() {
        const {card_number, security_code, expiration, depositMoney} = this.state;

        return (
            <div>
                <NavbarComp/>


                <form className="formDeposit" onSubmit={this.handleSubmit}>
                    <h1 className="title">Deposit Money</h1>
                    <input className="input" type="text" placeholder="card number" name="card_number"
                           value={card_number}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="security code" name="security_code"
                           value={security_code}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="expiration" name="expiration"
                           value={expiration}
                           onChange={this.handleChange}/><br/>
                    <br/>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <input className="input" type="text" placeholder="Cost" name="depositMoney"
                           value={depositMoney}
                           onChange={this.handleChange}/><br/>
                    <br/>
                    <input type="submit" name="buttonSubmitDeposit" className="buttonSubmitDeposit"
                           value="Submit"/>
                </form>
            </div>
        );
    }
}

export default DepositMoney;
import React, {Component} from 'react';
import '../css/DepositMoney.css';
import NavbarComp from "./NavbarComp";

import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";

class DepositMoney extends Component {

    constructor(props) {
        super(props);
        this.state = {
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
        const {card_number, security_code, name_on_card, expiration, depositMoney} = this.state;

        fetch(JuliaLocalIpOW + '/depositmoney', {
            //fetch('http://10.101.177.21:9091/depositmoney', {

            method: 'POST',
            body: JSON.stringify({
                    card_number,
                    security_code,
                    expiration,
                    depositMoney
                }
            )
        })
            .then(resp =>{
                if(resp){
                    alert("You have successfully replenished your wallet in the amount of "+depositMoney)
                }
                else {
                    this.setState({errorMsg: 'Replenishment error'});
                }
            });
    }

    componentDidMount() {
    }

    goBack() {
        window.location = "/lesser";
    }

    render() {
        const {card_number, security_code, expiration, depositMoney} = this.state;

        return (
            <div>
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <form className="formDeposit" onSubmit={this.handleSubmit}>
                    <h1 className="title">Deposit Money</h1>
                    <input className="input" type="text" placeholder="card_number" name="Card Number"
                           value={card_number}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="security_code" name="Security Code"
                           value={security_code}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="expiration" name="Expiration"
                           value={expiration}
                           onChange={this.handleChange}/><br/>
                    <br/>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <input className="input" type="text" placeholder="Cost" name="depositMoney"
                           value={depositMoney}
                           onChange={this.handleChange}/><br/>
                    <input type="submit" name="buttonLogin" className="buttonSubmit"
                           value="Submit"/>
                </form>
            </div>
        );
    }
}

export default DepositMoney;
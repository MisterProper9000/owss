import React, {Component, useState} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";

import {Modal, Button} from "react-bootstrap";
import DialogWindow from "./Dialog window";


function goToInfo(props) {
    const isLoggedIn = props;
    if (isLoggedIn) {
        console.log("test : ok");
        return <DialogWindow/>;
    } else {
        console.log("test : error");
        return <DialogWindow/>;
    }
}

class Registration extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            first_name: '',
            last_name: '',
            company_name: '',
            type: "Individuals",
            email: '',
            address: '',
            phone: '',
            sum_moto: '',
            bank_account: '',
            password: '',
            errorMsg: '',
            data: [],
            isRegOk: false
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        //this.handleClose = this.handleClose.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const {
            first_name, last_name, company_name, type, email, phone, address,
            sum_moto, bank_account, password
        } = this.state;

        fetch('http://10.101.177.21:9091/reg', {
            method: 'POST',
            body: JSON.stringify({
                first_name,
                last_name,
                company_name,
                type,
                email,
                password,
                phone,
                address,
                bank_account,
                sum_moto
            })
        }).then((resp) => {
            return resp.json()
        }).then(response => {
            //console.log(response + "- response");
            if (response === true) {
                this.setState({errorMsg: 'You are registered'});
                this.setState({isRegOk: true});
                //console.log("isRegOk: " + this.state.isRegOk);
                //this.setShow(true);

            } else {
                this.setState({errorMsg: 'Enter correct data!'});
                //console.log("isRegOk: " + this.state.isRegOk);
                //setShow(true);
            }

        })
    }

    componentDidMount() {
    }

    // handleClose(props) {
    //     setShow(props);
    //     window.location = "/info_lesser";
    // }

    render() {
        const {first_name, last_name, company_name, type, email, phone, address, sum_moto, bank_account, password} = this.state;

        return (
            <div>
                {/*<Modal show={show} onHide={this.handleClose}>*/}
                {/*    <Modal.Header closeButton>*/}
                {/*        <Modal.Title>Rigistration</Modal.Title>*/}
                {/*    </Modal.Header>*/}
                {/*    <Modal.Body>You have been successfully registered!<br/> Go to your account.</Modal.Body>*/}
                {/*    <Modal.Footer>*/}
                {/*        <Button variant="secondary" onClick={this.handleClose}>*/}
                {/*            Ok*/}
                {/*        </Button>*/}
                {/*    </Modal.Footer>*/}
                {/*</Modal>*/}
                <NavbarComp/>
                <form className="formLogin" onSubmit={this.handleSubmit}>
                    <h1 className="title">Sign up</h1>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <div>
                        <select className="option" name="type" value={type} onChange={this.handleChange}>
                            <option value="Individuals">Individuals</option>
                            <option value="Entities">Entities</option>
                        </select>
                    </div>
                    <input className="input" type="text" placeholder="first name" name="first_name"
                           value={first_name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="last name" name="last_name"
                           value={last_name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="company name" name="company_name"
                           value={company_name}
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
                    <input className="input" type="text" placeholder="sum_moto" name="sum_moto"
                           value={sum_moto}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="password" placeholder="password" name="password"
                           value={password}
                           onChange={this.handleChange}/><br/>

                    <input type="submit" name="buttonLogin" className="input btn btn-outline-danger"
                           value="Ok"/>
                    {/*<Button variant="primary" onClick={this.handleSubmit}>*/}
                    {/*    Ok*/}
                    {/*</Button>*/}
                </form>
            </div>
        );
    }
}

export default Registration;
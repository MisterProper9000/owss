import React, {Component} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";


import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";


class Statistic extends Component {

    constructor(props) {
        super(props);
        this.state = {
            startStatDate: '',
            endStatDate: '',
            id: Cookies.get('token'),
            errorMsg: '',
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    componentDidMount() {
    }

    goBack() {
        window.location = "/lesser";
    }

    handleSubmit(event) {
        event.preventDefault();
        const {startStatDate, endStatDate, id} = this.state;

        if (startStatDate === "" || endStatDate === "") {
            this.setState({errorMsg: 'All fields must be filled'});
            return;
        }

        fetch(JuliaLocalIpOW + '/findStat', {
            //fetch('http://10.101.177.21:9091/login', {
            method: 'POST',
            body: JSON.stringify({
                startStatDate,
                endStatDate,
                id
            })
        }).then((resp) => {
            return resp.json()
        }).then(response => {
            console.log(response)
        });
    }

    render() {
        const {startStatDate, endStatDate} = this.state;
        return (
            <div>
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <div className="mainMargin">
                    <h1 className="titleDop">Statistics</h1>
                    <form onSubmit={this.handleSubmit}>
                        <h3>Choose period of statistic</h3>
                        <div className="errorMsg">{this.state.errorMsg}</div>
                        <input className="input" type="date" placeholder="From" name="startStatDate"
                               value={startStatDate}
                               onChange={this.handleChange}/><br/>
                        <input className="input" type="date" placeholder="To" name="endStatDate"
                               value={endStatDate}
                               onChange={this.handleChange}/><br/>
                        <input type="submit" name="buttonLogin" className="buttonSubmit"
                               value="Ok"/>
                    </form>
                </div>

            </div>
        );
    }
}

export default Statistic;
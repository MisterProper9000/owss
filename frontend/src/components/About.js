import React, {Component} from 'react';
import '../css/About.css';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";


import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";


class About extends Component {

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
                        <div className="all">
                            <div className="title">
                                <div className="what">What is ScooShar?</div>
                                <div className="about">ScooShar is the new modern scooter sharing in the South Asia that
                                    provides you large opportunities for travelling and doing your business from your
                                    home!
                                </div>
                            </div>

                            <div className="blockbusiness">
                                <div className="forbusiness">
                                    ScooShar for business
                                </div>
                                <div className="business">
                                    If you have a scooters and you want them to make a profit — rent them out using our
                                    platform.
                                </div>
                                <div className="money">
                                    Service provides you platform with information about your scooters, money
                                    transactions, usage staticstics and Ability to buy insurance.
                                </div>
                            </div>

                            <div className="blocktravel">
                                <div className="fortravel">
                                    ScooShar for travel
                                </div>
                                <div className="travel">If you are a tourist you could rent a scooter for your trip with
                                    a low price. Enjoy the city, nature, pay for comfortable tariff and do not think
                                    about insurance!
                                </div>
                                <div className="mobile">You can download mobile application on your phone for any mobile
                                    platform. Currently application supports 5 languages: English, Russian, Spanish,
                                    German and Vietnamese!
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default About;
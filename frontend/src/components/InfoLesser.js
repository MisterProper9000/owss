import React, {Component} from 'react';
import NavbarComp from "./NavbarComp"
import '../css/InfoLesser.css';
import { Button, Form } from 'react-bootstrap';


class InfoLesser extends Component {

    constructor(props) {
        super(props);
        this.state = {
            first_name: '',
            last_name: '',
            email: '',
            sum_moto: '',

        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const {first_name, last_name, company_name, type, email, phone, address, sum_moto, bank_account, password} = this.state;

        fetch('http://10.101.177.21:9091/', {
            method: 'POST',
            body: JSON.stringify({
                first_name,
                last_name,
                email,
                sum_moto
            })
        }).then(response => response.json()).then(response => {
            if (response == true) {
                this.setState({errorMsg: ''});
                window.location = "/login"
            } else {
                this.setState({errorMsg: 'Enter correct data!'});
            }
        })
    }


render() {
        return(
            <div>
                <NavbarComp/>
                <div id = "main"  align={"center"} vertical-align = "center">
                    <div>
                        <p><b>Name: </b>
                        <input readOnly type = "text"
                               value = {"Ivan"}/>
                        </p>
                    </div>
                    <div>
                        <p><b>Surname:  </b>
                            <input readOnly type = "text"
                                   value = {"Ivanov"}/>
                        </p>
                    </div>
                    <div>
                        <p><b>email:  </b>
                            <input readOnly type = "text"
                                   value = {"IvanovIvan@email.com"}/>
                        </p>
                    </div>
                    <div>
                        <p><b>Number of moto  </b>
                            <input readOnly type = "text"
                                   value = {"123"}/>
                        </p>
                    </div>

                </div>
            </div>

        );
    }
}

export default InfoLesser;
import React, {Component} from 'react';
import NavbarComp from "./NavbarComp"
import '../css/Login.css';


class InfoLesser extends Component {

    constructor(props) {
        super(props);
        this.state = {

        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});

        fetch('http://10.101.177.12:9091/reg', {
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
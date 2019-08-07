import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";
import './../css/Lesser.css'

import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-balham.css';


import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";

class Lesser extends Component {
    goScooterInfo;

    constructor(props) {
        super(props);
        this.state = {
            id_client: Cookies.get('token'),
            type: '',
            first_name: '',
            last_name: '',
            email: '',
            password: '',
            phone: '',
            address: '',
            sum_moto: '',
            bank_account: '',

            errorMsg: '',

            selected_idd: 0,


            lesserData: [],

            id: '',
            auto_number: '',
            model: '',
            insurance: '',
            status_reserv: '',
            status_rent: '',

            listid: [],


            balance: '',

            columnDefs: [
                {headerName: "ID", field: "id", width: 70},
                {headerName: "Scooter", field: "auto_number",width: 100},
                {headerName: "Model", field: "model",width: 130},
                {headerName: "Insuranse", field: "insurance",width: 100},
                {headerName: "Reserved", field: "status_reserv",width: 100},
                {headerName: "In rent", field: "status_rent",width: 80}

            ],
            rowData: [],
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({selected_id: event});
    }

    depositMoney() {
        window.location = "/deposit_money";
    }

    addNewScooter() {
        window.location = "/regmoto";
    }

    getScooterInfo(selected_id) {
        //this.setState({selected_idd: selected_id});
        Cookies.set('id_scooter',selected_id);
        console.log(selected_id);

        fetch(JuliaLocalIpOW + '/goToScooterInfo', {
            method: 'POST',
            body: Cookies.get('id_scooter')
        })
            .then(resp => {
                return resp.json()
            })
            .then(resp => {
                //console.log(resp);
                if (resp != true) {
                    this.setState({errorMsg: 'Enter an existing scooter'});
                } else {
                    window.location = "/scooter_info";
                }

            })

    }


    goToPaymentInfo() {
        window.location = "/payment_info"
    }


    componentDidMount() {
        fetch(JuliaLocalIpOW + '/goToScooterInfo', {
            method: 'POST',
            body: this.state.id_client
        })

        fetch(JuliaLocalIpOW + '/lesserinfo', {
            method: 'POST',
            body: this.state.id_client
        }).then(resp => {
            return resp.json()
        })
            .then(resp => this.setState(
                {
                    first_name: resp.first_name,
                    last_name: resp.last_name,
                    type: resp.type,
                    email: resp.email,
                    phone: resp.phone,
                    company_name: resp.company_name,
                    address: resp.address,
                    sum_moto: resp.sum_moto,
                    bank_account: resp.bank_account,
                }));

        fetch(JuliaLocalIpOW + '/balanceInqueryLessor', {
            //fetch('http://10.101.177.21:9091/balanceInqueryLessor', {
            method: 'POST',
            body: JSON.stringify({
                    id: Cookies.get('token')
                }
            )
        }).then((resp) => {
            return resp.json()
        }).then(resp => {
            //console.log("kek")
            this.setState({balance: " " + resp.toString()});
        });



        fetch(AndreyLocalIpOW + '/infomoto', {
            method: 'POST',
            body: this.state.id_client
        })
            .then(result => result.json())
            .then(rowData => {
                // var stringified = JSON.stringify(rowData);
                // stringified = stringified.replace('"insurance": "true"', '"insurance": "yes"');
                // stringified = stringified.replace('"insurance": "false"', '"insurance": "no"');
                // stringified = stringified.replace('"status_reserv": "true"', '"status_reserv": "yes"');
                // stringified = stringified.replace('"status_reserv": "false"', '"status_reserv": "no"');
                // stringified = stringified.replace('"status_rent": "false"', '"status_rent": "no"');
                // stringified = stringified.replace('"status_rent": "true"', '"status_rent": "yes"');
                // var jsonObject = JSON.parse(stringified);
                this.setState({rowData});
            })
    }

    render() {
        const {selected_id} = this.state;
        return (
            <div>
                <NavbarComp/>
                <div className="pagePersonalAccount">
                    <h1 className="titleDop">Personal account: {this.state.first_name} {this.state.last_name}</h1>
                    <div className="rowLesser">
                        <div className="columnLesser">
                            <table className="tableLesser">
                                <tr>
                                    <th>Type:</th>
                                    <td>{this.state.type}</td>
                                </tr>
                                <tr>
                                    <th>First Name:</th>
                                    <td>{this.state.first_name}</td>
                                </tr>
                                <tr>
                                    <th>Last Name</th>
                                    <td>{this.state.last_name}</td>
                                </tr>
                                <tr>
                                    <th>Bank account</th>
                                    <td>{this.state.bank_account}</td>
                                </tr>
                                <tr>
                                    <th>Email</th>
                                    <td>{this.state.email}</td>
                                </tr>
                                <tr>
                                    <th>Phone</th>
                                    <td>{this.state.phone}</td>
                                </tr>
                                <tr>
                                    <th>Address</th>
                                    <td>{this.state.address}</td>
                                </tr>
                                <tr>
                                    <th>Number of scooters</th>
                                    <td>{this.state.sum_moto}</td>
                                </tr>
                            </table>

                            <div className="balance">
                                <tr>
                                    <th>Your balance  </th>
                                    <td>{this.state.balance}</td>
                                </tr>
                                <button className="buttonDeposit" onClick={this.depositMoney}>Deposit money</button>
                            </div>
                            {/*<button className="buttonPaymentInfo" onClick={this.goToPaymentInfo}>Payment Information*/}
                            {/*</button>*/}
                        </div>

                        <div className="columnLesser">
                            <div>Scooters</div>
                            <div
                                className="ag-theme-balham"
                                style={{height: '200px', width: '600px'}}
                            >
                                <AgGridReact
                                    //pagination={true}
                                    enableFilter={true}
                                    enableSorting={true}
                                    columnDefs={this.state.columnDefs}
                                    rowData={this.state.rowData}>
                                </AgGridReact>
                            </div>
                            <button className="buttonNewScooter" onClick={this.addNewScooter}>Add new scooter
                            </button>
                            <br/>
                            <br/>


                            <div className="rental">
                                <tr>
                                    <th width="100%">Rental history</th>
                                    <td><text>Enter id scooter</text>
                                        <div className="errorMsg">{this.state.errorMsg}</div>
                                        <input id="id_selected_input" type="text" placeholder="" name="selected_id"/><br/>
                                    </td>

                                </tr>
                                <button className="buttonshowinfo"
                                        onClick={() => this.getScooterInfo(document.getElementById("id_selected_input").value)}>Show
                                    info
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Lesser;
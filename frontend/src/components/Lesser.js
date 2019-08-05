import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";
import './../css/Lesser.css'

import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-balham.css';


class Lesser extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id_client: Cookies.get('token'),
            type: 'Personal Owner',
            first_name: 'Jools',
            last_name: 'Blinnikova',
            email: 'kate@gmail.com',
            password: '',
            phone: '89811234567',
            address: 'Torzhkovskaya 15',
            sum_moto: '1',
            bank_account: '1234567',

            lesserData: [],

            id: '',
            auto_number: '',
            model: '',
            insurance: '',

            balance: '',

            columnDefs: [
                {headerName: "ID", field: "id", width: 100},
                {headerName: "Scooter number", field: "auto_number"},
                {headerName: "Model", field: "model"},
                {headerName: "Insuranse", field: "insurance"}

            ],
            rowData: [],
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    depositMoney() {
        window.location = "/deposit_money";
    }

    addNewScooter() {
        window.location = "/regmoto";
    }

    getScooterInfo() {
        window.location = "/scooter_info";
    }

    goToPaymentInfo() {
        window.location = "/payment_info"
    }


    componentDidMount() {

        fetch('http://10.101.177.21:9091/lesserinfo', {
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
                }))


        fetch('http://10.101.177.21:9091/balanceInqueryLessor', {
            method: 'POST',
            body: JSON.stringify({
                    email: 'test2@gmail.com',
                }
            )
        }).then((resp) => {
            return resp.json()
        }).then(function (jsonData) {
            return JSON.stringify(jsonData);
        }).then(resp => {
            var bal = resp.split('"balance":');
            this.setState({balance: bal});
        })


        fetch('http://10.101.177.21:9091//infomoto', {
            method: 'POST',
            body: this.state.id_client
        })
            .then(result => result.json())
            .then(rowData => this.setState({rowData}))
    }

    render() {
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
                            <br/>
                            <br/>
                            <div className="balance">
                                <tr>
                                    <th>Your balance</th>
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
                            {/*<button className="buttonInfoScooter" onClick={this.getScooterInfo}>Scooter Info*/}
                            {/*</button>*/}

                            {/*<div className="rowLesser">*/}
                            {/*    <div className="columnLesser">*/}
                            {/*        <button className="buttonNewScooter" onClick={this.addNewScooter}>Add new scooter*/}
                            {/*        </button>*/}
                            {/*    </div>*/}
                            {/*    <div className="columnLesser">*/}
                            {/*        <button className="buttonInfoScooter" onClick={this.getScooterInfo}>Scooter Info*/}
                            {/*        </button>*/}
                            {/*    </div>*/}
                            {/*</div>*/}

                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Lesser;
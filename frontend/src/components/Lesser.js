import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";
import './../css/Lesser.css'

import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-balham.css';


import {AndreyLocalIpOW} from "./ipConfigs";

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


            selected_id: 0,


            lesserData: [],

            id: '',
            auto_number: '',
            model: '',
            insurance: '',
            status_reserv:'',
            status_rent:'',

            listid:[],


            balance: '',

            columnDefs: [
                {headerName: "ID", field: "id", width: 100},
                {headerName: "Scooter number", field: "auto_number"},
                {headerName: "Model", field: "model"},
                {headerName: "Insuranse", field: "insurance"},
                {headerName: "Reserved", field: "status_reserv"},
                {headerName: "In rent", field: "status_rent"}

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
        Cookies.remove('id_scooter');
        this.setState({selected_id:selected_id});
        Cookies.set('id_scooter',this.state.selected_id);
        console.log(selected_id);

        fetch(AndreyLocalIpOW + '/goToScooterInfo', {
            method: 'POST',
            body: Cookies.get('id_scooter')
        })
            .then(resp => {
            //return resp.json()
                console.log(resp.json());
        })
        //window.location = "/scooter_info";
    }

    goToPaymentInfo() {
        window.location = "/payment_info"
    }


    componentDidMount() {

        fetch(AndreyLocalIpOW + '/goToScooterInfo', {
            method: 'POST',
            body: this.state.id_client
        })


        fetch(AndreyLocalIpOW + '/lesserinfo', {
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

        fetch(AndreyLocalIpOW + '/balanceInqueryLessor', {
        //fetch('http://10.101.177.21:9091/balanceInqueryLessor', {
            method: 'POST',
            body: JSON.stringify({
                    email: 'a@mail.com',
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


        fetch('http://10.101.177.21:9091/infomoto', {
            method: 'POST',
            body: this.state.id_client
        })
            .then(result => result.json())
            .then(rowData => {
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
                            <br/>

                            <div className="selectScooter">
                                <tr>
                                    <th>Scooter information</th>
                                    <text className="selectId">Enter id scooter</text>
                                    <input id="id_selected_input" type="text" placeholder="" name="selected_id"/><br/>
                                </tr>
                                <button className="buttonDeposit" onClick={()=>this.getScooterInfo(document.getElementById("id_selected_input").value)}>Find</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Lesser;
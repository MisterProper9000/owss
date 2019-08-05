import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";
import './../css/PaymentInfo.css'

import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-balham.css';


class PaymentInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {

            columnDefs: [
                {headerName: "Time", field: "time", width: 100},
                {headerName: "Name", field: "name"},
                {headerName: "Value", field: "value"},

            ],
            rowData: [],
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    goBack() {
        window.location = "/lesser";
    }

    componentDidMount() {
        // fetch('http://10.101.177.21:9091/paymentinfo')
        //     .then(result => result.json())
        //     .then(rowData => this.setState({rowData}))
    }

    render() {
        return (
            <div>
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <div className="payment">
                    <h1 className="title">Payment Information</h1>

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
                </div>
            </div>
        );
    }
}

export default PaymentInfo;
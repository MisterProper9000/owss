import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";
import './../css/ScooterInfo.css'


import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-balham.css';


class ScooterInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {

            id_moto: '',
            auto_number: '',
            model: '',
            insurance: '',

            id: '',
            begin_time: '',
            end_time: '',
            cost: '',
            tariff: '',


            columnDefs: [
                {headerName: "ID", field: "id_moto", width: 100},
                {headerName: "Moto number", field: "auto_number"},
                {headerName: "Model", field: "model"},
                {headerName: "Insuranse", field: "insurance"}

            ],
            rowData: [],

            columnRent: [
                {headerName: "ID", field: "id", width: 100},
                {headerName: "Begin time", field: "begin_time"},
                {headerName: "End time", field: "end_time"},
                {headerName: "Cost", field: "cost"},
                {headerName: "Tariff", field: "tariff"},

            ],
            rowRent: [],
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
        // fetch('http://10.101.177.21:9091/motoinfo')
        //     .then(result => result.json())
        //     .then(rowData => this.setState({rowData}))

        // fetch('http://10.101.177.21:9091/rentinfo')
        //     .then(result => result.json())
        //     .then(rowRent => this.setState({rowRent}))
    }

    render() {
        return (
            <div>
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <h1 className="titleDop">Scooter Information</h1>
                <div className="rowScooter">
                    <div className="columnScooter">
                        <div className="tableMoto">
                            <div
                                className="ag-theme-balham"
                                style={{height: '100px', width: '600px'}}
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
                        <div className="columnScooter">

                        </div>
                    </div>

                    <div className="columnScooter">

                    </div>
                </div>

                <br/>
                <div className="tableMoto">
                    <div>Rents</div>

                    <div
                        className="ag-theme-balham"
                        style={{height: '200px', width: '1000px'}}
                    >
                        <AgGridReact
                            //pagination={true}
                            enableFilter={true}
                            enableSorting={true}
                            columnDefs={this.state.columnRent}
                            rowData={this.state.rowRent}>
                        </AgGridReact>
                    </div>
                </div>

            </div>
        );
    }
}

export default ScooterInfo;
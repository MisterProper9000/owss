import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";
import './../css/ScooterInfo.css'


import {AgGridReact} from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-balham.css';
import {JuliaLocalIpOW} from "./ipConfigs";

class ScooterInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {


            id: '',
            begin_time: '',
            end_time: '',
            cost: '',
            tariff_time: '',


            balance: '',

            columnRent: [
                {headerName: "ID rent", field: "id", width: 100},
                {headerName: "begin_time", field: "begin_time"},
                {headerName: "end_time", field: "end_time"},
                {headerName: "cost", field: "cost"},
                {headerName: "tariff_time", field: "tariff_time"}

            ],
            rowRent: [],

            // columnDefs: [
            //     {headerName: "ID", field: "id", width: 100},
            //     {headerName: "Scooter number", field: "auto_number"},
            //     {headerName: "Model", field: "model"},
            //     {headerName: "Insuranse", field: "insurance"},
            //     {headerName: "Reserved", field: "status_reserv"},
            //     {headerName: "In rent", field: "status_rent"}
            //
            // ],
            // rowData: [],

            // //idowner: Cookies.get('token'),
            // id: '',
            // auto_number: '',
            // model: '',
            // insurance: '',
            // status_reserv: '',
            // status_rent: '',
            // idowner:'',
            // rent:'',
            // res:'',
            //
            // // id: '1',
            // // begin_time: '',
            // // end_time: '',
            // // cost: '',
            // // tariff: '',
            // //
            // // data: [],
            //
            // // columnRent: [
            // //     {headerName: "ID", field: "id", width: 100},
            // //     {headerName: "Begin time", field: "begin_time"},
            // //     {headerName: "End time", field: "end_time"},
            // //     {headerName: "Cost", field: "cost"},
            // //     {headerName: "Tariff", field: "tariff"},
            // //
            // // ],
            // // rowRent: [],
            //
            // columnData: [
            //     {headerName: "ID", field: "id", width: 100},
            //     {headerName: "auto_number", field: "auto_number"},
            //     {headerName: " model", field: "model"},
            //     {headerName: "insurance", field: "insurance"},
            //     {headerName: "status_reserv", field: "status_reserv"},
            //     {headerName: "status_rent", field: "status_rent"},
            //
            // ],
            // rowData: [],
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
        fetch(JuliaLocalIpOW+'/gotorentforscooter', {
            method: 'POST',
            body: Cookies.get('id_scooter')
        })
            .then(result => result.json())
            .then(rowRent => {
                //console.log(rowData)
                this.setState({rowRent});
            })
    }

    render() {
        return (
            <div>
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <div className="mainMargin">
                    <h1 className="titleDop">Scooter Information</h1>
                    <div
                        className="ag-theme-balham"
                        style={{height: '300px', width: '100%'}}
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
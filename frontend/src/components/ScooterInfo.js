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

            idowner: Cookies.get('token'),
            id_moto: '1',
            auto_number: '',
            model: '',
            insurance: '',

            id: '',
            begin_time: '',
            end_time: '',
            cost: '',
            tariff: '',


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
        // fetch('http://10.101.177.21:9091/listScooterId')
        //     .then((resp) => {
        //         return resp.json()
        //     })
        //     .then(data => {
        //         let idFromDB = data.map(id => {
        //             return {value: id, display: id}
        //         });
        //         this.setState({listId: [{value: 'all', display: 'Select id'}].concat(idFromDB)});
        //     }).catch(error => {
        //     console.log(error);
        // });


        // fetch('http://10.101.177.21:9091//rentmoto', {
        //     method: 'POST',
        //     body: this.state.id
        // })
        //     .then(result => result.json())
        //     .then(rowRent => this.setState({rowRent}))
    }

    render() {
        const {id} = this.state;
        return (
            <div className="tableInfoScooter">
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <h1 className="titleDop">Scooter Information</h1>
                {/*<text className="selectId">Select id</text>*/}

                {/*<select name="id" className="form-control dark" value={id} onChange={this.handleChange}*/}
                {/*        onClick={this.addDataToTheTable}>*/}
                {/*    {this.state.listId.map((id) => <option key={id.value}*/}
                {/*                                           value={id.value}>{id.display}</option>)}*/}
                {/*</select>*/}

                <div>
                    <table className="tableLesser">
                        <tr>
                            <th>Id scooter:</th>
                            <td>{this.state.id_moto}</td>
                        </tr>
                        <tr>
                            <th>Scooter number:</th>
                            <td>{this.state.auto_number}</td>
                        </tr>
                        <tr>
                            <th>Model</th>
                            <td>{this.state.model}</td>
                        </tr>
                        <tr>
                            <th>Insurance</th>
                            <td>{this.state.insurance}</td>
                        </tr>
                    </table>
                </div>

                <div>
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
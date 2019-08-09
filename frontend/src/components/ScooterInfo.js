import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";
import Cookies from "js-cookie";
import './../css/ScooterInfo.css'
import QRCode from 'qrcode.react';


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

            idmoto: Cookies.get('id_scooter'),
            idowner: Cookies.get('token'),
            auto_number: '',
            model: '',
            insurance: '',
            status_reserv: '',
            status_rent: '',

            balance: '',

            columnRent: [
                {headerName: "ID rent", field: "id", width: 100},
                {headerName: "Begin Rent", field: "begin_time"},
                {headerName: "End Rent", field: "end_time"},
                {headerName: "Trans Amount", field: "cost"},
                {headerName: "Tariff", field: "tariff_time"}

            ],
            rowRent: [],

            columnScooter: [
                //{headerName: "ID", field: "idmoto", width: 100},
                {headerName: "Scooter number", field: "auto_number"},
                {headerName: "Model", field: "model"},
                {headerName: "Insuranse", field: "insurance"},
                {headerName: "Reserved", field: "status_reserv"},
                {headerName: "In rent", field: "status_rent"}

            ],
            rowScooter: [],

            // rowScooter: [
            //     {
            //         auto_number: this.state.auto_number, model: this.state.model,
            //         insurance: this.state.insurance, status_reserv: this.state.status_reserv,
            //         status_rent: this.state.status_rent
            //     },
            // ],


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
        // fetch(JuliaLocalIpOW+'/infoaboutonemoto', {
        //     method: 'POST',
        //     body: Cookies.get('id_scooter')
        // })
        //     .then(result => result.json())
        //     .then(rowScooter => {
        //         //console.log(rowScooter)
        //         this.setState({
        //             rowScooter
        //             // auto_number: this.state.auto_number,
        //             // model: this.state.model,
        //             // insurance: this.state.insurance,
        //             // status_reserv: this.state.status_reserv,
        //             // status_rent: this.state.status_rent
        //         });
        //     });

        fetch(JuliaLocalIpOW + '/gotorentforscooter', {
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
        var qr = "sfb_moto:"+Cookies.get('id_scooter');
        return (
            <div>
                <NavbarComp/>
                <button className="buttonBack" onClick={this.goBack}> Back</button>
                <div className="mainMargin">
                    <h1 className="titleDop">Rental History</h1>
                    <h3 className="titleDop2">Id scooter: {this.state.idmoto}</h3>

                    {/*<table className="tableLesser">*/}
                    {/*    <tr>*/}
                    {/*        <th><h3 className="titleDop2">Id scooter: {this.state.idmoto}</h3></th>*/}
                    {/*        <td><QRCode value={qr} /></td>*/}
                    {/*    </tr>*/}
                    {/*</table>*/}
                    <div className="qr">
                        <QRCode value={qr} />
                    </div>

                    {/*<div*/}
                    {/*    className="ag-theme-balham"*/}
                    {/*    style={{height: '100px', width: '100%'}}*/}
                    {/*>*/}
                    {/*    <AgGridReact*/}
                    {/*        //pagination={true}*/}
                    {/*        enableFilter={true}*/}
                    {/*        enableSorting={true}*/}
                    {/*        columnScooter={this.state.columnScooter}*/}
                    {/*        rowScooter={this.state.rowScooter}>*/}
                    {/*    </AgGridReact>*/}
                    {/*</div>*/}
                    {/*<br/>*/}
                    {/*<br/>*/}
                    <div className="tablerenthistory">
                        <div
                            className="ag-theme-balham"
                            style={{height: '200px', width: '100%'}}
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



            </div>

        );
    }
}

export default ScooterInfo;
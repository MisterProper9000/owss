import React, {Component} from 'react';
import '../css/Info.css'
import {
    BootstrapTable,
    TableHeaderColumn
} from 'react-bootstrap-table';
import '../../node_modules/react-bootstrap-table/css/react-bootstrap-table.css'
import NavbarComp from "./NavbarComp";


import {AndreyLocalIpOW, JuliaLocalIpOW} from "./ipConfigs";

class Info extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: '',
            type: '',
            first_name: '',
            last_name: '',
            company_name: '',
            email: '',
            password: '',
            phone: '',
            address: '',
            sum_moto: '',
            bank_account: '',
            data: [],
            forms: [],
            isContentShown: false,
        };

        this.handleChange = this.handleChange.bind(this);
        this.getListOfId = this.getListOfId.bind(this);
        this.addDataToTheTable = this.addDataToTheTable.bind(this);
        this.getForms = this.getForms.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    getListOfId() {
        fetch(JuliaLocalIpOW + '/infolistid')
        //fetch('http://10.101.177.21:9091/infolistid')
            .then((resp) => {
                return resp.json()
            })
            .then(data => {
                let idFromDB = data.map(id => {
                    return {value: id, display: id}
                });
                this.setState({listId: [{value: 'all', display: 'Select id'}].concat(idFromDB)});
            }).catch(error => {
            console.log(error);
        });

    }

    getForms() {
        fetch(JuliaLocalIpOW +  '/info')
            .then(resp => {
                return resp.json()
            })
            .then(resp => this.setState(
                {
                    first_name: resp.first_name,
                    last_name: resp.last_name,
                }))
            .catch(error => {
                console.log(error);
            });
        this.getListOfId();
    }

    addDataToTheTable() {
        this.getForms();
        this.getListOfId();
        let array = [];
        for (let i = 0; i < this.state.forms.length; i++) {
            if (this.state.id == this.state.forms[i].id) {

                array.push({
                    id: this.state.forms[i].id,
                    first_name: this.state.forms[i].first_name,
                    last_name: this.state.forms[i].last_name,
                    type: this.state.forms[i].type,
                    email: this.state.forms[i].email,
                    phone: this.state.forms[i].phone,
                    company_name: this.state.forms[i].company_name,
                    address: this.state.forms[i].address,
                    sum_moto: this.state.forms[i].sum_moto,
                    bank_account: this.state.forms[i].bank_account,
                });
            }
        }

        this.setState({
            data: array,
            isContentShown: true
        });
    }

    render() {
        const {id} = this.state;
        return (
            <div>
                <NavbarComp/>
                <div className="infoForm">
                    <div className="getInfo">
                        <button onClick={this.getForms} class="btn btn-outline-danger btn-lg">Get info about lessors</button>
                        <br/>
                        {this.state.isContentShown &&
                        <text className="selectId">Select id</text>
                        }
                        {this.state.isContentShown &&
                        <select name="id" className="form-control dark" value={id} onChange={this.handleChange}
                                onClick={this.addDataToTheTable}>
                            {this.state.listId.map((id) => <option key={id.value}
                                                                   value={id.value}>{id.display}</option>)}
                        </select>
                        }
                    </div>
                    {this.state.isContentShown &&
                    <BootstrapTable data={this.state.data} headerAlign='left'>
                        <TableHeaderColumn isKey dataField='id'
                                           tdStyle={{backgroundColor: 'white'}}>
                            ID
                        </TableHeaderColumn>
                        <TableHeaderColumn dataField='first_name' tdStyle={{backgroundColor: 'white'}}>
                            FirstName
                        </TableHeaderColumn>
                        <TableHeaderColumn dataField='last_name' tdStyle={{backgroundColor: 'white'}}>
                            LastName
                        </TableHeaderColumn>
                        <TableHeaderColumn dataField='email' tdStyle={{backgroundColor: 'white'}}>
                            Email
                        </TableHeaderColumn>

                        <TableHeaderColumn iskey dataField='type' tdStyle={{backgroundColor: 'white'}}>
                            Type
                        </TableHeaderColumn>
                        <TableHeaderColumn dataField='phone' tdStyle={{backgroundColor: 'white'}}>
                            Phone
                        </TableHeaderColumn>
                        <TableHeaderColumn dataField='company_name' tdStyle={{backgroundColor: 'white'}}>
                            Company_name
                        </TableHeaderColumn>
                        <TableHeaderColumn iskey dataField='address' tdStyle={{backgroundColor: 'white'}}>
                            Address
                        </TableHeaderColumn>
                        <TableHeaderColumn dataField='sum_moto' tdStyle={{backgroundColor: 'white'}}>
                            Count moto
                        </TableHeaderColumn>
                        <TableHeaderColumn dataField='bank_account' tdStyle={{backgroundColor: 'white'}}>
                            Bank account
                        </TableHeaderColumn>
                    </BootstrapTable>}

                </div>
            </div>
        );
    }
}

export default Info;
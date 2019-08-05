import React, {Component} from 'react';
import {Container, Row, Col} from 'reactstrap';
import '../css/Info.css'
import {
    BootstrapTable,
    TableHeaderColumn
} from 'react-bootstrap-table';
import '../../node_modules/react-bootstrap-table/css/react-bootstrap-table.css'
import NavbarComp from "./NavbarComp";



class Moto extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: '',
            auto_number: '',
            model: '',
            id_owner: '',
            insurance: '',
            status: '',
            data: [],
            forms: [],

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
        fetch('http://10.101.177.21:9091/infolistidmoto')
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
        fetch('http://10.101.177.21:9091/infomoto')
            .then(resp => {
                return resp.json()
            })
            .then(resp => this.setState(
                {
                    forms: resp,
                    isContentShown: true
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
                    auto_number: this.state.forms[i].auto_number,
                    model: this.state.forms[i].model,
                    id_owner: this.state.forms[i].id_owner,
                    insurance: this.state.forms[i].insurance,
                    status: this.state.forms[i].status,
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
                    <div className="rowLesser">
                        <div className="columnLesser">
                    </div>
                        <div className="columnLesser">
                        </div>
                    </div>

                    <div className="getInfo">
                        <button onClick={this.getForms} class="btn btn-outline-danger btn-lg">Get info about moto
                        </button>
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
                        Id
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='auto_number' tdStyle={{backgroundColor: 'white'}}>
                        Auto_number
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='model' tdStyle={{backgroundColor: 'white'}}>
                        Model
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='id_owner' tdStyle={{backgroundColor: 'white'}}>
                        Id_owner
                    </TableHeaderColumn>
                    <TableHeaderColumn iskey dataField='insurance' tdStyle={{backgroundColor: 'white'}}>
                        insurance
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='status' tdStyle={{backgroundColor: 'white'}}>
                        Status
                    </TableHeaderColumn>
                </BootstrapTable>}
                <br/>


                    <br/>
                    <br/>
                    <br/>


                </div>
            </div>
        );
    }
}

export default Moto;
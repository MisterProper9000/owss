import React, { Component } from 'react';
import {BootstrapTable,
    TableHeaderColumn} from 'react-bootstrap-table';

import '../../node_modules/react-bootstrap-table/css/react-bootstrap-table.css'


class Table extends Component {
    render() {
        return (
            <div>
                <BootstrapTable data={this.props.data}>
                    <TableHeaderColumn isKey dataField='id' width="80px">
                        ID
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='auto_number'>
                        Scooter Number
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='model'>
                        Model
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='insurance'>
                        Insurance
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='status_reserv' width="120px">
                        Reserved
                    </TableHeaderColumn>
                    <TableHeaderColumn dataField='status_rent' width="120px">
                        In rent
                    </TableHeaderColumn>
                </BootstrapTable>
            </div>
        );
    }
}

export default Table;




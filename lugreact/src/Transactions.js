import React, { Component } from 'react';
import Table from 'react-bootstrap/lib/Table';

class Transactions extends Component {
  render() {
    return (

  <Table striped bordered condensed hover>
    <thead>
      <tr>
        <th>#___ {this.props.trans.length}</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Username</th>
      </tr>
    </thead>
    <tbody>
    {this.props.trans.map(function(item){
     console.log(item.name)
     return <tr>
            <td>{item.name}</td>
            <td>Mark</td>
            <td>Otto</td>
            <td>@mdo</td>
          </tr>
    })}
    </tbody>
  </Table>
    );
  }
}

export default Transactions;
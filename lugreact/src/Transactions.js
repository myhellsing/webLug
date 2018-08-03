import React, { Component } from 'react';
import Table from 'react-bootstrap/lib/Table';

class Transactions extends Component {
  render() {
    return (
  <div>
  <p>Всего трат: {this.props.trans.length}</p>
  <Table striped bordered condensed hover>
    <thead>
      <tr>
        <th>#</th>
        <th>Название</th>
        <th>Сумма</th>
        <th>Категория</th>
        <th>Тип</th>
      </tr>
    </thead>
    <tbody>
    {this.props.trans.map(function(item,i){
     return <tr key={i}>
            <td>{i+1}</td>
            <td>{item.name}</td>
            <td>{item.sum}</td>
            <td>{item.category.name}</td>
            <td>{item.type}</td>
          </tr>
    })}
    </tbody>
  </Table>
  </div>
    );
  }
}

export default Transactions;
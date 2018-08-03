import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Transactions from './Transactions';
import { Button } from 'reactstrap';


/* sample data*
 var trans = [   {"name":"аренда квартиры",
              "sum":31000.0,
              "category":{"name":"unknown","aliases":[]},
              "date":"Jan 2, 2013, 12:00:00 AM",
              "type":"OUTCOME"},

              {"name":"коммунальные платежи",
              "sum":350.0,
              "category":{"name":"unknown","aliases":[]},
              "date":"Jan 2, 2013, 12:00:00 AM",
              "type":"OUTCOME"},

              {"name":"молоко",
              "sum":32.9,
              "category":{"name":"unknown","aliases":[]}}
              ]
              /**/



 //   var trans = $.getJson("http://localhost:4567/transactions");
console.log("XMM")
//console.log(trans);
class App extends Component {
  constructor() {
     super();
     this.state = {
        month: 0,
        trans: []
     }
  }
  componentDidMount() {
  /**/
   let trans = fetch("/transactions").then(res => res.json())
     .then(
       (res) => {
          console.log("iiii");
          this.setState({trans: res});
          console.log(this.state.trans);
       },
       (error) => {
              console.log("ERROR");
       }
     );
     /**/
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <p className="App-intro">
          To get started, edit <code>src/App.js</code> and save to reload. Hello!
        </p>
        <p>список месяцев для выбора</p>
        <Button color="info" onClick={() => { this.setState({month : 0}); console.log(this.state.month) }} active ={this.state.month === 0}>Январь</Button>{' '}
        <Button color="info" onClick={() => { this.setState({month : 1}); console.log(this.state.month) }} active ={this.state.month === 1}>Февраль</Button>{' '}

        <p>список трат тут будет</p>
        <Transactions trans={this.state.trans.length ? this.state.trans[this.state.month].transactions : []}/>
      </div>
    );
  }
}

export default App;



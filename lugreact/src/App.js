import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Transactions from './Transactions';

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

class App extends Component {
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

        <p>список трат тут будет</p>
        <Transactions trans={trans}/>
      </div>
    );
  }
}

export default App;



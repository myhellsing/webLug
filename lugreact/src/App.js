import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Transactions from './Transactions';
import { Button } from 'reactstrap';
import { ButtonToolbar } from 'reactstrap';


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

class App extends Component {
  constructor() {
     super();
     this.state = {
        year : 2017,
        month: 0,
        trans: null,
        alltrans: false
     }
  }

  componentDidMount() {
  /**/
   let trans = fetch("/transactions").then(res => res.json())
     .then(
       (res) => {
          const byYear = res.reduce((acc, next) => {
            const year = new Date(next.date).getUTCFullYear();
               acc[year] = acc[year] || {};
               const month =new Date(next.date).getUTCMonth();               ;
               acc[year][month] = next;
               return acc;
          }, {})

          this.setState({trans: byYear});
       },
       (error) => {
              console.log("ERROR");
       }
     );
     /**/
  }

  getYears() {
   var years = this.state.trans ? Object.keys(this.state.trans) : [this.state.year]
    return years;
  }

  renderYearButton(item){
   return (<Button key={item} color="info" onClick={() => { this.setState({year : item}) }}
      active ={this.state.year === item}>{item}</Button>);
  }

    getMonths(){
        const res = [];
        const d = new Date();
        for (let i=0;i<12;i++){
            d.setMonth(i)
            res.push(d.toLocaleDateString('ru',{month: 'long'}))
        }
        return res;
    }

  renderMonthButton(i,name){
   return (<Button key={i} color="info" onClick={() => { this.setState({month : i}) }}
     active ={this.state.month === i}>{name}</Button>);
  }

  getTrans(){
   var transactions = [];
    if (this.state.trans  ){
        if (!this.state.alltrans){
            return this.state.trans[this.state.year][this.state.month].transactions ;
        }
        Object.keys(this.state.trans).forEach((year) =>{
             Object.keys(this.state.trans[year]).forEach((month)=>{

                transactions = transactions.concat(this.state.trans[year][month].transactions);
            });
        });
    }
    console.log(transactions);
    return transactions;
  }


  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <div className="alltrans">
          <Button key="all" color="info" onClick={() => { this.setState({alltrans : !this.state.alltrans}) }}
              active ={this.state.alltrans}>За все время</Button>
              </div>
        <p>Выберите год</p>
        <div className="years-menu">
          <ButtonToolbar>
          {this.getYears().map(item => this.renderYearButton(item))}
          </ButtonToolbar>
        </div>
        <p>список месяцев для выбора</p>
          <ButtonToolbar>
        { this.getMonths().map((item, i) => this.renderMonthButton(i, item))}
          </ButtonToolbar>
        <p>список трат тут будет</p>

        <Transactions trans={this.getTrans()}/>
      </div>
    );
  }
}

export default App;



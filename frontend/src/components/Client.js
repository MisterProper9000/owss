import React, {Component} from 'react';
import NavbarComp from "./NavbarComp";

class Client extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: [],
        };

        this.handleChange = this.handleChange.bind(this);
        this.getForms = this.getForms.bind(this);
        this.getClients = this.getClients.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }


    getForms() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", 'http://localhost:9091/clients', true);
        xhr.onreadystatechange = function () {
            if (this.readyState !== 4) return;
            let answer =this.responseText;
            console.log(JSON.parse(answer))
        };
        xhr.send();
    }

    getClients() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", 'http://localhost:9091/data', true);
        xhr.onreadystatechange = function () {
            if (this.readyState !== 4) return;
            let answer =this.responseText;
            console.log(JSON.parse(answer))
        };
        xhr.send();
    }

    render() {
        return (
            <div>
                <NavbarComp/>
                <button onClick={this.getForms} className="btn btn-secondary btn-lg">Получить информацию</button>
                <button onClick={this.getClients} className="btn btn-secondary btn-lg">Клиенты</button>
            </div>
        );
    }
}

export default Client;
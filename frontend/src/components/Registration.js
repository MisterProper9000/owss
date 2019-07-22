import React, {Component} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";

class Registration extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            name: '',
            type: "Individuals",
            email: '',
            address: '',
            phone: '',
            sum_moto: '',
            password: '',
            data: [],
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const {name, type, email, phone, address, sum_moto, password} = this.state;

        fetch('http://10.101.177.21:9091/reg', {
            method: 'POST',
            body: JSON.stringify({
                name,
                type,
                email,
                password,
                phone,
                address,
                sum_moto
            })
        })
    }

    componentDidMount() {
    }

    render() {
        const {name, type, email, phone, address, sum_moto, password} = this.state;

        return (
            <div>
                <NavbarComp/>
                <form className="formLogin" onSubmit={this.handleSubmit}>
                    <h1>Sign up</h1>
                    <div>
                        <select className="option" name="type" value={type} onChange={this.handleChange}>
                            <option value="Individuals">Individuals</option>
                            <option value="Entities">Entities</option>
                        </select>
                    </div>
                    <input className="input" type="text" placeholder="name" name="name"
                           value={name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="email" name="email"
                           value={email}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="phone" name="phone"
                           value={phone}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="address" name="address"
                           value={address}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="sum_moto" name="sum_moto"
                           value={sum_moto}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="password" placeholder="password" name="password"
                           value={password}
                           onChange={this.handleChange}/><br/>
                    <div className="errorMsg">{this.state.errorMsg}</div>
                    <input type="submit" name="buttonLogin" className="input btn btn-secondary"
                           value="Ok"/>
                </form>
            </div>
        );
    }
}

export default Registration;
import React, {Component} from 'react';
import '../css/Login.css';
import NavbarComp from "./NavbarComp";

class Registration extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            first_name: '',
            last_name: '',
            company_name: '',
            type: "Individuals",
            email: '',
            address: '',
            phone: '',
            sum_moto: '',
            bank_account:'',
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
        const {first_name, last_name,company_name, type, email, phone, address, sum_moto,bank_account, password} = this.state;

        fetch('http://10.101.177.21:9091/reg', {
            method: 'POST',
            body: JSON.stringify({
                first_name,
                last_name,
                company_name,
                type,
                email,
                password,
                phone,
                address,
                bank_account,
                sum_moto
            })
        })
    }

    componentDidMount() {
    }

    render() {
        const {first_name, last_name,company_name, type, email, phone, address, sum_moto,bank_account, password} = this.state;

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
                    <input className="input" type="text" placeholder="first name" name="first_name"
                           value={first_name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="last name" name="last_name"
                           value={last_name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="company name" name="company_name"
                           value={company_name}
                           onChange={this.handleChange}/><br/>
                    <input className="input" type="text" placeholder="bank account" name="bank_account"
                           value={bank_account}
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
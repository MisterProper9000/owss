import React, {Component} from 'react'
import {Nav, Navbar} from "react-bootstrap";
import Cookies from "js-cookie";
const navbarColor = {backgroundColor: '#660033'};

class NavbarComp extends Component {
    render() {
        if(Cookies.get('token')){
            return (
                <Navbar style={navbarColor} collapseOnSelect expand="lg"  variant="dark">
                    <Navbar.Brand href="/">ScooShar</Navbar.Brand>
                    <Navbar.Brand href="/logout">Log out</Navbar.Brand>
                    <Navbar.Brand href="/info_lesser">Info</Navbar.Brand>
                    <Navbar.Brand href="/regmoto">RegMoto</Navbar.Brand>
                    <Navbar.Brand href="/infomoto">InfoMoto</Navbar.Brand>
                    <Navbar.Brand href="/lesser">Lesser</Navbar.Brand>
                    <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                    <Navbar.Collapse id="responsive-navbar-nav">
                        <Nav className="mr-auto">
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            )}
            else{
                return (
                    <Navbar style={navbarColor} collapseOnSelect expand="lg"  variant="dark">
                        <Navbar.Brand href="/">ScooShar</Navbar.Brand>
                        <Navbar.Brand href="/login">Sign in</Navbar.Brand>
                        <Navbar.Brand href="/reg">Sign up</Navbar.Brand>
                        <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                        <Navbar.Collapse id="responsive-navbar-nav">
                            <Nav className="mr-auto">
                            </Nav>
                        </Navbar.Collapse>
                    </Navbar>
                )
            }
        }


}

export default NavbarComp;
import React, {Component} from 'react'
import {Nav, Navbar} from "react-bootstrap";

class NavbarComp extends Component {
    render() {
        return (
            <Navbar collapseOnSelect expand="lg" bg="black" variant="dark">
                <Navbar.Brand href="/">ScooShar</Navbar.Brand>
                <Navbar.Brand href="/login">Sign in</Navbar.Brand>
                <Navbar.Brand href="/reg">Sign up</Navbar.Brand>
                <Navbar.Brand href="/info_lesser">Info</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="mr-auto">
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}

export default NavbarComp;


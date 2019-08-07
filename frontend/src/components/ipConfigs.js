import React, {Component} from 'react';

export var AndreyLocalIpOW = 'http://10.101.177.21:9091';
export var JuliaLocalIpOW = 'http://10.101.177.21:9091';

class ipConfigs extends Component{
    constructor(props) {
        super(props);
        this.AndreyLocalMachine = 'http://10.101.177.12:9091';
        this.JuliaLocalMachine = 'http://10.101.177.21:9091';

    }
    static getAndreyIp(){
        return this.AndreyLocalMachine;
    }

    static getJuliaIp(){
        return this.JuliaLocalMachine;
    }
}

export default ipConfigs;


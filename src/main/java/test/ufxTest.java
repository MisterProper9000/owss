package test;

import openway.service.UFXServiceImpl;

public class ufxTest {

    public static void main(String argv[]){
        UFXServiceImpl obj = new UFXServiceImpl();

        String res  = obj.requestCreateClient("Ivanov", "Ivan",
                                            1, "ivan@ya.com", "1");
        System.out.println(res);

    }

}

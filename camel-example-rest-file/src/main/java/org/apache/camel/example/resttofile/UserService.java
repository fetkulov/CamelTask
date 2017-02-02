package org.apache.camel.example.resttofile;


import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;


public class UserService {

    /**
     * List all myPojos
     *
     * @return the list of created myPojos
     */

    public Collection<MyPojo> listPojos(){
        Map<String, MyPojo> pojos = new TreeMap<>();
        MyPojo mpojo1 = new MyPojo();
        mpojo1.setMyAction("testAction1");
        mpojo1.setMyBody("John Doe");
        mpojo1.setTestHeader("header1");
        MyPojo mpojo2 = new MyPojo();
        mpojo2.setMyAction("testAction2");
        mpojo2.setMyBody("Donald Fred");
        mpojo2.setTestHeader("header2");

        pojos.put("1", mpojo2);
        pojos.put("2", mpojo2);
        return pojos.values();
    }
}


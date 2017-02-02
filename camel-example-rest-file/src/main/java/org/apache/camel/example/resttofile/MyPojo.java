package org.apache.camel.example.resttofile;

public class MyPojo {

    private String myAction;
    private String myBody;
    private String testHeader;

    public MyPojo() {
    }

    public String getMyAction() {
        return myAction;
    }

    public void setMyAction(String myAction) {
        this.myAction = myAction;
    }

    public String getMyBody() {
        return myBody;
    }

    public void setMyBody(String myBody) {
        this.myBody = myBody;
    }

    public String getTestHeader() {
        return testHeader;
    }

    public void setTestHeader(String testHeader) {
        this.testHeader = testHeader;
    }

    @Override
    public String toString() {
        return "{" +
                "\"testHeader\":\"" + testHeader + "\"" +
                ", \"myAction\":\"" + myAction + "\"" +
                ", \"myBody\":\"" + myBody + "\"" +
                "}";
    }
}

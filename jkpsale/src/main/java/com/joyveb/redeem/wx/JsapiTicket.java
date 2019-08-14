package com.joyveb.redeem.wx;

public class JsapiTicket {

    private String id;
    private String jsapiTicket;
    private int expiresIn;

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

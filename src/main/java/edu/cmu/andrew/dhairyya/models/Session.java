package edu.cmu.andrew.dhairyya.models;
import edu.cmu.andrew.dhairyya.utils.APPCrypt;

import java.util.UUID;

public class Session {

    public  String token = null;
    private String id = null;
    private String userName = null;

    public Session(Client client) throws Exception{
        this.id = client.getClientId();
        //this.token = APPCrypt.encrypt(user.id);
        this.token = UUID.randomUUID().toString();
        this.userName = client.getFullName();
    }

    public Session(Vendor vendor)throws Exception{
        this.id = vendor.getVendorId();
        //this.token = APPCrypt.encrypt(user.id);
        this.token = UUID.randomUUID().toString();
        this.userName = vendor.getFullName();
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
}

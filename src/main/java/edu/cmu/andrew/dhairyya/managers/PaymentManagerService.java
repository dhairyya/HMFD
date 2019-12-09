package edu.cmu.andrew.dhairyya.managers;

import edu.cmu.andrew.dhairyya.exceptions.AppException;

public class PaymentManagerService  extends Manager {
    public static PaymentManagerService _self;

    public PaymentManagerService() {
    }

    public static PaymentManagerService getInstance(){
        if (_self == null)
            _self = new PaymentManagerService();
        return _self;
    }

    public String makeAPayment (double Amount) throws AppException {
        try {
            return "Payment done successfully";
        } catch (Exception e) {
            throw handleException("Payment for the client Failed", e);
        }
    }
}

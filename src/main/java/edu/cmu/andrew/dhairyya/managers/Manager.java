package edu.cmu.andrew.dhairyya.managers;

import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.dhairyya.exceptions.AppException;
import edu.cmu.andrew.dhairyya.exceptions.AppInternalServerException;
import edu.cmu.andrew.dhairyya.utils.MongoPool;
import edu.cmu.andrew.dhairyya.utils.AppLogger;
import org.bson.Document;

public class Manager {

    public Manager() {

    }

    protected AppException handleException(String message, Exception e){
        if (e instanceof AppException && !(e instanceof AppInternalServerException))
            return (AppException)e;
        AppLogger.error(message, e);
        return new AppInternalServerException(-1);
    }
}

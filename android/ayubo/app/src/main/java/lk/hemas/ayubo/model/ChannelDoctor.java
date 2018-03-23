package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/18/2018. model for Channeling doctor
 */

public class ChannelDoctor implements Serializable{

    private int DocId;
    private String DocName;
    private String Date;
    private String Specialisation;
    private int SpecialiseID;
    private String Hospital;

    public int getDocId() {
        return DocId;
    }

    public void setDocId(int docId) {
        DocId = docId;
    }

    public String getDocName() {
        return DocName;
    }

    public void setDocName(String docName) {
        DocName = docName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        Specialisation = specialisation;
    }

    public int getSpecialiseID() {
        return SpecialiseID;
    }

    public void setSpecialiseID(int specialiseID) {
        SpecialiseID = specialiseID;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }
}

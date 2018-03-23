package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/19/2018. model for Sessions
 */

public class Session implements Serializable {

    private int DocId;
    private String DocName;
    private String Date;
    private int SessionID;
    private String Time;
    private int Blocked;
    private String Specialisation;
    private int MaxCounsultationCount;
    private int CurrentAppNumber;
    private String Hospital;
    private int HospitalID;
    private int SpecialiseID;
    private double HospitalFee;
    private double DoctorFee;
    private double VATFee;

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

    public int getSessionID() {
        return SessionID;
    }

    public void setSessionID(int sessionID) {
        SessionID = sessionID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getBlocked() {
        return Blocked;
    }

    public void setBlocked(int blocked) {
        Blocked = blocked;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        Specialisation = specialisation;
    }

    public int getMaxCounsultationCount() {
        return MaxCounsultationCount;
    }

    public void setMaxCounsultationCount(int maxCounsultationCount) {
        MaxCounsultationCount = maxCounsultationCount;
    }

    public int getCurrentAppNumber() {
        return CurrentAppNumber;
    }

    public void setCurrentAppNumber(int currentAppNumber) {
        CurrentAppNumber = currentAppNumber;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }

    public int getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(int hospitalID) {
        HospitalID = hospitalID;
    }

    public int getSpecialiseID() {
        return SpecialiseID;
    }

    public void setSpecialiseID(int specialiseID) {
        SpecialiseID = specialiseID;
    }

    public double getHospitalFee() {
        return HospitalFee;
    }

    public void setHospitalFee(double hospitalFee) {
        HospitalFee = hospitalFee;
    }

    public double getDoctorFee() {
        return DoctorFee;
    }

    public void setDoctorFee(double doctorFee) {
        DoctorFee = doctorFee;
    }

    public double getVATFee() {
        return VATFee;
    }

    public void setVATFee(double VATFee) {
        this.VATFee = VATFee;
    }
}

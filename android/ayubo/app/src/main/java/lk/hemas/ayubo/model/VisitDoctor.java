package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/17/2018. Model for Doctors information on visits
 */

public class VisitDoctor implements Serializable{

    private int id;
    private String name;
    private String echannelling;
    private String doc990;
    private String hemas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEchannelling() {
        return echannelling;
    }

    public void setEchannelling(String echannelling) {
        this.echannelling = echannelling;
    }

    public String getDoc990() {
        return doc990;
    }

    public void setDoc990(String doc990) {
        this.doc990 = doc990;
    }

    public String getHemas() {
        return hemas;
    }

    public void setHemas(String hemas) {
        this.hemas = hemas;
    }
}

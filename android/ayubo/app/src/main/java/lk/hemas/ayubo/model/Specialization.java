package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/17/2018. model for Specialization
 */

public class Specialization implements Serializable {

    private int id;
    private String name;
    private String type;
    private int echannelling;
    private int doc990;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEchannelling() {
        return echannelling;
    }

    public void setEchannelling(int echannelling) {
        this.echannelling = echannelling;
    }

    public int getDoc990() {
        return doc990;
    }

    public void setDoc990(int doc990) {
        this.doc990 = doc990;
    }

    public String getHemas() {
        return hemas;
    }

    public void setHemas(String hemas) {
        this.hemas = hemas;
    }
}

package lk.hemas.ayubo.model;

import java.io.Serializable;

/**
 * Created by Sabri on 3/17/2018. model for Specialization
 */

public class Specialization implements Serializable {

    private int SpecialiseId;
    private String Specialisation;

    public int getSpecialiseId() {
        return SpecialiseId;
    }

    public void setSpecialiseId(int specialiseId) {
        SpecialiseId = specialiseId;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        Specialisation = specialisation;
    }
}

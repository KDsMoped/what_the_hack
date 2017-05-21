package de.hsd.hacking.Data;

import java.util.ArrayList;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class NameHolder {

    private ArrayList<String> surNames;
    private ArrayList<String> lastNames;

    public NameHolder(){
        this.surNames = new ArrayList<String>(10);
        this.lastNames = new ArrayList<String>(10);
    }

    public ArrayList<String> getSurNames() {
        return surNames;
    }

    public void setSurNames(ArrayList<String> surNames) {
        this.surNames = surNames;
    }

    public ArrayList<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(ArrayList<String> lastNames) {
        this.lastNames = lastNames;
    }
}

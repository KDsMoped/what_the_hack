package de.hsd.hacking.Data;

import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Utils.RandomUtils;

import java.util.ArrayList;

/**
 * Created by ju on 04.07.17.
 */

/**
 * This class is a simple data holder for random company names.
 */
public class MissionVariablesHolder {
    private ArrayList<String> companyNames = new ArrayList<String>();
    private ArrayList<String> passwordApplications = new ArrayList<String>();
    private ArrayList<String> universities = new ArrayList<String>();
    private ArrayList<String> towns = new ArrayList<String>();

    public MissionVariablesHolder(){}

    /**
     * Returns a random company name from the list.
     * @return
     */
    public String getRandomCompany() {
        return companyNames.get(RandomUtils.randomInt(companyNames.size()));
    }

    /**
     * Returns a random password application from the list.
     * @return
     */
    public String getRandomPasswordApplication() {
        return passwordApplications.get(RandomUtils.randomInt(passwordApplications.size()));
    }

    /**
     * Returns a random university name from the list.
     * @return
     */
    public String getRandomUniversity() {
        return universities.get(RandomUtils.randomInt(universities.size()));
    }

    /**
     * Returns a random university name from the list.
     * @return
     */
    public String getRandomTown() {
        return towns.get(RandomUtils.randomInt(towns.size()));
    }

    public ArrayList<String> getCompanyNames() {
        return companyNames;
    }

    public void setCompanyNames(ArrayList<String> companyNames) {
        this.companyNames = companyNames;
    }
}

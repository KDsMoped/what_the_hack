package de.hsd.hacking.Data;

import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Utils.RandomUtils;

import java.util.ArrayList;

/**
 * This class is a simple data holder for random company names.
 * @author Florian, Julian
 */
public class MissionVariablesHolder {
    private ArrayList<String> companyNames = new ArrayList<String>();
    private ArrayList<String> passwordApplications = new ArrayList<String>();
    private ArrayList<String> webservices = new ArrayList<String>();
    private ArrayList<String> software = new ArrayList<String>();
    private ArrayList<String> universities = new ArrayList<String>();
    private ArrayList<String> institutions = new ArrayList<String>();
    private ArrayList<String> towns = new ArrayList<String>();
    private ArrayList<String> countries = new ArrayList<String>();

    public MissionVariablesHolder(){}

    /**
     * Returns a random company name from the list.
     * @return
     */
    public String getRandomCompany() {
        return RandomUtils.randomElement(companyNames);
    }


    /**
     * Returns a random institution from the list.
     * @return
     */
    public String getRandomInstitution() {
        return RandomUtils.randomElement(institutions);
    }


    /**
     * Returns a random company name from the list.
     * @return
     */
    public String getRandomCountry() {
        return RandomUtils.randomElement(countries);
    }

    /**
     * Returns a random password application from the list.
     * @return
     */
    public String getRandomPasswordApplication() {
        return RandomUtils.randomElement(passwordApplications);
    }

    /**
     * Returns a random university name from the list.
     * @return
     */
    public String getRandomUniversity() {
        return RandomUtils.randomElement(universities);
    }

    /**
     * Returns a random kind of web service from the list.
     * @return
     */
    public String getRandomWebService() {
        return RandomUtils.randomElement(webservices);
    }

    /**
     * Returns a random kind of web service from the list.
     * @return
     */
    public String getRandomSoftware() {
        return RandomUtils.randomElement(software);
    }

    /**
     * Returns a random town name from the list.
     * @return
     */
    public String getRandomTown() {
        return RandomUtils.randomElement(towns);
    }

    public ArrayList<String> getCompanyNames() {
        return companyNames;
    }

    public void setCompanyNames(ArrayList<String> companyNames) {
        this.companyNames = companyNames;
    }
}

package de.hsd.hacking.Data;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by ju on 04.07.17.
 */

/**
 * This class is a simple data holder for random company names.
 */
public class CompanyNamesHolder {
    private ArrayList<String> companyNames;

    public CompanyNamesHolder(){
        this.companyNames = new ArrayList<String>();
    }

    /**
     * Returns a random company name from the list.
     * @return
     */
    public String getRandom() {
        return companyNames.get(MathUtils.random(companyNames.size() - 1));
    }

    public ArrayList<String> getCompanyNames() {
        return companyNames;
    }

    public void setCompanyNames(ArrayList<String> companyNames) {
        this.companyNames = companyNames;
    }
}

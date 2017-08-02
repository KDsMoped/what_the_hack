package de.hsd.hacking.Data;

import de.hsd.hacking.Entities.Employees.Employee;

import java.util.ArrayList;

public class NameHolder {

    private ArrayList<String> surNamesMale;
    private ArrayList<String> surNamesFemale;
    private ArrayList<String> surNamesALL;
    private ArrayList<String> lastNames;

    public NameHolder() {
        this.surNamesMale = new ArrayList<String>(10);
        this.surNamesFemale = new ArrayList<String>(10);
        this.lastNames = new ArrayList<String>(10);
    }

    public ArrayList<String> getSurNamesMale() {
        return surNamesMale;
    }

    public ArrayList<String> getSurNamesFemale() {
        return surNamesFemale;
    }

    public ArrayList<String> getSurNames(Employee.Gender gender) {
        switch (gender) {
            case MALE:
                return surNamesMale;
            case FEMALE:
                return surNamesFemale;
            case UNDECIDED: {

                if (surNamesALL == null) {
                    surNamesALL = new ArrayList<String>();
                    surNamesALL.addAll(surNamesMale);
                    surNamesALL.addAll(surNamesFemale);
                }

                return surNamesALL;
            }
        }

        return null;
    }


    public ArrayList<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(ArrayList<String> lastNames) {
        this.lastNames = lastNames;
    }
}

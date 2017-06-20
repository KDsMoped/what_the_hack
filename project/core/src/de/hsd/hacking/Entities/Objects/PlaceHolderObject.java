package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeState;

/**
 * Created by Cuddl3s on 20.06.2017.
 */

public class PlaceHolderObject extends Object implements Interactable {


    private final Object placeHolderForObject;

    public PlaceHolderObject(Object object) {
        super(null, object.isBlocking(), object.isTouchable(), object.isInteractable(), Direction.DOWN, 0);
        this.placeHolderForObject = object;
    }

    @Override
    public EmployeeState interact(Employee e) {
        if (isInteractable()){
            return ((Interactable)placeHolderForObject).interact(e);
        }
        //Sollte nicht vorkommen, dann ist irgendwas falschgelaufen
        throw new IllegalStateException("Placeholder object declared interactable, but parent object was not.");
    }

    @Override
    public void occupy() {
        if (isInteractable()){
            ((Interactable)placeHolderForObject).occupy();
        }
    }

    @Override
    public void deOccupy() {
        if (isInteractable()){
            ((Interactable)placeHolderForObject).deOccupy();
        }
    }

    @Override
    public boolean isOccupied() {
        if (isInteractable()){
            return ((Interactable)placeHolderForObject).isOccupied();
        }
        //Sollte nicht vorkommen
        return false;
    }

    @Override
    public boolean isDelegatingInteraction() {
        if (isInteractable()) {
            return ((Interactable) placeHolderForObject).isDelegatingInteraction();
        }
        return false;
    }
}

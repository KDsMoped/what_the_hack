package de.hsd.hacking.Utils;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Cuddl3s on 22.05.2017.
 */

public class RandomIntPool {

    private LinkedList<Integer> numbers;


    public RandomIntPool(){
        numbers = new LinkedList<Integer>();
    }

    public RandomIntPool(Integer ... possibleNumbers){
        numbers = new LinkedList<Integer>();
        Collections.addAll(numbers, possibleNumbers);
        Collections.shuffle(numbers);
    }

    public RandomIntPool(FromTo fromTo){
        numbers = new LinkedList<Integer>();
        for (int i = fromTo.from; i <= fromTo.to ; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
    }

    public boolean addNumber(Integer number) {
        if (numbers.contains(number)){
            return false;
        }else{
            numbers.add(number);
            Collections.shuffle(numbers);
            return true;
        }
    }

    public Integer getRandomNumber(){
        return numbers.removeFirst();
    }

    public Integer getRandomNumberWithoutRemoving(){
        Integer number = numbers.getFirst();
        Collections.shuffle(numbers);
        return number;
    }

    public boolean removeNumber(Integer number){
        return numbers.remove(number);
    }

    public int getSize(){
        return numbers.size();
    }
}

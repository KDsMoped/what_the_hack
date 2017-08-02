package de.hsd.hacking.Utils.Provider;

/**
 * Used for references to a String that are evaluated when called. This should usually be implemented by an anonymous inner class.
 *
 * @author Hendrik
 */
public interface StringProvider{
    String get();
}
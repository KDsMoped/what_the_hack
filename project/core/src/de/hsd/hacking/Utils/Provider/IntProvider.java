package de.hsd.hacking.Utils.Provider;

/**
 * Used for references to an int that are evaluated when called. This should usually be implemented by an anonymous inner class.
 *
 * @author Hendrik
 */
public interface IntProvider {
    int get();
}

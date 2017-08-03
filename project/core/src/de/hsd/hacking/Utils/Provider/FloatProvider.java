package de.hsd.hacking.Utils.Provider;

/**
 * Used for references to a float that are evaluated when called. This should usually be implemented by an anonymous inner class.
 *
 * @author Hendrik
 */
public interface FloatProvider {
    float get();
}

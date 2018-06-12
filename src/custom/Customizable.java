package custom;

/**
 * <p>
 * This interface offers a single method customSetup.
 * Implementing the method by client and refer to the methods in CustomUtils,
 * the app can thereby run the custom.
 * </p>
 */
public interface Customizable {

    /**
     * Set up the clients' custom by overriding it.
     */
    void customSetup();
}

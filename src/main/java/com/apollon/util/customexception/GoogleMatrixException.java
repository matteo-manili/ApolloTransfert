package com.apollon.util.customexception;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class GoogleMatrixException extends Exception {
    private static final long serialVersionUID = 5551310488888048280L;

	/**
     * Constructor for UserExistsException.
     *
     * @param message exception message
     */
    public GoogleMatrixException(final String message) {
        super(message);
    }

	public GoogleMatrixException() {
		super();
	}
    
    
    
    
}

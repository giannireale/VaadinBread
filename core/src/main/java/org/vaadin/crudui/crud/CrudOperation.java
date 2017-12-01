package org.vaadin.crudui.crud;

/**
 * @author Alejandro Duarte.
 */
public enum CrudOperation implements Operation {

    CANCEL, READ, ADD, UPDATE, DELETE;

	/* (non-Javadoc)
	 * @see org.vaadin.crudui.crud.Operation#getOperationName()
	 */
	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return name();
	}

}

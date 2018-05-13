package com.test.components.service;

import com.test.components.exception.ComponentException;
import com.test.components.model.ComponentDetails;

//Interface providing the contracts for the service methods
public interface IComponentService {

    ComponentDetails retrieveComponentDetails(String filter) throws ComponentException;
}

package com.test.components.controller;

import com.test.components.constants.ErrorCode;
import com.test.components.constants.ServiceConstants;
import com.test.components.constants.TestConstants;
import com.test.components.exception.ComponentException;
import com.test.components.model.ComponentDetails;
import com.test.components.service.impl.ComponentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentControllerTest {

    @InjectMocks
    ComponentController componentController;



    @Mock
    ComponentService componentService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenGlobalComponentName_WhenRetrievingComponents_ThenReturnValidResponse(){

        ComponentDetails componentDetails = new ComponentDetails();
        componentDetails.setComponents(1);
        try {
            when(componentService.retrieveComponentDetails("Global")).thenReturn(componentDetails);
        } catch (ComponentException e) {
            e.printStackTrace();
        }


        ResponseEntity responseEntity = componentController.retrieveComponentDetails("Global");
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        Assert.assertEquals(componentDetails.getComponents(),((ComponentDetails)responseEntity.getBody()).getComponents());

    }

    @Test
    public void givenServicesComponentName_WhenRetrievingComponents_ThenReturnNotFoundResponse(){

        ComponentDetails componentDetails = new ComponentDetails();
        componentDetails.setComponents(0);
        try {
            when(componentService.retrieveComponentDetails("Services")).thenReturn(componentDetails);
        } catch (ComponentException e) {
            e.printStackTrace();
        }


        ResponseEntity responseEntity = componentController.retrieveComponentDetails("Services");
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
        Assert.assertEquals(TestConstants.NO_COMPONENTS_RESPONSE,responseEntity.getBody());

    }

    @Test
    public void givenNoNames_WhenRetrievingComponents_ThenReturnValidResponse(){

        ComponentDetails componentDetails = new ComponentDetails();
        componentDetails.setComponents(26);
        try {
            when(componentService.retrieveComponentDetails(null)).thenReturn(componentDetails);
        } catch (ComponentException e) {
            e.printStackTrace();
        }


        ResponseEntity responseEntity = componentController.retrieveComponentDetails(null);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        Assert.assertEquals(componentDetails.getComponents(),((ComponentDetails)responseEntity.getBody()).getComponents());

    }


    @Test
    public void givenNoNames_WhenRetrievingComponents_ThenReturnException()  {

        ComponentDetails componentDetails = new ComponentDetails();
        componentDetails.setComponents(26);
        try {
            when(componentService.retrieveComponentDetails(null)).thenThrow(new ComponentException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.COMPONENTS_ERROR_1,ServiceConstants.ERROR_TECHNICAL_PROBLEM,ServiceConstants.ERROR_DETAILED_TECHNICAL_INTERNAL_SERVER_ERROR_4));
            ResponseEntity responseEntity = componentController.retrieveComponentDetails(null);
            Assert.assertNotNull(responseEntity);
            Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
        } catch (ComponentException e) {
            e.printStackTrace();
        }

    }







}

package com.test.components.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.components.config.ComponentConfiguration;
import com.test.components.entities.ComponentEntity;
import com.test.components.exception.ComponentException;
import com.test.components.model.Component;
import com.test.components.model.ComponentDetails;
import com.test.components.model.ComponentList;
import com.test.components.repo.IComponentRepository;
import com.test.components.service.impl.ComponentService;
import com.test.components.transform.ComponentTransformer;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class ComponentServiceTest {

    @InjectMocks
    @Autowired
    ComponentService componentService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ComponentConfiguration componentConfiguration;

    @Mock
    ComponentTransformer componentTransformer;

    @Mock
    IComponentRepository componentRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test

    public void givenRegionsName_WhenRetrievingComponents_ThenReturnValidResponse(){

        InputStream inputStream = null;
        ObjectMapper objectMapper = new ObjectMapper();

        inputStream = getClass().getClassLoader().getResourceAsStream("testdata/digitaloceanapiresponse.json");
        String apiResponse = null;
        try {
             apiResponse = IOUtils.toString(inputStream,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ComponentList componentList = null;
        try {
            componentList = objectMapper.readValue(apiResponse,new TypeReference<ComponentList>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseEntity  responseEntity = new ResponseEntity(componentList,HttpStatus.OK);

        when(restTemplate.getForEntity(Mockito.anyString(),Matchers.any(Class.class))).thenReturn(responseEntity);

        when(componentConfiguration.getComponentsApiUrl()).thenReturn("https://dummyurl");

        List<ComponentEntity> componentEntityList = new ArrayList<ComponentEntity>();
        componentEntityList.add(new ComponentEntity(123456,"Regions","operational"));
        when(componentTransformer.convertComponentsToEntities(Mockito.anyList())).thenReturn(componentEntityList);

        when(componentRepository.saveAll(any())).thenReturn(componentEntityList);

        ComponentDetails componentDetails = null;
        try {
            componentDetails = componentService.retrieveComponentDetails("Regions");
        } catch (ComponentException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(componentDetails);
        Assert.assertEquals(1,componentDetails.getComponents());


    }


}






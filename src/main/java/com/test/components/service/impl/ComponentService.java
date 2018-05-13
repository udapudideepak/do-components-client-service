package com.test.components.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.components.config.ComponentConfiguration;
import com.test.components.constants.ErrorCode;
import com.test.components.constants.ServiceConstants;
import com.test.components.entities.ComponentEntity;
import com.test.components.exception.ComponentException;
import com.test.components.model.Component;
import com.test.components.model.ComponentDetails;
import com.test.components.model.ComponentList;
import com.test.components.repo.IComponentRepository;
import com.test.components.service.IComponentService;
import com.test.components.transform.ComponentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.text.MessageFormat.format;

//Implementer class of components interface contracts.
@Service
public class ComponentService implements IComponentService {

    Logger logger = Logger.getLogger(ComponentService.class.getName());

    @Autowired
    IComponentRepository componentRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ComponentConfiguration componentConfiguration;

    @Autowired
    ComponentTransformer componentTransformer;

    String[] allowedStatus = {"operational","degraded_performance","partial_outage","major_outage"};

    @Override
    //@HystrixCommand(fallBackMethod="defaultResponse")
    public ComponentDetails retrieveComponentDetails(String filter) throws ComponentException {

        //get the components by calling the digital ocean API URL

        logger.info("Start --> retrieveComponentDetails");
        ResponseEntity<ComponentList> response = null;

        try{
            response = getDigitalOceanComponentsURIResponse();
            logger.finest(format("Response from the digital ocean API is {0}",response));
         }
        catch(Exception e){
            ComponentException exception = new ComponentException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.COMPONENTS_ERROR_1,ServiceConstants.ERROR_TECHNICAL_PROBLEM,ServiceConstants.ERROR_DETAILED_TECHNICAL_INTERNAL_SERVER_ERROR_1);
            logger.finer(format("Exception has happened while invoking the DO API :{0}",e));
            throw exception;
        }

        //convert the response into list of components

        List<Component> componentList  = null;

        try{
            if(!CollectionUtils.isEmpty(response.getBody().getComponents())){
                componentList = validateAndConvertResponseToComponentList(response.getBody().getComponents(),filter);
                logger.finest(format("Component list after validation is {0}",componentList));

            }
        }
        catch (Exception e){

            ComponentException exception = new ComponentException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.COMPONENTS_ERROR_2,ServiceConstants.ERROR_TECHNICAL_PROBLEM,ServiceConstants.ERROR_DETAILED_TECHNICAL_INTERNAL_SERVER_ERROR_2);
            logger.finer(format("Exception happened while the validations were being performed : {0}",e));
            throw exception;

        }

        //transform the components to convert the page_id and group_id into a composite id.

        List<ComponentEntity> entityList = null;

        try{
            
            if(!CollectionUtils.isEmpty(componentList)){
                entityList = componentTransformer.convertComponentsToEntities(componentList);
                logger.finest(format("Entity list after transformation is {0}",entityList));
            }
        }
        catch (Exception e){

            ComponentException exception = new ComponentException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.COMPONENTS_ERROR_3,ServiceConstants.ERROR_TECHNICAL_PROBLEM,ServiceConstants.ERROR_DETAILED_TECHNICAL_INTERNAL_SERVER_ERROR_3);
            logger.finer(format("Exception happened as part of transformation of components : {0}",e));
            throw exception;

        }

        try{

            //save all the component entities into H2 DB

            if(!CollectionUtils.isEmpty(entityList)){
                componentRepository.saveAll(entityList);
                logger.finer("Entities saved into the H2 DB without any issues.");
            }
        }
        catch(Exception e){

            ComponentException exception = new ComponentException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.COMPONENTS_ERROR_4,ServiceConstants.ERROR_TECHNICAL_PROBLEM,ServiceConstants.ERROR_DETAILED_TECHNICAL_INTERNAL_SERVER_ERROR_4);
            logger.finest(format("Exception happened while the entities were being saved into H2 : {0}",e));
            throw exception;

        }
        ComponentDetails componentDetails = new ComponentDetails();
        if(!CollectionUtils.isEmpty(entityList)){
            componentDetails.setComponents(entityList.size());
            logger.info("Component details set and returned");
        }
        else{
            componentDetails.setComponents(0);
        }
        logger.info("End --> retrieveComponentDetails");
        return componentDetails;
    }


    private ResponseEntity<ComponentList> getDigitalOceanComponentsURIResponse(){
        String resourceUrl = componentConfiguration.getComponentsApiUrl();
        ResponseEntity<ComponentList> response = null;

        if(!StringUtils.isEmpty(resourceUrl)){
            response = restTemplate.getForEntity(resourceUrl,ComponentList.class);

        }
        return response;
    }



    //group_id!=null and status validations are being done in the below method.
    private List<Component> validateAndConvertResponseToComponentList(List<Component> componentList, String names){

        List<Component> retList = null;

        if(!StringUtils.isEmpty(names)){
            //in case of multiple names, this block is applicable
            if(names.indexOf(",")!=-1){
                String[] filterNames = StringUtils.split(names,",");
                retList = componentList.stream().filter(component -> (component.getGroupId()!=null && Arrays.asList(filterNames).contains(component.getName()) && Arrays.asList(allowedStatus).contains(component.getStatus()))).collect(Collectors.toList());
            }
            else{
                //for only one name, this is applicable
                retList = componentList.stream().filter(component -> (component.getGroupId()!=null && Arrays.asList(names).contains(component.getName()) && Arrays.asList(allowedStatus).contains(component.getStatus()))).collect(Collectors.toList());
            }
        }
        else{
            //No name condition
            retList = componentList.stream().filter(component -> (component.getGroupId()!=null && Arrays.asList(allowedStatus).contains(component.getStatus()))).collect(Collectors.toList());
        }

        return retList;

    }

    //fall back method of hystrix circuit breaker
    public ComponentDetails defaultResponse(){

        ComponentDetails componentDetails = new ComponentDetails();
        componentDetails.setComponents(0);
        return componentDetails;
    }
}

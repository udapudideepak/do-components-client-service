package com.test.components.controller;

import com.test.components.exception.ComponentException;
import com.test.components.model.ComponentDetails;
import com.test.components.model.ErrorResponse;
import com.test.components.service.IComponentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static java.text.MessageFormat.format;

@RestController
public class ComponentController {
    Logger logger = Logger.getLogger(ComponentController.class.getName());

    @Autowired
    IComponentService componentService;

    @ApiOperation(value="Retrieve components by invoking the Digital Ocean API",notes="This API retrieves all the component details or"
    +" the components based on the filters by the component names that are passed separated by comma.", response = ComponentDetails.class)
    @ApiResponses({
            @ApiResponse(code=200,message="Successfully retrieved the component details.", response=ComponentDetails.class),
            @ApiResponse(code=500,message="Internal server error.", response=ErrorResponse.class),
            @ApiResponse(code=200,message="No components found.")})
    @GetMapping(value="/api/v1/components",produces = "application/json")
    @ResponseBody
    public ResponseEntity retrieveComponentDetails(@ApiParam(value="names") @RequestParam(value="names",required=false) String names){

        logger.info("Start --> retrieveComponentDetails ");
        logger.info(format("Filters being applied are {0}", names));

        try{
            ComponentDetails componentDetails = componentService.retrieveComponentDetails(names);
            logger.finer(format("Component service returned {0} components.",componentDetails.getComponents()));
            if(componentDetails.getComponents()>0){
                logger.info("Response is good with the components.");
                logger.info("End --> retrieveComponentDetails ");
                return new ResponseEntity(componentDetails,HttpStatus.OK);
            }
            else{
                logger.info("Components were not found.");
                logger.info("End --> retrieveComponentDetails ");
                return new ResponseEntity("No components found.",HttpStatus.NOT_FOUND);
            }
       }
       catch(ComponentException e){
            logger.finer(format("Exception happened while the service was being invoked : {0}",e));
           logger.info("End --> retrieveComponentDetails ");
            return new ResponseEntity(new ErrorResponse(e.getHttpStatusCode(),e.getErrorCode(),e.getUserMessage(),e.getDetailedMessage()),e.getHttpStatusCode());
       }


    }
}

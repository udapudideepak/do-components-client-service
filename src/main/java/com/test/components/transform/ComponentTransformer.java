package com.test.components.transform;

import com.test.components.entities.ComponentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComponentTransformer {

    public List<ComponentEntity> convertComponentsToEntities(List<com.test.components.model.Component> componentList){
        //Using the hash of group_id and page_id as the composite id
        List entitiesList = componentList.stream().map(component -> {
            ComponentEntity entity = new ComponentEntity(component.hashCode(),component.getName(),component.getStatus());
            return entity;
        }).collect(Collectors.toList());
        return entitiesList;
    }
}

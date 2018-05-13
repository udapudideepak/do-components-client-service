package com.test.components.repo;

import com.test.components.entities.ComponentEntity;
import org.springframework.data.repository.CrudRepository;

//Spring data interface for making H2 DB calls
public interface IComponentRepository extends CrudRepository<ComponentEntity,Integer> {
}

package com.test.components.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

//Entity class to store the components into the H2 DB
@Entity(name="components")
public class ComponentEntity {

    //composite id is being used
    @Id
    @Column(name="composite_id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="status")
    private String status;

    public ComponentEntity(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }


    public ComponentEntity(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentEntity that = (ComponentEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, status);
    }
}

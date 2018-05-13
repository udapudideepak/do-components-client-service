package com.test.components.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

//DO API response store model class. hashcode and equals methods based on group_id and page_id have been implemented.
public class Component implements Serializable {
    @JsonProperty("page_id")
    String pageId;

    @JsonProperty("name")
    String name;

    @JsonProperty("status")
    String status;

    @JsonProperty("group_id")
    String groupId;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(pageId, component.pageId) &&
                Objects.equals(groupId, component.groupId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pageId, groupId);
    }
}

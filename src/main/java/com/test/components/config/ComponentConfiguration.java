package com.test.components.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentConfiguration {

    //digital ocean URL
    @Value("${digital.ocean.api.url}")
    private String componentsApiUrl;

    public String getComponentsApiUrl() {
        return componentsApiUrl;
    }

    public void setComponentsApiUrl(String componentsApiUrl) {
        this.componentsApiUrl = componentsApiUrl;
    }

    @Override
    public String toString() {
        return "ComponentConfiguration{" +
                "componentsApiUrl='" + componentsApiUrl + '\'' +
                '}';
    }
}

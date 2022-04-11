package com.jira.kitsjiraclient.entities;

//import javax.persistence.*;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name="jiratools_config")
public class ConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="config_name")
    private String configKey;

    @Column(name="config_value" , length = 3000)
    private String configValue;

    public ConfigEntity(long id, String configKey, String configValue) {
        this.id = id;
        this.configKey = configKey;
        this.configValue = configValue;
    }
    public ConfigEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }


    public String toString(){
        return "{"+"\""+getConfigKey()+"\":"+"\""+getConfigValue()+"\"}" ;
    }

}

package com.jira.kitsjiraclient.config;

import com.jira.kitsjiraclient.entities.ConfigEntity;
import com.jira.kitsjiraclient.repository.ConfigRepo;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JiraToolsAppAllConfigs {



    Logger logger = LoggerFactory.getLogger(JiraToolsAppAllConfigs.class);

    @Autowired
    ConfigRepo configRepo;

    private JSONObject allConfigs=null;

    public JiraToolsAppAllConfigs() {
        //logger.info(String.valueOf(configRepo.findAll()));
    }

    public JSONObject getAllConfigs() {

        List<ConfigEntity> configs = configRepo.findAll();
        if(configs !=null) {
            allConfigs = new JSONObject();
            configs.stream()
                    .forEach(con -> {
                        //JSONObject tmp = new JSONObject();
                    	try {
							allConfigs.put(con.getConfigKey(), con.getConfigValue());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        //allConfigs.putAll(tmp);
                        //allConfigs.put(tmp);
                    });
            //logger.info("All Configurations are: " + configs);
        }
        System.out.println(allConfigs);
        return allConfigs;
    }

    public void setAllConfigs(JSONObject allConfigs) {
        this.allConfigs = allConfigs;
    }


}

package com.aosom.aosombase.nacos.parse;

import com.alibaba.nacos.spring.util.ConfigParse;
import com.alibaba.nacos.spring.util.parse.DefaultJsonConfigParse;
import com.alibaba.nacos.spring.util.parse.DefaultPropertiesConfigParse;
import com.alibaba.nacos.spring.util.parse.DefaultXmlConfigParse;
import com.alibaba.nacos.spring.util.parse.DefaultYamlConfigParse;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesParse extends DefaultJsonConfigParse {

       private static Map<String, ConfigParse> DEFAULT_CONFIG_PARSE_MAP = new HashMap(8);
     static {

         DefaultJsonConfigParse jsonConfigParse = new DefaultJsonConfigParse();
         DefaultPropertiesConfigParse propertiesConfigParse = new DefaultPropertiesConfigParse();
         DefaultYamlConfigParse yamlConfigParse = new DefaultYamlConfigParse();
         DefaultXmlConfigParse xmlConfigParse = new DefaultXmlConfigParse();
         DEFAULT_CONFIG_PARSE_MAP.put(jsonConfigParse.processType().toLowerCase(), jsonConfigParse);
         DEFAULT_CONFIG_PARSE_MAP.put(propertiesConfigParse.processType().toLowerCase(), propertiesConfigParse);
         DEFAULT_CONFIG_PARSE_MAP.put(yamlConfigParse.processType().toLowerCase(), yamlConfigParse);
         DEFAULT_CONFIG_PARSE_MAP.put(xmlConfigParse.processType().toLowerCase(), xmlConfigParse);

     }


     public    static Properties toProperties(final String context, String type) {

         if (context == null) {
             return new Properties();
         }
         // Again the type lowercase, ensure the search
         type = type.toLowerCase();

         Properties properties = new Properties();
         if (DEFAULT_CONFIG_PARSE_MAP.containsKey(type)) {
             ConfigParse configParse = DEFAULT_CONFIG_PARSE_MAP.get(type);
             properties.putAll(configParse.parse(context));
             return properties;
         } else {
             throw new UnsupportedOperationException("Parsing is not yet supported for this type profile : " + type);
         }
     }

}

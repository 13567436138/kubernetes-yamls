package com.aosom.aosombase.nacos.entity;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 存放解析规则
 */
public class MetaDataParseRule {

     private  String ruleName;


     private Class   ruleClass;



     private HashMap<String,Field>   rules;


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Class getRuleClass() {
        return ruleClass;
    }

    public void setRuleClass(Class ruleClass) {
        this.ruleClass = ruleClass;
    }

    public void  addRules(String  key, Field  field){
        if(rules==null){
            rules=new HashMap<>();
        }
        rules.put(key,field);
    }

    public HashMap<String, Field> getRules() {
        return rules;
    }

    public void setRules(HashMap<String, Field> rules) {
        this.rules = rules;
    }
}

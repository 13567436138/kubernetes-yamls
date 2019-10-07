package com.aosom.aosombase.nacos.entity;

public class BaseTool {

    /**
     * 获得ruleCashId
     * @param groupId
     * @param dataId
     * @return
     */
      public  static final    String generageRuleCasheId(String groupId,String dataId){
        return   groupId+dataId;
    }
}

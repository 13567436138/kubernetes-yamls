package com.aosom.aosombase.shardingsphere;

import java.util.List;
import java.util.Properties;


public class AosomLoadBalanceShardingsPhere  implements  org.apache.shardingsphere.spi.masterslave.MasterSlaveLoadBalanceAlgorithm   {


    @Override
    public String getDataSource(String s, String s1, List<String> list) {


        return null;
    }

    @Override
    public String getType() {
        return "loadBalance";
    }

    @Override
    public Properties getProperties() {
        return null;
    }


    @Override
    public void setProperties(Properties properties) {

    }
}

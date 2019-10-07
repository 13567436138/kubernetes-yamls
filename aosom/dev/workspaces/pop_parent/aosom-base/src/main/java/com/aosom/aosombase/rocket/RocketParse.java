package com.aosom.aosombase.rocket;

import com.aosom.aosombase.entity.RocketMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RocketParse {

    @Autowired
    private List<RocketMessageSupport> process;


    public  void  process(RocketMessage  message){
           if(process!=null&&process.size()>0){
               for(RocketMessageSupport  p : process){
                   if(p.support(message)){
                       p.process();
                   }
               }
           }
    }

}

package com.aosom.configuration;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/25 16:33
 * @Description:
 */
@Aspect
@Component
public class DataSourceAop {
    @Before("execution(* com.aosom.mapper..*.skustockdb*(..))")
    public void setDataSourceskustockdb() {
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.skustockdb);
    }

    @Before("execution(* com.aosom.mapper..*.sellerstockdb*(..))")
    public void setDataSourcesellerstockdb() {
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.sellerstockdb);
    }
    @Before("execution(* com.aosom.mapper..*.stocklogdb*(..))")
    public void setDataSourcestocklogdb() {
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.stocklogdb);
    }

}

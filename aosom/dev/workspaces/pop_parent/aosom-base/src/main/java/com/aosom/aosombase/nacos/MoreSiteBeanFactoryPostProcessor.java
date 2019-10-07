package com.aosom.aosombase.nacos;

import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent;

import com.aosom.aosombase.nacos.adapter.PropertiesParseAndRefreshAdapter;
import com.aosom.aosombase.nacos.annotation.MoreSiteEntity;
import com.aosom.aosombase.nacos.annotation.MoreSiteValue;
import com.aosom.aosombase.nacos.entity.ClassScanUtil;
import com.aosom.aosombase.nacos.entity.MetaDataParseRule;
import com.aosom.aosombase.nacos.listener.RefreshDataSourceListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.*;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.alibaba.nacos.spring.util.NacosUtils.toProperties;
import static com.aosom.aosombase.nacos.entity.BaseTool.generageRuleCasheId;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;


/**
 * 在类初始化钱加载
 */
public class MoreSiteBeanFactoryPostProcessor  implements BeanDefinitionRegistryPostProcessor,EnvironmentAware, ResourceLoaderAware,ApplicationListener<NacosConfigReceivedEvent>,     ApplicationContextAware ,Ordered{
    public static final String BEAN_NAME = "moreSiteBeanFactoryPostProcessor";



    private static final String PLACEHOLDER_PREFIX = "${";

    private static final String PLACEHOLDER_SUFFIX = "}";

    private static final String VALUE_SEPARATOR = ":";

    private  ApplicationContext  applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            //加载当前羡慕的所有多站点配置类
            registerFeignClients();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }




    private String getGroupId(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("groupId");
        if (StringUtils.hasText(value)) {
            return   value ;
        }

        throw new IllegalStateException("Either groupId must be provided in @"
                + MoreSiteEntity.class.getSimpleName());
    }
    private String getDataId(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("dataId");
        if (StringUtils.hasText(value)) {
            return    value ;
        }

        throw new IllegalStateException("Either dataId must be provided in @"
                + MoreSiteEntity.class.getSimpleName());
    }


    public void registerFeignClients() throws ClassNotFoundException, IOException {

        String basePackage=deduceMainApplicationClass().getPackage().getName();
        ClassScanUtil util=new ClassScanUtil();
        Set<Class<?>> set=util.doScan(basePackage);

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        //设置annotation
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                MoreSiteEntity.class);
        scanner.addIncludeFilter(annotationTypeFilter);


        Set<BeanDefinition> candidateComponents = scanner
                .findCandidateComponents(basePackage);
        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                // verify annotated class is an interface
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();


                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(
                                MoreSiteEntity.class.getCanonicalName());

                String groupId = getGroupId(attributes);  //groupId
                String dataId=getDataId(attributes);

                String  metaCasheId=generageRuleCasheId(groupId,dataId);
                String ruleName = annotationMetadata.getClassName();

                //获取当前class
                Class  ruleClass=Class.forName(ruleName);

                parseClassForRule(metaCasheId,ruleName,ruleClass);
            }
        }
    }

    /**
     * 解析当前class 存放当缓存中
     * @param metaCasheId
     * @param ruleName
     * @param ruleClass
     */
   public   static void parseClassForRule(String  metaCasheId,String ruleName,Class ruleClass){

       ReflectionUtils.doWithFields(ruleClass, new ReflectionUtils.FieldCallback() {
           @Override
           public void doWith(Field field) throws IllegalArgumentException {
               MoreSiteValue annotation = getAnnotation(field, MoreSiteValue.class);
               saveMedaDataRule(metaCasheId, ruleName, ruleClass,annotation  , field,field.getModifiers());
           }
       });
   }

   private static   void saveMedaDataRule(String  metaCasheId, String ruleName, Class ruleClass, MoreSiteValue  annotation,Field  field,int modifiers){
       if (annotation != null) {
           if (Modifier.isStatic(modifiers)) {
               return;
           }
           String   propertyKey = resolvePlaceholder(annotation.value());
           if (propertyKey == null) {
               return;
           }
           //保存到metaData
           MetaDataParseRule  rule=     MoreSiteContainer.getMetaDataCacheRules().get(metaCasheId);
           if(rule==null){
               rule=new MetaDataParseRule();
               rule.setRuleName(ruleName);
               rule.setRuleClass(ruleClass);
               rule.addRules(propertyKey,field);
               MoreSiteContainer.getMetaDataCacheRules().put(metaCasheId,rule);
           }else {
               rule.addRules(propertyKey,field);
           }

       }
   }

    private static String resolvePlaceholder(String placeholder) {
        if (!placeholder.startsWith(PLACEHOLDER_PREFIX)) {
            return null;
        }

        if (!placeholder.endsWith(PLACEHOLDER_SUFFIX)) {
            return null;
        }

        if (placeholder.length() <= PLACEHOLDER_PREFIX.length() + PLACEHOLDER_SUFFIX.length()) {
            return null;
        }

        int beginIndex = PLACEHOLDER_PREFIX.length();
        int endIndex = placeholder.length() - PLACEHOLDER_PREFIX.length() + 1;
        placeholder = placeholder.substring(beginIndex, endIndex);

        int separatorIndex = placeholder.indexOf(VALUE_SEPARATOR);
        if (separatorIndex != -1) {
            return placeholder.substring(0, separatorIndex);
        }

        return placeholder;
    }



    /**
     * 获取mainClass
     * @return
     */
    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        }
        catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment);

    }


    private Environment environment;
    private ResourceLoader resourceLoader;
    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader=resourceLoader;
    }

    /**
     * 接受跟新数据
     * 刷新对象数据
     * @param event
     */
    @Override
    public void onApplicationEvent(NacosConfigReceivedEvent event) {
        //判断我是否要接受数据
         String  metaCasheId=generageRuleCasheId(event.getGroupId(),event.getDataId());
         MetaDataParseRule  rule=    MoreSiteContainer.getMetaDataCacheRules().get(metaCasheId);
        if(rule==null){
            return;
        }

        String content = event.getContent();
        if (content != null) {
            String groupId=event.getGroupId();
            String dataId=event.getDataId();
            Properties configProperties = toProperties( content,event.getType());
            PropertiesParseAndRefreshAdapter.parseAndRefresh(rule,configProperties,groupId,dataId,getTypeConverter());
            if(groupId.equals(MoreSiteContainer.datasource_group_id)){
                //发送刷新数据库
                applicationContext.publishEvent(new RefreshDataSourceListener.RefreshDataSourceEvent(this,groupId,dataId));
            }
        }
    }

    private    TypeConverter  converter =null;
    private TypeConverter getTypeConverter() {

        if(converter==null){
            converter=new  SimpleTypeConverter();
        }
        return  converter;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}

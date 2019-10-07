package com.aosom.aosombase.tag;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * tag的解析
 * 在spirngmvc  中加入拦截器，  解析我们返回的对象，在http 头部加上tag信息
 *
 *
 * @AnnotationTag    在需要tag上打字段，或者名称上加上注释
 */
public class TagParse {


    /**
     * 根据传入的对象解析tag
     * @param data
     * @param tags
     * @throws IllegalAccessException
     */
    public   static  void parseTag(Object   data,final Set<String> tags) throws IllegalAccessException {

        Class  dataClass= data.getClass();  //当前对象类型
        Annotation[]   annotations=dataClass.getAnnotationsByType(AnnotationTag.class);

        if(annotations.length>0){
            //添加对象上总的标识
            for( Annotation  annotation:  annotations){
                String t=((AnnotationTag) annotation).value();
                if(!tags.contains(t)){
                    tags.add(t);
                }
            }
            //添加字段里的内容
            Field[]  fields= dataClass.getDeclaredFields();
            for(Field field: fields){
                //判断属性值上是否有值
                AnnotationTag[]  fieldAnnotation=   field.getAnnotationsByType(AnnotationTag.class);
                if(fieldAnnotation.length>0){
                    //解析当前属性
                    field.setAccessible(true);
                    Object  value=  field.get(data);
                    if(value==null){
                        continue;
                    }
                    //判断当前值是不是普通类型
                    if( field.getType().isPrimitive()|| field.getType()==String.class){
                        //普通类型    添加普通属性值
                        for(AnnotationTag  atag:fieldAnnotation){
                            String t=atag.value()+value;
                            if(!tags.contains(t)){
                                tags.add(t);
                            }
                        }
                    }else {
                        //如果是复杂对象
                        //先把当前的保存
                        boolean autoParse=false;
                        for(AnnotationTag  atag:fieldAnnotation){
                            //复杂对象的头部
                            String t=atag.value();
                            autoParse=autoParse||atag.autoParse();
                            if(!tags.contains(t)){
                                tags.add(t);
                            }
                        }
                        if(autoParse==false){
                            continue;
                        }
                        if (value instanceof List) {
                            //如果是list
                            List  listValue=(List) value;
                            if(listValue.size()>0){
                                for(Object listItem: listValue){
                                    parseTag(listItem,tags);
                                }
                            }

                        } else if (value instanceof Map) {
                            //如果是map
                            Map  mapValue=(Map) value;
                            if(mapValue.size()>0){
                                for(Object mapKey: mapValue.keySet()){
                                    parseTag(mapValue.get(mapKey),tags);
                                }
                            }
                        } else {
                            //如果是不同对象
                            parseTag(value,tags);
                        }
                    }

                }
            }
        }

    }







    /**
     *标记在对象和字段上，用户tag标识
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD,ElementType.TYPE})
    @Documented
    public static @interface  AnnotationTag{
        //tag前缀或者名称
        String value();

        //复杂对象是否在循环解析  list  map    Object
        boolean  autoParse() default true;
    }

}

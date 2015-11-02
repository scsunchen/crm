package com.invado.core.utils;







import com.invado.core.annotations.NativeQueryResultColumn;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nikola on 08/09/2015.
 */
public class NativeQueryResultsMapper {

    private static Logger log = LoggerFactory.logger(NativeQueryResultsMapper.class);

    public static <T> List<T> map(List<Object[]> objectArrayList, Class<T> genericType) {
        List<T> ret = new ArrayList<T>();
        List<Field> mappingFields = getNativeQueryResultColumnAnnotatedFields(genericType);
        try {
            for (Object[] objectArr : objectArrayList) {
                T t = genericType.newInstance();
                for (int i = 0; i < objectArr.length; i++) {
                    BeanUtils.setProperty(t, mappingFields.get(i).getName(), objectArr[i]);
                }
                ret.add(t);
            }
        } catch (InstantiationException ie) {
            log.debug("Cannot instantiate: ", ie);
            ret.clear();
        } catch (IllegalAccessException iae) {
            log.debug("Illegal access: ", iae);
            ret.clear();
        } catch (InvocationTargetException ite) {
            log.debug("Cannot invoke method: ", ite);
            ret.clear();
        }
        return ret;
    }

    // Get ordered list of fields
    private static <T> List<Field> getNativeQueryResultColumnAnnotatedFields(Class<T> genericType) {
        Field[] fields = genericType.getDeclaredFields();
        List<Field> orderedFields = Arrays.asList(new Field[fields.length]);
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(NativeQueryResultColumn.class)) {
                NativeQueryResultColumn nqrc = fields[i].getAnnotation(NativeQueryResultColumn.class);
                orderedFields.set(nqrc.index(), fields[i]);
            }
        }
        return orderedFields;
    }
}

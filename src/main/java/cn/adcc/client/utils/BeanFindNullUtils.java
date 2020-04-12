package cn.adcc.client.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanFindNullUtils {

    public static <T> String[] findNull(T current) {
        Class clazz = current.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> nullPropName = new ArrayList<>();
        for (Field field : fields) {
            try {
                String propertyName = field.getName();
                PropertyDescriptor pd = new PropertyDescriptor(propertyName, clazz);
                Method getterMethod = pd.getReadMethod();
                if (getterMethod.invoke(current) == null){
                    nullPropName.add(propertyName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return nullPropName.toArray(new String[nullPropName.size()]);
    }
}

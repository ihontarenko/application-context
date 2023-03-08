package pro.javadev;

abstract public class ClassUtils {

    public static String getShortName(Class<?> clazz) {
        return clazz.getName().substring(clazz.getPackageName().length() + 1);
    }

}

package pro.javadev.bean.creation;

import pro.javadev.bean.BeanDependency;
import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.ObjectCreationException;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractBeanCreationStrategy implements BeanCreationStrategy {

    protected Object[] getArguments(List<BeanDependency> dependencies, BeanFactory factory) {
        List<Object>         arguments    = new ArrayList<>();

        for (BeanDependency dependency : dependencies) {
            List<String> names = factory.getBeanNames(dependency.getBeanClass());
            String       name  = dependency.getBeanName();

            if (name == null && names.size() > 1) {
                throw new ObjectCreationException("SPECIFY ACTUAL BEAN NAME ONE OF "+ names +" OF TYPE: " + dependency.getBeanClass());
            } else if (names.size() == 1) {
                name = names.get(0);
            }

            if (name == null) {
                throw new ObjectCreationException("NO BEAN FOUND OF TYPE: " + dependency.getBeanClass());
            }

            arguments.add(factory.getBean(name));
        }

        return arguments.toArray(Object[]::new);
    }

}

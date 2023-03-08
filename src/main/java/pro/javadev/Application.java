package pro.javadev;

import pro.javadev.app.services.ServiceA;
import pro.javadev.app.services.ServiceB;
import pro.javadev.bean.context.AnnotationApplicationContext;
import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.definition.SupplierBeanDefinition;
import pro.javadev.bean.processor.ApplicationContextAwareBeanProcessor;

public class Application {

    public static void main(String... arguments) {
        ApplicationContext context = AnnotationApplicationContext.run(Application.class);
        context.getBeanFactory().registerBeanDefinition(new SupplierBeanDefinition("ANOTHER_SERVICE_2", ServiceB.class, ServiceB::new));

        context.addBeanProcessor(new ApplicationContextAwareBeanProcessor(context));

        System.out.println(
                (Object) context.getBean("ANOTHER_SERVICE_2")
        );

        System.out.println("FINISH");
    }

}

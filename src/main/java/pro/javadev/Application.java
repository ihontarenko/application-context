package pro.javadev;

import pro.javadev.app.services.ServiceInterface;
import pro.javadev.app.services.Storage;
import pro.javadev.app.services.UserService;
import pro.javadev.bean.context.AnnotationApplicationContext;
import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.processor.ApplicationContextAwareBeanProcessor;
import pro.javadev.bean.processor.InjectableFieldsFillerBeanProcessor;
import pro.javadev.bean.processor.LoggingBeanProcessor;

public class Application {

    public static void main(String... arguments) {
        ApplicationContext context = AnnotationApplicationContext.run(Application.class);

        context.addBeanProcessor(new LoggingBeanProcessor(System.out::println));
        context.addBeanProcessor(new ApplicationContextAwareBeanProcessor());
        context.addBeanProcessor(new InjectableFieldsFillerBeanProcessor());

        System.out.println(
                context.getBean(Storage.class)
        );

        System.out.println(
                context.getBean(UserService.class, "IN_MEMORY_USER_SERVICE")
        );

        System.out.println(
                context.getBean(ServiceInterface.class, "SERVICE_E")
        );

        System.out.println(
                context.getBean(ServiceInterface.class, "SERVICE_D")
        );

        System.out.println(
                context.getBean(ServiceInterface.class, "SERVICE_A")
        );

        System.out.println("FINISH");
    }

}

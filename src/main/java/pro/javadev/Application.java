package pro.javadev;

import pro.javadev.sandbox.User;
import pro.javadev.sandbox.services.Storage;
import pro.javadev.bean.context.AnnotationApplicationContext;
import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.processor.ApplicationContextAwareBeanProcessor;
import pro.javadev.bean.processor.PropertyValueBeanProcessor;
import pro.javadev.bean.processor.InjectableFieldsFillerBeanProcessor;
import pro.javadev.bean.processor.LoggingBeanProcessor;

public class Application {

    public static void main(String... arguments) {
        ApplicationContext context = AnnotationApplicationContext.run(Application.class);

        context.addBeanProcessor(new LoggingBeanProcessor(System.out::println));
        context.addBeanProcessor(new ApplicationContextAwareBeanProcessor());
        context.addBeanProcessor(new InjectableFieldsFillerBeanProcessor());
        context.addBeanProcessor(new PropertyValueBeanProcessor(
                System.getProperties(), System.getenv()
        ));

        Storage.InMemoryStorage storage = (Storage.InMemoryStorage) context.getBean(Storage.class);

        System.out.println(storage.getJavaHome());
        System.out.println(storage);
        System.out.println("service: " + storage.getService());

        System.out.println(context.getBean(User.class));

        System.out.println("FINISH");
    }

}

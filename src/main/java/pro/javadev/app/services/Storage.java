package pro.javadev.app.services;

import pro.javadev.bean.Bean;
import pro.javadev.bean.BeanInjection;
import pro.javadev.bean.PropertyValue;

@Bean
public interface Storage {

    class InMemoryStorage implements Storage {

        @BeanInjection("SERVICE_D")
        private ServiceInterface service;

        @PropertyValue("java.home")
        private String javaHome;

        public ServiceInterface getService() {
            return service;
        }

        public String getJavaHome() {
            return javaHome;
        }

        @Override
        public String toString() {
            return "InMemoryStorage{" +
                    "\njavaHome=" + javaHome +
                    "\nservice=" + service +
                    "\n}";
        }
    }

}

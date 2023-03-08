package pro.javadev.bean;

import pro.javadev.bean.creation.BeanCreationStrategy;
import pro.javadev.bean.definition.BeanDefinition;

import java.util.Arrays;
import java.util.List;

public class BeanCreationStrategyResolver {

    private final List<BeanCreationStrategy> strategies;

    public BeanCreationStrategyResolver(BeanCreationStrategy... strategies) {
        this.strategies = Arrays.asList(strategies);
    }

    public BeanCreationStrategy resolve(BeanDefinition definition) {
        for (BeanCreationStrategy strategy : strategies) {
            if (strategy.canCreate(definition)) {
                definition.setBeanCreationStrategy(strategy);
                return strategy;
            }
        }
        throw new IllegalArgumentException("UNSUPPORTED BEAN CLASS: " + definition.getBeanName());
    }
}

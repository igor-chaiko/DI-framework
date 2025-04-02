package framework.bean.lazy_init;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import framework.bean.bean_definition.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import static framework.container.IoCContainerImpl.instantiate;

public class LazyInitCallback implements MethodInterceptor {
    private final BeanDefinition beanDefinition;
    private volatile Object target;

        public LazyInitCallback(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (target == null) {
            synchronized (this) {
                if (target == null) {
                    target = instantiate(beanDefinition);
                }
            }
        }

        return method.invoke(target, args);
    }

    public static Object createLazyProxy(Class<?> type, BeanDefinition beanDefinition) throws Exception {
        Enhancer enhancer = new Enhancer();
        if (type.isInterface()) {
            enhancer.setInterfaces(new Class<?>[] { type });
        } else {
            enhancer.setSuperclass(type);
        }
        enhancer.setCallback(new LazyInitCallback(beanDefinition));

        Constructor<?> constructor = beanDefinition.getInstanceMeta().getConstructor();
        Object[] constructorArgs = beanDefinition.getInstanceMeta().getArgsToInvokeConstructorWith().toArray(); //new Object[0]

        Class<?>[] argTypes = Arrays.stream(constructor.getParameterTypes())
            .toArray(Class<?>[]::new);

        return enhancer.create(argTypes, constructorArgs);
    }
}

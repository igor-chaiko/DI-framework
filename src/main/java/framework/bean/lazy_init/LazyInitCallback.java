package framework.bean.lazy_init;

import java.lang.reflect.Method;

import framework.bean.bean_definition.BeanDefinition;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import static framework.container.IoCContainer.instantiate;

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
}

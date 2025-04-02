package framework.bean.scope;

import java.util.Map;

import framework.bean.scope.utils.ObjectFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocal implements Scope {
    private final java.lang.ThreadLocal<Map<String, Object>> threadLocalBeans =
        java.lang.ThreadLocal.withInitial(ConcurrentHashMap::new);

    private final AtomicInteger beanCount = new AtomicInteger(0);

    @Override
    public Object get(String beanName, ObjectFactory<?> objectFactory) {
        Map<String, Object> scopedBeans = threadLocalBeans.get();

        return scopedBeans.computeIfAbsent(beanName, name -> {
            try {
                beanCount.incrementAndGet();
                return objectFactory.getObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public int getBeansCount() {
        return beanCount.get();
    }
}

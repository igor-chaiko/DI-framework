package framework.bean.scope;

public enum ScopeType {
    SINGLETON,
    PROTOTYPE,
    THREAD_LOCAL, // Для каждого потока создается своя map, где он хранит свои объекты (beans)
}

package framework.bean.cyclic_dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import framework.bean.bean_definition.BeanDefinition;
import framework.bean.scope.ScopeType;

public class DependencyGraph {
    private final Map<String, List<String>> adjacencyList = new HashMap<>();

    public void addEdge(BeanDefinition from, BeanDefinition to) {
        adjacencyList.computeIfAbsent(from.getId(), k -> new ArrayList<>()).add(to.getId());
        if (hasCycle()) {
            if (from.getScopeType() == ScopeType.SINGLETON && to.getScopeType() == ScopeType.SINGLETON &&
                (from.getLazyInit() || to.getLazyInit())) {
                return;
            }

            throw new IllegalStateException("Cyclic dependency detected: " + from.getId() + " -> " + to.getId());
        }
    }

    private boolean hasCycle() {
        for (String node : adjacencyList.keySet()) {
            if (dfs(node, new HashSet<>(), new HashSet<>())) {
                return true;
            }
        }

        return false;
    }

    private boolean dfs(String node, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(node)) {
            return true;
        }

        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recursionStack.add(node);

        for (String neighbor : adjacencyList.getOrDefault(node, Collections.emptyList())) {
            if (dfs(neighbor, visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }
}

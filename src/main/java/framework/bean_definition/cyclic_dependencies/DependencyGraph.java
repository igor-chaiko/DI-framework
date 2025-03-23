package framework.bean_definition.cyclic_dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyGraph {
    private final Map<String, List<String>> adjacencyList = new HashMap<>();

    public void addEdge(String from, String to) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        if (hasCycle()) {
            throw new IllegalStateException("Cyclic dependency detected: " + from + " -> " + to);
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

# To-Do List for Code Refactoring

## Immediate Tasks

1. **Thread Safety**
    - Make a deep copy of the `World` object for each simulation.

2. **Shared Data Structures**
    - Research thread-safe data structures in Java.
    - Implement these structures into the code.

3. **Immutable Objects**
    - Identify parameters that should not change during simulation.
    - Make these parameters immutable.

4. **Thread-Local Storage**
    - Identify which variables each thread should independently keep track of.
    - Implement `ThreadLocal` for these variables.

5. **Environment Properties**
    - Make environment properties non-static.
    - Pass them as parameters.

6. **Executor Service**
    - Reevaluate the usage of fixed-size thread pool, explore if it's the best choice for your case.

## Deferred Tasks

1. **Error Handling**
    - Review and improve error-handling mechanisms.

2. **Resource Cleanup**
    - Implement resource cleanup measures like shutting down `executorService`.


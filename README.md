# Intechcore Java Common Utilities Library

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven-Central](https://img.shields.io/maven-central/v/com.intechcore.scomponents/common-core)](https://central.sonatype.com/artifact/com.intechcore.scomponents/common-core)
[![Hits-of-Code](https://hitsofcode.com/github/Scomponents/java-common-core?branch=master)](https://hitsofcode.com/github/Scomponents/java-common-core/view?branch=master)

A comprehensive collection of utility classes and patterns for common Java development tasks,
including safe property access, event management, exception handling, and type conversions.

## Features

- 🛡️ **Null-safe property access** with monadic chaining
- 🎯 **Type-safe event system** with deferred publication
- 🔄 **Stream conversion utilities** for I/O operations
- 🔍 **Exception analysis tools** for root cause detection
- 🏷️ **Identity-based comparison** for object tracking

## Installation

### Maven
```xml
<dependency>
    <groupId>com.intechcore.scomponents</groupId>
    <artifactId>common-core</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Gradle
```groovy
implementation 'com.intechcore.scomponents:common-core:1.1.0'
```

## Usage Examples

### Getter Monad - Safe Data Transformation Pipeline
```java
enum Doc4jAnchorExtractor {
    ONE_CELL_ANCHOR(AnchorType.ONE_CELL_BINDING,
            new GetterMonad<>(CTOneCellAnchor::getPic),
            new GetterMonad<>(CTOneCellAnchor::getSp),
            new GetterMonad<>(CTOneCellAnchor::getGraphicFrame),
            new GetterMonad<>(CTOneCellAnchor::getGrpSp),
            new GetterMonad<>(CTOneCellAnchor::getCxnSp)
    ),

    TWO_CELL_ANCHOR(AnchorType.TWO_CELL_BINDING,
            new GetterMonad<>(CTTwoCellAnchor::getPic),
            new GetterMonad<>(CTTwoCellAnchor::getSp),
            new GetterMonad<>(CTTwoCellAnchor::getGraphicFrame),
            new GetterMonad<>(CTTwoCellAnchor::getGrpSp),
            new GetterMonad<>(CTTwoCellAnchor::getCxnSp)
    ),

    ABSOLUTE_ANCHOR(AnchorType.ABSOLUTE_BINDING,
            new GetterMonad<>(CTAbsoluteAnchor::getPic),
            new GetterMonad<>(CTAbsoluteAnchor::getSp),
            new GetterMonad<>(CTAbsoluteAnchor::getGraphicFrame),
            new GetterMonad<>(CTAbsoluteAnchor::getGrpSp),
            new GetterMonad<>(CTAbsoluteAnchor::getCxnSp)
    );
    public final AnchorType anchorType;

    public final GetterMonad<CTPicture> ctPictureCaster;
    public final GetterMonad<CTShape> ctShapeCaster;
    public final GetterMonad<CTGraphicalObjectFrame> ctDrawingObjectCaster;
    public final GetterMonad<CTGroupShape> ctGroupShapeCaster;
    public final GetterMonad<CTConnector> ctConnectorShapeCaster;

    Doc4jAnchorExtractor(AnchorType anchorType,
                         GetterMonad<CTPicture> ctPictureCaster,
                         GetterMonad<CTShape> ctShapeCaster,
                         GetterMonad<CTGraphicalObjectFrame> ctDrawingObjectCaster,
                         GetterMonad<CTGroupShape> ctGroupShapeCaster,
                         GetterMonad<CTConnector> ctConnectorShapeCaster) {

        this.anchorType = anchorType;

        this.ctPictureCaster = ctPictureCaster;
        this.ctShapeCaster = ctShapeCaster;
        this.ctDrawingObjectCaster = ctDrawingObjectCaster;
        this.ctGroupShapeCaster = ctGroupShapeCaster;
        this.ctConnectorShapeCaster = ctConnectorShapeCaster;
    }
}

enum AnchorType {
    ONE_CELL_BINDING,
    TWO_CELL_BINDING,
    ABSOLUTE_BINDING;
}
```

### Event Manager - Strongly Typed Event Management System
```java
// Define your event classes
public class UserLoggedInEvent {
    private final String username;
    public UserLoggedInEvent(String username) { this.username = username; }
    public String getUsername() { return username; }
}

public class ApplicationShutdownEvent {
    // Parameterless event
}

// Create event manager
IEventManager eventManager = new EventManager();

// Subscribe to events
eventManager.subscribe(UserLoggedInEvent.class, event -> {
    System.out.println("User logged in: " + event.getUsername());
});

eventManager.subscribe(ApplicationShutdownEvent.class, () -> {
    System.out.println("Application shutting down...");
});

// Publish events
eventManager.notify(new UserLoggedInEvent("john_doe"));
eventManager.notify(ApplicationShutdownEvent.class);

```

### Scoped Event Management with RestorePointEventManager
```java
IEventManager mainEventManager = new EventManager();
RestorePointEventManager scopedManager = new RestorePointEventManager(mainEventManager);

// Temporary subscriptions for a specific operation
scopedManager.subscribe(DataLoadedEvent.class, this::handleData);
scopedManager.subscribe(ErrorEvent.class, this::handleError);

try {
    performOperation(); // Uses the temporary subscriptions
} finally {
    // Clean up all temporary subscriptions
    scopedManager.removeStoredSubscribers();
}
// Main event manager is now clean of our temporary listeners
```

### IdentityComparator - Object Identity Tracking
```java
// Create a map that uses identity comparison instead of equals()
Map<Runnable, String> taskMap = new TreeMap<>(new IdentityComparator<>());

Runnable task1 = () -> System.out.println("Task 1");
Runnable task2 = () -> System.out.println("Task 2");

// Even if tasks have same content, they're different objects
taskMap.put(task1, "First task");
taskMap.put(task2, "Second task");

// Useful for tracking object instances in collections
Set<Object> identitySet = new TreeSet<>(new IdentityComparator<>());
```

Star this repo if you find it useful! ⭐

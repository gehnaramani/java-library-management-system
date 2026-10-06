# LibraDesk — Java Library Management System

A Java-based Library Management System with a real localhost web interface.

## Features
- Book catalogue
- Search books
- Issue book
- Available/issued book counters
- Library activity
- Java HTTP server

## Run

JDK 17+:

```bash
javac --add-modules jdk.httpserver src/LibrarySystem.java
java --add-modules jdk.httpserver -cp src LibrarySystem
```

Open `http://localhost:8080`.

## Concepts
Java classes, objects, methods, static state, HTTP server, and event handling.

# Storage Management System

A desktop inventory app for a small business: products, customers, suppliers, purchases, and transactions, all in one place. I built it with Java Swing and a Neo4j graph database, and wrote every UI component from scratch instead of reaching for Swing's stock widgets.

Three parts of it are worth calling out. A table view with live substring search across every field, filterable by dropdown and resizable to fullscreen. A graph view, built with JGraphX, where clicking a node shows how it connects to the rest of the data. And an analytics screen with a custom calendar picker for choosing a date range, feeding charts (built with JFreeChart) for total sales over time, the ten best selling and most profitable products, average delivery time per supplier, and current stock against each product's minimum.

Login and registration are custom as well, with role based access controlling who can do what.

## Stack

Java, Neo4j, JGraphX for the graph view, JFreeChart for the analytics charts. The code follows MVC, with a singleton wrapping the Neo4j driver. Exact versions are in `pom.xml` if you need them.

## Running it locally

Clone the repo, then open `pom.xml` and set `maven.compiler.source` and `maven.compiler.target` to the JDK version you have installed.

Launch Neo4j Desktop, create a project, and import `neo4j.dump` into it, setting whatever password you like when it asks. Open that DBMS's settings and set `dbms.security.auth_enabled=false` so the app can connect without extra configuration. From there:

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="org.example.views.MainWindow"
```

## Screenshots

### Data table
![Data Table Interface](src/main/resources/demo/img.png)

### Search
![Search Feature](src/main/resources/demo/img_1.png)

### Graph view
![Graph View](src/main/resources/demo/img_2.png)

### Calendar picker
![Calendar Selection](src/main/resources/demo/img_3.png)

### Sales over time
![Sales Over Time](src/main/resources/demo/img_4.png)

### Best selling products
![Best Selling Products](src/main/resources/demo/img_5.png)

### Most profitable products
![Most Profitable Products](src/main/resources/demo/img_6.png)

### Delivery time by supplier
![Delivery Time Analysis](src/main/resources/demo/img_7.png)

### Stock levels
![Stock Analysis](src/main/resources/demo/img_8.png)

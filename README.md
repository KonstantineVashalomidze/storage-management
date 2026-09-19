# Storage Management System

A desktop inventory system built with Java Swing and Neo4j. It handles products, customers, suppliers, purchases, and transactions, with a graph view, calendar-based analytics, and UI components built from scratch rather than pulled from Swing's stock widgets.

## Features

**Data management**
- Custom table view with real-time substring search across every field
- Dropdown filters for quick navigation
- Fullscreen, resizable layout

**Visualization**
- Interactive graph view of the underlying data, built with JGraphX
- Click a node to see its full details

**Analytics**
- Calendar based date range picker for sales analysis
- Total sales over time, top 10 best selling and most profitable products, average delivery time per supplier, stock levels against minimums

**Auth**
- Custom login and registration, with role based access

## Stack

Java 21, Neo4j 5.15.0, JGraphX 4.2.2 for graph visualization, JFreeChart 1.0.13 for charts. Built with MVC and a singleton for the database connection.

## Running it locally

1. Clone the repo and `cd storage-management`
2. Launch Neo4j Desktop, create a project, and import `neo4j.dump`. Set `dbms.security.auth_enabled=false`
3. `mvn clean install`
4. `mvn exec:java -Dexec.mainClass="org.example.views.MainWindow"`

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

## Contact

vashalomidzekonstantine@gmail.com

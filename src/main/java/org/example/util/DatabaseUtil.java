package org.example.util;

import org.example.models.*;
import org.example.models.Transaction;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private static final String URI = "bolt://localhost:7687"; // Neo4j database URI
    private static final String USERNAME = "neo4j"; // Neo4j username
    private static final String PASSWORD = "password"; // Neo4j password
    private static Driver driver;
    private static DatabaseUtil instance;

    // Private constructor to prevent external instantiation
    private DatabaseUtil() {
        // Initialize the database connection
        initializeDatabase();
    }

    // Method to get the singleton instance
    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            driver = GraphDatabase.driver(URI, AuthTokens.basic(USERNAME, PASSWORD));
            System.out.println("Connected to Neo4j database.");
        } catch (Exception e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public Session getSession() {
        if (driver == null) {
            initializeDatabase();
        }
        return driver.session();
    }

    public void closeDatabase() {
        if (driver != null) {
            driver.close();
            System.out.println("Database connection closed.");
        }
    }

    public List<Product> getAllTheProduct()
    {
        List<Product> productList = new ArrayList<>();

        try (Session session = getSession()) {
            Result result = session.run("MATCH (p:Product) RETURN p");

            while (result.hasNext()) {
                Record record = result.next();
                Node userNode = record.get("p").asNode();

                // Retrieve user properties from the node
                String productName = userNode.get("productName").asString();
                String category = userNode.get("category").asString();
                String sellingPrice = userNode.get("sellingPrice").asString();
                String image = userNode.get("image").asString();
                String stockQuantity = userNode.get("stockQuantity").asString();
                String description = userNode.get("description").asString();
                String costPrice = userNode.get("costPrice").asString();
                String minimumStockLevel = userNode.get("minimumStockLevel").asString();
                String unitOfMeasure = userNode.get("unitOfMeasure").asString();
                String dateAdded = userNode.get("dateAdded").asString();
                String lastUpdated = userNode.get("lastUpdated").asString();
                String productID = userNode.get("productID").asString();

                // Create and return a Product object
                var product = new Product(productName, category, sellingPrice, image, stockQuantity, description, costPrice, minimumStockLevel, unitOfMeasure, dateAdded, lastUpdated, productID);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;


    }


    public List<User> getAllTheUsers()
    {
        List<User> userList = new ArrayList<>();

        try (Session session = getSession()) {
            Result result = session.run("MATCH (u:User) RETURN u");

            while (result.hasNext()) {
                Record record = result.next();
                Node userNode = record.get("u").asNode();

                // Retrieve user properties from the node
                String username = userNode.get("username").asString();
                String role = userNode.get("role").asString();
                String password = userNode.get("password").asString();
                String userId = userNode.get("userId").asString();

                // Create and return a User object
                var user = new User(username, role, password, userId);
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;



    }


    public User getUserByUsername(String uName) {
        try (var session = getSession())
        {
            var result = session.run("MATCH (u:User {username: $username}) return (u)", Values.parameters("username", uName));
            if (result.hasNext()) {
                Record record = result.next();
                Node userNode = record.get("u").asNode();

                // Retrieve user properties from the node
                String username = userNode.get("username").asString();
                String role = userNode.get("role").asString();
                String password = userNode.get("password").asString();
                String userId = userNode.get("userId").asString();

                // Create and return a User object
                return new User(username, role, password, userId);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addUser(User newUser)
    {
        try (var session = getSession())
        {
            session.run("CREATE (:User {username: $username, role: $role, password: $password, userId: $userId})",
                    Values.parameters("username", newUser.getUsername(),
                            "role", newUser.getRole(),
                            "password", newUser.getPassword(),
                            "userId", newUser.getUserId()));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public List<Customer> getAllTheCustomers() {
        List<Customer> customerList = new ArrayList<>();

        try (Session session = getSession()) {
            Result result = session.run("MATCH (c:Customer) RETURN c");

            while (result.hasNext()) {
                Record record = result.next();
                Node customerNode = record.get("c").asNode();

                String customerID = customerNode.get("customerID").asString();
                String customerName = customerNode.get("customerName").asString();
                String contactInformation = customerNode.get("contactInformation").asString();

                var customer = new Customer(customerID, customerName, contactInformation);
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;

    }

    public List<Supplier> getAllTheSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();

        try (Session session = getSession()) {
            Result result = session.run("MATCH (c:Supplier) RETURN c");

            while (result.hasNext()) {
                Record record = result.next();
                Node supplierNode = record.get("c").asNode();

                String supplierID = supplierNode.get("supplierID").asString();
                String supplierName = supplierNode.get("supplierName").asString();
                String contactInformation = supplierNode.get("contactInformation").asString();

                var supplier = new Supplier(supplierID, supplierName, contactInformation);
                supplierList.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierList;
    }

    public List<Purchase> getAllThePurchases() {
        List<Purchase> purchaseList = new ArrayList<>();
        try (Session session = getSession())
        {
            Result result = session.run(
                    "MATCH (p:Purchase)-[:FROM_SUPPLIER]->(s:Supplier),\n" +
                    "      (p)-[:THE_PRODUCT]->(pr:Product),\n" +
                    "      (p)-[:MADE_BY_USER]->(u:User)\n" +
                    "      RETURN p.purchaseId AS purchaseId,\n" +
                    "      p.purchaseDate AS purchaseDate,\n" +
                    "      p.deliveryDate AS deliveryDate,\n" +
                    "      p.quantity AS quantity,\n" +
                    "      s.supplierID AS supplierID,\n" +
                    "      pr.productID AS productID,\n" +
                    "      u.userId AS userId\n");

            while (result.hasNext())
            {
                Record record = result.next();

                // Extract properties from the record
                String purchaseId = record.get("purchaseId").asString();
                String purchaseDate = record.get("purchaseDate").asString();
                String deliveryDate = record.get("deliveryDate").asString();
                String quantity = record.get("quantity").asString();
                String supplierId = record.get("supplierID").asString();
                String productId = record.get("productID").asString();
                String userId = record.get("userId").asString();

                // Create a Purchase object and add it to the list
                Purchase purchase = new Purchase(
                        purchaseId,
                        purchaseDate,
                        deliveryDate,
                        quantity,
                        supplierId,
                        productId,
                        userId
                );

                purchaseList.add(purchase);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return purchaseList;



    }

    public List<Transaction> getAllTheTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        try (Session session = getSession())
        {
            Result result = session.run(
                    "MATCH (t:Transaction)-[:MADE_PURCHASE]->(c:Customer),\n" +
                            "(t)-[:OF_PRODUCT]->(p:Product)\n" +
                            "RETURN t.date AS date,\n" +
                            "t.discountsApplied AS discountsApplied,\n" +
                            "t.totalCost AS totalCost,\n" +
                            "t.transactionId AS transactionId,\n" +
                            "c.customerID AS customerID,\n" +
                            "p.productID AS productID\n");

            while (result.hasNext())
            {
                Record record = result.next();

                // Extract properties from the record
                String transactionID = record.get("transactionId").asString();
                String date = record.get("date").asString();
                String totalCost = record.get("totalCost").asString();
                String customerID = record.get("customerID").asString();
                String productID = record.get("productID").asString();
                String discountsApplied = record.get("discountsApplied").asString();



                // Create a Purchase object and add it to the list
                Transaction transaction = new Transaction(
                        transactionID,
                        date,
                        totalCost,
                        customerID,
                        productID,
                        discountsApplied
                );

                transactionList.add(transaction);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return transactionList;

    }

    public List<Product> searchProducts(String searchTerm) {
        List<Product> productList = new ArrayList<>();
        try (Session session = getSession()) {
            String query = "MATCH (p:Product) WHERE p.productName =~ '(?i).*" + searchTerm + ".*' OR p.category =~ '(?i).*" + searchTerm + ".*' OR p.description =~ '(?i).*" + searchTerm + ".*' OR p.unitOfMeasure =~ '(?i).*" + searchTerm + ".*' OR p.costPrice =~ '(?i).*" + searchTerm + ".*' OR p.sellingPrice =~ '(?i).*" + searchTerm + ".*' OR p.stockQuantity =~ '(?i).*" + searchTerm + ".*' OR p.minimumStockLevel =~ '(?i).*" + searchTerm + ".*' RETURN p";
            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                Node productNode = record.get("p").asNode();

                // Extract properties directly and create Product object
                String productName = productNode.get("productName").asString();
                String category = productNode.get("category").asString();
                String description = productNode.get("description").asString();
                String unitOfMeasure = productNode.get("unitOfMeasure").asString();
                String image = productNode.get("image").asString();
                String costPrice = productNode.get("costPrice").asString();
                String sellingPrice = productNode.get("sellingPrice").asString();
                String stockQuantity = productNode.get("stockQuantity").asString();
                String minimumStockLevel = productNode.get("minimumStockLevel").asString();
                String dateAdded = productNode.get("dateAdded").asString();
                String lastUpdated = productNode.get("lastUpdated").asString();
                String productID = productNode.get("productID").asString();

                // Create Product object and add to the list
                Product product = new Product(
                        productName,
                        category,
                        sellingPrice,
                        image,
                        stockQuantity,
                        description,
                        costPrice,
                        minimumStockLevel,
                        unitOfMeasure,
                        dateAdded,
                        lastUpdated,
                        productID



                );


                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }


    public List<Customer> searchCustomers(String searchTerm) {
        List<Customer> customerList = new ArrayList<>();
        try (Session session = getSession()) {
            String query = "MATCH (c:Customer) WHERE c.customerName =~ '(?i).*" + searchTerm + ".*' OR c.contactInformation =~ '(?i).*" + searchTerm + ".*' OR c.customerID =~ '(?i).*" + searchTerm + ".*' RETURN c";
            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                Node customerNode = record.get("c").asNode();

                // Extract properties directly and create Customer object
                String customerID = customerNode.get("customerID").asString();
                String customerName = customerNode.get("customerName").asString();
                String contactInformation = customerNode.get("contactInformation").asString();

                // Create Customer object and add to the list
                Customer customer = new Customer(
                        customerID,
                        customerName,
                        contactInformation
                );
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;

    }

    public List<Transaction> searchTransactions(String searchTerm) {
        List<Transaction> transactionList = new ArrayList<>();
        try (Session session = getSession()) {
            String query = "MATCH (transaction:Transaction)-[:MADE_PURCHASE]->(customer:Customer), "
                    + "(transaction)-[:OF_PRODUCT]->(product:Product) "
                    + "WHERE transaction.date =~ '(?i).*" + searchTerm + ".*' OR "
                    + "transaction.discountsApplied =~ '(?i).*" + searchTerm + ".*' OR "
                    + "transaction.totalCost =~ '(?i).*" + searchTerm + ".*' OR "
                    + "transaction.transactionId =~ '(?i).*" + searchTerm + ".*' OR "
                    + "customer.customerID =~ '(?i).*" + searchTerm + ".*' OR "
                    + "product.productID =~ '(?i).*" + searchTerm + ".*' RETURN transaction, customer, product";
            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                Node transactionNode = record.get("transaction").asNode();
                Node customerNode = record.get("customer").asNode();
                Node productNode = record.get("product").asNode();

                // Extract properties directly and create Transaction object
                String transactionID = transactionNode.get("transactionId").asString();
                String date = transactionNode.get("date").asString();
                String totalCost = transactionNode.get("totalCost").asString();
                String discountsApplied = transactionNode.get("discountsApplied").asString();
                String customerID = customerNode.get("customerID").asString();
                String productID = productNode.get("productID").asString();

                // Create Transaction object and add to the list
                Transaction transaction = new Transaction(
                        transactionID,
                        date,
                        totalCost,
                        customerID,
                        productID,
                        discountsApplied
                );
                transactionList.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionList;
    }



    public List<Purchase> searchPurchases(String searchTerm) {
        List<Purchase> purchaseList = new ArrayList<>();

        try (Session session = getSession()) {
            String query = "MATCH (purchase:Purchase)-[:FROM_SUPPLIER]->(supplier:Supplier), "
                    + "(purchase)-[:MADE_BY_USER]->(user:User), "
                    + "(purchase)-[:THE_PRODUCT]->(product:Product) "
                    + "WHERE purchase.purchaseDate =~ '(?i).*" + searchTerm + ".*' OR "
                    + "purchase.deliveryDate =~ '(?i).*" + searchTerm + ".*' OR "
                    + "purchase.purchaseId =~ '(?i).*" + searchTerm + ".*' OR "
                    + "purchase.quantity =~ '(?i).*" + searchTerm + ".*' OR "
                    + "supplier.supplierID =~ '(?i).*" + searchTerm + ".*' OR "
                    + "product.productID =~ '(?i).*" + searchTerm + ".*' OR "
                    + "user.userId =~ '(?i).*" + searchTerm + ".*' RETURN purchase, supplier, user, product";

            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                Node purchaseNode = record.get("purchase").asNode();
                Node supplierNode = record.get("supplier").asNode();
                Node userNode = record.get("user").asNode();
                Node productNode = record.get("product").asNode();

                // Extract properties directly and create Purchase object
                String purchaseID = purchaseNode.get("purchaseId").asString();
                String purchaseDate = purchaseNode.get("purchaseDate").asString();
                String deliveryDate = purchaseNode.get("deliveryDate").asString();
                String quantity = purchaseNode.get("quantity").asString();
                String supplierID = supplierNode.get("supplierID").asString();
                String productID = productNode.get("productID").asString();
                String userID = userNode.get("userId").asString();


                // Create Purchase object and add to the list
                Purchase purchase = new Purchase(
                        purchaseID,
                        purchaseDate,
                        deliveryDate,
                        quantity,
                        supplierID,
                        productID,
                        userID
                );
                purchaseList.add(purchase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchaseList;
    }




    public List<User> searchUsers(String searchTerm) {
        List<User> userList = new ArrayList<>();
        try (Session session = getSession()) {
            String query = "MATCH (u:User) WHERE u.username =~ '(?i).*" + searchTerm + ".*' OR u.role =~ '(?i).*" + searchTerm + ".*' OR u.userId =~ '(?i).*" + searchTerm + ".*' RETURN u";
            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                Node userNode = record.get("u").asNode();

                // Extract properties directly and create User object
                String username = userNode.get("username").asString();
                String role = userNode.get("role").asString();
                String password = userNode.get("password").asString();
                String userId = userNode.get("userId").asString();

                // Create User object and add to the list
                User user = new User(
                        username,
                        role,
                        password,
                        userId
                );
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }




    public List<Supplier> searchSuppliers(String searchTerm) {
        List<Supplier> supplierList = new ArrayList<>();
        try (Session session = getSession()) {
            String query = "MATCH (s:Supplier) WHERE s.supplierName =~ '(?i).*" + searchTerm + ".*' OR s.contactInformation =~ '(?i).*" + searchTerm + ".*' OR s.supplierID =~ '(?i).*" + searchTerm + ".*' RETURN s";
            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                Node supplierNode = record.get("s").asNode();

                // Extract properties directly and create Supplier object
                String supplierID = supplierNode.get("supplierID").asString();
                String supplierName = supplierNode.get("supplierName").asString();
                String contactInformation = supplierNode.get("contactInformation").asString();

                // Create Supplier object and add to the list
                Supplier supplier = new Supplier(
                        supplierID,
                        supplierName,
                        contactInformation
                );
                supplierList.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierList;
    }









}

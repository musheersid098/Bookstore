package coe528Final;
/**
 *
 * @author musheer siddiqui
 * @author mihir patel
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class GUI extends Application {
    private Stage primaryStage;
    private BookStore bookStore = new BookStore();
    private Owner currentOwner =  new Owner();
    private Customer currentCustomer;
    private TableView<Book> bookTable;
    private TableView<Customer> customerTable;
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    private ArrayList<Customer> selectedCustomers = new ArrayList<>();
 
    @Override // Overrides the start method in the Application class
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    showLoginScreen(); //Default App Screen is Login Screen (used by Owner and Customer)
    primaryStage.setTitle("JMM BookStore"); // Set the stage title
    primaryStage.show(); // Display the stage
  }

  //login screen
private void showLoginScreen(){
    Label loginWelcomeMessage = new Label ("Welcome to the JMM Bookstore!");
    Label usernameLabel = new Label ("Username: ");
    TextField usernameField = new TextField();
    usernameField.setMaxWidth(150);
    Label passwordLabel = new Label ("Password: ");
    TextField passwordField = new TextField();
    passwordField.setMaxWidth(150);
    Button loginButton = new Button("Login");
    Label messageLabel = new Label(); 
    
    loginButton.setOnAction(e-> {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        String role = authenticate(username, password);
        if(role.equals("Customer")){
            showCustomerStartScreen();
        }else if (role.equals("Owner")){
            showOwnerStartScreen();
        }else{
            messageLabel.setText("Incorrect username or password, try again");
        }
    });
    
    VBox loginLayout = new VBox(50, loginWelcomeMessage, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, messageLabel);
    loginLayout.setStyle ("-fx-padding: 20; -fx-alignment: center;");
    primaryStage.setScene(new Scene(loginLayout, 900, 700)); 
}

//login authenticator
private String authenticate(String username, String password) {
     if (username.equals(currentOwner.getUsername()) && password.equals(currentOwner.getPassword())) {
        return "Owner";
    }
     for (Customer customer: currentOwner.getCustomers()){
         if(customer.getUsername().equals(username) && customer.getPassword().equals(password)){
             currentCustomer = customer;
             return "Customer";
         }
     }
    return "invalid";
}

//customer start screen
private void showCustomerStartScreen(){
    Label welcomeLabel = new Label("Hello, " + currentCustomer.getUsername()+ ". You have " + currentCustomer.getPoints() + " points (" + currentCustomer.getStatus() + ")");
    bookTable = new TableView<>();
    
    TableColumn<Book, String> titleColumn = new TableColumn<>("Book Title");
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
    
    TableColumn<Book, Double> priceColumn = new TableColumn <> ("Book Price");
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
   
    TableColumn<Book, Void> selectColumn = new TableColumn<> ("Select");
    selectColumn.setCellFactory(col -> new TableCell<>(){
        private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    if (checkBox.isSelected()) {
                        selectedBooks.add(book);
                        System.out.println("Book added: " + book.getBookTitle()+ ",Price: "+ book.getBookPrice());

                    } else {
                        selectedBooks.remove(book);
                        System.out.println("Book removed: " + book.getBookTitle());

                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                }
            }
    });
    bookTable.getColumns().addAll(titleColumn,priceColumn, selectColumn);
    ObservableList<Book> observableBooks = FXCollections.observableArrayList(bookStore.getBooks());
    bookTable.setItems(observableBooks);

  
        
    Button buyButton = new Button("Buy");
    buyButton.setOnAction(e -> {
        if (selectedBooks.isEmpty()){
        showAlert("Error", "No book selected");
        return;
        }
        double cost = 0.0;
        for (Book book: selectedBooks){
            currentCustomer.buyABook(book, currentOwner);
            bookStore.removeABook(book.getBookTitle());
            cost += book.getBookPrice();

        }
        
        welcomeLabel.setText("Hello, " + currentCustomer.getUsername()+ ". You have " + currentCustomer.getPoints() + " points (" + currentCustomer.getStatus() + ")");
        checkoutScreen(cost);
        
        selectedBooks.clear();
        bookTable.setItems(observableBooks);
    });
    
    Button redeemAndBuyButton = new Button ("Redeem and Buy");
        redeemAndBuyButton.setOnAction(e -> {
        double amountOwed = 0;
        if (selectedBooks.isEmpty()){
        showAlert("Error", "No book selected");
        return;
        }
        
        for (Book book: selectedBooks){
            amountOwed += currentCustomer.redeemPointsAndBuy(book, currentOwner);
            bookStore.removeABook(book.getBookTitle());
        }
        
        welcomeLabel.setText("Hello, " + currentCustomer.getUsername()+ ". You have " + currentCustomer.getPoints() + " points (" + currentCustomer.getStatus() + ")");
        checkoutScreen(amountOwed);
        
        selectedBooks.clear();
        bookTable.setItems(observableBooks);
    });
        
    Button logoutButton = new Button("Logout");
    logoutButton.setOnAction(e -> showLoginScreen());
    

    VBox customerLayout = new VBox(50, welcomeLabel, bookTable, buyButton, redeemAndBuyButton, logoutButton);
    customerLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

    primaryStage.setScene(new Scene(customerLayout, 900, 700));
}

//checkout screen
public void checkoutScreen(double amountOwed){
        currentOwner.writeCustomers();
        Label costLabel = new Label ("Total Cost: $" + amountOwed);
        Label pointsStatusLabel = new Label("Points: "+ currentCustomer.getPoints() +", Status: " + currentCustomer.getStatus());

        selectedBooks.clear();
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> showLoginScreen());
        
        VBox checkoutLayout = new VBox(50, costLabel, pointsStatusLabel, logoutButton);
        checkoutLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        primaryStage.setScene(new Scene(checkoutLayout, 900, 700));
    
}

//owner start screen
public void showOwnerStartScreen(){
      Label welcomeLabel = new Label("Hello Owner!");
      Button manageCustomersButton = new Button("Manage Customers");
      Button manageBooksButton = new Button("Manage Books");
      Button logoutButton = new Button("Logout");
      manageCustomersButton.setOnAction(e -> showOwnerCustomerScreen());
      manageBooksButton.setOnAction (e -> showOwnerBooksScreen());
      logoutButton.setOnAction(e -> showLoginScreen());

    VBox ownerLayout = new VBox(50, welcomeLabel, manageCustomersButton, manageBooksButton, logoutButton);
    ownerLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

    primaryStage.setScene(new Scene(ownerLayout, 900, 700));
}

//error alert
public void showAlert(String title, String message){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

//owner customer screen
public void showOwnerCustomerScreen(){
    Label titleLabel = new Label("Manage Customers");
    customerTable = new TableView<>();

    TableColumn<Customer, String> usernameColumn = new TableColumn<>("Customer Username");
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

    TableColumn<Customer, String> passwordColumn = new TableColumn<>("Customer Password");
    passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    
    TableColumn<Customer, Integer> pointsColumn = new TableColumn<> ("Customer Points");
    pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
    
    TableColumn<Customer, Void> selectColumn = new TableColumn<>("Select");
    selectColumn.setCellFactory(col -> new TableCell<>(){
        private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    if (checkBox.isSelected()) {
                        selectedCustomers.add(customer);
                    } else {
                        selectedCustomers.remove(customer);
                        
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                }
            }
    });
       // Add columns to table
    customerTable.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn, selectColumn);

    ObservableList<Customer> observableCustomers = FXCollections.observableArrayList(currentOwner.getCustomers());
    customerTable.setItems(observableCustomers);

    TextField usernameField = new TextField();
    usernameField.setPromptText("Enter Customer Username");

    TextField passwordField = new TextField();
    passwordField.setPromptText("Enter Customer Password");
    
    Button addButton = new Button("Add");
    addButton.setOnAction(e -> {
        String customerUsername = usernameField.getText();
        String customerPassword = passwordField.getText();
        if (!customerUsername.isEmpty() && !customerPassword.isEmpty()) {
            currentOwner.addACustomer(customerUsername, customerPassword);
            observableCustomers.setAll(currentOwner.getCustomers());
            customerTable.setItems(observableCustomers);
            usernameField.clear();
            passwordField.clear();
                } else {
                    showAlert("Error", "This username already exists!");
                }
    });
    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(e -> {
        if (selectedCustomers.isEmpty()) { 
            showAlert("Error", "No customers selected"); 
            return;
        }
        for (Customer customer: selectedCustomers){ 
            if(currentOwner.removeACustomer(customer.getUsername())){ 
                System.out.println("Customer removed: "+ customer.getUsername()); 
            }
        }
            observableCustomers.removeAll(selectedCustomers); 
            selectedCustomers.clear(); 
    });
    Button backButton = new Button ("Back");
    backButton.setOnAction(e -> showOwnerStartScreen());
    
    VBox layout = new VBox(50, titleLabel, customerTable, usernameField, passwordField, addButton, deleteButton, backButton);
    layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
    primaryStage.setScene(new Scene(layout, 900, 700));
}

//owner books screen
private void showOwnerBooksScreen(){
    Label titleLabel = new Label("Manage Books");
    bookTable = new TableView<>();

    // Setup table columns
    TableColumn<Book, String> titleColumn = new TableColumn<>("Book Title");
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

    TableColumn<Book, Double> priceColumn = new TableColumn<>("Book Price");
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));

    TableColumn<Book, Void> selectColumn = new TableColumn<>("Select");
    selectColumn.setCellFactory(col -> new TableCell<>(){
        private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    if (checkBox.isSelected()) {
                        selectedBooks.add(book);
                    } else {
                        selectedBooks.remove(book);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                }
            }
    });

    bookTable.getColumns().addAll(titleColumn, priceColumn, selectColumn);

    ObservableList<Book> observableBooks = FXCollections.observableArrayList(bookStore.getBooks());
    bookTable.setItems(observableBooks);

    TextField nameField = new TextField();
    nameField.setPromptText("Enter Book Name");

    TextField priceField = new TextField();
    priceField.setPromptText("Enter Price");

    Button addButton = new Button("Add");
    addButton.setOnAction(e -> {
        String bookName = nameField.getText();
        String priceText = priceField.getText();
        if (!bookName.isEmpty() && !priceText.isEmpty()) {
            try {
                double bookPrice = Double.parseDouble(priceText);
                if (bookStore.addABook(bookName, bookPrice)) {
                    observableBooks.setAll(bookStore.getBooks());
                    bookTable.setItems(observableBooks);
                    nameField.clear();
                    priceField.clear();
                } else {
                    showAlert("Error", "This book already exists!");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid price.");
            }
        } else {
            showAlert("Error", "Both fields must be filled");
        }
    });

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> showOwnerStartScreen());

    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(e -> {
        if (selectedBooks.isEmpty()) {
            showAlert("Error", "No book selected");
            return;
        }
        for (Book book: selectedBooks){
            if(bookStore.removeABook(book.getBookTitle())){
                System.out.println("Book(s) removed: "+ book.getBookTitle());
            }
        }
            observableBooks.removeAll(selectedBooks);
            selectedBooks.clear();
    });

    VBox addBookBox = new VBox(50, nameField, priceField, addButton);
    addBookBox.setStyle("-fx-alignment: center; -fx-padding: 10;");
    
    VBox deleteBookBox = new VBox(50, deleteButton, backButton);
    deleteBookBox.setStyle("-fx-alignment: center; -fx-padding: 10;");
    
    VBox layout = new VBox(50, titleLabel, bookTable, addBookBox, deleteBookBox);
    layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
    
    primaryStage.setScene(new Scene(layout, 900, 700));
}

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
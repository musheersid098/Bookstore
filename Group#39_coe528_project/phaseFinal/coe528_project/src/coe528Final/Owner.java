package coe528Final;

import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
/**
 *
 * @author Mihir Patel
 */
public class Owner extends User {
    
    private ArrayList<Customer> customers = new ArrayList<>();
    private final String fileName = "customers.txt";
    
    
    //constructor, reads customers.txt
    public Owner(){
        super("Owner", "admin123");
        customers=  new ArrayList<>();
        readCustomers();
    }
    
    //adds book to bookstore
    public void addABook(String title, double price, BookStore bookstore) {
        bookstore.addABook(title, price);
    }
    
    //removes book from bookstore
     public void removeABook(String title, double price, BookStore bookstore) {
        bookstore.removeABook(title);
    }

     //adds customer to customers.txt
    public boolean addACustomer(String username, String password) {
        for (Customer customer: customers){
            if (customer.getUsername().equals(username)){
                return false;
            }
        }
        
        Customer newCustomer = new Customer(username, password, 0, new Silver());
        customers.add(newCustomer);
        writeCustomers();
        return true;
    }
    
    //removes customer from customers.txt
    public boolean removeACustomer(String username) {
        Customer customerToRemove = null;
        for (Customer customer : customers){
            if(customer.getUsername().equals(username)){
                customerToRemove = customer;
                break;
            }
        }
if (customerToRemove != null){
    customers.remove(customerToRemove);
    writeCustomers();
    return true;
}
return false;
    }
    
    //gets all customers in arraylist
    public ArrayList<Customer> getCustomers(){
        return customers;
    }

    
 //reads customer.txt
public ArrayList<Customer> readCustomers(){
    ArrayList<Customer> customerList = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
        String line;
        while ((line = reader.readLine()) != null){
            String[] customerData = line.split(",");
            String username = customerData[0];
            String password = customerData[1];
            int points  = Integer.parseInt(customerData[2]);
            
            CustomerStatus status = points >= 1000 ? new Gold(): new Silver();
            customers.add(new Customer(username, password, points, status));
        }
    }catch (IOException e){
        System.out.println("Error reading customer file.");
    }
    return customerList;
}

//writes to customers.txt
public void writeCustomers(){
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
        for (Customer customer: customers){
            writer.write(customer.getUsername() + "," + customer.getPassword() + "," + customer.getPoints());
            writer.newLine();
        }
    } catch (IOException e){
        System.out.println("Error writing to customer file.");
    }
}

//username getter
public String getUsername(){
    return this.username;
}


//password getter
public String getPassword(){
    return this.password;
}
}


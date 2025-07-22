package coe528Final;

/**
 *
 * @author Jaswant Hemanth Kumar Rekha
 */

public class Book {
    protected String title;
    protected double price;

    //Constructor for instance variables
    public Book(String title, double price) {
        this.title = title;
        this.price = price;
    }

    //getter for title
    public String getBookTitle() {
        return title;
    }
    
    //setter for price
    public double getBookPrice() {
        return price;
    }
}


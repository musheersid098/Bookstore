package coe528Final;

/**
 *
 * @author Jaswant Hemanth Kumar Rekha
 */

import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class BookStore {
    private ArrayList<Book> books = new ArrayList<>();
    private final String fileName = "books.txt";
    
    //constructor for variables and arrays
    public BookStore(){
        books = new ArrayList<>();
        readBooks();
    }
    
    //adding books and rewrites books.txt
    public boolean addABook(String title, double price) {
        for (Book book : books) {
            if (book.getBookTitle().equals(title)) {
                return false;
            }
        }
        Book newBook = new Book(title,price);
        books.add(newBook);
        writeBooks();
        return true;
    }
    
    //removes the book and rewrites books.txt
    public boolean removeABook(String bookTitle) {
       Book bookToRemove = null;
        for (Book book: books){
           if (book.getBookTitle().equals(bookTitle)){
               bookToRemove = book;
               break;
           }
       }
       if (bookToRemove != null){
           books.remove(bookToRemove);
           writeBooks();
           return true;
       }
       return false;
    }
    
    
    //removes book from books.txt
    public void buyABook(Book book) {
        books.remove(book);
        writeBooks();
    }

    //removes book from books.txt
    public void redeemPointsAndBuy(Book book) {
        books.remove(book);
        writeBooks();
    }

    //returns arraylist of all books in store
    public ArrayList<Book> getBooks() {    
return new ArrayList<>(books);
    }
    
    //reads the books.txt file
    private void readBooks(){
        try (BufferedReader reader= new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = reader.readLine())!= null){
                String[] bookData = line.split(",");
                String title = bookData[0];
                double price=  Double.parseDouble(bookData[1]);
                books.add(new Book(title,price));
            }
        } catch (IOException e){
            System.out.println("Bookstore is empty");
        }
}
    //writes to the books.txt file
    private void writeBooks(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            for (Book book: books){
                writer.write(book.getBookTitle()+"," + book.getBookPrice());
                writer.newLine();
            }
        } catch (IOException e){
        System.out.println("Book NOT saved");
    }
    }
}

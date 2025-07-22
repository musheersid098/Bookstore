
package coe528Final;

/**
 *
 * @author Mihir Patel
 */
public class User {
    
    protected String password;
    public String username;
    
    //constructor for username and password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
       
    }
    
    //login function ensures the input is same as the registered username and passwords
    public boolean login(String username, String password){
        return this.username.equals(username) && this.password.equals(password);
    }
    
    public void logout(){
        System.out.println("User " + username + " is now logged out");
    }
    
}



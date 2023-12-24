import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        String uName;
        String password;
        String title;
        String description;
        System.out.println("Welcome to To Do List from CLI");

        Scanner sc = new Scanner(System.in);
        System.out.print("User Name : ");
        uName = sc.nextLine();

        System.out.print("Enter password : ");
        password = sc.nextLine();

        if (uName.equals("admin") && password.equals("admin")) {
            System.out.println("Welcome to the dashboard");
            System.out.println("type 'add' to add");
            System.out.println("type 'show' to show list");

            String value;
            System.out.print("Enter What you want to do : ");
            value = sc.nextLine();

            if (value.equals("add")) {
                System.out.print("Title : ");
                title = sc.nextLine();
                System.out.println("Description : ");
                description = sc.nextLine();
                System.out.println("Title: " + title + " Description: " + description);

                // Connect to the database and save the Todo information
                saveTodoToDatabase(title, description);
            } else if (value.equals("show")) {
                displaySavedTodos();
            }

        } else {
            System.out.println("Incorrect username or password. Please try again!!");
        }

        sc.close();
    }

    private static void saveTodoToDatabase(String title, String description) {
        // Database connection parameters
        String jdbcUrl = "jdbc:mysql://localhost:3306/todolist";
        String username = "root";
        String password = "";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // SQL query to insert Todo information
            String sql = "INSERT INTO todo (title, description) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, description);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Todo information saved to the database successfully.");
                } else {
                    System.out.println("Failed to save Todo information to the database.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displaySavedTodos() {
       
        String jdbcUrl = "jdbc:mysql://localhost:3306/todolist";
        String username = "root";
        String password = "";

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // SQL query to select all Todo records
            String sql = "SELECT title, description FROM todo";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
               
                ResultSet resultSet = preparedStatement.executeQuery();

                // Display the results
                int counter = 1;

                while (resultSet.next()) {
                    String savedTitle = resultSet.getString("title");
                    String savedDescription = resultSet.getString("description");

                    System.out.println(counter + ". Title: " + savedTitle);
                    System.out.println("   Description: " + savedDescription);
                    System.out.println();

                    counter++;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}

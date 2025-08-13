/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


// DATABASE NAME: library
// for login in this gui app only => username: admin , password: admin








package library;


import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.proteanit.sql.DbUtils;

public class LIBRARY {

    // ========= DB CONNECTION =========
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mysql?user=root&password=Lithiyasree12@"
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "DB Connection Failed");
            return null;
        }
    }

    // ========= CREATE / RESET DATABASE =========
    public static void create() {
        try (Connection connection = connect();
             Statement stmt = connection.createStatement()) {

            if (connection == null) return;

            // Drop existing
            ResultSet catalogs = connection.getMetaData().getCatalogs();
            while (catalogs.next()) {
                if ("library".equalsIgnoreCase(catalogs.getString(1))) {
                    stmt.executeUpdate("DROP DATABASE library");
                }
            }
            catalogs.close();

            stmt.executeUpdate("CREATE DATABASE library");
            stmt.executeUpdate("USE library");

            // Users table
            stmt.executeUpdate(
                "CREATE TABLE users (" +
                "UID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "Username VARCHAR(30), Password VARCHAR(30), Admin BOOLEAN)"
            );
            stmt.executeUpdate(
                "INSERT INTO users (Username, Password, Admin) VALUES " +
                "('admin','admin',TRUE)," +
                "('john','john123',FALSE)," +
                "('sarah','sarah123',FALSE)"
            );

            // Books table
            stmt.executeUpdate(
                "CREATE TABLE books (" +
                "BID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "BName VARCHAR(50), Genre VARCHAR(20), Price INT)"
            );
            stmt.executeUpdate(
                "INSERT INTO books (BName, Genre, Price) VALUES " +
                "('War and Peace','Mystery',200)," +
                "('The Guest Book','Fiction',300)," +
                "('The Perfect Murder','Mystery',150)," +
                "('Accidental Presidents','Biography',250)," +
                "('The Wicked King','Fiction',350)"
            );

            // Issue table
            stmt.executeUpdate(
                "CREATE TABLE issue (" +
                "IID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "UID INT, BID INT," +
                "IssueDate VARCHAR(20), Period INT," +
                "ReturnDate VARCHAR(20), Fine INT)"
            );

            JOptionPane.showMessageDialog(null, "Database Created/Reset Successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Create/Reset failed: " + ex.getMessage());
        }
    }

    // ========= LOGIN =========
    public static void login() {
        JFrame f = new JFrame("Login");
        JLabel l1 = new JLabel("Username");
        JLabel l2 = new JLabel("Password");
        JTextField F_user = new JTextField();
        JPasswordField F_pass = new JPasswordField();
        JButton login_but = new JButton("Login");
        JButton create_but = new JButton("Create/Reset DB");

        l1.setBounds(30, 15, 100, 30);
        l2.setBounds(30, 50, 100, 30);
        F_user.setBounds(110, 15, 200, 30);
        F_pass.setBounds(110, 50, 200, 30);
        login_but.setBounds(130, 90, 80, 25);
        create_but.setBounds(100, 130, 150, 25);

        login_but.addActionListener(e -> {
            String username = F_user.getText().trim();
            String password = String.valueOf(F_pass.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter Username and Password");
                return;
            }
            try (Connection connection = connect();
                 Statement stmt = connection.createStatement()) {

                if (connection == null) return;

                stmt.executeUpdate("USE library");
                String st = "SELECT * FROM users WHERE Username='" + username +
                            "' AND Password='" + password + "'";
                ResultSet rs = stmt.executeQuery(st);
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Wrong Username/Password!");
                } else {
                    boolean isAdmin = rs.getBoolean("Admin");
                    int uid = rs.getInt("UID");
                    f.dispose();
                    if (isAdmin) adminMenu();
                    else userMenu(uid);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Login error: " + ex.getMessage());
            }
        });

        create_but.addActionListener(e -> create());

        f.add(l1); f.add(l2); f.add(F_user); f.add(F_pass);
        f.add(login_but); f.add(create_but);
        f.setSize(400, 220);
        f.setLayout(null);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ========= ADMIN MENU =========
    public static void adminMenu() {
        JFrame f = new JFrame("Admin Functions");
//        String[] btns = {"View Books", "View Users", "View Issued Books", "Issue Book",
//                         "Add User", "Add Book", "Return Book", "Create/Reset"};

        String[] btns = {"View Books", "View Users", "View Issued Books", "Issue Book",
                 "Add User", "Add Book", "Update User", "Delete User",
                 "Update Book", "Delete Book", "Return Book", "Create/Reset"};

        JButton[] buttons = new JButton[btns.length];
        for (int i = 0; i < btns.length; i++) {
            buttons[i] = new JButton(btns[i]);
            buttons[i].setBounds(20 + (i % 4) * 150, 30 + (i / 4) * 60, 130, 30);
            f.add(buttons[i]);
        }

        buttons[0].addActionListener(e -> viewTable("SELECT * FROM books", "Books Available"));
        buttons[1].addActionListener(e -> viewTable("SELECT * FROM users", "Users List"));
        buttons[2].addActionListener(e -> viewTable("SELECT * FROM issue", "Issued Books"));
        buttons[3].addActionListener(e -> issueBook());
        buttons[4].addActionListener(e -> addUser());
        buttons[5].addActionListener(e -> addBook());
//        buttons[6].addActionListener(e -> returnBook());
//        buttons[7].addActionListener(e -> create());
        buttons[6].addActionListener(e -> updateUser());
        buttons[7].addActionListener(e -> deleteUser());
        buttons[8].addActionListener(e -> updateBook());
        buttons[9].addActionListener(e -> deleteBook());
        buttons[10].addActionListener(e -> returnBook());
        buttons[11].addActionListener(e -> create());


//        f.setSize(650, 200);
        f.setSize(800, 300); // bigger now for more buttons

        f.setLayout(null);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ========= USER MENU =========
    public static void userMenu(int uid) {
        JFrame f = new JFrame("User Functions");
        JButton viewBooks = new JButton("View Books");
        JButton myBooks = new JButton("My Books");

        viewBooks.setBounds(20, 20, 120, 25);
        myBooks.setBounds(150, 20, 120, 25);

        viewBooks.addActionListener(e -> viewTable("SELECT * FROM books", "Books Available"));
        myBooks.addActionListener(e -> myBooks(uid));

        f.add(viewBooks); f.add(myBooks);
        f.setSize(300, 120);
        f.setLayout(null);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ========= VIEW TABLE =========
    public static void viewTable(String query, String title) {
        try (Connection connection = connect();
             Statement stmt = connection.createStatement()) {

            if (connection == null) return;

            stmt.executeUpdate("USE library");
            ResultSet rs = stmt.executeQuery(query);
            JTable table = new JTable();
            table.setModel(DbUtils.resultSetToTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table);

            JFrame frame = new JFrame(title);
            frame.add(scrollPane);
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);

            MouseAdapter closer = new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) frame.dispose();
                }
            };
            table.addMouseListener(closer);
            scrollPane.addMouseListener(closer);
            frame.getContentPane().addMouseListener(closer);

            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "View failed: " + ex.getMessage());
        }
    }

    // ========= MY BOOKS =========
    public static void myBooks(int uid) {
        String sql =
            "SELECT i.IID, i.UID, i.BID, i.IssueDate AS ISSUED_DATE, i.ReturnDate AS RETURN_DATE, " +
            "i.Period AS PERIOD, i.Fine AS FINE, b.BName AS bname, b.Genre AS genre, b.Price AS price " +
            "FROM issue i JOIN books b ON i.BID = b.BID WHERE i.UID = " + uid + " ORDER BY i.IID";
        viewTable(sql, "My Books");
        
     


    }
    
    

    // ========= ADD USER =========
    public static void addUser() {
        JTextField uname = new JTextField();
        JTextField pass = new JTextField();
        JCheckBox adminCheck = new JCheckBox("Admin?");
        Object[] msg = {"Username:", uname, "Password:", pass, adminCheck};

        if (JOptionPane.showConfirmDialog(null, msg, "Add User",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection connection = connect();
                 Statement stmt = connection.createStatement()) {

                if (connection == null) return;

                stmt.executeUpdate("USE library");
                stmt.executeUpdate(
                    "INSERT INTO users (Username, Password, Admin) VALUES ('" +
                    uname.getText().trim() + "','" + pass.getText().trim() + "'," +
                    adminCheck.isSelected() + ")"
                );
                JOptionPane.showMessageDialog(null, "User Added!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Add User failed: " + ex.getMessage());
            }
        }
    }

    // ========= ADD BOOK =========
    public static void addBook() {
        JTextField bname = new JTextField();
        JTextField genre = new JTextField();
        JTextField price = new JTextField();
        Object[] msg = {"Book Name:", bname, "Genre:", genre, "Price:", price};

        if (JOptionPane.showConfirmDialog(null, msg, "Add Book",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection connection = connect();
                 Statement stmt = connection.createStatement()) {

                if (connection == null) return;

                stmt.executeUpdate("USE library");
                int priceInt = Integer.parseInt(price.getText().trim());
                stmt.executeUpdate(
                    "INSERT INTO books (BName, Genre, Price) VALUES ('" +
                    bname.getText().trim() + "','" + genre.getText().trim() + "'," + priceInt + ")"
                );
                JOptionPane.showMessageDialog(null, "Book Added!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Add Book failed: " + ex.getMessage());
            }
        }
    }
    


    // ========= ISSUE BOOK =========
    public static void issueBook() {
        JTextField uid = new JTextField();
        JTextField bid = new JTextField();
        JTextField date = new JTextField();
        JTextField period = new JTextField();
        Object[] msg = {
            "User ID:", uid, "Book ID:", bid,
            "Issue Date (dd-MM-yyyy):", date, "Period (days):", period
        };

        if (JOptionPane.showConfirmDialog(null, msg, "Issue Book",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection connection = connect();
                 Statement stmt = connection.createStatement()) {

                if (connection == null) return;

                stmt.executeUpdate("USE library");
                int periodInt = Integer.parseInt(period.getText().trim());
                stmt.executeUpdate(
                    "INSERT INTO issue (UID, BID, IssueDate, Period, ReturnDate, Fine) " +
                    "VALUES (" + uid.getText().trim() + "," + bid.getText().trim() + ",'" +
                    date.getText().trim() + "'," + periodInt + ",'',0)"
                );
                JOptionPane.showMessageDialog(null, "Book Issued!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Issue Book failed: " + ex.getMessage());
            }
        }
    }

    // ========= RETURN BOOK =========
    public static void returnBook() {
        JTextField iid = new JTextField();
        JTextField retDate = new JTextField();
        Object[] msg = {"Issue ID:", iid, "Return Date (dd-MM-yyyy):", retDate};

        if (JOptionPane.showConfirmDialog(null, msg, "Return Book",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection connection = connect();
                 Statement stmt = connection.createStatement()) {

                if (connection == null) return;

                stmt.executeUpdate("USE library");
                ResultSet rs = stmt.executeQuery(
                    "SELECT IssueDate, Period FROM issue WHERE IID=" + iid.getText().trim()
                );
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Invalid Issue ID!");
                    return;
                }
                String issueDateStr = rs.getString("IssueDate");
                int period = rs.getInt("Period");
                rs.close();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date issueDate = sdf.parse(issueDateStr);
                Date returnDate = sdf.parse(retDate.getText().trim());

                long diffDays = (returnDate.getTime() - issueDate.getTime()) / (1000L * 60 * 60 * 24);
                long daysLate = diffDays - period;
                int fine = (daysLate > 0) ? (int) daysLate * 10 : 0;

                stmt.executeUpdate(
                    "UPDATE issue SET ReturnDate='" + retDate.getText().trim() +
                    "', Fine=" + fine + " WHERE IID=" + iid.getText().trim()
                );

                JOptionPane.showMessageDialog(null, "Fine: Rs. " + fine);
                JOptionPane.showMessageDialog(null, "Book Returned!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Return failed: " + ex.getMessage());
            }
        }
    }

    
    // ========= Delete Book =========
    public static void deleteBook() {
        String bid = JOptionPane.showInputDialog("Enter Book ID to Delete:");
        if (bid != null && !bid.trim().isEmpty()) {
            try (Connection connection = connect();
                 Statement stmt = connection.createStatement()) {
                if (connection == null) return;
                stmt.executeUpdate("USE library");
                int rows = stmt.executeUpdate("DELETE FROM books WHERE BID=" + bid.trim());
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Book Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Book ID not found!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Delete Book failed: " + ex.getMessage());
            }
        }
    }

    // ========= Update Book =========
    

    public static void updateBook() {
        String bid = JOptionPane.showInputDialog("Enter Book ID to Update:");
        if (bid == null || bid.trim().isEmpty()) return;

        try (Connection connection = connect()) {
            if (connection == null) return;
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("USE library");

            // 1. Fetch current book details
            ResultSet rsBook = stmt.executeQuery("SELECT BName, Genre, Price FROM books WHERE BID=" + bid.trim());
            if (!rsBook.next()) {
                JOptionPane.showMessageDialog(null, "Book ID not found!");
                return;
            }
            String currentName = rsBook.getString("BName");
            String currentGenre = rsBook.getString("Genre");
            int currentPrice = rsBook.getInt("Price");
            rsBook.close();

            // 2. Pre-fill the dialog with current values
            JTextField bname = new JTextField(currentName);
            JTextField genre = new JTextField(currentGenre);
            JTextField price = new JTextField(String.valueOf(currentPrice));

            Object[] msg = {
                "New Book Name (leave as is to keep):", bname,
                "New Genre (leave as is to keep):", genre,
                "New Price (leave as is to keep):", price
            };

            if (JOptionPane.showConfirmDialog(null, msg, "Update Book",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                // 3. If field left unchanged or empty, keep old value
                String newName = bname.getText().trim().isEmpty() ? currentName : bname.getText().trim();
                String newGenre = genre.getText().trim().isEmpty() ? currentGenre : genre.getText().trim();
                int newPrice;

                try {
                    newPrice = price.getText().trim().isEmpty()
                            ? currentPrice
                            : Integer.parseInt(price.getText().trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Price must be a valid integer");
                    return;
                }

                // 4. Update in DB
                String sql = "UPDATE books SET BName=?, Genre=?, Price=? WHERE BID=?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, newName);
                    ps.setString(2, newGenre);
                    ps.setInt(3, newPrice);
                    ps.setInt(4, Integer.parseInt(bid.trim()));
                    int rows = ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, rows > 0 ? "Book Updated!" : "Book ID not found!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update Book failed: " + ex.getMessage());
        }
    }

    
    // ========= DELETE USER =========
    public static void deleteUser() {
        String uid = JOptionPane.showInputDialog("Enter User ID to Delete:");
        if (uid != null && !uid.trim().isEmpty()) {
            try (Connection connection = connect();
                 Statement stmt = connection.createStatement()) {
                if (connection == null) return;
                stmt.executeUpdate("USE library");
                int rows = stmt.executeUpdate("DELETE FROM users WHERE UID=" + uid.trim());
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "User Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "User ID not found!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Delete User failed: " + ex.getMessage());
            }
        }
    }

    
    
    // ========= Update USER =========

    
    public static void updateUser() {
        String uid = JOptionPane.showInputDialog("Enter User ID to Update:");
        if (uid == null || uid.trim().isEmpty()) return;

        try (Connection connection = connect()) {
            if (connection == null) return;
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("USE library");

            // 1. Fetch existing user details
            ResultSet rsUser = stmt.executeQuery("SELECT Username, Password, Admin FROM users WHERE UID=" + uid.trim());
            if (!rsUser.next()) {
                JOptionPane.showMessageDialog(null, "User ID not found!");
                return;
            }
            String currentUsername = rsUser.getString("Username");
            String currentPassword = rsUser.getString("Password");
            boolean currentAdmin = rsUser.getBoolean("Admin");
            rsUser.close();

            JTextField uname = new JTextField(currentUsername);
            JTextField pass = new JTextField(currentPassword);
            JCheckBox adminCheck = new JCheckBox("Admin?", currentAdmin);
            Object[] msg = {"New Username (leave as is to keep):", uname,
                            "New Password (leave as is to keep):", pass,
                            adminCheck};

            if (JOptionPane.showConfirmDialog(null, msg, "Update User",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                // 2. If unchanged or blank, keep old value
                String newUsername = uname.getText().trim().isEmpty() ? currentUsername : uname.getText().trim();
                String newPassword = pass.getText().trim().isEmpty() ? currentPassword : pass.getText().trim();
                boolean newAdmin = adminCheck.isSelected();

                // 3. Update user table
                String sql = "UPDATE users SET Username=?, Password=?, Admin=? WHERE UID=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, newUsername);
                ps.setString(2, newPassword);
                ps.setBoolean(3, newAdmin);
                ps.setInt(4, Integer.parseInt(uid.trim()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "User Updated!");
            }

            // === Now update issued books if exist ===
            ResultSet rs = stmt.executeQuery("SELECT * FROM issue WHERE UID=" + uid.trim());
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "This user has no issued books.");
                return;
            }

            while (rs.next()) {
                int iid = rs.getInt("IID");
                JTextField bidField = new JTextField(String.valueOf(rs.getInt("BID")));
                JTextField issueDateField = new JTextField(rs.getString("IssueDate"));
                JTextField periodField = new JTextField(String.valueOf(rs.getInt("Period")));
                JTextField returnDateField = new JTextField(rs.getString("ReturnDate"));
                JTextField fineField = new JTextField(String.valueOf(rs.getInt("Fine")));

                Object[] bookMsg = {
                    "Book ID:", bidField,
                    "Issue Date (dd-MM-yyyy):", issueDateField,
                    "Period (days):", periodField,
                    "Return Date (dd-MM-yyyy):", returnDateField,
                    "Fine:", fineField
                };

                if (JOptionPane.showConfirmDialog(null, bookMsg,
                        "Update Issued Book (IID=" + iid + ")",
                        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                    String updateIssue = "UPDATE issue SET BID=?, IssueDate=?, Period=?, ReturnDate=?, Fine=? WHERE IID=?";
                    PreparedStatement psIssue = connection.prepareStatement(updateIssue);
                    psIssue.setInt(1, Integer.parseInt(bidField.getText().trim()));
                    psIssue.setString(2, issueDateField.getText().trim());
                    psIssue.setInt(3, Integer.parseInt(periodField.getText().trim()));
                    psIssue.setString(4, returnDateField.getText().trim());
                    psIssue.setInt(5, Integer.parseInt(fineField.getText().trim()));
                    psIssue.setInt(6, iid);
                    psIssue.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Issued book updated for IID " + iid);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update failed: " + ex.getMessage());
        }
    }

    
    
    // ========= MAIN =========
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LIBRARY::login);
    }
}

































//=========================FOR REFERENCE (IF NO CHANGE MADE " " REPLACED SO DON'T USE)=============================================



    // ========= Update Book =========


//public static void updateBook() {
//        String bid = JOptionPane.showInputDialog("Enter Book ID to Update:");
//        if (bid == null || bid.trim().isEmpty()) return;
//
//        JTextField bname = new JTextField();
//        JTextField genre = new JTextField();
//        JTextField price = new JTextField();
//        Object[] msg = {"New Book Name:", bname, "New Genre:", genre, "New Price:", price};
//
//        if (JOptionPane.showConfirmDialog(null, msg, "Update Book",
//                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
//            try {
//                int priceInt = Integer.parseInt(price.getText().trim());
//                try (Connection connection = connect()) {
//                    if (connection == null) return;
//                    try (Statement stmt = connection.createStatement()) {
//                        stmt.executeUpdate("USE library");
//                    }
//                    String sql = "UPDATE books SET BName=?, Genre=?, Price=? WHERE BID=?";
//                    try (PreparedStatement ps = connection.prepareStatement(sql)) {
//                        ps.setString(1, bname.getText().trim());
//                        ps.setString(2, genre.getText().trim());
//                        ps.setInt(3, priceInt);
//                        ps.setInt(4, Integer.parseInt(bid.trim()));
//                        int rows = ps.executeUpdate();
//                        if (rows > 0)
//                            JOptionPane.showMessageDialog(null, "Book Updated!");
//                        else
//                            JOptionPane.showMessageDialog(null, "Book ID not found!");
//                    }
//                }
//            } catch (NumberFormatException nfe) {
//                JOptionPane.showMessageDialog(null, "Price must be a valid integer");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Update Book failed: " + ex.getMessage());
//            }
//        }
//    }




   // ========= Update USER =========


//    public static void updateUser() {
//    String uid = JOptionPane.showInputDialog("Enter User ID to Update:");
//    if (uid == null || uid.trim().isEmpty()) return;
//
//    JTextField uname = new JTextField();
//    JTextField pass = new JTextField();
//    JCheckBox adminCheck = new JCheckBox("Admin?");
//    Object[] msg = {"New Username:", uname, "New Password:", pass, adminCheck};
//
//    if (JOptionPane.showConfirmDialog(null, msg, "Update User",
//            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
//        try (Connection connection = connect()) {
//            if (connection == null) return;
//            try (Statement stmt = connection.createStatement()) {
//                stmt.executeUpdate("USE library");
//            }
//            String sql = "UPDATE users SET Username=?, Password=?, Admin=? WHERE UID=?";
//            try (PreparedStatement ps = connection.prepareStatement(sql)) {
//                ps.setString(1, uname.getText().trim());
//                ps.setString(2, pass.getText().trim());
//                ps.setBoolean(3, adminCheck.isSelected());
//                ps.setInt(4, Integer.parseInt(uid.trim()));
//                int rows = ps.executeUpdate();
//                if (rows > 0)
//                    JOptionPane.showMessageDialog(null, "User Updated!");
//                else
//                    JOptionPane.showMessageDialog(null, "User ID not found!");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Update User failed: " + ex.getMessage());
//        }
//    }
//}
   


//======================ADD BOOK WITH PREPARED STATEMENT======================



//    public static void addBook() {
//    JTextField bname = new JTextField();
//    JTextField genre = new JTextField();
//    JTextField price = new JTextField();
//
//    Object[] msg = {"Book Name:", bname, "Genre:", genre, "Price:", price};
//
//    if (JOptionPane.showConfirmDialog(null, msg, "Add Book",
//            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
//
//        String bNameStr = bname.getText().trim();
//        String genreStr = genre.getText().trim();
//        String priceStr = price.getText().trim();
//
//        if (bNameStr.isEmpty() || genreStr.isEmpty() || priceStr.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "All fields are required");
//            return;
//        }
//
//        try {
//            int priceInt = Integer.parseInt(priceStr);
//
//            try (Connection connection = connect()) {
//                if (connection == null) return;
//                try (Statement stmt = connection.createStatement()) {
//                    stmt.executeUpdate("USE library");
//                }
//
//                String sql = "INSERT INTO books (BName, Genre, Price) VALUES (?, ?, ?)";
//                try (PreparedStatement ps = connection.prepareStatement(sql)) {
//                    ps.setString(1, bNameStr);
//                    ps.setString(2, genreStr);
//                    ps.setInt(3, priceInt);
//                    ps.executeUpdate();
//                }
//
//                JOptionPane.showMessageDialog(null, "Book Added!");
//            }
//        } catch (NumberFormatException nfe) {
//            JOptionPane.showMessageDialog(null, "Price must be an integer");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Add Book failed: " + ex.getMessage());
//        }
//    }
//}

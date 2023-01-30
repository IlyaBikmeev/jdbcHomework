package ru.bikmeev.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bikmeev.model.Book;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Data
public class BookDaoImpl implements BookDao {
    private final String driver;
    private final String url;
    private final String username;
    private final String password;

    public BookDaoImpl(@Value("${database.driver}") String driver,
                       @Value("${database.url}") String url,
                       @Value("${database.username}") String username,
                       @Value("${database.password}") String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Book book) {
        String query = "INSERT INTO book(title, author, genre, amount) " +
                "VALUES(?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getGenre());
            ps.setInt(4, book.getAmount());
            ps.executeUpdate();

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Book getById(int id) {
        String query = "SELECT * FROM book WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet data = ps.executeQuery();
            if(!data.next()) {
                throw new NoSuchElementException("No such book!");
            }
            return new Book (
                    data.getInt("id"),
                    data.getString("title"),
                    data.getString("author"),
                    data.getString("genre"),
                    data.getInt("amount")
            );

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        throw new NoSuchElementException("No such book!");
    }

    @Override
    public List<Book> getAll() {
        String query = "SELECT * FROM book";
        List<Book> result = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            Statement st = connection.createStatement()) {
            ResultSet data = st.executeQuery(query);

            while(data.next()) {
                result.add(
                        new Book(
                                data.getInt("id"),
                                data.getString("title"),
                                data.getString("author"),
                                data.getString("genre"),
                                data.getInt("id")
                        )
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

package nhom8.dao;

import nhom8.utils.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DAO<T> {
    Connection conn = DataSource.getInstance().getConnection();

    public abstract ArrayList<T> getAll() throws SQLException;

    public abstract T get(int id) throws SQLException;

    public abstract boolean create(T t) throws SQLException;

    public abstract boolean update(T t) throws SQLException;

    public abstract boolean delete(int id) throws SQLException;

}

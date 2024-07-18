package nhom8.dao;

import nhom8.utils.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public abstract class DAO<T> {
    Connection conn = DataSource.getInstance().getConnection();

    public abstract ArrayList<T> getAll() throws SQLException;

    public abstract T get(int id) throws SQLException;

    public abstract Map<String, Object> create(T t) throws SQLException;

    public abstract Map<String, Object> update(T t) throws SQLException;

    public abstract Map<String, Object> delete(int id) throws SQLException;

}

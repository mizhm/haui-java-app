package nhom8.dao;

import nhom8.model.User;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class UserDAO extends DAO<User> {
    @Override
    public ArrayList<User> getAll() throws SQLException {
        return null;
    }

    @Override
    public User get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(User user) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> update(User user) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        return Map.of();
    }

    public User auth(String email, String password) {
        User obj = null;
        String sql = "{CALL usp_check_user(?, ?)}";

        try {
            CallableStatement cs = conn.prepareCall(sql);
            cs.setString(1, email);
            cs.setString(2, password);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                obj = new User(
                        rs.getInt("id"),
                        rs.getNString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }
}

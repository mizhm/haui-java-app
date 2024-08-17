package nhom8.dao;

import nhom8.model.User;
import nhom8.utils.Common;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDAO extends DAO<User> {
    @Override
    public ArrayList<User> getAll() throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        String sql = "{call usp_get_all_user(?, ?,?)}";
        CallableStatement cs = conn.prepareCall(sql);
        cs.setNull(1, Types.NVARCHAR);
        cs.setNull(2, Types.NVARCHAR);
        cs.setNull(3, Types.TINYINT);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt(1));
            u.setName(rs.getNString(2));
            u.setEmail(rs.getString(3));
            u.setPassword(rs.getString(4));
            u.setRole(rs.getInt(5));
            list.add(u);
        }
        return list;
    }

    @Override
    public User get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(User user) throws SQLException {
        String sql = "{call usp_insert_user(?, ?, ?, ?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setNString(1, user.getName());
        cs.setString(2, user.getEmail());
        cs.setString(3, user.getPassword());
        cs.setInt(4, user.getRole());
        cs.registerOutParameter(5, Types.BIT);
        cs.registerOutParameter(6, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(5));
        output.put("message", cs.getNString(6));
        return output;
    }

    @Override
    public Map<String, Object> update(User user) throws SQLException {
        String sql = "{call usp_update_user(?, ?, ?, ?, ?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, user.getId());
        cs.setNString(2, user.getName());
        cs.setString(3, user.getEmail());
        cs.setString(4, user.getPassword());
        cs.setInt(5, user.getRole());
        cs.registerOutParameter(6, Types.BIT);
        cs.registerOutParameter(7, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(6));
        output.put("message", cs.getNString(7));
        return output;
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        Map<String, Object> output = new HashMap<>();
        String sql = "{call usp_delete_user(?,?,?)}";

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, id);
        cs.registerOutParameter(2, Types.BIT);
        cs.registerOutParameter(3, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(2));
        output.put("message", cs.getNString(3));
        return output;
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

    public ArrayList<User> getWithCondition(User user) throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        String sql = "{call usp_get_all_user(?, ?, ?)}";
        CallableStatement cs = conn.prepareCall(sql);
        if (!Common.isNullOrEmpty(user.getName())) {
            cs.setNString(1, user.getName());
        } else {
            cs.setNull(1, Types.NVARCHAR);
        }

        if (!Common.isNullOrEmpty(user.getEmail())) {
            cs.setString(2, user.getEmail());
        } else {
            cs.setNull(2, Types.VARCHAR);
        }

        if (!Common.isNullOrEmpty(user.getRole())) {
            cs.setInt(3, user.getRole());
        } else {
            cs.setNull(3, Types.TINYINT);
        }
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt(1));
            u.setName(rs.getNString(2));
            u.setEmail(rs.getString(3));
            u.setPassword(rs.getString(4));
            u.setRole(rs.getInt(5));
            list.add(u);
        }
        return list;
    }
}

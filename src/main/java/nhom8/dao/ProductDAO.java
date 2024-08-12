package nhom8.dao;

import nhom8.model.Product;
import nhom8.utils.Common;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDAO extends DAO<Product> {
    @Override
    public ArrayList<Product> getAll() throws SQLException {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "{call usp_get_all_product(?, ?, ?, ?, ?)}";
        CallableStatement cs = conn.prepareCall(sql);
        cs.setNull(1, Types.NVARCHAR);
        cs.setNull(2, Types.INTEGER);
        cs.setNull(3, Types.FLOAT);
        cs.setNull(4, Types.FLOAT);
        cs.setNull(5, Types.BOOLEAN);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            Product obj = new Product();
            obj.setId(rs.getInt(1));
            obj.setName(rs.getNString(2));
            obj.setPrice(rs.getFloat(3));
            obj.setStatus(rs.getBoolean(4));
            obj.setCreatedAt(rs.getString(5));
            obj.setUpdatedAt(rs.getString(6));
            obj.setCategoryId(rs.getInt(7));
            obj.setCategoryName(rs.getString(8));
            list.add(obj);
        }
        return list;
    }

    @Override
    public Product get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(Product product) throws SQLException {
        String sql = "{call usp_insert_product(?, ?, ?, ?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, product.getCategoryId());
        cs.setNString(2, product.getName());
        cs.setFloat(3, product.getPrice());
        cs.setBoolean(4, product.getStatus());
        cs.registerOutParameter(5, Types.BIT);
        cs.registerOutParameter(6, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(5));
        output.put("message", cs.getNString(6));
        return output;
    }

    @Override
    public Map<String, Object> update(Product product) throws SQLException {
        String sql = "{call usp_update_product(?,?, ?, ?, ?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, product.getId());
        cs.setInt(2, product.getCategoryId());
        cs.setNString(3, product.getName());
        cs.setFloat(4, product.getPrice());
        cs.setBoolean(5, product.getStatus());
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
        String sql = "{call usp_delete_product(?,?,?)}";

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, id);
        cs.registerOutParameter(2, Types.BIT);
        cs.registerOutParameter(3, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(2));
        output.put("message", cs.getNString(3));
        return output;
    }

    public ArrayList<Product> getWithCondition(Product product) throws SQLException {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "{call usp_get_all_product(?, ?, ?, ?, ?)}";
        CallableStatement cs = conn.prepareCall(sql);
        if (!Common.isNullOrEmpty(product.getName())) {
            cs.setNString(1, product.getName());
        } else {
            cs.setNull(1, Types.NVARCHAR);
        }

        if (!Common.isNullOrEmpty(product.getCategoryId())) {
            cs.setInt(2, product.getCategoryId());
        } else {
            cs.setNull(2, Types.INTEGER);
        }

        if (!Common.isNullOrEmpty(product.getFromPrice())) {
            cs.setFloat(3, product.getFromPrice());
        } else {
            cs.setNull(3, Types.FLOAT);
        }

        if (!Common.isNullOrEmpty(product.getToPrice())) {
            cs.setFloat(4, product.getToPrice());
        } else {
            cs.setNull(4, Types.FLOAT);
        }

        if (!Common.isNullOrEmpty(product.getStatus())) {
            cs.setBoolean(5, product.getStatus());
        } else {
            cs.setNull(5, Types.BOOLEAN);
        }
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            Product obj = new Product();
            obj.setId(rs.getInt(1));
            obj.setName(rs.getNString(2));
            obj.setPrice(rs.getFloat(3));
            obj.setStatus(rs.getBoolean(4));
            obj.setCreatedAt(rs.getString(5));
            obj.setUpdatedAt(rs.getString(6));
            obj.setCategoryId(rs.getInt(7));
            obj.setCategoryName(rs.getString(8));
            list.add(obj);
        }
        return list;
    }
}

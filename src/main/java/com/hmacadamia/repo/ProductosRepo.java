package com.hmacadamia.repo;

import com.hmacadamia.pos.ProductoVenta;
import com.hmacadamia.pos.Venta;
import com.hmacadamia.util.ConexionBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductosRepo implements RepositorioGenerico<ProductoVenta>{

    private Connection getConnection() throws SQLException {
        return ConexionBD.getInstance();
    }
    @Override
    public List<ProductoVenta> findall() {
        List<ProductoVenta> pventas = new ArrayList<ProductoVenta>();

        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery("select * from productos")) {

            while (rs.next()) {
                ProductoVenta pventa = getPVenta(rs);
                pventas.add(pventa);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pventas;
    }

    @Override
    public ProductoVenta searchbyid(Long id) {
        return null;
    }

    @Override
    public void save(ProductoVenta productosRepo) {

    }

    @Override
    public void remove(Long id) {

    }

    private ProductoVenta getPVenta(ResultSet rs) throws SQLException {
        ProductoVenta pventa = new ProductoVenta();
        pventa.setId(rs.getInt("id"));
        pventa.setDescripcion(rs.getString("descripcion"));
        pventa.setPrecio(rs.getDouble("price"));
        pventa.setUrlimg(rs.getString("urlimg"));

        return pventa;
    }
}

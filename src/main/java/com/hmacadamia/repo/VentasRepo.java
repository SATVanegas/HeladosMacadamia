package com.hmacadamia.repo;

import com.hmacadamia.pos.Venta;
import com.hmacadamia.util.ConexionBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentasRepo implements RepositorioGenerico<Venta>{

    private Connection getConnection() throws SQLException {
        return ConexionBD.getInstance();
    }

    @Override
    public List<Venta> findall() {
        List<Venta> ventas = new ArrayList<Venta>();



        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery("select * from ventas")) {

            while (rs.next()) {
                Venta venta = getVenta(rs);
                ventas.add(venta);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ventas;
    }

    @Override
    public Venta searchById(Long id) {
        return null;
    }

    @Override
    public Venta searchById(List<Venta> l, Long id) {
        return null;
    }


    @Override
    public void save(Venta venta) {

    }

    @Override
    public void remove(Long id) {

    }

    private  Venta getVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getInt("id"));
        venta.setFecha(rs.getDate("fecha_venta"));
        venta.setTotal(rs.getDouble("total"));
        venta.setProductoInventarios(rs.getString("productos"));

        return venta;
    }
}

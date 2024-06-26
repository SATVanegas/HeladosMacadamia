package com.hmacadamia.repo;

import com.hmacadamia.gastos.Categoria;
import com.hmacadamia.pos.CategoriaProducto;
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
    public ProductoVenta searchById(Long id) {
        return null;
    }

    @Override
    public ProductoVenta searchById(List<ProductoVenta> l, Long id) {
        int left = 0;
        int right = l.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midId = l.get(mid).getId();

            if (midId == id) {
                return l.get(mid);
            } else if (midId < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null; // Si no se encuentra el producto
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
        String sn = (rs.getString("categoria"));
        pventa.setCategoria(setCategoria(sn));

        return pventa;
    }

    private CategoriaProducto setCategoria(String cat){
        CategoriaProducto CategoriaProducto = null;

        switch (cat){
            case "Infantiles":
                CategoriaProducto = com.hmacadamia.pos.CategoriaProducto.INFANTILES;
                break;
            case "Batidos":
                CategoriaProducto = com.hmacadamia.pos.CategoriaProducto.BATIDOS;
                break;
            case "Conos":
                CategoriaProducto = com.hmacadamia.pos.CategoriaProducto.CONOS;
                break;
            case "Cucuruchos":
                CategoriaProducto = com.hmacadamia.pos.CategoriaProducto.CUCURUCHOS;
                break;
        }
        return CategoriaProducto;
    }
}

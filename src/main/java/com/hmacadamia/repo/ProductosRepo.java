package com.hmacadamia.repo;
import com.hmacadamia.pos.CategoriaProducto;
import com.hmacadamia.superclass.Producto;
import com.hmacadamia.util.ConexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductosRepo implements RepositorioGenerico<Producto>{

    private Connection getConnection() throws SQLException {
        return ConexionBD.getInstance();
    }
    @Override
    public List<Producto> findall() {
        List<Producto> pventas = new ArrayList<Producto>();

        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery("select * from productos")) {

            while (rs.next()) {
                Producto pventa = getPVenta(rs);
                pventas.add(pventa);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pventas;
    }

    @Override
    public Producto searchById(Long id) {
        return null;
    }

    @Override
    public Producto searchById(List<Producto> l, Long id) {
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
    public void save(Producto productosRepo) {

    }

    @Override
    public void remove(Long id) {

    }

    private Producto getPVenta(ResultSet rs) throws SQLException {
        Producto pventa = new Producto();
        pventa.setId(rs.getInt("id"));
        pventa.setDescripcion(rs.getString("descripcion"));
        pventa.setPrecio(rs.getDouble("price"));
        pventa.setUrlimg(rs.getString("urlimg"));
        String sn = (rs.getString("categoria"));
        pventa.setCategoria(setCategoria(sn));
        pventa.setProduct(rs.getBoolean("Isproduct"));

        return pventa;
    }

    private CategoriaProducto setCategoria(String cat){

        return switch (cat) {
            case "CHIPS" -> CategoriaProducto.CHIPS;
            case "Gaseosas" -> CategoriaProducto.GASEOSAS;
            case "Adiciones" -> com.hmacadamia.pos.CategoriaProducto.ADICIONES;
            case "Salsas" -> CategoriaProducto.SALSAS;
            case "Inventario" -> CategoriaProducto.INVENTARIO;
            default -> null;
        };
    }
}

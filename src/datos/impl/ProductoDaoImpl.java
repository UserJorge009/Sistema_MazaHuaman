package datos.impl;

import database.Conexion;
import dominio.Producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import data.ProductoDao;

public class ProductoDaoImpl implements ProductoDao<Producto> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public ProductoDaoImpl() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Producto> listar(String texto, int totalPorPagina, int numPagina) {
        List<Producto> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("Select p.id, p.nombre, p.descripcion,p.categoria_id,c.nombre as categoriaNombre from productos p inner join categorias c on p.categoria_id=c.id where p.nombre LIKE ? ORDER BY p.id ASC LIMIT ?,?");
            ps.setString(1, "%" + texto + "%");
            ps.setInt(2, (numPagina - 1) * totalPorPagina);
            ps.setInt(3, totalPorPagina);
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Producto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            rs=null;
            ps = null;
            CON.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Producto obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO productos (nombre,descripcion,categoria_id) VALUES (?,?,?)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            ps.setInt(3, obj.getCategoriaId());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Producto obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("Update productos set nombre=?, descripcion=?, categoria_id=? where id=?");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            ps.setInt(3, obj.getCategoriaId());
            ps.setInt(4, obj.getId());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean eliminar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("DELETE FROM productos WHERE id=?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public int total() {
        int total = 0;
        try {
            ps = CON.conectar().prepareStatement("SELECT COUNT(id) FROM productos");
            rs=ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            rs=null;
            CON.desconectar();
        }
        return total;
    }

    @Override
    public int existe(String texto) {
        int id = 0;
        try {
            ps = CON.conectar().prepareStatement("SELECT id FROM productos WHERE nombre=?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            while (rs.next()) {
                id=rs.getInt(1);
            }   
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            rs=null;
            CON.desconectar();
        }
        return id;
    }
}
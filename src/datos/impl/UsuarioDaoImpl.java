/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.impl;

import data.UsuarioDao;
import dominio.Usuario;
import database.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kgrem
 */
public class UsuarioDaoImpl implements UsuarioDao<Usuario> {
    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;
    public UsuarioDaoImpl(){
        CON = Conexion.getInstancia();
    }
    @Override
    public List<Usuario> listar(String texto) {
        List<Usuario> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("SELECT * FROM usuarios where nombre LIKE ?");
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Usuario(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Usuario obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO usuarios (nombre,email,clave,rol) VALUES (?,?,?,?)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getClave());
            ps.setString(4,obj.getRol());
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
    public boolean actualizar(Usuario obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("Update usuarios set nombre=?, email=?, clave=?,rol=? where id=?");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getClave());
            ps.setString(4, obj.getRol());
            ps.setInt(5, obj.getId());
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
            ps = CON.conectar().prepareStatement("DELETE FROM usuarios WHERE id=?");
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
            ps = CON.conectar().prepareStatement("SELECT COUNT(id) FROM usuarios");
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
            ps = CON.conectar().prepareStatement("SELECT id FROM usuarios WHERE nombre=?");
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
 
   @Override
    public Usuario Login(String email, String password) {
        Usuario usuario = null;
        try {
            ps = CON.conectar().prepareStatement("SELECT id,nombre,email,rol FROM usuarios where email=?, and clave=?");
            ps.setString(1, email);
            ps.setString(2, password);
            rs=ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), "");
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
        return usuario;
    }
}

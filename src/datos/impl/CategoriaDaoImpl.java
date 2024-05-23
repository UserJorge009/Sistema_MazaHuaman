package datos.impl;

import database.Conexion;
import data.CategoriaDao;
import dominio.Categoria;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaDaoImpl implements CategoriaDao<Categoria> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public CategoriaDaoImpl() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Categoria> listar(String texto) {
        List<Categoria> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("Select * from categorias where nombre like ?");
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Categoria(rs.getInt(1), rs.getString(2)));
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
    public boolean insertar(Categoria obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO categorias (nombre) VALUES (?)");
            ps.setString(1, obj.getNombre());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Categoria obj) {
        resp=false;
        try{
           ps=CON.conectar().prepareStatement("UPDATE categorias SET nombre=? WHERE id=?");
           ps.setString(1,obj.getNombre());
           ps.setInt(2,obj.getId());
           if(ps.executeUpdate()>0){
               resp=true;
           }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally{
           ps=null;
           CON.desconectar(); 
        }
        return resp;
    }

    @Override
    public boolean eliminar(int id) {
        resp=false;
        try{
           ps=CON.conectar().prepareStatement("DELETE FROM categorias WHERE id=?");
           ps.setInt(1,id);
           if(ps.executeUpdate()>0){
               resp=true;
           }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally{
           ps=null;
           CON.desconectar(); 
        }
        return resp;
    }
    public static void main(String[] args) {
        CategoriaDao datos = new CategoriaDaoImpl();
        System.out.println(datos.listar("").size());
        System.out.println(datos.listar("").get(0));
    }

}

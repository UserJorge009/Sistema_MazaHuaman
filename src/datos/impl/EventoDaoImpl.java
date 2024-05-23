/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.impl;

import database.Conexion;
import dominio.Evento;//primero
import data.EventoDao;//segundo
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author kgrem
 */
public class EventoDaoImpl implements EventoDao<Evento>{
   private final Conexion CON;
   private PreparedStatement ps;
   private ResultSet rs;
    private boolean resp;

    public EventoDaoImpl() {
        CON = Conexion.getInstancia();
    }   

    @Override
    public List<Evento> listar(String texto) {
        List<Evento> registros = new ArrayList();
            try {
                ps = CON.conectar().prepareStatement("Select * from eventos where nombre like ?");
                ps.setString(1, "%" + texto + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    registros.add(new Evento(rs.getInt(1), rs.getString(2),rs.getNString(3),rs.getNString(4),rs.getString(5),rs.getBigDecimal(6)));
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
    public boolean insertar(Evento obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO eventos (nombre, descripcion, fecha_inicio, fecha_fin, costo) VALUES (?,?,?,?,?)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            ps.setString(3, obj.getFecha_inicio());
            ps.setString(4, obj.getFecha_fin());
            ps.setBigDecimal(5, obj.getCosto());
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
    public boolean actualizar(Evento obj) {
        resp=false;
        try{
           ps=CON.conectar().prepareStatement("UPDATE eventos SET nombre=?, descripcion=?, fecha_inicio=?, fecha-fin=?, costo=? WHERE id=?");
           ps.setString(1,obj.getNombre());
           ps.setString(2,obj.getDescripcion());
           ps.setString(3,obj.getFecha_inicio());
           ps.setString(4,obj.getFecha_fin());
           ps.setBigDecimal(5,obj.getCosto());
           ps.setInt(6,obj.getId());
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
           ps=CON.conectar().prepareStatement("DELETE FROM eventos WHERE id=?");
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
        EventoDao datos = new EventoDaoImpl();
        System.out.println(datos.listar("").size());
        System.out.println(datos.listar("").get(0));
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;
import datos.impl.UsuarioDaoImpl;
import dominio.Usuario;
import data.UsuarioDao;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author kgrem
 */
public class UsuarioControl {
    private final UsuarioDaoImpl DATOSUS;
    private Usuario obj;
    public UsuarioControl() {
        this.DATOSUS = new UsuarioDaoImpl();
        this.obj = new Usuario();
    }
    private DefaultTableModel modeloTabla;
    private int registrosMostrados;
    
        public DefaultTableModel listar(String texto) {
        List<Usuario> lista = new ArrayList();
        lista.addAll(DATOSUS.listar(texto));
        //Establecemos la columna del tableModel
        String[] titulos = {"ID", "NOMBRE","CORREO","CLAVE","ROL"};
        //Declaramos un vector que será el que agreguemos como registro al DefaultTableModel
        String[] registro = new String[5];
        //agrego los títulos al DefaultTableModel
        this.modeloTabla = new DefaultTableModel(null, titulos);

        //Recorrer toda mi lista y la pasare al DefaultTableModel
        for (Usuario item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = item.getEmail();
            registro[3] = item.getClave();
            registro[4] = item.getRol();
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }
    public String insertar(Usuario obj) {
        if (DATOSUS.existe(obj.getNombre())!=0) {
            return "El registro ya existe.";
        } else {
            if (DATOSUS.insertar(obj)) {
                return "OK";
            } else {
                return "Error en el ingreso de datos.";
            }
        }
    }
        public String actualizar(Usuario obj) {
        int id = DATOSUS.existe(obj.getNombre());
        if (id!=0 && id!=obj.getId()) {
            return "El registro ya existe.";
        } else {
            if (DATOSUS.actualizar(obj)) {
                return "OK";
            } else {
                return "Error en el ingreso de datos.";
            }
        }
    }

    public String eliminar(int id) {
        if (DATOSUS.eliminar(id)) {
            return "OK";
        } else {
            return "Error en la eliminación de datos.";
        }
    }
    private static String encriptar( String valor ){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        
        byte[] hash = md.digest(valor.getBytes());
        StringBuilder sb = new StringBuilder();
        
        for ( byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public String login(String email, String clave) {
        String resp = "0";
        Usuario usu = (Usuario) DATOSUS.Login(email, encriptar(clave));
        if (usu != null) {
                Variables.usuarioId = usu.getId();
                Variables.rolNombre = usu.getRol();
                Variables.usuarioNombre = usu.getEmail();
                resp = "1";
        }else {
            resp= "0";
        }
        return resp;
    }
    public int total(){
        return DATOSUS.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
}

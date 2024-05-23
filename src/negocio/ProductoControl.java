package negocio;

import datos.impl.ProductoDaoImpl;
import datos.impl.CategoriaDaoImpl;
import dominio.Producto;
import dominio.Categoria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import data.ProductoDao;

public class ProductoControl {

    private final ProductoDao DATOS;
    private final CategoriaDaoImpl DATOSCAT;
    private Producto obj;

    public ProductoControl() {
        this.DATOS = new ProductoDaoImpl();
        this.DATOSCAT = new CategoriaDaoImpl();
        this.obj = new Producto();
    }
    private DefaultTableModel modeloTabla;
    private int registrosMostrados;

    public DefaultTableModel listar(String texto, int totalPorPagina, int numPagina) {
        this.registrosMostrados=0;
        List<Producto> lista = new ArrayList();
        lista.addAll(DATOS.listar(texto, totalPorPagina, numPagina));
        //Establecemos la columna del tableModel
        String[] titulos = {"ID", "NOMBRE", "DESCRIPCION", "ID CATEGORIA", "CATEGORIA"};
        //Declaramos un vector que será el que agreguemos como registro al DefaultTableModel
        String[] registro = new String[5];
        //agrego los títulos al DefaultTableModel
        this.modeloTabla = new DefaultTableModel(null, titulos);

        //Recorrer toda mi lista y la pasare al DefaultTableModel
        for (Producto item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = item.getDescripcion();
            registro[3] = Integer.toString(item.getCategoriaId());
            registro[4] = item.getCategoriaNombre();
            this.modeloTabla.addRow(registro);
            this.registrosMostrados=this.registrosMostrados+1;
        }
        return this.modeloTabla;
    }

    public String insertar(Producto obj) {
        if (DATOS.existe(obj.getNombre())!=0) {
            return "El registro ya existe.";
        } else {
            if (DATOS.insertar(obj)) {
                return "OK";
            } else {
                return "Error en el ingreso de datos.";
            }
        }
    }

    public String actualizar(Producto obj) {
        int id = DATOS.existe(obj.getNombre());
        if (id!=0 && id!=obj.getId()) {
            return "El registro ya existe.";
        } else {
            if (DATOS.actualizar(obj)) {
                return "OK";
            } else {
                return "Error en el ingreso de datos.";
            }
        }
    }

    public String eliminar(int id) {
        if (DATOS.eliminar(id)) {
            return "OK";
        } else {
            return "Error en la eliminación de datos.";
        }
    }
    
    public int total(){
        return DATOS.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
    
    public DefaultComboBoxModel seleccionar(){
        DefaultComboBoxModel items= new DefaultComboBoxModel();
        List<Categoria> lista=new ArrayList();
        lista=DATOSCAT.listar("");
        for (Categoria item: lista){
            items.addElement(new Categoria(item.getId(),item.getNombre()));
        }
        return items;
    }
}

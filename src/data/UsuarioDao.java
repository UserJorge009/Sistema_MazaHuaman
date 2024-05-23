/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data;

import java.util.List;

/**
 *
 * @author kgrem
 */
public interface UsuarioDao<T> {
     public List<T> listar(String texto);
      public boolean insertar(T obj);
    public boolean actualizar(T obj);
    public boolean eliminar(int id);
    public int total();
    public int existe(String texto);
    public T Login(String email, String password);
}

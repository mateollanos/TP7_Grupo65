package ar.edu.unju.escmi.tp7.dao;

import ar.edu.unju.escmi.tp7.entities.Producto;

public interface ProductoDAO {
	public void guardarProducto(Producto producto);
	public void modificarPrecioProducto(Producto producto);
	public Producto buscarProductoPorId(long idProducto);
	public void eliminarProductoLogicamente(Producto producto);
}

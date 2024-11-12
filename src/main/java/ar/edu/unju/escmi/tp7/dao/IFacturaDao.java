package ar.edu.unju.escmi.tp7.dao;

import ar.edu.unju.escmi.tp7.entities.Factura;

public interface IFacturaDao {
	
	public void guardarFactura(Factura factura);
	public Factura buscarFacturaPorId(long idFactura);
	public void eliminarFacturaLogicamente(Factura Sfactura);
	public void mostrarTodasLasFacturas();
	public void mostrarFacturasFiltradas();
	public void modificarFactura(Factura factura);
	
}
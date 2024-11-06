package ar.edu.unju.escmi.tp7.dao;

import ar.edu.unju.escmi.tp7.entities.Factura;

public interface FacturaDAO {
	public void guardarFactura(Factura factura);
	public Factura buscarFacturaPorId(long idFactura);
	public void eliminarFacturaLogicamente(Factura factura);
	public void mostrarTodasLasFacturas();
	public void mostrarFacturasFiltradas();
}


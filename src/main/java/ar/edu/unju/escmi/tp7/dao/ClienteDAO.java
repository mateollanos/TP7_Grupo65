package ar.edu.unju.escmi.tp7.dao;

import ar.edu.unju.escmi.tp7.entities.Cliente;

public interface ClienteDAO {
	public void guardarCliente(Cliente cliente);
	public void modificarCliente(Cliente cliente);
	public Cliente buscarClientePorId(long idCliente);
	public void borrarCliente(Cliente cliente);
	public void mostrarTodosLosClientes();
}

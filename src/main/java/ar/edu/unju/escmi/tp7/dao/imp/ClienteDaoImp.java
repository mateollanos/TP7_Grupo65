package ar.edu.unju.escmi.tp7.dao.imp;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import ar.edu.unju.escmi.tp7.config.EmfSingleton;
import ar.edu.unju.escmi.tp7.dao.IClienteDao;
import ar.edu.unju.escmi.tp7.entities.Cliente;

public class ClienteDaoImp implements IClienteDao{
	
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
	@Override
	public void guardarCliente(Cliente cliente) {
		try {
			manager.getTransaction().begin();
			manager.merge(cliente);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction()!=null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se pudo guardar el cliente.");
		}
	}

	@Override
	public void modificarCliente(Cliente cliente) {
		try {
			manager.getTransaction().begin();
			manager.merge(cliente);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction()!=null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se pudo modificar los datos del cliente.");
		}		
	}

	@Override
	public void mostrarTodosLosClientes() {
		Query query = manager.createQuery("SELECT e FROM Cliente e",Cliente.class);
		@SuppressWarnings("unchecked")
		List<Cliente> clientes = query.getResultList();
		for(Cliente cli : clientes) {
			cli.mostrarDatos();
		}
	}

	@Override
	public Cliente buscarClientePorId(long idCliente) {
		
			return manager.find(Cliente.class, idCliente);
	}
	
}

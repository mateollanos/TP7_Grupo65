package ar.edu.unju.escmi.tp7.dao.imp;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

import ar.edu.unju.escmi.tp7.config.EmfSingleton;
import ar.edu.unju.escmi.tp7.dao.ClienteDAO;
import ar.edu.unju.escmi.tp7.entities.Cliente;

public class ImpClienteDAO implements ClienteDAO{
	
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
	@Override
	public void guardarCliente(Cliente cliente) {
	    try {
	        if (cliente.getId() != 0) {
	            cliente.setId(0); 
	        }

	        manager.getTransaction().begin();
	        manager.merge(cliente);
	        manager.getTransaction().commit();
	        
	        System.out.println("Cliente registrado con éxito!");
	    } catch (Exception e) {
	        if (manager.getTransaction() != null) {
	            manager.getTransaction().rollback();
	        }
	        System.out.println("No se pudo guardar el cliente.");
	        e.printStackTrace();
	    } finally {
	        manager.close();
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
		}finally {
			manager.close();
		}		
	}

	@Override
	public void borrarCliente(Cliente cliente) {
		try {
			manager.getTransaction().begin();
			manager.remove(cliente);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction()!=null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se pudo borrar al cliente.");
		}finally {
			manager.close();
		}		
	}

	@Override
	public void mostrarTodosLosClientes() {
	    Query query = manager.createQuery("SELECT e FROM Cliente e"); 
	    @SuppressWarnings("unchecked")
	    List<Cliente> clientes = query.getResultList();
	    for (Cliente cli : clientes) {
	        cli.mostrarDatos(); 
	    }
	}


	@Override
	public Cliente buscarClientePorId(long idCliente) {
	    if (manager.isOpen()) {
	        return manager.find(Cliente.class, idCliente);
	    } else {
	        throw new IllegalStateException("El EntityManager está cerrado.");
	    }
	}

	
}


package ar.edu.unju.escmi.tp7.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import ar.edu.unju.escmi.tp7.config.EmfSingleton;
import ar.edu.unju.escmi.tp7.dao.FacturaDAO;
import ar.edu.unju.escmi.tp7.entities.Factura;

public class ImpFacturaDAO implements FacturaDAO {

	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
	
	@Override
	public void guardarFactura(Factura factura) {
		try {
			manager.getTransaction().begin();
			manager.persist(factura);
			manager.getTransaction().commit();
		}catch(Exception e) {
			if(manager.getTransaction() != null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se pudo guardar la factura");	
		}finally {
			manager.close();
		}
	}

	@Override
	public Factura buscarFacturaPorId(long idFactura) {
		return manager.find(Factura.class, idFactura);
	}

	@Override
	public void eliminarFacturaLogicamente(Factura factura) {
		try {
			manager.getTransaction().begin();
			manager.remove(factura);
			manager.getTransaction().commit();
		}catch(Exception e) {
			if(manager.getTransaction() != null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se pudo eliminar la factura");
		}finally {
			manager.close();
		}
	}

	@Override
	public void mostrarTodasLasFacturas() {
		TypedQuery<Factura> query = manager.createQuery("SELECT e FROM Factura e",Factura.class);
		List<Factura> facturas = query.getResultList();
		for(Factura factura : facturas) {
			factura.mostrarFactura();
		}
	}

	@Override
	public void mostrarFacturasFiltradas() {
	    TypedQuery<Factura> query = manager.createQuery("SELECT f FROM Factura f WHERE f.total >= :monto", Factura.class);
	    query.setParameter("monto", 500000.0); 
	    List<Factura> facturas = query.getResultList();
	    
	    for (Factura factura : facturas) {
	        factura.mostrarFactura(); 
	    }
	}



}
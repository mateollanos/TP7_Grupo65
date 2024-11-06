package ar.edu.unju.escmi.tp7.dao.imp;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import ar.edu.unju.escmi.tp7.config.EmfSingleton;
import ar.edu.unju.escmi.tp7.dao.ProductoDAO;
import ar.edu.unju.escmi.tp7.entities.Producto;

public class ImpProductoDAO implements ProductoDAO {
	
	private EntityManager manager;

    public ImpProductoDAO() {
        this.manager = EmfSingleton.getInstance().getEmf().createEntityManager();
    }
	
	@Override
	public void guardarProducto(Producto producto) {
	    try {
	        manager.getTransaction().begin();  
	        manager.merge(producto);       
	        manager.getTransaction().commit(); 
	        System.out.println("¡Producto registrado con éxito! 🛒");
	    } catch (Exception e) {
	        if (manager.getTransaction() != null) {
	            manager.getTransaction().rollback(); 
	        }
	        e.printStackTrace();
	        System.out.println("No se pudo guardar el producto");
	    } finally {
	        manager.close(); 
	    }
	}


	
	@Override
	public Producto buscarProductoPorId(long idProducto) {
	    Producto producto = null;
	    try {
	        manager.getTransaction().begin();
	        producto = manager.find(Producto.class, idProducto);
	        manager.getTransaction().commit();
	    } catch (Exception e) {
	        if (manager.getTransaction() != null) {
	            manager.getTransaction().rollback();
	        }
	        e.printStackTrace();
	        System.out.println("No se pudo buscar el producto");
	    } finally {
	        if (manager.isOpen()) {
	            manager.close();
	        }
	    }
	    return producto;
	}



	@Override
	public void modificarPrecioProducto(Producto producto) {
	    EntityTransaction transaction = null;
	    try {
	        if (!manager.isOpen()) {
	            manager = EmfSingleton.getInstance().getEmf().createEntityManager(); 
	        }

	        transaction = manager.getTransaction();
	        transaction.begin();

	        manager.merge(producto);

	        transaction.commit();
	        System.out.println("¡Producto modificado con éxito! 💲");

	    } catch (Exception e) {
	        if (transaction != null && transaction.isActive()) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	        System.out.println("No se pudo modificar el producto");
	    }
	}



	@Override
    public void eliminarProductoLogicamente(Producto producto) {
        EntityTransaction transaction = null;
        try {
            if (!manager.isOpen()) {
                manager = EmfSingleton.getInstance().getEmf().createEntityManager();
            }

            transaction = manager.getTransaction();
            transaction.begin();

            producto.setEstado(true);
            manager.merge(producto);

            transaction.commit();

            System.out.println("¡Producto eliminado con éxito! 🗑️");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("No se pudo eliminar el producto");
        } finally {
            if (manager.isOpen()) {
                manager.close();
            }
        }
    }

}

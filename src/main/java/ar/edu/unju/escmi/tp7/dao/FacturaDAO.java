package ar.edu.unju.escmi.tp7.entities;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Table(name = "Facturas")
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fact_id")
    private long id; 
    
    @Column(name = "fact_fecha")
    private LocalDate fecha = LocalDate.now();
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false) 
    private Cliente cliente;
    
    @Column(name = "fact_total")
    private double total;
    
    @Column(name = "fact_estado")
    private boolean estado;
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<DetalleFactura> detalles;
    
    public Factura(LocalDate fecha, Cliente cliente, double total) {
        super();
        this.fecha = fecha;
        this.cliente = cliente;
        this.total = total;
        this.estado = true;
    }
    
    public Factura() {}

    public long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
    
    public void mostrarFactura() {
        System.out.println("\nID: "+id);
        System.out.println("Fecha: "+fecha);
        System.out.println("Cliente: "+cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("Domicilio del cliente: "+cliente.getDomicilio());
        System.out.println("Monto total: "+total);
    }

    public void calcularTotal() {
        this.total = 0.0;
        
        if (detalles != null) {
            for (DetalleFactura detalle : detalles) {
                if (detalle != null) {
                    detalle.calcularSubtotal(); 
                    this.total += detalle.getSubtotal();
                }
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "plan_estrategicas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanEstrategicas.findAll", query = "SELECT p FROM PlanEstrategicas p"),
    @NamedQuery(name = "PlanEstrategicas.findOrdered", query = "SELECT p FROM PlanEstrategicas p ORDER BY p.id DESC"),
    @NamedQuery(name = "PlanEstrategicas.findById", query = "SELECT p FROM PlanEstrategicas p WHERE p.id = :id"),
    @NamedQuery(name = "PlanEstrategicas.findByPeriodo", query = "SELECT p FROM PlanEstrategicas p WHERE p.programacion.año = :año"),
    @NamedQuery(name = "PlanEstrategicas.findByFecha", query = "SELECT p FROM PlanEstrategicas p WHERE p.fechaHoraAlta >= :fechaDesde AND p.fechaHoraAlta <= :fechaHasta")})

public class PlanEstrategicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "valor_variable")
    private double valorVariable;
    @Basic(optional = false)
    @Column(name = "programa_presupuestario")
    private BigDecimal programaPresupuestario;
    @Basic(optional = false)
    @Column(name = "variable2")
    private double variable2;
    @Column(name = "resultado")
    private double resultado;
    @Column(name = "valor")
    private double valor;
    @Column(name = "resultado_presupuesto")
    private double resultadoPresupuesto;
    @Basic(optional = true)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @Column(name = "metas_alcanzada")
    private boolean metasAlcanzada;
    @OneToMany(mappedBy = "usuarioAlta")
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposObjetivos tiposObjetivos;
    @JoinColumn(name = "accion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Acciones accion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Empresas empresa;
    @JoinColumn(name = "actividad", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Actividades actividad;
    @JoinColumn(name = "programacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Programacion programacion;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosActividad estado;
    @JoinColumn(name = "periodo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Periodo periodo;

    public PlanEstrategicas() {
    }

    public PlanEstrategicas(Integer id) {
        this.id = id;
    }

    public PlanEstrategicas(Integer id,double variable2, double resultado) {
        this.id = id;
        this.variable2 = variable2;
        this.resultado = resultado;
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TiposObjetivos getTiposObjetivos() {
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }
    
    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public BigDecimal getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(BigDecimal programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public double getVariable2() {
        return variable2;
    }

    public void setVariable2(double variable2) {
        this.variable2 = variable2;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public EstadosActividad getEstado() {
        return estado;
    }

    public void setEstado(EstadosActividad estado) {
        this.estado = estado;
    }

    public double getResultadoPresupuesto() {
        return resultadoPresupuesto;
    }

    public void setResultadoPresupuesto(double resultadoPresupuesto) {
        this.resultadoPresupuesto = resultadoPresupuesto;
    }

    public boolean isMetasAlcanzada() {
        return metasAlcanzada;
    }

    public void setMetasAlcanzada(boolean metasAlcanzada) {
        this.metasAlcanzada = metasAlcanzada;
    }  

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanEstrategicas)) {
            return false;
        }
        PlanEstrategicas other = (PlanEstrategicas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.PlanEstrategicas[ id=" + id + " ]";
    }

}

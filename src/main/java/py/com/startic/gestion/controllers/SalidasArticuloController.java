package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;

import py.com.startic.gestion.models.SalidasArticulo;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.CantidadItem;
import py.com.startic.gestion.datasource.RepSalidaObjetoGasto;
import py.com.startic.gestion.datasource.RepSalidaArticulo;
import py.com.startic.gestion.datasource.RepSalidaArticuloDepositoSuma;
import py.com.startic.gestion.datasource.RepSalidaArticuloDpto;
import py.com.startic.gestion.datasource.RepSalidaArticuloDptoSuma;
import py.com.startic.gestion.datasource.RepSalidaArticuloPorDeposito;
import py.com.startic.gestion.datasource.RepSalidaArticuloProveedor;
import py.com.startic.gestion.datasource.RepSalidaObjetoGastoSuma;
import py.com.startic.gestion.datasource.ReportesInventario;
import py.com.startic.gestion.models.ArchivosReporte;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DetallesEntradaArticulo;
import py.com.startic.gestion.models.DetallesSalidaArticulo;
import py.com.startic.gestion.models.DetallesSalidaArticuloCambios;
import py.com.startic.gestion.models.FirmasArticulosSalida;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.Inventarios;
import py.com.startic.gestion.models.Marcas;
import py.com.startic.gestion.models.Modelos;
import py.com.startic.gestion.models.ObjetosGasto;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.PedidosArticulo;
import py.com.startic.gestion.models.Personas;
import py.com.startic.gestion.models.Proveedores;
import py.com.startic.gestion.models.ReporteInventarioValorizado;
import py.com.startic.gestion.models.SalidasArticuloCambios;
import py.com.startic.gestion.models.Sede;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "salidasArticuloController")
@ViewScoped
public class SalidasArticuloController extends AbstractController<SalidasArticulo> {

    @Inject
    private UsuariosController personaController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private ArticulosController articuloController;
    @Inject
    private ArticulosStockCriticoController articuloStockCriticoController;
    @Inject
    private SalidasArticuloCambiosController salidaArticuloCambioController;
    @Inject
    private SalidasArticuloController salidaArticuloController;
    @Inject
    private DetallesSalidaArticuloCambiosController detalleSalidaArticuloCambioController;
    @Inject
    private DetallesSalidaArticuloController detallesSalidaArticuloController;
    @Inject
    private InventariosController inventarioController;
    @Inject
    private SedeController sedeController;
    @Inject
    private ArchivosReporteController archivosReporteController;
    @Inject
    private FirmasArticulosSalidaController firmasArticulosSalidaController;

    private Collection<DetallesSalidaArticulo> detalles;
    private Collection<DetallesSalidaArticulo> detallesEdit;
    private Collection<DetallesSalidaArticulo> detallesEditOri;
    private SalidasArticulo salidaArticuloOri;

    private DetallesSalidaArticulo detalleSelected;
    private DetallesSalidaArticulo detalleSelectedEdit;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaDesdeSalida;
    private Date fechaHastaSalida;

    private Articulos articulo;
    private String deposito;
    private Articulos articuloEdit;
    private Articulos articuloFiltro;
    private List<Articulos> listaArticuloFiltroSalida;
    private Departamentos departamentoFiltro;
    private List<Departamentos> listadepartamentoFiltroSalida;
    private ObjetosGasto objetosGastoFiltro;
    private List<ObjetosGasto> listaObjetosGastoFiltroSalida;
    private List<Sede> listadepositoFiltroSalida;
    private List<Proveedores> listaProveedorFiltroSalida;
    private List<Articulos> listaArticulos;
    private Integer neto;
    private Integer stock;
    private SalidasArticulo docImprimir;
    private String nombreArchivo;
    private Personas personaUsuario;
    private String url;

    private Integer cantidad;
    private Integer cantidadEdit;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private BigDecimal totalSalida;
    private Integer cantTotalSalida;

    private Integer nroFormularioEdit;
    private ParametrosSistema par;

    private SalidasArticulo salidaArticulo;
    private PedidosArticulo pedidosArticulo;
    private String ordenarPor;
    private String tipoOrden;
    private ArchivosReporte archivosReporte;

    private String codigoSuministro;
    private Date fechaVencimiento;
    private Marcas marca;
    private Modelos modelo;
    private Usuarios usuario;
    private String sessionId;
    private ObjetosGasto objetosGasto;
    private String mensajeNeto;
    private PieChartModel pieModel2;
    private PieChartModel pieModelO;
    private boolean False;
    private String estiloPieDepartamento;
    private String estiloPieObjetoGasto;
    private Object Arrays;
    private Sede sede;
    private Integer nroFormulario;
    private HttpSession session;
    private UploadedFile file;
    private Part fileUpload;
    private String nombre;
    private String content;
    private String endpoint;
    private String accion;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Part getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(Part fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getEstiloPieDepartamento() {
        return estiloPieDepartamento;
    }

    public void setEstiloPieDepartamento(String estiloPieDepartamento) {
        this.estiloPieDepartamento = estiloPieDepartamento;
    }

    public String getEstiloPieObjetoGasto() {
        return estiloPieObjetoGasto;
    }

    public void setEstiloPieObjetoGasto(String estiloPieObjetoGasto) {
        this.estiloPieObjetoGasto = estiloPieObjetoGasto;
    }

    public PieChartModel getPieModelO() {
        return pieModelO;
    }

    public void setPieModelO(PieChartModel pieModelO) {
        this.pieModelO = pieModelO;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public BigDecimal getTotalSalida() {
        return totalSalida;
    }

    public void setTotalSalida(BigDecimal totalSalida) {
        this.totalSalida = totalSalida;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Integer getNroFormularioEdit() {
        return nroFormularioEdit;
    }

    public void setNroFormularioEdit(Integer nroFormularioEdit) {
        this.nroFormularioEdit = nroFormularioEdit;
    }

    public Integer getNeto() {
        return neto;
    }

    public void setNeto(Integer neto) {
        this.neto = neto;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(Integer nroFormulario) {
        this.nroFormulario = nroFormulario;
    }

    public List<ObjetosGasto> getListaObjetosGastoFiltroSalida() {
        return listaObjetosGastoFiltroSalida;
    }

    public void setListaObjetosGastoFiltroSalida(List<ObjetosGasto> listaObjetosGastoFiltroSalida) {
        this.listaObjetosGastoFiltroSalida = listaObjetosGastoFiltroSalida;
    }

    public List<Articulos> getListaArticuloFiltroSalida() {
        return listaArticuloFiltroSalida;
    }

    public void setListaArticuloFiltroSalida(List<Articulos> listaArticuloFiltroSalida) {
        this.listaArticuloFiltroSalida = listaArticuloFiltroSalida;
    }

    public ObjetosGasto getObjetosGasto() {
        return objetosGasto;
    }

    public void setObjetosGasto(ObjetosGasto objetosGasto) {
        this.objetosGasto = objetosGasto;
    }

    public String getCodigoSuministro() {
        return codigoSuministro;
    }

    public ObjetosGasto getObjetosGastoFiltro() {
        return objetosGastoFiltro;
    }

    public void setObjetosGastoFiltro(ObjetosGasto objetosGastoFiltro) {
        this.objetosGastoFiltro = objetosGastoFiltro;
    }

    public void setCodigoSuministro(String codigoSuministro) {
        this.codigoSuministro = codigoSuministro;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Marcas getMarca() {
        return marca;
    }

    public void setMarca(Marcas marca) {
        this.marca = marca;
    }

    public SalidasArticulo getDocImprimir() {
        return docImprimir;
    }

    public void setDocImprimir(SalidasArticulo docImprimir) {
        this.docImprimir = docImprimir;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Modelos getModelo() {
        return modelo;
    }

    public void setModelo(Modelos modelo) {
        this.modelo = modelo;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Personas getPersonaUsuario() {
        return personaUsuario;
    }

    public void setPersonaUsuario(Personas personaUsuario) {
        this.personaUsuario = personaUsuario;
    }

    public ArchivosReporte getArchivosReporte() {
        return archivosReporte;
    }

    public void setArchivosReporte(ArchivosReporte archivosReporte) {
        this.archivosReporte = archivosReporte;
    }

    public PedidosArticulo getPedidosArticulo() {
        return pedidosArticulo;
    }

    public void setPedidosArticulo(PedidosArticulo pedidosArticulo) {
        this.pedidosArticulo = pedidosArticulo;
    }

    public Sede getSede() {
        if (session.getAttribute("sedeSelected") != null) {
            sede = (Sede) session.getAttribute("sedeSelected");
            session.removeAttribute("sedeSelected");
            actualizarListas(sede);
        }
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public List<Departamentos> getListadepartamentoFiltroSalida() {
        return listadepartamentoFiltroSalida;
    }

    public void setListadepartamentoFiltroSalida(List<Departamentos> listadepartamentoFiltroSalida) {
        this.listadepartamentoFiltroSalida = listadepartamentoFiltroSalida;
    }

    public List<Sede> getListadepositoFiltroSalida() {
        return listadepositoFiltroSalida;
    }

    public void setListadepositoFiltroSalida(List<Sede> listadepositoFiltroSalida) {
        this.listadepositoFiltroSalida = listadepositoFiltroSalida;
    }

    public List<Proveedores> getListaProveedorFiltroSalida() {
        return listaProveedorFiltroSalida;
    }

    public void setListaProveedorFiltroSalida(List<Proveedores> listaProveedorFiltroSalida) {
        this.listaProveedorFiltroSalida = listaProveedorFiltroSalida;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public List<Articulos> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<Articulos> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
    }

    public void prepareReporteNroFormulario() {
        if (getSelected() != null) {

        }

    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        salidaArticulo = prepareCreate(null);
        fechaDesdeSalida = ejbFacade.getSystemDateOnly(-30);
        fechaHastaSalida = ejbFacade.getSystemDateOnly();
        fechaDesde = null;
        fechaHasta = null;
        ordenarPor = "1";
        tipoOrden = "1";
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        pieModel2 = new PieChartModel();
        pieModelO = new PieChartModel();

        estiloPieDepartamento = "visibility: hidden;width:100px;height: 100px";

        estiloPieObjetoGasto = "visibility: hidden;width:100px;height: 100px";

        super.initParams();

        buscarPorFechaSalida();
    }

    @Override
    public SalidasArticulo prepareCreate(ActionEvent event) {
        SalidasArticulo nroFor = super.prepareCreate(null);
        nroFor.setNroFormulario(obtenerProximoNroFormulario());
        file = null;
        detalles = null;
        detalleSelected = null;
        articulo = null;
        pedidosArticulo = null;
        cantidad = 0;
        // nroFormulario = 0;
        neto = null;
        stock = null;

        totalSalida = BigDecimal.ZERO;

        Inventarios inv = null;
        try {
            inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (inv == null) {
            JsfUtil.addErrorMessage("Para ingresar un movimiento debe haber al menos un inventario vigente");
            return null;
        }

        salidaArticulo = super.prepareCreate(event);

        if (salidaArticulo != null) {
            // salidaArticulo.setNroFormulario(nroFormulario);
            salidaArticulo.setPersona(null);
            salidaArticulo.setDepartamento(null);
            salidaArticulo.setInventario(inv);
        }

        return salidaArticulo;
    }

    public void prepareEdit() {
        if (getSelected() != null) {
            actualizarListas(getSelected().getSede());
        }
        detallesEdit = null;
        detalleSelectedEdit = null;
        articuloEdit = null;
        cantidadEdit = 0;
        salidaArticuloOri = null;
        neto = null;
        stock = null;

        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Debe seleccionar un formulario");
            return;
        }

        nroFormularioEdit = getSelected().getNroFormulario();

        salidaArticuloOri = new SalidasArticulo();

        salidaArticuloOri.setDepartamento(getSelected().getDepartamento());
        salidaArticuloOri.setEmpresa(getSelected().getEmpresa());
        salidaArticuloOri.setFechaHoraAlta(getSelected().getFechaHoraAlta());
        salidaArticuloOri.setFechaHoraUltimoEstado(getSelected().getFechaHoraUltimoEstado());
        salidaArticuloOri.setId(getSelected().getId());
        salidaArticuloOri.setInventario(getSelected().getInventario());
        salidaArticuloOri.setNroFormulario(getSelected().getNroFormulario());
        salidaArticuloOri.setPersona(getSelected().getPersona());
        salidaArticuloOri.setPersonas(getSelected().getPersonas());
        salidaArticuloOri.setUsuarioAlta(getSelected().getUsuarioAlta());
        salidaArticuloOri.setUsuarioUltimoEstado(getSelected().getUsuarioUltimoEstado());

        detallesEdit
                = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class
                ).setParameter("salidaArticulo", getSelected()).getResultList();
        detallesEditOri
                = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class
                ).setParameter("salidaArticulo", getSelected()).getResultList();
        /*
        detallesEditOri = new ArrayList<>();

        for (DetallesSalidaArticulo det : detallesEdit) {
            
            detallesEditOri.add(det);
        }
         */
    }

    public boolean verificarNroFormularioCreate() {
        return verificarNroFormulario(salidaArticulo.getNroFormulario());
    }

    public boolean verificarNroFormularioEdit() {
        if (!nroFormularioEdit.equals(getSelected().getNroFormulario())) {
            return verificarNroFormulario(getSelected().getNroFormulario());
        }
        return true;
    }

    public boolean verificarNroFormulario(Integer nro) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        Date fecha = ejbFacade.getSystemDate();

        CantidadItem cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from salidas_articulo where nro_formulario = ?1 and date_format(fecha_salida,'%Y') = ?2", CantidadItem.class
        ).setParameter(1, nro).setParameter(2, sdf.format(fecha)).getSingleResult();

        if (cant.getCantidad() > 0) {
            JsfUtil.addErrorMessage("Nro de formulario ya existe");
            return false;
        }

        return true;

    }

    public String obtenerInstalador1() {

        return url + "/instaladores/firmajem-1.0.exe";
    }

    public String obtenerInstalador2() {

        File file = new File(par.getRutaArchivos() + "/registro_firma.cmd");
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setHeader("Content-Disposition", "attachment;filename=registro_firma.cmd");
        response.setContentLength((int) file.length());
        ServletOutputStream out = null;

        try {
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            int i = 0;
            while ((i = input.read(buffer)) != -1) {
                out.write(buffer);
                out.flush();
            }
            FacesContext.getCurrentInstance().getResponseComplete();
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
        return null;
    }

    private List<Articulos> obtenerListaArticulos(Sede sede) {
        if (sede != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findByDeposito", Articulos.class
            ).setParameter("sede", sede).getResultList();
        }
        return null;
    }

    public void actualizarListas(Sede sede) {
        listaArticulos = obtenerListaArticulos(sede);
    }

    public void resetearListas(Sede sede) {
        articulo = null;

        if (getSelected() != null) {
            getSelected().setArticulo(null);

        }
        actualizarListas(sede);
    }

    /* private BigDecimal calcularTotal(BigDecimal cantidad, BigDecimal precio){
       BigDecimal precioTotal;
        
       return monto.divide(div, 0, RoundingMode.HALF_UP);
    }
     */
    private List<DetallesEntradaArticulo> obtenerDetallesEntradaArticulo(Articulos art, Integer cantidad) {
        return null;
    }

    public void cargarDatosAdicionales() {
        if (articulo != null) {
            marca = articulo.getMarca();
            modelo = articulo.getModelo();
            objetosGasto = articulo.getObjetoGasto();
            codigoSuministro = articulo.getCodigoSuministro();
            // costoUnitario;
            cantidad = 0;
            fechaVencimiento = null;
            costoUnitario = BigDecimal.ZERO;
            codigoSuministro = "";

            cantTotalSalida = 0;

            if (detalles != null) {
                for (DetallesSalidaArticulo det : detalles) {
                    if (articulo.equals(det.getArticulo())) {
                        cantTotalSalida += det.getCantidad();
                    }
                }
            }

            Date fecha = ejbFacade.getSystemDate();
            List<ReportesInventario> listaInv = inventarioController.obtenerDatosInventario2(fecha, articulo);

            stock = 0;
            for (ReportesInventario rep : listaInv) {
                if (articulo.getCodigo().equals(rep.getArticulo())) {
                    stock = rep.getStock().intValue();
                    break;
                }
            }
        }

    }

    public Integer getNetoSalida() {
        // return (neto==null || cantTotalSalida == null)?0:neto - cantTotalSalida;

        if (stock == null || cantTotalSalida == null) {
            return 0;
        } else {
            return stock - cantTotalSalida;
        }
    }

    public void cargarDatosAdicionalesEdit() {
        if (articuloEdit != null) {
            //precioUnitario = articuloEdit.getPrecioUnitario;
            marca = articuloEdit.getMarca();
            modelo = articuloEdit.getModelo();
            objetosGasto = articuloEdit.getObjetoGasto();
            codigoSuministro = articuloEdit.getCodigoSuministro();

            Date fecha = ejbFacade.getSystemDate();

            List<ReportesInventario> listaInv = inventarioController.obtenerDatosInventario(fecha);

            stock = 0;
            for (ReportesInventario rep : listaInv) {
                if (articuloEdit.getCodigo().equals(rep.getArticulo())) {
                    stock = rep.getStock().intValue();
                    break;
                }
            }
        }
    }

    public SalidasArticuloController() {
        // Inform the Abstract parent controller of the concrete SalidasArticulo Entity
        super(SalidasArticulo.class
        );
    }

    private Integer obtenerProximoNroFormulario() {

        Object obj = this.ejbFacade.getEntityManager().createQuery("select COALESCE(max(p.nroFormulario),0) from SalidasArticulo p").getSingleResult();

        if (obj instanceof Long) {
            return ((Long) obj).intValue() + 1;
        } else {
            return ((Integer) obj) + 1;
        }
    }

    public String getOrdenarPor() {
        return ordenarPor;
    }

    public void setOrdenarPor(String ordenarPor) {
        this.ordenarPor = ordenarPor;
    }

    public Collection<DetallesSalidaArticulo> getDetallesEdit() {
        return detallesEdit;
    }

    public void setDetallesEdit(Collection<DetallesSalidaArticulo> detallesEdit) {
        this.detallesEdit = detallesEdit;
    }

    public String getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(String tipoOrden) {
        this.tipoOrden = tipoOrden;
    }

    public Departamentos getDepartamentoFiltro() {
        return departamentoFiltro;
    }

    public void setDepartamentoFiltro(Departamentos departamentoFiltro) {
        this.departamentoFiltro = departamentoFiltro;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaDesdeSalida() {
        return fechaDesdeSalida;
    }

    public void setFechaDesdeSalida(Date fechaDesdeSalida) {
        this.fechaDesdeSalida = fechaDesdeSalida;
    }

    public Date getFechaHastaSalida() {
        return fechaHastaSalida;
    }

    public void setFechaHastaSalida(Date fechaHastaSalida) {
        this.fechaHastaSalida = fechaHastaSalida;
    }

    public DetallesSalidaArticulo getDetalleSelectedEdit() {
        return detalleSelectedEdit;
    }

    public void setDetalleSelectedEdit(DetallesSalidaArticulo detalleSelectedEdit) {
        this.detalleSelectedEdit = detalleSelectedEdit;
    }

    public Articulos getArticuloEdit() {
        return articuloEdit;
    }

    public void setArticuloEdit(Articulos articuloEdit) {
        this.articuloEdit = articuloEdit;
    }

    public Integer getCantidadEdit() {
        return cantidadEdit;
    }

    public void setCantidadEdit(Integer cantidadEdit) {
        this.cantidadEdit = cantidadEdit;
    }

    public Articulos getArticuloFiltro() {
        return articuloFiltro;
    }

    public void setArticuloFiltro(Articulos articuloFiltro) {
        this.articuloFiltro = articuloFiltro;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public SalidasArticulo getSalidaArticulo() {
        return salidaArticulo;
    }

    public void setSalidaArticulo(SalidasArticulo salidaArticulo) {
        this.salidaArticulo = salidaArticulo;
    }

    public DetallesSalidaArticulo getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DetallesSalidaArticulo detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    public Collection<DetallesSalidaArticulo> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesSalidaArticulo> detalles) {
        this.detalles = detalles;
    }

    public Collection<DetallesSalidaArticulo> getDetallesEditOri() {
        return detallesEditOri;
    }

    public void setDetallesEditOri(Collection<DetallesSalidaArticulo> detallesEditOri) {
        this.detallesEditOri = detallesEditOri;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        personaController.setSelected(null);
        departamentoController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        /*
        if (this.getSelected() == null && this.getItems() != null) {
            if (!this.getItems().isEmpty()) {
                this.setSelected(getItems().iterator().next());
            }
        }
         */
        seleccionar();
    }

    public boolean verSalidaArticuloEnDpto() {

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class
            ).setParameter("usuario", usuario.getId()).setParameter("rol", -16).getSingleResult();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return false;
    }

    public boolean verTodosSalidaArticulo() {

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class
            ).setParameter("usuario", usuario.getId()).setParameter("rol", -17).getSingleResult();
            return true;

        } catch (Exception e) {
            // e.printStackTrace();
        }

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class
            ).setParameter("usuario", usuario.getId()).setParameter("rol", -18).getSingleResult();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return false;
    }

    @Override
    public Collection<SalidasArticulo> getItems() {
        return super.getItems2();
    }

    public String datePattern() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public String datePattern2() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }

    public void buscarPorFechaAlta() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();

            if ("1".equals(ordenarPor)) {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_hora_alta >= ?1 AND fecha_hora_alta <= ?2 ORDER BY CONVERT(nro_formulario,signed) DESC;", SalidasArticulo.class
                    ).setParameter(1, fechaDesde).setParameter(2, nuevaFechaHasta).getResultList());

                } else {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_hora_alta >= ?1 AND fecha_hora_alta <= ?2 ORDER BY CONVERT(nro_formulario,signed) ASC;", SalidasArticulo.class
                    ).setParameter(1, fechaDesde).setParameter(2, nuevaFechaHasta).getResultList());

                }
                // setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedNroFormulario", SalidasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("tipo", "signed").getResultList());
            } else {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrdered", SalidasArticulo.class
                    ).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());

                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedAsc", SalidasArticulo.class
                    ).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
                }

            }

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                // detalles = art.getDetallesSalidaArticuloCollection();
                detalles
                        = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class
                        ).setParameter("salidaArticulo", getSelected()).getResultList();
                detalleSelected = null;
            } else {
                detalles = null;
                detalleSelected = null;
                setSelected(null);
            }

        }
    }

    public void buscarPorFechaSalida() {
        if (fechaDesdeSalida == null || fechaHastaSalida == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHastaSalida);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHastaSalida = cal.getTime();

            if ("1".equals(ordenarPor)) {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_salida >= ?1 AND fecha_salida <= ?2 ORDER BY CONVERT(nro_formulario,signed) DESC;", SalidasArticulo.class
                    ).setParameter(1, fechaDesdeSalida).setParameter(2, nuevaFechaHastaSalida).getResultList());

                } else {
                    setItems(this.ejbFacade.getEntityManager().createNativeQuery(
                            "select * from salidas_articulo where fecha_salida >= ?1 AND fecha_salida <= ?2 ORDER BY CONVERT(nro_formulario,signed) ASC;", SalidasArticulo.class
                    ).setParameter(1, fechaDesdeSalida).setParameter(2, nuevaFechaHastaSalida).getResultList());

                }
                // setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedNroFormulario", SalidasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("tipo", "signed").getResultList());
            } else {
                if ("1".equals(tipoOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrdered", SalidasArticulo.class
                    ).setParameter("fechaDesde", fechaDesdeSalida).setParameter("fechaHasta", nuevaFechaHastaSalida).getResultList());

                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedAsc", SalidasArticulo.class
                    ).setParameter("fechaDesde", fechaDesdeSalida).setParameter("fechaHasta", nuevaFechaHastaSalida).getResultList());
                }
            }

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                // detalles = art.getDetallesSalidaArticuloCollection();
                detalles
                        = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class
                        ).setParameter("salidaArticulo", getSelected()).getResultList();

                totalSalida = BigDecimal.ZERO;

                for (DetallesSalidaArticulo det : detalles) {
//                    totalCostoTotal = totalCostoTotal.add(det.)                

                    detalleSelected = null;
                }
                //else {
                detalles = null;
                detalleSelected = null;
                setSelected(null);

            }

        }
    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class
            ).setParameter("salidaArticulo", getSelected()).getResultList();

            totalSalida = BigDecimal.ZERO;
            for (DetallesSalidaArticulo det : detalles) {
                totalSalida = totalSalida.add(det.getCostoTotal() == null ? BigDecimal.ZERO : det.getCostoTotal());
            }

        } else {
            detalles = null;
        }

    }

    public void buscarPorFechaAlta2() {
        if (nroFormulario == null || "".equals(nroFormulario)) {

            if (verSalidaArticuloEnDpto()) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedNroFormularioAsignado", SalidasArticulo.class
                ).setParameter("nroFormulario", nroFormulario).setParameter("departamento", usuario.getDepartamento()).getResultList());

            } else if (verTodosSalidaArticulo()) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedNroFormulario", SalidasArticulo.class
                ).setParameter("nroFormulario", nroFormulario).getResultList());

            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findOrderedNroFormularioAsignadoPersona", SalidasArticulo.class
                ).setParameter("nroFormulario", nroFormulario).setParameter("usuario", usuario).getResultList());
            }
        }
    }

    /**
     * Sets the "items" attribute with a collection of DetallesSalidaArticulo
     * entities that are retrieved from SalidasArticulo?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for DetallesSalidaArticulo page
     */
    public String navigateDetallesSalidaArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesSalidaArticulo_items", this.getSelected().getDetallesSalidaArticuloCollection());
        }
        return "/pages/detallesSalidaArticulo/index";
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePersona(ActionEvent event) {
        if (this.getSelected() != null && personaController.getSelected() == null) {
            personaController.setSelected(this.getSelected().getPersona());
        }
    }

    public void prepareEmpresas(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareArchivosReporte(ActionEvent event) {
        if (this.getSelected() != null && archivosReporteController.getSelected() == null) {
            archivosReporteController.setSelected(this.getSelected().getArchivosReporte());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSede(ActionEvent event) {
        if (this.getSelected() != null && sedeController.getSelected() == null) {
            sedeController.setSelected(this.getSelected().getSede());
        }
    }

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DetallesSalidaArticulo det : detalles) {
                if (det.equals(detalleSelected)) {

                    totalSalida = totalSalida.subtract(detalleSelected.getCostoTotal());

                    cantTotalSalida -= detalleSelected.getCantidad();
                    detalles.remove(det);
                    break;
                }
            }
        }
    }

    public void borrarDetalleEdit() {
        if (detallesEdit != null && detalleSelectedEdit != null) {
            for (DetallesSalidaArticulo det : detallesEdit) {
                if (det.getArticulo().getCodigo().equals(detalleSelectedEdit.getArticulo().getCodigo())) {
                    detallesEdit.remove(det);
                    break;
                }
            }
        }
    }

    /*
    
    public void borrarDetalle() {
        
        
        Collection<DetallesSalidaArticulo> detallesNuevo = new ArrayList<>();
        
        boolean encontrado = false;
        
        if (detalles != null && detalleSelected != null) {
            for (DetallesSalidaArticulo det : detalles) {
                if (det.getArticulo().getCodigo().equals(detalleSelected.getArticulo().getCodigo())) {
                    //detalles.remove(det);
                    encontrado = true;
                }else{
                    if(!encontrado){
                        detallesNuevo.add(det);
                    }else{
                        DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                        Date fecha = ejbFacade.getSystemDate();

                        dsa.setItem(det.getItem() - 1);
                        dsa.setCantidad(det.getCantidad());
                        dsa.setArticulo(det.getArticulo());
                        dsa.setId(Integer.valueOf(format.format(fecha)));
                        dsa.setInventario(det.getInventario());
                        detallesNuevo.add(dsa);
                    }
                }
            }
            
            detalles = detallesNuevo;
        }
    }
     */
    @Override
    public void delete(ActionEvent event) {

        Articulos art = null;

        Collection<DetallesSalidaArticulo> col = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class
        ).setParameter("salidaArticulo", getSelected()).getResultList();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        Date fecha = ejbFacade.getSystemDate();

        for (DetallesSalidaArticulo det : col) {
            art = det.getArticulo();

            art.setStock(art.getStock() + det.getCantidad());

            articuloController.setSelected(art);

            articuloController.save(event);

            detallesSalidaArticuloController.setSelected(det);

            detallesSalidaArticuloController.getSelected().setFechaHoraBorrado(fecha);
            detallesSalidaArticuloController.getSelected().setUsuarioBorrado(usu);

            detallesSalidaArticuloController.save(event);

            detallesSalidaArticuloController.delete(event);
        }

        getSelected().setFechaHoraBorrado(fecha);
        getSelected().setUsuarioBorrado(usu);

        super.save(event);

        super.delete(event);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaHastaSalida);
        cal.add(Calendar.DATE, 1);
        Date nuevaFechaHasta = cal.getTime();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findFechaEntradaOrdered", SalidasArticulo.class
        ).setParameter("fechaDesde", fechaDesdeSalida).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
        setSelected(null);

        detalleSelected = null;
        detalles = null;

    }

    public List<ReporteInventarioValorizado> obtenerEntradas(Inventarios inventario, Articulos articulo, Integer cantidad, Collection<DetallesSalidaArticulo> det) {
        // List<ReporteInventarioValorizado> lista = ejbFacade.getEntityManager().createNamedQuery("ReporteInventarioValorizado.findByArticulo", ReporteInventarioValorizado.class).setParameter("inventario", inventario).setParameter("articulo", articulo).getResultList();

        List<ReporteInventarioValorizado> lista = ejbFacade.getEntityManager().createNativeQuery("select * from reporte_inventario_valorizado AS e WHERE e.inventario = ? AND e.articulo = ? ORDER BY fecha_vencimiento, e.fecha_entrada, e.costo_unitario", ReporteInventarioValorizado.class
        ).setParameter(1, inventario.getId()).setParameter(2, articulo.getCodigo()).getResultList();

        List<ReporteInventarioValorizado> resp = new ArrayList<>();

        Integer cantidadNeto;

        Integer cantidadSobrante = cantidad;
        for (ReporteInventarioValorizado rep : lista) {

            cantidadNeto = rep.getCantEntrada() - rep.getCantSalida();

            if (cantidadNeto > 0) {
                if (det != null) {
                    for (DetallesSalidaArticulo d : det) {
                        if (d.getDetalleInventario() != null && rep.getDetalleInventario() != null) {
                            if (rep.getDetalleInventario().equals(d.getDetalleInventario())) {
                                cantidadNeto = cantidadNeto - d.getCantidad();
                            }
                        }
                        if (d.getDetalleEntradaArticulo() != null && rep.getDetalleEntradaArticulo() != null) {
                            if (rep.getDetalleEntradaArticulo().equals(d.getDetalleEntradaArticulo())) {
                                cantidadNeto = cantidadNeto - d.getCantidad();
                            }
                        }
                    }

                    for (ReporteInventarioValorizado d : resp) {
                        if (d.getDetalleInventario() != null && rep.getDetalleInventario() != null) {
                            if (rep.getDetalleInventario().equals(d.getDetalleInventario())) {
                                cantidadNeto = cantidadNeto - d.getCantEntrada();
                            }
                        }
                        if (d.getDetalleEntradaArticulo() != null && rep.getDetalleEntradaArticulo() != null) {
                            if (rep.getDetalleEntradaArticulo().equals(d.getDetalleEntradaArticulo())) {
                                cantidadNeto = cantidadNeto - d.getCantEntrada();
                            }
                        }
                    }
                }
            }

            if (cantidadNeto > 0) {
                ReporteInventarioValorizado item = new ReporteInventarioValorizado();
                if (cantidadNeto > cantidadSobrante) {
                    item.setCantEntrada(cantidadSobrante);
                } else {
                    item.setCantEntrada(cantidadNeto);
                }

                item.setCostoUnitario(rep.getCostoUnitario());
                item.setCostoTotal(rep.getCostoUnitario().multiply(new BigDecimal(item.getCantEntrada())));

                item.setTipo(rep.getTipo());
                item.setDetalleEntradaArticulo(rep.getDetalleEntradaArticulo());
                item.setDetalleInventario(rep.getDetalleInventario());
                item.setInventario(rep.getInventario());
                item.setFechaVencimiento(rep.getFechaVencimiento());
                item.setArticulo(articulo);

                resp.add(item);

                /*
                if("E".equals(rep.getTipo())){
                    item.setDetalleEntradaArticulo(rep.getDetalleEntradaArticulo());
                    item.setEntradaArticulo(rep.getDetalleEntradaArticulo().getEntradaArticulo());
                }else{
                    item.setDetalleInventario(rep.getDetalleInventario());
                }
                
                item.setDetalleSalidaArticulo(null);
                item.setInventario(rep.getInventario());
                item.setFechaVencimiento(rep.getFechaVencimiento());
                item.setSalidaArticulo(null);
                item.setArticulo(articulo);
                
                resp.add(item);
                 */
                cantidadSobrante = cantidadSobrante - (cantidadNeto - rep.getCantSalida());
                if (cantidadSobrante <= 0) {
                    break;
                }
            }
        }

        if (cantidadSobrante > 0) {
            resp = new ArrayList<>();
        }

        return resp;
    }

    public void agregar() {

        if (articulo == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
            return;
        }

        if (cantidad == null) {
            JsfUtil.addErrorMessage("Debe especificar la cantidad");
            return;
        } else if (cantidad <= 0) {
            JsfUtil.addErrorMessage("La cantidad debe ser mayor a cero");
            return;
        } else if (stock - cantTotalSalida - cantidad < 0) {
            JsfUtil.addErrorMessage("La cantidad del artículo seleccionado no esta disponible en stock.");
            return;
        }

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        SimpleDateFormat format = new SimpleDateFormat("ddHHmmss");

        int contador = 0;

        List<ReporteInventarioValorizado> lista = obtenerEntradas(salidaArticulo.getInventario(), articulo, cantidad, detalles);

        if (lista == null) {
            JsfUtil.addErrorMessage("La cantidad del artículo seleccionado no esta disponible en stock");
            return;
        }

        if (lista.isEmpty()) {
            JsfUtil.addErrorMessage("La cantidad del artículo seleccionado no esta disponible en stock");
            return;
        }

        for (ReporteInventarioValorizado item : lista) {
            /*
            DetallesSalidaArticulo actual = new DetallesSalidaArticulo(dsa);
            actual.setId(Integer.valueOf(format.format(fecha) + contador));
            actual.setCantidad(item.getCantidad());
            actual.setArticulo(articulo);
            
            if (!verificarNeto(actual)) {
                JsfUtil.addErrorMessage("El artículo seleccionado no esta disponible en stock");
                return;
            }
             */

            Date fecha = ejbFacade.getSystemDate();
            DetallesSalidaArticulo dsa = new DetallesSalidaArticulo();
            dsa.setItem(detalles.size() + 1);
            dsa.setCantidad(item.getCantEntrada());
            dsa.setArticulo(articulo);
            dsa.setSede(articulo.getRubro().getSede());

            dsa.setMarca(marca);
            dsa.setCostoUnitario(item.getCostoUnitario());
            dsa.setCostoTotal(item.getCostoUnitario() == null ? BigDecimal.ZERO : item.getCostoUnitario().multiply(new BigDecimal(item.getCantEntrada())));

            totalSalida = totalSalida.add(dsa.getCostoTotal());

            cantTotalSalida += dsa.getCantidad();

            dsa.setModelo(modelo);
            dsa.setObjetoGasto(objetosGasto);

            dsa.setCodigoSuministro(codigoSuministro);

            contador++;
            dsa.setId(Integer.valueOf(format.format(fecha) + contador));
            dsa.setInventario(salidaArticulo.getInventario());

            dsa.setEntradaArticulo(item.getDetalleEntradaArticulo() == null ? null : item.getDetalleEntradaArticulo().getEntradaArticulo());
            dsa.setDetalleEntradaArticulo(item.getDetalleEntradaArticulo());
            dsa.setDetalleInventario(item.getDetalleInventario());
            dsa.setFechaVencimiento(item.getDetalleEntradaArticulo() == null ? item.getDetalleInventario().getFechaVencimiento() : item.getDetalleEntradaArticulo().getFechaVencimiento());

            detalles.add(dsa);
        }
    }

    /*
    public void agregar() {

        if (articulo == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
            return;
        }

        if (cantidad == null) {
            JsfUtil.addErrorMessage("Debe especificar la cantidad");
            return;
        } else if (cantidad <= 0) {
            JsfUtil.addErrorMessage("La cantidad debe ser mayor a cero");
            return;
        }

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        boolean encontro = false;

        int contador = 0;
        for (DetallesSalidaArticulo det : detalles) {
            contador++;
            if (det.getArticulo().getCodigo().equals(articulo.getCodigo())) {

                // if (articulo.getStock() < det.getCantidad() + cantidad) {
                DetallesSalidaArticulo actual = new DetallesSalidaArticulo(det);
                actual.setCantidad(actual.getCantidad() + cantidad);
                if (!verificarNeto(actual)) {
                    JsfUtil.addErrorMessage("El artículo seleccionado no esta disponible en stock");
                    return;
                }

                detalles.remove(det);

                DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                Date fecha = ejbFacade.getSystemDate();

                dsa.setItem(det.getItem());
                dsa.setCantidad(det.getCantidad() + cantidad);
                dsa.setArticulo(articulo);
                dsa.setId(Integer.valueOf(format.format(fecha)));
                dsa.setInventario(salidaArticulo.getInventario());

                dsa.setMarca(marca);
                dsa.setModelo(modelo);
                dsa.setObjetoGasto(objetosGasto);
                dsa.setCodigoSuministro(codigoSuministro);
              //  dsa.setCostoUnitario(costoUnitario);
                
                detalles.add(dsa);
                encontro = true;
                break;
            }
        }

        if (!encontro) {
            DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();
            
            // if (articulo.getStock() < cantidad) {
            //    JsfUtil.addErrorMessage("La cantidad del articulo no debe supera el stock actual");
            //    return;
            // }

            DetallesSalidaArticulo actual = new DetallesSalidaArticulo(dsa);
            actual.setId(Integer.valueOf(format.format(fecha)));
            actual.setCantidad(cantidad);
            actual.setArticulo(articulo);
            if (!verificarNeto(actual)) {
                JsfUtil.addErrorMessage("El artículo seleccionado no esta disponible en stock");
                return;
            }

            dsa.setItem(contador + 1);
            dsa.setCantidad(cantidad);
            dsa.setArticulo(articulo);

            dsa.setMarca(marca);
            dsa.setModelo(modelo);
            dsa.setObjetoGasto(objetosGasto);

            dsa.setCodigoSuministro(codigoSuministro);

            dsa.setId(Integer.valueOf(format.format(fecha)));
            dsa.setInventario(salidaArticulo.getInventario());

            detalles.add(dsa);
        }

        // articulo = null;
        // cantidad = null;
    }
     */
    public void agregarEdit() {

        if (getSelected() == null) {
            JsfUtil.addErrorMessage("Debe seleccionar un formulario");
            return;
        }

        if (articuloEdit == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
            return;
        }

        if (cantidadEdit == null) {
            JsfUtil.addErrorMessage("Debe especificar la cantidad");
            return;
        } else if (cantidadEdit <= 0) {
            JsfUtil.addErrorMessage("La cantidad debe ser mayor a cero");
            return;
        }

        if (detallesEdit == null) {
            detallesEdit = new ArrayList<>();
        }

        boolean encontro = false;

        int cantidadEditOri;

        DetallesSalidaArticulo detOriEncontrado = null;
        int contador = 0;
        for (DetallesSalidaArticulo det : detallesEdit) {
            contador++;
            if (det.getArticulo().getCodigo().equals(articuloEdit.getCodigo())) {

                cantidadEditOri = 0;
                detOriEncontrado = null;

                for (DetallesSalidaArticulo detOri : detallesEditOri) {
                    if (detOri.getArticulo().getCodigo().equals(articuloEdit.getCodigo())) {
                        cantidadEditOri = detOri.getCantidad();
                        detOriEncontrado = detOri;
                        break;
                    }
                }
                /*
                if (articuloEdit.getStock() < (det.getCantidad() + cantidadEdit - cantidadEditOri)) {
                    JsfUtil.addErrorMessage("La diferencia entre la cantidad actual (" + cantidadEditOri + ") y la cantidad nueva (" + (det.getCantidad() + cantidadEdit) + ") es mayor que el stock actual. Diferencia (" + (det.getCantidad() + cantidadEdit - cantidadEditOri) + "), stock actual (" + articuloEdit.getStock() + ")");
                    return;
                }
                 */

                DetallesSalidaArticulo actual = new DetallesSalidaArticulo(det);
                actual.setCantidad(actual.getCantidad() + cantidadEdit);
                if (!verificarNeto(actual)) {
                    JsfUtil.addErrorMessage("El artículo seleccionado no esta disponible en stock");
                    return;
                }

                detallesEdit.remove(det);

                DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                // SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
                // Date fecha = ejbFacade.getSystemDate();
                dsa.setItem(det.getItem());
                dsa.setCantidad(det.getCantidad() + cantidadEdit);
                dsa.setArticulo(det.getArticulo());
                //dsa.setId(Integer.valueOf(format.format(fecha)));
                dsa.setId(det.getId());
                dsa.setInventario(det.getInventario());

                dsa.setDepartamento(det.getDepartamento());
                dsa.setEmpresa(det.getEmpresa());
                dsa.setFechaHoraAlta(det.getFechaHoraAlta());
                dsa.setFechaHoraUltimoEstado(det.getFechaHoraUltimoEstado());
                dsa.setNroFormulario(det.getNroFormulario());
                dsa.setPersona(det.getPersona());
                dsa.setSalidaArticulo(det.getSalidaArticulo());
                dsa.setUsuarioAlta(det.getUsuarioAlta());
                dsa.setUsuarioUltimoEstado(det.getUsuarioUltimoEstado());

                detallesEdit.add(dsa);
                encontro = true;
                break;
            }
        }

        if (!encontro) {

            DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            cantidadEditOri = 0;
            detOriEncontrado = null;
            for (DetallesSalidaArticulo detOri : detallesEditOri) {
                if (detOri.getArticulo().getCodigo().equals(articuloEdit.getCodigo())) {
                    cantidadEditOri = detOri.getCantidad();
                    detOriEncontrado = detOri;
                    break;
                }
            }
            /*
            if (articuloEdit.getStock() < cantidadEdit - cantidadEditOri) {
                JsfUtil.addErrorMessage("La diferencia entre la cantidad actual (" + cantidadEditOri + ") y la cantidad nueva (" + cantidadEdit + ") es mayor que el stock actual. Diferencia (" + (cantidadEdit - cantidadEditOri) + "), stock actual (" + articuloEdit.getStock() + ")");

                return;
            }
             */

            DetallesSalidaArticulo actual = new DetallesSalidaArticulo(dsa);
            actual.setId(Integer.valueOf(format.format(fecha)));
            actual.setCantidad(cantidadEdit);
            actual.setArticulo(articuloEdit);
            if (!verificarNeto(actual)) {
                JsfUtil.addErrorMessage("El artículo seleccionado no esta disponible en stock");
                return;
            }

            if (detOriEncontrado == null) {
                dsa.setItem(contador + 1);
                dsa.setCantidad(cantidadEdit);
                dsa.setArticulo(articuloEdit);
                dsa.setId(Integer.valueOf(format.format(fecha)));
                dsa.setInventario(getSelected().getInventario());
                dsa.setSalidaArticulo(getSelected());
                dsa.setNroFormulario(contador);
                dsa.setPersona(getSelected().getPersona());
                dsa.setDepartamento(getSelected().getDepartamento());

            } else {
                dsa.setItem(contador + 1);
                dsa.setCantidad(cantidadEdit);
                dsa.setArticulo(articuloEdit);
                dsa.setId(detOriEncontrado.getId());
                dsa.setInventario(detOriEncontrado.getInventario());
                dsa.setSalidaArticulo(detOriEncontrado.getSalidaArticulo());
                dsa.setFechaHoraUltimoEstado(detOriEncontrado.getFechaHoraUltimoEstado());
                dsa.setUsuarioUltimoEstado(detOriEncontrado.getUsuarioUltimoEstado());
                dsa.setFechaHoraAlta(detOriEncontrado.getFechaHoraAlta());
                dsa.setUsuarioAlta(detOriEncontrado.getUsuarioAlta());
                dsa.setEmpresa(detOriEncontrado.getEmpresa());
                dsa.setNroFormulario(detOriEncontrado.getNroFormulario());
                dsa.setPersona(detOriEncontrado.getPersona());
                dsa.setDepartamento(detOriEncontrado.getDepartamento());
            }
            detallesEdit.add(dsa);
        }

        // cantidad = null;
        // articulo = null;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        String key = event.getRowKey();

        boolean encontro = false;
        if (key != null && !((Integer) oldValue).equals((Integer) newValue)) {
            int cantidadEditOri;

            for (DetallesSalidaArticulo det : detallesEdit) {
                if (det.getId().equals(Integer.valueOf(key))) {
                    encontro = true;
                    cantidadEditOri = 0;

                    if (((Integer) newValue).intValue() <= 0) {
                        JsfUtil.addErrorMessage("No se puede cambiar la cantidad a un valor igual o menor a cero");
                        det.setCantidad(cantidadEditOri);
                        break;
                    }

                    for (DetallesSalidaArticulo detOri : detallesEditOri) {
                        if (detOri.getArticulo().getCodigo().equals(det.getArticulo().getCodigo())) {
                            cantidadEditOri = detOri.getCantidad();
                            break;
                        }
                    }

                    if (det.getArticulo().getStock() < (((Integer) newValue) - cantidadEditOri)) {
                        JsfUtil.addErrorMessage("La diferencia entre la cantidad actual (" + cantidadEditOri + ") y la cantidad nueva (" + newValue + ") es mayor que el stock actual. Diferencia (" + (((Integer) newValue) - cantidadEditOri) + "), stock actual (" + det.getArticulo().getStock() + ")");
                        det.setCantidad(cantidadEditOri);
                        return;
                    }

                    // det.setCantidad(((Integer) newValue));
                    /*
                    detallesEdit.remove(det);

                    DetallesSalidaArticulo dsa = detallesSalidaArticuloController.prepareCreate(null);

                    SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                    Date fecha = ejbFacade.getSystemDate();

                    dsa.setId(det.getId());
                    dsa.setItem(det.getItem());
                    dsa.setCantidad(((Integer) newValue));
                    dsa.setArticulo(det.getArticulo());
                    dsa.setInventario(det.getInventario());
                    dsa.setDepartamento(det.getDepartamento());
                    dsa.setEmpresa(det.getEmpresa());
                    dsa.setFechaHoraAlta(det.getFechaHoraAlta());
                    dsa.setFechaHoraUltimoEstado(det.getFechaHoraUltimoEstado());
                    dsa.setNroFormulario(det.getNroFormulario());
                    dsa.setPersona(det.getPersona());
                    dsa.setSalidaArticulo(det.getSalidaArticulo());
                    dsa.setUsuarioAlta(det.getUsuarioAlta());
                    dsa.setUsuarioUltimoEstado(det.getUsuarioUltimoEstado());
                    detallesEdit.add(dsa);
                     */
                    break;
                }
            }
        }

        if (!encontro) {
            JsfUtil.addErrorMessage("Error: no se puede validar el stock. Cancelando");
        }
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            if (!verificarNroFormularioEdit()) {
                return;
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            if (!verificarNeto(fecha, detallesEdit)) {
                JsfUtil.addErrorMessage(mensajeNeto);
                return;
            }

            if (detallesEdit != null) {
                if (detallesEdit.isEmpty()) {
                    JsfUtil.addErrorMessage("Debe haber al menos un detalle");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Debe haber al menos un detalle.");
                return;
            }

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setNroFormulario(nroFormularioEdit);

            super.save(event);

            // Guardar registro de cambios
            SalidasArticuloCambios sac = salidaArticuloCambioController.prepareCreate(event);

            sac.setSalidaArticulo(getSelected());
            sac.setDepartamento(salidaArticuloOri.getDepartamento());
            sac.setEmpresa(salidaArticuloOri.getEmpresa());
            sac.setFechaHoraAlta(salidaArticuloOri.getFechaHoraAlta());
            sac.setFechaHoraUltimoEstado(salidaArticuloOri.getFechaHoraUltimoEstado());
            sac.setInventario(salidaArticuloOri.getInventario());
            sac.setNroFormulario(salidaArticuloOri.getNroFormulario());
            sac.setPersona(salidaArticuloOri.getPersona());
            sac.setPersonas(salidaArticuloOri.getPersonas());
            sac.setFechaHoraCambio(fecha);
            sac.setUsuarioAlta(salidaArticuloOri.getUsuarioAlta());
            sac.setUsuarioCambio(usu);
            sac.setUsuarioUltimoEstado(salidaArticuloOri.getUsuarioUltimoEstado());

            String cambio = "IGUAL";

            if (getSelected().getDepartamento() != null) {
                if (!getSelected().getDepartamento().equals(salidaArticuloOri.getDepartamento())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getDepartamento() != null) {
                cambio = "MODIFICADO";
            }

            sac.setDepartamentoNuevo(getSelected().getDepartamento());

            if (getSelected().getInventario() != null) {
                if (!getSelected().getInventario().equals(salidaArticuloOri.getInventario())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getInventario() != null) {
                cambio = "MODIFICADO";
            }

            sac.setInventarioNuevo(getSelected().getInventario());

            if (getSelected().getNroFormulario() != null) {
                if (!getSelected().getNroFormulario().equals(salidaArticuloOri.getNroFormulario())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getNroFormulario() != null) {
                cambio = "MODIFICADO";
            }

            sac.setNroFormularioNuevo(getSelected().getNroFormulario());

            if (getSelected().getPersona() != null) {
                if (!getSelected().getPersona().equals(salidaArticuloOri.getPersona())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getPersona() != null) {
                cambio = "MODIFICADO";
            }

            sac.setPersonaNueva(getSelected().getPersona());

            if (getSelected().getPersonas() != null) {
                if (!getSelected().getPersonas().equals(salidaArticuloOri.getPersonas())) {
                    cambio = "MODIFICADO";
                }
            } else if (salidaArticuloOri.getPersonas() != null) {
                cambio = "MODIFICADO";
            }

            sac.setPersonasNuevas(getSelected().getPersonas());

            sac.setCambio(cambio);

            javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(secuencia),0) + 1 as VALOR from salidas_articulo_cambios WHERE salida_articulo = " + getSelected().getId() + ";", NroSecuencia.class
            );

            NroSecuencia secuencia = (NroSecuencia) query.getSingleResult();

            sac.setSecuencia(secuencia.getSecuencia());

            salidaArticuloCambioController.setSelected(sac);
            salidaArticuloCambioController.saveNew(event);

            // Fin Guardar registro de cambios
            int cantidadNueva;
            boolean itero;
            boolean encontro;

            DetallesSalidaArticulo detGuardar = null;

            DetallesSalidaArticuloCambios dsac = null;

            for (DetallesSalidaArticulo det : detallesEdit) {

                itero = false;
                encontro = false;
                cantidadNueva = 0;
                for (DetallesSalidaArticulo detOri : detallesEditOri) {
                    itero = true;
                    if (det.getArticulo().getCodigo().equals(detOri.getArticulo().getCodigo())) {
                        cantidadNueva = det.getCantidad() - detOri.getCantidad();
                        detGuardar = detOri;
                        encontro = true;
                        break;
                    } else {
                        cantidadNueva = det.getCantidad();
                    }
                }

                if (!itero) {
                    cantidadNueva = det.getCantidad();

                }

                if (cantidadNueva != 0) {
                    Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class
                    ).setParameter("codigo", det.getArticulo().getCodigo()).getSingleResult();

                    art.setStock(art.getStock() - cantidadNueva);

                    articuloController.setSelected(art);

                    articuloController.save(event);

                    if (encontro) {
                        det.setFechaHoraUltimoEstado(fecha);
                        det.setUsuarioUltimoEstado(usu);
                        if (detGuardar != null) {
                            det.setUsuarioAlta(detGuardar.getUsuarioAlta());
                            det.setFechaHoraAlta(detGuardar.getFechaHoraAlta());
                            det.setEmpresa(detGuardar.getEmpresa());
                        }

                        detallesSalidaArticuloController.setSelected(det);
                        detallesSalidaArticuloController.save(event);

                        // Guardar registro del cambio
                        dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                        dsac.setArticulo(detGuardar.getArticulo());
                        dsac.setCantidad(detGuardar.getCantidad());
                        dsac.setDepartamento(detGuardar.getDepartamento());
                        dsac.setEmpresa(detGuardar.getEmpresa());
                        dsac.setFechaHoraAlta(detGuardar.getFechaHoraAlta());
                        dsac.setFechaHoraUltimoEstado(detGuardar.getFechaHoraUltimoEstado());
                        dsac.setFechaHoraCambio(fecha);
                        dsac.setUsuarioCambio(usu);
                        dsac.setUsuarioAlta(detGuardar.getUsuarioAlta());
                        dsac.setUsuarioUltimoEstado(detGuardar.getUsuarioUltimoEstado());
                        dsac.setInventario(detGuardar.getInventario());
                        dsac.setItem(detGuardar.getItem());
                        dsac.setNroFormulario(detGuardar.getNroFormulario());
                        dsac.setPersona(detGuardar.getPersona());
                        dsac.setSalidaArticulo(detGuardar.getSalidaArticulo());
                        dsac.setSalidaArticuloCambio(sac);
                        dsac.setDetalleSalidaArticulo(detGuardar);
                        dsac.setSalidaArticulo(getSelected());

                        cambio = "IGUAL";
                        if (detGuardar.getArticulo() != null) {
                            if (!detGuardar.getArticulo().equals(det.getArticulo())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getArticulo() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setArticuloNuevo(det.getArticulo());

                        if (detGuardar.getCantidad() != det.getCantidad()) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setCantidadNueva(det.getCantidad());

                        if (detGuardar.getDepartamento() != null) {
                            if (!detGuardar.getDepartamento().equals(det.getDepartamento())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getDepartamento() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setDepartamentoNuevo(det.getDepartamento());

                        if (detGuardar.getInventario() != null) {
                            if (!detGuardar.getInventario().equals(det.getInventario())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getInventario() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setInventarioNuevo(det.getInventario());

                        if (detGuardar.getItem() != det.getItem()) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setItemNuevo(det.getItem());

                        if (detGuardar.getNroFormulario() != null) {
                            if (!detGuardar.getNroFormulario().equals(det.getNroFormulario())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getNroFormulario() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setNroFormularioNuevo(det.getNroFormulario());

                        if (detGuardar.getPersona() != null) {
                            if (!detGuardar.getPersona().equals(det.getPersona())) {
                                cambio = "MODIFICADO";
                            }
                        } else if (det.getPersona() != null) {
                            cambio = "MODIFICADO";
                        }
                        dsac.setPersonaNueva(det.getPersona());

                        dsac.setCambio(cambio);

                        query
                                = ejbFacade.getEntityManager().createNativeQuery(
                                        "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detGuardar.getId() + ";", NroSecuencia.class
                                );

                        secuencia = (NroSecuencia) query.getSingleResult();

                        dsac.setSecuencia(secuencia.getSecuencia());
                        detalleSalidaArticuloCambioController.setSelected(dsac);
                        detalleSalidaArticuloCambioController.saveNew(event);

                        // Fin Guardar registro del cambio
                    } else {
                        det.setId(null);
                        det.setFechaHoraUltimoEstado(fecha);
                        det.setUsuarioUltimoEstado(usu);
                        det.setFechaHoraAlta(fecha);
                        det.setUsuarioAlta(usu);
                        det.setEmpresa(usu.getEmpresa());

                        detallesSalidaArticuloController.setSelected(det);
                        detallesSalidaArticuloController.saveNew(event);

                        // Guardar registro del cambio
                        dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                        dsac.setArticulo(det.getArticulo());
                        dsac.setCantidad(det.getCantidad());
                        dsac.setDepartamento(det.getDepartamento());
                        dsac.setEmpresa(det.getEmpresa());
                        dsac.setFechaHoraAlta(det.getFechaHoraAlta());
                        dsac.setFechaHoraUltimoEstado(det.getFechaHoraUltimoEstado());
                        dsac.setFechaHoraCambio(fecha);
                        dsac.setUsuarioCambio(usu);
                        dsac.setUsuarioAlta(det.getUsuarioAlta());
                        dsac.setUsuarioUltimoEstado(det.getUsuarioUltimoEstado());
                        dsac.setInventario(det.getInventario());
                        dsac.setItem(det.getItem());
                        dsac.setNroFormulario(det.getNroFormulario());
                        dsac.setPersona(det.getPersona());
                        dsac.setSalidaArticulo(det.getSalidaArticulo());
                        dsac.setSalidaArticuloCambio(sac);
                        dsac.setDetalleSalidaArticulo(null);
                        dsac.setSalidaArticulo(getSelected());

                        dsac.setCambio("NUEVO");

                        query
                                = ejbFacade.getEntityManager().createNativeQuery(
                                        "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detGuardar.getId() + ";", NroSecuencia.class
                                );

                        secuencia = (NroSecuencia) query.getSingleResult();

                        dsac.setSecuencia(secuencia.getSecuencia());
                        detalleSalidaArticuloCambioController.setSelected(dsac);
                        detalleSalidaArticuloCambioController.saveNew(event);

                        // Fin Guardar registro del cambio
                    }

                } else if (cantidad == 0 && encontro) {
                    // Guardar registro del cambio
                    dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                    dsac.setArticulo(detGuardar.getArticulo());
                    dsac.setArticuloNuevo(detGuardar.getArticulo());
                    dsac.setCantidad(detGuardar.getCantidad());
                    dsac.setCantidadNueva(detGuardar.getCantidad());
                    dsac.setDepartamento(detGuardar.getDepartamento());
                    dsac.setDepartamentoNuevo(detGuardar.getDepartamento());
                    dsac.setEmpresa(detGuardar.getEmpresa());
                    dsac.setFechaHoraAlta(detGuardar.getFechaHoraAlta());
                    dsac.setFechaHoraUltimoEstado(detGuardar.getFechaHoraUltimoEstado());
                    dsac.setFechaHoraCambio(fecha);
                    dsac.setUsuarioCambio(usu);
                    dsac.setUsuarioAlta(detGuardar.getUsuarioAlta());
                    dsac.setUsuarioUltimoEstado(detGuardar.getUsuarioUltimoEstado());
                    dsac.setInventario(detGuardar.getInventario());
                    dsac.setInventarioNuevo(detGuardar.getInventario());
                    dsac.setItem(detGuardar.getItem());
                    dsac.setItemNuevo(detGuardar.getItem());
                    dsac.setNroFormulario(detGuardar.getNroFormulario());
                    dsac.setNroFormularioNuevo(detGuardar.getNroFormulario());
                    dsac.setPersona(detGuardar.getPersona());
                    dsac.setPersonaNueva(detGuardar.getPersona());
                    dsac.setSalidaArticulo(detGuardar.getSalidaArticulo());
                    dsac.setSalidaArticuloCambio(sac);
                    dsac.setDetalleSalidaArticulo(detGuardar);
                    dsac.setSalidaArticulo(getSelected());

                    dsac.setCambio("IGUAL");

                    query
                            = ejbFacade.getEntityManager().createNativeQuery(
                                    "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detGuardar.getId() + ";", NroSecuencia.class
                            );

                    secuencia = (NroSecuencia) query.getSingleResult();

                    dsac.setSecuencia(secuencia.getSecuencia());
                    detalleSalidaArticuloCambioController.setSelected(dsac);
                    detalleSalidaArticuloCambioController.saveNew(event);
                    // Guardar registro del cambio
                }

            }

            // Ahora borrar los que ya no estan
            for (DetallesSalidaArticulo detOri : detallesEditOri) {
                encontro = false;
                itero = false;
                for (DetallesSalidaArticulo det : detallesEdit) {
                    itero = true;
                    if (det.getArticulo().getCodigo().equals(detOri.getArticulo().getCodigo())) {
                        cantidadNueva = det.getCantidad() - detOri.getCantidad();
                        detGuardar = detOri;
                        encontro = true;
                        break;
                    } else {
                        cantidadNueva = det.getCantidad();

                    }
                }

                if (!encontro) {
                    Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class
                    ).setParameter("codigo", detOri.getArticulo().getCodigo()).getSingleResult();

                    art.setStock(art.getStock() + detOri.getCantidad());

                    articuloController.setSelected(art);

                    articuloController.save(event);

                    // Guardar registro del cambio
                    dsac = detalleSalidaArticuloCambioController.prepareCreate(event);
                    dsac.setArticulo(detOri.getArticulo());
                    dsac.setCantidad(detOri.getCantidad());
                    dsac.setDepartamento(detOri.getDepartamento());
                    dsac.setEmpresa(detOri.getEmpresa());
                    dsac.setFechaHoraAlta(detOri.getFechaHoraAlta());
                    dsac.setFechaHoraUltimoEstado(detOri.getFechaHoraUltimoEstado());
                    dsac.setFechaHoraCambio(fecha);
                    dsac.setUsuarioCambio(usu);
                    dsac.setUsuarioAlta(detOri.getUsuarioAlta());
                    dsac.setUsuarioUltimoEstado(detOri.getUsuarioUltimoEstado());
                    dsac.setInventario(detOri.getInventario());
                    dsac.setItem(detOri.getItem());
                    dsac.setNroFormulario(detOri.getNroFormulario());
                    dsac.setPersona(detOri.getPersona());
                    dsac.setSalidaArticulo(detOri.getSalidaArticulo());
                    dsac.setSalidaArticuloCambio(sac);
                    dsac.setDetalleSalidaArticulo(detOri);
                    dsac.setSalidaArticulo(getSelected());

                    dsac.setCambio("BORRADO");

                    query
                            = ejbFacade.getEntityManager().createNativeQuery(
                                    "select ifnull(max(secuencia),0) + 1 as VALOR from detalles_salida_articulo_cambios WHERE detalle_salida_articulo = " + detOri.getId() + ";", NroSecuencia.class
                            );

                    secuencia = (NroSecuencia) query.getSingleResult();

                    dsac.setSecuencia(secuencia.getSecuencia());
                    detalleSalidaArticuloCambioController.setSelected(dsac);
                    detalleSalidaArticuloCambioController.saveNew(event);
                    // Fin Guardar registro del cambio

                    detallesSalidaArticuloController.setSelected(detOri);
                    detallesSalidaArticuloController.delete(event);
                }
            }

            /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findAll", SalidasArticulo.class).getResultList());

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", art).getResultList();

                detalleSelected = null;
            } else {
                setSelected(null);
            }
             */
            if (fechaDesde == null) {
                fechaDesde = ejbFacade.getSystemDateOnly(-30);
            }
            if (fechaHasta == null) {
                fechaHasta = ejbFacade.getSystemDateOnly();
            }

            buscarPorFechaAlta();

            articuloStockCriticoController.verifArticulosStockCritico();
        }
    }

    public void saveNewOnly(ActionEvent event) {
        super.saveNew(event);
    }

    private boolean verificarNeto(DetallesSalidaArticulo detalleArt) {

        List<DetallesSalidaArticulo> lista = new ArrayList<>();

        lista.add(detalleArt);

        return verificarNeto(ejbFacade.getSystemDate(), lista);

    }

    private boolean verificarNeto(Date fecha, Collection<DetallesSalidaArticulo> detallesArt) {

        List<ReportesInventario> listaInv = inventarioController.obtenerDatosInventario(fecha);

        BigInteger stock = BigInteger.ZERO;
        BigInteger bigInteger;

        boolean resp = true;

        mensajeNeto = "Los sgtes articulos no estan disponibles: ";

        for (DetallesSalidaArticulo det : detallesArt) {

            // Encontrar el neto que esta en el inventario del articulo en cuestion
            for (ReportesInventario inv : listaInv) {
                stock = BigInteger.ZERO;
                if (det.getArticulo().getCodigo().equals(inv.getArticulo())) {
                    stock = inv.getStock();
                    break;
                }
            }

            // Comparamos el neto con la cantidad del articulo que queremos ingresar salida
            bigInteger = BigInteger.valueOf(det.getCantidad());
            if (stock.compareTo(bigInteger) < 0) {
                mensajeNeto += "Articulo " + det.getArticulo().getCodigo() + " - " + det.getArticulo().getDescripcion() + ": Stock (" + stock + "), Pedido (" + det.getCantidad() + ")\n";
                resp = false;
            }
        }

        return resp;
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            if (!verificarNroFormularioCreate()) {
                return;
            }

            if (detalles != null) {
                if (detalles.isEmpty()) {
                    JsfUtil.addErrorMessage("Debe habar al menos un detalle");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Debe haber al menos un detalle.");
                return;
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            if (!verificarNeto(fecha, detalles)) {
                JsfUtil.addErrorMessage(mensajeNeto);
                return;
            }

            salidaArticulo.setFechaHoraUltimoEstado(fecha);
            salidaArticulo.setNroFormulario(obtenerProximoNroFormulario());
            salidaArticulo.setUsuarioUltimoEstado(usu);
            salidaArticulo.setFechaHoraAlta(fecha);
            salidaArticulo.setUsuarioAlta(usu);
            salidaArticulo.setEmpresa(usu.getEmpresa());
            salidaArticulo.setTotal_salida(totalSalida);
            // salidaArticulo.setSede(sede);
            //salidaArticulo.setDepartamento(salidaArticulo.getPersona().getDepartamento());

            setSelected(salidaArticulo);

            super.saveNew(event);

            for (DetallesSalidaArticulo det : detalles) {
                det.setSalidaArticulo(getSelected());
                det.setFechaHoraUltimoEstado(fecha);
                det.setUsuarioUltimoEstado(usu);
                det.setFechaHoraAlta(fecha);
                det.setUsuarioAlta(usu);
                det.setEmpresa(usu.getEmpresa());
                det.setNroFormulario(getSelected().getNroFormulario());
                det.setPersona(getSelected().getPersona());
                det.setDepartamento(getSelected().getDepartamento());
                //det.setSede(sede);
                det.setId(null);

                Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class
                ).setParameter("codigo", det.getArticulo().getCodigo()).getSingleResult();

                art.setStock(art.getStock() - det.getCantidad());

                articuloController.setSelected(art);

                articuloController.save(event);

                detallesSalidaArticuloController.setSelected(det);

                detallesSalidaArticuloController.saveNew(event);

            }
            /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findAll", SalidasArticulo.class).getResultList());

            if (getItems2().size() > 0) {
                SalidasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findBySalidaArticulo", DetallesSalidaArticulo.class).setParameter("salidaArticulo", art).getResultList();

                detalleSelected = null;
            } else {
                setSelected(null);
            }
             */

            if (fechaDesde == null) {
                fechaDesde = ejbFacade.getSystemDateOnly(-30);
            }
            if (fechaHasta == null) {
                fechaHasta = ejbFacade.getSystemDateOnly();
            }

            buscarPorFechaAlta();

            articuloStockCriticoController.verifArticulosStockCritico();
        }

    }

    public void pdf(boolean generarPdf) {

        if (listaArticuloFiltroSalida != null) {
            if (!listaArticuloFiltroSalida.isEmpty()) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaSalida);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByListaArticuloFechaSalida", DetallesSalidaArticulo.class
                    ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("articulo", listaArticuloFiltroSalida).getResultList();

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepSalidaArticulo> listafinal2 = new ArrayList<>();
                    RepSalidaArticulo item = null;
                    for (DetallesSalidaArticulo det : lista) {

                        item = new RepSalidaArticulo();

                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setCodigo(det.getArticulo().getCodigo());

                        String fechaString = "";
                        System.out.println("-----------------------");
                        if (det.getDetalleEntradaArticulo() != null) {
                            System.out.println("detalle articulo no es null");
                            if (det.getDetalleEntradaArticulo().getFechaVencimiento() != null) {
                                System.out.println("fecha vencimiento no es null");

                                fechaString = format2.format(det.getDetalleEntradaArticulo().getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);

                                //            } else if (det.getDetallesInventario() != null && det.getDetallesInventario().getFechaVencimiento() != null) {
                                //             fechaString = format2.format(det.getDetallesInventario().getFechaVencimiento());
                            }
                        } else {
                            System.out.println("detalle articulo es null");
                            if (det.getFechaVencimiento() != null) {
                                fechaString = format2.format(det.getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);
                            }
                        }

                        item.setVencimiento(fechaString);
                        item.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        item.setNroFormulario(det.getSalidaArticulo().getNroFormulario());
                        item.setMarca(det.getArticulo() == null ? "" : (det.getArticulo().getMarca() == null ? "" : det.getArticulo().getMarca().getDescripcion()));
                        item.setCantidad(det.getCantidad());
                        item.setCostoUnitario(det.getCostoUnitario());
                        item.setCostoTotal(det.getCostoTotal());
                        item.setDepartamento(det.getDepartamento() == null ? "" : det.getDepartamento().getNombre());
                        item.setUsuarioPedido(det.getSalidaArticulo().getPersonas());
                        item.setUsuarioSalida(det.getSalidaArticulo().getUsuarioAlta().getNombresApellidos());

                        listafinal2.add(item);

                        //det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listafinal2);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeSalida));
                    map.put("fechaHasta", format2.format(fechaHastaSalida));

                    String descArticulo = "";
                    for (Articulos art : listaArticuloFiltroSalida) {
                        if (!"".equals(descArticulo)) {
                            descArticulo += ", ";
                        }

                        descArticulo += art.getCodigo() + "-" + art.getDescripcion();
                        //descObjetosGasto += obj.getCodigo() + "-" + obj.getDescripcion();

                    }
                    map.put("descArticulo", descArticulo);

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;
                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticulo.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe selecionar al menos un Artículo");
            }
        } else {
            JsfUtil.addErrorMessage("Debe selecionar al menos un Artículo");
        }
        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    /**
     *
     * @param generarPdf
     */
    public void pdfDepartamento(boolean generarPdf) {

        if (listadepartamentoFiltroSalida != null) {
            if (!listadepartamentoFiltroSalida.isEmpty()) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;
                    JRBeanCollectionDataSource beanCollectionDataSource1 = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaSalida);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByListaDptoFechaSalida", DetallesSalidaArticulo.class
                    ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("departamento", listadepartamentoFiltroSalida).getResultList();
                    List<RepSalidaArticuloDpto> listaDepartamento = new ArrayList<>();
                    RepSalidaArticuloDpto item = null;

                    List<RepSalidaArticuloDptoSuma> listaDepartamentoSuma = new ArrayList<>();

                    for (DetallesSalidaArticulo det : lista) {

                        item = new RepSalidaArticuloDpto();

                        item.setDepartamento(det.getDepartamento() == null ? "" : det.getDepartamento().getNombre());
                        item.setCodigo(det.getArticulo().getCodigo());
                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        item.setCantidad(det.getCantidad());
                        item.setMarca(det.getMarca() == null ? "" : det.getMarca().getDescripcion());
                        item.setNroFormulario(det.getNroFormulario());
                        item.setUsuarioSalida(det.getSalidaArticulo().getUsuarioAlta().getNombresApellidos());

                        listaDepartamento.add(item);
                        //   det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        boolean encontro = false;
                        for (RepSalidaArticuloDptoSuma suma : listaDepartamentoSuma) {
                            if (suma.getCodigo().equals(det.getArticulo().getCodigo())) {

                                RepSalidaArticuloDptoSuma nuevo = new RepSalidaArticuloDptoSuma(suma.getCodigo(), suma.getArticulo(), suma.getCantidad() + det.getCantidad(), suma.getDepartamento());

                                listaDepartamentoSuma.remove(suma);
                                listaDepartamentoSuma.add(nuevo);
                                encontro = true;
                                break;
                            }
                        }
                        if (!encontro) {
                            RepSalidaArticuloDptoSuma nuevo = new RepSalidaArticuloDptoSuma(det.getArticulo().getCodigo(),
                                    det.getArticulo().getDescripcion(), det.getCantidad(),
                                    det.getDepartamento().getNombre());
                            listaDepartamentoSuma.add(nuevo);
                        }
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaDepartamento);

                    beanCollectionDataSource1 = new JRBeanCollectionDataSource(listaDepartamentoSuma);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeSalida));
                    map.put("fechaHasta", format2.format(fechaHastaSalida));
                    map.put("datasource1", beanCollectionDataSource1);
                    String descDpto = "";
                    for (Departamentos dep : listadepartamentoFiltroSalida) {
                        if (!"".equals(descDpto)) {
                            descDpto += ", ";
                        }

                        descDpto += dep.getNombre();

                    }
                    map.put("descDpto", descDpto);

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;

                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloDpto.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloDptoExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe escoger un departamento.");
            }
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void graficoDepartamento() {
        pieModel2 = new PieChartModel();
        if (departamentoFiltro != null) {
            HttpServletResponse httpServletReponse = null;
            JRBeanCollectionDataSource beanCollectionDataSource = null;

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHastaSalida);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();

            List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByDptoFechaSalida", DetallesSalidaArticulo.class
            ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("departamento", departamentoFiltro).getResultList();

            if (lista.isEmpty()) {
                JsfUtil.addErrorMessage("No hay datos");
                return;
            }
            int cant = 0;
            for (DetallesSalidaArticulo art : lista) {
                pieModel2.set(art.getArticulo().getDescripcion(), art.getCantidad());
                cant++;
            }
            pieModel2.setTitle("Articulos");
            pieModel2.setLegendPosition("s");
            pieModel2.setFill(true);
            pieModel2.setShowDataLabels(true);
            pieModel2.setDiameter(250);

            estiloPieDepartamento = "visibility: hidden;width:300px;height: " + (300 + (cant * 20)) + "px";

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("exportChart();");
        } else {
            JsfUtil.addSuccessMessage("Debe seleccionar un departamento");

        }
    }

    public void reporteSalidaObjetoGastoExcel() {

        if (listaObjetosGastoFiltroSalida != null) {
            if (!listaObjetosGastoFiltroSalida.isEmpty()) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;
                    JRBeanCollectionDataSource beanCollectionDataSource1 = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaSalida);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByListaObjetosGastoFechaSalida", DetallesSalidaArticulo.class
                    ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("objetosGasto", listaObjetosGastoFiltroSalida).getResultList();

                    List<RepSalidaObjetoGasto> listaoG = new ArrayList<>();
                    RepSalidaObjetoGasto item = null;
                    List<RepSalidaObjetoGastoSuma> listaoGSuma = new ArrayList<>();

                    for (DetallesSalidaArticulo det : lista) {
                        item = new RepSalidaObjetoGasto();

                        item.setObjetosGasto(det.getArticulo().getObjetoGasto().getCodigo());
                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setDepartamento(det.getDepartamento() == null ? "" : det.getDepartamento().getNombre());
                        item.setCantidad(det.getCantidad());
                        item.setCostoUnitario(det.getCostoUnitario());
                        item.setCostoTotal(det.getCostoTotal());
                        item.setCodArticulo(det.getArticulo() == null ? "" : det.getArticulo().getCodigo());
                        item.setNroFormulario(det.getNroFormulario());
                        item.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        item.setPersonaDestino(det.getSalidaArticulo().getPersonas());
                        item.setMarca(det.getMarca() == null ? "" : det.getMarca().getDescripcion());
                        item.setModelo(det.getModelo() == null ? "" : det.getModelo().getDescripcion());

                        listaoG.add(item);

                        boolean encontro = false;
                        for (RepSalidaObjetoGastoSuma suma : listaoGSuma) {
                            if (det.getArticulo().getObjetoGasto() != null && suma.getObjetosGasto().equals(det.getArticulo().getObjetoGasto().getCodigo())) {

                                RepSalidaObjetoGastoSuma nuevo = new RepSalidaObjetoGastoSuma(suma.getCodArticulo(), suma.getArticulo(), suma.getCantidad() + det.getCantidad(), suma.getObjetosGasto(), suma.getoGDescripcion());

                                listaoGSuma.remove(suma);
                                listaoGSuma.add(nuevo);
                                encontro = true;
                                break;
                            }
                        }

                        if (det.getArticulo().getObjetoGasto() != null && !encontro) {
                            RepSalidaObjetoGastoSuma nuevo = new RepSalidaObjetoGastoSuma(
                                    det.getArticulo().getCodigo(),
                                    det.getArticulo().getDescripcion(),
                                    det.getCantidad(),
                                    det.getArticulo().getObjetoGasto().getCodigo(),
                                    det.getArticulo().getObjetoGasto().getCodigo() + " " + det.getArticulo().getObjetoGasto().getDescripcion());
                            listaoGSuma.add(nuevo);
                        }

                        //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaoG);

                    beanCollectionDataSource1 = new JRBeanCollectionDataSource(listaoGSuma);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeSalida));
                    map.put("fechaHasta", format2.format(fechaHastaSalida));
                    map.put("datasource1", beanCollectionDataSource1);

                    String descObjetosGasto = "";
                    for (ObjetosGasto obj : listaObjetosGastoFiltroSalida) {
                        if (!"".equals(descObjetosGasto)) {
                            descObjetosGasto += ", ";
                        }

                        descObjetosGasto += obj.getCodigo() + "-" + obj.getDescripcion();

                    }

                    map.put("descobjetosGasto", descObjetosGasto);

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;

                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaObjetoGastoExcel.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                    servletOutputStream = httpServletResponse.getOutputStream();

                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    exporter.exportReport();

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe selecionar al menos un código de objeto del gasto.");
            }
        } else {
            JsfUtil.addErrorMessage("Debe selecionar al menos un código de objeto del gasto");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void graficoObjetoGasto() {
        pieModelO = new PieChartModel();
        if (objetosGastoFiltro != null) {
            HttpServletResponse httpServletReponse = null;
            JRBeanCollectionDataSource beanCollectionDataSource = null;

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHastaSalida);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();

            List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByObjetosGastoFechaSalida", DetallesSalidaArticulo.class
            ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("objetosGasto", objetosGastoFiltro).getResultList();
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            if (lista.isEmpty()) {
                JsfUtil.addErrorMessage("No hay datos");
                return;
            }
            int cant2 = 0;
            for (DetallesSalidaArticulo art : lista) {
                pieModelO.set(art.getArticulo().getDescripcion(), art.getCantidad());
                cant2++;
            }
            pieModelO.setTitle("Articulos");
            pieModelO.setLegendPosition("s");
            pieModelO.setFill(true);
            pieModelO.setShowDataLabels(true);
            pieModelO.setDiameter(250);
            estiloPieObjetoGasto = "visibility: hidden;width:300px;height: " + (300 + (cant2 * 20)) + "px";
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("exportChartObjeto();");
        } else {
            JsfUtil.addSuccessMessage("Debe seleccionar un Objeto del Gasto");

        }
    }

    public void pdfDeposito(boolean generarPdf) {

        if (listadepositoFiltroSalida != null) {
            if (!listadepositoFiltroSalida.isEmpty()) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;
                    JRBeanCollectionDataSource beanCollectionDataSource1 = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaSalida);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByListaDepositoFechaSalida", DetallesSalidaArticulo.class
                    ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("sede", listadepositoFiltroSalida).getResultList();
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepSalidaArticuloPorDeposito> listaDeposito = new ArrayList<>();
                    RepSalidaArticuloPorDeposito item = null;

                    List<RepSalidaArticuloDepositoSuma> listaDepositoSuma = new ArrayList<>();

                    for (DetallesSalidaArticulo det : lista) {

                        item = new RepSalidaArticuloPorDeposito();
                        String fechaString = "";

                        System.out.println("-----------------------");
                        if (det.getDetalleEntradaArticulo() != null) {
                            System.out.println("detalle articulo no es null");
                            if (det.getDetalleEntradaArticulo().getFechaVencimiento() != null) {
                                System.out.println("fecha vencimiento no es null");

                                fechaString = format2.format(det.getDetalleEntradaArticulo().getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);

                                //            } else if (det.getDetallesInventario() != null && det.getDetallesInventario().getFechaVencimiento() != null) {
                                //             fechaString = format2.format(det.getDetallesInventario().getFechaVencimiento());
                            }
                        } else {
                            System.out.println("detalle articulo es null");
                            if (det.getFechaVencimiento() != null) {
                                fechaString = format2.format(det.getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);
                            }
                        }

                        item.setVencimiento(fechaString);
                        item.setDepartamento(det.getDepartamento() == null ? "" : det.getDepartamento().getNombre());
                        item.setCodigo(det.getArticulo().getCodigo());
                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setMarca(det.getArticulo().getMarca().getDescripcion());
                        item.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        item.setCantidad(det.getCantidad());
                        item.setUsuarioPedido(det.getSalidaArticulo().getPersonas());
                        item.setUsuarioRetiro(det.getSalidaArticulo().getResponsableRetiro() == null ? "" : det.getSalidaArticulo().getResponsableRetiro().getNombresApellidos());
                        item.setUsuarioAutoriza(det.getSalidaArticulo().getResponsableAutoriza() == null ? "" : det.getSalidaArticulo().getResponsableAutoriza().getNombresApellidos());
                        item.setCostoUnitario(det.getCostoUnitario());
  
                        item.setCostoTotal(det.getCostoUnitario().multiply(new BigDecimal(det.getCantidad())));

                        //item.setDeposito(det.getSede().getDescripcion());
                        item.setNroFormulario(det.getNroFormulario());
                        item.setUsuarioSalida(det.getSalidaArticulo().getUsuarioAlta().getNombresApellidos());

                        listaDeposito.add(item);
                        //   det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        boolean encontro = false;
                        for (RepSalidaArticuloDepositoSuma suma : listaDepositoSuma) {
                            if (suma.getCodigo().equals(det.getArticulo().getCodigo())) {

                                RepSalidaArticuloDepositoSuma nuevo = new RepSalidaArticuloDepositoSuma(suma.getCodigo(), suma.getArticulo(), suma.getCantidad() + det.getCantidad(), suma.getDeposito());

                                listaDepositoSuma.remove(suma);
                                listaDepositoSuma.add(nuevo);
                                encontro = true;
                                break;
                            }
                        }
                        if (!encontro) {
                            RepSalidaArticuloDepositoSuma nuevo = new RepSalidaArticuloDepositoSuma(det.getArticulo().getCodigo(),
                                    det.getArticulo().getDescripcion(), det.getCantidad(),
                                    det.getSede().getDescripcion());
                            listaDepositoSuma.add(nuevo);
                        }
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaDeposito);

                    beanCollectionDataSource1 = new JRBeanCollectionDataSource(listaDepositoSuma);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    // SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeSalida));
                    map.put("fechaHasta", format2.format(fechaHastaSalida));
                    map.put("datasource1", beanCollectionDataSource1);
                    String descDeposito = "";
                    for (Sede depo : listadepositoFiltroSalida) {
                        if (!"".equals(descDeposito)) {
                            descDeposito += ", ";
                        }

                        descDeposito += depo.getDescripcion();

                    }
                    map.put("descDeposito", descDeposito);

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;

                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloPorDeposito.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe escoger un deposito.");
            }
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void pdfNroFormuario(boolean generarPdf) {

        if (nroFormulario != null) {
            if (!nroFormulario.equals(0)) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaSalida);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByBuscarNroFormulario", DetallesSalidaArticulo.class
                    ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("nroFormulario", nroFormulario).getResultList();

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepSalidaArticulo> listafinal2 = new ArrayList<>();
                    RepSalidaArticulo item = null;
                    for (DetallesSalidaArticulo det : lista) {

                        item = new RepSalidaArticulo();
                        item.setNroFormulario(nroFormulario);

                        String fechaString = "";
                        System.out.println("-----------------------");
                        if (det.getDetalleEntradaArticulo() != null) {
                            System.out.println("detalle articulo no es null");
                            if (det.getDetalleEntradaArticulo().getFechaVencimiento() != null) {
                                System.out.println("fecha vencimiento no es null");

                                fechaString = format2.format(det.getDetalleEntradaArticulo().getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);

                                //            } else if (det.getDetallesInventario() != null && det.getDetallesInventario().getFechaVencimiento() != null) {
                                //             fechaString = format2.format(det.getDetallesInventario().getFechaVencimiento());
                            }
                        } else {
                            System.out.println("detalle articulo es null");
                            if (det.getFechaVencimiento() != null) {
                                fechaString = format2.format(det.getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);
                            }
                        }

                        item.setVencimiento(fechaString);
                        item.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        item.setNroFormulario(det.getSalidaArticulo().getNroFormulario());
                        item.setMarca(det.getArticulo() == null ? "" : (det.getArticulo().getMarca() == null ? "" : det.getArticulo().getMarca().getDescripcion()));
                        item.setCantidad(det.getCantidad());
                        item.setCostoUnitario(det.getCostoUnitario());
                        item.setCostoTotal(det.getCostoTotal());
                        item.setArticulo(det.getArticulo() == null ? "" : det.getArticulo().getDescripcion());
                        item.setDeposito(det.getSede() == null ? "" : det.getSede().getDescripcion());
                        item.setDepartamento(det.getDepartamento() == null ? "" : det.getDepartamento().getNombre());
                        item.setUsuarioSalida(det.getSalidaArticulo().getUsuarioAlta().getNombresApellidos());
                        item.setUsuarioPedido(det.getSalidaArticulo().getPersonas());

                        listafinal2.add(item);

                        //det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listafinal2);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeSalida));
                    map.put("fechaHasta", format2.format(fechaHastaSalida));
                    if (nroFormulario != null) {
                        map.put("nroFormulario", nroFormulario);
                    }

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;
                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloNroFormulario.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe selecionar al menos un NroFormulario");
            }
        } else {
            JsfUtil.addErrorMessage("Debe selecionar al menos un NroFormulario");
        }
        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void pdf2(boolean generarPdf) {

        if (nroFormulario != null) {
            if (!nroFormulario.equals(0)) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaSalida);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByNroFormulario", DetallesSalidaArticulo.class
                    ).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("nroFormulario", nroFormulario).getResultList();

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepSalidaArticulo> listafinal2 = new ArrayList<>();
                    RepSalidaArticulo item = null;
                    for (DetallesSalidaArticulo det : lista) {

                        item = new RepSalidaArticulo();

                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setCodigo(det.getArticulo().getCodigo());

                        String fechaString = "";
                        System.out.println("-----------------------");
                        if (det.getDetalleEntradaArticulo() != null) {
                            System.out.println("detalle articulo no es null");
                            if (det.getDetalleEntradaArticulo().getFechaVencimiento() != null) {
                                System.out.println("fecha vencimiento no es null");

                                fechaString = format2.format(det.getDetalleEntradaArticulo().getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);

                                //            } else if (det.getDetallesInventario() != null && det.getDetallesInventario().getFechaVencimiento() != null) {
                                //             fechaString = format2.format(det.getDetallesInventario().getFechaVencimiento());
                            }
                        } else {
                            System.out.println("detalle articulo es null");
                            if (det.getFechaVencimiento() != null) {
                                fechaString = format2.format(det.getFechaVencimiento());
                                System.out.println("vencimiento:" + fechaString);
                            }
                        }

                        item.setVencimiento(fechaString);
                        item.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        item.setNroFormulario(det.getSalidaArticulo().getNroFormulario());
                        item.setMarca(det.getArticulo() == null ? "" : (det.getArticulo().getMarca() == null ? "" : det.getArticulo().getMarca().getDescripcion()));
                        item.setCantidad(det.getCantidad());
                        item.setCostoUnitario(det.getCostoUnitario());
                        item.setCostoTotal(det.getCostoTotal());
                        item.setDepartamento(det.getDepartamento() == null ? "" : det.getDepartamento().getNombre());
                        item.setUsuarioSalida(det.getSalidaArticulo().getUsuarioAlta().getNombresApellidos());

                        listafinal2.add(item);

                        //det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listafinal2);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeSalida));
                    map.put("fechaHasta", format2.format(fechaHastaSalida));
                    map.put("nroFormulario", getSelected().getNroFormulario());

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;
                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticulo.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe selecionar al menos un Artículo");
            }
        } else {
            JsfUtil.addErrorMessage("Debe selecionar al menos un Artículo");
        }
        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void verDoc(ArchivosReporte doc) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            try {
                if (doc.getArchivo() != null) {
                    fileByte = Files.readAllBytes(new File("/D:/ReporteNroFormulario" + "/" + doc.getArchivo()).toPath());
                }
                // fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + doc.getArchivo()).toPath());
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
            }

            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");
        } finally {

        }
    }

    public void actualizarFirmarMultiple(SalidasArticulo item) {
        setSelected(item);
        super.save(null);
        setSelected(null);
    }

    public void firmar() {

        Date fecha = ejbFacade.getSystemDate();
        FirmasArticulosSalida firma = null;
        /*
        try{
            firma = ejbFacade.getEntityManager().createNamedQuery("Firmas.findBySesion", Firmas.class).setParameter("sesion", sessionId).setParameter("estado", "IN").setParameter("fechaHora", fecha).getSingleResult();
        }catch(Exception ex){
            
        }
         */

        if (firma == null) {
            firma = firmasArticulosSalidaController.prepareCreate(null);

            firma.setSalidaArticulo(getSelected());
            firma.setEmpresa(usuario.getEmpresa());
            firma.setPersonaUltimoEstado(personaUsuario);
            firma.setFechaHoraUltimoEstado(fecha);
            firma.setFechaHora(fecha);
            firma.setEstado("PE");
            firma.setSesion(sessionId);
            firma.setPersona(personaUsuario);

            firmasArticulosSalidaController.setSelected(firma);
            firmasArticulosSalidaController.saveNew(null);

        }

        int cont = 30;
        FirmasArticulosSalida firma2 = null;

        while (cont >= 0) {

            try {
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                firma2 = ejbFacade.getEntityManager().createNamedQuery("FirmasArticulosSalida.findById", FirmasArticulosSalida.class).setParameter("id", firma.getId()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                cont = 0;
                break;
            }

            System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);

            if (!firma2.getEstado().equals("PE")) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            cont--;
        }

        if (cont > 0) {

            if (firma2 != null) {
                if (firma2.getEstado().equals("AC")) {
                    if (docImprimir != null) {
                        docImprimir.setArchivosReporte(ejbFacade.getEntityManager().createNamedQuery("ArchivosReporte.findById", ArchivosReporte.class).setParameter("id", docImprimir.getId()).getSingleResult());
                    }

                } else {
                    JsfUtil.addErrorMessage("Error en proceso");
                }
            } else {
                JsfUtil.addErrorMessage("Error en proceso.");
            }
        } else {
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasArticulosSalidaController.setSelected(firma2);
                firmasArticulosSalidaController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }

    }

    public void prepareVerDoc(SalidasArticulo doc) {
        ArchivosReporte item = obtenerPrimerArchivo(doc);

        //prepareVerDoc(item);
    }

    private ArchivosReporte obtenerPrimerArchivo(SalidasArticulo doc) {
        if (doc != null) {
            List<ArchivosReporte> lista = ejbFacade.getEntityManager().createNativeQuery("select a.* from archivos_reporte as a where a.salida_articulo = ?1 and id in (select min(id) from archivos_reporte as d where d.salida_articulo = a.salida_articulo)", ArchivosReporte.class).setParameter(1, doc.getId()).getResultList();
            if (!lista.isEmpty()) {
                return lista.get(0);
            }
        }

        return null;
    }

    public void pdfProveedor(boolean generarPdf) {

        if (listaProveedorFiltroSalida != null) {
            if (!listaProveedorFiltroSalida.isEmpty()) {
                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaSalida);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesSalidaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesSalidaArticulo.findByProveedorFechaSalida", DetallesSalidaArticulo.class).setParameter("fechaSalidaDesde", fechaDesdeSalida).setParameter("fechaSalidaHasta", nuevaFechaHasta).setParameter("proveedor", listaProveedorFiltroSalida).getResultList();
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepSalidaArticuloProveedor> listaProveedor = new ArrayList<>();
                    RepSalidaArticuloProveedor item = null;
                    for (DetallesSalidaArticulo det : lista) {
                        item = new RepSalidaArticuloProveedor();

                        item.setProveedor(det.getEntradaArticulo().getProveedor() == null ? "" : det.getEntradaArticulo().getProveedor().getNombre());
                        item.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                        item.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                        item.setFechaLlamado(det.getEntradaArticulo().getFechaLlamado());
                        item.setNroFactura(det.getEntradaArticulo().getNroFactura() == null ? "" : det.getEntradaArticulo().getNroFactura());

                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setCodigo(det.getArticulo().getCodigo());
                        item.setMarca(det.getMarca() == null ? "" : det.getMarca().getDescripcion());

                        item.setCantidad(det.getCantidad());
                        item.setPrecioUnitario(det.getCostoUnitario());
                        item.setImporteTotal(det.getCostoTotal());

                        listaProveedor.add(item);

                        //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaProveedor);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    // SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeSalida));
                    map.put("fechaHasta", format2.format(fechaHastaSalida));

                    String descProveedor = "";
                    for (Proveedores prov : listaProveedorFiltroSalida) {
                        if (!"".equals(descProveedor)) {
                            descProveedor += ", ";
                        }

                        descProveedor += prov.getNombre();

                    }
                    map.put("descProveedor", descProveedor);

                    String reportPath = null;
                    JasperPrint jasperPrint = null;

                    ServletOutputStream servletOutputStream = null;
                    if (generarPdf) {
                        reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticulosPorProveedor.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidasArticuloPorProveedorExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe escoger un proveedor.");
            }

            ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
        }
    }

}

package py.com.startic.gestion.controllers;

import java.time.Year;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.ChartObjetivoPrincipal;
import py.com.startic.gestion.models.Periodo;
import py.com.startic.gestion.models.TiposObjetivos;

@Named(value = "chartObjetivoPrincipalController")
@ViewScoped
public class ChartObjetivoPrincipalController extends AbstractController<ChartObjetivoPrincipal> {

    private BarChartModel modelDocumentosMesaPorDiaBar;// Modelo para el gráfico 
    private DashboardModel dashboardModel;
    private LineChartModel modelDocumentosMesaPorDiaLinear;
    private PieChartModel modelDocumentosMesaPorDiaPie;
    private Date fechaDesde;
    private Year año;
    private Date fechaHasta;
    private String tipoGrafico;
    private List<TiposObjetivos> listaTiposDoc;
    private List<Acciones> listaAcciones;
    private List<Periodo> listaPeriodo;
    private Periodo periodo;
    private Integer tipo;
    private double resultado;
    private double valorVariable;
    private double variable2;
    private double metasAlcanzada;
    private double porcentaje;
    private String tipoObjetivo;
    static final public String COLORES_CHART = "31a00d,fffb2f,4a148c,827717,0d47a1,f57f17,FFFF00,bf360c,3e2723,880e4f,b71c1c";

    private List<ChartObjetivoPrincipal> lista;

    public ChartObjetivoPrincipalController() {
        // Inform the Abstract parent controller of the concrete Contactos Entity
        super(ChartObjetivoPrincipal.class);
    }

    @PostConstruct
    public void init() {

        tipoGrafico = "Lineas";
        tipoObjetivo = "Misional";
        fechaDesde = ejbFacade.getSystemDateOnly(-15);
        fechaHasta = ejbFacade.getSystemDateOnly();
        listaPeriodo = ejbFacade.getEntityManager().createNamedQuery("Periodo.findByObjetivo", Periodo.class).getResultList();
        listaTiposDoc = ejbFacade.getEntityManager().createNamedQuery("TiposObjetivos.findAll", TiposObjetivos.class).getResultList();
        createBarChart();
        initChartObjetivoPrincipal();
        //calcularPorcentajeMisional();

    }

    public void initChartObjetivoPrincipal() {
        lista = null;
        if (tipoObjetivo.equals("Misinal")) {
            //setItems(ejbFacade.getEntityManager().createNamedQuery("ChartObjetivoPrincipal.findByObjetivoPeriodo", ChartObjetivoPrincipal.class).setParameter("tipoObjetivo", tipoObjetivo).setParameter("periodo", periodo).getResultList());
            calcularPorcentajeMisional();
        } else if (tipoObjetivo.equals("Area de Apoyo")) {
            // setItems(ejbFacade.getEntityManager().createNamedQuery("ChartObjetivoPrincipal.findByObjetivoPeriodo", ChartObjetivoPrincipal.class).setParameter("tipoObjetivo", tipoObjetivo).setParameter("periodo", periodo).getResultList());
            calcularPorcentajeAreaDeApoyo();
        }
    }

    public void calcularPorcentajeMisional() {
        if (variable2 != 0) {
            porcentaje = (Math.round(variable2 / valorVariable) * 100);
        } else {
            porcentaje = 0;
        }

        // Llamar al método para actualizar el gráfico después de calcular el porcentaje
        createBarChart();

    }

    public void calcularPorcentajeAreaDeApoyo() {
        if (metasAlcanzada != 0) {
            porcentaje = (Math.round(metasAlcanzada / 8) * 100);
        } else {
            porcentaje = 0;
        }

        // Llamar al método para actualizar el gráfico después de calcular el porcentaje
        createBarChart();

    }

    // Crear el gráfico de barras con el porcentaje calculado
    private void createBarChart() {

        modelDocumentosMesaPorDiaBar = new BarChartModel();

        // Crear una serie de datos para el gráfico
        ChartSeries porcentajeSerie = new ChartSeries();
        porcentajeSerie.setLabel("Misional");
         porcentajeSerie.setLabel("Area de Apoyo");

        // Agregar los datos calculados al gráfico
        porcentajeSerie.set("2024", porcentaje); // Mostrar solo el porcentaje calculado
        porcentajeSerie.set("2025", porcentaje); // Mostrar solo el porcentaje calculado
        porcentajeSerie.set("2026", porcentaje); // Mostrar solo el porcentaje calculado
        porcentajeSerie.set("2027", porcentaje); // Mostrar solo el porcentaje calculado
        porcentajeSerie.set("2028", porcentaje); // Mostrar solo el porcentaje calculado

        // Agregar la serie al modelo de gráfico
        modelDocumentosMesaPorDiaBar.addSeries(porcentajeSerie);
    }

    public PieChartModel getModelDocumentosMesaPorDiaPie() {
        return modelDocumentosMesaPorDiaPie;
    }

    public void setModelDocumentosMesaPorDiaPie(PieChartModel modelDocumentosMesaPorDiaPie) {
        this.modelDocumentosMesaPorDiaPie = modelDocumentosMesaPorDiaPie;
    }

    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }

    public LineChartModel getModelDocumentosMesaPorDiaLinear() {
        return modelDocumentosMesaPorDiaLinear;
    }

    public void setModelDocumentosMesaPorDiaLinear(LineChartModel modelDocumentosMesaPorDiaLinear) {
        this.modelDocumentosMesaPorDiaLinear = modelDocumentosMesaPorDiaLinear;
    }

    public List<ChartObjetivoPrincipal> getLista() {
        return lista;
    }

    public void setLista(List<ChartObjetivoPrincipal> lista) {
        this.lista = lista;
    }

    public List<TiposObjetivos> getListaTiposDoc() {
        return listaTiposDoc;
    }

    public void setListaTiposDoc(List<TiposObjetivos> listaTiposDoc) {
        this.listaTiposDoc = listaTiposDoc;
    }

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    public BarChartModel getModelDocumentosMesaPorDiaBar() {
        return modelDocumentosMesaPorDiaBar;
    }

    public void setModelDocumentosMesaPorDiaBar(BarChartModel modelDocumentosMesaPorDiaBar) {
        this.modelDocumentosMesaPorDiaBar = modelDocumentosMesaPorDiaBar;
    }

    public List<Periodo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Periodo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
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

    public DashboardModel getDashboardModel() {
        return dashboardModel;
    }

    public void setDashboardModel(DashboardModel dashboardModel) {
        this.dashboardModel = dashboardModel;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public double getVariable2() {
        return variable2;
    }

    public void setVariable2(double variable2) {
        this.variable2 = variable2;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getMetasAlcanzada() {
        return metasAlcanzada;
    }

    public void setMetasAlcanzada(double metasAlcanzada) {
        this.metasAlcanzada = metasAlcanzada;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getTipoObjetivo() {
        return tipoObjetivo;
    }

    public void setTipoObjetivo(String tipoObjetivo) {
        this.tipoObjetivo = tipoObjetivo;
    }

    public Year getAño() {
        return año;
    }

    public void setAño(Year año) {
        this.año = año;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    

}

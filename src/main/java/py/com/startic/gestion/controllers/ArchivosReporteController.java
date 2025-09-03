package py.com.startic.gestion.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.ArchivosReporte;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.BajaArticulos;
import py.com.startic.gestion.models.Empresas;
import py.com.startic.gestion.models.EntradasArticulo;
import py.com.startic.gestion.models.FirmasArticulosSalida;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.SalidasArticulo;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "archivosReporteController")
@ViewScoped
public class ArchivosReporteController extends AbstractController<ArchivosReporte> {

    @Inject
    private SalidasArticuloController salidasArticuloController;
    @RequestScoped
    private UploadedFile file;
    private ParametrosSistema par;
    private SalidasArticulo salidaArticulo;
    private ArchivosReporte archivoReporte;
    private String nombre;
    private String content;
    private String archivo;
    private HttpSession session;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public SalidasArticulo getSalidaArticulo() {
        return salidaArticulo;
    }

    public void setSalidaArticulo(SalidasArticulo salidaArticulo) {
        this.salidaArticulo = salidaArticulo;
    }

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
    }

    public ArchivosReporte getArchivoReporte() {
        return archivoReporte;
    }

    public void setArchivoReporte(ArchivosReporte archivoReporte) {
        this.archivoReporte = archivoReporte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
    

    @Override
    public ArchivosReporte prepareCreate(ActionEvent event) {
        ArchivosReporte arch = super.prepareCreate(event);

        file = null;
        salidaArticulo = null;

        return arch;
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        // HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        // usuario = (Usuarios) session.getAttribute("Usuarios");
    }

    public ArchivosReporteController() {
        // Inform the Abstract parent controller of the concrete Empresas Entity
        super(ArchivosReporte.class);
    }

    public ArchivosReporte prepareArchivosReporte(SalidasArticulo salidas) {
        ArchivosReporte arch = this.prepareCreate(null);

        arch.setSalidaArticulo(salidas);

        return arch;
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSalidasReporte(ActionEvent event) {
        if (this.getSelected() != null && salidasArticuloController.getSelected() == null) {
            salidasArticuloController.setSelected(this.getSelected().getSalidaArticulo());
        }
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usuario = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
        }

        super.save(event);
    }

    public String saveNewArchivo() throws IOException {
        if (getSelected() != null) {
            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usuario = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(new Empresas(1));

            String filename = FilenameUtils.getName(file.getFileName());
            InputStream input = file.getInputStream();
            OutputStream output = new FileOutputStream(new File("C:\\Users\\DELL\\Documents\\sistema\\archivos" + filename));

            getSelected().setArchivo(file.getFileName());
            // getSelected().setSalidaArticulo(salidaArticulo);

            try {
                IOUtils.copy(input, output);

            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
                FacesMessage message = new FacesMessage("CARGA CORRECTA");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }

        super.saveNew(null);
        if (salidaArticulo != null) {
            SalidasArticulo sal = ejbFacade.getEntityManager().createNamedQuery("SalidasArticulo.findById", SalidasArticulo.class
            ).setParameter("id", getSelected().getSalidaArticulo().getId()).getSingleResult();
            sal.setArchivosReporte(getSelected());

            salidasArticuloController.setSelected(sal);

            salidasArticuloController.save(null);
        }

        return "/pages/salidasArticulo/index";

    }

    public void jButtonAutionPerformed() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.jem.gov.py/biblioteca/"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR, no se puede ejecutar la accion ");

        }

    }
     public void prepareCerrarDialogoVerDoc() {
        if (archivoReporte != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
            f.delete();

            archivoReporte = null;
        }
    }

    public String getContent() {

        nombre = "";
        try {
            if (archivoReporte != null) {
               if (archivoReporte.getSalidaArticulo() != null) {

                    byte[] fileByte = null;

                    if (archivoReporte.getArchivo() != null) {
                        try {
                            fileByte = Files.readAllBytes(new File("/D:/ReporteNroFormulario" + "/" + archivoReporte.getArchivo()).toPath());

                        } catch (IOException ex) {
                            JsfUtil.addErrorMessage("No tiene documento adjunto");
                            content = "";
                        }
                    }

                    if (fileByte != null) {
                        Date fecha = ejbFacade.getSystemDate();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);

                        String partes[] = archivoReporte.getArchivo().split("[.]");
                        String ext = "pdf";

                        if (partes.length > 1) {
                            ext = partes[partes.length - 1];
                        }

                        nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                        FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
                        outputStream.write(fileByte);

                        outputStream.close();

                        // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        // return "http://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;
    }

}

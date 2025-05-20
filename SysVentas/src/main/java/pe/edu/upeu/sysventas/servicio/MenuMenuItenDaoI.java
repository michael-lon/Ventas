package pe.edu.upeu.sysventas.servicio;

import pe.edu.upeu.sysventas.dto.MenuMenuItemTO;

import java.util.List;
import java.util.Properties;

public interface MenuMenuItenDaoI {
    public List<MenuMenuItemTO> listaAccesos(String perfil, Properties
            idioma);
}

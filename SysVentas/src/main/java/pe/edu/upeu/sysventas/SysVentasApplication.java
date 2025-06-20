package pe.edu.upeu.sysventas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pe.edu.upeu.sysventas.modelo.Marca;
import pe.edu.upeu.sysventas.repositorio.EmisorRepository;
import pe.edu.upeu.sysventas.repositorio.MarcaRepository;

import java.util.List;

@SpringBootApplication
public class SysVentasApplication extends Application /*implements CommandLineRunner*/ {
	private static ConfigurableApplicationContext configurableApplicationContext;
	private Parent parent;
	/*@Autowired
	private MarcaRepository marcaRepository;*/

	public static void main(String[] args) {
		//SpringApplication.run(SysVentasApplication.class, args);
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SysVentasApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		configurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_producto.fxml"));
		fxmlLoader.setControllerFactory(configurableApplicationContext::getBean);
		parent= fxmlLoader.load();
	}


	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
		scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("SysAlmacen Spring Java-FX");
		stage.setResizable(false);
		stage.show();
	}

	/*@Override
	public void run(String... args) throws Exception {
		List<Marca> listaMarcas = marcaRepository.findAll();
		for (Marca marca : listaMarcas) {
			System.out.println(marca.getNombre());
		}
	}*/
}

package pe.edu.upeu.sysventas.control;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.componente.ColumnInfo;
import pe.edu.upeu.sysventas.componente.ComboBoxAutoComplete;
import pe.edu.upeu.sysventas.componente.TableViewHelper;
import pe.edu.upeu.sysventas.componente.Toast;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.modelo.Producto;
import pe.edu.upeu.sysventas.servicio.CategoriaService;
import pe.edu.upeu.sysventas.servicio.MarcaService;
import pe.edu.upeu.sysventas.servicio.ProductoService;
import pe.edu.upeu.sysventas.servicio.UnidadMedidaService;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

@Controller
public class ProductoController {

    @FXML
    TextField txtNombreProducto, txtPUnit,
            txtPUnitOld, txtUtilidad, txtStock, txtStockOld,
            txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxMarca;
    @FXML
    ComboBox<ComboBoxOption> cbxCategoria;
    @FXML
    ComboBox<ComboBoxOption> cbxUnidMedida;
    @FXML
    private TableView<Producto> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    MarcaService ms;
    @Autowired
    CategoriaService cs;
    @Autowired
    ProductoService ps;
    @Autowired
    UnidadMedidaService ums;

    private Validator validator;
    ObservableList<Producto> listarProducto;
    Producto formulario;
    Long idProductoCE=0L;


    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();

        // Usar Platform.runLater para ejecutar el código después de que la escena se haya cargado
        /* Platform.runLater(() -> {
         stage = (Stage) miContenedor.getScene().getWindow();
         System.out.println("El título del stage es: " + stage.getTitle());
         });
        */

        cbxMarca.getItems().addAll(ms.listarCombobox());
        cbxMarca.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxMarca.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxMarca);

        cbxCategoria.getItems().addAll(cs.listarCombobox());
        cbxCategoria.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxCategoria.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxCategoria);


        cbxUnidMedida.getItems().addAll(ums.listarCombobox());
        cbxUnidMedida.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxUnidMedida.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxUnidMedida);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Nombre Producto", new ColumnInfo("nombre", 200.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("P. Unitario", new ColumnInfo("pu", 150.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Utilidad", new ColumnInfo("utilidad", 100.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Marca", new ColumnInfo("marca.nombre", 200.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Categoria", new ColumnInfo("categoria.nombre", 200.0)); // Columna visible "Columna 2" mapea al campo "campo2"
// Definir las acciones de actualizar y eliminar
        Consumer<Producto> updateAction = (Producto producto) -> {
            System.out.println("Actualizar: " + producto);
            //editForm(producto);
        };
        Consumer<Producto> deleteAction = (Producto producto) -> {System.out.println("Actualizar: " + producto);
            ps.eliminarRegEntidad(producto.getIdProducto()); /*deletePerson(usuario);*/
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };
// Usar el helper para agregar las columnas en el orden correcto
        tableViewHelper.addColumnsInOrderWithSize(tableView, columns,updateAction, deleteAction );
// Agregar botones de eliminar y modificar
        tableView.setTableMenuButtonVisible(true);
        listar();


    }

    public void listar(){
        try {
            tableView.getItems().clear();
            listarProducto = FXCollections.observableArrayList(ps.listarEntidad());
            tableView.getItems().addAll(listarProducto);
            // Agregar un listener al campo de texto txtFiltroDato para filtrar los productos
           /* txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrarProductos(newValue);
            });*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}

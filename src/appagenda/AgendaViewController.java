package appagenda;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import entidades.Persona;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author javis
 */
public class AgendaViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private EntityManager entityManager;
    @FXML
    private TableView<Persona> tableViewAgenda;
    @FXML
    private TableColumn<Persona, String> columnNombre;
    @FXML
    private TableColumn<Persona, String> columnApellidos;
    @FXML
    private TableColumn<Persona, String> columnEmail;
    @FXML
    private TableColumn<Persona, String> columnProvincia;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private Button buttonGuardar;

    private entidades.Persona personaSeleccionada;
    @FXML
    private Button buttonNuevo;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonSuprimir;
    @FXML
    private AnchorPane rootAgendaView;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Alert alert = new Alert(AlertType.CONFIRMATION);
//        alert.setTitle("Prueba");
//        alert.showAndWait();
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnProvincia.setCellValueFactory(
                cellData -> {

                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getProvincia() != null) {
                        property.setValue(cellData.getValue().getProvincia().getNombre());
                    }
                    return property;
                });

        // ¿Pos?
        tableViewAgenda.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    personaSeleccionada = newValue;

                    if (personaSeleccionada != null) {
                        textFieldNombre.setText(personaSeleccionada.getNombre());
                        textFieldApellidos.setText(personaSeleccionada.getApellidos());
                    } else {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });

    }

    public void cargarTodasPersonas() {
        Query queryPersonaFindAll = entityManager.createNamedQuery("Persona.findAll");
        List<Persona> listPersona = queryPersonaFindAll.getResultList();
        tableViewAgenda.setItems(FXCollections.observableArrayList(listPersona)
        );
    }
    
    //Duda
//    public void resetearTabla(){
//        tableViewAgenda.getSelectionModel().clearSelection();
//        
////        textFieldNombre.setText("");
////        textFieldApellidos.setText("");
//        
//    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        if (personaSeleccionada.getId() != null) {

            //Actualizar propiedades
            personaSeleccionada.setNombre(textFieldNombre.getText());
            personaSeleccionada.setApellidos(textFieldApellidos.getText());

            //Hacer el merge y el commit
            entityManager.getTransaction().begin();
            entityManager.merge(personaSeleccionada);
            entityManager.getTransaction().commit();

            //Actualizar los nuevos valroes
            int numFilaSeleccionada = tableViewAgenda.getSelectionModel().getSelectedIndex();
            tableViewAgenda.getItems().set(numFilaSeleccionada, personaSeleccionada);

            // Devolver el foco al tableview
            TablePosition pos = new TablePosition(tableViewAgenda, numFilaSeleccionada, null);
            tableViewAgenda.getFocusModel().focus(pos);
            tableViewAgenda.requestFocus();

        }

    }

    @FXML
    private void onActionButtonNuevo(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PersonaDetalleView.fxml"));
            Parent rootDetalleView = fxmlLoader.load();

            PersonaDetalleViewController personaDetalleViewController = (PersonaDetalleViewController) fxmlLoader.getController();
            personaDetalleViewController.setRootAgendaView(rootAgendaView);

            //Intercambio de datos funcionales con el detalle
            personaDetalleViewController.setTableViewPrevio(tableViewAgenda);

            personaSeleccionada = new Persona();
            personaDetalleViewController.setPersona(entityManager, personaSeleccionada, true);

            //Cargar datos de persona
            personaDetalleViewController.mostrarDatos();

            // Ocultar la vista de la lista
            rootAgendaView.setVisible(false);
            //Añadir la vista detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootAgendaView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
        } catch (IOException ex) {
            Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onActionButtonEditar(ActionEvent event) {
        try {

            if (personaSeleccionada.getId() != null) {
                // Cargar la vista de detalle
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PersonaDetalleView.fxml"));
                Parent rootDetalleView = fxmlLoader.load();

                PersonaDetalleViewController personaDetalleViewController = (PersonaDetalleViewController) fxmlLoader.getController();
                personaDetalleViewController.setRootAgendaView(rootAgendaView);

                personaDetalleViewController.setPersona(entityManager, personaSeleccionada, false);

                //Cargar datos de persona
                personaDetalleViewController.mostrarDatos();

                //Intercambio de datos funcionales con el detalle
                personaDetalleViewController.setTableViewPrevio(tableViewAgenda);

                // Ocultar la vista de la lista
                rootAgendaView.setVisible(false);
                //Añadir la vista detalle al StackPane principal para que se muestre
                StackPane rootMain = (StackPane) rootAgendaView.getScene().getRoot();
                rootMain.getChildren().add(rootDetalleView);

            }

        } catch (IOException ex) {
            Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {

        if (personaSeleccionada.getId() != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(personaSeleccionada.getNombre() + " "
                    + personaSeleccionada.getApellidos());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    // Acciones a realizar si el usuario acepta
                    entityManager.getTransaction().begin();
                    entityManager.merge(personaSeleccionada);
                    entityManager.remove(personaSeleccionada);
                    entityManager.getTransaction().commit();
                    tableViewAgenda.getItems().remove(personaSeleccionada);
                    tableViewAgenda.getFocusModel().focus(null);
                    tableViewAgenda.requestFocus();

                } catch (Exception exS) {

                }

            } else {
                // Acciones a realizar si el usuario cancela
                int numFilaSeleccionada = tableViewAgenda.getSelectionModel().getSelectedIndex();
                tableViewAgenda.getItems().set(numFilaSeleccionada, personaSeleccionada);
                TablePosition pos = new TablePosition(tableViewAgenda, numFilaSeleccionada, null);
                tableViewAgenda.getFocusModel().focus(pos);
                tableViewAgenda.requestFocus();
            }
        }
    }

}

package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Book;
import model.entities.User;
import model.services.BookService;

public class BookListController implements Initializable, DataChangeListener{
	
	private BookService service;
	
	private ObservableList<Book> obsList;
	
	@FXML
	private TableView<Book> tableViewBook;

	@FXML
	private TableColumn<Book, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Book, String> tableColumnBookCode;
	
	@FXML
	private TableColumn<Book, String> tableColumnTitleBook;
	
	@FXML
	private TableColumn<Book, String> tableColumnCategory;
	
	@FXML
	private TableColumn<Book, Integer> tableColumnQuantity;	
	
	@FXML
	private TableColumn<Book, Integer> tableColumnLoans;
	
	@FXML
	private TableColumn<User, String> tableColumnHistoric;
	
	@FXML
	private TableColumn<Book, Book> tableColumnEDIT;
	
	@FXML
	private TableColumn<Book, Book> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Book obj = new Book();
		createDialogForm(obj, "/gui/BookForm.fxml", parentStage);
	}
	
	public void setBookService(BookService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnBookCode.setCellValueFactory(new PropertyValueFactory<>("bookCode"));
		tableColumnTitleBook.setCellValueFactory(new PropertyValueFactory<>("titleBook"));
		tableColumnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
		tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		tableColumnLoans.setCellValueFactory(new PropertyValueFactory<>("loans"));
		tableColumnHistoric.setCellValueFactory(new PropertyValueFactory<>("historic"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewBook.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Book> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewBook.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Book obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			BookFormController controller = loader.getController();
			controller.setBook(obj);
			controller.setBookService(new BookService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter user data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Book , Book>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Book obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/BookForm.fxml", Utils.currentStage(event)));
			}
		});
		
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Book, Book>() {
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Book obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
		
	}


	private void removeEntity(Book obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
	

}

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
import model.entities.User;
import model.services.UserService;

public class UserListController implements Initializable, DataChangeListener{
	
	private UserService service;
	
	private ObservableList<User> obsList;

	@FXML
	private TableView<User> tableViewUser;

	@FXML
	private TableColumn<User, Integer> tableColumnId;

	@FXML
	private TableColumn<User, String> tableColumnName;

	@FXML
	private TableColumn<User, String> tableColumnCPF;

	@FXML
	private TableColumn<User, String> tableColumnDateOfBirth;
	
	@FXML
	private TableColumn<User, String> tableColumnNumberOfPhone;
	
	@FXML
	private TableColumn<User, String> tableColumnAddress;
	
	@FXML
	private TableColumn<User, String> tableColumnHistoric;
	
	@FXML
	private TableColumn<User, User> tableColumnLoan;
	
	@FXML
	private TableColumn<User, User> tableColumnReturnBook;
	
	@FXML
	private TableColumn<User, User> tableColumnEDIT;
	
	@FXML
	private TableColumn<User, User> tableColumnREMOVE;

	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		User obj = new User();
		createDialogForm(obj, "/gui/UserForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			initializeNodes();
	}
	
	public void setUserService(UserService service) {
		this.service = service;
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
		tableColumnNumberOfPhone.setCellValueFactory(new PropertyValueFactory<>("numberOfPhone"));
		tableColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		tableColumnHistoric.setCellValueFactory(new PropertyValueFactory<>("historic"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewUser.prefHeightProperty().bind(stage.heightProperty());
	}
	
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<User> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewUser.setItems(obsList);
		initLoanButtons();
		initReturnBookButtons();
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(User obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			UserFormController controller = loader.getController();
			controller.setUser(obj);
			controller.setUserService(new UserService());
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
	
	private void initLoanButtons() {
		tableColumnLoan.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnLoan.setCellFactory(param -> new TableCell<User, User>() {
			private final Button button = new Button("loan");

			@Override
			protected void updateItem(User obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> loanEntity(obj, "/gui/LoanForm.fxml", Utils.currentStage(event), "loan"));
			}
		});
	}
	
	private void initReturnBookButtons() {
		tableColumnReturnBook.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnReturnBook.setCellFactory(param -> new TableCell<User, User>() {
			private final Button button = new Button("return book");

			@Override
			protected void updateItem(User obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> loanEntity(obj, "/gui/LoanForm.fxml", Utils.currentStage(event), "return"));
			}
		});
	}
	
	private void loanEntity(User obj, String absoluteName, Stage parentStage, String option) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			LoanFormController controller = loader.getController();
			controller.setUser(obj);
			controller.setOption(option);
			controller.setUserService(new UserService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter data");
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

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<User, User>() {
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(User obj, boolean empty) {
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
	
	private void removeEntity(User obj) {
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

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<User, User>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(User obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/UserForm.fxml", Utils.currentStage(event)));
			}
		});
		
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}


}

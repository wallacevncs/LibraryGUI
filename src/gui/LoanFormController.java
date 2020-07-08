package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.User;
import model.exceptions.ValidationException;
import model.services.UserService;

public class LoanFormController implements Initializable{

	private User entity;
	
	private UserService service;
	
	private String option;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtBookCode;
	
	@FXML
	private Label labelErrorBookCode;
	
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			String bookCode = getFormData();
			if(option.equals("loan")) {
				if(!service.loan(entity, bookCode))
					Alerts.showAlert("", null, "book not available in our collection", AlertType.INFORMATION);
			}
			else {
				service.returnBook(entity, bookCode);
			}
			
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void setOption(String option) {
		this.option = option;
	}
	
	public void setUser(User entity) {
		this.entity = entity;
	}
	
	public void setUserService(UserService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		initializeNodes();
	}
	
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtBookCode, 30);		
	}
	
	public void updateFormData() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));

	}
	
	private String getFormData() {
		User obj = new User();
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtBookCode.getText() == null || txtBookCode.getText().trim().equals("")) {
			exception.addError("book code", "Field can't be empty");
		}

		String bookCode = txtBookCode.getText();

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return bookCode;
	}
	
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorBookCode.setText((fields.contains("book code")) ? errors.get("book code") : "");
		
	}

}

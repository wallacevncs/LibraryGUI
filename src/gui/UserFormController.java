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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.User;
import model.exceptions.ValidationException;
import model.services.UserService;

public class UserFormController implements Initializable {
	
	private User entity;
	
	private UserService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtCPF;
	
	@FXML
	private TextField txtDateOfBirth;
	
	@FXML
	private TextField txtNumberOfPhone;
	
	@FXML
	private TextField txtAddress;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorCPF;
	
	@FXML
	private Label labelErrorDateOfBirth;
	
	@FXML
	private Label labelErrorNumberOfPhone;
	
	@FXML
	private Label labelErrorAddress;
	
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
			entity = getFormData();
			service.saveOrUpdate(entity);
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
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	public void setUser(User entity) {
		this.entity = entity;
	}
	
	public void setUserService(UserService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		initializeNodes();
	}
	
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtCPF, 11);
		Constraints.setTextFieldMaxLength(txtDateOfBirth, 10);
		Constraints.setTextFieldMaxLength(txtNumberOfPhone, 11);
		Constraints.setTextFieldMaxLength(txtAddress, 100);
		
	}
	
	public void updateFormData() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtCPF.setText(entity.getCpf());
		txtDateOfBirth.setText(entity.getDateOfBirth());
		txtNumberOfPhone.setText(entity.getNumberOfPhone());
		txtAddress.setText(entity.getAddress());
	}
	
	private User getFormData() {
		User obj = new User();
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(txtCPF.getText() == null || txtCPF.getText().trim().equals("")) {
			exception.addError("cpf", "Field can't be empty");
		}
		obj.setCpf(txtCPF.getText());
		
		if(txtDateOfBirth.getText() == null || txtDateOfBirth.getText().trim().equals("")) {
			exception.addError("date of birth", "Field can't be empty");
		}
		obj.setDateOfBirth(txtDateOfBirth.getText());
		
		if(txtNumberOfPhone.getText() == null || txtNumberOfPhone.getText().trim().equals("")) {
			exception.addError("number of phone", "Field can't be empty");
		}
		obj.setNumberOfPhone(txtNumberOfPhone.getText());
		
		if(txtAddress.getText() == null || txtAddress.getText().trim().equals("")) {
			exception.addError("address", "Field can't be empty");
		}
		obj.setAddress(txtAddress.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name")) ? errors.get("name") : "");

		labelErrorCPF.setText((fields.contains("cpf")) ? errors.get("cpf") : "");
		
		labelErrorDateOfBirth.setText((fields.contains("date of birth")) ? errors.get("date of birth") : "");

		labelErrorNumberOfPhone.setText((fields.contains("number of phone")) ? errors.get("number of phone") : "");

		labelErrorAddress.setText((fields.contains("address")) ? errors.get("address") : "");
	}

}



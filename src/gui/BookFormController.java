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
import model.entities.Book;
import model.exceptions.ValidationException;
import model.services.BookService;

public class BookFormController implements Initializable{
	
	private Book entity;
	
	private BookService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtBookCode;
	
	@FXML
	private TextField txtTitleBook;
	
	@FXML
	private TextField txtCategory;
	
	@FXML
	private TextField txtQuantity;
	
	@FXML
	private TextField txtLoans;
	
	@FXML
	private Label labelErrorBookCode;
	
	@FXML
	private Label labelErrorTitleBook;
	
	@FXML
	private Label labelErrorCategory;
	
	@FXML
	private Label labelErrorQuantity;
	
	@FXML
	private Label labelErrorLoans;
	
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
	
	public void setBook(Book entity) {
		this.entity = entity;
	}
	
	public void setBookService(BookService service) {
		this.service = service;
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		initializeNodes();		
	}
		
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtBookCode, 30);
		Constraints.setTextFieldMaxLength(txtTitleBook, 255);
		Constraints.setTextFieldMaxLength(txtCategory, 10);
		Constraints.setTextFieldInteger(txtQuantity);
		Constraints.setTextFieldInteger(txtLoans);
	}
	
	public void updateFormData() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtBookCode.setText(entity.getBookCode());
		txtTitleBook.setText(entity.getTitleBook());
		txtCategory.setText(entity.getCategory());
		txtQuantity.setText(String.valueOf(entity.getQuantity()));
		txtLoans.setText(String.valueOf(entity.getLoans()));
	}
	
	private Book getFormData() {
		Book obj = new Book();
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtBookCode.getText() == null || txtBookCode.getText().trim().equals(""))
			exception.addError("book code", "Field can't be empty");
		
		obj.setBookCode(txtBookCode.getText());
		
		if (txtTitleBook.getText() == null || txtTitleBook.getText().trim().equals(""))
			exception.addError("title book", "Field can't be empty");

		obj.setTitleBook(txtTitleBook.getText());
		
		if (txtCategory.getText() == null || txtCategory.getText().trim().equals("")) 
			exception.addError("category", "Field can't be empty");
		
		obj.setCategory(txtCategory.getText());
		
		if (txtQuantity.getText() == null || txtQuantity.getText().trim().equals("")) 
			exception.addError("quantity", "Field can't be empty");
		
		obj.setQuantity(Utils.tryParseToInt(txtQuantity.getText()));
		
		if (txtLoans.getText() == null || txtLoans.getText().trim().equals("")) 
			exception.addError("loans", "Field can't be empty");
		
		obj.setLoans(Utils.tryParseToInt(txtLoans.getText()));
		
		if (exception.getErrors().size() > 0) 
			throw exception;
		

		return obj;
	}
	
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorBookCode.setText((fields.contains("book code")) ? errors.get("book code") : "");

		labelErrorTitleBook.setText((fields.contains("title book")) ? errors.get("title book") : "");
		
		labelErrorCategory.setText((fields.contains("category")) ? errors.get("category") : "");

		labelErrorQuantity.setText((fields.contains("quantity")) ? errors.get("quantity") : "");

		labelErrorLoans.setText((fields.contains("loans")) ? errors.get("loans") : "");
	}
	
	
}

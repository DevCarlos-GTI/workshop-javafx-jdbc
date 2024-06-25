package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// criamos a dependencia
	private Seller entity;

	private SellerService service;

	private DepartmentService departmentService;

	// meus Fxmls
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Department> obsList;

	// vamos setar o Departamento
	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	// vamos setar o DepartamentoService
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	// escrever na minha lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// criar os eventos da botões
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);// Salva

			// notificar
			notifyDataChangeListeners();

			// salvo fecha a janela
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		// TODO Auto-generated method stub
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	// metoda para pegar o user digitou e instanciar um departamento p salvar
	private Seller getFormData() {

		// validar excecoes
		ValidationException exception = new ValidationException("Validation error");

		Seller obj = new Seller();
		obj.setId(Utils.tryParseToInt(txtId.getText())); // convertento p int o vai ser digitado p user

		if (txtName.getText() == null || txtName.getText().trim().equals("")) { // trim verifica se ta fazio
			exception.addError("name", "Field can´t be empty"); // o nome ñ pode ser fazio
		}
		obj.setName(txtName.getText()); // precisa convert pois ja é uma String

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close(); // no botão cancelar vai fechar a janela
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes(); // chamei a função
	}

	// vamos criar as funções d restrições
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);// so aceita numero inteiro
		Constraints.setTextFieldMaxLength(txtName, 70); // maximo 70 caracteres
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}

	// criar metos p pegar os dados da Entidades Seller p as Caxias de TextField
	public void updateFormData() {
		// temos testar 1° se a Entidade Seller inserida
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		// meu TextFild recebe o pega o id da Entidade Seller
		txtId.setText(String.valueOf(entity.getId()));// aqui converto de Int p String
		txtName.setText(entity.getName()); // ñ precisa converter p String pois ja é String
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary())); // nesse caso tenho convert
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));// ZoneId.systemDefault())
																									// do																										// pc
		}
		if(entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();// se for null pegue o 1°
		}else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	// o metodo responsável por carregar a lista no ComboBox de departamento
	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();// aqui traz todos Depart do DB
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	// metodo p erros
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

	//metodo p iniciar o comboBox
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}

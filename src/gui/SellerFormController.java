package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;


public class SellerFormController implements Initializable {
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
	//criamos a dependencia
	private Seller entity;
	
	private SellerService service;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelError;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	//vamos setar o Departamento
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	//vamos setar o DepartamentoService
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	//escrever na minha lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	//criar os eventos da botões
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);//Salva
			
			//notificar
			notifyDataChangeListeners();
			
			//salvo fecha a janela
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private void notifyDataChangeListeners() {
		// TODO Auto-generated method stub
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	//metoda para pegar o user digitou e instanciar um departamento p salvar
	private Seller getFormData() {
		
		//validar excecoes
		ValidationException exception = new ValidationException("Validation error");
		
		Seller obj = new Seller();
		obj.setId(Utils.tryParseToInt(txtId.getText())); //convertento p int o vai ser digitado p user
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) { //trim verifica se ta fazio
			exception.addError("name", "Field can´t be empty"); // o nome ñ pode ser fazio 
		}
		obj.setName(txtName.getText()); // precisa convert pois ja é uma String
		
		if(exception.getErrors().size() >0) {
			throw exception;
		}
		
		return obj;
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close(); //no botão cancelar vai fechar a janela
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes(); //chamei a função 
	}
	
	//vamos criar as funções d restrições
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);//so aceita numero inteiro
		Constraints.setTextFieldMaxLength(txtName, 30); // maximo 30 caracteres 
	}

	//criar metos p pegar os dados da Entidades Seller p as Caxias de TextField
	public void updateFormData() {
		//temos testar 1° se a Entidade Seller inserida
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		//meu TextFild recebe o pega o id da Entidade Seller
		txtId.setText(String.valueOf(entity.getId()));//aqui converto de Int p String
		txtName.setText(entity.getName()); //ñ precisa converter p String pois ja é String
	}
	
	//metodo p erros
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelError.setText(errors.get("name"));
		}
	}

}
 
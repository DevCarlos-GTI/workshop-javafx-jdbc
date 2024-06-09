package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	//criamos a dependencia
	private Department entity;
	
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
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	//criar os eventos da botões
	@FXML
	public void onBtSaveAction() {
		//System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
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

	//criar metos p pegar os dados da Entidades Department p as Caxias de TextField
	public void updateFormData() {
		//temos testar 1° se a Entidade Department inserida
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		//meu TextFild recebe o pega o id da Entidade Department
		txtId.setText(String.valueOf(entity.getId()));//aqui converto de Int p String
		txtName.setText(entity.getName()); //ñ precisa converter p String pois ja é String
	}

}
 
package gui.util;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) { //vamos ter uma ação com button
		
		//vamos criar ação
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
			//(downcast ) é quando o objeto se passa como se fosse um subtipo dele.
	}
}

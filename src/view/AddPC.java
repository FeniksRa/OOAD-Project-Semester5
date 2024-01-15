package view;

import controller.PCController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddPC {

	Scene scene;
	BorderPane bp;
	Label labelPcID;
	TextField textFieldPcID;
	Button add;
	VBox container, conButton, mergeContainer;
	Insets paddingContainer, marginButton;

	void initialize() {
		bp = new BorderPane();
		labelPcID = new Label("Masukkan Pc ID");
		textFieldPcID = new TextField();
		add = new Button("Add PC");

		mergeContainer = new VBox();
		container = new VBox();
		conButton = new VBox();
		
		paddingContainer = new Insets(10, 10, 10, 10);
		marginButton = new Insets(15, 5, 5, 5);
		
		scene = new Scene(bp, 500, 500);
	}

	void layouting() {
		container.getChildren().addAll(labelPcID, textFieldPcID);
		conButton.getChildren().addAll(add);
		mergeContainer.getChildren().addAll(container, conButton);

		conButton.setAlignment(Pos.CENTER);
		VBox.setMargin(add, marginButton);
		
		mergeContainer.setMaxWidth(150);
		textFieldPcID.setMaxWidth(150);
		add.setAlignment(Pos.CENTER);
		mergeContainer.setAlignment(Pos.CENTER);
		
		bp.setCenter(mergeContainer);
	}

	void actions(Stage primaryStage) {
		add.setOnMouseClicked(e -> {
			String idText = textFieldPcID.getText();
			boolean check = false;
			
			if(idText.equals(null)) {
				alert(check, "Please Input Pc ID", primaryStage);
			}
			
			check = new PCController().addNewPC();
			
			if(check == true) {
				alert(check, "Add PC Success", primaryStage);
			}else {
				alert(check, "Add PC Failed", primaryStage);
			}
		});
	}

	public void alert(boolean check, String msg, Stage primaStage) {
		Alert alert;
		
		if(check == true) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(msg);
			new HomeAdmin(primaStage);
		}else {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(msg);
		}
		alert.show();
	}
	
	public AddPC(Stage primaryStage) {
		initialize();
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("Welcome Admin");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

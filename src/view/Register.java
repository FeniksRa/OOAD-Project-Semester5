package view;

import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Kelas untuk menangani registrasi pengguna baru pada aplikasi.
 */
public class Register{
	Scene scene;
	GridPane gp;
	FlowPane fp;
	BorderPane bp;
	Label title, nameLbl, passLbl, ageLbl;
	TextField nameTF;
	PasswordField passwordTF;
	Spinner<Integer> age;
	VBox registerContainer, registerForm;
	Button registBtn, back;
	
	 /**
     * Metode inisialisasi untuk mengatur atribut dan elemen UI awal.
     */
	void initialize() {
    	bp = new BorderPane();
    	title = new Label("Register");
    	nameLbl = new Label("Username");
    	passLbl = new Label("Password");
    	ageLbl = new Label("Age");
    	nameTF = new TextField();
    	passwordTF = new PasswordField();
    	age = new Spinner<>(13, 65, 13);
    	registerContainer = new VBox();
    	registerForm = new VBox();
    	registBtn = new Button("Register");
    	back = new Button("Back");
    	
    	scene = new Scene(bp, 1000, 500);
    }
    
	/**
     * Metode untuk mengatur tata letak elemen UI pada laman registrasi.
     */
    void layouting() {
    	registerContainer.getChildren().addAll(title, 
				registerForm, registBtn, back);
		registerForm.getChildren().addAll(nameLbl, nameTF,
				passLbl, passwordTF, ageLbl, age);
		nameTF.setPromptText("Enter your username");
	    passwordTF.setPromptText("Enter your password");
		bp.setCenter(registerContainer);
		
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		nameTF.setMaxWidth(200);
		passwordTF.setMaxWidth(200);
		registBtn.setMaxWidth(200);
		back.setMaxWidth(200);
		
		registerContainer.setSpacing(20);
		registerContainer.setAlignment(Pos.CENTER);
		registerForm.setSpacing(5);
		registerForm.setMaxWidth(200);
    }
    
    /**
     * Metode untuk menangani aksi tombol pada laman registrasi.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
    void actions(Stage primaryStage) {
    	registBtn.setOnMouseClicked(e->{
    		String name = nameTF.getText();
    		String pass = passwordTF.getText();
    		int ages = age.getValue();
    		UserController userController = new UserController();
    		boolean user = false;
    		user = userController.addNewUser(name, pass, ages);
    		if(user == true){
    			try {
					new Login().loginIn(primaryStage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    	});
    	
    	back.setOnAction(e->{
			new Home().start(primaryStage);
		});
    }
    
    /**
     * Metode untuk memulai proses registrasi pengguna.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     * @throws Exception Jika terjadi kesalahan saat menampilkan laman.
     */
    void Registration(Stage primaryStage) throws Exception{
    	initialize();
		layouting();
		primaryStage.show();
		primaryStage.setTitle("Register");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
		actions(primaryStage);
    }
}



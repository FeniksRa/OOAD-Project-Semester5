package view;

import javax.security.auth.login.LoginException;

import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;

/**
 * Kelas Login menangani tampilan dan logika untuk proses login pengguna.
 */
public class Login {
	Scene scene;
	GridPane gp;
	FlowPane fp;
	BorderPane bp;
	Label title, nameLbl, passLbl;
	TextField nameTF;
	PasswordField passwordTF;
	VBox loginContainer, loginForm;
	Button loginBtn, back;

	 /**
     * Inisialisasi elemen-elemen utama.
     */
	void initialize() {
		bp = new BorderPane();
		title = new Label("Login");
		nameLbl = new Label("Username");
		passLbl = new Label("Password");
		nameTF = new TextField();
		passwordTF = new PasswordField();
		loginContainer = new VBox();
		loginForm = new VBox();
		loginBtn = new Button("Login");
		back = new Button("Back");

		scene = new Scene(bp, 1000, 500);
	}
	
	 /**
     * Menentukan tata letak tampilan login, termasuk menambahkan elemen-elemen
     * seperti label, TextField, dan tombol ke dalam VBox dan BorderPane.
     */
	void layouting() {
		loginContainer.getChildren().addAll(title, loginForm, loginBtn, back);
		loginForm.getChildren().addAll(nameLbl, nameTF, passLbl, passwordTF);
		
		nameTF.setPromptText("Enter your username");
	    passwordTF.setPromptText("Enter your password");
		bp.setCenter(loginContainer);

		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		nameTF.setMaxWidth(200);
		passwordTF.setMaxWidth(200);
		loginBtn.setMaxWidth(200);
		back.setMaxWidth(200);
		
		loginContainer.setSpacing(20);
		loginContainer.setAlignment(Pos.CENTER);
		loginForm.setSpacing(5);
		loginForm.setMaxWidth(200);
	}

	/**
     * Menangani aksi-aksi yang terkait dengan tombol-tombol di tampilan login.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     */
	void actions(Stage primaryStage) {
		loginBtn.setOnAction(e -> {
			String name = nameTF.getText();
			String pass = passwordTF.getText();
			UserController userController = new UserController();

			try {
				User userLogin = userController.getUserData(name, pass);

				switch (userLogin.getUserRole()) {
				case "Admin":
					// Redirect to Admin page
					redirectToAdminPage(primaryStage);
					break;
				case "Computer Technician":
					// Redirect to Technician page
					redirectToTechnicianPage(primaryStage, userLogin.getUserID());
					break;
				case "Customer":
					// Redirect to Customer page
					redirectToCustomerPage(primaryStage, userLogin.getUserID());
					break;
				case "Operator":
					redirectToOperatorPage(primaryStage, userLogin.getUserID());
					break;
				default:
					// Handle unexpected role
					throw new LoginException("Unexpected user role: " + userLogin.getUserRole());
				}
			} catch (LoginException l) {
				// Display an alert for invalid login
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("Invalid Username/Password");
				alert.setContentText("Please check your username and password and try again.");
				alert.show();
			}
		});
		
		back.setOnAction(e->{
			new Home().start(primaryStage);
		});
	}

	/**
     * Mengarahkan pengguna ke tampilan HomeAdmin setelah berhasil login sebagai Admin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     */
	private void redirectToAdminPage(Stage primaryStage) {
		// Code to redirect to Admin page
		HomeAdmin homeAdmin = new HomeAdmin(primaryStage);
		System.out.println("Admin Page");

	}

	/**
     * Mengarahkan pengguna ke tampilan HomeTechnician setelah berhasil login sebagai Technician.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna yang berhasil login.
     */
	private void redirectToTechnicianPage(Stage primaryStage, int userId) {
		// Code to redirect to Technician page
		HomeTechnician homeTechnician = new HomeTechnician(primaryStage, userId);
		System.out.println("Technician Page");
	}

	/**
     * Mengarahkan pengguna ke tampilan HomeCustomer setelah berhasil login sebagai Customer.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId       ID pengguna yang berhasil login.
     */
	private void redirectToCustomerPage(Stage primaryStage, int userId) {
		// Code to redirect to Customer page
		HomeCustomer homeCustomer = new HomeCustomer(primaryStage, userId);
		System.out.println("Customer Page");
	}

	/**
     * Mengarahkan pengguna ke tampilan HomeOperator setelah berhasil login sebagai Operator.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna yang berhasil login.
     */
	private void redirectToOperatorPage(Stage primaryStage, int userId) {
		new HomeOperator(primaryStage, userId);
		System.out.println("Operator Page");
	}
	
	/**
     * Menampilkan tampilan login pada stage yang diberikan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @throws Exception Jika terjadi kesalahan selama proses login.
     */
	public void loginIn(Stage primaryStage) throws Exception {
		initialize();
		layouting();
		primaryStage.show();
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
		actions(primaryStage);
	}
}

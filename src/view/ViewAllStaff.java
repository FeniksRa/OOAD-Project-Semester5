package view;

import java.util.List;

import controller.PCController;
import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

/**
 * Kelas untuk menampilkan daftar seluruh staf dengan kemampuan mengubah peran (role) staf.
 */
public class ViewAllStaff {

	Scene scene;
	BorderPane bp;
	Button changeRole, back;
	VBox mergeContainer;
	HBox buttonContainer, userContainer;

	ComboBox<Integer> userId;
	ComboBox<String> newRole;

	List<User> listUser;

	/**
     * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan mendapatkan data seluruh staf.
     */
	void initialize() {
		bp = new BorderPane();
		changeRole = new Button("Change Role");
		back = new Button("Back");

		mergeContainer = new VBox(10);
		buttonContainer = new HBox(10);
		userContainer = new HBox(20);

		userId = new ComboBox<>();
		newRole = new ComboBox<>();

		listUser = new UserController().getAllUserData();

		userId.setPromptText("Select Id");
		newRole.setPromptText("Select New Role");

		for (User user : listUser) {
			userId.getItems().add(user.getUserID());
		}

		newRole.getItems().addAll("Admin", "Operator", "Computer Technician");

		scene = new Scene(bp, 1000, 500);
	}

	/**
     * Metode untuk mengatur tata letak elemen-elemen UI pada laman tampilan seluruh staf.
     */
	void layouting() {
		buttonContainer.getChildren().addAll(userId, newRole, changeRole, back);
		mergeContainer.getChildren().addAll(userContainer, buttonContainer);

		buttonContainer.setAlignment(Pos.CENTER);
		userContainer.setAlignment(Pos.CENTER);
		mergeContainer.setAlignment(Pos.CENTER);
		mergeContainer.setSpacing(25);
       
		for (User user : listUser) {
			userContainer.getChildren().addAll(createShowUserDataBox(user.getUserID(), user.getUserRole()));
		}
		
		ScrollPane scrollPane = new ScrollPane(userContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
		VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 20, 10));
        mainContainer.getChildren().addAll(scrollPane, mergeContainer);
        mainContainer.setSpacing(10);
		
		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
		StackPane stackPane = new StackPane();
	    stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);

		stackPane.setAlignment(Pos.CENTER);

		bp.setCenter(stackPane);
	}

	 /**
     * Metode untuk menangani aksi tombol pada laman tampilan seluruh staf.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> back(primaryStage));
		changeRole.setOnMouseClicked(e -> changeRole(primaryStage));
	}

	/**
     * Metode untuk mengubah peran staf berdasarkan inputan pengguna.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	void changeRole(Stage primaryStage) {
		Integer id = userId.getValue();
		String role = newRole.getValue();

		if (id == null) {
			alert(false, "Please select user id", primaryStage);
			return;
		}
		if (role == null) {
			alert(false, "Please select new role", primaryStage);
			return;
		}

		boolean check = new UserController().changeUserRole(id, role);

		if (check == true)
			alert(check, "Success change role", primaryStage);
	}

	/**
     * Metode untuk kembali ke laman admin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	void back(Stage primaryStage) {
		new HomeAdmin(primaryStage);
	}

	/**
     * Metode untuk membuat dan mengembalikan VBox yang menampilkan data staf.
     * @param id User ID staf.
     * @param userRole Peran (role) staf.
     * @return VBox yang berisi data staf.
     */
	VBox createShowUserDataBox(Integer id, String userRole) {
		VBox userDataBox = new VBox(5);
		ImageView sementara = new ImageView(new Image(getClass().getResourceAsStream("/resources/icon-profile.png")));
		sementara.setFitWidth(95);
		sementara.setFitHeight(95);
		userDataBox.getChildren().addAll(sementara, new Label("User " + id), new Label(userRole));
		userDataBox.setAlignment(Pos.CENTER);

		return userDataBox;
	}

	/**
     * Metode untuk menampilkan alert dengan pesan yang sesuai.
     * @param check Indikasi berhasil atau tidaknya suatu aksi.
     * @param msg Pesan yang akan ditampilkan pada alert.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	public void alert(boolean check, String msg, Stage primaryStage) {
		Alert alert;

		if (check == true) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(msg);
			new HomeAdmin(primaryStage);
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(msg);
		}
		alert.show();
	}
	
	/**
     * Metode untuk membuat dan mengembalikan objek Background berdasarkan gambar latar.
     * @return Background yang dibuat dari gambar latar.
     */
	private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/admin-bg.png"));
	    BackgroundImage background = new BackgroundImage(
	            backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	            BackgroundPosition.DEFAULT, new BackgroundSize(1000, 500, false, false, false, true));
	    
	    return new Background(background);
	}
	
	 /**
     * Metode untuk membuat dan mengembalikan objek StackPane tambahan untuk latar belakang.
     * @param content Node yang akan ditampilkan di atas latar belakang.
     * @return StackPane tambahan untuk latar belakang.
     */
	private StackPane createAdditionalBackgroundPane(Node content) {
	    StackPane additionalBackgroundPane = new StackPane();
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    additionalBackgroundPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);" +
	            "-fx-background-radius: 10;");
	    return additionalBackgroundPane;
	}

	/**
     * Konstruktor untuk membuat objek ViewAllStaff.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	public ViewAllStaff(Stage primaryStage) {
		initialize();
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("View All Staff");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

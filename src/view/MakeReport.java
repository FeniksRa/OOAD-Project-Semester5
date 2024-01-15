package view;

import java.util.ArrayList;
import java.util.List;

import controller.PCController;
import controller.ReportController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PC;

/**
 * Kelas untuk membuat laporan terkait PC yang dapat diakses oleh Operator dan Customer.
 */
public class MakeReport {

	Scene scene;
	Button backButton, reportButton;
	VBox container1;
	BorderPane borderPane;
	FlowPane flowPane;
	Insets marginContainer, paddingContainer;
	TextArea reportNote;
	ComboBox<Integer> pcComboBox;
	List<PC> listPCId;

	/**
     * Metode inisialisasi untuk mengatur atribut dan elemen UI awal.
     */
	void initialize() {
		backButton = new Button("Back");
		reportButton = new Button("Send");
		container1 = new VBox();
		reportNote = new TextArea();
		pcComboBox = new ComboBox<>();

		borderPane = new BorderPane();
		flowPane = new FlowPane();
		listPCId = new PCController().getAllPCData();

		marginContainer = new Insets(0, 0, 20, 0);
		scene = new Scene(borderPane, 1000, 500);

		pcComboBox.setPromptText("Select PC");
		for (PC pc : listPCId) {
			pcComboBox.getItems().addAll(pc.getPCID());
		}
	}

	/**
     * Metode untuk mengatur tata letak elemen UI pada laman pembuatan laporan.
     * @param userRole Peran pengguna yang membuka laman (Operator atau Customer).
     */
	void layouting(String userRole) {
		reportNote.setPrefWidth(850);
		reportNote.setPrefHeight(400);

		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(pcComboBox, reportButton, backButton);

		HBox reportBox = new HBox(10);
		reportBox.getChildren().addAll(reportNote);

		container1.getChildren().addAll(reportBox, buttonBox);
		container1.setAlignment(Pos.BASELINE_CENTER);
		container1.setSpacing(20);
		buttonBox.setAlignment(Pos.CENTER);
		reportBox.setAlignment(Pos.CENTER);
		
		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground(userRole));
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(container1);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);

        borderPane.setCenter(stackPane);

	}

	/**
     * Metode untuk menangani aksi tombol pada laman pembuatan laporan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     * @param userRole Peran pengguna yang membuka laman (Operator atau Customer).
     * @param userId ID pengguna yang membuka laman.
     */
	void action(Stage primaryStage, String userRole, int userId) {
		// TODO Auto-generated method stub
		backButton.setOnAction(e -> goBack(userRole, userId));
		reportButton.setOnAction(e -> createReport(primaryStage, userRole));
	}

	 /**
     * Metode untuk kembali ke laman utama (Home) sesuai peran pengguna.
     * @param userRole Peran pengguna yang membuka laman (Operator atau Customer).
     * @param userId ID pengguna yang membuka laman.
     */
	private void goBack(String userRole, int userId) {
		// Handle the action to go back to the Home Page
		Stage primaryStage = (Stage) scene.getWindow();
		if (userRole.equals("Operator")) {
			new HomeOperator(primaryStage, userId);
		} else {
			new HomeCustomer(primaryStage, userId);
		}
//        primaryStage.setScene(homePage.getScene());
		primaryStage.setTitle("Home Page");
	}

	 /**
     * Metode untuk membuat dan menyimpan laporan baru ke dalam database.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     * @param userRole Peran pengguna yang membuka laman (Operator atau Customer).
     */
	private void createReport(Stage primaryStage, String userRole) {
		Integer pcID = pcComboBox.getValue();

		// Mengambil nilai dari TextArea dan menyimpannya dalam variabel String
		String reportNoteText = reportNote.getText();

		if (pcID == null || reportNoteText.isEmpty()) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setTitle("Error");
			errorAlert.setHeaderText("Failed to submit report. Please try again.");
			errorAlert.showAndWait();
		} else {
			ReportController reportController = new ReportController();
			boolean success = reportController.addNewReport(userRole, pcID, reportNoteText);

			if (success) {
				Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
				successAlert.setTitle("Success");
				successAlert.setHeaderText("Report submitted successfully.");
				successAlert.showAndWait();
			} else {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setTitle("Error");
				errorAlert.setHeaderText("Failed to submit report. Please try again.");
				errorAlert.showAndWait();
			}
		}
	}
	
	/**
     * Metode untuk membuat latar belakang laman sesuai dengan peran pengguna.
     * @param userRole Peran pengguna yang membuka laman (Operator atau Customer).
     * @return Objek Background yang berisi gambar latar belakang sesuai peran pengguna.
     */
	private Background createBackground(String userRole) {
		Image backgroundImage;
		if (userRole.equals("Operator")) {
			backgroundImage = new Image(getClass().getResourceAsStream("/resources/operator-bg.png"));
		} else {
			backgroundImage = new Image(getClass().getResourceAsStream("/resources/icafe-bg.jpg"));
		}
	    BackgroundImage background = new BackgroundImage(
	            backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	            BackgroundPosition.DEFAULT, new BackgroundSize(1000, 500, false, false, false, true));
	    
	    return new Background(background);
	}
	
	/**
     * Metode untuk membuat lapisan tambahan pada latar belakang dengan ukuran maksimum.
     * @param content Node yang berisi elemen-elemen laman.
     * @return StackPane yang berisi lapisan tambahan dengan elemen laman.
     */
	private StackPane createAdditionalBackgroundPane(Node content) {
	    StackPane additionalBackgroundPane = new StackPane();
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    
	    return additionalBackgroundPane;
	}

	/**
     * Konstruktor untuk membuat objek MakeReport dan menyiapkan laman pembuatan laporan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     * @param userRole Peran pengguna yang membuka laman (Operator atau Customer).
     * @param userId ID pengguna yang membuka laman.
     */
	public MakeReport(Stage primaryStage, String userRole, int userId) {
		initialize();
		layouting(userRole);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Report");
		action(primaryStage, userRole, userId);
	}

	/**
     * Metode untuk mendapatkan objek Scene yang digunakan pada laman pembuatan laporan.
     * @return Objek Scene dari laman pembuatan laporan.
     */
	public Scene getScene() {
		return scene;
	}
}

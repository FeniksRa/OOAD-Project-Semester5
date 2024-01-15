package view;

import javax.security.auth.login.LoginException;

import controller.JobController;
import controller.PCController;
import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
import model.Job;
import model.PC;
import model.User;

/**
 * Kelas AddStaffJob bertanggung jawab untuk menangani antarmuka pengguna
 * dalam menambahkan pekerjaan baru untuk teknisi komputer. Kelas ini
 * mencakup tampilan GUI serta logika pemrosesan data terkait penugasan pekerjaan.
 */
public class AddStaffJob {
	Scene scene;
	BorderPane bp;
	VBox containerV;
	HBox containerH;
	Label title;
	Button add, back;

	TableColumn<PC, Integer> pcIDCol;
	TableColumn<PC, String> pcStatusCol;

	TableView<PC> tableView;

	Insets paddingBp;

	ObservableList<PC> pcList;
	ObservableList<Job> jobList;
	ObservableList<User> userList;
	ComboBox<Integer> jobID;
	ComboBox<Integer> userID;
	ComboBox<Integer> pcID;
	ComboBox<String> pcStat;

	JobController jobController;
	PCController pcController;
	UserController userController;
	

	/**
     * Metode initialize digunakan untuk menginisialisasi komponen-komponen
     * GUI dan pengaturan awal yang diperlukan untuk tampilan penugasan pekerjaan.
     * Inisialisasi termasuk pembuatan objek-objek, alokasi memori untuk
     * beberapa koleksi data, dan pengaturan prompt text pada combo box.
     */
	void initialize() {
		bp = new BorderPane();
		pcList = FXCollections.observableArrayList(new PCController().getAllPCData());
		jobList = FXCollections.observableArrayList(new JobController().getAllJobData());
		userList = FXCollections.observableArrayList(new UserController().getAllTechnician());
		jobID = new ComboBox<>();
		userID = new ComboBox<>();
		pcID = new ComboBox<>();
		pcStat = new ComboBox<>();
		title = new Label("Computer Technician's Job");

		back = new Button("Back");
		add = new Button("Add New Job");

		pcIDCol = new TableColumn<>("PC ID");
		pcStatusCol = new TableColumn<>("PC Status");

		tableView = new TableView<PC>(pcList);

		containerH = new HBox();
		containerV = new VBox();

		jobController = new JobController();
		pcController = new PCController();
		userController = new UserController();

		jobID.setPromptText("Select Job Id");
		userID.setPromptText("Select Technician Id");
		pcID.setPromptText("Select PC Id");
		pcStat.setPromptText("Select PC Status");

		for (Job j : jobList) {
			jobID.getItems().add(j.getJobID());
		}

		for (PC pc : pcList) {
			pcID.getItems().add(pc.getPCID());
		}

		pcStat.getItems().addAll("Usable", "Maintenance", "Broken");
		
		for (User user : userList) {
			userID.getItems().add(user.getUserID());
		}

		paddingBp = new Insets(10);

		scene = new Scene(bp, 1000, 500);
	}

	 /**
     * Metode layouting bertanggung jawab untuk menata elemen-elemen GUI pada tampilan.
     * Ini mencakup konfigurasi tabel, combo box, dan pengaturan tata letak.
     * Selain itu, metode ini menetapkan latar belakang dan membangun
     * tampilan akhir untuk ditampilkan di BorderPane.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		containerV.getChildren().addAll(title, tableView, containerH);
		containerH.getChildren().addAll(pcID, pcStat, userID, add, back);

		pcIDCol.setCellValueFactory(new PropertyValueFactory<>("PCID"));
		pcStatusCol.setCellValueFactory(new PropertyValueFactory<>("PCCondition"));

		tableView.getColumns().addAll(pcIDCol, pcStatusCol);

		for (TableColumn<PC, ?> column : tableView.getColumns()) {
			column.setMinWidth(70);
		}

		tableView.setPrefSize(700, 500);
		containerH.setAlignment(Pos.CENTER);
		containerH.setSpacing(10);
		containerV.setSpacing(10);
		
		ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
		VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10));
        mainContainer.getChildren().addAll(scrollPane, containerV);
        mainContainer.setSpacing(10);
		
		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);

        bp.setCenter(stackPane);
	}
	
	/**
     * Metode actions mengatur perilaku tombol dan elemen GUI lainnya
     * yang melibatkan interaksi pengguna. Ini mencakup logika tombol kembali
     * dan penanganan penambahan pekerjaan baru ke dalam sistem.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> {
			new ViewAllStaffJob(primaryStage);
		});

		add.setOnMouseClicked(e -> {
			int selectedUserID = userID.getValue();
			int selectedPcID = pcID.getValue();
			jobController.addNewJob(selectedUserID, selectedPcID);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("Job Assigned.");
			alert.showAndWait();
		});

		userID.setOnAction(e -> handleUserIDSelection());

		pcID.setOnAction(e -> handlePCIDSelection());
	}
	
	/**
     * Metode handleUserIDSelection mengatasi peristiwa pemilihan ID pengguna
     * pada combo box. Ini memastikan bahwa pengguna yang dipilih memiliki
     * peran "Computer Technician" sebelum menugaskan pekerjaan.
     */
	void handleUserIDSelection() {
		int selectedUserID = userID.getValue();
		try {
			if (!userController.getUserDataWithUserId(selectedUserID).getUserRole().equals("Computer Technician")) {
				showErrorMessage("Selected User is not a Computer Technician.");
				userID.getSelectionModel().clearSelection();
			}
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Metode handlePCIDSelection mengatasi peristiwa pemilihan ID PC pada combo box.
     * Ini memeriksa apakah ID PC yang dipilih ada dan apakah pekerjaan
     * sudah ditugaskan ke PC tersebut oleh teknisi komputer lainnya.
     */
	void handlePCIDSelection() {
		Integer selectedPCID = pcID.getValue();
		
		if(selectedPCID == null) return;
		
		if (!pcController.isPCIdExists(selectedPCID)) {
			showErrorMessage("Selected PC does not exist.");
			pcID.getSelectionModel().clearSelection();
		}

		if (jobController.getJobById(selectedPCID) != null) {
			showErrorMessage("Another Computer Technician is already working on this PC.");
			pcID.getSelectionModel().clearSelection();
		}
	}
	
	/**
     * Metode showErrorMessage menampilkan dialog kesalahan dengan pesan yang
     * diberikan kepada pengguna. Ini digunakan untuk memberi tahu pengguna
     * jika terjadi kesalahan selama proses penugasan pekerjaan.
     * @param message Pesan kesalahan yang akan ditampilkan.
     */
	void showErrorMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	/**
     * Metode createBackground membuat latar belakang tampilan menggunakan gambar yang
     * ditentukan. Ini menentukan properti latar belakang seperti pengulangan
     * gambar dan ukuran latar belakang.
     * @return Objek Background yang telah dibuat.
     */
	private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/admin-bg.png"));
	    BackgroundImage background = new BackgroundImage(
	            backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	            BackgroundPosition.DEFAULT, new BackgroundSize(1000, 500, false, false, false, true));
	    
	    return new Background(background);
	}
	
	/**
     * Metode createAdditionalBackgroundPane membuat lapisan tambahan untuk menampung
     * elemen-elemen tampilan seperti tabel dan tombol. Ini memberikan tampilan
     * yang dapat disesuaikan dengan ukuran maksimum dan menampilkannya di dalam StackPane.
     * @param content Node yang akan ditampilkan di dalam lapisan tambahan.
     * @return StackPane yang berisi elemen-elemen tambahan dan kontennya.
     */
	private StackPane createAdditionalBackgroundPane(Node content) {
	    StackPane additionalBackgroundPane = new StackPane();
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    
	    return additionalBackgroundPane;
	}
	
	/**
     * Konstruktor kelas AddStaffJob. Metode ini digunakan untuk membuat
     * objek AddStaffJob, menginisialisasi tampilan, menata elemen-elemen,
     * dan menetapkan aksi-aksi yang diperlukan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     */
	public AddStaffJob(Stage primaryStage) {
		initialize();
		layouting();
		primaryStage.show();
		primaryStage.setTitle("Add Staff Job");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
		actions(primaryStage);
	}

}

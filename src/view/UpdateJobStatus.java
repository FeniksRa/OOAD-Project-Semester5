package view;

import controller.JobController;
import controller.PCController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

/**
 * Kelas untuk menampilkan dan mengelola halaman pembaruan status pekerjaan.
 */
public class UpdateJobStatus {

	Scene scene;
	BorderPane bp;
	VBox containerV;
	HBox containerH;
	Button update, back;

	TableColumn<Job, Integer> jobIDCol;
	TableColumn<Job, Integer> pcIDCol;
	TableColumn<Job, Integer> userIDCol;
	TableColumn<Job, String> jobStatusCol;

	TableView<Job> tableView;

	Insets paddingBp;

	ObservableList<Job> jobList;
	ComboBox<Integer> jobID;
	ComboBox<String> jobStatus;

	JobController jobController;
	PCController pcController;

	/**
	 * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan data pekerjaan.
	 */
	void initialize() {
		bp = new BorderPane();
		jobList = FXCollections.observableArrayList(new JobController().getAllJobData());
		jobID = new ComboBox<>();
		jobStatus = new ComboBox<>();
		back = new Button("Back");
		update = new Button("Update Job Status");

		jobIDCol = new TableColumn<>("Job ID");
		pcIDCol = new TableColumn<>("PC ID");
		userIDCol = new TableColumn<>("User ID");
		jobStatusCol = new TableColumn<>("Job Status");

		tableView = new TableView<Job>(jobList);

		containerH = new HBox();
		containerV = new VBox();

		jobController = new JobController();
		pcController = new PCController();

		jobID.setPromptText("Select Job Id");
		jobStatus.setPromptText("Select Job Status");

		for (Job j : jobList) {
			jobID.getItems().add(j.getJobID());
			jobStatus.getItems().add(j.getJobStatus());
		}

		paddingBp = new Insets(10);

		scene = new Scene(bp, 1000, 500);
	}
	
	/**
	 * Metode untuk mengatur tata letak elemen-elemen UI pada laman pembaruan status pekerjaan.
	 */
	@SuppressWarnings("unchecked")
	void layouting() {
		containerV.getChildren().addAll(tableView, containerH);
		containerH.getChildren().addAll(jobID, jobStatus, update, back);

		jobIDCol.setCellValueFactory(new PropertyValueFactory<>("JobID"));
		pcIDCol.setCellValueFactory(new PropertyValueFactory<>("PcID"));
		userIDCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
		jobStatusCol.setCellValueFactory(new PropertyValueFactory<>("JobStatus"));

		tableView.getColumns().addAll(jobIDCol, pcIDCol, userIDCol, jobStatusCol);

		for (TableColumn<Job, ?> column : tableView.getColumns()) {
			column.setMinWidth(70);
		}
		
		ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10));
        mainContainer.getChildren().addAll(scrollPane, containerV);
        mainContainer.setSpacing(5);
		
		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);
        
		tableView.setPrefSize(700, 500);
		containerH.setAlignment(Pos.CENTER);
		containerH.setSpacing(10);
		containerV.setSpacing(10);
        bp.setCenter(stackPane);
	}

	/**
	 * Metode untuk menangani aksi tombol pada laman pembaruan status pekerjaan.
	 * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
	 */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> {
			new ViewAllStaffJob(primaryStage);
		});

		update.setOnMouseClicked(e -> {
			int selectedID = jobID.getValue();
			String selectedStatus = jobStatus.getValue();
			Job job = jobController.getJobById(selectedID);
			jobController.updateJobStatus(selectedID, selectedStatus);
			if (selectedStatus.equals("Complete")) {
				pcController.updatePCCondition(job.getPcID(), "Usable");
			} else if (selectedStatus.equals("UnComplete")) {
				pcController.updatePCCondition(job.getPcID(), "Maintenance");
			}

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("Job Status has been updated.");
			alert.showAndWait();
		});
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
	    
	    return additionalBackgroundPane;
	}

	/**
	 * Konstruktor untuk membuat objek UpdateJobStatus.
	 * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
	 */
	public UpdateJobStatus(Stage primaryStage) {
		initialize();
		layouting();
		primaryStage.show();
		primaryStage.setTitle("Update Job Status");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
		actions(primaryStage);
	}
}

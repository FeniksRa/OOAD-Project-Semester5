package view;

import controller.JobController;
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
 * Kelas HomeTechnician menangani tampilan untuk pengguna dengan peran Technician.
 */
public class HomeTechnician {

	Scene scene;
	BorderPane bp;
	VBox containerV;
	HBox containerH;
	Button submit, back;

	TableColumn<Job, Integer> jobIDCol;
	TableColumn<Job, Integer> pcIDCol;
	TableColumn<Job, String> jobStatusCol;

	TableView<Job> tableView;

	Insets paddingBp;

	ObservableList<Job> jobList;
	ComboBox<Integer> jobID;

	JobController jobController;

	/**
     * Inisialisasi elemen-elemen utama.
     * @param id ID pengguna saat ini.
     */
	void initialize(Integer id) {

		bp = new BorderPane();
		jobList = FXCollections.observableArrayList(new JobController().getAllJobData());
		jobID = new ComboBox<>();
		submit = new Button("Confirm");
		back = new Button("Back");

		jobIDCol = new TableColumn<>("Job ID");
		pcIDCol = new TableColumn<>("PC ID");
		jobStatusCol = new TableColumn<>("Job Status");

		tableView = new TableView<Job>(jobList);

		containerH = new HBox();
		containerV = new VBox();

		jobController = new JobController();

		jobID.setPromptText("Select Job Id");

		for (Job j : jobList) {
			jobID.getItems().add(j.getJobID());
		}

		paddingBp = new Insets(10);

		scene = new Scene(bp, 1000, 500);
	}

	/**
     * Menentukan tata letak tampilan HomeTechnician, termasuk menambahkan elemen-elemen
     * seperti tombol dan tabel pekerjaan ke dalam VBox dan HBox.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		containerV.getChildren().addAll(containerH);
		containerH.getChildren().addAll(jobID, submit, back);

		jobIDCol.setCellValueFactory(new PropertyValueFactory<>("JobID"));
		pcIDCol.setCellValueFactory(new PropertyValueFactory<>("PcID"));
		jobStatusCol.setCellValueFactory(new PropertyValueFactory<>("JobStatus"));

		tableView.getColumns().addAll(jobIDCol, pcIDCol, jobStatusCol);

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

		containerH.setAlignment(Pos.CENTER);
		containerH.setSpacing(10);
		containerV.setSpacing(10);
		tableView.setPrefSize(700, 500);
		
        bp.setCenter(stackPane);
	}

	/**
     * Menangani aksi-aksi yang terkait dengan tombol-tombol di tampilan HomeTechnician.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> {
			try {
				new Login().loginIn(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		submit.setOnMouseClicked(e -> {
			completeJob();
		});
	}

	/**
     * Menyelesaikan pekerjaan yang dipilih dan memperbarui tampilan.
     */
	private void completeJob() {
		Integer selectedJobID = jobID.getValue();

		if (selectedJobID != null) {
			// Check if the selected job has status 'UnComplete'
			Job selectedJob = jobController.getJobById(selectedJobID);

			if (selectedJob != null && selectedJob.getJobStatus().equals("UnComplete")) {
				// Code to complete the job
				boolean success = jobController.updateJobStatus(selectedJobID, "Complete");

				if (success) {
					showAlert("Job ID " + selectedJobID + " completed successfully.");
					// Refresh the job list
					jobList.setAll(jobController.getAllJobData());
					tableView.refresh();
				} else {
					showAlert("Failed to complete Job ID " + selectedJobID + ".");
				}
			} else {
				showAlert("Selected job is not in 'UnComplete' status.");
			}
		} else {
			showAlert("Please select a Job ID to complete.");
		}
	}

	/**
     * Menampilkan pemberitahuan (Alert) dengan pesan yang diberikan.
     * @param message Pesan yang ingin ditampilkan dalam pemberitahuan.
     */
	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	/**
     * Mengatur gambar latar belakang tampilan.
     */
	private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/ct-bg.png"));
	    BackgroundImage background = new BackgroundImage(
	            backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	            BackgroundPosition.DEFAULT, new BackgroundSize(1000, 500, false, false, false, true));
	    
	    return new Background(background);
	}
	
	
	private StackPane createAdditionalBackgroundPane(Node content) {
	    StackPane additionalBackgroundPane = new StackPane();
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    
	    return additionalBackgroundPane;
	}

	/**
     * Konstruktor untuk menginisialisasi, menentukan tata letak, dan menangani aksi
     * dalam tampilan HomeTechnician.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param id ID pengguna saat ini.
     */
	public HomeTechnician(Stage primaryStage, Integer id) {
		initialize(id);
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("View Technician Job");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

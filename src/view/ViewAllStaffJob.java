package view;

import controller.JobController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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

/**
 * Kelas untuk menampilkan daftar pekerjaan semua staf teknisi komputer.
 */
public class ViewAllStaffJob {

	Scene scene;
	BorderPane bp;
	VBox containerV;
	HBox containerH;
	Label title;
	Button addStaffJob, updateJobStatus, back;

	TableColumn<Job, Integer> jobIDCol;
	TableColumn<Job, Integer> pcIDCol;
	TableColumn<Job, Integer> userIDCol;
	TableColumn<Job, String> jobStatusCol;

	TableView<Job> tableView;

	Insets paddingBp;

	ObservableList<Job> jobList;
	ComboBox<Integer> jobID;

	JobController jobController;

	/**
     * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan mendapatkan data pekerjaan staf.
     */
	void initialize() {
		bp = new BorderPane();
		jobList = FXCollections.observableArrayList(new JobController().getAllJobData());
		jobID = new ComboBox<>();
		title = new Label("Computer Technician's Job");
		back = new Button("Back");
		addStaffJob = new Button("Add Staff Job");
		updateJobStatus = new Button("Update Job Status");

		jobIDCol = new TableColumn<>("Job ID");
		pcIDCol = new TableColumn<>("PC ID");
		userIDCol = new TableColumn<>("User ID");
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
     * Metode untuk mengatur tata letak elemen-elemen UI pada laman tampilan pekerjaan staf.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		containerV.getChildren().addAll(containerH);
		containerH.getChildren().addAll(jobID, addStaffJob, updateJobStatus, back);

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
     * Metode untuk menangani aksi tombol pada laman tampilan pekerjaan staf.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> {
			new HomeAdmin(primaryStage);
		});

		addStaffJob.setOnMouseClicked(e -> {
			new AddStaffJob(primaryStage);
		});

		updateJobStatus.setOnMouseClicked(e -> {
			new UpdateJobStatus(primaryStage);
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
     * Konstruktor untuk membuat objek ViewAllStaffJob.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	public ViewAllStaffJob(Stage primaryStage) {
		initialize();
		layouting();
		primaryStage.show();
		primaryStage.setTitle("View All Staff Job");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
		actions(primaryStage);
	}

}

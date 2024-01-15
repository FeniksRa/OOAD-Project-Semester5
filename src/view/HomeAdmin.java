package view;

import controller.PCController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Kelas HomeAdmin menangani tampilan Home untuk pengguna dengan peran Admin.
 */
public class HomeAdmin {

	Scene scene;
	GridPane gp;
	FlowPane fp;
	BorderPane bp;
	Label label;
	Button addPc, updatePc, deletePc, viewAllStaff, viewAllStaffJob, viewAllReport, viewAllTransactionHistory, back;
	VBox container1, container2, container3, mergeContainer;

	Insets marginContainer, paddingContainer;

	void initialize() {
		fp = new FlowPane();
		bp = new BorderPane();
		addPc = new Button("Add PC");
		updatePc = new Button("Update PC");
		deletePc = new Button("Delete PC");
		viewAllStaff = new Button("View All Staff");
		viewAllStaffJob = new Button("View All Staff Job");
		viewAllReport = new Button("View All Report");
		viewAllTransactionHistory = new Button("View All Transaction History");
		back = new Button("Back");

		container1 = new VBox();
		container2 = new VBox();
		container3 = new VBox();
		mergeContainer = new VBox(10);

		marginContainer = new Insets(0, 20, 0, 0);
		paddingContainer = new Insets(10, 10, 10, 10);

		scene = new Scene(bp, 1000, 500);
	}

	/**
     * Menentukan tata letak tampilan HomeAdmin, termasuk menambahkan elemen-elemen
     * seperti tombol dan label ke dalam VBox dan FlowPane.
     */
	void layouting() {
		container1.getChildren().addAll(addPc, updatePc, deletePc);
		container2.getChildren().addAll(viewAllStaff, viewAllStaffJob);
		container3.getChildren().addAll(viewAllReport, viewAllTransactionHistory);
		fp.getChildren().addAll(container1, container2, container3);
		mergeContainer.getChildren().addAll(fp, back);

		FlowPane.setMargin(container1, marginContainer);
		FlowPane.setMargin(container2, marginContainer);
		container1.setPadding(paddingContainer);
		container2.setPadding(paddingContainer);
		container3.setPadding(paddingContainer);

		container1.setStyle("-fx-border-color: grey; -fx-border-width: 2px; -fx-border-radius: 10px");
		container2.setStyle("-fx-border-color: grey; -fx-border-width: 2px; -fx-border-radius: 10px");
		container3.setStyle("-fx-border-color: grey; -fx-border-width: 2px; -fx-border-radius: 10px");

		container1.setSpacing(20);
		container2.setSpacing(20);
		container3.setSpacing(20);
		
		

		setBackground();
		back.setAlignment(Pos.CENTER);
		fp.setAlignment(Pos.CENTER);
		mergeContainer.setAlignment(Pos.CENTER);
		bp.setCenter(mergeContainer);
	}

	/**
     * Menangani aksi-aksi yang terkait dengan tombol-tombol di tampilan HomeAdmin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     */
	void actions(Stage primaryStage) {
		addPc.setOnMouseClicked(e -> {
//			new AddPC(primaryStage);
			boolean check = false;
			check = new PCController().addNewPC();

			if (check == true) {
				new AddPC(primaryStage).alert(check, "Add PC Success", primaryStage);
			} else {
				new AddPC(primaryStage).alert(check, "Add PC Failed", primaryStage);
			}
		});
		updatePc.setOnMouseClicked(e -> {
			new UpdatePC(primaryStage);
		});
		deletePc.setOnMouseClicked(e -> {
			new DeletePC(primaryStage);
		});

		viewAllStaff.setOnMouseClicked(e -> {
			new ViewAllStaff(primaryStage);
		});
		viewAllStaffJob.setOnMouseClicked(e -> {
			new ViewAllStaffJob(primaryStage);
		});

		viewAllTransactionHistory.setOnMouseClicked(e -> {
			new TransactionHeaderAdmin(primaryStage);
		});
		viewAllReport.setOnMouseClicked(e -> {
			new ViewAllReport(primaryStage);
		});

		back.setOnAction(e -> {
			try {
				new Login().loginIn(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	/**
     * Menetapkan latar belakang berupa gambar pada BorderPane.
     */
	void setBackground() {
		Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/admin-bg.png"));

		BackgroundImage background = new BackgroundImage(
                backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(1000, 500, false, false, false, true));

		bp.setBackground(new Background(background));
	}

	 /**
     * Konstruktor untuk menginisialisasi, menentukan tata letak, dan menangani aksi
     * dalam tampilan HomeAdmin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     */
	public HomeAdmin(Stage primaryStage) {
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

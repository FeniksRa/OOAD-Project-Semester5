package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Kelas HomeOperator menangani tampilan Home untuk pengguna dengan peran Operator.
 */
public class HomeOperator {

	Scene scene;
	BorderPane bp;
	FlowPane flowPane;
	Button assignUsertoAnotherPC, finishBook, cancelBook, makeReport, back;
	VBox container1, container2, mergeContainer;
	Insets marginContainer, paddingContainer;

	/**
     * Inisialisasi elemen-elemen utama dan membuat objek Scene.
     */
	void initialize() {
		bp = new BorderPane();
		flowPane = new FlowPane();
		assignUsertoAnotherPC = new Button("Assign user to Another pc");
		finishBook = new Button("Finish Book");
		cancelBook = new Button("Cancel Book");
		back = new Button("Back");
		makeReport = new Button("Make Report");
		
		container1 = new VBox();
		container2 = new VBox();
		mergeContainer = new VBox(10);
		
		marginContainer = new Insets(0, 20, 0, 0);
		paddingContainer = new Insets(10, 10, 10, 10);

		scene = new Scene(bp, 1000, 500);
	}

	/**
     * Menentukan tata letak tampilan HomeOperator, termasuk menambahkan elemen-elemen
     * seperti tombol dan label ke dalam VBox dan FlowPane.
     */
	void layouting() {
		container1.getChildren().addAll(assignUsertoAnotherPC, makeReport);
		container2.getChildren().addAll(finishBook, cancelBook);
		
		flowPane.getChildren().addAll(container1, container2);
		mergeContainer.getChildren().addAll(flowPane, back);
		
		FlowPane.setMargin(container1, marginContainer);
		FlowPane.setMargin(container2, marginContainer);
		container1.setPadding(paddingContainer);
		container2.setPadding(paddingContainer);
		
		container1.setStyle("-fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 10px;");
		container2.setStyle("-fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 10px;");

		
		container1.setSpacing(20);
		container2.setSpacing(20);
		
		setBackground();
		back.setAlignment(Pos.CENTER);
		flowPane.setAlignment(Pos.CENTER);
		mergeContainer.setAlignment(Pos.CENTER);
		
		bp.setCenter(mergeContainer);
	}

	/**
     * Menangani aksi-aksi yang terkait dengan tombol-tombol di tampilan HomeOperator.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
	void actions(Stage primaryStage, int userId) {
		back.setOnAction(e-> back(primaryStage));
		assignUsertoAnotherPC.setOnAction(e-> assignUsertoAnotherPc(primaryStage, userId));
		finishBook.setOnAction(e -> finishBook(primaryStage, userId));
		cancelBook.setOnAction(e -> cancelBook(primaryStage, userId));
		
		makeReport.setOnAction(e-> makeReport(primaryStage, userId));
	}

	/**
     * Menampilkan tampilan pembuatan laporan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
	void makeReport(Stage primaryStage, int userId) {
		new MakeReport(primaryStage, "Operator", userId);
	}

	/**
     * Kembali ke tampilan login.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     */
	void back(Stage primaryStage) {
		try {
			new Login().loginIn(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**
     * Menampilkan tampilan penugasan pengguna ke PC lain.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
	void assignUsertoAnotherPc(Stage primaryStage, int userId) {
		new AssignUserToAnotherPC(primaryStage, userId);
	}
	
	 /**
     * Menampilkan tampilan menyelesaikan pemesanan PC.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
	void finishBook(Stage primaryStage, int userId) {
		new FinishBook(primaryStage, userId);
	}
	
	 /**
     * Menampilkan tampilan pembatalan pemesanan PC.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
	void cancelBook(Stage primaryStage, int userId) {
		new CancelBook(primaryStage, userId);
	}
	
	/**
     * Mengatur gambar latar belakang tampilan.
     */
	void setBackground() {
		Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/operator-bg.png"));

		BackgroundImage background = new BackgroundImage(
                backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(1000, 500, false, false, false, true));

		bp.setBackground(new Background(background));
	}
	
	/**
     * Konstruktor untuk menginisialisasi, menentukan tata letak, dan menangani aksi
     * dalam tampilan HomeOperator.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
	public HomeOperator(Stage primaryStage, int userId) {
		initialize();
		layouting();
		actions(primaryStage, userId);
		primaryStage.show();
		primaryStage.setTitle("Welcome Operator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

package view;

import java.util.List;

import controller.PCController;
import controller.PcBookController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
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
import model.PCBook;

/**
 * Kelas AssignUserToAnotherPC bertanggung jawab untuk menangani antarmuka pengguna
 * dalam proses menugaskan pengguna ke PC lain. Kelas ini mencakup tampilan GUI,
 * logika pemrosesan data, dan tindakan yang terkait dengan penugasan pengguna ke PC baru.
 */
public class AssignUserToAnotherPC {
	Scene scene;
	Button backButton, assignButton, backButton1;
	BorderPane borderPane;
	FlowPane flowPane;
	Insets marginContainer, paddingContainer;
	VBox container1;
	HBox buttonBox, buttonBox1;

	TableView<PCBook> bookingSchedule;

	TableColumn<PCBook, Integer> bookIdCol;
	TableColumn<PCBook, String> bookDateCol;
	TableColumn<PCBook, Integer> pcIdCol;
	TableColumn<PCBook, Integer> userIdCol;

	ObservableList<PCBook> bookList;
	ObservableList<PC> pcList;
	ComboBox<Integer> pcId;
	ComboBox<Integer> bookId;
	
	/**
     * Metode initialize digunakan untuk menginisialisasi komponen-komponen GUI
     * dan pengaturan awal yang diperlukan untuk tampilan penugasan pengguna ke PC baru.
     * Inisialisasi termasuk pembuatan objek-objek, alokasi memori untuk beberapa koleksi data,
     * dan pengaturan prompt text pada combo box.
     */
	void initialize() {
		backButton = new Button("Back");
		backButton1 = new Button("Back");
		assignButton = new Button("Assign to Another Pc");
		bookList = FXCollections.observableArrayList(new PcBookController().getAllPCBookedData());
		pcList = FXCollections.observableArrayList(new PCController().getAllPCData());
		borderPane = new BorderPane();
		flowPane = new FlowPane();
		container1 = new VBox();
		buttonBox = new HBox();
		buttonBox1 = new HBox();
		bookId = new ComboBox<>();
		pcId = new ComboBox<>();

		bookIdCol = new TableColumn<PCBook, Integer>("Book Id");
		bookDateCol = new TableColumn<PCBook, String>("Booked Date");
		pcIdCol = new TableColumn<PCBook, Integer>("Pc Id");
		userIdCol = new TableColumn<PCBook, Integer>("User Id");

		bookingSchedule = new TableView<PCBook>(bookList);

		bookId.setPromptText("Select Book Id");
		for (PCBook pcBook : bookList) {
			bookId.getItems().add(pcBook.getBookID());
		}
		pcId.setPromptText("Select new pc id");
		for (PC pc : pcList) {
			pcId.getItems().add(pc.getPCID());
		}

		marginContainer = new Insets(0, 0, 20, 0);
		scene = new Scene(borderPane, 1000, 500);
	}

	/**
     * Metode layouting bertanggung jawab untuk menata elemen-elemen GUI pada tampilan.
     * Ini mencakup konfigurasi tabel, combo box, dan pengaturan tata letak.
     * Selain itu, metode ini menetapkan latar belakang dan membangun tampilan akhir untuk ditampilkan di BorderPane.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		buttonBox.getChildren().addAll(bookId, backButton);
		buttonBox1.getChildren().addAll(pcId, assignButton, backButton1);
		container1.getChildren().addAll(buttonBox, buttonBox1);

		buttonBox.setSpacing(10);
		buttonBox1.setSpacing(10);
		container1.setSpacing(5);
		
		bookIdCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));
		bookDateCol.setCellValueFactory(new PropertyValueFactory<>("BookedDate"));
		pcIdCol.setCellValueFactory(new PropertyValueFactory<>("PCID"));
		userIdCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
		bookingSchedule.getColumns().addAll(bookIdCol, bookDateCol, pcIdCol, userIdCol);

		for (TableColumn<PCBook, ?> column : bookingSchedule.getColumns()) {
			column.setMinWidth(70);
		}
		
		ScrollPane scrollPane = new ScrollPane(bookingSchedule);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10));
        mainContainer.getChildren().addAll(scrollPane, container1);
        mainContainer.setSpacing(5);
        
		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);

		buttonBox1.setVisible(false);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox1.setAlignment(Pos.CENTER);
		borderPane.setCenter(stackPane);
	}

	/**
     * Metode action mengatur perilaku tombol dan elemen GUI lainnya yang melibatkan interaksi pengguna.
     * Ini mencakup logika tombol kembali, penugasan PC baru, dan pemilihan buku PC untuk dipindahkan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	void action(Stage primaStage, int userId) {
		backButton.setOnAction(e -> goBack(userId));
		backButton1.setOnAction(e -> goBack(userId));
		bookId.setOnAction(e -> {
			buttonBox1.setVisible(true);
			buttonBox.setVisible(false);
		});
		assignButton.setOnAction(e -> assignUserToAnotherPc(primaStage, userId));
	}

	/**
     * Metode assignUserToAnotherPc bertanggung jawab untuk mengeksekusi logika
     * penugasan pengguna ke PC baru. Ini memeriksa ketersediaan PC baru, apakah PC
     * sudah dipesan pada tanggal yang sama, dan mengatur penugasan pengguna ke PC baru.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	private void assignUserToAnotherPc(Stage primaStage, int userId) {
		int newPcId = pcId.getValue();
		int bookPcId = bookId.getValue();

		PCBook bookedPc = new PcBookController().getPCBookedDetail(bookPcId);

		PC checkPc = new PCController().getPCDetail(newPcId);
		if (checkPc == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Pc is not exist.");
			alert.show();
			return;
		} else if (checkPc.getPCCondition().equals("Maintenance") || checkPc.getPCCondition().equals("Broken")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Pc is not available.");
			alert.show();
			return;
		}

		// Check PC is booked or not booked

		List<PCBook> listBookedData = new PcBookController().getPCBookedData(newPcId, bookedPc.getBookedDate());

		if (!listBookedData.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Pc already booked.");
			alert.show();
			return;
		}

		// Assign User to Another Pc

		boolean checkAssign = new PcBookController().assignUserToNewPC(bookPcId, newPcId);

		if (checkAssign == true) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("Successfully assign user to another pc.");
			alert.show();
			new HomeOperator(primaStage, userId);
		}
	}

	/**
     * Metode goBack menangani aksi untuk kembali ke halaman utama.
     * Ini digunakan ketika tombol kembali ditekan.
     * @param userId ID pengguna yang sedang aktif.
     */
	private void goBack(int userId) {
		// Handle the action to go back to the Home Page
		Stage primaryStage = (Stage) scene.getWindow();
		HomeOperator homeOperator = new HomeOperator(primaryStage, userId);
		primaryStage.setTitle("Home Page");
	}
	
	/**
     * Metode createBackground membuat latar belakang tampilan menggunakan gambar yang
     * ditentukan. Ini menentukan properti latar belakang seperti pengulangan
     * gambar dan ukuran latar belakang.
     * @return Objek Background yang telah dibuat.
     */
	private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/operator-bg.png"));
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
     * Konstruktor kelas AssignUserToAnotherPC. Metode ini digunakan untuk membuat
     * objek AssignUserToAnotherPC, menginisialisasi tampilan, menata elemen-elemen,
     * dan menetapkan aksi-aksi yang diperlukan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId       ID pengguna yang sedang aktif.
     */
	public AssignUserToAnotherPC(Stage primaryStage, Integer userId) {
		initialize();
		layouting();
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Home Page");
		action(primaryStage, userId);
	}
}

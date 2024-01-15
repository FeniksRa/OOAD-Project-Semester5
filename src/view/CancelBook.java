package view;

import java.time.LocalDate;

import controller.PcBookController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PCBook;
import model.TransactionHeader;

/**
 * Kelas CancelBook bertanggung jawab untuk menangani antarmuka pengguna
 * dalam proses pembatalan pemesanan PC. Kelas ini mencakup tampilan GUI, logika pemrosesan data,
 * dan tindakan yang terkait dengan pembatalan pemesanan PC oleh pengguna.
 */
public class CancelBook {

	Scene scene;
	Button backButton, cancelBookButton, backButton1;
	BorderPane borderPane;
	FlowPane flowPane;
	Insets marginContainer, paddingContainer;
	VBox container1;
	HBox buttonBox, dateBox;
	DatePicker bookedDatePicker;

	TableView<PCBook> bookingSchedule;

	TableColumn<PCBook, Integer> bookIdCol;
	TableColumn<PCBook, String> bookDateCol;
	TableColumn<PCBook, Integer> pcIdCol;
	TableColumn<PCBook, Integer> userIdCol;

	ObservableList<PCBook> bookList;
	ComboBox<Integer> bookId;

	/**
     * Metode initialize digunakan untuk menginisialisasi komponen-komponen GUI
     * dan pengaturan awal yang diperlukan untuk tampilan pembatalan pemesanan PC.
     * Inisialisasi termasuk pembuatan objek-objek, alokasi memori untuk beberapa koleksi data,
     * dan pengaturan prompt text pada combo box dan date picker.
     */
	void initialize() {

		bookList = FXCollections.observableArrayList(new PcBookController().getPcBookedByDate(null));

		backButton = new Button("Back");
		backButton1 = new Button("Back");
		borderPane = new BorderPane();
		flowPane = new FlowPane();
		container1 = new VBox();
		buttonBox = new HBox();
		bookId = new ComboBox<>();
		cancelBookButton = new Button("Cancel Book");
		bookedDatePicker = new DatePicker();
		bookedDatePicker.setPromptText("Select Booked Date");
		dateBox = new HBox();

		bookingSchedule = new TableView<PCBook>();

		bookIdCol = new TableColumn<PCBook, Integer>("Book Id");
		bookDateCol = new TableColumn<PCBook, String>("Booked Date");
		pcIdCol = new TableColumn<PCBook, Integer>("Pc Id");
		userIdCol = new TableColumn<PCBook, Integer>("User Id");

		marginContainer = new Insets(0, 0, 20, 0);
		scene = new Scene(borderPane, 1000, 500);
	}

	/**
     * Metode layouting bertanggung jawab untuk menata elemen-elemen GUI pada tampilan.
     * Ini mencakup konfigurasi tabel, combo box, date picker, dan pengaturan tata letak.
     * Selain itu, metode ini menetapkan latar belakang dan membangun tampilan akhir untuk ditampilkan di BorderPane.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		buttonBox.getChildren().addAll(bookId, cancelBookButton, backButton);
		dateBox.getChildren().addAll(bookedDatePicker, backButton1);
		container1.getChildren().addAll(dateBox, buttonBox);

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

        buttonBox.setVisible(false);
		container1.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		dateBox.setAlignment(Pos.CENTER);
		dateBox.setSpacing(10);
		container1.setAlignment(Pos.CENTER);

		borderPane.setCenter(stackPane);
	}

	/**
     * Metode action mengatur perilaku tombol dan elemen GUI lainnya yang melibatkan interaksi pengguna.
     * Ini mencakup logika tombol kembali, tombol pembatalan pemesanan, dan pemilihan tanggal untuk menampilkan pemesanan.
     * @param primaStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	void action(Stage primaStage, int userId) {
		backButton.setOnAction(e -> goBack(userId));
		backButton1.setOnAction(e -> goBack(userId));
		bookedDatePicker.setOnAction(e -> {
			dateBox.setVisible(false);
			buttonBox.setVisible(true);
			backButton.setVisible(true);
			bookList = FXCollections.observableArrayList(
					new PcBookController().getPcBookedByDate(bookedDatePicker.getValue().toString()));
			bookingSchedule.setItems(bookList);
			bookId.setPromptText("Select Book Id");
			for (PCBook pcBook : bookList) {
				bookId.getItems().add(pcBook.getBookID());
			}
			bookingSchedule.refresh();
		});
		cancelBookButton.setOnAction(e -> cancelBook(primaStage, userId));
	}

	/**
     * Metode cancelBook memproses pembatalan pemesanan PC berdasarkan pilihan yang dibuat oleh pengguna.
     * Ini memeriksa apakah pembatalan dapat dilakukan berdasarkan ID dan tanggal pemesanan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	private void cancelBook(Stage primaryStage, int userId) {
		Integer id = bookId.getValue();
		LocalDate dateNow = LocalDate.now();

		if (id == null)
			return;
		if (bookedDatePicker.getValue().isBefore(dateNow)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("the booked date has passed");
			alert.show();
			new CancelBook(primaryStage, userId);
			return;
		}
		Boolean check = new PcBookController().deleteBookData(id);
		if (check == true) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("Successfully canceled the pc order");
			alert.show();
			new HomeOperator(primaryStage, userId);
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
//        HomeCustomer homePage = new HomeCustomer(primaryStage, userId);
		HomeOperator homeOperator = new HomeOperator(primaryStage, userId);
//        primaryStage.setScene(homePage.getScene());
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
     * Konstruktor kelas CancelBook. Metode ini digunakan untuk membuat
     * objek CancelBook, menginisialisasi tampilan, menata elemen-elemen,
     * dan menetapkan aksi-aksi yang diperlukan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	public CancelBook(Stage primaryStage, Integer userId) {
		// TODO Auto-generated constructor stub
		initialize();
		layouting();
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Home Page");
		action(primaryStage, userId);
	}

}

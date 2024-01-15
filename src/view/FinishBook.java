package view;


import java.time.LocalDate;
import java.util.List;

import controller.PCController;
import controller.PcBookController;
import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import model.PCBook;

/**
 * Kelas FinishBook bertanggung jawab untuk menangani antarmuka pengguna
 * dalam proses penyelesaian pemesanan PC. Kelas ini mencakup tampilan GUI,
 * logika pemrosesan data, dan tindakan yang terkait dengan penyelesaian pemesanan PC oleh operator.
 */
public class FinishBook {
	
	Scene scene;
	Button backButton, finishBookButton;
	BorderPane borderPane;
	FlowPane flowPane;
	Insets marginContainer, paddingContainer;
	VBox container1;
	HBox buttonBox;
	DatePicker datePicker;
	
	ObservableList<PCBook> listPcShow;
	
	TableView<PCBook> bookingSchedule;

	TableColumn<PCBook, Integer> bookIdCol;
	TableColumn<PCBook, String> bookDateCol;
	TableColumn<PCBook, Integer> pcIdCol;
	TableColumn<PCBook, Integer> userIdCol;
	
	/**
     * Metode initialize digunakan untuk menginisialisasi komponen-komponen GUI
     * dan pengaturan awal yang diperlukan untuk tampilan penyelesaian pemesanan PC.
     * Inisialisasi termasuk pembuatan objek-objek, alokasi memori untuk beberapa koleksi data,
     * dan penyesuaian prompt text pada date picker.
     */
	void initialize() {
		
		finishBookButton = new Button("Finish Book");
		backButton = new Button("Back");
		borderPane = new BorderPane();
		flowPane = new FlowPane();
		container1 = new VBox();
		buttonBox  = new HBox();
		datePicker = new DatePicker();
		listPcShow = FXCollections.observableArrayList(new PcBookController().getAllPCBookedData());
		
		bookIdCol = new TableColumn<PCBook, Integer>("Book Id");
		bookDateCol = new TableColumn<PCBook, String>("Booked Date");
		pcIdCol = new TableColumn<PCBook, Integer>("Pc Id");
		userIdCol = new TableColumn<PCBook, Integer>("User Id");
		
		bookingSchedule = new TableView<PCBook>(listPcShow);
		
		marginContainer = new Insets(0, 0, 20, 0);
		scene = new Scene(borderPane, 1000, 500);
	}
	
	/**
     * Metode layouting bertanggung jawab untuk menata elemen-elemen GUI pada tampilan.
     * Ini mencakup konfigurasi tombol, date picker, tabel, dan tata letak keseluruhan.
     * Selain itu, metode ini menetapkan latar belakang dan membangun tampilan akhir untuk ditampilkan di BorderPane.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		
		buttonBox.getChildren().addAll(datePicker, finishBookButton, backButton);
		container1.getChildren().addAll(buttonBox);
		
        container1.setSpacing(10);
        buttonBox.setSpacing(10);
        
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("BookID"));
		bookDateCol.setCellValueFactory(new PropertyValueFactory<>("BookedDate"));
		pcIdCol.setCellValueFactory(new PropertyValueFactory<>("PCID"));
		userIdCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
		bookingSchedule.getColumns().addAll(bookIdCol, bookDateCol, pcIdCol, userIdCol);
		
		bookDateCol.setSortType(TableColumn.SortType.ASCENDING);
        bookingSchedule.getSortOrder().add(bookDateCol);

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
		
		buttonBox.setAlignment(Pos.CENTER);
		borderPane.setCenter(stackPane);
		
	}
	
	/**
     * Metode action menetapkan perilaku tombol pada tampilan.
     * Ini mencakup aksi tombol "Back" dan "Finish Book" yang akan dijalankan
     * ketika tombol tersebut ditekan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang menggunakan aplikasi.
     */
	void action(Stage primaStage, int userId){
		backButton.setOnAction(e -> goBack(userId));
		finishBookButton.setOnAction(e-> finishBook(primaStage, userId));
	}
	
	/**
     * Metode finishBook menangani penyelesaian pemesanan PC berdasarkan
     * tanggal yang dipilih oleh operator. Ini memvalidasi input, menyelesaikan
     * pemesanan, menghapus data pemesanan dari database, menambahkan transaksi,
     * dan kembali ke tampilan utama operator.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId       ID pengguna yang sedang menggunakan aplikasi.
     */
	private void finishBook(Stage primaStage, int userId) {
		String datePicked = datePicker.getValue().toString();
		
		if(datePicked.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Please pick a date.");
			alert.show();
		}
		
		// Get ListPcBook yang akan di finish berdasarkan date yang dipilih
		List<PCBook> listFinishBook = new PcBookController().getPcBookedByDate(datePicked);
		
		// Validasi listPcBook nya terdapat isi atau empty
		if(listFinishBook.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("There is no book data on chosen date.");
			alert.show();
			return;
		}
		
		// Validasi apakah datepicked telah melewati hari ini atau tidak
		if(!datePicker.getValue().isBefore(LocalDate.now())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Chosen date has not passed today's date.");
			alert.show();
			return;
		}
		
		// Finish Book and Delete
		new PcBookController().finishBook(listFinishBook); 
		for (PCBook pcBook : listFinishBook) {
			new PcBookController().deleteBookData(pcBook.getBookID());
		}
		
		// Add Transaction
		new TransactionController().addTransaction(listFinishBook, userId);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("Succes finish book.");
		alert.show();
		
		// direct balik ke HomeOperator
		new HomeOperator(primaStage, userId);
	}

	/**
     * Metode goBack menangani aksi kembali ke tampilan utama operator.
     * Ini digunakan ketika tombol "Back" ditekan.
     * @param userId ID pengguna yang sedang menggunakan aplikasi.
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
     * Konstruktor kelas FinishBook. Metode ini digunakan untuk membuat objek FinishBook,
     * menginisialisasi tampilan, menata elemen-elemen, dan menetapkan aksi-aksi yang diperlukan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang menggunakan aplikasi.
     */
	public FinishBook(Stage primaryStage, Integer userId) {
		initialize();
		layouting();
		primaryStage.setScene(scene);
        primaryStage.show();
		primaryStage.setTitle("Home Page");
		action(primaryStage, userId);
	}

}

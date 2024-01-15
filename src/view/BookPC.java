package view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controller.PCController;
import controller.PcBookController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PC;

/**
 * Kelas BookPC bertanggung jawab untuk menangani antarmuka pengguna
 * dalam proses pemesanan PC. Kelas ini mencakup tampilan GUI, logika pemrosesan data,
 * dan tindakan yang terkait dengan pemesanan PC oleh pengguna.
 */
public class BookPC {

	Scene scene;
	GridPane gridPane;
	FlowPane flowPane;
	BorderPane borderPane;
	Label pcStatus1, pcStatus2, pcStatus3, pcStatus4, PcID;
	ImageView image1, image2, image3, image4;
	VBox container1, container2, container3, mergeContainer;
	Insets marginContainer, paddingContainer;
	Button backButton, sendButton;
	ComboBox<Integer> pcComboBox;
	DatePicker bookedDate;
	List<PC> listPCId;

	/**
     * Metode initialize digunakan untuk menginisialisasi komponen-komponen GUI
     * dan pengaturan awal yang diperlukan untuk tampilan pemesanan PC.
     * Inisialisasi termasuk pembuatan objek-objek, alokasi memori untuk beberapa koleksi data,
     * dan pengaturan prompt text pada combo box.
     */
	void initialize() {
		flowPane = new FlowPane();
		borderPane = new BorderPane();
		backButton = new Button("Back");
		sendButton = new Button("Send");
		pcComboBox = new ComboBox<>();
		bookedDate = new DatePicker();
		listPCId = new PCController().getAllPCData();

		container1 = new VBox();
		container2 = new VBox();
		container3 = new VBox();
		mergeContainer = new VBox();

		marginContainer = new Insets(0, 20, 0, 0);

		scene = new Scene(borderPane, 1000, 500);

		pcComboBox.setPromptText("Select PC");
		for (PC pc : listPCId) {
			pcComboBox.getItems().addAll(pc.getPCID());
		}
	}

	/**
     * Metode layouting bertanggung jawab untuk menata elemen-elemen GUI pada tampilan.
     * Ini mencakup konfigurasi gambar, combo box, dan pengaturan tata letak.
     * Selain itu, metode ini menetapkan latar belakang dan membangun tampilan akhir untuk ditampilkan di BorderPane.
     */
	void layouting() {
		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(pcComboBox, bookedDate, sendButton, backButton);

		FlowPane pcStatusContainer = new FlowPane(20, 10);
        
        for (PC pc : listPCId) {
			pcStatusContainer.getChildren()
					.addAll(createIconLabelPair(image1, ((Integer) pc.getPCID()).toString(), pc.getPCCondition()));
		}

		pcStatusContainer.setAlignment(Pos.CENTER);
		buttonBox.setAlignment(Pos.CENTER);
		
		ScrollPane scrollPane = new ScrollPane(pcStatusContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");

		VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10));
        mainContainer.getChildren().addAll(scrollPane, buttonBox);
        mainContainer.setSpacing(10);
        
        StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);

        borderPane.setCenter(stackPane);
	}

	/**
     * Metode action mengatur perilaku tombol dan elemen GUI lainnya yang melibatkan interaksi pengguna.
     * Ini mencakup logika tombol kembali dan tombol kirim untuk melakukan pemesanan PC.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	void action(Stage primaryStage, int userId) {
		backButton.setOnAction(e -> goBack(userId));
		sendButton.setOnAction(e -> sendBookPc(primaryStage, userId));
	}

	/**
     * Metode goBack menangani aksi untuk kembali ke halaman utama.
     * Ini digunakan ketika tombol kembali ditekan.
     *
     * @param userId ID pengguna yang sedang aktif.
     */
	private void goBack(int userId) {
		Stage primaryStage = (Stage) scene.getWindow();
		HomeCustomer homePage = new HomeCustomer(primaryStage, userId);
		primaryStage.setTitle("Home Page");
	}

	/**
     * Metode alert menampilkan jendela peringatan dengan pesan yang sesuai
     * berdasarkan parameter yang diberikan.
     *
     * @param check      Nilai boolean yang menunjukkan keberhasilan atau kegagalan suatu operasi.
     * @param msg        Pesan yang akan ditampilkan dalam jendela peringatan.
     * @param primaStage Objek Stage yang digunakan untuk menampilkan jendela peringatan.
     */
	public void alert(boolean check, String msg, Stage primaStage) {
		Alert alert;

		if (check == true) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(msg);
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(msg);
		}
		alert.show();
	}
	
	/**
     * Metode sendBookPc memproses pemesanan PC berdasarkan pilihan yang dibuat oleh pengguna.
     * Ini memeriksa apakah ComboBox dan DatePicker telah dipilih sebelum melakukan pemesanan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	private void sendBookPc(Stage primaryStage, int userId) {
		 // Memeriksa apakah ComboBox dan DatePicker telah dipilih
		if (pcComboBox.getValue() == null || bookedDate.getValue() == null) {
	        // Menampilkan alert jika ada pilihan yang kosong
	        alert(false, "Please select a PC and provide a booked date.", primaryStage);
	    } else {
	        // Melakukan proses booking jika pilihan tidak kosong
	        int pcId = pcComboBox.getValue();
	        String bookedDateTxt = bookedDate.getValue().toString();

	        boolean check = new PcBookController().addNewBook(pcId, userId, bookedDateTxt);
	        if (check) {
	            alert(true, "Success Booked a PC!", primaryStage);
	        } else {
	            alert(false, "Failed to book a PC. Please try again.", primaryStage);
	        }
	    }
	}

	/**
     * Metode createIconLabelPair membuat pasangan ikon dan label untuk mewakili status PC.
     * Ini mengatur warna latar belakang berdasarkan status PC, dan menampilkan informasi terkait PC.
     * @param imageView ImageView yang menampilkan ikon PC.
     * @param PcID ID PC yang akan ditampilkan.
     * @param pcStatusLabel Status PC yang akan ditampilkan.
     * @return VBox berisi pasangan ikon dan label.
     */
	private VBox createIconLabelPair(ImageView imageView, String PcID, String pcStatusLabel) {
		VBox pairContainer = new VBox(10);
		ImageView sementara = new ImageView(new Image(getClass().getResourceAsStream("/resources/icon.png")));
		sementara.setFitWidth(95);
		sementara.setFitHeight(95);
		
		String backgroundColor;
	    double borderRadius = 50.0; // You can adjust this value
	    switch (pcStatusLabel.toLowerCase()) {
	        case "usable":
	            backgroundColor = "-fx-background-color: green;";
	            break;
	        case "maintenance":
	            backgroundColor = "-fx-background-color: yellow;";
	            break;
	        case "broken":
	            backgroundColor = "-fx-background-color: red;";
	            break;
	        default:
	            backgroundColor = "-fx-background-color: white;"; // Default color
	            break;
	    }
		
	    pairContainer.setStyle("-fx-border-radius: " + borderRadius + "px; " + backgroundColor);
		
		pairContainer.getChildren().addAll(sementara, new Label("PC " + PcID), new Label(pcStatusLabel));
		pairContainer.setAlignment(Pos.CENTER);
		return pairContainer;
	}
	
	/**
     * Metode createBackground membuat latar belakang tampilan menggunakan gambar yang
     * ditentukan. Ini menentukan properti latar belakang seperti pengulangan
     * gambar dan ukuran latar belakang.
     * @return Objek Background yang telah dibuat.
     */
	private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/icafe-bg.jpg"));
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
	    additionalBackgroundPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);" +
	            "-fx-background-radius: 10;");
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    
	    return additionalBackgroundPane;
	}

	/**
     * Konstruktor kelas BookPC. Metode ini digunakan untuk membuat
     * objek BookPC, menginisialisasi tampilan, menata elemen-elemen,
     * dan menetapkan aksi-aksi yang diperlukan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     * @param userId ID pengguna yang sedang aktif.
     */
	public BookPC(Stage primaryStage, int userId) {
		initialize();
		layouting();
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Book PC");
		action(primaryStage, userId);
	}

	/**
     * Metode getScene mengembalikan objek Scene yang terkait dengan kelas BookPC.
     */
	public Scene getScene() {
		return scene;
	}
}

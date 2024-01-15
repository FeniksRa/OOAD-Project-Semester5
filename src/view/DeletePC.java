package view;

import java.util.List;

import controller.PCController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

/**
 * Kelas DeletePC bertanggung jawab untuk menangani antarmuka pengguna
 * dalam proses penghapusan data PC. Kelas ini mencakup tampilan GUI,
 * logika pemrosesan data, dan tindakan yang terkait dengan penghapusan data PC oleh pengguna.
 */
public class DeletePC {

	Scene scene;
	BorderPane bp;
	Button delete, back;
	FlowPane flowPane;

	HBox hContainer, showPc;
	VBox vContainer;
	ComboBox<Integer> pcId;
	ImageView image1;

	List<PC> listPc;

	/**
     * Metode initialize digunakan untuk menginisialisasi komponen-komponen GUI
     * dan pengaturan awal yang diperlukan untuk tampilan penghapusan data PC.
     * Inisialisasi termasuk pembuatan objek-objek, alokasi memori untuk beberapa koleksi data,
     * dan pengaturan prompt text pada combo box.
     */
	void initialize() {

		bp = new BorderPane();
		delete = new Button("Delete");
		back = new Button("Back");
		hContainer = new HBox(10);
		showPc = new HBox(20);
		vContainer = new VBox();
		flowPane = new FlowPane();

		listPc = new PCController().getAllPCData();
		pcId = new ComboBox<>();
		
		pcId.setPromptText("Select Id");

		for (PC pc : listPc) {
			pcId.getItems().addAll(pc.getPCID());
		}

		scene = new Scene(bp, 1000, 500);
	}

	 /**
     * Metode layouting bertanggung jawab untuk menata elemen-elemen GUI pada tampilan.
     * Ini mencakup konfigurasi combo box, tombol-tombol, dan tata letak keseluruhan.
     * Selain itu, metode ini menetapkan latar belakang dan membangun tampilan akhir untuk ditampilkan di BorderPane.
     */
	void layouting() {
		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(pcId, delete, back);

		FlowPane showPc = new FlowPane(20, 10);

		for (PC pc : listPc) {
			showPc.getChildren().
			addAll(createIconLabelPair(image1, ((Integer) pc.getPCID()).toString(), pc.getPCCondition()));
		}
		
		showPc.setAlignment(Pos.CENTER);
		buttonBox.setAlignment(Pos.CENTER);
		
		ScrollPane scrollPane = new ScrollPane(showPc);
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
        
		bp.setCenter(stackPane);
	}

	/**
     * Metode actions mengatur perilaku tombol dan elemen GUI lainnya yang melibatkan interaksi pengguna.
     * Ini mencakup logika tombol kembali, tombol penghapusan PC, dan pemilihan PC pada combo box.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> back(primaryStage));
		delete.setOnMouseClicked(e -> delete(primaryStage));
	}

	/**
     * Metode delete memproses penghapusan data PC berdasarkan pilihan yang dibuat oleh pengguna.
     * Ini memeriksa apakah PC yang akan dihapus telah dipilih, dan jika iya, melakukan penghapusan data.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     */
	void delete(Stage primaryStage) {
		Integer pcID = pcId.getValue();

		if (pcID == null) {
			alert(false, "Please select pc Id", primaryStage);
			return;
		}

		boolean check = new PCController().deletePC(pcID);
		
		if(check == true) alert(check, "Delete PC status berhasil", primaryStage);
	}

	/**
     * Metode back menangani aksi untuk kembali ke halaman utama.
     * Ini digunakan ketika tombol kembali ditekan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     */
	void back(Stage primaryStage) {
		new HomeAdmin(primaryStage);
	}

	/**
     * Metode alert menampilkan dialog pemberitahuan ke pengguna
     * berdasarkan status operasi yang dilakukan.
     * @param check        Status operasi (berhasil atau gagal).
     * @param msg          Pesan yang ditampilkan pada dialog.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     */
	public void alert(boolean check, String msg, Stage primaryStage) {
		Alert alert;

		if (check == true) {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(msg);
			new HomeAdmin(primaryStage);
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(msg);
		}
		alert.show();
	}

	/**
     * Metode createIconLabelPair membuat struktur tampilan untuk setiap elemen PC
     * yang ditampilkan, termasuk ikon, label PC ID, dan status PC.
     * @param image1 ImageView untuk menampilkan ikon PC.
     * @param PcID ID PC yang ditampilkan.
     * @param pcStatusLabel Status PC yang ditampilkan.
     * @return VBox yang berisi elemen-elemen tampilan untuk satu PC.
     */
	private VBox createIconLabelPair(ImageView image1, String PcID, String pcStatusLabel) {
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
	    additionalBackgroundPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);" +
	            "-fx-background-radius: 10;");
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    
	    return additionalBackgroundPane;
	}

	/**
     * Konstruktor kelas DeletePC. Metode ini digunakan untuk membuat
     * objek DeletePC, menginisialisasi tampilan, menata elemen-elemen,
     * dan menetapkan aksi-aksi yang diperlukan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan.
     */
	public DeletePC(Stage primaryStage) {
		initialize();
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("Delete PC");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

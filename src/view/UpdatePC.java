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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PC;

/**
 * Kelas untuk menampilkan dan mengelola halaman pembaruan kondisi PC.
 */
public class UpdatePC {

	Scene scene;
	BorderPane bp;
	GridPane gp;
	Button update, back;
	FlowPane flowPane;

	HBox hContainer, pcContainer;
	VBox vContainer;
	ComboBox<String> pcCondition;
	ComboBox<Integer> pcId;
	ImageView image1;

	List<PC> listPc;

	/**
	 * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan data PC.
	 */
	void initialize() {
		bp = new BorderPane();
		gp = new GridPane();
		update = new Button("Update");
		back = new Button("Back");
		hContainer = new HBox(10);
		pcContainer = new HBox(20);
		vContainer = new VBox(10);
		flowPane = new FlowPane();
		
		pcId = new ComboBox<>();
		pcCondition = new ComboBox<>();
		listPc = new PCController().getAllPCData();

		pcId.setPromptText("Select Id");
		pcCondition.setPromptText("Select new Condition");
		
		for (PC pc : listPc) {
			pcId.getItems().addAll(pc.getPCID());
		}
		pcCondition.getItems().addAll("Usable", "Maintenance", "Broken");

		scene = new Scene(bp, 1000, 500);
	}
	
	/**
	 * Metode untuk mengatur tata letak elemen-elemen UI pada laman pembaruan kondisi PC.
	 */
	void layouting() {
		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(pcId, pcCondition, update, back);

		FlowPane pcStatusContainer = new FlowPane(20, 10);
        
        for (PC pc : listPc) {
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

        bp.setCenter(stackPane);
	}

	/**
	 * Metode untuk menangani aksi tombol pada laman pembaruan kondisi PC.
	 * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
	 */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> back(primaryStage));
		update.setOnMouseClicked(e -> update(primaryStage));
	}

	/**
	 * Metode untuk memperbarui kondisi PC berdasarkan pemilihan pengguna.
	 * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
	 */
	void update(Stage primaryStage) {
		Integer pcID = pcId.getValue();
		String newStatus = pcCondition.getValue();

		if(newStatus.equals(new PCController().getPCDetail(pcID).getPCCondition())) {
			alert(false, "Please select the new condition", primaryStage);
		}
		
		if (pcID == null) {
			alert(false, "Please select pc Id", primaryStage);
			return;
		}
		
		if (newStatus.isEmpty()) {
			alert(false, "Please select new condition for pc", primaryStage);
			return;
		}
		
		boolean check = new PCController().updatePCCondition(pcID, newStatus);
		
		if(check == true) alert(check, "PC Status Updated", primaryStage);
	}

	/**
	 * Metode untuk kembali ke laman admin.
	 * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
	 */
	void back(Stage primaryStage) {
		new HomeAdmin(primaryStage);
	}

	/**
	 * Metode untuk menampilkan pesan informasi atau kesalahan.
	 * @param check Status keberhasilan operasi.
	 * @param msg Pesan yang akan ditampilkan.
	 * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
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
	 * Metode untuk membuat dan mengembalikan tata letak ikon dan label kondisi PC.
	 * @param imageView Objek ImageView untuk ikon PC.
	 * @param PcID ID PC yang ditampilkan.
	 * @param pcStatusLabel Status kondisi PC yang ditampilkan.
	 * @return VBox berisi ikon, label ID PC, dan label kondisi PC.
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
	    additionalBackgroundPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);" +
	            "-fx-background-radius: 10;");
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    
	    return additionalBackgroundPane;
	}

	/**
	 * Konstruktor untuk membuat objek UpdatePC.
	 * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
	 */
	public UpdatePC(Stage primaryStage) {
		initialize();
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("Update PC");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

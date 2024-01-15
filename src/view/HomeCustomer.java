package view;

import java.util.List;

import controller.PCController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * Kelas HomeCustomer menangani tampilan Home untuk pengguna dengan peran Customer.
 */
public class HomeCustomer {

    Scene scene;
    GridPane gridPane;
    FlowPane flowPane;
    BorderPane borderPane;
    Label pcStatus1, pcStatus2, pcStatus3, pcStatus4;
    ImageView image1, image2, image3, image4;
    Button bookPc, bookPcHistory, report, back;
    VBox container1, container2, container3, mergeContainer;
    List<PC> listPCId;

    Insets marginContainer, paddingContainer;

    /**
     * Inisialisasi elemen-elemen utama dan membuat objek Scene.
     */
    void initialize() {
        flowPane = new FlowPane();
        borderPane = new BorderPane();
        bookPc = new Button("Book PC");
        bookPcHistory = new Button("History");
        report = new Button("Report");
        back = new Button("Back");
        listPCId = new PCController().getAllPCData();

        container1 = new VBox();
        container2 = new VBox();
        container3 = new VBox();
        mergeContainer = new VBox();

        marginContainer = new Insets(0, 20, 0, 0);
        scene = new Scene(borderPane, 1000, 500);
    }
    
    /**
     * Menentukan tata letak tampilan HomeCustomer, termasuk menambahkan elemen-elemen
     * seperti tombol dan label ke dalam VBox dan FlowPane.
     */
    void layouting() {
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(bookPc, bookPcHistory, report, back);
        FlowPane pcStatusContainer = new FlowPane(20, 10);

        for (PC pc : listPCId) {
            pcStatusContainer.getChildren().addAll(createIconLabelPair(image1, ((Integer) pc.getPCID()).toString(), pc.getPCCondition()));
        }

        buttonBox.setAlignment(Pos.CENTER);
        pcStatusContainer.setAlignment(Pos.BASELINE_CENTER);

        // Wrap the FlowPane in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(pcStatusContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");

        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10)); // Use the ScrollPane instead of pcStatusContainer
        mainContainer.getChildren().addAll(scrollPane, buttonBox); // Add the ScrollPane to mainContainer
        mainContainer.setSpacing(10);

        StackPane backgroundPane = new StackPane();
        backgroundPane.setBackground(createBackground());

        StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);

        borderPane.setCenter(stackPane);
    }

    /**
     * Menangani aksi-aksi yang terkait dengan tombol-tombol di tampilan HomeCustomer.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
    void action(Stage primaryStage, int userId) {
    	bookPc.setOnMouseClicked(e -> {
    		BookPC bookPCPage = new	BookPC(primaryStage, userId);
    		primaryStage.setScene(bookPCPage.getScene());
    		primaryStage.setTitle("Book PC");
    	});
    	
    	bookPcHistory.setOnMouseClicked(e ->{
    		TransactionHistoryCustomer THC = new TransactionHistoryCustomer(primaryStage, userId);
    		primaryStage.setScene(THC.getScene());
    		primaryStage.setTitle("History");
    	});
    	
    	report.setOnMouseClicked(e -> {
    		MakeReport makeReport = new MakeReport(primaryStage, "Customer", userId);
    		primaryStage.setScene(makeReport.getScene());
    		primaryStage.setTitle("Report");
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
     * Membuat pasangan ImageView dan Label yang menampilkan status PC.
     * @param imageView Objek ImageView yang menampilkan ikon PC.
     * @param PcID ID PC yang ditampilkan.
     * @param pcStatusLabel Status PC yang ditampilkan.
     * @return VBox yang berisi pasangan ImageView dan Label.
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
     * Membuat latar belakang berupa gambar pada BorderPane.
     * @return Objek Background yang berisi gambar latar belakang.
     */
    private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/icafe-bg.jpg"));
	    BackgroundImage background = new BackgroundImage(
	            backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	            BackgroundPosition.DEFAULT, new BackgroundSize(1000, 500, false, false, false, true));
	    
	    return new Background(background);
	}
	
    /**
     * Membuat StackPane dengan latar belakang tambahan untuk menampilkan konten.
     * @param content Node konten yang akan ditampilkan.
     * @return StackPane dengan latar belakang tambahan.
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
     * Konstruktor untuk menginisialisasi, menentukan tata letak, dan menangani aksi
     * dalam tampilan HomeCustomer.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @param userId ID pengguna saat ini.
     */
    public HomeCustomer(Stage primaryStage, int userId) {
        initialize();
        layouting();
		action(primaryStage, userId);
        primaryStage.setScene(scene);
        primaryStage.show();
		primaryStage.setTitle("Internet CLafes");
    }
}

package view;

import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.TransactionDetail;

/**
 * Kelas untuk menampilkan riwayat transaksi oleh pelanggan.
 */
public class TransactionHistoryCustomer {

    Scene scene;
    Button backButton;
    VBox container1, container2;
    BorderPane borderPane;
    FlowPane flowPane;
    Insets marginContainer, paddingContainer;
    
    TableColumn<TransactionDetail, String> bookedDate;
    TableColumn<TransactionDetail, String> bookedPCId;
    
    ObservableList<TransactionDetail> tDList;
    TableView<TransactionDetail> tableView;

    /**
     * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan data transaksi pelanggan.
     * @param id ID pelanggan yang digunakan untuk mengambil data transaksi terkait.
     */
    void initialize(Integer id) {
    	backButton = new Button("Back to Home");
    	container1 = new VBox();
    	container2 = new VBox();
    	borderPane = new BorderPane();
    	flowPane = new FlowPane();
    	
		tDList = FXCollections.observableArrayList(new TransactionController().getUserTransactionDetail(id));

    	
    	bookedPCId = new TableColumn<>("PC ID");
    	bookedDate = new TableColumn<>("Booked Date");
    	
    	tableView = new TableView<TransactionDetail>(tDList);
    	
    	marginContainer = new Insets(0, 0, 20, 0);
        scene = new Scene(borderPane, 1000, 500);
    }

    /**
     * Metode untuk mengatur tata letak elemen-elemen UI pada laman riwayat transaksi pelanggan.
     */
    @SuppressWarnings("unchecked")
	void layouting() {
    	HBox buttonBox = new HBox(10);
    	buttonBox.getChildren().addAll(backButton);
    	buttonBox.setAlignment(Pos.CENTER);
    	
    	container2.getChildren().addAll(backButton);
    	bookedPCId.setCellValueFactory(new PropertyValueFactory<>("PcID"));
    	bookedDate.setCellValueFactory(new PropertyValueFactory<>("BookedDate"));
    	
    	tableView.getColumns().addAll(bookedPCId, bookedDate);
    	
    	for (TableColumn<TransactionDetail, ?> column : tableView.getColumns()) {
			column.setMinWidth(40);
		}
    	
    	ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10));
        mainContainer.getChildren().addAll(scrollPane, container2);
        mainContainer.setSpacing(5);

    	StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);
    	
    	tableView.setPrefSize(700, 400);
    	container2.setSpacing(15);
        borderPane.setCenter(stackPane);

    }
    
    /**
     * Metode untuk menangani aksi tombol pada laman riwayat transaksi pelanggan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     * @param userId ID pelanggan yang digunakan untuk kembali ke laman utama pelanggan.
     */
    void action(Stage primaryStage, Integer userId) {
        backButton.setOnAction(e -> goBack(userId));
	}
    
    /**
     * Metode untuk kembali ke laman utama pelanggan.
     * @param userId ID pelanggan yang digunakan untuk kembali ke laman utama pelanggan.
     */
    private void goBack(int userId) {
        // Handle the action to go back to the Home Page
        Stage primaryStage = (Stage) scene.getWindow();
        HomeCustomer homePage = new HomeCustomer(primaryStage, userId);
//        primaryStage.setScene(homePage.getScene());
        primaryStage.setTitle("Home Page");
    }
    
    /**
     * Metode untuk membuat dan mengembalikan objek Background berdasarkan gambar latar.
     * @return Background yang dibuat dari gambar latar.
     */
    private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/icafe-bg.jpg"));
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
     * Konstruktor untuk membuat objek TransactionHistoryCustomer.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     * @param userId ID pelanggan yang digunakan untuk mengambil data transaksi terkait.
     */
    public TransactionHistoryCustomer(Stage primaryStage, Integer userId) {
        initialize(userId);
        layouting();
        primaryStage.setScene(scene);
        primaryStage.show();
		primaryStage.setTitle("Home Page");
		action(primaryStage, userId);
    }

    /**
     * Metode untuk mendapatkan objek Scene yang digunakan pada laman riwayat transaksi pelanggan.
     * @return Objek Scene dari laman riwayat transaksi pelanggan.
     */
	public Scene getScene() {
        return scene;
    }
}

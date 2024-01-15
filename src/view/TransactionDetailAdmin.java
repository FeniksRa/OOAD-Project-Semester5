package view;

import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.TransactionDetail;
import model.TransactionHeader;

/**
 * Kelas untuk menangani tampilan detail transaksi oleh Admin.
 */
public class TransactionDetailAdmin {

	Scene scene;
	BorderPane bp;
	VBox containerV;
	HBox containerH;
	Button submit, back;

	TableColumn<TransactionDetail, Integer> pcIDCol;
	TableColumn<TransactionDetail, String> customerNameCol;
	TableColumn<TransactionDetail, String> bookedDateCol;
	TableView<TransactionDetail> tableView;
	Insets paddingBp;
	ObservableList<TransactionDetail> tDList;
	ComboBox<Integer> tDID;

	/**
     * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan data transaksi.
     * @param id ID transaksi yang akan ditampilkan detailnya.
     */
	void initialize(Integer id) {
		bp = new BorderPane();
		tDList = FXCollections.observableArrayList(new TransactionController().getAllTransactionDetail(id));
		tDID = new ComboBox<>();
		submit = new Button("Confirm");
		back = new Button("Back");

		pcIDCol = new TableColumn<>("PC ID");
		customerNameCol = new TableColumn<>("Customer Name");
		bookedDateCol = new TableColumn<>("Booked Date");
		
		tableView = new TableView<TransactionDetail>(tDList);

		containerH = new HBox();
		containerV = new VBox();

		tDID.setPromptText("Select transaction Id");
		
		for (TransactionDetail tD : tDList) {
			tDID.getItems().add(tD.getPcID());
		}

		paddingBp = new Insets(10); // padding 10 untuk setiap sisi

		scene = new Scene(bp, 1000, 500); 
	}

	/**
     * Metode untuk mengatur tata letak elemen-elemen UI pada laman detail transaksi.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		containerV.getChildren().addAll(containerH);
		containerH.getChildren().addAll(back);

		pcIDCol.setCellValueFactory(new PropertyValueFactory<>("PcID"));
		customerNameCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
		bookedDateCol.setCellValueFactory(new PropertyValueFactory<>("BookedDate"));
		tableView.getColumns().addAll(pcIDCol, customerNameCol, bookedDateCol);
		
		for (TableColumn<TransactionDetail, ?> column : tableView.getColumns()) {
			column.setMinWidth(70);
		}
		
		ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10));
        mainContainer.getChildren().addAll(scrollPane, containerV);
        mainContainer.setSpacing(5);

		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);
		
		tableView.setPrefSize(700, 500);
		containerH.setAlignment(Pos.CENTER);
		containerH.setSpacing(10);
		containerV.setSpacing(10);
        bp.setCenter(stackPane);
	}

	/**
     * Metode untuk menangani aksi tombol pada laman detail transaksi oleh Admin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> {
			new TransactionHeaderAdmin(primaryStage);
		});
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
	    additionalBackgroundPane.setMaxSize(900, 400);
	    additionalBackgroundPane.getChildren().add(content);
	    
	    return additionalBackgroundPane;
	}

	/**
     * Konstruktor untuk membuat objek TransactionDetailAdmin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     * @param id ID transaksi yang akan ditampilkan detailnya.
     */
	public TransactionDetailAdmin(Stage primaryStage, Integer id) {
		initialize(id);
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("Transaction Header History Page");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

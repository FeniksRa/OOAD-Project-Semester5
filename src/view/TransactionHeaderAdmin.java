package view;

import java.util.ArrayList;
import java.util.List;

import controller.TransactionController;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Report;
import model.TransactionHeader;

/**
 * Kelas untuk menampilkan riwayat transaksi oleh Admin.
 */
public class TransactionHeaderAdmin {

	Scene scene;
	BorderPane bp;
	VBox containerV;
	HBox containerH;
	Button submit, back;

	TableColumn<TransactionHeader, Integer> transactionIDCol;
	TableColumn<TransactionHeader, Integer> userIDCol;
	TableColumn<TransactionHeader, String> staffNameCol;
	TableColumn<TransactionHeader, String> transactionDateCol;

	TableView<TransactionHeader> tableView;

	Insets paddingBp;

	ObservableList<TransactionHeader> tHList;
	ComboBox<Integer> tHID;

	/**
     * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan data transaksi.
     */
	void initialize() {

		bp = new BorderPane();
		tHList = FXCollections.observableArrayList(new TransactionController().getAllTransactionHeaderData());
		tHID = new ComboBox<>();
		submit = new Button("Confirm");
		back = new Button("Back");

		transactionIDCol = new TableColumn<>("Transaction ID");
		userIDCol = new TableColumn<>("User ID");
		staffNameCol = new TableColumn<>("Staff Name");
		transactionDateCol = new TableColumn<>("Transaction Date");

		tableView = new TableView<TransactionHeader>(tHList);

		containerH = new HBox();
		containerV = new VBox();

		tHID.setPromptText("Select Id");
		for (TransactionHeader tH : tHList) {
			tHID.getItems().add(tH.getTransactionID());
		}

		paddingBp = new Insets(10); // padding 10 untuk setiap sisi

		scene = new Scene(bp, 1000, 500);
	}

	/**
     * Metode untuk mengatur tata letak elemen-elemen UI pada laman riwayat transaksi.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		containerV.getChildren().addAll(tableView, containerH);
		containerH.getChildren().addAll(tHID, submit, back);

		transactionIDCol.setCellValueFactory(new PropertyValueFactory<>("TransactionID"));
		userIDCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
		staffNameCol.setCellValueFactory(new PropertyValueFactory<>("StaffName"));
		transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("TransactionDate"));
		tableView.getColumns().addAll(transactionIDCol, userIDCol, staffNameCol, transactionDateCol);

		for (TableColumn<TransactionHeader, ?> column : tableView.getColumns()) {
			column.setMinWidth(70);
		}

		ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
		VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getChildren().addAll(scrollPane, containerH);
        mainContainer.setSpacing(25);

		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        
		StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);

		tableView.setPrefSize(700, 500);
		containerH.setAlignment(Pos.CENTER);
        containerH.setSpacing(10);
        bp.setCenter(stackPane);
	}

	/**
     * Metode untuk menangani aksi tombol pada laman riwayat transaksi oleh Admin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	void actions(Stage primaryStage) {
		back.setOnMouseClicked(e -> {
			new HomeAdmin(primaryStage);
		});

		submit.setOnMouseClicked(e -> {
			Integer id = tHID.getValue();

			if (id == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Please choose transaction ID");
			} else {
				new TransactionDetailAdmin(primaryStage, id);
			}
		});
	}
	
	/**
     * Metode untuk membuat dan mengembalikan objek Background berdasarkan gambar latar.
     * @return Background yang dibuat dari gambar latar.
     */
	private Background createBackground() {
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/operator-bg.png"));
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
     * Konstruktor untuk membuat objek TransactionHeaderAdmin.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	public TransactionHeaderAdmin(Stage primaryStage) {
		initialize();
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("Transaction Header History Page");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

package view;

import controller.ReportController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Report;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

/**
 * Kelas untuk menampilkan laporan-laporan yang telah dibuat.
 */
public class ViewAllReport {

	Scene scene;
	BorderPane bp;
	GridPane gp;
	Button back;
	VBox container;
	ObservableList<Report> reportList;

	TableColumn<Report, Integer> reportIDCol;
	TableColumn<Report, String> userRoleCol;
	TableColumn<Report, Integer> pcIDCol;
	TableColumn<Report, String> reportNoteCol;
	TableColumn<Report, String> reportDateCol;

	TableView<Report> tableView;

	Insets paddingListView, paddingBorderPane;

	/**
     * Metode inisialisasi untuk menyiapkan elemen-elemen awal dan data laporan.
     */
	void initialize() {
		bp = new BorderPane();
		back = new Button("Back");
		container = new VBox(10);
		
		reportList = FXCollections.observableArrayList(new ReportController().getAllReportData());

		reportIDCol = new TableColumn<>("Report ID");
		userRoleCol = new TableColumn<>("User Role");
		pcIDCol = new TableColumn<>("PC ID");
		reportNoteCol = new TableColumn<>("Report Note");
		reportDateCol = new TableColumn<>("Report Date");

		tableView = new TableView<Report>(reportList);

		paddingListView = new Insets(5, 0, 5, 5);
		paddingBorderPane = new Insets(20, 10, 10, 20);

		scene = new Scene(bp, 1000, 500);
	}

	/**
     * Metode untuk mengatur tata letak elemen-elemen UI pada laman tampilan semua laporan.
     */
	@SuppressWarnings("unchecked")
	void layouting() {
		container.getChildren().addAll(back);
		// harus disesuaikan dengan getter object, misal getPcIDDDd, maka new
		// PropertyValueFactory<>("PcIDDDd")
		reportIDCol.setCellValueFactory(new PropertyValueFactory<>("ReportID"));
		userRoleCol.setCellValueFactory(new PropertyValueFactory<>("UserRole"));
		pcIDCol.setCellValueFactory(new PropertyValueFactory<>("PCID"));
		reportNoteCol.setCellValueFactory(new PropertyValueFactory<>("ReportNote"));
		reportDateCol.setCellValueFactory(new PropertyValueFactory<>("ReportDate"));

		tableView.getColumns().addAll(reportIDCol, userRoleCol, pcIDCol, reportNoteCol, reportDateCol);

		// membuat tabel tidak memiliki column lebih dari yang diinginkan
		for (TableColumn<Report, ?> column : tableView.getColumns()) {
			column.setMinWidth(70);
		}
		
		ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10;");
		
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(scrollPane, new Insets(10, 10, 0, 10));
        mainContainer.getChildren().addAll(scrollPane, container);
        mainContainer.setSpacing(5);

		StackPane backgroundPane = new StackPane();
		backgroundPane.setBackground(createBackground());
		StackPane additionalBackgroundPane = createAdditionalBackgroundPane(mainContainer);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundPane, additionalBackgroundPane);
		
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setPrefSize(700, 500);
		container.setAlignment(Pos.CENTER);

		bp.setCenter(stackPane);
	}

	/**
     * Metode untuk menangani aksi tombol pada laman tampilan semua laporan.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	private void actions(Stage primaryStage) {
		back.setOnAction(e-> {
			new HomeAdmin(primaryStage);
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
     * Konstruktor untuk membuat objek ViewAllReport.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan laman.
     */
	public ViewAllReport(Stage primaryStage) {
		initialize();
		layouting();
		actions(primaryStage);
		primaryStage.show();
		primaryStage.setTitle("View All Report");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
	}

}

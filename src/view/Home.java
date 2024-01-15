package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Kelas Home adalah kelas utama yang menangani tampilan awal (Home) dari aplikasi.
 * Kelas ini mengimplementasikan antarmuka JavaFX Application.
 */
public class Home extends Application {

	/**
     * Metode start adalah metode yang diimplementasikan dari antarmuka Application.
     * Metode ini memulai eksekusi aplikasi dan menampilkan tampilan utama (Home).
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Internet CLafes");

		Image icon = new Image(getClass().getResourceAsStream("/resources/justin.png"));
		primaryStage.getIcons().add(icon);

		// Create the layout for the Home screen
		StackPane homeLayout = createHomeLayout(primaryStage);

		// Set up the scene
		Scene scene = new Scene(homeLayout, 1000, 500);
		primaryStage.setScene(scene);

		// Show the stage
		primaryStage.show();
	}

	/**
     * Metode createHomeLayout bertanggung jawab untuk membuat tata letak
     * tampilan utama (Home). Ini mencakup pembuatan tombol login dan register,
     * menetapkan aksi tombol, dan menampilkan latar belakang berupa gambar.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @return StackPane yang berisi elemen-elemen tampilan untuk tampilan utama.
     */
	private StackPane createHomeLayout(Stage primaryStage) {
		// Create buttons for registration and login
		Button loginButton = new Button("Login");
		Button registerButton = new Button("Register");
		
		registerButton.setMaxWidth(200);
		loginButton.setMaxWidth(200);

		// Set up actions for the buttons
		registerButton.setOnAction(e -> {
			Register register = new Register();
			try {
				register.Registration(primaryStage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		loginButton.setOnAction(e -> {
			Login login = new Login();
			try {
				login.loginIn(primaryStage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		// Create layout for the Home screen with buttons
		VBox buttonsLayout = new VBox(20); // 20 is the spacing between elements
		buttonsLayout.setAlignment(Pos.CENTER);
		buttonsLayout.getChildren().addAll(registerButton, loginButton);

		// Load the background image
		Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/lumba1.png"));

		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

		// Create an ImageView for the background image
//        ImageView backgroundImageView = new ImageView(backgroundImage);

		// Set up the StackPane with buttons and background image
		StackPane homeLayout = new StackPane();
		homeLayout.setBackground(new Background(background));
		homeLayout.getChildren().addAll(buttonsLayout);
		
		Text namaWarnetText = new Text("Internet CLafes");
		namaWarnetText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    namaWarnetText.setFill(Color.WHITE);
	    namaWarnetText.setTranslateY(150);
	    
	    StackPane.setAlignment(namaWarnetText, Pos.TOP_CENTER);
	    
	    homeLayout.getChildren().add(namaWarnetText);


		return homeLayout;
	}

	/**
     * Metode main adalah metode utama yang dipanggil untuk menjalankan aplikasi.
     * Metode ini menggunakan metode launch dari kelas Application untuk memulai eksekusi.
     * @param args Argumen yang dapat diteruskan saat menjalankan aplikasi.
     */
	public static void main(String[] args) {
		launch(args);
	}
}

package view;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Kelas utama yang memulai aplikasi JavaFX.
 */
public class Main extends Application{
	
	/**
     * Metode utama yang memulai aplikasi.
     * @param args Argumen baris perintah yang diteruskan ke metode `launch`.
     */
    public static void main(String[] args) {
    	launch(args);
    }
    
    /**
     * Metode `start` yang diwarisi dari kelas Application. Metode ini dipanggil ketika
     * aplikasi JavaFX dimulai. Di dalamnya, sebuah objek Home dibuat dan metode start
     * pada objek tersebut dipanggil untuk menampilkan tampilan utama aplikasi.
     * @param primaryStage Objek Stage yang digunakan untuk menampilkan tampilan utama.
     * @throws Exception Jika terjadi kesalahan selama proses pengaturan tampilan.
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Home home = new Home();
		home.start(primaryStage);
	}
}

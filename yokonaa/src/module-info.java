/**
 * */
/**
 * */
module hehe {
	 requires javafx.controls;
	 requires javafx.fxml;
	 requires javafx.graphics;
	 requires java.desktop;
	 
	 // FIX: Correctly open 'main' to both JavaFX modules for reflection/resources
	 opens main to javafx.graphics, javafx.fxml;
	 
	 // EXPORT: Exports the main package code
	 exports main;
	 
	 // You should also ensure 'view' is opened if it contains any FXML or relies on reflection
	 // opens view to javafx.graphics, javafx.fxml; 
}
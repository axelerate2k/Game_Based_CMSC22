/**
 * 
 */
/**
 * 
 */
module hehe {
	 requires javafx.controls;
	    requires javafx.fxml;
	    requires javafx.graphics;
		requires java.desktop;
	    // Opens the package to JavaFX for reflection
	    opens main to javafx.graphics;
	    
	    // Exports your package if other modules need to access it
	    exports main;
}
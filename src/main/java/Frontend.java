import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Frontend extends Application {
    Stage window = new Stage();
    private TextField destination;
    private TextField country;
    private TextField date;
    private TextField duration;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        window.setTitle("Travel API");
        input();

    }

    public void input() {

        GridPane first = new GridPane();
        first.setAlignment(Pos.CENTER);
        first.setPadding(new Insets(15));
        first.setHgap(5);
        first.setVgap(5);

        Label title = new Label("Travel API");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        destination = new TextField();
        destination.setPromptText("Enter destination");
        country = new TextField();
        country.setPromptText("Enter country");
        date = new TextField();
        date.setPromptText("Enter date");
        duration = new TextField();
        duration.setPromptText("Enter duration");

        first.add(new Label("Destination: "), 0, 0 );
        first.add((destination), 1, 0);
        first.add(new Label("Country: "), 0, 1);
        first.add((country), 1, 1);
        first.add(new Label("Date: "), 0, 2);
        first.add((date), 1, 2);
        first.add(new Label("Duration: "), 0, 3);
        first.add((duration), 1, 3);
        

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            sendToBackendApi();
            output();
        });
        first.add(submitButton, 1, 4);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        Scene scene = new Scene(first, 400, 300);
        window.setScene(scene);
        window.show();
    }

    public void sendToBackendApi() {
        try {
            // Extract the user values dynamically from your global text field objects
            String destVal = destination.getText();
            String countryVal = country.getText();
            String dateVal = date.getText();
            
            // Convert your string duration input into a safe Integer primitive matching Python's expectations
            int durationVal = Integer.parseInt(duration.getText().trim());

            // Build a raw JSON payload data text string matching your Flask model columns exactly
            String jsonPayload = String.format(
                "{\"destination\":\"%s\", \"country\":\"%s\", \"date\":\"%s\", \"duration\":%d}",
                destVal, countryVal, dateVal, durationVal
            );

            // Configure the HTTP REST Client
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5000/destinations")) // Flask endpoint for your API
                    .header("Content-Type", "application/json") // Directs Flask to read request as JSON format
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Fire the asynchronous request over to your python backend socket panel thread
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                  .thenApply(HttpResponse::statusCode)
                  .thenAccept(statusCode -> {
                      System.out.println("API Server Status Code Response: " + statusCode);
                      if(statusCode == 201) {
                          System.out.println("Data saved cleanly to SQLite travel.db file!");
                      } else {
                          System.out.println("API Error occurrence on insert transaction.");
                      }
                  });

        } catch (NumberFormatException ex) {
            System.out.println("Error: Duration field text entry must be a valid integer number format.");
        } catch (Exception ex) {
            System.out.println("Network Error calling Flask application endpoint: " + ex.getMessage());
        }
    }

    public  void output() {


        Text title = new Text(0, 20, """
                                Travel API
                                """);
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        String destination = this.destination.getText();
        String country = this.country.getText();
        String date = this.date.getText();
        String duration = this.duration.getText();

        Text destinationLabel = new Text(0, 40, "Your next destination is " + destination + "\n");
        Text countryLabel = new Text(0, 60, "in " + country+ "\n");
        Text dateLabel = new Text(0, 80, "on " + date + "\n");
        Text durationLabel = new Text(0, 100, "for " + duration + " days");

        Pane second = new Pane();
        second.setPadding(new Insets(15));
        second.getChildren().addAll(title, destinationLabel, countryLabel, dateLabel, durationLabel);
        Scene scene = new Scene(second, 400, 300);
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



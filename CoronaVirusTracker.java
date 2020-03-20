/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coronavirustracker;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import static javafx.scene.text.Font.font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gursi
 */
public class CoronaVirusTracker extends Application {

    //Buttons
    private Button btnTrack;
    private Button btnAbout;
    //API key and other data
    private String key = "893ac47585msh50e4355db8b189dp1c33a1jsn6aa0455f1574";
    private String host = "covid-19-coronavirus-statistics.p.rapidapi.com";
    //Objects and Arrays for json
    JSONArray jsonData;
    JSONArray jsonarr_2;
    JSONObject finalData;
    JSONObject jsonobj_1;
    @Override
    public void start(Stage stage) {
        //Creating Layout 
        GridPane pane = new GridPane();

        //Initializing Buttons
        btnTrack = new Button("Track");
        btnAbout = new Button("About");

        //adding buttons to the gridpane.
        pane.addColumn(0, btnTrack);
        pane.addColumn(2, btnAbout);


        //adding gap between elements on screen
        pane.setVgap(15);
        pane.setHgap(15);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setAlignment(Pos.CENTER);

        //setting class to elements to use css
        btnTrack.getStyleClass().add("Buttons");
        btnAbout.getStyleClass().add("Buttons");
        
        Scene scene = new Scene(pane, 400, 250);
        scene.getStylesheets().add("coronavirustracker/Styles.css");
        stage.setTitle("CoronaVirus Tracker");
        stage.setScene(scene);
        stage.show();

        //Adding Action To Buttons
        btnTrack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String link = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/stats?country=India";

                    URL url = null;

                    url = new URL(link);

                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json; charset=utf-8");
                    connection.setRequestProperty("x-rapidapi-host", host);
                    connection.setRequestProperty("x-rapidapi-key", key);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line = null;
                    JSONObject obj;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    //Parsing the API Data
                    JSONParser parser = new JSONParser();
                    //Converting It To Object
                    obj = (JSONObject) parser.parse(stringBuilder.toString());
                    JSONArray jsonData = new JSONArray();
                    jsonData.add(obj);

                    for (int i = 0; i < jsonData.size(); i++) {
                    //Store the JSON objects in an array
                    //Get the index of the JSON object and print the values as per the index
                        jsonobj_1 = (JSONObject) jsonData.get(i);
                        jsonData = new JSONArray();
                        jsonData.add(jsonobj_1.get("data"));

                    }
                    JSONArray jsonarr_2 = null;
                    for (int i = 0; i < jsonData.size(); i++) {
                        jsonobj_1 = (JSONObject) jsonData.get(i);
                        //Store the JSON object in JSON array as objects (For level 2 array element i.e stats)
                        jsonarr_2 = (JSONArray) jsonobj_1.get("covid19Stats");

                    }
                    finalData = (JSONObject) jsonarr_2.get(0);

                    System.out.println(finalData.get("country"));
                    //Creatin a pop-up to display results on pop-up
                   Alert alert = new Alert(Alert.AlertType.NONE);   
                   alert.setTitle("Results");
                   alert.setHeaderText( "Country: " +(String)finalData.get("country") +"\n" +"Confirmed Cases: " +(Long)finalData.get("confirmed") + "\n" +"Recovered: " +(Long)finalData.get("recovered") + "\n" + "Total deaths: " +(Long)finalData.get("deaths"));
                   alert.showAndWait();
                     

                } catch (IOException ex) {
                    Logger.getLogger(CoronaVirusTracker.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(CoronaVirusTracker.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

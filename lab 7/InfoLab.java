// Code used by Dr Tasse in CS1083
// hacked up by OFK, March 2020, March 2021.

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;

import com.mysql.jdbc.jdbc2.optional.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;

public class InfoLab extends Application
{
   TextArea mainText;
   TextField inputField;
   Button OKbutton;

   ChoiceBox<String> firstLetterLastNameChoice;

   String[] letterChoices = {"N", "S", "P", "L"};

    // Put JDBC variable for connection here
    Connection con;

   public void start(Stage primaryStage)
   {
      Font mainFont = new Font("courier", 24);
      mainText = new TextArea();
      mainText.setFont(mainFont);
      mainText.setPrefRowCount(6);
      mainText.setPrefColumnCount(30);
      mainText.setWrapText(true);

      // open the JDBC connection, get the list of first letters
      // of Customer last names, overwriting letterChoices.  On exception write error msg
      // to mainText.

       try {

           MysqlDataSource mds = new MysqlDataSource();
    
           // in the connect string below, leave it as cs_owen because this
           // is the database that has table CUSTOMER

           mds.setURL("jdbc:mysql://pizza.unbsj.ca:3306/cs_owen?useSSL=false");

	   // but update the MySQL username and password to be yours
           // n.b. including credentials in source code is HORRIBLE security
           // so don't do this in real life

           mds.setUser("put your MySQL username here");  // MySQL username
           mds.setPassword("put your mySQL password here");   //MySQL password

           // make the JDBC connection to server
           con=mds.getConnection();
           
           Statement s=con.createStatement();
           ResultSet r=s.executeQuery("select distinct substring(CUS_LNAME,1,1) as fletter from cs_owen.CUSTOMER order by fletter");

           ArrayList<String> realLetterChoices = new ArrayList<>();

           while (r.next())
             realLetterChoices.add(r.getString("fletter")); 

           // bad style: should be in a finally block, really
           r.close(); s.close();  // at least we are doing closes if there is no exception
	   
           realLetterChoices.add("Any"); 

           // move them over into an array
           letterChoices = new String[realLetterChoices.size()];
	   int ctr=0;
           for (String choice : realLetterChoices)
             letterChoices[ctr++]=choice;
    
       } catch (Exception e) {
           mainText.setText("error "+e);
       }

      inputField = new TextField();
      inputField.setFont(mainFont);
      inputField.setMinWidth(500);
      inputField.setText("input area code here (try 615 or 713)");

      //inputField.setOnAction(this::processTextInput);  // no action

      // code for choice box      
      firstLetterLastNameChoice = new ChoiceBox<String>();
      firstLetterLastNameChoice.setStyle("-fx-font: 24px \"Courier\";");  
      firstLetterLastNameChoice.getItems().addAll(letterChoices);
      firstLetterLastNameChoice.getSelectionModel().select(0);

      // code for CustomerSearch  button and label
      // The action for this button will eventually search for customers in the specified
      // area code whose last names start with the chosen letter

      // Only create a functioning button if we have a connection
      if (con != null) {
        OKbutton = new Button("CustomerSearch");
        OKbutton.setFont(mainFont);
        OKbutton.setOnAction(this::processButton);
      }
      else {
         OKbutton = new Button("Oops No Connection");
         OKbutton.setFont(mainFont);
      }

      // putting it all together
      VBox pane = new VBox(inputField,
                           firstLetterLastNameChoice,
                           OKbutton, mainText);
      pane.setSpacing(10);
      Scene theScene = new Scene(pane, 1400, 400);
      primaryStage.setTitle("INFO 1103 Lab GUI");
      primaryStage.setScene(theScene);

      primaryStage.show();
  }

   
   // code for the Fire button, needs to be completed...
   public void processButton(ActionEvent event)
   {
      String msg = "";  // the formatted result of the query should be put in this string
      String usersChoiceForFirstLetterOfLastName = firstLetterLastNameChoice.getSelectionModel().getSelectedItem();

      // Do a JDBC lookup here and send the output to mainText via msg
      // ... code to be written:
      // You already have Connection con set up from earlier.

 
      // form and execute a query like
      //   select ... from cs_owen.CUSTOMER where CUS_LNAME like 'Z%'
      //      AND CUS_AREACODE=506
      
      // but if user chose "Any" for first letter of last name, 
      //   you should omit the      CUS_LNAME like 'Z%' in above example
      //   and do the selection only on the basis of CUS_AREACODE

      String q = "select * from cs_owen.CUSTOMER where "; 

      // code for students to write next...to handle the first letters
      // of the name, which can include Any

      if (!usersChoiceForFirstLetterOfLastName.equals("Any")) {
          // student code to go here: replace "" with something more interesting
          q += "";
      }
      q += "CUS_AREACODE="+inputField.getText();

      // query string is now complete and ready to be passed to the database
      // form a Statement, do an executeQuery

      // iterate over the resulting ResultSet
      // output chosen stuff about person (fname, lname, phone num) to msg

      // ONLY ONE OF THE BELOW SHOULD BE USED

      mainText.setText("Query: " +q); // to debug
      // mainText.setText(msg);  // TO SEE THE OUTPUT
   }

   public static void main(String[] args)
   {
      launch(args);
   }
}




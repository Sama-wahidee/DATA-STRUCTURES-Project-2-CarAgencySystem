package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.module.ModuleDescriptor.Opens;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Predicate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	int count = 0;
	TableView<Cars> tableView = new TableView();
	ComboBox<String> brandCB = new ComboBox<>();
	ComboBox<String> modelCB = new ComboBox<>();
	ComboBox<String> yearCB = new ComboBox<>();
	ComboBox<String> colorCB = new ComboBox<>();
	ComboBox<String> priceCB = new ComboBox<>();

	ObservableList<Cars> cars = FXCollections.observableArrayList();
	DoubleLinkedList brand = new DoubleLinkedList();
	LinkStack finishedO = new LinkStack();
	LinkQueue InProcessO = new LinkQueue();
	Cars temp;
	Cars car;
	String b;

	public void start(Stage primaryStage) throws Exception {
		/////////////////////////////////////////////////////////
		// the upload files stage

		primaryStage.setTitle("Wholesome Motors");
		StackPane st = new StackPane();
		Scene scene = new Scene(st, 700, 700);
		backGround(st, "C:\\Users\\sama6\\Downloads\\Car.jpg");
		VBox vb = new VBox(30);
		Button uploadC = new Button("Upload available cars file");
		uploadC.setFont(new Font("Arial", 20));
		uploadC.setStyle(
				"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
		Button uploadO = new Button("Upload orders file");
		uploadO.setFont(new Font("Arial", 20));
		uploadO.setStyle(
				"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
		vb.setAlignment(Pos.CENTER);
		vb.getChildren().addAll(uploadC);
		st.getChildren().add(vb);
		primaryStage.setScene(scene);
		primaryStage.show();
		Stage tabsStage = new Stage();

		{
			// an action event to read the cars data
			uploadC.setOnAction(e -> {
				try {
					// reading the data from the file
					FileChooser fileChooser = new FileChooser();
					Stage fileChooserStage = new Stage();
					File file = fileChooser.showOpenDialog(fileChooserStage);
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					ArrayList<String> readFile = new ArrayList<>();
					while ((line = br.readLine()) != null) {
						readFile.add(line);
					}
					// addind the data into the lists
					String[] oneLine = new String[5];
					int k = 0;
					for (int i = 0; i < readFile.size() - 1; i++) {
						oneLine = readFile.get(i + 1).split(",");
						for (int j = 0; j < oneLine.length; j++) {
							oneLine[j] = oneLine[j].trim();
						}

						ArrayList<String> onelineL = new ArrayList<String>(Arrays.asList(oneLine));
						for (int j = 0; j < 1; j++) {
							// if the brand already exist then add the car object to the linked list of its
							// location
							if (brand.search(onelineL.get(0))) {
								int price = (int) Integer
										.parseInt(onelineL.get(4).substring(0, onelineL.get(4).length() - 1)) * 1000;
								int year = (int) Integer.parseInt(onelineL.get(2));
								Cars car = new Cars(onelineL.get(0), onelineL.get(1), year, onelineL.get(3), price);
								car.setUrl("C:\\Users\\sama6\\Downloads\\" + (k + 1) + ".png");
								brand.getNode(onelineL.get(0)).linkedList.addFirst(car);
								cars.add(0, car);
								k++;

							}
							// if the brand does not exist then add it to the double Linked list then add
							// the car object to the linked list of its
							// location
							else if (!(brand.search(onelineL.get(0)))) {
								String addBrand = onelineL.get(0);
								int price = (int) Integer
										.parseInt(onelineL.get(4).substring(0, onelineL.get(4).length() - 1)) * 1000;
								int year = (int) Integer.parseInt(onelineL.get(2));
								LinkedList l = new LinkedList();
								Cars car = new Cars(onelineL.get(0), onelineL.get(1), year, onelineL.get(3), price);
								l.addFirst(car);
								car.setUrl("C:\\Users\\sama6\\Downloads\\" + (k + 1) + ".png");
								brand.addFirst(addBrand, l);
								cars.add(0, car);
								k++;
							}
						}
					}
					brand.sort();
					brand.singleSort();
					System.out.println(brand.print());
					vb.getChildren().add(uploadO);
				} catch (FileNotFoundException e1) {
					dialog(AlertType.ERROR, "File not found");
				} catch (IOException e1) {
				}
				dialog(AlertType.INFORMATION, "The cars data has been uploaded successfully");
			});
			// an action event to read the order file and separate it into stack and queue
			uploadO.setOnAction(e -> {
				try {
					// reading the data from the file
					FileChooser fileChooser = new FileChooser();
					Stage fileChooserStage = new Stage();
					File file = fileChooser.showOpenDialog(fileChooserStage);
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					ArrayList<String> readFile = new ArrayList<>();
					while ((line = br.readLine()) != null) {
						readFile.add(line);

					}
					// addind the data into the lists
					String[] oneLine = new String[5];
					for (int i = 0; i < readFile.size() - 1; i++) {
						oneLine = readFile.get(i + 1).split(",");
						for (int j = 0; j < oneLine.length; j++) {
							oneLine[j] = oneLine[j].trim();
						}

						ArrayList<String> onelineL = new ArrayList<String>(Arrays.asList(oneLine));
						for (int j = 0; j < 1; j++) {
							// if the order status finished then add it to the stack
							if (onelineL.get(8).equals("Finished")) {
								int year = (int) Integer.parseInt(onelineL.get(4));
								int price = (int) Integer
										.parseInt(onelineL.get(6).substring(0, onelineL.get(6).length() - 1)) * 1000;
								Cars c = new Cars(onelineL.get(2), onelineL.get(3), year, onelineL.get(5), price);
								int phoneNum = (int) Integer.parseInt(onelineL.get(1));
								Date date = new SimpleDateFormat("dd/MM/yyyy").parse(onelineL.get(7));
								finishedO.push(new Orders(onelineL.get(0), phoneNum, c, date, onelineL.get(8)));
							}
							// if the order status in process then add it to the queue
							else if (onelineL.get(8).equals("InProcess")) {
								int year = (int) Integer.parseInt(onelineL.get(4));
								int price = (int) Integer
										.parseInt(onelineL.get(6).substring(0, onelineL.get(6).length() - 1)) * 1000;
								Cars c = new Cars(onelineL.get(2), onelineL.get(3), year, onelineL.get(5), price);
								int phoneNum = (int) Integer.parseInt(onelineL.get(1));
								Date date = new SimpleDateFormat("dd/MM/yyyy").parse(onelineL.get(7));
								InProcessO.enQueue(new Orders(onelineL.get(0), phoneNum, c, date, onelineL.get(8)));
							}
						}
					}

				} catch (FileNotFoundException e1) {
					dialog(AlertType.ERROR, "File not found");
				} catch (IOException e1) {
				} catch (ParseException e1) {
				}
				dialog(AlertType.INFORMATION, "The orders data has been uploaded successfully");
				primaryStage.close();
				tabsStage.show();

			});
		}
		TabPane tabPane = new TabPane();
		Tab tab1 = new Tab("Brand operations");
		tab1.setStyle(
				"-fx-background-color: #808080; -fx-border-color: #FFFF00; -fx-border-width: 1px;-fx-text-fill: #000000");
		Tab tab2 = new Tab("Insert new car");
		tab2.setStyle(
				"-fx-background-color: #808080; -fx-border-color: #FFFF00; -fx-border-width: 1px;-fx-text-fill: #000000");
		Tab tab3 = new Tab("Cars table");
		tab3.setStyle(
				"-fx-background-color: #808080; -fx-border-color: #FFFF00; -fx-border-width: 1px;-fx-text-fill: #000000");
		Tab tab4 = new Tab("Panding order");
		tab4.setStyle(
				"-fx-background-color: #808080; -fx-border-color: #FFFF00; -fx-border-width: 1px;-fx-text-fill: #000000");
		Tab tab5 = new Tab("Aprove order");
		tab5.setStyle(
				"-fx-background-color: #808080; -fx-border-color: #FFFF00; -fx-border-width: 1px;-fx-text-fill: #000000");
		Tab tab6 = new Tab("Save");
		tab6.setStyle(
				"-fx-background-color: #808080; -fx-border-color: #FFFF00; -fx-border-width: 1px;-fx-text-fill: #000000");
		Tab tab7 = new Tab("Report");
		tab7.setStyle(
				"-fx-background-color: #808080; -fx-border-color: #FFFF00; -fx-border-width: 1px;-fx-text-fill: #000000");
		tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5, tab6, tab7);
		Scene tabScene = new Scene(tabPane, 700, 700);
		tabsStage.setTitle("Wholesome Motors");
		tabsStage.setScene(tabScene);
		/////////////////////////////////////////////////////////////////////////////////////////
		// the first tab that allow the user to insert new car brand or
		///////////////////////////////////////////////////////////////////////////////////////// update/delete/search
		///////////////////////////////////////////////////////////////////////////////////////// on
		///////////////////////////////////////////////////////////////////////////////////////// thecar
		///////////////////////////////////////////////////////////////////////////////////////// brand
		///////////////////////////////////////////////////////////////////////////////////////// data
		{
			BorderPane tab1BP = new BorderPane();
			tab1.setContent(tab1BP);
			backGround(tab1BP, "C:\\Users\\sama6\\Downloads\\car1.png");
			Button insert1 = new Button("Insert new car brand.");
			Button update1 = new Button("Update a pre-existing car brand.");
			Button delete1 = new Button("Delete a pre-existing car brand.");
			Button search1 = new Button("Search for a car brand.");
			insert1.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			insert1.setMaxWidth(400);
			insert1.setFont(new Font("Arial", 15));
			update1.setMaxWidth(400);
			update1.setFont(new Font("Arial", 15));
			update1.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			delete1.setMaxWidth(400);
			delete1.setFont(new Font("Arial", 15));
			delete1.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			search1.setMaxWidth(400);
			search1.setFont(new Font("Arial", 15));
			search1.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			VBox buttonVB = new VBox();
			buttonVB.setAlignment(Pos.CENTER);
			buttonVB.setSpacing(20);
			buttonVB.getChildren().addAll(insert1, update1, delete1, search1);
			tab1BP.setLeft(buttonVB);

			// an action handler to insert a new car into the list after checking if it
			// does not exist
			insert1.setOnAction(e -> {

				update1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				delete1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				search1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				insert1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #808080; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				TextField tf = new TextField("insert the new brand here...");
				tf.setMaxWidth(300);
				tf.setMinHeight(50);
				Button insertBT = new Button("Insert");
				insertBT.setStyle(
						"-fx-background-color: #FFFF00; -fx-border-color: #ffffff; -fx-border-width: 2px;-fx-text-fill: #000000");

				insertBT.setFont(new Font("Arial", 20));
				VBox insertVB = new VBox();
				insertVB.setSpacing(10);
				insertVB.getChildren().addAll(tf, insertBT);
				insertVB.setAlignment(Pos.CENTER);
				tab1BP.setCenter(insertVB);
				insertBT.setOnAction(m -> {
					if (brand.search(tf.getText())) {
						dialog(AlertType.ERROR, "Sorry, the brand you are trying to add already exists!");

					} else {
						brand.addFirst(tf.getText(), new LinkedList());
						// sort the linked list after adding a new location
						brand.sort();
						dialog(AlertType.INFORMATION, "The brand have been added successfully.");
					}

				});
			});
			// an action handler to update an existing brand after checking if it does
			// exist
			update1.setOnAction(e -> {
				update1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #808080; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				delete1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				search1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				insert1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				ArrayList<String> brands = new ArrayList<>();
				DoubleNode cur = brand.getfirst();
				while (cur != null) {
					brands.add(cur.getBrand());
					cur = cur.next;
				}
				ComboBox<String> comb = new ComboBox<>();
				comb.setMaxWidth(150);
				for (int i = 0; i < brands.size(); i++) {
					comb.getItems().add(brands.get(i));
				}
				comb.setOnAction(m -> {
					DoubleNode curr = brand.getfirst();
					brands.clear();
					while (curr != null) {
						brands.add(curr.getBrand());
						curr = curr.next;
					}
					comb.getItems().removeAll();
					for (int i = 0; i < brands.size(); i++) {
						comb.getItems().set(i, brands.get(i));
					}
				});
				TextField newTF = new TextField("insert the new brand here...");
				newTF.setMaxWidth(300);
				newTF.setMinHeight(50);
				Button updateBT = new Button("Update");
				updateBT.setFont(new Font("Arial", 20));
				updateBT.setStyle(
						"-fx-background-color: #FFFF00; -fx-border-color: #ffffff; -fx-border-width: 2px;-fx-text-fill: #000000");
				VBox updateVB = new VBox();
				updateVB.setSpacing(10);
				updateVB.getChildren().addAll(comb, newTF, updateBT);
				updateVB.setAlignment(Pos.CENTER);
				tab1BP.setCenter(updateVB);
				updateBT.setOnAction(m -> {
					if (brand.search(comb.getValue())) {
						brand.update(brand.getNode(comb.getValue()), newTF.getText());
						System.out.print(brand.getfirst().getBrand());
						for (int i = 0; i < cars.size(); i++) {
							if (cars.get(i).getBrand().compareTo(comb.getValue()) == 0) {
								cars.get(i).setBrand(newTF.getText());
							}
						}
						// sort the linked list after updating the location
						brand.sort();
						dialog(AlertType.INFORMATION, "The brand have been updated successfully.");
					} else {
						dialog(AlertType.ERROR, "Sorry, the brand you are trying to update does not exist!");
					}
				});
			});

			// this action to delete an existing brand
			delete1.setOnAction(e -> {
				update1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				delete1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #808080; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				search1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				insert1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				ArrayList<String> brands = new ArrayList<>();
				DoubleNode cur = brand.getfirst();
				while (cur != null) {
					brands.add(cur.getBrand());
					cur = cur.next;
				}
				ComboBox<String> comb = new ComboBox<>();
				comb.setMaxWidth(150);
				for (int i = 0; i < brands.size(); i++) {
					comb.getItems().add(brands.get(i));
				}
				comb.setOnAction(m -> {
					DoubleNode curr = brand.getfirst();
					brands.clear();
					while (curr != null) {
						brands.add(curr.getBrand());
						curr = curr.next;
					}
					comb.getItems().removeAll();
					for (int i = 0; i < brands.size(); i++) {
						comb.getItems().set(i, brands.get(i));
					}
				});
				Button deleteBT = new Button("Delete");
				deleteBT.setFont(new Font("Arial", 20));
				deleteBT.setStyle(
						"-fx-background-color: #FFFF00; -fx-border-color: #ffffff; -fx-border-width: 2px;-fx-text-fill: #000000");
				VBox deleteVB = new VBox();
				deleteVB.setSpacing(10);
				deleteVB.getChildren().addAll(comb, deleteBT);
				deleteVB.setAlignment(Pos.CENTER);
				tab1BP.setCenter(deleteVB);
				deleteBT.setOnAction(m -> {
					if (!(brand.search(comb.getValue()))) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Dialog");
						alert.setHeaderText("");
						alert.setContentText("Sorry, the brand you are trying to delete does not exist!");
						alert.showAndWait();
					} else {
						brand.remove(comb.getValue());
						for (int i = 0; i < cars.size(); i++) {
							if (cars.get(i).getBrand().equals(comb.getValue())) {
								cars.remove(cars.get(i));
							}

							if (cars.get(i).getBrand().compareTo(comb.getValue()) == 0) {
								cars.remove(cars.get(i));
							}
						}
						dialog(AlertType.INFORMATION, "The brand have been deleted successfully.");
					}
				});

			});

			// action to search for a specific brand
			search1.setOnAction(e -> {
				insert1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				update1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				delete1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				search1.setStyle(
						"-fx-background-color: #000000; -fx-border-color: #808080; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				TextField tf = new TextField("insert the brand you are searching for here...");
				tf.setMaxWidth(300);
				tf.setMinHeight(50);
				Button searchBT = new Button("Search");
				searchBT.setFont(new Font("Arial", 20));
				searchBT.setStyle(
						"-fx-background-color: #FFFF00; -fx-border-color: #ffffff; -fx-border-width: 2px;-fx-text-fill: #000000");
				VBox searchVB = new VBox();
				searchVB.setSpacing(10);
				searchVB.getChildren().addAll(tf, searchBT);
				searchVB.setAlignment(Pos.CENTER);
				tab1BP.setCenter(searchVB);
				searchBT.setOnAction(m -> {
					if (!(brand.search(tf.getText()))) {
						dialog(AlertType.ERROR, "Sorry, the brand you are searching for does not exist!");

					} else {
						dialog(AlertType.INFORMATION, "The brand have been founded.");
						tabPane.getSelectionModel().select(tab2);
					}
				});
			});
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////
		// the 2nd tab to insert a new car recored
		{
			BorderPane tab2BP = new BorderPane();
			tab2.setContent(tab2BP);
			backGround(tab2BP, "C:\\Users\\sama6\\Downloads\\car2.png");
			Button insert2 = new Button("Insert a new car recored");
			insert2.setFont(new Font("Arial", 30));
			VBox buttonVB = new VBox();
			buttonVB.setAlignment(Pos.CENTER);
			buttonVB.setSpacing(20);
			buttonVB.getChildren().addAll(insert2);
			tab2BP.setCenter(buttonVB);
			insert2.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			insert2.setOnAction(e -> {
				ArrayList<String> brands = new ArrayList<>();
				DoubleNode cur = brand.getfirst();
				while (cur != null) {
					brands.add(cur.getBrand());
					cur = cur.next;
				}
				ComboBox<String> comb = new ComboBox<>();
				comb.setMaxWidth(150);
				for (int i = 0; i < brands.size(); i++) {
					comb.getItems().add(brands.get(i));
				}
				comb.setOnAction(m -> {
					comb.getItems().removeAll();
					for (int i = 0; i < brands.size(); i++) {
						comb.getItems().add(brands.get(i));

					}
				});
				TextField modelTF = new TextField("insert the car model here...");
				modelTF.setMaxWidth(300);
				modelTF.setMinHeight(50);
				TextField yearTF = new TextField("insert the car year here...");
				yearTF.setMaxWidth(300);
				yearTF.setMinHeight(50);
				TextField colorTF = new TextField("insert the car color here...");
				colorTF.setMaxWidth(300);
				colorTF.setMinHeight(50);
				TextField priceTF = new TextField("insert the car price here...(ex:200K)");
				priceTF.setMaxWidth(300);
				priceTF.setMinHeight(50);
				Button insertBT = new Button("Insert");
				insertBT.setFont(new Font("Arial", 20));
				insertBT.setStyle(
						"-fx-background-color: #FFFF00; -fx-border-color: #ffffff; -fx-border-width: 2px;-fx-text-fill: #000000");
				buttonVB.getChildren().addAll(comb, modelTF, yearTF, colorTF, priceTF, insertBT);
				insertBT.setOnAction(m -> {
					Cars car = new Cars();
					car.setBrand(comb.getValue());
					car.setModel(modelTF.getText());
					if (isNum(yearTF.getText())) {
						int year = (int) Integer.parseInt(yearTF.getText());
						car.setYear(year);
					} else {
						dialog(AlertType.ERROR, "The year you enterd is not valid .");

					}
					car.setColor(colorTF.getText());
					String price = priceTF.getText().substring(0, priceTF.getText().length() - 1);
					if (isNum(price)) {
						int priceN = (int) Integer.parseInt(price) * 1000;
						car.setPrice(priceN);
					} else {
						dialog(AlertType.ERROR, "The price you enterd is not valid .");

					}
					brand.singleSort();
					if (car.getBrand() != null && car.getModel() != null && car.getYear() != 0000
							&& car.getColor() != null && car.getPrice() != 0) {
						brand.getNode(comb.getValue()).linkedList.addFirst(cars);
						cars.add(car);
						dialog(AlertType.INFORMATION, "The record have been added successfully.");
					} else {
						dialog(AlertType.WARNING, "You have to insert valid data to compleate the insert operation.");
					}

					// sort the double linked list after adding a new record
					brand.sort();
				});

			});

		}
		//////////////////////////////////////////////////////////////////////////
		// the cars table tab that allows the user to delete/ update/ show info for a
		////////////////////////////////////////////////////////////////////////// selected
		////////////////////////////////////////////////////////////////////////// car
		{
			BorderPane tab3BP = new BorderPane();
			Button uploadTable = new Button("Upload table");
			StackPane stt = new StackPane(uploadTable);
			stt.setAlignment(Pos.CENTER);
			uploadTable.setAlignment(Pos.CENTER);
			uploadTable.setFont(new Font("Arial", 20));
			uploadTable.setStyle(
					"-fx-background-color: #FFFF00; -fx-border-color: #ffffff; -fx-border-width: 2px;-fx-text-fill: #000000");
			brandCB.getItems().addAll("AUDI", "BMW", "CADILLAC", "CHEVROLET", "DODGE", "FORD", "HONDA", "HYUNDAI",
					"JAGUAR", "JEEP", "KIA", "LEXUS", "MAZDA", "MERCEDES", "NISSAN", "TOYOTA", "TESLA", "VOLKSWAGEN");
			brandCB.setValue("Brand");
			modelCB.setValue("Model");
			// fill the models according to the brand
			brandCB.setOnAction(m -> {
				String s = brandCB.getValue();
				switch (s) {
				case "AUDI":
					modelCB.getItems().setAll("A3", "A6", "Q3");
					break;
				case "BMW":
					modelCB.getItems().setAll("X3", "X5", "X6", "i3");
					break;
				case "CADILLAC":
					modelCB.getItems().setAll("CT5", "XT4", "Escalade");

					break;
				case "CHEVROLET":
					modelCB.getItems().setAll("Malibu", "Camaro", "Impala", "Corvette");

					break;
				case "DODGE":
					modelCB.getItems().setAll("Challenger", "Durango", "Journey");

					break;
				case "FORD":
					modelCB.getItems().setAll("MUSTANG", "F-150", "Explorer", "Escape");

					break;
				case "HONDA":
					modelCB.getItems().setAll("Civic", "Accord", "Pilot", "Fit");

					break;
				case "HYUNDAI":
					modelCB.getItems().setAll("Tucson", "Sonata", "Santa Fe", "Kona");

					break;
				case "JAGUAR":
					modelCB.getItems().setAll("F-PACE", "XE", "XF");

					break;
				case "JEEP":
					modelCB.getItems().setAll("Wrangler", "Cherokee", "Renegade");

					break;
				case "KIA":
					modelCB.getItems().setAll("Optima", "RIO", "Sportage", "Sorento");

					break;
				case "LEXUS":
					modelCB.getItems().setAll("ES", "RX", "IS", "NX");

					break;
				case "MAZDA":
					modelCB.getItems().setAll("CX-5", "CX-9:", "MX-5 Miata");

					break;
				case "MERCEDES":
					modelCB.getItems().setAll("C300", "C200", "S-Class", "C-Class");

					break;
				case "NISSAN":
					modelCB.getItems().setAll("Altima", "Rogue", "Maxima", "Sentra");

					break;
				case "TOYOTA":
					modelCB.getItems().setAll("Camry", "Corolla", "Corolla");

					break;
				case "TESLA":
					modelCB.getItems().setAll("3", "X");

					break;
				case "VOLKSWAGEN":
					modelCB.getItems().setAll("Golf", "Passat", "Tiguan", "Polo", "Jetta", "Atlas");
					break;

				default:
					break;
				}
			});
			yearCB.getItems().addAll("Befor 2010", "Befor 2015", "Befor 2020", "Befor 2050");
			yearCB.setValue("Year");
			colorCB.getItems().addAll("Black", "Blue", "Brown", "Gold", "Gray", "Green", "Navy", "Orange", "Pink",
					"Purple", "Red", "Silver", "White", "Yellow");
			colorCB.setValue("Color");
			priceCB.getItems().addAll("Less than 100k", "Less than 110k", "Less than 120k", "Less than 130k",
					"Less than 140k", "Less than 150k", "Less than 160k", "Less than 170k", "Less than 180k",
					"Less than 190k", "Less than 200k", "Less than 210k", "Less than 220k", "Less than 230k",
					"Less than 240k", "Less than 250k", "Less than 260k");
			priceCB.setValue("Price");
			HBox comboHB = new HBox(brandCB, modelCB, yearCB, colorCB, priceCB);
			comboHB.setSpacing(5);
			VBox combovb = new VBox();
			combovb.setSpacing(5);
			combovb.getChildren().addAll(comboHB, stt);
			tab3BP.setTop(combovb);
			tab3.setContent(tab3BP);
			uploadTable.setOnAction(m -> {
				tableView.setItems(cars);
				ObservableList<Cars> c = FXCollections.observableArrayList();
				if (brandCB.getValue() != "Brand") {
					for (int i = 0; i < cars.size(); i++) {
						if (cars.get(i).getBrand().equals(brandCB.getValue())) {
							if (!(c.contains(cars.get(i)))) {
								c.add(cars.get(i));
							}
						}
					}
					for (int k = 0; k < c.size(); k++) {
						if (!(cars.get(k).getBrand().equals(brandCB.getValue()))) {
							c.remove(k);
						}
						tableView.setItems(c);
					}
				}
				if (modelCB.getValue() != "Model") {
					for (int i = 0; i < cars.size(); i++) {
						if (cars.get(i).getModel().equals(modelCB.getValue())) {
							if (!(c.contains(cars.get(i)))) {
								c.add(cars.get(i));
							}

						}
					}
					for (int k = 0; k < c.size(); k++) {
						if (!(cars.get(k).getModel().equals(modelCB.getValue()))) {
							c.remove(k);
						}

					}
					tableView.setItems(c);

				}
				if (yearCB.getValue() != "Year") {
					int year = (int) Integer.parseInt(yearCB.getValue().substring(6, yearCB.getValue().length()));
					for (int i = 0; i < cars.size(); i++) {
						if (cars.get(i).getYear() == year) {
							if (!c.contains(cars.get(i))) {
								c.add(cars.get(i));
							}
						}
					}
					for (int k = 0; k < c.size(); k++) {
						if (cars.get(k).getYear() != year) {
							c.remove(k);
						}

					}
					tableView.setItems(c);

				}
				if (colorCB.getValue() != "Color") {
					for (int i = 0; i < cars.size(); i++) {
						if (cars.get(i).getColor().equals(colorCB.getValue())) {
							if (!c.contains(cars.get(i))) {
								c.add(cars.get(i));
							}
						}
					}
					for (int k = 0; k < c.size(); k++) {
						if (!(cars.get(k).getColor().equals(colorCB.getValue()))) {
							c.remove(k);
						}
					}
					tableView.setItems(c);
				}
				if (priceCB.getValue() != "Price") {
					int price = (int) Integer.parseInt(yearCB.getValue().substring(9, yearCB.getValue().length()))
							* 1000;
					for (int i = 0; i < cars.size(); i++) {
						if (cars.get(i).getYear() == price) {
							if (!c.contains(cars.get(i))) {
								c.add(cars.get(i));
							}
						}
					}
					for (int k = 0; k < c.size(); k++) {
						if (cars.get(k).getPrice() != price) {
							c.remove(k);
						}
					}
					tableView.setItems(c);
				}
				TableColumn<Cars, String> column1 = new TableColumn<>("Brand");
				column1.setCellValueFactory(new PropertyValueFactory<Cars, String>("brand"));
				TableColumn<Cars, String> column2 = new TableColumn<>("Model");
				column2.setCellValueFactory(new PropertyValueFactory<Cars, String>("model"));
				TableColumn<Cars, Integer> column3 = new TableColumn<>("Year");
				column3.setCellValueFactory(new PropertyValueFactory<>("year"));
				TableColumn<Cars, String> column4 = new TableColumn<>("Color");
				column4.setCellValueFactory(new PropertyValueFactory<Cars, String>("color"));
				TableColumn<Cars, Integer> column5 = new TableColumn<>("Price");
				column5.setCellValueFactory(new PropertyValueFactory<Cars, Integer>("price"));
				TableColumn<Cars, Void> deleteColumn = new TableColumn<>("Delete");
				deleteColumn.setCellFactory(param -> {
					TableCell<Cars, Void> cell = new TableCell<>() {
						private final Button deleteBT = new Button("X");

						{
							deleteBT.setAlignment(Pos.CENTER);
							deleteBT.setStyle(
									"-fx-background-color: #000000; -fx-border-color: #000000; -fx-border-width: 2px;-fx-text-fill: #FF0000");
							// delete the selected car from the linked list
							deleteBT.setOnAction(event -> {
								Cars car = getTableView().getItems().get(getIndex());
								brand.getNode(car.getBrand()).linkedList.remove(car);
								cars.remove(car);
								dialog(AlertType.INFORMATION, "The car recored has been deleted successfully");
								tableView.setItems(cars);

							});
						}

						@Override
						protected void updateItem(Void item, boolean empty) {
							super.updateItem(item, empty);
							if (empty) {
								setGraphic(null);
							} else {
								setGraphic(deleteBT);
							}
						}
					};
					return cell;
				});
				TableColumn<Cars, Void> infoColumn = new TableColumn<>("Info");
				infoColumn.setCellFactory(param -> {
					TableCell<Cars, Void> cell = new TableCell<>() {

						private final Button infoBT = new Button("Info");

						{
							infoBT.setAlignment(Pos.CENTER);
							infoBT.setStyle(
									"-fx-background-color: #000000; -fx-border-color: #000000; -fx-border-width: 2px;-fx-text-fill: #ffffff");
							// show the selected car info
							infoBT.setOnAction(event -> {
								Stage infoS = new Stage();
								car = getTableView().getItems().get(getIndex());
								b = car.getBrand();
								TextArea infoTa = new TextArea();
								Button uploadInfo = new Button("Upload info");
								uploadInfo.setFont(new Font("Arial", 20));
								uploadInfo.setMaxWidth(200);
								Button nextBT = new Button("         Next             ");
								nextBT.setFont(new Font("Arial", 20));
								nextBT.setStyle(
										"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
								nextBT.setMaxWidth(uploadInfo.getMaxWidth());
								nextBT.setAlignment(Pos.TOP_RIGHT);
								uploadInfo.setStyle(
										"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
								ImageView imageView = new ImageView();
								Button orderBT = new Button("order this car.");
								orderBT.setFont(new Font("Arial", 20));
								orderBT.setStyle(
										"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
								orderBT.setAlignment(Pos.TOP_RIGHT);
								VBox dataVB = new VBox();
								dataVB.getChildren().addAll(nextBT, uploadInfo, imageView);
								dataVB.setAlignment(Pos.CENTER);
								BorderPane infoBP = new BorderPane();
								VBox VB = new VBox();
								VB.getChildren().addAll(orderBT, infoTa);
								VB.setAlignment(Pos.CENTER);
								infoBP.setBottom(VB);
								infoBP.setCenter(dataVB);
								Scene infoSc = new Scene(infoBP, 700, 700);
								backGround(infoBP, "C:\\Users\\sama6\\Downloads\\black.png");
								infoS.setTitle("Information");
								infoS.setScene(infoSc);
								infoS.show();
								// an action to upload the car info
								uploadInfo.setOnAction(m -> {
									infoTa.setFont(new Font("Arial", 28));
									infoTa.setBackground(new Background(
											new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

									infoTa.setText("Brand: " + car.getBrand() + "\n" + "Model: " + car.getModel() + "\n"
											+ "Year: " + car.getYear() + "\n" + "Color: " + car.getColor() + "\n"
											+ "Price: " + car.getPrice() + "\n");
									Image image = new Image(car.getUrl());
									imageView.setImage(image);
									imageView.setPreserveRatio(true);
									imageView.setFitWidth(400);
									imageView.setFitHeight(300);
								});
								// an action to show the next car info
								nextBT.setOnAction(e -> {
									infoTa.setText("");
									b = car.getBrand();
									if (brand.getNode(b).linkedList.getNode(car).next != null) {
										car = (Cars) brand.getNode(b).linkedList.getNode(car).next.element;
									} else {
										if (brand.getNode(b).next != null) {
											car = (Cars) brand.getNode(b).next.linkedList.getfirst().element;
										} else {
											dialog(AlertType.ERROR, "There is no next location");

										}
									}

								});
								// the order button show a new stage that allow the user to make a new order
								// with the selected car
								orderBT.setOnAction(m -> {
									infoS.close();
									Stage orderS = new Stage();
									BorderPane orderBP = new BorderPane();
									Scene orderSC = new Scene(orderBP, 700, 700);
									orderS.setScene(orderSC);
									orderS.show();
									orderS.setTitle("New order");
									backGround(orderBP, "C:\\Users\\sama6\\Downloads\\car3.png");
									Label brandl = new Label("    Brand:    ");
									brandl.setFont(new Font(40));
									brandl.setStyle("-fx-text-fill: #ffffff");
									TextField brandTF = new TextField(car.getBrand());
									brandTF.setEditable(false);
									brandTF.setMaxWidth(300);
									brandTF.setMinHeight(50);
									HBox brandHB = new HBox();
									brandHB.setAlignment(Pos.CENTER);
									brandHB.setSpacing(5);
									brandHB.getChildren().addAll(brandl, brandTF);
									Label modell = new Label("    Model:   ");
									modell.setFont(new Font(40));
									modell.setStyle("-fx-text-fill: #ffffff");
									TextField modelTF = new TextField(car.getModel());
									modelTF.setEditable(false);
									modelTF.setMaxWidth(300);
									modelTF.setMinHeight(50);
									HBox modelHB = new HBox();
									modelHB.setAlignment(Pos.CENTER);
									modelHB.setSpacing(5);
									modelHB.getChildren().addAll(modell, modelTF);
									Label yearl = new Label("    Year:      ");
									yearl.setFont(new Font(40));
									yearl.setStyle("-fx-text-fill: #ffffff");
									TextField yearTF = new TextField(car.getYear() + "");
									yearTF.setEditable(false);
									yearTF.setMaxWidth(300);
									yearTF.setMinHeight(50);
									HBox yearHB = new HBox();
									yearHB.setAlignment(Pos.CENTER);
									yearHB.setSpacing(5);
									yearHB.getChildren().addAll(yearl, yearTF);
									Label colorl = new Label("     Color:   ");
									colorl.setFont(new Font(40));
									colorl.setStyle("-fx-text-fill: #ffffff");
									TextField colorTF = new TextField(car.getColor());
									colorTF.setEditable(false);
									colorTF.setMaxWidth(300);
									colorTF.setMinHeight(50);
									HBox colorHB = new HBox();
									colorHB.setAlignment(Pos.CENTER);
									colorHB.setSpacing(5);
									colorHB.getChildren().addAll(colorl, colorTF);
									Label pricel = new Label("    Price:     ");
									pricel.setFont(new Font(40));
									pricel.setStyle("-fx-text-fill: #ffffff");
									TextField priceTF = new TextField(car.getPrice() + "");
									priceTF.setEditable(false);
									priceTF.setMaxWidth(300);
									priceTF.setMinHeight(50);
									HBox priceHB = new HBox();
									priceHB.setAlignment(Pos.CENTER);
									priceHB.setSpacing(5);
									priceHB.getChildren().addAll(pricel, priceTF);
									Label namel = new Label("Customer name:  ");
									namel.setFont(new Font(40));
									namel.setStyle("-fx-text-fill: #ffffff");
									TextField nameTF = new TextField();
									nameTF.setMaxWidth(300);
									nameTF.setMinHeight(50);
									HBox nameHB = new HBox();
									nameHB.setAlignment(Pos.CENTER);
									nameHB.setSpacing(5);
									nameHB.getChildren().addAll(namel, nameTF);
									Label phonel = new Label("Customer mobile:");
									phonel.setFont(new Font(40));
									phonel.setStyle("-fx-text-fill: #ffffff");
									TextField phoneTF = new TextField();
									phoneTF.setMaxWidth(300);
									phoneTF.setMinHeight(50);
									HBox phoneHB = new HBox();
									phoneHB.setAlignment(Pos.CENTER);
									phoneHB.setSpacing(5);
									phoneHB.getChildren().addAll(phonel, phoneTF);
									Label datel = new Label("  Order Date: ");
									datel.setFont(new Font(40));
									datel.setStyle("-fx-text-fill: #ffffff");
									TextField dateTF = new TextField();
									dateTF.setMaxWidth(300);
									dateTF.setMinHeight(50);
									HBox dateHB = new HBox();
									dateHB.setAlignment(Pos.CENTER);
									dateHB.setSpacing(5);
									dateHB.getChildren().addAll(datel, dateTF);
									Button OrderBT = new Button("Add this car to orders list.");
									OrderBT.setFont(new Font("Arial", 20));
									OrderBT.setStyle(
											"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
									VBox vb = new VBox();
									vb.setSpacing(10);
									vb.setAlignment(Pos.CENTER);
									vb.getChildren().addAll(brandHB, modelHB, yearHB, colorHB, priceHB, dateHB, nameHB,
											phoneHB, OrderBT);
									orderBP.setCenter(vb);
									//
									OrderBT.setOnAction(o -> {
										if (nameTF != null && phoneTF.getText() != null && dateTF.getText() != null) {
											if ((isPhoneNum(phoneTF.getText())) && isDate(dateTF.getText())) {
												int phone = (int) Integer.parseInt(phoneTF.getText());
												Date date = null;
												try {
													date = new SimpleDateFormat("dd/MM/yyyy").parse(dateTF.getText());
												} catch (ParseException e1) {
												}
												InProcessO.enQueue(
														new Orders(nameTF.getText(), phone, car, date, "InProcess"));
												dialog(AlertType.INFORMATION,
														"The order have been add to the orders list.");
												orderS.close();
											}
										}
									});

								});

							});
						}

						@Override
						protected void updateItem(Void item, boolean empty) {
							super.updateItem(item, empty);
							if (empty) {
								setGraphic(null);
							} else {
								setGraphic(infoBT);
							}
						}
					};
					return cell;
				});

				TableColumn<Cars, Void> UpdateColumn = new TableColumn<>("Update");
				UpdateColumn.setCellFactory(param -> {
					TableCell<Cars, Void> cell = new TableCell<>() {

						private final Button updateBT = new Button("Update");

						{
							updateBT.setAlignment(Pos.CENTER);
							updateBT.setStyle(
									"-fx-background-color: #000000; -fx-border-color: #000000; -fx-border-width: 2px;-fx-text-fill: #008000");
							// this action update the selected car recored
							updateBT.setOnAction(event -> {
								Label updatel = new Label("Update car recored");
								StackPane pp = new StackPane(updatel);
								pp.setAlignment(Pos.CENTER);
								Stage updateS = new Stage();
								updateS.setTitle("Update");
								updatel.setFont(new Font("Arial", 50));
								updatel.setStyle(
										"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
								BorderPane updateBP = new BorderPane();
								updateBP.setTop(pp);
								Cars car = getTableView().getItems().get(getIndex());
								VBox updateVB = new VBox();
								updateBP.setCenter(updateVB);
								Button updateBT1 = new Button("Update");
								updateBT1.setFont(new Font("Arial", 20));
								CheckBox brandCB = new CheckBox("Update brand");
								brandCB.setFont(new Font("Arial", 15));
								CheckBox modelCB = new CheckBox("Update model");
								modelCB.setFont(new Font("Arial", 15));
								CheckBox yearCB = new CheckBox("Update year");
								yearCB.setFont(new Font("Arial", 15));
								CheckBox colorCB = new CheckBox("Update color");
								colorCB.setFont(new Font("Arial", 15));
								CheckBox priceCB = new CheckBox("Update price");
								priceCB.setFont(new Font("Arial", 15));
								TextField newBrandTF = new TextField("Insert the car brand here..");
								newBrandTF.setMaxWidth(300);
								newBrandTF.setMinHeight(40);
								TextField modelTF = new TextField("insert the car model here...");
								modelTF.setMaxWidth(300);
								modelTF.setMinHeight(40);
								TextField yearTF = new TextField("insert the car year here...");
								yearTF.setMaxWidth(300);
								yearTF.setMinHeight(40);
								TextField colorTF = new TextField("insert the car color here...");
								colorTF.setMaxWidth(300);
								colorTF.setMinHeight(40);
								TextField priceTF = new TextField("insert the car price here...");
								priceTF.setMaxWidth(300);
								priceTF.setMinHeight(40);
								HBox cbHB = new HBox();
								cbHB.getChildren().addAll(brandCB, modelCB, yearCB, colorCB, priceCB);
								cbHB.setSpacing(5);
								cbHB.setAlignment(Pos.CENTER);
								Pane p = new Pane();
								p.setBackground(new Background(
										new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
								p.getChildren().add(cbHB);
								Scene updateSc = new Scene(updateBP, 700, 700);
								backGround(updateBP, "C:\\Users\\sama6\\Downloads\\car1.png");
								updateS.setScene(updateSc);
								updateS.show();
								updateVB.setSpacing(20);
								updateVB.getChildren().addAll(cbHB, updateBT1);
								updateVB.setAlignment(Pos.CENTER);
								// see if the model combo box is selected to give the user a text box to update
								// it
								modelCB.setOnAction(h -> {
									if (modelCB.isSelected()) {
										updateVB.getChildren().add(modelTF);
										updateBT1.setOnAction(m -> {
											((Cars) brand.getNode(car.getBrand()).linkedList.getNode(car).element)
													.setModel(modelTF.getText());
											car.setModel(modelTF.getText());
											tableView.setItems(cars);
											dialog(AlertType.INFORMATION, "The record have been updated successfully.");
										});
									} else {
										updateVB.getChildren().removeAll(modelTF);
									}
								});
								// see if the year combo box is selected to give the user a text box to update
								// it
								yearCB.setOnAction(o -> {
									if (yearCB.isSelected()) {
										updateVB.getChildren().add(yearTF);
										updateBT1.setOnAction(m -> {
											if (isNum(yearTF.getText())) {
												int year = (int) Integer.parseInt(yearTF.getText());
												((Cars) brand.getNode(car.getBrand()).linkedList.getNode(car).element)
														.setYear(year);
												car.setYear(year);
												tableView.setItems(cars);

												dialog(AlertType.INFORMATION,
														"The record have been updated successfully.");
											} else {
												dialog(AlertType.WARNING,
														"You have to insert valid age to compleate the update operation.");
											}
										});

									} else {
										updateVB.getChildren().removeAll(yearTF);

									}
								});
								// see if the color combo box is selected to give the user a text box to update
								// it
								colorCB.setOnAction(d -> {
									if (colorCB.isSelected()) {
										updateVB.getChildren().add(colorTF);
										updateBT1.setOnAction(m -> {
											((Cars) brand.getNode(car.getBrand()).linkedList.getNode(car).element)
													.setColor(colorTF.getText());
											car.setColor(colorTF.getText());
											tableView.setItems(cars);
											dialog(AlertType.INFORMATION, "The record have been updated successfully.");
										});
									} else {
										updateVB.getChildren().removeAll(colorTF);
									}
								});
								// see if the price combo box is selected to give the user a text box to update
								// it
								priceCB.setOnAction(w -> {
									if (priceCB.isSelected()) {
										updateVB.getChildren().add(priceTF);
										updateBT1.setOnAction(m -> {
											if (isNum(yearTF.getText())) {
												int price = (int) Integer.parseInt(
														priceTF.getText().substring(0, priceTF.getText().length() - 1))
														* 1000;
												((Cars) brand.getNode(car.getBrand()).linkedList.getNode(car).element)
														.setYear(price);
												car.setPrice(price);
												tableView.setItems(cars);
												dialog(AlertType.INFORMATION,
														"The record have been updated successfully.");
											} else {
												dialog(AlertType.WARNING,
														"You have to insert valid age to compleate the update operation.");
											}
										});

									} else {
										updateVB.getChildren().removeAll(priceTF);

									}
								});
								// see if the brand combo box is selected to give the user a text box to update
								// it
								brandCB.setOnAction(a -> {
									if (brandCB.isSelected()) {
										updateVB.getChildren().add(newBrandTF);
										updateBT1.setOnAction(m -> {
											temp = (Cars) brand.getNode(car.getBrand()).linkedList.getNode(car).element;
											brand.getNode(car.getBrand()).linkedList.remove(temp);
											if (brand.search(newBrandTF.getText())) {
												brand.getNode(car.getBrand()).linkedList.addFirst(temp);
												((Cars) brand.getNode(car.getBrand()).linkedList.getNode(car).element)
														.setBrand(newBrandTF.getText());
												car.setBrand(newBrandTF.getText());
												tableView.setItems(cars);

												dialog(AlertType.INFORMATION,
														"The record have been updated successfully.");

											} else {
												brand.addFirst(newBrandTF.getText(), new LinkedList());
												brand.getNode(newBrandTF.getText()).linkedList.addFirst(temp);
												((Cars) brand.getNode(car.getBrand()).linkedList.getNode(car).element)
														.setBrand(newBrandTF.getText());
												car.setBrand(newBrandTF.getText());
												tableView.setItems(cars);

												dialog(AlertType.INFORMATION,
														"The record have been updated successfully.");

											}
										});
									} else {
										updateVB.getChildren().removeAll(newBrandTF);

									}

								});

							});
						}

						@Override
						protected void updateItem(Void item, boolean empty) {
							super.updateItem(item, empty);
							if (empty) {
								setGraphic(null);
							} else {
								setGraphic(updateBT);
							}
						}
					};
					return cell;
				});
				tableView.getColumns().setAll(column1, column2, column3, column4, column5, deleteColumn, UpdateColumn,
						infoColumn);
				tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY);
				tab3BP.setCenter(tableView);

			});

		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// the forth tab that allows to add a new order for a non existing car
		{
			BorderPane tab4BP = new BorderPane();
			backGround(tab4BP, "C:\\Users\\sama6\\Downloads\\car3.png");
			Label brandl = new Label("    Brand:    ");
			brandl.setFont(new Font(40));
			brandl.setStyle("-fx-text-fill: #ffffff");
			TextField brandTF = new TextField();
			brandTF.setMaxWidth(300);
			brandTF.setMinHeight(50);
			HBox brandHB = new HBox();
			brandHB.setAlignment(Pos.CENTER);
			brandHB.setSpacing(5);
			brandHB.getChildren().addAll(brandl, brandTF);
			Label modell = new Label("    Model:   ");
			modell.setFont(new Font(40));
			modell.setStyle("-fx-text-fill: #ffffff");
			TextField modelTF = new TextField();
			modelTF.setMaxWidth(300);
			modelTF.setMinHeight(50);
			HBox modelHB = new HBox();
			modelHB.setAlignment(Pos.CENTER);
			modelHB.setSpacing(5);
			modelHB.getChildren().addAll(modell, modelTF);
			Label yearl = new Label("    Year:      ");
			yearl.setFont(new Font(40));
			yearl.setStyle("-fx-text-fill: #ffffff");
			TextField yearTF = new TextField();
			yearTF.setMaxWidth(300);
			yearTF.setMinHeight(50);
			HBox yearHB = new HBox();
			yearHB.setAlignment(Pos.CENTER);
			yearHB.setSpacing(5);
			yearHB.getChildren().addAll(yearl, yearTF);
			Label colorl = new Label("     Color:   ");
			colorl.setFont(new Font(40));
			colorl.setStyle("-fx-text-fill: #ffffff");
			TextField colorTF = new TextField();
			colorTF.setMaxWidth(300);
			colorTF.setMinHeight(50);
			HBox colorHB = new HBox();
			colorHB.setAlignment(Pos.CENTER);
			colorHB.setSpacing(5);
			colorHB.getChildren().addAll(colorl, colorTF);
			Label pricel = new Label("    Price:     ");
			pricel.setFont(new Font(40));
			pricel.setStyle("-fx-text-fill: #ffffff");
			TextField priceTF = new TextField();
			priceTF.setMaxWidth(300);
			priceTF.setMinHeight(50);
			HBox priceHB = new HBox();
			priceHB.setAlignment(Pos.CENTER);
			priceHB.setSpacing(5);
			priceHB.getChildren().addAll(pricel, priceTF);
			Label namel = new Label("Customer name:  ");
			namel.setFont(new Font(40));
			namel.setStyle("-fx-text-fill: #ffffff");
			TextField nameTF = new TextField();
			nameTF.setMaxWidth(300);
			nameTF.setMinHeight(50);
			HBox nameHB = new HBox();
			nameHB.setAlignment(Pos.CENTER);
			nameHB.setSpacing(5);
			nameHB.getChildren().addAll(namel, nameTF);
			Label phonel = new Label("Customer mobile:");
			phonel.setFont(new Font(40));
			phonel.setStyle("-fx-text-fill: #ffffff");
			TextField phoneTF = new TextField();
			phoneTF.setMaxWidth(300);
			phoneTF.setMinHeight(50);
			HBox phoneHB = new HBox();
			phoneHB.setAlignment(Pos.CENTER);
			phoneHB.setSpacing(5);
			phoneHB.getChildren().addAll(phonel, phoneTF);
			Label datel = new Label("  Order Date: ");
			datel.setFont(new Font(40));
			datel.setStyle("-fx-text-fill: #ffffff");
			TextField dateTF = new TextField();
			dateTF.setMaxWidth(300);
			dateTF.setMinHeight(50);
			HBox dateHB = new HBox();
			dateHB.setAlignment(Pos.CENTER);
			dateHB.setSpacing(5);
			dateHB.getChildren().addAll(datel, dateTF);
			Button OrderBT = new Button("Add this car to orders list.");
			OrderBT.setFont(new Font("Arial", 20));
			OrderBT.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			VBox vb1 = new VBox();
			vb1.setSpacing(10);
			vb1.setAlignment(Pos.CENTER);
			vb1.getChildren().addAll(brandHB, modelHB, yearHB, colorHB, priceHB, dateHB, nameHB, phoneHB, OrderBT);
			tab4BP.setCenter(vb1);
			tab4.setContent(tab4BP);
			// if all the data is valid this action will add the order to the queue
			OrderBT.setOnAction(o -> {
				if (brandTF.getText() != null && modelTF.getText() != null && yearTF.getText() != null
						&& colorTF.getText() != null && priceTF.getText() != null && nameTF != null
						&& phoneTF.getText() != null && dateTF.getText() != null) {
					if ((isPhoneNum(phoneTF.getText())) && isDate(dateTF.getText()) && isNum(yearTF.getText())
							&& isNum(priceTF.getText().substring(0, priceTF.getText().length() - 1))) {
						int phone = (int) Integer.parseInt(phoneTF.getText());
						Date date = null;
						int year = (int) Integer.parseInt(yearTF.getText());
						int price = (int) Integer
								.parseInt(priceTF.getText().substring(0, priceTF.getText().length() - 1)) * 1000;
						Cars c = new Cars(brandTF.getText(), modelTF.getText(), year, colorTF.getText(), price);
						try {
							date = new SimpleDateFormat("dd/MM/yyyy").parse(dateTF.getText());
						} catch (ParseException e1) {
						}
						InProcessO.enQueue(new Orders(nameTF.getText(), phone, c, date, "InProcess"));
						dialog(AlertType.INFORMATION, "The order have been add to the orders list.");

					}
				}
			});
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// the fifth tab that allow the user to approve /cancel/reset the in process
		//////////////////////////////////////////////////////////////////////////////////////////////////// orders
		{
			BorderPane tab5BP = new BorderPane();
			backGround(tab5BP, "C:\\Users\\sama6\\Downloads\\car3.png");
			Button uploadOrder = new Button("Upload the order");
			VBox vb1 = new VBox();
			vb1.setSpacing(10);
			vb1.setAlignment(Pos.CENTER);
			vb1.getChildren().addAll(uploadOrder);
			Label brandl = new Label("    Brand:    ");
			brandl.setFont(new Font(20));
			brandl.setStyle("-fx-text-fill: #ffffff");
			TextField brandTF = new TextField();
			brandTF.setEditable(false);
			brandTF.setMaxWidth(300);
			brandTF.setMinHeight(30);
			HBox brandHB = new HBox();
			brandHB.setAlignment(Pos.CENTER);
			brandHB.setSpacing(5);
			brandHB.getChildren().addAll(brandl, brandTF);
			Label modell = new Label("    Model:   ");
			modell.setFont(new Font(20));
			modell.setStyle("-fx-text-fill: #ffffff");
			TextField modelTF = new TextField();
			modelTF.setEditable(false);
			modelTF.setMaxWidth(300);
			modelTF.setMinHeight(30);
			HBox modelHB = new HBox();
			modelHB.setAlignment(Pos.CENTER);
			modelHB.setSpacing(5);
			modelHB.getChildren().addAll(modell, modelTF);
			Label yearl = new Label("    Year:      ");
			yearl.setFont(new Font(20));
			yearl.setStyle("-fx-text-fill: #ffffff");
			TextField yearTF = new TextField();
			yearTF.setEditable(false);
			yearTF.setMaxWidth(300);
			yearTF.setMinHeight(30);
			HBox yearHB = new HBox();
			yearHB.setAlignment(Pos.CENTER);
			yearHB.setSpacing(5);
			yearHB.getChildren().addAll(yearl, yearTF);
			Label colorl = new Label("     Color:   ");
			colorl.setFont(new Font(20));
			colorl.setStyle("-fx-text-fill: #ffffff");
			TextField colorTF = new TextField();
			colorTF.setEditable(false);
			colorTF.setMaxWidth(300);
			colorTF.setMinHeight(30);
			HBox colorHB = new HBox();
			colorHB.setAlignment(Pos.CENTER);
			colorHB.setSpacing(5);
			colorHB.getChildren().addAll(colorl, colorTF);
			Label pricel = new Label("    Price:     ");
			pricel.setFont(new Font(20));
			pricel.setStyle("-fx-text-fill: #ffffff");
			TextField priceTF = new TextField();
			priceTF.setEditable(false);
			priceTF.setMaxWidth(300);
			priceTF.setMinHeight(30);
			HBox priceHB = new HBox();
			priceHB.setAlignment(Pos.CENTER);
			priceHB.setSpacing(5);
			priceHB.getChildren().addAll(pricel, priceTF);
			Label namel = new Label("Customer name:  ");
			namel.setFont(new Font(20));
			namel.setStyle("-fx-text-fill: #ffffff");
			TextField nameTF = new TextField();
			nameTF.setEditable(false);
			nameTF.setMaxWidth(300);
			nameTF.setMinHeight(30);
			HBox nameHB = new HBox();
			nameHB.setAlignment(Pos.CENTER);
			nameHB.setSpacing(5);
			nameHB.getChildren().addAll(namel, nameTF);
			Label phonel = new Label("Customer mobile:");
			phonel.setFont(new Font(20));
			phonel.setStyle("-fx-text-fill: #ffffff");
			TextField phoneTF = new TextField();
			phoneTF.setEditable(false);
			phoneTF.setMaxWidth(300);
			phoneTF.setMinHeight(30);
			HBox phoneHB = new HBox();
			phoneHB.setAlignment(Pos.CENTER);
			phoneHB.setSpacing(5);
			phoneHB.getChildren().addAll(phonel, phoneTF);
			Label datel = new Label("  Order Date: ");
			datel.setFont(new Font(20));
			datel.setStyle("-fx-text-fill: #ffffff");
			TextField dateTF = new TextField();
			dateTF.setEditable(false);
			dateTF.setMaxWidth(300);
			dateTF.setMinHeight(50);
			HBox dateHB = new HBox();
			dateHB.setAlignment(Pos.CENTER);
			dateHB.setSpacing(5);
			dateHB.getChildren().addAll(datel, dateTF);
			Button aproveBT = new Button("Aprove this order.");
			aproveBT.setFont(new Font("Arial", 20));
			tab5BP.setCenter(vb1);
			tab5.setContent(tab5BP);
			aproveBT.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			Button returnBT = new Button("Return this order.");
			returnBT.setFont(new Font("Arial", 20));
			returnBT.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			Button cancelBT = new Button("cancel this order.");
			cancelBT.setFont(new Font("Arial", 20));
			cancelBT.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			HBox btHB = new HBox();
			btHB.setSpacing(5);
			btHB.setAlignment(Pos.CENTER);
			vb1.getChildren().addAll(brandHB, modelHB, yearHB, colorHB, priceHB, dateHB, nameHB, phoneHB, btHB);
			// this button upload the first order in the queue and allow the user
			// approve/cancel/reset the order due to if the car exists in the linked list
			uploadOrder.setOnAction(e -> {
				if (InProcessO.getFront() != null) {
					brandTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getBrand());
					String b = brandTF.getText();
					modelTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getModel());
					String m = modelTF.getText();
					yearTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getYear() + "");
					int y = (int) Integer.parseInt(yearTF.getText());
					colorTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getColor());
					String c = colorTF.getText();
					priceTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getPrice() + "");
					int price = (int) Integer.parseInt(priceTF.getText());
					nameTF.setText(((Orders) InProcessO.getFront().element).getCustomerName());
					phoneTF.setText("0" + ((Orders) InProcessO.getFront().element).getCustomerMobile() + "");
					dateTF.setText(((Orders) InProcessO.getFront().element).getOrderDate() + "");
					Cars car1 = new Cars(b, m, y, c, price);
					if (brand.getNode(b).linkedList.getNode(car1) != null) {
						btHB.getChildren().setAll(aproveBT);
						// if the car exist then the user can now approve the order by this action
						aproveBT.setOnAction(v -> {
							Orders o = InProcessO.deQueue();
							o.setOrderStatus("Finished");
							finishedO.push(o);
							brand.getNode(b).linkedList.remove(car1);
							for (int i = 0; i < cars.size(); i++) {
								if (cars.get(i).compareTo(car1) == 0) {
									cars.remove(i);
								}
							}
							dialog(AlertType.INFORMATION, "Your car is ready to go");
							if (InProcessO.getFront() != null) {
								brandTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getBrand());
								modelTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getModel());
								yearTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getYear() + "");
								colorTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getColor());
								priceTF.setText(
										((Orders) InProcessO.getFront().element).getOrderdCar().getPrice() + "");
								nameTF.setText(((Orders) InProcessO.getFront().element).getCustomerName());
								phoneTF.setText(
										"0" + ((Orders) InProcessO.getFront().element).getCustomerMobile() + "");
								dateTF.setText(((Orders) InProcessO.getFront().element).getOrderDate() + "");
							} else {
								dialog(AlertType.ERROR, "There is no orders left.");
							}
						});

					} else {
						// if the car does not exist then the user can now cancel or reset the order by
						// this action
						btHB.getChildren().setAll(returnBT, cancelBT);
						returnBT.setOnAction(k -> {
							InProcessO.enQueue(InProcessO.deQueue());
							dialog(AlertType.INFORMATION, "The order have been added to the orders list again.");
							if (InProcessO.getFront() != null) {
								brandTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getBrand());
								modelTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getModel());
								yearTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getYear() + "");
								colorTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getColor());
								priceTF.setText(
										((Orders) InProcessO.getFront().element).getOrderdCar().getPrice() + "");
								nameTF.setText(((Orders) InProcessO.getFront().element).getCustomerName());
								phoneTF.setText(
										"0" + ((Orders) InProcessO.getFront().element).getCustomerMobile() + "");
								dateTF.setText(((Orders) InProcessO.getFront().element).getOrderDate() + "");
							} else {
								dialog(AlertType.ERROR, "There is no orders left.");
							}
						});
						cancelBT.setOnAction(l -> {
							InProcessO.deQueue();
							dialog(AlertType.INFORMATION, "The order have been canceled.");
							if (InProcessO.getFront() != null) {
								brandTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getBrand());
								modelTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getModel());
								yearTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getYear() + "");
								colorTF.setText(((Orders) InProcessO.getFront().element).getOrderdCar().getColor());
								priceTF.setText(
										((Orders) InProcessO.getFront().element).getOrderdCar().getPrice() + "");
								nameTF.setText(((Orders) InProcessO.getFront().element).getCustomerName());
								phoneTF.setText(
										"0" + ((Orders) InProcessO.getFront().element).getCustomerMobile() + "");
								dateTF.setText(((Orders) InProcessO.getFront().element).getOrderDate() + "");
							} else {
								dialog(AlertType.ERROR, "There is no orders left.");
							}
						});

					}
				}
			});
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// the six tab that saves the lists, queue, stack to a files
		{
			BorderPane tab6BP = new BorderPane();
			backGround(tab6BP, "C:\\Users\\sama6\\Downloads\\car4.jpeg");
			Button cars = new Button("Save the cars file");
			cars.setFont(new Font("Arial", 30));
			cars.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			Button orders = new Button("Save the orders file");
			orders.setFont(new Font("Arial", 30));
			orders.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			VBox vbB = new VBox();
			Label l = new Label("\n\n\n\n\n\n\n\n");
			vbB.setAlignment(Pos.CENTER);
			vbB.getChildren().addAll(cars, orders, l);
			vbB.setSpacing(50);
			tab6BP.setCenter(vbB);
			tab6.setContent(tab6BP);
			// action to save the cars in a file
			cars.setOnAction(o -> {
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter("cars.txt"));
					String s = brand.print().toString();
					writer.write(s);
					writer.close();
					dialog(AlertType.INFORMATION, "cars records has been written to the file.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			// action to save the orders in a file
			orders.setOnAction(o -> {
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt"));
					String s = InProcessO.printQueue().toString() + finishedO.PrintStack().toString();
					writer.write(s);
					writer.close();
					dialog(AlertType.INFORMATION, "orders records has been written to the file.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		{
			BorderPane tab7BP = new BorderPane();
			tab7.setContent(tab7BP);
			backGround(tab7BP, "C:\\Users\\sama6\\Downloads\\car1.png");
			Button upload = new Button("Upload the last 10 sold cars");
			upload.setFont(new Font("Arial", 30));
			VBox vbR = new VBox();
			vbR.setAlignment(Pos.CENTER);
			vbR.getChildren().add(upload);
			vbR.setSpacing(20);
			tab7BP.setCenter(vbR);
			upload.setStyle(
					"-fx-background-color: #000000; -fx-border-color: #FFFF00; -fx-border-width: 2px;-fx-text-fill: #ffffff");
			upload.setOnAction(m -> {

				Label l = new Label();
				l.setFont(new Font(14));
				l.setStyle("-fx-background-color: #000000; -fx-border-width: 2px;-fx-text-fill: #ffffff");
				l.setText(finishedO.PrintStack10().toString());
				vbR.getChildren().setAll(upload, l);
			});
		}

	}

//////////////////////////////////////////////////////////////////////////////////////
	public void dialog(AlertType t, String s) {
		Alert alert = new Alert(t);
		alert.setTitle("Dialog");
		alert.setHeaderText("");
		alert.setContentText(s);
		alert.showAndWait();
	}

//////////////////////////////////////////////////////////////////////////////////////
	// method to set a background
	private void backGround(Pane p, String url) {
		try {
			BackgroundImage bGI = new BackgroundImage(new Image(url), BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			Background bGround = new Background(bGI);
			p.setBackground(bGround);
		} catch (Exception e) {
			dialog(AlertType.ERROR, "Sorry, there was an error while uploading the background");
		}
	}

	////////////////////////////////////////////////////////////////
	// method to check if a string is a number
	private boolean isNum(String s) {
		if (s == null) {
			return false;
		} else {
			try {
				int year = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				// dialog(AlertType.ERROR, "The num you are tring to insert is unvalid!");
				return false;
			}
			return true;
		}

	}

	/////////////////////////////////////////////////////////
	// method to check if a string is a phoneNumber
	private boolean isPhoneNum(String s) {
		if (s == null) {

			return false;
		}
		if (isNum(s)) {
			int phone = (int) Integer.parseInt(s);
			if (phone > 59999999 && phone < 56000000) {
				dialog(AlertType.ERROR, "The phone number you are trying to add is NOT valid, try another one please.");
				return false;
			} else {
				return true;
			}

		} else {
			dialog(AlertType.ERROR, "The phone number you are trying to add is NOT valid, try another one please.");
			return false;
		}
	}
	////////////////////////////////////////////////////////////
	// method to check if a string is a date

	public boolean isDate(String s) {
		if (s == null) {
			return false;
		} else {
			try {
				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(s);
			} catch (ParseException e) {
				dialog(AlertType.ERROR, "The date you are trying to add is NOT valid, try another one please.");
				return false;
			}
			return true;
		}
	}

}

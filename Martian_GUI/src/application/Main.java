package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import martian_stuff.GreenMartian;
import martian_stuff.Martian;
import martian_stuff.MartianManager;
import martian_stuff.RedMartian;


public class Main extends Application {

	//Summon the Boss.
		MartianManager mm = new MartianManager();

		//file i/o stuff
		File martiansIn = new File( "src\\application\\martiansIn.txt" );
		File martiansOut = new File( "src\\application\\martiansOut.txt");
		MediaPlayer soundFX;
		Scanner feedMe;
		PrintWriter outWithIt;
		FileWriter inkIt;

		//Main button sound
		String buttonClick = "button2.mp3";
		Media click = new Media(new File(buttonClick).toURI().toString());

		//toggle groups
		protected ToggleGroup toggleInteraction = new ToggleGroup();
		protected ToggleGroup toggleRGB = new ToggleGroup();
		protected ToggleGroup toggleESP = new ToggleGroup();
		protected ToggleGroup toggleVeg = new ToggleGroup();

		//panes and tabs
		RadioButton cmdType; //initial input point: A radio button that displays appropriate panes for other interactions
		StackPane inputsPane; //Belongs to root.  Generic pane for the various inputs needed to interact with the Martians
		StackPane commandsPane; //Belongs to root.  Holds selections that call inputPane prompts
		HBox reqInfoPane; //Belongs to root.  Info box and output pane

		VBox managePane; //belongs to commandsPane.  Used to build Martians as individuals or from a file, save them to a file, or remove martians
		VBox makeMartiansInputsPane; //belongs to inputsPane. Is called by managePane. Contains all the input fields to make a single Martian.
		VBox rGBPane; //belongs to makeMartiansInputPane
		VBox eSPPane; //belongs to makeMartiansInputPane
		VBox vegPane; //belongs to makeMartiansInputPane

		VBox killPane; //belongs to managePane, for the various methods of removing Martians
		VBox killInputPane; //belongs to inputsPane. Is called by killPane

		VBox statsPane;  //Belongs to commandsPane.  Used to display the toString from the MartianManager's ArrayList, and a graph
		CheckBox statsOrGraph; //Belongs to the statsPane.  Used to display either the graph or the reqInfo pane.
		HBox chartPane; //Belongs to reqInfoPane, replaces the text field on call
		@SuppressWarnings("rawtypes")
		BarChart martianBarChart;

		TabPane interactPane; //Belongs to commandsPane.  Used to interact with Martians: Find them, teleport them, change their volume and make them speak
		Tab singleInteract; //belongs to interactPane
		HBox singleInteractions; //belongs to singleInteract tab
		VBox findMartianInputPane; //belongs to singleInteractions
		VBox setVolumePane; //belongs to singleInteractions
		Slider setVolume = new Slider(); //belongs to singleInteractions
		ComboBox<String> subjectsBox = new ComboBox<String>(); //belongs to singleInteractions.  Contains an interactive list of Martians.
		Tab groupInteract; //belongs to interactPane
		HBox groupInteractions; //belongs to groupInteract
		VBox teleportInputPane; //belongs to groupInteractions
		VBox groupTeleportInputPane; //belongs to groupInteractions

		//Lists
		ArrayList<Integer> IDsList = new ArrayList<>();
		ObservableList<Integer> EyesOnIDs;

		//text containers
		TextArea reqInfo = new TextArea();
		TextField inputLocation;
		TextField inputgroupLocation;
		TextField inputVolume;
		TextField inputId;
		TextField inputTeleportId;
		TextField inputKillId;
		TextField inputFindId;

		//buttons
		Button loadMinions = new Button("Make Many Martians");
		Button makeMartian = new Button("Make One Martian");
		Button findMartian = new Button("Search for a Martian");
		Button killButton = new Button("Execute Martians");
		Button noTeleporters = new Button("Eradicate Teleporters");
		Button armageddonIt = new Button("Armageddon It!");
		Button hiveSpeak = new Button("Vocalize as One");
		Button teleport = new Button("Teleport");
		Button massTeleport = new Button("Teleport on Masse");
		Button send = new Button("Go!");
		Button sendThem = new Button("Send them all!");
		Button getList = new Button("Display All My Martians");
		Button buildThatMartian = new Button("Create");
		Button showTeleportMenu = new Button("Send them somewhere");
		Button execute = new Button("Execute");
		Button summon = new Button("Summon");
		Button saveMartians = new Button("Store Martians");
		Button showGraph = new Button("Show me a picture.");

		//labels
		Label label1 = new Label();
		Label label2 = new Label();
		Label killLabel = new Label();
		Label findLabel = new Label();
		Label subjectsLabel = new Label();

		//primatives
		int w = 300;
		int h = 200;
		String rgb;
		int id;
		int volume;
		boolean hasESP;
		boolean isVegetarian;

	//set the stage

	public void start(Stage primaryStage) {

		try {
			primaryStage.setTitle("Martian Control Panel");
			Pane root = buildNestedGui();
			Scene scene = new Scene(root,825,550);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//build the root gui
	public Pane buildNestedGui() {
		GridPane root = new GridPane();
		root.getChildren().add(buildCmdType());
		root.add(buildCmdType(),0,0);
		root.add(buildCommandsPane(),1,0);
		root.add(buildInputsPane(),0,1);
		root.add(buildReqInfoPane(),1,1);
		root.add(buildChartPane(), 1, 1);

		//initialize and populate local IDs field immediately
		for(int i=0; i < mm.getNumMartians(); i++){
			IDsList.add(mm.getMartianAt(i).getId());
		}

		return root;
	}

	//build radio button pane from which the user begins interfacing with their martians
	private Pane buildCmdType() {
		VBox vBoxInteract = new VBox();
		vBoxInteract.setMinWidth(300);

		Label lblCmdType = new Label("Choose Command Type");
		vBoxInteract.getChildren().add(lblCmdType);

		ToggleInteractionEventHandler toggleInteractionEventHandler = new ToggleInteractionEventHandler();

		RadioButton rbManage = new RadioButton("Manage");
		rbManage.setSelected(true);
		rbManage.setToggleGroup(toggleInteraction);
		rbManage.setOnAction(toggleInteractionEventHandler);
		vBoxInteract.getChildren().add(rbManage);

		RadioButton rbInteract = new RadioButton("Interact");
		rbInteract.setToggleGroup(toggleInteraction);
		rbInteract.setOnAction(toggleInteractionEventHandler);
		vBoxInteract.getChildren().add(rbInteract);

		RadioButton rbStats = new RadioButton("Stats");
		rbStats.setToggleGroup(toggleInteraction);
		rbStats.setOnAction(toggleInteractionEventHandler);
		vBoxInteract.getChildren().add(rbStats);

		return vBoxInteract;
	}

	//build commands pane
	public StackPane buildCommandsPane(){

		commandsPane = new StackPane();
		commandsPane.setMaxWidth(250);
		commandsPane.setMinWidth(250);
		buildManagePane();
		buildInteractionTabPane();
		buildStatsPane();
		commandsPane.getChildren().addAll(managePane,interactPane,statsPane);
		return commandsPane;
	}

	//build martian management pane
		public VBox buildManagePane(){
			managePane = new VBox();
			managePane.setVisible(true);

			MakeMinionsEventHandler makeMinionsEventHandler = new MakeMinionsEventHandler();
			loadMinions.setOnAction(makeMinionsEventHandler);

			DisplayMakeOneMartianEventHandler displayMakeOneMartianEventHandler = new DisplayMakeOneMartianEventHandler();
			makeMartian.setOnAction(displayMakeOneMartianEventHandler);

			SaveMartiansEventHandler saveMartiansEventHandler = new SaveMartiansEventHandler();
			saveMartians.setOnAction(saveMartiansEventHandler);

			KillMartianInputDisplayEventHandler killMartianInputDisplayEventHandler = new KillMartianInputDisplayEventHandler();
			killButton.setOnAction(killMartianInputDisplayEventHandler);

			managePane.getChildren().addAll(loadMinions,makeMartian,saveMartians, killButton);
			return managePane;
		}

		public VBox buildKillInputPane(){
			killInputPane = new VBox();

			killLabel = new Label("Enter the ID of the condemned Martian");
			killInputPane.getChildren().add(killLabel);
			inputKillId = new TextField();
			killInputPane.getChildren().add(inputKillId);

			KillMartianEventHandler killMartianEventHandler = new KillMartianEventHandler();
			execute.setOnAction(killMartianEventHandler);
			killInputPane.getChildren().add(execute);

			NoTeleportersEventHandler noTeleportersEventHandler = new NoTeleportersEventHandler();
			noTeleporters.setOnAction(noTeleportersEventHandler);
			killInputPane.getChildren().add(noTeleporters);

			ArmageddonItEventHandler armageddonItEventHandler = new ArmageddonItEventHandler();
			armageddonIt.setOnAction(armageddonItEventHandler);
			killInputPane.getChildren().add(armageddonIt);

			return killInputPane;
		}

	//build generic inputs pane (that presents available commands as per user selections)
	public StackPane buildInputsPane(){
		inputsPane = new StackPane();
		inputsPane.setMinWidth(300);

		inputsPane.getChildren().add(buildMakeMartiansInputsPane());
		makeMartiansInputsPane.setVisible(false);

		inputsPane.getChildren().add(buildTeleportInputPane());
		teleportInputPane.setVisible(false);

		inputsPane.getChildren().add(buildGroupTeleportInputPane());

		inputsPane.getChildren().add(buildKillInputPane());
		killInputPane.setVisible(false);

		inputsPane.getChildren().add(buildFindMartianInputPane());
		findMartianInputPane.setVisible(false);

		return inputsPane;
	}

	//build the tab pane for single interactions
	public TabPane buildInteractionTabPane(){
		interactPane = new TabPane();
		interactPane.setMinWidth(400);

		interactPane.setVisible(false);
		interactPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		interactPane.getTabs().add(buildSingleInteractTab());
		interactPane.getTabs().add(buildGroupInteractTab());

		return interactPane;
	}

	//build the text output pane/box
	public HBox buildReqInfoPane(){
		reqInfoPane = new HBox();
		reqInfoPane.setMinWidth(700);
		reqInfo.setEditable(false);
		reqInfo.setWrapText(true);
		reqInfo.clear();
		reqInfoPane.getChildren().add(reqInfo);
		return reqInfoPane;
	}

	//Build the chart pane
	public HBox buildChartPane(){
		chartPane = new HBox();
		chartPane.setMinWidth(700);
		chartPane.setVisible(false);

		return chartPane;
	}


	//build the tabs for the tab pane
	public Tab buildSingleInteractTab(){
		singleInteract = new Tab("Order one Martian");
		singleInteractions = new HBox();

		FindMartianDisplayEventHandler findMartianDisplayEventHandler = new FindMartianDisplayEventHandler();
		findMartian.setOnAction(findMartianDisplayEventHandler);

		DisplayTeleportPaneEventHandler displayTeleportPaneEventHandler = new DisplayTeleportPaneEventHandler();
		teleport.setOnAction(displayTeleportPaneEventHandler);

		singleInteractions.getChildren().addAll(findMartian,subjectsBox,teleport);

		singleInteract.setContent(singleInteractions);

		return singleInteract;

	}

	public Tab buildGroupInteractTab(){
		groupInteract = new Tab("Order all Martians");
		InteractTabSelectionChangedEventHandler interactTabSelectionChangedEventHandler = new InteractTabSelectionChangedEventHandler();
		groupInteract.setOnSelectionChanged(interactTabSelectionChangedEventHandler);
		groupInteractions = new HBox();

		VocalizeAsOneEventHandler vocalizeAsOneEventHandler = new VocalizeAsOneEventHandler();
		hiveSpeak.setOnAction(vocalizeAsOneEventHandler);
		groupInteractions.getChildren().add(hiveSpeak);

		DisplaygroupTeleportInputPaneEventHandler DisplaygroupTeleportInputPaneEventHandler = new DisplaygroupTeleportInputPaneEventHandler();
		massTeleport.setOnAction(DisplaygroupTeleportInputPaneEventHandler);
		groupInteractions.getChildren().add(massTeleport);

		groupInteract.setContent(groupInteractions);

		return groupInteract;
	}

	//build stats pane for grids and other info gathering
	public VBox buildStatsPane(){
		statsPane = new VBox();
		statsPane.setVisible(false);

		statsOrGraph = new CheckBox();
		statsOrGraph.setText("Check to display a graph of your martians by volume.");
		statsOrGraph.setSelected(false);

		SetStatsOrGraphSelectedEventHandler setStatsOrGraphSelectedEventHandler = new SetStatsOrGraphSelectedEventHandler();
		statsOrGraph.setOnAction(setStatsOrGraphSelectedEventHandler);

		ShowMinionsEventHandler showMinionsEventHandler = new ShowMinionsEventHandler();
		getList.setOnAction(showMinionsEventHandler);

		statsPane.getChildren().addAll(getList,statsOrGraph);
		return statsPane;
	}

	//build actions pane
	public VBox buildMakeMartiansInputsPane(){
		makeMartiansInputsPane = new VBox();
		makeMartiansInputsPane.setVisible(true);

		Label label1 = new Label("Choose Red or Green");
		makeMartiansInputsPane.getChildren().add(label1);
		makeMartiansInputsPane.getChildren().add(buildRGBPane());

		Label label2 = new Label("Enter an Integer ID number");
		makeMartiansInputsPane.getChildren().add(label2);
		inputId = new TextField();
		makeMartiansInputsPane.getChildren().add(inputId);

		makeMartiansInputsPane.getChildren().add(buildVolumeSliderBox());

		Label label4 = new Label("Does this Martian have ESP?");
		makeMartiansInputsPane.getChildren().add(label4);
		makeMartiansInputsPane.getChildren().add(buildESPPane());

		Label label5 = new Label("Are they vegetarian?");
		makeMartiansInputsPane.getChildren().add(label5);
		makeMartiansInputsPane.getChildren().add(buildVegPane());

		BuildMartianEventHandler buildMartianEventHandler = new BuildMartianEventHandler();
		buildThatMartian.setOnAction(buildMartianEventHandler);
		makeMartiansInputsPane.getChildren().add(buildThatMartian);

		return makeMartiansInputsPane;
	}

	//VBox to hold the Red or Green RadioButton
	public VBox buildRGBPane(){
		rGBPane = new VBox();

		RGBToggleEventsHandler rGBToggleEventsHandler = new RGBToggleEventsHandler();

		RadioButton rbMakeRed = new RadioButton("Red");
		rbMakeRed.setToggleGroup(toggleRGB);
		rbMakeRed.setOnAction(rGBToggleEventsHandler);
		rbMakeRed.setSelected(true);
		rGBPane.getChildren().add(rbMakeRed);

		RadioButton rbMakeGreen = new RadioButton("Green");
		rbMakeGreen.setToggleGroup(toggleRGB);
		rbMakeGreen.setOnAction(rGBToggleEventsHandler);
		rGBPane.getChildren().add(rbMakeGreen);

		return rGBPane;
	}

	//VBox to hold the ESP RadioButton
	public VBox buildESPPane(){
		eSPPane = new VBox();

		ESPToggleEventsHandler eSPToggleEventsHandler = new ESPToggleEventsHandler();

		RadioButton rbESPYes = new RadioButton("Yes");
		rbESPYes.setToggleGroup(toggleESP);
		rbESPYes.setOnAction(eSPToggleEventsHandler);
		rbESPYes.setSelected(true);
		eSPPane.getChildren().add(rbESPYes);

		RadioButton rbESPNo = new RadioButton("No");
		rbESPNo.setToggleGroup(toggleESP);
		rbESPNo.setOnAction(eSPToggleEventsHandler);
		eSPPane.getChildren().add(rbESPNo);

		eSPPane.getChildren().addAll();
		return eSPPane;
	}

	//VBox to hold the vegetarian RadioButton
	public VBox buildVegPane(){
		vegPane = new VBox();

		VegToggleEventsHandler vegToggleEventsHandler = new VegToggleEventsHandler();

		RadioButton rbVegYes = new RadioButton("Yes");
		rbVegYes.setToggleGroup(toggleVeg);
		rbVegYes.setOnAction(vegToggleEventsHandler);
		vegPane.getChildren().add(rbVegYes);

		RadioButton rbVegNo = new RadioButton("No");
		rbVegNo.setToggleGroup(toggleVeg);
		rbVegNo.setSelected(true);
		rbVegNo.setOnAction(vegToggleEventsHandler);
		vegPane.getChildren().add(rbVegNo);

		vegPane.getChildren().addAll();
		return vegPane;
	}

	public VBox buildTeleportInputPane(){
		teleportInputPane = new VBox();
		teleportInputPane.setVisible(false);

		label1 = new Label("Enter Destination");
		teleportInputPane.getChildren().add(label1);
		inputLocation = new TextField();
		teleportInputPane.getChildren().add(inputLocation);

		label2 = new Label("Enter a Martian ID to send just one");
		teleportInputPane.getChildren().add(label2);
		inputTeleportId = new TextField();
		teleportInputPane.getChildren().add(inputTeleportId);
		teleportInputPane.getChildren().add(send);

		TeleportEventHandler teleportEventHandler = new TeleportEventHandler();
		send.setOnAction(teleportEventHandler);

		return teleportInputPane;
	}

	public VBox buildGroupTeleportInputPane(){
 		groupTeleportInputPane = new VBox();
 		groupTeleportInputPane.setVisible(false);

 		label1 = new Label("Enter Destination");
 		groupTeleportInputPane.getChildren().add(label1);
 		inputgroupLocation = new TextField();
		groupTeleportInputPane.getChildren().add(inputgroupLocation);
		groupTeleportInputPane.getChildren().add(sendThem);

		GroupTeleportEventHandler groupTeleportEventHandler = new GroupTeleportEventHandler();
		sendThem.setOnAction(groupTeleportEventHandler);

 		return groupTeleportInputPane;
 	}


	public VBox buildFindMartianInputPane(){
		findMartianInputPane = new VBox();

		findLabel = new Label("Enter an ID to search for a Martian");
		findMartianInputPane.getChildren().add(findLabel);
		inputFindId = new TextField();
		findMartianInputPane.getChildren().add(inputFindId);

		FindMartianEventHandler findMartianEventHandler = new FindMartianEventHandler();
		findMartianInputPane.getChildren().add(summon);
		summon.setOnAction(findMartianEventHandler);

		return findMartianInputPane;
	}

	public void displayAvailableMartians(){
		reqInfo.appendText("Available Martians: \n");
		for(int i=0; i<mm.getNumMartians(); i++){
			reqInfo.appendText("Martian ID: " + mm.getMartianAt(i).getId() + "\n");
		}
	}

	//private classes for event handling
 	private class ToggleInteractionEventHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent event) {

			RadioButton rb = (RadioButton)toggleInteraction.getSelectedToggle();

			String whatCommand = rb.getText();
			String beep = "StarTrekEnterpriseComputerSoundFX.mp3";
			Media StarTrekEnterpriseComputerSoundFX = new Media(new File(beep).toURI().toString());
			soundFX = new MediaPlayer(StarTrekEnterpriseComputerSoundFX);

			switch( whatCommand ) {
			case "Manage":
				managePane.setVisible(true);
				interactPane.setVisible(false);
				statsPane.setVisible(false);
				inputsPane.setVisible(false);
				reqInfo.clear();
				reqInfo.setVisible(true);
				chartPane.setVisible(false);
				soundFX.play();
				break;
			case "Interact":
				reqInfo.clear();
				reqInfo.setVisible(true);
				chartPane.setVisible(false);
				interactPane.setVisible(true);
				statsPane.setVisible(false);
				managePane.setVisible(false);
				buildSubjectsComboBox();
				soundFX.play();
				break;
			case "Stats":
				reqInfo.clear();
				reqInfo.setVisible(true);
				statsPane.setVisible(true);
				managePane.setVisible(false);
				interactPane.setVisible(false);
				inputsPane.setVisible(false);
				soundFX.play();
				break;
			}
		}
	}

 	private ComboBox<String> buildSubjectsComboBox(){
		subjectsLabel.setText("Choose a Martian to interact with:");
		subjectsBox.getItems().clear();

		for(int i=0; i<mm.getNumMartians(); i++){
			if(mm.getMartianAt(i) instanceof RedMartian)
				subjectsBox.getItems().add("Red Martian #" + mm.getMartianAt(i).getId());
			else
				subjectsBox.getItems().add("Green Martian #" + mm.getMartianAt(i).getId());
		}

		SummonMinionBoxDisplayEventHandler summonMinionBoxDisplayEventHandler = new SummonMinionBoxDisplayEventHandler();
		subjectsBox.setOnAction(summonMinionBoxDisplayEventHandler);

		return subjectsBox;
	}

 	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VBox buildVolumeSliderBox(){

 		VBox sliderBox = new VBox();
        final Label volLabel = new Label("Volume");
        final Slider volSlider = new Slider();
        setVolume.setLayoutX(100);
 		setVolume.setLayoutY(50);
 		setVolume.setMin(0);
 		setVolume.setMax(100);
 		setVolume.setMajorTickUnit(25);
 		setVolume.setMinorTickCount(1);
 		setVolume.setValue(0);
        volSlider.valueProperty().addListener(new ChangeListener() {

            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                volLabel.textProperty().setValue(
                        String.valueOf((int) volSlider.getValue()));
            }
        });
        sliderBox.getChildren().addAll(volSlider, volLabel);
        volLabel.textProperty().setValue("Volume");

 		return sliderBox;
 	}

 	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BarChart buildIDsBarChart(){

 		NumberAxis xAxis = new NumberAxis();
 		CategoryAxis yAxis = new CategoryAxis();
 		final BarChart<Number, String> martianBarChart =
                new BarChart<Number, String>(xAxis, yAxis);
 		martianBarChart.setTitle("Martian Volumes by ID");
 		yAxis.setLabel("ID Number");
 		xAxis.setLabel("Volume");

 		XYChart.Series MartianIDs = new XYChart.Series();
 		MartianIDs.setName("Martian IDs and Volumes");
 		for(int i=0; i<mm.getNumMartians(); i++){
 			String graphIdString = "ID# " + mm.getMartianAt(i).getId();
 			int graphVolume = mm.getMartianAt(i).getVolume();
 			MartianIDs.getData().add(new XYChart.Data(graphVolume,graphIdString));
 		}

 		martianBarChart.getData().addAll(MartianIDs);
 		return martianBarChart;
 	}


	//Build input driven event handler classes
	private class MakeMinionsEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed) {

			soundFX = new MediaPlayer(click);
			soundFX.play();

			reqInfo.clear();
			String lolol = "wickedlaugh1.mp3";
			Media wickedlaugh1 = new Media(new File(lolol).toURI().toString());
			soundFX = new MediaPlayer(wickedlaugh1);

			try {
				feedMe = new Scanner(martiansIn);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			while(feedMe.hasNext()){
				rgb = "";
				id = -1;
				volume = 0;
				hasESP = false;
				isVegetarian = false;

				rgb = feedMe.next();
				id = feedMe.nextInt();
				if(feedMe.hasNextInt())
					volume = feedMe.nextInt();
				if(feedMe.hasNext() && feedMe.next().equalsIgnoreCase("T"))
					hasESP = true;
				if(feedMe.hasNext() && feedMe.next().equalsIgnoreCase("T"))
					isVegetarian = true;

				boolean idTaken = false;
				for(int i=0; i<IDsList.size(); i++){
						if(id == IDsList.get(i))
							idTaken = true;
				}

				if(idTaken)
					reqInfo.appendText("ID taken, creation failed. \n");
				else{
					if(id > 0){
						if(!(rgb.equalsIgnoreCase("G") || rgb.equalsIgnoreCase("R"))){
							reqInfo.appendText("You failed to create a Martian. \n");
						}
						else if(rgb.equalsIgnoreCase("R")){
							mm.addMartian(new RedMartian(id, volume, hasESP, isVegetarian));
							reqInfo.appendText("You successfully created a Red Martian. \n");
							IDsList.add(id);
						}
						else{
							mm.addMartian(new GreenMartian(id, volume, hasESP, isVegetarian));
							reqInfo.appendText("You successfully created a Green Martian. \n");
							IDsList.add(id);
						}
					}
					else
						reqInfo.appendText("ID must be greater than 0. \n");
				}
			}
			soundFX.play();
		}
	}


	private class BuildMartianEventHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {

			String dingMartianDone = "arc1.mp3";
			Media arc1 = new Media(new File(dingMartianDone).toURI().toString());

			String martianFailure = "ESPARK1.mp3";
			Media failArc = new Media(new File(martianFailure).toURI().toString());

			id = -1;
			volume = -1;

			//Martian type radio button
			RadioButton rbRGB = (RadioButton)toggleRGB.getSelectedToggle();
			if(rbRGB.getText().isEmpty())
				reqInfo.appendText("You didn't choose what kind of Martian you wanted. \n");
			else{
				rgb = rbRGB.getText();

				switch( rgb ) {
				case "Red":
					rgb = "R";
					break;
				case "Green":
					rgb = "G";
					break;
				}
			}

			if(inputId.getText().isEmpty())
				reqInfo.appendText("You didn't enter a valid Id number. \n");
			else
				id = Integer.parseInt(inputId.getText());

			volume = (int)((setVolume.getValue() * 10) / 10);

			//ESP RaidoButton
			RadioButton rbESP = (RadioButton)toggleESP.getSelectedToggle();

			if(rbESP.getText().isEmpty())
				reqInfo.appendText("You didn't say whether he has ESP or not \n");
			else{
				String watBool = rbESP.getText();

				switch( watBool ) {
				case "Yes":
					hasESP = true;
					break;
				case "No":
					hasESP = false;
					break;
				}
			}

			//Vegetarian radio button
			RadioButton rbVeg = (RadioButton)toggleVeg.getSelectedToggle();

			if(rbVeg.getText().isEmpty())
				reqInfo.appendText("You didn't say if this martian was a vegetarian or not. \n");
			else{
				String vegBool = rbVeg.getText();

				switch( vegBool ) {
				case "Yes":
					isVegetarian = true;
					break;
				case "No":
					isVegetarian = false;
					break;
				}
			}

			boolean idGone = false;
			for(int i=0; i<IDsList.size(); i++){
				if(id == IDsList.get(i))
					idGone = true;
			}
			if(idGone){
				reqInfo.appendText("ID taken!  You failed to create a new Martian, settings not applied. \n");
				soundFX = new MediaPlayer(failArc);
				soundFX.play();
			}
			else{
				if(!(rgb.equalsIgnoreCase("G") || rgb.equalsIgnoreCase("R")) || id <= 0){
					reqInfo.appendText("You can't have an ID less than zero. \n" + "You failed to create a Martian. \n");
					soundFX = new MediaPlayer(failArc);
					soundFX.play();
				}
				else if(rgb.equalsIgnoreCase("R")){
					mm.addMartian(new RedMartian(id, volume, hasESP, isVegetarian));
					reqInfo.appendText("You successfully created a Red Martian. \n");
					IDsList.add(id);
					soundFX = new MediaPlayer(arc1);
					soundFX.play();
				}
				else{
					mm.addMartian(new GreenMartian(id, volume, hasESP, isVegetarian));
					reqInfo.appendText("You successfully created a Green Martian. \n");
					IDsList.add(id);
					soundFX = new MediaPlayer(arc1);
					soundFX.play();
				}
			}
		}
	}

	private class RGBToggleEventsHandler implements EventHandler<ActionEvent>{


		public void handle(ActionEvent event) {

			//Martian type radio button
			RadioButton rbRGB = (RadioButton)toggleRGB.getSelectedToggle();
			rgb = rbRGB.getText();

			switch( rgb ) {
			case "Red":
				rgb = "R";
				break;
			case "Green":
				rgb = "G";
				break;
			}
		}
	}

	private class ESPToggleEventsHandler implements EventHandler<ActionEvent>{


		public void handle(ActionEvent e){
			//ESP RaidoButton
			RadioButton rbESP = (RadioButton)toggleESP.getSelectedToggle();
			String watBool = rbESP.getText();

			switch( watBool ) {
			case "Yes":
				hasESP = true;
				break;
			case "No":
				hasESP = false;
				break;
			}
		}
	}

	private class VegToggleEventsHandler implements EventHandler<ActionEvent>{


		public void handle(ActionEvent e){
			//Vegetarian radio button
			RadioButton rbVeg = (RadioButton)toggleVeg.getSelectedToggle();
			String vegBool = rbVeg.getText();

			switch( vegBool ) {
			case "Yes":
				isVegetarian = true;
				break;
			case "No":
				isVegetarian = false;
				break;
			}
		}
	}


	private class SaveMartiansEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			reqInfo.clear();

			try {
				inkIt = new FileWriter(martiansOut);
				outWithIt = new PrintWriter(inkIt);

				for(int i=0; i<mm.getNumMartians(); i++){
					if(mm.getMartianAt(i) instanceof RedMartian)
						outWithIt.print("R ");
					else
						outWithIt.print("G ");

					outWithIt.print(mm.getMartianAt(i).getId() + " " + mm.getMartianAt(i).getVolume() + " ");

					if(mm.getMartianAt(i).hasESP())
						outWithIt.print("T ");
					else
						outWithIt.print("F ");
					if(mm.getMartianAt(i).isVegetarian())
						outWithIt.print("T ");
					else
						outWithIt.print("F ");
					outWithIt.println("");
				}
				outWithIt.close();

				String ftw = "MetroidEventTriggerSoundEffect.mp3";
				Media iWin = new Media(new File(ftw).toURI().toString());
				soundFX = new MediaPlayer(iWin);
				soundFX.play();
			} catch (IOException e) {
				String martianFailure = "ESPARK1.mp3";
				Media failArc = new Media(new File(martianFailure).toURI().toString());
				soundFX = new MediaPlayer(failArc);
				soundFX.play();
				e.printStackTrace();
			}

			reqInfo.appendText("Martians stored successfully in martiansOut.txt \n");
		}
	}

	private class KillMartianEventHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {

			String blastEm = "MetroidPlasmaBeamSoundEffect.mp3";
			Media MetroidPlasmaBeamSoundEffect = new Media(new File(blastEm).toURI().toString());
			String toasty = "MortalKombat3ToastySoundEffect.mp3";
			Media MortalKombat3ToastySoundEffect = new Media(new File(toasty).toURI().toString());

			if(inputKillId.getText().isEmpty())
				reqInfo.appendText("You can't kill what never existed. \n");
			else{
				int killID = Integer.parseInt(inputKillId.getText());
				if(mm.getMartianWithId(killID) instanceof Martian){
					reqInfo.appendText("Casualty report: \nMartian " + killID + " is no longer with us. \n");
					mm.removeMartian(mm.getMartianWithId(killID));

					soundFX = new MediaPlayer(MetroidPlasmaBeamSoundEffect);
					soundFX.play();
					soundFX = new MediaPlayer(MortalKombat3ToastySoundEffect);
					soundFX.play();

					for(int i=0; i<IDsList.size(); i++){
						if(IDsList.get(i) == killID){
							IDsList.remove(i);
						}
					}
				}
				else{
					reqInfo.appendText("You can't kill whom isn't here. \n");
				}
			}
		}

	}

	private class NoTeleportersEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {

			String boom = "MegaManDeathSoundEffect.mp3";
			Media MegaManDeathSoundEffect = new Media(new File(boom).toURI().toString());
			soundFX = new MediaPlayer(MegaManDeathSoundEffect);
			soundFX.play();

			reqInfo.clear();
			reqInfo.appendText("Casualty List:\n");
			for(int i=0; i<mm.getNumMartians(); i++){
				if(mm.getMartianAt(i) instanceof GreenMartian){
					reqInfo.appendText("Martian ID#:" + mm.getMartianAt(i).getId() + "\n");
					for(int j=0; j<IDsList.size(); j++){
						if(mm.getMartianAt(i).getId() == IDsList.get(j)){
							IDsList.remove(j);
						}
					}
				}
			}
			mm.obliterateTeleporters();
			reqInfo.appendText("There are no more Green Martians. \n");
		}

	}

	private class ArmageddonItEventHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			reqInfo.clear();
			reqInfo.appendText("Casualty List: \n");
			for(int i=0; i<mm.getNumMartians(); i++){
				reqInfo.appendText("Martian #" + mm.getMartianAt(i).getId() + ", \n");
			}
			mm.obliterateMartians();
			IDsList.clear();
			reqInfo.appendText("Casualty Rate: 100% \n" + "Game over man! GAME OVER! \n");

			String endItAll = "armageddonIt.mp3";

			Media armageddonMP3 = new Media(new File(endItAll).toURI().toString());

			soundFX = new MediaPlayer(armageddonMP3);
			soundFX.play();

		}

	}

	private class FindMartianEventHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent arg0) {

			if(!(inputFindId.getText().isEmpty())){
					Martian findMe = new GreenMartian(Integer.parseInt(inputFindId.getText()));

					if(mm.getMartianWithId(Integer.parseInt(inputFindId.getText())) instanceof Martian && Integer.parseInt(inputFindId.getText()) >= 0){
						reqInfo.appendText("Found ID# " + findMe.getId() + "\n");
						reqInfo.appendText(mm.getMartianWithId(findMe.getId()).speak() + "\n");
						if(findMe instanceof RedMartian){
							String rubldyRoc = "StarCraftDragoonLokPiiSoundEffect.mp3";
							Media StarCraftDragoonLokPiiSoundEffect = new Media(new File(rubldyRoc).toURI().toString());
							soundFX = new MediaPlayer(StarCraftDragoonLokPiiSoundEffect);
							soundFX.play();
						}
						else if(findMe instanceof GreenMartian){
							String grubldyGroc = "StarCraftFenixDragoonNachNagalasSoundEffect.mp3";
							Media StarCraftFenixDragoonNachNagalasSoundEffect = new Media(new File(grubldyGroc).toURI().toString());
							soundFX = new MediaPlayer(StarCraftFenixDragoonNachNagalasSoundEffect);
							soundFX.play();
						}
					}

					else if	(mm.getMartianClosestToId(Integer.parseInt(inputFindId.getText())) instanceof Martian && Integer.parseInt(inputFindId.getText()) >= 0){

						try {
							findMe = (Martian)(mm.getMartianClosestToId(Integer.parseInt(inputFindId.getText())).clone());
							if(mm.getMartianClosestToId(findMe) instanceof RedMartian){
								String rubldyRoc = "StarCraftDragoonLokPiiSoundEffect.mp3";
								Media StarCraftDragoonLokPiiSoundEffect = new Media(new File(rubldyRoc).toURI().toString());
								soundFX = new MediaPlayer(StarCraftDragoonLokPiiSoundEffect);
								soundFX.play();
							}
							else{
								String grubldyGroc = "StarCraftFenixDragoonNachNagalasSoundEffect.mp3";
								Media StarCraftFenixDragoonNachNagalasSoundEffect = new Media(new File(grubldyGroc).toURI().toString());
								soundFX = new MediaPlayer(StarCraftFenixDragoonNachNagalasSoundEffect);
								soundFX.play();
							}
						} catch (NumberFormatException | CloneNotSupportedException e) {
							e.printStackTrace();
						}

						reqInfo.appendText("Martian not found, the closest Martian is: \n" + findMe.getId() + "\nPlease reenter a Valid ID. \n");
					}
					else
						reqInfo.appendText("The ID entered is probably invalid. \n");
				}
				else
					reqInfo.appendText("You must enter an Id to search for. \n");
		}
	}

	private class VocalizeAsOneEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			reqInfo.clear();
			reqInfo.appendText(mm.groupSpeak());

			String allShout = "OrcsMarchOnMinasTirith.mp3";
			Media OrcsMarchOnMinasTirith = new Media(new File(allShout).toURI().toString());
			soundFX = new MediaPlayer(OrcsMarchOnMinasTirith);
			soundFX.play();
		}
	}


	private class TeleportEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			if(!(inputTeleportId.getText().isEmpty()) || !(inputLocation.getText().isEmpty()) ){
				if(mm.getMartianWithId(Integer.parseInt(inputTeleportId.getText())) instanceof GreenMartian){
					reqInfo.appendText("Martian #" + mm.getTeleporterWithId(Integer.parseInt(inputTeleportId.getText())).teleport(inputLocation.getText()) + "\n");
					String poof = "BeamMeUpScotty.mp3";
					Media BeamMeUpScotty = new Media(new File(poof).toURI().toString());
					soundFX = new MediaPlayer(BeamMeUpScotty);
					soundFX.play();
				}
				else
					reqInfo.appendText("ID supplied is not a teleporter. \n");
			}
			else
				reqInfo.appendText("Both ID and Destination inputs required for individual teleportation \n");
		}
	}

	private class GroupTeleportEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			if(inputgroupLocation.getText().isEmpty())
				reqInfo.appendText("The destination field cannot be empty when attempting to teleport. \n");
			else{
				reqInfo.appendText("Teleport progress report: \n" + mm.groupTeleport(inputgroupLocation.getText()) + "\nMass teleport successful. \n");
				String poof = "BeamMeUpScotty.mp3";
				Media BeamMeUpScotty = new Media(new File(poof).toURI().toString());
				soundFX = new MediaPlayer(BeamMeUpScotty);
				soundFX.play();
			}
		}
	}

	private class ShowMinionsEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent ButtonPressed) {

			soundFX = new MediaPlayer(click);
			soundFX.play();

			String tuning = "TuneRadio.mp3";
			Media TuneRadio = new Media(new File(tuning).toURI().toString());
			soundFX = new MediaPlayer(TuneRadio);
			soundFX.play();

			reqInfo.clear();
			reqInfo.setVisible(true);
			chartPane.setVisible(false);
			ArrayList<Martian> sortedList = new ArrayList<>();
			sortedList = mm.sortedMartians();
			for(int i=0; i<sortedList.size(); i++){
				reqInfo.appendText(sortedList.get(i).toString() + "\n");
			}
		}
	}

	private class SummonMinionBoxDisplayEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			String getMeAnInt = subjectsBox.getValue();
			String foundMeInt = "";
			if(subjectsBox.getValue() != null){
				for(int i=0; i<getMeAnInt.length(); i++){
					if(Character.isDigit(getMeAnInt.charAt(i)))
						foundMeInt += getMeAnInt.charAt(i);
				}
			reqInfo.appendText(mm.getMartianWithId(Integer.parseInt(foundMeInt)).speak() + "\n");
			if(mm.getMartianWithId(Integer.parseInt(foundMeInt)) instanceof RedMartian){
				String rubldyRoc = "StarCraftDragoonLokPiiSoundEffect.mp3";
				Media StarCraftDragoonLokPiiSoundEffect = new Media(new File(rubldyRoc).toURI().toString());
				soundFX = new MediaPlayer(StarCraftDragoonLokPiiSoundEffect);
				soundFX.play();
			}
			else{
				String grubldyGroc = "StarCraftFenixDragoonNachNagalasSoundEffect.mp3";
				Media StarCraftFenixDragoonNachNagalasSoundEffect = new Media(new File(grubldyGroc).toURI().toString());
				soundFX = new MediaPlayer(StarCraftFenixDragoonNachNagalasSoundEffect);
				soundFX.play();
			}

			}
		}
	}

 	//build display event handler classes
	private class DisplayMakeOneMartianEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			inputsPane.setVisible(true);
			makeMartiansInputsPane.setVisible(true);
			killInputPane.setVisible(false);
			teleportInputPane.setVisible(false);
			groupTeleportInputPane.setVisible(false);
			findMartianInputPane.setVisible(false);
			chartPane.setVisible(false);
			reqInfoPane.setVisible(true);

			reqInfo.clear();

			reqInfo.appendText(
					"1. Pick which Martian to make. \n" +
							"2. Enter an Integer that will be this Martian's ID number. \n" +
							"3. Enter an Integer for how loud this martian will be. \n" +
							"4. Does this Martian have ESP? \n" +
							"5. Is this Martian a vegetarian? \n");
		}
	}

	private class KillMartianInputDisplayEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			inputsPane.setVisible(true);
			killInputPane.setVisible(true);
			teleportInputPane.setVisible(false);
			groupTeleportInputPane.setVisible(false);
			makeMartiansInputsPane.setVisible(false);
			findMartianInputPane.setVisible(false);
			chartPane.setVisible(false);
			reqInfoPane.setVisible(true);

			reqInfo.clear();
			displayAvailableMartians();
		}
	}

	private class DisplayTeleportPaneEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			inputsPane.setVisible(true);
			teleportInputPane.setVisible(true);
			groupTeleportInputPane.setVisible(false);
			makeMartiansInputsPane.setVisible(false);
			findMartianInputPane.setVisible(false);
			killInputPane.setVisible(false);
			chartPane.setVisible(false);
			reqInfoPane.setVisible(true);

			reqInfo.clear();
			reqInfo.appendText("To teleport a single Martian, use one of the following IDs: \n");
			for(int i=0; i<mm.getNumMartians(); i++){
				if(mm.getMartianAt(i) instanceof GreenMartian)
				reqInfo.appendText("Available teleporter ID: " + mm.getMartianAt(i).getId() + "\n");
			}
		}
	}

	private class DisplaygroupTeleportInputPaneEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			inputsPane.setVisible(true);
			groupTeleportInputPane.setVisible(true);
			teleportInputPane.setVisible(false);
			makeMartiansInputsPane.setVisible(false);
			findMartianInputPane.setVisible(false);
			killInputPane.setVisible(false);
			chartPane.setVisible(false);
			reqInfoPane.setVisible(true);

			reqInfo.clear();
			reqInfo.appendText("Enter a location to send all your Green Martians to \n");
		}
	}

	private class FindMartianDisplayEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent buttonPressed){

			soundFX = new MediaPlayer(click);
			soundFX.play();

			inputsPane.setVisible(true);
			findMartianInputPane.setVisible(true);
			killInputPane.setVisible(false);
			teleportInputPane.setVisible(false);
			groupTeleportInputPane.setVisible(false);
			makeMartiansInputsPane.setVisible(false);
			chartPane.setVisible(false);
			reqInfoPane.setVisible(true);

			reqInfo.clear();
			reqInfo.appendText("Enter a Martian's ID number.  \n" +
								"If the ID is not found, we will display the closest match. \n" +
								"If this happens, reenter the a valid ID number. \n");
		}
	}

	private class InteractTabSelectionChangedEventHandler implements EventHandler<Event>{
		public void handle(Event tabChanged){

			String newTab = "button1.mp3";
			Media tabChange = new Media(new File(newTab).toURI().toString());
			soundFX = new MediaPlayer(tabChange);
			soundFX.play();

			if(singleInteract.isSelected()){
				groupInteract.selectedProperty();
			}
			singleInteract.selectedProperty();
		}
	}

	private class SetStatsOrGraphSelectedEventHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent boxChecked){

			if(statsOrGraph.isSelected()){
				reqInfoPane.setVisible(false);
				chartPane.getChildren().clear();
				chartPane.getChildren().add(buildIDsBarChart());
				chartPane.setVisible(true);

				String tuning = "TuneRadio.mp3";
				Media TuneRadio = new Media(new File(tuning).toURI().toString());
				soundFX = new MediaPlayer(TuneRadio);
				soundFX.play();
			}
			else{
				reqInfoPane.setVisible(true);
				chartPane.setVisible(false);
				chartPane.getChildren().clear();

				String buttonOff = "button3.mp3";
				Media graphOff = new Media(new File(buttonOff).toURI().toString());
				soundFX = new MediaPlayer(graphOff);
				soundFX.play();
			}
		}
	}

	//start method
	public static void main(String[] args) {
		launch(args);
	}
}

/******************************************************************************
CalendarGUI
https://youtu.be/hR-y9KFj1Os

-version of assignment 1 (print a calendar) using JavaFX

CLASSES
  -CalendarGUI: runner for application
    METHODS
    -public void start(Stage primaryStage): runs app, restricts window to calendar size
    -public void stop(): exits program and closes all running Timer threads
  -CalendarGUIApp: displays calendar an keeps track of events
    PROPERTIES
      -private static ArrayList<DatePane> allCalendarDays
      -private static ArrayList<EventPane> eventList
      -private static YearMonth currentYearMonth:
      -private static VBox calview, private static HBox container, private static ScrollPane scrollPane,
      private static BorderPane screen, private static Text calendarTitle, private static HBox titleBar,
      private static GridPane dayLabels, private static GridPane calendar: store non-DatePane elements of calendar
      -public static Color[] currentTheme: stores current theme of calendar
      -public static Color[] lightMode, public static Color[] darkMode, public static Color[] pastelMode, public static Color[] retroMode,
       public static Color[] neonMode: store themes as input for changeTheme
    METHODS
      -private static void previousMonth(): decrements currentYearMonth by 1, populates calendar
      -private static void nextMonth(): increments currentYearMonth by 1, populates calendar
      -public static void populateCalendar(YearMonth yearMonth): sets events, changes calendar title to yearMonth
      -public BorderPane getScreen(): returns BorderPane holding all elements for Stage
      -public static YearMonth getCurrentYearMonth(): getter method for private currentYearMonth
      -public static ArrayList<EventPane> getEventList() : getter method for private eventList
      -public static void addEvent(EventPane addEvent): adds addEvent to eventList
      -public static void removeEvent(String remEvent): removes remEvent from eventList
      -public static void editEvent(String oldEvent, EventPane newEvent): removes oldEvent from eventList, adds newEvent to eventList
      -public static void setEvents(YearMonth yearMonth): reloads all DatePanes, assigns day numbers and events to each, counts number of events and cuts off if needed
      -public static void setUpLayout(YearMonth yearMonth): prepares calendar for filling by setting up GridPane with values
      -public static void changeTheme(Color[] colorList): carries out cosmetic changes on all calendar elements
  -DatePane: stores day number, events as children
    PROPERTIES
      -private LocalDateTime date: stores date of DatePane
      -public VBox events: stores events being displayed on DatePane
      -public StackPane datePane: stores day number
      -public VBox container: stores datePane and events
      -public int evnum: stores number of events for DatePane
      -private Text dateText: stores text of day number
    METHODS
      -private void setUpLayout(): adds padding and listener for mouse click
      -public LocalDateTime getDate(): getter method for private date
      -public void setDate(LocalDateTime date): setter method for private date
      -private boolean TimeValidator(String time): checks validity of time in accordance with HH:MM:SS for use in event creation/editing dialogs
      -public VBox getEvents(): getter method for private events
      -private void createActionDialog(): creates dialog for choosing between available actions
      -private void createAddDialog(): creates dialog for adding event
      -private void createEditChoice(): creates dialog for choosing event to edit
      -private void createEditDialog(String event):creates dialog for editing event
      -private void createDelDialog(): creates dialog for choosing event to delete
      -private void createSaveDialog():creates dialog for choosing file to save current calendar to
      -private void saveCalendar(String file): saves eventList in this format
        name
        description
        date
      -private void createLoadDialog():creates dialog for choosing file to load calendar from
      -private void loadCalendar(String file): parses file (if possible) and adds events to eventList
  -EventPane: stores information of event
    PROPERTIES
      -private Text title: stores title of event as displayes
      -private String name: stores full name of event
      -private LocalDateTime date: stores date of event
      -private String description: stores description of event
      -private Timer t: stores timer set for date
    METHODS
      -public void setUpLayout(): sets padding, title, mouseover text, and sets up timer
      -private void setUpTimer(): sets up timer for date and calls timerEndDialog once complete
      -public String getName(): getter method for private name
      -public LocalDateTime getDate(): getter method for private date
      -public String getDescription(): getter method for private description
      -public String toString(): converts to string for display in CRUD dialogs
      -public String saveToString(): converts to string for saving in file
      -public void timerEndDialog(): creates dialog that event has completed
******************************************************************************/
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.function.*;
import javafx.application.*;
import javafx.beans.binding.Bindings;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class CalendarGUI extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Calendar");
    primaryStage.setScene(new Scene(new CalendarGUIApp(YearMonth.now()).getScreen()));
    primaryStage.show();
    primaryStage.setMaxWidth(primaryStage.getWidth());
    primaryStage.setMaxHeight(primaryStage.getHeight());
  }
  @Override
  public void stop() {
      System.exit(0);
  }
  public static void main(String[] args) {
    launch(args);
  }
}

class CalendarGUIApp {
  private static ArrayList<DatePane> allCalendarDays = new ArrayList<>(35);
  private static ArrayList<EventPane> eventList = new ArrayList<EventPane>();
  private static YearMonth currentYearMonth;

  private static VBox calview = new VBox();
  private static HBox container = new HBox();
  private static ScrollPane scrollPane = new ScrollPane();
  private static BorderPane screen = new BorderPane();
  private static Text calendarTitle = new Text();
  private static HBox titleBar = new HBox();
  private static GridPane dayLabels = new GridPane();
  private static GridPane calendar = new GridPane();

  public static Color[] currentTheme = new Color[13];
  //                                             0                     1                    2                    3                     4                    5                    6                     7                    8                    9                    10                    11                   12
  //                                                                   1                    2                    3                     2                    4
  //                                                                   calendarTitle        gridBorder           dayLabel              dayNameColor         titleBar             dayCellClick          activeText           inactiveText         activeBKG            inactiveBKG           eventText            eventBKG
  public static Color[] lightMode = new Color[]  {Color.web("000000"), Color.web("000000"), Color.web("000000"), Color.web("a9a9a9"),  Color.web("000000"), Color.web("ffffff"), Color.web("#d7e3f5"), Color.web("000000"), Color.web("a9a9a9"), Color.web("ffffff"), Color.web("dcdcdc"),  Color.web("ffffff"), Color.web("a9a9a9")};
  public static Color[] darkMode = new Color[]   {Color.web("ffffff"), Color.web("ffffff"), Color.web("ffffff"), Color.web("#262626"), Color.web("ffffff"), Color.web("000000"), Color.web("#7d8a9e"), Color.web("ffffff"), Color.web("dcdcdc"), Color.web("000000"), Color.web("a9a9a9"),  Color.web("ffffff"), Color.web("a9a9a9")};
  public static Color[] pastelMode = new Color[] {Color.web("ffffff"), Color.web("ffffff"), Color.web("fae3d9"), Color.web("#fae3d9"), Color.web("8ac6d1"), Color.web("ffb6b9"), Color.web("#ffb6b9"), Color.web("708090"), Color.web("ffffff"), Color.web("8ac6d1"), Color.web("#bbded6"), Color.web("ffffff"), Color.web("708090")};
  public static Color[] retroMode = new Color[]  {Color.web("ffffff"), Color.web("293462"), Color.web("f76262"), Color.web("#293462"), Color.web("fff1c1"), Color.web("f76262"), Color.web("#fff1c1"), Color.web("ffffff"), Color.web("dcdcdc"), Color.web("293462"), Color.web("#216583"), Color.web("ffffff"), Color.web("216583")};
  public static Color[] neonMode = new Color[]   {Color.web("ffffff"), Color.web("fffdaf"), Color.web("444444"), Color.web("#444444"), Color.web("00faac"), Color.web("444444"), Color.web("#302387"), Color.web("ff3796"), Color.web("dcdcdc"), Color.web("444444"), Color.web("#ff3796"), Color.web("ffffff"), Color.web("ff3796")};

  public CalendarGUIApp(YearMonth yearMonth) {
    //sample events (usage deprecated)
    //addEvent(new EventPane("school starts", LocalDateTime.of(2019, 8, 28, 0, 0, 0), "wake up at 6 for 0 period pe"));
    //addEvent(new EventPane("final due", LocalDateTime.of(2019, 8, 8, 0, 0, 0), "i <3 javafx"));
    //addEvent(new EventPane("yee", LocalDateTime.of(2019, 8, 6, 0, 0, 0), "example event"));
    //create original view of calendar
    currentTheme = lightMode;
    setUpLayout(yearMonth);
    populateCalendar(yearMonth);
    changeTheme(lightMode);
    //root.setPadding(new Insets(15));
  }
  private static void previousMonth() {
    currentYearMonth = currentYearMonth.minusMonths(1);
    populateCalendar(currentYearMonth);
  }
  private static void nextMonth() {
    currentYearMonth = currentYearMonth.plusMonths(1);
    populateCalendar(currentYearMonth);
  }
  public static void populateCalendar(YearMonth yearMonth) {
    setEvents(yearMonth);
    calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
  }
  public BorderPane getScreen() {
    return screen;
  }
  public static YearMonth getCurrentYearMonth() {
    return currentYearMonth;
  }
  public static ArrayList<EventPane> getEventList() {
    return eventList;
  }
  public void setAllCalendarDays(ArrayList<DatePane> allCalendarDays) {
    this.allCalendarDays = allCalendarDays;
  }
  public static void addEvent(EventPane addEvent) {
    eventList.add(addEvent);
  }
  public static void removeEvent(String remEvent) {
    eventList.removeIf((eeee) -> (eeee.toString().equals(remEvent)));
  }
  public static void editEvent(String oldEvent, EventPane newEvent) {
    removeEvent(oldEvent);
    addEvent(newEvent);
  }
  public static void setEvents(YearMonth yearMonth) {

    //set calendar to start from last sunday before (and including) first of yearMonth
    LocalDateTime calendarDate = LocalDateTime.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1, 0, 0);
    while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
      calendarDate = calendarDate.minusDays(1);
    }

    for (DatePane ap : allCalendarDays) {

      //resets all DatePanes before refilling
      ap.getChildren().clear();
      ap.evnum = 0;

      //sets date, text color, and backgrounf color of datePane
      ap.setDate(calendarDate);

      //counts adds EventPanes corresponding to DatePane
      ap.events = new VBox();
      for (EventPane e : eventList) {
        if (e.getDate().getYear() == ap.getDate().getYear() && e.getDate().getMonthValue() == ap.getDate().getMonthValue() && e.getDate().getDayOfMonth() == ap.getDate().getDayOfMonth()) {
          ap.events.getChildren().add(e);
          ap.evnum += 1;
        }
      }

      //shortens number of EventPanes being displayed to stop overflow of cell and combines with Pane with date on it
      int extra = ap.events.getChildren().size() - 2;
      while (ap.events.getChildren().size() > 2) {
        ap.events.getChildren().remove(ap.events.getChildren().size() - 1);
      }
      if (extra <= 0) {
        ap.container = new VBox(ap.datePane, ap.events);
      }
      else {
        ap.container = new VBox(ap.datePane, ap.events, new Text("+" + extra + " more"));
      }

      //adds combined Panes to DatePane, increments counter for calendarDate
      ap.getChildren().add(ap.container);
      ap.setAlignment(ap.container, Pos.CENTER);
      calendarDate = calendarDate.plusDays(1);
    }
  }
  public static void setUpLayout(YearMonth yearMonth) {
    //set up gridpane for dates
    currentYearMonth = yearMonth;
    calendar = new GridPane();
    calendar.setPrefSize(600, 400);
    //calendar.setGridLinesVisible(true);
    for (int i = 1; i < 7; i++) {
      for (int j = 0; j < 7; j++) {
        DatePane ap = new DatePane();
        //ap.setPrefSize(200,200);
        ap.setBorder(new Border(new BorderStroke(CalendarGUIApp.currentTheme[2], BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.0))));
        ap.setMinSize(100, 100);
        calendar.add(ap,j,i);
        allCalendarDays.add(ap);
      }
    }

    calendarTitle = new Text(currentYearMonth.getMonth().toString());
    Button previousMonth = new Button("<<");
    previousMonth.setOnAction(e -> previousMonth());
    Button nextMonth = new Button(">>");
    nextMonth.setOnAction(e -> nextMonth());
    String[] themes = {"Light Mode", "Dark Mode", "Pastel", "Retro", "Neon"};
    ComboBox CT =  new ComboBox(FXCollections.observableArrayList(themes));
    // Create action event
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
          if (CT.getValue().equals("Light Mode")) {
            changeTheme(lightMode);
          }
          if (CT.getValue().equals("Dark Mode")) {
            changeTheme(darkMode);
          }
          if (CT.getValue().equals("Pastel")) {
            changeTheme(pastelMode);
          }
          if (CT.getValue().equals("Retro")) {
            changeTheme(retroMode);
          }
          if (CT.getValue().equals("Neon")) {
            changeTheme(neonMode);
          }
        }
    };
    CT.setOnAction(event);
    CT.getSelectionModel().selectFirst();

    //combine parts of calendar (titlebar, day labels, calendar gridpane)
    titleBar = new HBox(previousMonth, calendarTitle, nextMonth, CT);
    titleBar.setAlignment(Pos.BASELINE_CENTER);
    titleBar.setSpacing(5);

    StackPane heading = new StackPane(titleBar, CT);
    heading.setAlignment(titleBar, Pos.BOTTOM_CENTER);
    heading.setAlignment(CT, Pos.BOTTOM_RIGHT);
    calview = new VBox(heading, dayLabels, calendar);
    calview.setSpacing(3);
    titleBar.setBackground(new Background(new BackgroundFill(CalendarGUIApp.currentTheme[5], CornerRadii.EMPTY, Insets.EMPTY)));
    titleBar.setPadding(new Insets(5));
    container = new HBox(calview);
    scrollPane = new ScrollPane(container);
    scrollPane.setFitToHeight(true);
    screen = new BorderPane(scrollPane);
  }
  public static void changeTheme(Color[] colorList) {
    currentTheme = colorList;
    titleBar.setBackground(new Background(new BackgroundFill(CalendarGUIApp.currentTheme[5], CornerRadii.EMPTY, Insets.EMPTY)));

    calendarTitle.setFont(Font.font("Barlow", 20.0));
    calendarTitle.setStroke(CalendarGUIApp.currentTheme[1]);
    calendarTitle.setFill(CalendarGUIApp.currentTheme[1]);
    Text[] dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"), new Text("Wednesday"), new Text("Thursday"), new Text("Friday"), new Text("Saturday")};
    dayLabels = new GridPane();
    dayLabels.setPrefWidth(600);
    Integer col = 0;
    for (Text txt : dayNames) {
      txt.setStroke(CalendarGUIApp.currentTheme[4]);
      txt.setFill(CalendarGUIApp.currentTheme[4]);
      StackPane ap = new StackPane();
      ap.setPrefSize(200, 10);
      ap.getChildren().add(txt);
      ap.setAlignment(txt, Pos.CENTER);
      ap.setPadding(new Insets(5, 0, 5, 0));
      ap.setBorder(new Border(new BorderStroke(CalendarGUIApp.currentTheme[2], BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.0))));
      calendar.add(ap, col++, 0);
      ap.setBackground(new Background(new BackgroundFill(CalendarGUIApp.currentTheme[3], CornerRadii.EMPTY, Insets.EMPTY)));
    }
    for (EventPane e : eventList) {
      e.setBorder(new Border(new BorderStroke(CalendarGUIApp.currentTheme[12], BorderStrokeStyle.SOLID, new CornerRadii(3.0), new BorderWidths(1.0))));
      e.setBackground(new Background(new BackgroundFill(CalendarGUIApp.currentTheme[12], new CornerRadii(3.0), Insets.EMPTY)));
    }
    setEvents(currentYearMonth);
  }
}

class DatePane extends StackPane {
  private LocalDateTime date;
  public VBox events = new VBox();
  public StackPane datePane;
  public VBox container = new VBox();
  public int evnum = 0;
  private Text dateText;

  public DatePane(Node... children) {
    //super(children);
    this.getChildren().add(container);
    this.container.getChildren().addAll(events);
    setUpLayout();
    // Add action handler for mouse clicked

  }
  private void setUpLayout() {
    this.setPadding(new Insets(5));
    this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        setBackground(new Background(new BackgroundFill(CalendarGUIApp.currentTheme[6], CornerRadii.EMPTY, Insets.EMPTY)));
        createActionDialog();
      }
    });
  }
  public LocalDateTime getDate() {
    return date;
  }
  public void setDate(LocalDateTime date) {
    this.date = date;
    Text txt = new Text(String.valueOf(date.getDayOfMonth()));
    txt.setFont(Font.font("Barlow", 15.0));
    if (getDate().getMonth() == CalendarGUIApp.getCurrentYearMonth().getMonth()) {
      txt.setStroke(CalendarGUIApp.currentTheme[7]);
      txt.setFill(CalendarGUIApp.currentTheme[7]);
      setBackground(new Background(new BackgroundFill(CalendarGUIApp.currentTheme[9], CornerRadii.EMPTY, Insets.EMPTY)));
    }
    else {
      txt.setStroke(CalendarGUIApp.currentTheme[8]);
      txt.setFill(CalendarGUIApp.currentTheme[8]);
      setBackground(new Background(new BackgroundFill(CalendarGUIApp.currentTheme[10], CornerRadii.EMPTY, Insets.EMPTY)));
    }
    this.setBorder(new Border(new BorderStroke(CalendarGUIApp.currentTheme[2], BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.0))));

    //sets up Pane with date of DatePane
    datePane = new StackPane(txt);
    datePane.setPadding(new Insets(3));
    datePane.setAlignment(txt, Pos.TOP_LEFT);
  }
  private boolean TimeValidator(String time) {
    if (time.length() == 8) {
        if ((("01".contains(time.substring(0, 1)) && "0123456789".contains(time.substring(1, 2))) || (("2".contains(time.substring(0, 1)) && "0123".contains(time.substring(1, 2)))) && ":".contains(time.substring(2, 3)))) {
            if (("012345".contains(time.substring(3, 4)) && "0123456789".contains(time.substring(4, 5)) && ":".contains(time.substring(5, 6)))) {
                if (("012345".contains(time.substring(6, 7)) && "0123456789".contains(time.substring(7)))) {
                    return false;
                }
                else {
                    return true;
                }
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }
    else {
        return true;
    }
  }
  public VBox getEvents() {
    return events;
  }
  private void createActionDialog() {
    ChoiceDialog<String> dialog = new ChoiceDialog<String>();
    if (evnum > 0) {
      dialog = new ChoiceDialog<String>("Add event", "Add event", "Edit event", "Delete event", "Save calendar", "Load calendar");
    }
    else {
      dialog = new ChoiceDialog<String>("Add event", "Add event", "Save calendar", "Load calendar");
    }
    dialog.setTitle("CalendarGUI");
    dialog.setHeaderText("Select an action:");
    dialog.setContentText("Action:");

    Optional<String> result = dialog.showAndWait();

    result.ifPresent(action -> {
      if (action == "Add event") {
        createAddDialog();
      }
      if (action == "Edit event") {
        createEditChoice();
      }
      if (action == "Delete event") {
        createDelDialog();
      }
      if (action == "Save calendar") {
        createSaveDialog();
      }
      if (action == "Load calendar") {
        createLoadDialog();
      }
    });
    CalendarGUIApp.populateCalendar(CalendarGUIApp.getCurrentYearMonth());
    while (dialog.isShowing()) {
      dialog.close();
    }
  }
  private void createAddDialog() {
    Dialog<ArrayList<String>> addDialog = new Dialog<>();
    addDialog.setTitle("Add Event");
    addDialog.setHeaderText("Add event on " + getDate().toString());

    // Set the icon (must be included in the project).
    //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

    // Set the button types.
    ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
    addDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Create the username and password labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField evName = new TextField();
    evName.setPromptText("Event name");
    TextField description = new TextField();
    description.setPromptText("Description");
    TextField time = new TextField();
    time.setPromptText("HH:MM:SS");

    grid.add(new Label("Event name:"), 0, 0);
    grid.add(evName, 1, 0);
    grid.add(new Label("Description:"), 0, 1);
    grid.add(description, 1, 1);
    grid.add(new Label("Time:"), 0, 2);
    grid.add(time, 1, 2);

    // Enable/Disable login button depending on whether a username was entered.
    Node okButton = addDialog.getDialogPane().lookupButton(okButtonType);
    okButton.setDisable(true);

    //Do some validation (using the Java 8 lambda syntax).
    evName.textProperty().addListener((observable, oldValue, newValue) -> {
        time.textProperty().addListener((observable2, oldValue2, newValue2) -> {
            if ((newValue.trim().isEmpty())) {
                okButton.setDisable(true);
            }
            else {
              okButton.setDisable(TimeValidator(newValue2));
            }
        });
    });

    addDialog.getDialogPane().setContent(grid);

    // Request focus on the username field by default.
    Platform.runLater(() -> evName.requestFocus());

    // Convert the result to a username-password-pair when the login button is clicked.
    addDialog.setResultConverter(dialogButton -> {
        if (dialogButton == okButtonType) {
            ArrayList<String> results = new ArrayList<String>();
            results.add(evName.getText());
            results.add(description.getText());
            results.add(time.getText());
            return results;
        }
        return null;
    });

    Optional<ArrayList<String>> addResult = addDialog.showAndWait();

    addResult.ifPresent(result -> {
        EventPane newEvent = new EventPane(result.get(0), LocalDateTime.of(getDate().getYear(), getDate().getMonth(), getDate().getDayOfMonth(), Integer.parseInt(result.get(2).substring(0, 2)), Integer.parseInt(result.get(2).substring(3, 5)), Integer.parseInt(result.get(2).substring(6))), result.get(1));
        CalendarGUIApp.addEvent(newEvent);
        CalendarGUIApp.changeTheme(CalendarGUIApp.currentTheme);
        //this.events.getChildren().add(newEvent);
        //this.evnum += 1;
    });
    addDialog.close();
  }
  private void createEditChoice() {
    ArrayList<String> evNames = new ArrayList<String> ();
    for (EventPane eeee : CalendarGUIApp.getEventList()) {
      if (eeee.getDate().getYear() == getDate().getYear() && eeee.getDate().getMonthValue() == getDate().getMonthValue() && eeee.getDate().getDayOfMonth() == getDate().getDayOfMonth()) {
        evNames.add(eeee.toString());
      }
    }
    ChoiceDialog<String> editChoiceDialog = new ChoiceDialog<String>("", evNames);

    editChoiceDialog.setTitle("Edit event");
    editChoiceDialog.setHeaderText("Select an event:");
    editChoiceDialog.setContentText("Event:");
    Optional<String> editChoiceResult = editChoiceDialog.showAndWait();
    editChoiceResult.ifPresent(event -> {
      createEditDialog(event);
    });
    editChoiceDialog.close();
  }
  private void createEditDialog(String event) {
    Dialog<ArrayList<String>> editDialog = new Dialog<>();
    editDialog.setTitle("Edit Event");
    editDialog.setHeaderText("Edit event " + event + " on " + getDate().toString());

    // Set the icon (must be included in the project).
    //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

    // Set the button types.
    ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
    editDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Create the username and password labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField evName = new TextField();
    evName.setPromptText("Event name");
    TextField description = new TextField();
    description.setPromptText("Description");
    TextField time = new TextField();
    time.setPromptText("HH:MM:SS");

    grid.add(new Label("Event name:"), 0, 0);
    grid.add(evName, 1, 0);
    grid.add(new Label("Description:"), 0, 1);
    grid.add(description, 1, 1);
    grid.add(new Label("Time:"), 0, 2);
    grid.add(time, 1, 2);

    // Enable/Disable login button depending on whether a username was entered.
    Node okButton = editDialog.getDialogPane().lookupButton(okButtonType);
    okButton.setDisable(true);

    //Do some validation (using the Java 8 lambda syntax).
    evName.textProperty().addListener((observable, oldValue, newValue) -> {
      time.textProperty().addListener((observable2, oldValue2, newValue2) -> {
        if ((newValue.trim().isEmpty())) {
            okButton.setDisable(true);
        }
        else {
          okButton.setDisable(TimeValidator(newValue2));
        }
      });
    });
    editDialog.getDialogPane().setContent(grid);

    // Request focus on the username field by default.
    Platform.runLater(() -> evName.requestFocus());

    // Convert the result to a username-password-pair when the login button is clicked.
    editDialog.setResultConverter(dialogButton -> {
        if (dialogButton == okButtonType) {
            ArrayList<String> results = new ArrayList<String>();
            results.add(evName.getText());
            results.add(description.getText());
            results.add(time.getText());
            return results;
        }
        return null;
    });

    Optional<ArrayList<String>> editResult = editDialog.showAndWait();

    editResult.ifPresent(result -> {
        EventPane newEvent = new EventPane(result.get(0), LocalDateTime.of(getDate().getYear(), getDate().getMonth(), getDate().getDayOfMonth(), Integer.parseInt(result.get(2).substring(0, 2)), Integer.parseInt(result.get(2).substring(3, 5)), Integer.parseInt(result.get(2).substring(6))), result.get(1));
        CalendarGUIApp.editEvent(event, newEvent);
        CalendarGUIApp.changeTheme(CalendarGUIApp.currentTheme);
    });
    editDialog.close();
  }
  private void createDelDialog() {
    ArrayList<String> evNames = new ArrayList<String> ();
    for (EventPane eeee : CalendarGUIApp.getEventList()) {
      if (eeee.getDate().getYear() == getDate().getYear() && eeee.getDate().getMonthValue() == getDate().getMonthValue() && eeee.getDate().getDayOfMonth() == getDate().getDayOfMonth()) {
        evNames.add(eeee.toString());
      }
    }
    ChoiceDialog<String> delChoiceDialog = new ChoiceDialog<String>("", evNames);

    delChoiceDialog.setTitle("Edit event");
    delChoiceDialog.setHeaderText("Select an event:");
    delChoiceDialog.setContentText("Event:");
    Optional<String> delChoiceResult = delChoiceDialog.showAndWait();
    delChoiceResult.ifPresent(event -> {
      CalendarGUIApp.removeEvent(event);
      //this.events.getChildren().removeIf((eeee) -> (eeee.toString().equals(event)));
      //this.evnum -= 1;
    });
    delChoiceDialog.close();
  }
  private void createSaveDialog() {
    Dialog<String> saveDialog = new Dialog<>();
    saveDialog.setTitle("Save Calendar");
    saveDialog.setHeaderText("Save calendar to file");

    // Set the icon (must be included in the project).
    //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

    // Set the button types.
    ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
    saveDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Create the username and password labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField fileName = new TextField();
    fileName.setPromptText("File name");

    grid.add(new Label("File name:"), 0, 0);
    grid.add(fileName, 1, 0);

    // Enable/Disable login button depending on whether a username was entered.
    Node okButton = saveDialog.getDialogPane().lookupButton(okButtonType);
    okButton.setDisable(true);

    //Do some validation (using the Java 8 lambda syntax).
    fileName.textProperty().addListener((observable, oldValue, newValue) -> {
      if ((newValue.trim().isEmpty())) {
          okButton.setDisable(true);
      }
      okButton.setDisable(false);
    });

    saveDialog.getDialogPane().setContent(grid);

    // Request focus on the username field by default.
    Platform.runLater(() -> fileName.requestFocus());

    // Convert the result to a username-password-pair when the login button is clicked.
    saveDialog.setResultConverter(dialogButton -> {
        if (dialogButton == okButtonType) {
            String results = fileName.getText();
            return results;
        }
        return null;
    });

    Optional<String> saveResult = saveDialog.showAndWait();

    saveResult.ifPresent(result -> {
        saveCalendar(result);
    });
    saveDialog.close();
  }
  private void saveCalendar(String file) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
      for (EventPane e : CalendarGUIApp.getEventList()) {
        writer.append(e.saveToString());
      }
      writer.close();
    }
    catch (IOException ex) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText("Error");
      alert.setContentText("There was an error saving the file.");
      alert.showAndWait();
    }
  }
  private void createLoadDialog() {
    Dialog<String> loadDialog = new Dialog<>();
    loadDialog.setTitle("Load Calendar");
    loadDialog.setHeaderText("Load calendar from file");

    // Set the icon (must be included in the project).
    //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

    // Set the button types.
    ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
    loadDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Create the username and password labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField fileName = new TextField();
    fileName.setPromptText("File name");

    grid.add(new Label("File name:"), 0, 0);
    grid.add(fileName, 1, 0);

    // Enable/Disable login button depending on whether a username was entered.
    Node okButton = loadDialog.getDialogPane().lookupButton(okButtonType);
    okButton.setDisable(true);

    //Do some validation (using the Java 8 lambda syntax).
    fileName.textProperty().addListener((observable, oldValue, newValue) -> {
      if ((newValue.trim().isEmpty())) {
          okButton.setDisable(true);
      }
      okButton.setDisable(false);
    });

    loadDialog.getDialogPane().setContent(grid);

    // Request focus on the username field by default.
    Platform.runLater(() -> fileName.requestFocus());

    // Convert the result to a username-password-pair when the login button is clicked.
    loadDialog.setResultConverter(dialogButton -> {
        if (dialogButton == okButtonType) {
            String results = fileName.getText();
            return results;
        }
        return null;
    });

    Optional<String> loadResult = loadDialog.showAndWait();

    loadResult.ifPresent(result -> {
        loadCalendar(result);
    });
    loadDialog.close();
  }
  private void loadCalendar(String file) {
    //String str = "2019-08-06 12:12";
    DateTimeFormatter formatterHM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
    try {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
          String name = line;
          line = reader.readLine();
          String description = line;
          line = reader.readLine();
          String time = line;
          line = reader.readLine();
          LocalDateTime dt = date;
          if (time.length() == 16) {
            dt = LocalDateTime.parse(time, formatterHM);
            CalendarGUIApp.addEvent(new EventPane(name, dt, description));
          }
          else if (time.length() == 19) {
            dt = LocalDateTime.parse(time, formatterHMS);
            CalendarGUIApp.addEvent(new EventPane(name, dt, description));
          }
        }

      }
      catch (NullPointerException ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Error");
        alert.setContentText("There was an error loading the file.");
        alert.showAndWait();
      }
    }
    catch (IOException ex) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error Dialog");
      alert.setHeaderText("Error");
      alert.setContentText("There was an error loading the file.");
      alert.showAndWait();
    }
    CalendarGUIApp.changeTheme(CalendarGUIApp.currentTheme);
  }
}

class EventPane extends StackPane {
  private Text title = new Text();
  private String name;
  private LocalDateTime date;
  private String description;
  private Timer t;

  public EventPane(EventPane e) {
    this(e.getName(), e.getDate(), e.getDescription());
  }
  public EventPane(String name, LocalDateTime date, String description) {

    this.name = name;
    this.date = date;
    this.description = description;
    setUpLayout();
  }
  public void setUpLayout() {
    this.title = new Text(name);
    if (this.title.getLayoutBounds().getWidth() > 60) {
      this.title = new Text(name.substring(0, 5) + "...");
    }
    this.title.setFont(Font.font("Bandeins Sans", FontWeight.THIN, 15.0));
    this.title.setStroke(CalendarGUIApp.currentTheme[11]);
    this.title.setFill(CalendarGUIApp.currentTheme[11]);
    Tooltip tip = new Tooltip(description);
    Tooltip.install(this.title, tip);
    this.getChildren().add(this.title);
    this.setPadding(new Insets(0));
    this.setAlignment(title, Pos.BOTTOM_CENTER);
    this.setMaxWidth(60);
    StackPane.setMargin(title, new Insets(2,8,2,8));
    setUpTimer();
  }
  private void setUpTimer() {
    Calendar calendarSet = Calendar.getInstance();
    Date current = calendarSet.getTime();
    calendarSet.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
    Date timerSet = calendarSet.getTime();
    TimerTask task = new TimerTask() {
      public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
              try {
                Runtime.getRuntime().exec(new String[] { "osascript", "-e", "display notification \"Calendar timer has ended\" with title \"Calendar\" sound name \"default\""});
                timerEndDialog();
              }
              catch (IOException ex) {

              }
            }
        });
        t.cancel();
      }
    };
    this.t = new Timer("Timer");
    long delay = (timerSet.getTime()-current.getTime());
    if (delay >= 0) {
      this.t.schedule(task, delay);
    }
  }
  public String getName() {
    return name;
  }
  public LocalDateTime getDate() {
    return date;
  }
  public String getDescription() {
    return description;
  }
  public String toString() {
    return getName() + " (" + String.format("%02d", getDate().getHour()) + ":" + String.format("%02d", getDate().getMinute()) + ":" + String.format("%02d", getDate().getSecond()) + ")";
  }
  public String saveToString() {
    return getName() + "\n" + getDescription() + "\n" + getDate().toString().replace('T', ' ') + "\n";
  }
  public void timerEndDialog() {
    Dialog<String> timerEnd = new Dialog<>();
    timerEnd.setTitle(this.name + " (" + this.date + ")");
    timerEnd.setHeaderText(this.name);

    // Set the icon (must be included in the project).
    //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

    // Set the button types.
    ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
    timerEnd.getDialogPane().getButtonTypes().addAll(okButtonType);

    // Create the username and password labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    Text desc = new Text(this.description);

    grid.add(desc, 0, 0);

    timerEnd.getDialogPane().setContent(grid);

    // Convert the result to a username-password-pair when the login button is clicked.
    timerEnd.setResultConverter(dialogButton -> {
        if (dialogButton == okButtonType) {

        }
        return null;
    });

    Optional<String> loadResult = timerEnd.showAndWait();

    loadResult.ifPresent(result -> {
    });
    timerEnd.close();
  }
  }

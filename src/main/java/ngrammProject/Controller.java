package ngrammProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;

import static ngrammProject.MainApp.setDataModelList;


public class Controller implements Initializable {

    private static Stage pStage;
    private Desktop desktop = Desktop.getDesktop();
    private static double countText1 = 0;
    private static double countText2 = 0;

    void setPrimaryStage(Stage pStage) {
        MainApp.pStage = pStage;
    }

    @FXML
    private Button button;

    @FXML
    private TextField InputTextField;

    @FXML
    private DirectoryChooser chooser = new DirectoryChooser();

    @FXML
    private Slider modeSlider;

    @FXML
    private TableView<DataModel> dataModelTableView;

    @FXML
    private TableColumn<DataModel, String> columnNgramName;

    @FXML
    private TableColumn<DataModel, Double> columnText1Name;

    @FXML
    private TableColumn<DataModel, Double> columnText2Name;

    @FXML
    private TableColumn<DataModel, Double> columnDifferenceName;

    @FXML
    private TextArea firstText;

    @FXML
    private TextArea secondText;

    @FXML
    private TextField firstTextName;

    @FXML
    private TextField secondTextName;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private TextField sqareDeviation;

    @FXML
    private CheckBox punctuationCheck;

    @FXML
    private CheckBox registerCheck;

    @FXML
    private CheckBox spaceCheck;

    @FXML
    private CheckBox changeSpaceOn_;


    private MainApp mainApp;

    public Controller() {}

    private String getDataFirstText() {
        String result = firstText.getText();
        result = result.replaceAll("[^А-Яа-я -!\",./:;?]", "");
        if (!spaceCheck.isSelected()) {
            result = result.replaceAll("\\s", "");
        }
        if (!registerCheck.isSelected()) {
            result = result.toLowerCase();
        }
        if (!punctuationCheck.isSelected()) {
            result = result.replaceAll("[-!\",./:;?]", "");
        }
        if (changeSpaceOn_.isSelected()) {
            result = result.replaceAll(" ", "_");
        }
        return result;
    }

    private String getDataSecondText() {
        String result = secondText.getText();
        result = result.replaceAll("[^А-Яа-я -!\",./:;?]", "");
        if (!spaceCheck.isSelected()) {
            result = result.replaceAll(" ", "");
        }
        if (!registerCheck.isSelected()) {
            result = result.toLowerCase();
        }
        if (!punctuationCheck.isSelected()) {
            result = result.replaceAll("[-!\",./:;?]", "");
        }
        if (changeSpaceOn_.isSelected()) {
            result = result.replaceAll(" ", "_");
        }
        return result;
    }

    public void doFindNGramm() {
        setDataModelList(ProduceResult());
        inputDataALL();
        dataModelTableView.refresh();
        dataModelTableView.setItems(ProduceResult());
    }

    private ObservableList<DataModel> ProduceResult() {
        ObservableList<DataModel> result = FXCollections.observableArrayList();
        HashMap<String, Integer> insertMap1 = new HashMap<String, Integer>();
        HashMap<String, Integer> insertMap2 = new HashMap<String, Integer>();
        String resultValue = "";
        String key = "";
        double sizeMap1 = 0;
        double sizeMap2 = 0;
        double average1 = 0;
        double average2 = 0;
        double differenceAverage = 0;
        double sqareDeviance = 0;
        int bufferNumber = 0;

        if (modeSlider.getValue() <= 50.0) {
            insertMap1 = findOne(getDataFirstText());
            insertMap2 = findOne(getDataSecondText());
        } else {
            insertMap1 = findBi(getDataFirstText());
            insertMap2 = findBi(getDataSecondText());
        }
        for (Map.Entry<String, Integer> pair1 : insertMap1.entrySet()) {
            sizeMap1 = sizeMap1 + pair1.getValue();
        }
        for (Map.Entry<String, Integer> pair2 : insertMap2.entrySet()) {
            sizeMap2 = sizeMap2 + pair2.getValue();
        }

        textField1.setText(Double.toString(sizeMap1));
        textField2.setText(Double.toString(sizeMap2));

        if (insertMap1.size() > insertMap2.size()) {
            // TODO: extract to separate method 0
            for (Map.Entry<String, Integer> pair : insertMap1.entrySet()) {
                key = pair.getKey();
                if (insertMap2.containsKey(key)) {
                    bufferNumber = insertMap2.get(key);
                }
                average1 = pair.getValue() / sizeMap1;
                if (bufferNumber == 0) {
                    average2 = 0;
                } else {
                    average2 = bufferNumber / sizeMap2;
                }
                differenceAverage = Math.abs(average1 - average2);
                result.add(new DataModel(pair.getKey(), average1, average2, differenceAverage));
                bufferNumber = 0;
                sqareDeviance += differenceAverage * differenceAverage;


            }
        } else {
            // TODO: extract to separate method 0
            for (Map.Entry<String, Integer> pair : insertMap2.entrySet()) {
                key = pair.getKey();
                if (insertMap1.containsKey(key)) {
                    bufferNumber = insertMap1.get(key);
                }
                average2 = pair.getValue() / sizeMap1;
                if (bufferNumber == 0) {
                    average2 = 0;
                } else {
                    average1 = bufferNumber / sizeMap2;
                }
                differenceAverage = Math.abs(average1 - average2);
                result.add(new DataModel(pair.getKey(), average2, average1, differenceAverage));
                bufferNumber = 0;
                sqareDeviance += differenceAverage * differenceAverage;
            }
        }
        sqareDeviation.setText(String.valueOf(sqareDeviance));
        return result;
    }

    private HashMap<String, Integer> findOne(String inputText) {
        inputText = inputText.replaceAll(" ", "");
        HashMap<String, Integer> oneGmap = new HashMap<String, Integer>();
        for (String s : inputText.split("")) {
            Integer frecuency = oneGmap.get(s);
            oneGmap.put(s, frecuency == null ? 1 : frecuency + 1);
        }
        return oneGmap;
    }


    private HashMap<String, Integer> findBi(String inputText) {
        HashMap<String, Integer> biGmap = new HashMap<String, Integer>();
        String tempStr = "";
        String[] bigrammWordArray = inputText.split(" ");

        for (String s : bigrammWordArray) {
            for (int i = 0; i < s.length() - 1; i++) {
                tempStr = s.charAt(i) + "" + s.charAt(i + 1);
                Integer frecuency = biGmap.get(tempStr);
                biGmap.put(tempStr, frecuency == null ? 1 : frecuency + 1);
            }
        }
        return biGmap;
    }

    public void initialize(URL location, ResourceBundle resources) {
        inputDataALL();
    }

    private void inputDataALL() {
        columnNgramName.setCellValueFactory(cellData -> cellData.getValue().ngrammNameProperty());
        columnText1Name.setCellValueFactory(cellData -> cellData.getValue().firsTextAverageProperty().asObject());
        columnText2Name.setCellValueFactory(cellData -> cellData.getValue().secondTextAverageProperty().asObject());
        columnDifferenceName.setCellValueFactory(cellData -> cellData.getValue().differenceProperty().asObject());
    }

    public void inputData() {
        columnNgramName.setCellValueFactory(cellData -> cellData.getValue().ngrammNameProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        dataModelTableView.setItems(mainApp.getData());
    }

    // TODO: naming
    public void getTextFronFile1() {
        // TODO: field name conventions
        FileChooser choose1 = new FileChooser();
        // TODO: move to place with used
        String text = "";
        String filename = "";
        // TODO: field name conventions
        String ClearName = "";
        String Extension = "";
        firstText.clear();
        File file = choose1.showOpenDialog(mainApp.getPrimaryStage());
        if (file == null) {
            return;
        }
        filename = file.getName();
        ClearName = FilenameUtils.removeExtension(filename);
        Extension = FilenameUtils.getExtension(filename);

        // TODO: extract to separate method 1
        if (file != null) {
            if (Extension.equals("doc") || Extension.equals("docx")) {
                try {
                    FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                    HWPFDocument document = new HWPFDocument(fis);
                    WordExtractor extractor = new WordExtractor(document);
                    String[] fileData = extractor.getParagraphText();
                    for (int i = 0; i < fileData.length; i++) {
                        if (fileData[i] != null)
                            text += fileData[i];
                    }
                } catch (Exception exep) {
                    exep.printStackTrace();
                }
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "windows-1251"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        text += line;
                    }
                } catch (IOException e) {}
            }
        }
        firstText.setText(text);
        firstTextName.setText(ClearName);
    }

    public void getTextFronFile2() {
        FileChooser choose1 = new FileChooser();
        String text = "";
        String filename = "";
        String ClearName = "";
        String Extension = "";
        secondText.clear();
        File file = choose1.showOpenDialog(mainApp.getPrimaryStage());
        filename = file.getName();
        ClearName = FilenameUtils.removeExtension(filename);
        Extension = FilenameUtils.getExtension(filename);

        // TODO: extract to separate method 1
        if (file != null) {
            if (Extension.equals("doc") || Extension.equals("docx")) {
                try {
                    FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                    HWPFDocument document = new HWPFDocument(fis);
                    WordExtractor extractor = new WordExtractor(document);
                    String[] fileData = extractor.getParagraphText();
                    for (int i = 0; i < fileData.length; i++) {
                        if (fileData[i] != null)
                            text += fileData[i];
                    }
                } catch (Exception exep) {
                    exep.printStackTrace();
                }
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "windows-1251"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        text += line;
                    }
                } catch (IOException e) {
                }

            }
        }
        secondText.setText(text);
        secondTextName.setText(ClearName);
    }

    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInFile() {
        FileChooser choose = new FileChooser();
        String[] strArray = new String[4];
        ObservableList<DataModel> dataList = ProduceResult();
        File file = choose.showOpenDialog(mainApp.getPrimaryStage());
        // TODO: if true
        if (true) {
            try {
                FileWriter writer = new FileWriter(file.getAbsolutePath());
                for (DataModel modelObject : dataList) {
                    writer.write(modelObject.getNgrammName() + " " + modelObject.getFirsTextAverage() + " " +
                            modelObject.getSecondTextAverage() + " " + modelObject.getDifference() + "\r\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter writer = new FileWriter(file.getAbsolutePath());
                for (int i = 0; i < 4; i++) {
                    for (DataModel modelObject : dataList) {
                        switch (i) {
                            case 0:
                                writer.write(modelObject.getNgrammName() + "\r\n");
                                break;
                            case 1:
                                writer.write(modelObject.getFirsTextAverage() + "\r\n");
                                break;
                            case 2:
                                writer.write(modelObject.getSecondTextAverage() + "\r\n");
                                break;
                            case 3:
                                writer.write(modelObject.getDifference() + "\r\n");
                                break;
                            default:
                                break;
                        }
                    }
                    writer.write("\r\n" + "\r\n" + "\r\n");
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

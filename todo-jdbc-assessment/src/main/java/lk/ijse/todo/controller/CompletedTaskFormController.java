package lk.ijse.todo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.todo.dto.TasksDto;
import lk.ijse.todo.dto.tm.CompleteTm;
import lk.ijse.todo.model.TasksModel;

import java.sql.SQLException;
import java.util.List;

public class CompletedTaskFormController {
    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colDueDate;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CompleteTm> tblComplete;

    private TasksModel tasksModel = new TasksModel();

    public void initialize(){
        setCellValueFactory();
        loadCompletedTasks();
    }

    private void loadCompletedTasks() {
        try {
            ObservableList<CompleteTm> obList = FXCollections.observableArrayList();

            List<TasksDto> dtoList = tasksModel.loadCompletedTasks();

            for (TasksDto dto : dtoList) {
                obList.add(new CompleteTm(dto.getDescription(), dto.getDueDate()));
            }

            for (int i = 0; i < obList.size(); i++) {
                final  int index = i;
                obList.get(i).getBtnDelete().setOnAction(event -> {

                    int taskId = dtoList.get(index).getTaskId();
                    deleteTask(taskId);
                    refreshTable();

                });
            }

            tblComplete.setItems(obList);
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void refreshTable() {
        loadCompletedTasks();
    }

    private void deleteTask(int taskId) {
        try {
            boolean isDeleted = tasksModel.deleteTask(taskId);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION, "Task is deleted").show();
            }
        }
        catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colDescription.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("description"));
        colDueDate.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dueDate"));
        colDelete.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("btnDelete"));
    }

}
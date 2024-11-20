package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class VueActionBas extends HBox {
    @FXML
    private Button Passer,Bateaux,Wagons;

    @FXML
    private TextField textField;
    @FXML
    private VBox boxDest;
    private IJeu jeu;

    public VueActionBas(IJeu jeu){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/vueActionBasFXML.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        textField.setVisible(false);
        this.jeu=jeu;
        Passer.setText("Passer");
        Passer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                jeu.passerAEteChoisi();
            }
        });
        Wagons.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                jeu.nouveauxPionsWagonsDemandes();
                textField.setVisible(true);
            }
        });
        Bateaux.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                jeu.nouveauxPionsBateauxDemandes();
                textField.setVisible(true);
            }
        });
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                jeu.leNombreDePionsSouhaiteAEteRenseigne(textField.getText());
                textField.setVisible(false);
                textField.clear();
            }
        });
    }
    public void creerBindings(){
        jeu.destinationsInitialesProperty().addListener(new ListChangeListener<IDestination>() {
            @Override
            public void onChanged(Change<? extends IDestination> change) {
                while(change.next()){
                    if(change.wasAdded()){
                        for (IDestination d : change.getAddedSubList()) {
                            boxDest.getChildren().add(new VueDestination(d));
                        }
                    }
                    if(change.wasRemoved()){
                        for(IDestination d: change.getRemoved()){
                            boxDest.getChildren().remove((trouveDestination(d)));
                        }
                    }
                }
            }
        });
        AnimationBouton(Passer);
        AnimationBouton(Bateaux);
        AnimationBouton(Wagons);
    }
    private void AnimationBouton(Button b) {
        b.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                b.setOpacity(0.8);
                b.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
                setStyle("-fx-cursor: hand;");
            }
        });
        b.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                b.setOpacity(1);
                b.setStyle("");
                setStyle("-fx-cursor: default");
            }
        });
    }
    public Node trouveDestination(IDestination d){
        for(Node l:boxDest.getChildren()){
            if(((VueDestination)l).getDestination()==d) return l;
        }
        return null;
    }

}

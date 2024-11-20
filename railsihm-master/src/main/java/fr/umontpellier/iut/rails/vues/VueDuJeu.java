package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.io.IOException;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends GridPane {

    private final IJeu jeu;
    private VuePlateau plateau;
    @FXML
    private Label actionAttendue;
    @FXML
    private VBox Gauche;
    @FXML
    private VBox Droite;
    @FXML
    private HBox Bas;
    @FXML
    private VBox Centre;

    public VueDuJeu(IJeu jeu) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueDuJeuFXML.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        this.setBackground(new Background(new BackgroundImage(new Image("images/fond.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        this.jeu = jeu;
        plateau = new VuePlateau(jeu);
        Centre.getChildren().add(plateau);

        VueJoueurCourant vjc=new VueJoueurCourant(jeu);
        vjc.creerBindings();
        Gauche.getChildren().add(vjc);

        VueAutresJoueurs vaj=new VueAutresJoueurs(jeu);
        vaj.creerBindings();
        Gauche.getChildren().add(vaj);

        VueActionBas vab=new VueActionBas(jeu);
        vab.creerBindings();
        Bas.getChildren().add(vab);

        VueChoixCartes vcc=new VueChoixCartes(jeu);
        vcc.creerBindings();
        Droite.getChildren().add(vcc);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();

        jeu.instructionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                actionAttendue.setText(t1);
            }
        });
    }


    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}

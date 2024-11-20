package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class VueJoueur extends HBox {
    @FXML
    private Label nom;
    @FXML
    private ImageView Avatar;
    @FXML
    private Label Score;
    public VueJoueur(IJoueur joueur){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueJoueur.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        nom.setText(joueur.getNom());
        Avatar.setImage(new Image("images/cartesWagons/avatar-"+joueur.getCouleur()+".png"));
        Score.setText("score : "+joueur.getScore());
    }

    public String getNom() {
        return nom.getText();
    }

    public Node getNomJoueur() {
        return nom;
    }

    public Label getScore() {
        return Score;
    }
}

package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends ImageView {

    private final ICarteTransport carteTransport;
    private final int nbCartes;

    public VueCarteTransport(ICarteTransport carteTransport, int nbCartes) {
        this.carteTransport = carteTransport;
        this.nbCartes = nbCartes;
        setImage(new Image(getSourceString(),105,105,true,true));
        setTranslateX(-75*(nbCartes-1));
        setTranslateZ(-10);
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(nbCartes==0) ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteTransportAEteChoisie(carteTransport);
                else ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteDuJoueurEstJouee(carteTransport);
            }
        });
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(nbCartes!=0){
                    setTranslateX(50);
                    setTranslateY(-20);
                }
                scaleYProperty().setValue(1.2);
                scaleXProperty().setValue(1.2);
                setStyle("-fx-cursor: hand");
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scaleYProperty().setValue(1);
                scaleXProperty().setValue(1);
                if(nbCartes!=0){setTranslateX(0);
                setTranslateY(0);}

            }
        });
    }
    public int getNbCartes(){
        return nbCartes;
    }
    public String getStringTypeCarte(){
        if (carteTransport.estDouble())return "DOUBLE";
        if (carteTransport.estBateau()) return "BATEAU";
        if (carteTransport.estWagon()) return "WAGON";
        if (carteTransport.estJoker()) return "JOKER";
        return null;
    }
    public String getSourceString(){
        return "images/cartesWagons/carte-"+getStringTypeCarte()+"-"+carteTransport.getStringCouleur()+".png";
    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }

}

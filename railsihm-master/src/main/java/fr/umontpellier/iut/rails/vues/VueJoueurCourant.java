package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {
    @FXML
    private HBox vueJoueur;
    @FXML
    private VBox cartesDestJoueur;
    private IJeu jeu;
    @FXML
    private VBox CartesTransAffichées;
    @FXML
    private Label nbPionsWagons,nbPionsBateaux;
    @FXML
    private VBox CartesPosées;
    private int spacing;
    public VueJoueurCourant(IJeu j){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueJoueurCourantFXML.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        jeu=j;
        setBackground(new Background(new BackgroundImage(new Image("images/background/parchemin.png",340,616,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));

        CartesTransAffichées.setSpacing(-40);
        CartesPosées.setSpacing(-20);
    }
    public void creerBindings(){
        jeu.joueurCourantProperty().addListener(new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur iJoueur, IJoueur t2) {
                CartesTransAffichées.getChildren().clear();
                CartesPosées.getChildren().clear();
                for (ICarteTransport c:t2.cartesTransportProperty()){
                    CartesTransAffichées.getChildren().add(new VueCarteTransport(c,1));
                }
                cartesDestJoueur.getChildren().clear();
                for(IDestination dest:t2.getDestinations()){
                    cartesDestJoueur.getChildren().add(new VueDestination(dest));
                }
                nbPionsBateaux.setText("Bateaux : " + t2.getNbPionsBateau());
                nbPionsWagons.setText("Wagons : " + t2.getNbPionsWagon());
                vueJoueur.getChildren().clear();
                vueJoueur.getChildren().add(new VueJoueur(t2));
            }
        });
        for(IJoueur t1:jeu.getJoueurs()){
            t1.cartesTransportProperty().addListener(new ListChangeListener<ICarteTransport>() {
                @Override
                public void onChanged(Change<? extends ICarteTransport> change) {
                    while (change.next()){
                        if(change.wasAdded()){
                            for(ICarteTransport c:change.getAddedSubList()){
                                CartesTransAffichées.getChildren().add(new VueCarteTransport(c,1));
                            }
                        }
                        if(change.wasRemoved()){
                            for(ICarteTransport c:change.getRemoved()){
                                CartesTransAffichées.getChildren().remove(trouveLabelCarteTransport(c));
                            }
                        }
                    }
                }
            });
            t1.cartesTransportPoseesProperty().addListener(new ListChangeListener<CarteTransport>() {
                @Override
                public void onChanged(Change<? extends CarteTransport> change) {
                    while (change.next()){
                        if(change.wasAdded()){
                            for(ICarteTransport c:change.getAddedSubList()){
                                CartesPosées.getChildren().add(new VueCarteTransport(c,1));
                            }
                        }
                        if(change.wasRemoved()){
                            for(ICarteTransport c:change.getRemoved()){
                                CartesPosées.getChildren().remove(trouveLabelCarteTransport(c));
                            }
                        }
                    }
                }
            });
        }
    }


    public static String getEnglish(String couleur) {
        if(couleur=="BLEU")return "LIGHTBLUE";
        if(couleur=="VERT")return "LIGHTGREEN";
        if(couleur=="ROUGE")return "#d75d4e";
        if(couleur=="ROSE")return "LIGHTPINK";
        if(couleur=="JAUNE")return "LIGHTYELLOW";
        return null;

    }

    private Node trouveLabelCarteTransport(ICarteTransport c) {
        for(Node l:CartesTransAffichées.getChildren()){
            if(((VueCarteTransport)l).getCarteTransport()==c) return l;
        }
        return null;
    }
}

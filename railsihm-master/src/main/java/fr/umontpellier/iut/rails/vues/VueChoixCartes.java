package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJeu;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static javafx.beans.binding.Bindings.when;

public class VueChoixCartes extends VBox {
    @FXML
    private ImageView piocheDest,piocheWagon,piocheBateau;
    @FXML
    private VBox boxCartesVisibles;
    private IJeu jeu;

    public VueChoixCartes(IJeu j){
        jeu=j;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/vueChoixCartesFXML.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void creerBindings(){
        jeu.cartesTransportVisiblesProperty().addListener(new ListChangeListener<ICarteTransport>() {
            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
                while(change.next()){
                    if(change.wasAdded()){
                        for(ICarteTransport c:change.getAddedSubList()){
                            boxCartesVisibles.getChildren().add(new VueCarteTransport(c,0));
                        }
                    }
                    if (change.wasRemoved()) {
                            for (ICarteTransport c : change.getRemoved()) {
                                boxCartesVisibles.getChildren().remove(trouveLabelCarteTransport(c));
                            }
                        }
                }
            }
        });
        piocheBateau.imageProperty().bind(when(jeu.piocheBateauVideProperty()).then(new Image("file:src/main/resources/images/cartesWagons/dos-BATEAUVide.png")).otherwise(new Image("file:src/main/resources/images/cartesWagons/dos-BATEAU.png")));
        piocheWagon.imageProperty().bind(when(jeu.piocheWagonVideProperty()).then(new Image("file:src/main/resources/images/cartesWagons/dos-WAGONVide.png")).otherwise(new Image("file:src/main/resources/images/cartesWagons/dos-WAGON.png")));
        piocheDest.imageProperty().bind(when(jeu.piocheDestinationVideProperty()).then(new Image("file:src/main/resources/images/cartesWagons/destinationsVide.png")).otherwise(new Image("file:src/main/resources/images/cartesWagons/destinations.png")));
        AnimationPioche(piocheBateau);
        AnimationPioche(piocheDest);
        AnimationPioche(piocheWagon);
        piocheBateau.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                jeu.uneCarteBateauAEtePiochee();
            }
        });
        piocheDest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                jeu.nouvelleDestinationDemandee();
            }
        });
        piocheWagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                jeu.uneCarteWagonAEtePiochee();
            }
        });
    }

    private void AnimationPioche(ImageView b) {
        b.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                b.setOpacity(0.8);
                setStyle("-fx-cursor: hand;");
            }
        });
        b.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                b.setOpacity(1);
                setStyle("-fx-cursor: default;");
            }
        });
    }

    private Node trouveLabelCarteTransport(ICarteTransport c) {
        for(Node l:boxCartesVisibles.getChildren()){
            if(((VueCarteTransport)l).getCarteTransport()==c) return l;
        }
        return null;
    }
}

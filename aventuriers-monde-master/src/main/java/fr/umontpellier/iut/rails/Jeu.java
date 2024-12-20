package fr.umontpellier.iut.rails;

import com.google.gson.Gson;
import fr.umontpellier.iut.gui.GameServer;
import fr.umontpellier.iut.rails.data.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Jeu implements Runnable {
    /**
     * Liste des joueurs
     */
    private final List<Joueur> joueurs;
    /**
     * Le joueur dont c'est le tour
     */
    private Joueur joueurCourant;
    /**
     * Liste des villes disponibles sur le plateau de jeu
     */
    private final List<Ville> portsLibres;
    /**
     * Liste des routes disponibles sur le plateau de jeu
     */
    private final List<Route> routesLibres;
    /**
     * Pile de pioche et défausse des cartes wagon
     */
    private final PilesCartesTransport pilesDeCartesWagon;
    /**
     * Pile de pioche et défausse des cartes bateau
     */
    private final PilesCartesTransport pilesDeCartesBateau;
    /**
     * Cartes de la pioche face visible (normalement il y a 6 cartes face visible)
     */
    private final List<CarteTransport> cartesTransportVisibles;
    /**
     * Pile des cartes "Destination"
     */
    private final List<Destination> pileDestinations;
    /**
     * File d'attente des instructions recues par le serveur
     */
    private final BlockingQueue<String> inputQueue;
    /**
     * Messages d'information du jeu
     */
    private final List<String> log;
    private String instruction;
    private Collection<Bouton> boutons;

    public Jeu(String[] nomJoueurs) {
        // initialisation des entrées/sorties
        inputQueue = new LinkedBlockingQueue<>();
        log = new ArrayList<>();

        // création des villes et des routes
        Plateau plateau = Plateau.makePlateauMonde();
        portsLibres = plateau.getPorts();
        routesLibres = plateau.getRoutes();

        // création des piles de pioche et défausses des cartes Transport (wagon et
        // bateau)
        ArrayList<CarteTransport> cartesWagon = new ArrayList<>();
        ArrayList<CarteTransport> cartesBateau = new ArrayList<>();
        for (Couleur c : Couleur.values()) {
            if (c == Couleur.GRIS) {
                continue;
            }
            for (int i = 0; i < 4; i++) {
                // Cartes wagon simples avec une ancre
                cartesWagon.add(new CarteTransport(TypeCarteTransport.WAGON, c, false, true));
            }
            for (int i = 0; i < 7; i++) {
                // Cartes wagon simples sans ancre
                cartesWagon.add(new CarteTransport(TypeCarteTransport.WAGON, c, false, false));
            }
            for (int i = 0; i < 4; i++) {
                // Cartes bateau simples (toutes avec une ancre)
                cartesBateau.add(new CarteTransport(TypeCarteTransport.BATEAU, c, false, true));
            }
            for (int i = 0; i < 6; i++) {
                // Cartes bateau doubles (toutes sans ancre)
                cartesBateau.add(new CarteTransport(TypeCarteTransport.BATEAU, c, true, false));
            }
        }
        for (int i = 0; i < 14; i++) {
            // Cartes wagon joker
            cartesWagon.add(new CarteTransport(TypeCarteTransport.JOKER, Couleur.GRIS, false, true));
        }
        pilesDeCartesWagon = new PilesCartesTransport(cartesWagon);
        pilesDeCartesBateau = new PilesCartesTransport(cartesBateau);

        // création de la liste pile de cartes transport visibles
        // (les cartes seront retournées plus tard, au début de la partie dans run())
        cartesTransportVisibles = new ArrayList<>();

        // création des destinations
        pileDestinations = Destination.makeDestinationsMonde();
        Collections.shuffle(pileDestinations);

        // création des joueurs
        ArrayList<Joueur.CouleurJouer> couleurs = new ArrayList<>(Arrays.asList(Joueur.CouleurJouer.values()));
        Collections.shuffle(couleurs);
        joueurs = new ArrayList<>();
        for (String nomJoueur : nomJoueurs) {
            joueurs.add(new Joueur(nomJoueur, this, couleurs.remove(0)));
        }
        this.joueurCourant = joueurs.get(0);
    }
    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public List<Ville> getPortsLibres() {
        return new ArrayList<>(portsLibres);
    }
    public void retirerRoute(Route r){
        routesLibres.remove(r);
    }
    public void retirerPort(Ville v){
        portsLibres.remove(v);
    }
    public List<Route> getRoutesLibres() {
        return new ArrayList<>(routesLibres);
    }

    public List<CarteTransport> getCartesTransportVisibles() {
        return new ArrayList<>(cartesTransportVisibles);
    }
    public boolean DestinationVide(){
        return pileDestinations.isEmpty();
    }
    public int getTaillePileDest(){
        return pileDestinations.size();
    }
    public void MettreAJour(CarteTransport carteSelect){
        cartesTransportVisibles.remove(carteSelect);
        if (pilesDeCartesBateau.estVide()){
            if (!pilesDeCartesWagon.estVide()){
                cartesTransportVisibles.add(pilesDeCartesWagon.piocher());

            }
        } else if (pilesDeCartesWagon.estVide()){
            if (!pilesDeCartesBateau.estVide()){
                cartesTransportVisibles.add(pilesDeCartesBateau.piocher());
            }
        } else {
            String choix= joueurCourant.bateauOuWagon();
            if(choix.equals("BATEAU")){
                cartesTransportVisibles.add(piocherCarteBateau());
            }
            else{
                cartesTransportVisibles.add(piocherCarteWagon());
            }
        }
        int nbJoker = 0;
        for (CarteTransport Carte: cartesTransportVisibles){
            if (Carte.getType().equals("JOKER")){
                nbJoker++;
                if (nbJoker >= 3){
                    cartesTransportVisibles.clear();
                    for (int nb = 0; nb < 3; nb++){
                        CarteTransport Transporttiré1 = this.piocherCarteWagon();
                        CarteTransport Transporttiré2 = this.piocherCarteBateau();
                        cartesTransportVisibles.add(Transporttiré1);
                        cartesTransportVisibles.add(Transporttiré2);
                    }
                }
            }
        }
    }
    /**
     * Exécute la partie
     *
     * C'est cette méthode qui est appelée pour démarrer la partie. Elle doit intialiser le jeu
     * (retourner les cartes transport visibles, puis demander à chaque joueur de choisir ses destinations initiales
     * et le nombre de pions wagon qu'il souhaite prendre) puis exécuter les tours des joueurs en appelant la
     * méthode Joueur.jouerTour() jusqu'à ce que la condition de fin de partie soit réalisée.
     */
    public void run() {
        //initialisation partie
        for (int nb = 0; nb < 3; nb++){
            CarteTransport Transporttiré1 = this.piocherCarteWagon();
            CarteTransport Transporttiré2 = this.piocherCarteBateau();
            cartesTransportVisibles.add(Transporttiré1);
            cartesTransportVisibles.add(Transporttiré2);
        }
        for(Joueur j: joueurs){
            joueurCourant=j;
            joueurCourant.initCartesTrans();
            joueurCourant.CartesDest(3,5);
            joueurCourant.initNbPions();
        }
        boolean fin=false;
        boolean premierAFini=false;
        int derniersTours=0;
        int nbTours=1;
        int indicePremierJoueur=0;
        int i;
        Joueur j;
        while(derniersTours<2){
            clearLog();
            log("Tour " + nbTours);
            i=indicePremierJoueur;
            if(fin){derniersTours++;}
            for(int x=0;x<joueurs.size();x++){
                j=joueurs.get(i);
                joueurCourant = j;
                joueurCourant.jouerTour();
                if(!fin) {
                    fin = joueurCourant.aTerminé();
                }
                if(fin && !premierAFini){
                    if(i!=joueurs.size()-1){indicePremierJoueur=i+1;}
                    premierAFini=true;
                    x=joueurs.size();
                }
                else if(fin && i==joueurs.size()-1){i=0;}
                else{i++;}
            }
            nbTours++;
        }
        clearLog();
        scores();
        // Fin de la partie
        prompt(" Fin de la partie.", new ArrayList<>(), true);
    }

    public void scores(){
        for(Joueur j:joueurs){
            String str="le score de "+j.getNom()+" est de "+j.calculerScoreFinal();
            log(str);
        }
    }



    /**
     * Pioche une carte de la pile de pioche des cartes wagon.
     *
     * @return la carte qui a été piochée (ou null si aucune carte disponible)
     */
    public CarteTransport piocherCarteWagon() {
        return pilesDeCartesWagon.piocher();
    }

    public boolean piocheWagonEstVide() {
        return pilesDeCartesWagon.estVide();

    }

    /**
     * Pioche une carte de la pile de pioche des cartes bateau.
     *
     * @return la carte qui a été piochée (ou null si aucune carte disponible)
     */
    public CarteTransport piocherCarteBateau() {
        return pilesDeCartesBateau.piocher();
    }

    public boolean piocheBateauEstVide() {
        return pilesDeCartesBateau.estVide();
    }
    public Destination piocherCarteDestination() {
        return this.pileDestinations.remove(0);
    }
    public void remettreDestination(Destination destination){
        this.pileDestinations.add(destination);
    }
    public void defausser(CarteTransport carte){
        if(carte.getType().equals(TypeCarteTransport.BATEAU)){
            pilesDeCartesBateau.defausser(carte);
        }
        else{
            pilesDeCartesWagon.defausser(carte);
        }
    }


    /**
     * Ajoute un message au log du jeu
     */
    public void log(String message) {
        log.add(message);
    }
    public void clearLog(){
        log.clear();
    }

    /**
     * Ajoute un message à la file d'entrées
     */
    public void addInput(String message) {
        inputQueue.add(message);
    }

    /**
     * Lit une ligne de l'entrée standard
     * C'est cette méthode qui doit être appelée à chaque fois qu'on veut lire
     * l'entrée clavier de l'utilisateur (par exemple dans {@code Player.choisir})
     *
     * @return une chaîne de caractères correspondant à l'entrée suivante dans la
     * file
     */
    public String lireLigne() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Envoie l'état de la partie pour affichage aux joueurs avant de faire un choix
     *
     * @param instruction l'instruction qui est donnée au joueur
     * @param boutons     labels des choix proposés s'il y en a
     * @param peutPasser  indique si le joueur peut passer sans faire de choix
     */
    public void prompt(String instruction, Collection<Bouton> boutons, boolean peutPasser) {
        this.instruction = instruction;
        this.boutons = boutons;

        System.out.println();
        System.out.println(this);
        if (boutons.isEmpty()) {
            System.out.printf(">>> %s: %s <<<\n", joueurCourant.getNom(), instruction);
        } else {
            StringJoiner joiner = new StringJoiner(" / ");
            for (Bouton bouton : boutons) {
                joiner.add(bouton.toPrompt());
            }
            System.out.printf(">>> %s: %s [%s] <<<\n", joueurCourant.getNom(), instruction, joiner);
        }
        GameServer.setEtatJeu(new Gson().toJson(dataMap()));
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        for (Joueur j : joueurs) {
            joiner.add(j.toString());
        }
        return joiner.toString();
    }

    public Map<String, Object> dataMap() {
        return Map.ofEntries(
                Map.entry("joueurs", joueurs.stream().map(Joueur::dataMap).toList()),
                Map.entry("joueurCourant", joueurs.indexOf(joueurCourant)),
                Map.entry("piocheWagon", pilesDeCartesWagon.dataMap()),
                Map.entry("piocheBateau", pilesDeCartesBateau.dataMap()),
                Map.entry("cartesTransportVisibles", cartesTransportVisibles),
                Map.entry("nbDestinations", pileDestinations.size()),
                Map.entry("instruction", instruction),
                Map.entry("boutons", boutons),
                Map.entry("log", log));
    }
}

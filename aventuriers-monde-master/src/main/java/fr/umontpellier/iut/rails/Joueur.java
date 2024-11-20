package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.*;

import java.util.*;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class Joueur {
    public enum CouleurJouer {
        JAUNE, ROUGE, BLEU, VERT, ROSE;
    }

    /**
     * Jeu auquel le joueur est rattaché
     */
    private final Jeu jeu;
    /**
     * Nom du joueur
     */
    private final String nom;
    /**
     * CouleurJouer du joueur (pour représentation sur le plateau)
     */
    private final CouleurJouer couleur;
    /**
     * Liste des villes sur lesquelles le joueur a construit un port
     */
    private final List<Ville> ports;
    /**
     * Liste des routes capturées par le joueur
     */
    private final List<Route> routes;
    /**
     * Nombre de pions wagons que le joueur peut encore poser sur le plateau
     */
    private int nbPionsWagon;
    /**
     * Nombre de pions wagons que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsWagonEnReserve;
    /**
     * Nombre de pions bateaux que le joueur peut encore poser sur le plateau
     */
    private int nbPionsBateau;
    /**
     * Nombre de pions bateaux que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsBateauEnReserve;
    /**
     * Liste des destinations à réaliser pendant la partie
     */
    private final List<Destination> destinations;
    /**
     * Liste des cartes que le joueur a en main
     */
    private final List<CarteTransport> cartesTransport;
    /**
     * Liste temporaire de cartes transport que le joueur est en train de jouer pour
     * payer la capture d'une route ou la construction d'un port
     */
    private final List<CarteTransport> cartesTransportPosees;
    /**
     * Score courant du joueur (somme des valeurs des routes capturées, et points
     * perdus lors des échanges de pions)
     */
    private int score;

    public Joueur(String nom, Jeu jeu, CouleurJouer couleur) {
        this.nom = nom;
        this.jeu = jeu;
        this.couleur = couleur;
        this.ports = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.nbPionsWagon = 0;
        this.nbPionsWagonEnReserve = 0;
        this.nbPionsBateau = 0;
        this.nbPionsBateauEnReserve = 0;
        this.cartesTransport = new ArrayList<>();
        this.cartesTransportPosees = new ArrayList<>();
        this.destinations = new ArrayList<>();
        this.score = 0;
    }

    public String getNom() {
        return nom;
    }
    public int GetnbPionsWagon() {
        return nbPionsWagon;
    }
    public int GetnbPionsBateau() {
        return nbPionsBateau;
    }
    public int GetnbWagonsEnReserve() {
        return nbPionsWagonEnReserve;
    }
    public int GetnbBateauEnReserve() {
        return nbPionsBateauEnReserve;
    }
    public void setPionsWagons(int PionWagon){
        this.nbPionsWagon = PionWagon;
    }
    public void setPionsBateau(int PionBateau){
        this.nbPionsBateau = PionBateau;
    }
    public void setPionsWagonsEnReserve(int PionBateau){
        this.nbPionsWagonEnReserve = PionBateau;
    }
    public void setPionsBateauEnReserve(int PionBateau){
        this.nbPionsBateauEnReserve = PionBateau;
    }

    /**
     * Cette méthode est appelée à tour de rôle pour chacun des joueurs de la partie.
     * Elle doit réaliser un tour de jeu, pendant lequel le joueur a le choix entre 5 actions possibles :
     *  - piocher des cartes transport (visibles ou dans la pioche)
     *  - échanger des pions wagons ou bateau
     *  - prendre de nouvelles destinations
     *  - capturer une route
     *  - construire un port
     */
    void jouerTour() {
        // IMPORTANT : Le corps de cette fonction est à réécrire entièrement
        // Un exemple très simple est donné pour illustrer l'utilisation de certaines méthodes


            List<Bouton> Boutons = new ArrayList<Bouton>();
            if (nbPionsBateauEnReserve > 0 && nbPionsWagon > 0) {
                Boutons.add(new Bouton("PIONS BATEAU"));
            }
            if (nbPionsWagonEnReserve > 0 && nbPionsBateau > 0) {
                Boutons.add(new Bouton("PIONS WAGON"));
            }

            List<String> routes = listeRoutesCapturables();
            List<String> ports = listePortsCapturables();
            List<String> transport = ChoixT();
            List<String> reponsesPossibles = new ArrayList<>();
            reponsesPossibles.addAll(routes);
            reponsesPossibles.addAll(ports);
            reponsesPossibles.addAll(transport);
            if (!jeu.DestinationVide()) {
                reponsesPossibles.add("DESTINATION");
            }
            String choix = choisir(
                    "A vous de Jouer: ",
                    reponsesPossibles,
                    Boutons,
                    true);
            //__________________Passez son tour____________________\\
            if (choix.equals("")) {
                log("tour passé");
            }
            //__________________Cartes Transport____________________\\
            else if (transport.contains(choix)) {
                int CarteTirés = 0;
                boolean terminé = false;
                boolean commencé = false;
                while (!terminé) {
                    if (commencé) {
                        choix = choisir(
                                "Piochez une carte transport",
                                ChoixT(),
                                null,
                                CarteTirés == 1);
                    }
                    commencé = true;
                    if (choix.equals("")) {
                        terminé = true;
                    } else if (choix.equals("WAGON")) {
                        this.cartesTransport.add(this.jeu.piocherCarteWagon());
                        CarteTirés++;
                    } else if (choix.equals("BATEAU")) {
                        this.cartesTransport.add(this.jeu.piocherCarteBateau());
                        CarteTirés++;
                    } else {
                        for (CarteTransport c : jeu.getCartesTransportVisibles()) {
                            if (c.getNom().equals(choix)) {
                                if (c.getType().equals(TypeCarteTransport.JOKER)) {
                                    if (CarteTirés == 0) {
                                        terminé = true;
                                        cartesTransport.add(c);
                                        CarteTirés++;
                                        jeu.MettreAJour(c);
                                    } else {
                                        log("INTERDIT!!!!!!!!");
                                    }
                                } else {
                                    cartesTransport.add(c);
                                    CarteTirés++;
                                    jeu.MettreAJour(c);
                                }
                            }
                        }
                    }
                    if (CarteTirés == 2) {
                        terminé = true;
                    }
                }
                log(nom + ": Cartes transport récupérées");
            }
            //__________________Cartes Destination____________________\\
            else if (choix.equals("DESTINATION")) {
                CartesDest(1, 4);
                log(nom + ": cartes destinations piochées");
            }
            //__________________Echanges Pions____________________\\
            else if (choix.equals("PIONS WAGON") || choix.equals("PIONS BATEAU")) {
                if (choix.equals("PIONS WAGON")) {
                    String strNombreDeWagon = choisir("combien de wagons prendre dans la reserve?", ChoixPions("WAGON"), null, false);
                    int Nombre = parseInt(strNombreDeWagon, 10);
                    setPionsWagons(GetnbPionsWagon() + Nombre);
                    setPionsBateau(GetnbPionsBateau() - Nombre);
                    setPionsWagonsEnReserve(GetnbWagonsEnReserve() - Nombre);
                    setPionsBateauEnReserve(GetnbBateauEnReserve() + Nombre);
                    score -= Nombre;
                } else if (choix.equals("PIONS BATEAU")) {
                    String strNombreDeWagon = choisir("combien de bateaux prendre dans la reserve?", ChoixPions("BATEAU"), null, false);
                    int Nombre = parseInt(strNombreDeWagon, 10);
                    setPionsWagons(GetnbPionsWagon() - Nombre);
                    setPionsBateau(GetnbPionsBateau() + Nombre);
                    setPionsBateauEnReserve(GetnbBateauEnReserve() - Nombre);
                    setPionsWagonsEnReserve(GetnbWagonsEnReserve() + Nombre);
                    score -= Nombre;
                }
                log(nom + ": Pions échangés");
            }
            //__________________Capture route____________________\\
            else if (routes.contains(choix)) {
                for (Route r : jeu.getRoutesLibres()) {
                    if (choix.equals(r.getNom())) {
                        capturerRoute(r);
                        if (r.estMaritime()) {
                            setPionsBateau(nbPionsBateau - r.getLongueur());
                        } else {
                            setPionsWagons(nbPionsWagon - r.getLongueur());
                        }
                    }
                }
                log(nom + ": route capturée");
            }
            //__________________Capture Port____________________\\
            else if (ports.contains(choix)) {
                for (Ville d : this.jeu.getPortsLibres()) {
                    if (choix.equals(d.getNom())) {
                        capturerPorts(d);
                        this.ports.add(d);
                        this.jeu.retirerPort(d);
                    }
                }
                log(nom + ": Ports capturée");
            }
        }

    /**
     * Attend une entrée de la part du joueur (au clavier ou sur la websocket) et
     * renvoie le choix du joueur.
     *
     * Cette méthode lit les entrées du jeu (`Jeu.lireligne()`) jusqu'à ce
     * qu'un choix valide (un élément de `choix` ou de `boutons` ou
     * éventuellement la chaîne vide si l'utilisateur est autorisé à passer) soit
     * reçu.
     * Lorsqu'un choix valide est obtenu, il est renvoyé par la fonction.
     *
     * Exemple d'utilisation pour demander à un joueur de répondre à une question
     * par "oui" ou "non" :
     *
     * ```
     * List<String> choix = Arrays.asList("Oui", "Non");
     * String input = choisir("Voulez-vous faire ceci ?", choix, null, false);
     * ```
     *
     * Si par contre on voulait proposer les réponses à l'aide de boutons, on
     * pourrait utiliser :
     *
     * ```
     * List<Bouton> boutons = Arrays.asList(new Bouton("Un", "1"), new Bouton("Deux", "2"), new Bouton("Trois", "3"));
     * String input = choisir("Choisissez un nombre.", null, boutons, false);
     * ```
     *
     * @param instruction message à afficher à l'écran pour indiquer au joueur la
     *                    nature du choix qui est attendu
     * @param choix       une collection de chaînes de caractères correspondant aux
     *                    choix valides attendus du joueur
     * @param boutons     une collection de `Bouton` représentés par deux String (label,
     *                    valeur) correspondant aux choix valides attendus du joueur
     *                    qui doivent être représentés par des boutons sur
     *                    l'interface graphique (le label est affiché sur le bouton,
     *                    la valeur est ce qui est envoyé au jeu quand le bouton est
     *                    cliqué)
     * @param peutPasser  booléen indiquant si le joueur a le droit de passer sans
     *                    faire de choix. S'il est autorisé à passer, c'est la
     *                    chaîne de caractères vide ("") qui signifie qu'il désire
     *                    passer.
     * @return le choix de l'utilisateur (un élement de `choix`, ou la valeur
     * d'un élément de `boutons` ou la chaîne vide)
     */
    public String bateauOuWagon(){
        List<String> list = new ArrayList<>();
        if(!jeu.piocheBateauEstVide()){
            list.add("BATEAU");
        }
        if(!jeu.piocheWagonEstVide()){
            list.add("WAGON");
        }
        return choisir("Quelle carte retourner",list,null,false);
    }
    public void CartesDest(int Min, int nbCarteTirés) {
        List<Bouton> BountonsDestinations=new ArrayList<>();
        ArrayList<Destination> ListDest = new ArrayList<Destination>();
        if(jeu.getTaillePileDest()<nbCarteTirés){nbCarteTirés=jeu.getTaillePileDest();}
        for (int i = 0; i < nbCarteTirés; i++) {
            Destination destinationtirée = jeu.piocherCarteDestination();
            ListDest.add(destinationtirée);
            BountonsDestinations.add(new Bouton(destinationtirée.toString(),destinationtirée.getNom()));
        }
        String choix=" ";
        do{
            choix= choisir("Quelles cartes défausser?",null,BountonsDestinations,true);
            int i=0;
            boolean trouve=false;
            if(!choix.equals("")){
                while (!trouve) {
                    Destination dest = ListDest.get(i);
                    if (dest.getNom().equals(choix)) {
                        ListDest.remove((dest));
                        jeu.remettreDestination(dest);
                        BountonsDestinations.remove(new Bouton(dest.toString(), dest.getNom()));
                        trouve = true;
                    }
                    i++;
                }
            }
        }while(!choix.equals("") && ListDest.size()>Min);
        for (int i = 0; i< ListDest.size(); i++) {
            destinations.add(ListDest.get(i));
        }
    }

    public void initCartesTrans(){
        for(int i=0;i<3;i++){
            cartesTransport.add(jeu.piocherCarteWagon());
        }
        for(int i=0;i<7;i++){
            cartesTransport.add(jeu.piocherCarteBateau());
        }
    }
    public void initNbPions(){
        List<String> nombres=Arrays.asList("10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25");
        String strNombreDeWagon= choisir("combien de wagon garder? (10-25)",nombres,null,false);
        int nombreDeWagon=parseInt(strNombreDeWagon,10);
        setPionsWagons(nombreDeWagon);
        setPionsBateau(60-nombreDeWagon);
        setPionsWagonsEnReserve(25-nombreDeWagon);
        setPionsBateauEnReserve(50-(60-nombreDeWagon));
    }
    public boolean aTerminé(){
        return GetnbPionsBateau()+GetnbPionsWagon()<=6;
    }
    public List<String> ChoixT() {
        List<String> choix = new ArrayList<>();
        if(!jeu.piocheBateauEstVide()){choix.add("BATEAU");}
        if(!jeu.piocheWagonEstVide()){choix.add("WAGON");}
        for(CarteTransport c:jeu.getCartesTransportVisibles()){
            choix.add(c.getNom());
        }
        return choix;
    }
    public List<String> ChoixPions(String type){
        List<String> list=new ArrayList<>();
        if(type.equals("BATEAU")){
            for(int i=1;i<=min(nbPionsBateauEnReserve,nbPionsWagon);i++){
                list.add(""+i);
            }
        }
        else{
            for(int i=1;i<=min(nbPionsBateau,nbPionsWagonEnReserve);i++){
                list.add(""+i);
            }
        }
        return list;

    }
    public List<String> listeRoutesCapturables(){
        List<String> list=new ArrayList<>();
        int[][] carteParCouleur=getCarteParcouleur();
        int nbJoker;
        int i;
        int nb=0;
        int nbMaxBateau = 0;
        int nbMaxWagon = 0;
        for(int x:carteParCouleur[0]){
            if(x>nbMaxWagon){
                nbMaxWagon=x;
            }
        }
        for(int x:carteParCouleur[2]){
            if(x>nbMaxBateau){
                nbMaxBateau=x;
            }
        }
        for(Route r:jeu.getRoutesLibres()){
            if(r.getCouleur()==Couleur.GRIS && !r.estTerrestrePaire()){
                if(r.estTerrestre() && r.getLongueur()<=nbMaxWagon+getnbJoker() && r.getLongueur()<=nbPionsWagon){
                    list.add(r.getNom());
                }
                else if(r.estMaritime() && r.getLongueur()<=nbMaxBateau+getnbJoker() && r.getLongueur()<=nbPionsBateau){
                    list.add(r.getNom());
                }

            }
            else if (r.estTerrestrePaire()){
                nbJoker=getnbJoker();
                for(int x:carteParCouleur[0]){
                    nb+=x/2;
                    if(x%2==1 && nbJoker>0 ){
                        nb++;
                        nbJoker--;
                    }
                }
                nb+=nbJoker/2;
                if(r.getLongueur()<=nb && r.getLongueur()<=nbPionsWagon/2){
                    list.add(r.getNom());
                }
            }
            else{
                nbJoker=getnbJoker();
                i=getIndice(r.getCouleur());
                if(r.estMaritime() && r.getLongueur()<=carteParCouleur[2][i]+nbJoker && r.getLongueur()<=nbPionsBateau ){
                    list.add(r.getNom());
                }
                else if (r.estTerrestre() && r.getLongueur()<=carteParCouleur[0][i]+nbJoker && r.getLongueur()<=nbPionsWagon) {
                    list.add(r.getNom());
                }

            }
        }
        return list;
    }
    public List<String> listePortsCapturables(){
        List<String> listPort = new ArrayList<>();
        int[][] carteParCouleur=getCarteParcouleur();
        int nbJoker;
        if(this.ports.size() < 3) {
            for (Ville v : jeu.getPortsLibres()) {
                nbJoker = getnbJoker();
                for (Route r : this.routes) {
                    if (r.getVille1() == v || r.getVille2() == v) {
                        for (int i = 0; i < 6; i++) {
                            if (carteParCouleur[3][i] + nbJoker >= 2 && carteParCouleur[1][i] + nbJoker >= 2) {
                                listPort.add(v.getNom());
                            }
                        }
                    }
                }
            }
        }
        return listPort;
    }
    public int getIndice(Couleur coul){
        if(coul.equals(Couleur.JAUNE)){
            return 0;
        }else if(coul.equals(Couleur.ROUGE)){
            return 1;
        }else if(coul.equals(Couleur.VERT)){
            return 2;
        }else if(coul.equals(Couleur.BLANC)){
            return 3;
        }else if(coul.equals(Couleur.VIOLET)){
            return 4;
        }else if(coul.equals(Couleur.NOIR)){
            return 5;
        }
        return -1;
    }
    public int[][] getCarteParcouleur(){
        int[][] mat=new int[4][6];
        int i;
        for(CarteTransport c:cartesTransport){
            if(c.getType()==TypeCarteTransport.JOKER){continue;}
            i=getIndice(c.getCouleur());
            if(c.getType()==TypeCarteTransport.WAGON && c.getAncre()){
                mat[0][i]++;
                mat[1][i]++;
            }
            else if(c.getType()==TypeCarteTransport.WAGON && !c.getAncre()){
                mat[0][i]++;
            }
            else if(c.getType()==TypeCarteTransport.BATEAU && c.estDouble()){
                mat[2][i]+=2;
            }
            else if(c.getType()==TypeCarteTransport.BATEAU && !c.estDouble()){
                mat[3][i]++;
                mat[2][i]++;
            }
        }
        return mat;
    }
    public int getnbJoker(){
        int nb=0;
        for(CarteTransport c:cartesTransport){
            if(c.getType().equals(TypeCarteTransport.JOKER)){
                nb++;
            }
        }
        return nb;
    }
    public List<CarteTransport> listeCartesDisposPort(){
        List<CarteTransport> ListeDispos = new ArrayList<CarteTransport>();
        int[][] carteCoul = getCarteParcouleur();
        int nbJokerPosés = 0;
        Couleur Coul = Couleur.GRIS;
        int nbBateauPosés =0;
        int nbWagonPosés = 0;
        for (CarteTransport c: cartesTransportPosees){
            if (c.getType().equals(TypeCarteTransport.JOKER)){
                nbJokerPosés++;
            }else {
                Coul = c.getCouleur();
                if (c.getType().equals(TypeCarteTransport.WAGON)){
                    nbWagonPosés++;
                }else {
                    nbBateauPosés++;
                }
            }
        }
        int nbJokerDispo = getnbJoker()+nbJokerPosés;
        if (Coul.equals(Couleur.GRIS)){
            for (Couleur c: getCouleurs()){
                if (carteCoul[1][getIndice(c)]+nbJokerDispo >= 2 ){
                    if (carteCoul[1][getIndice(c)] == 1){
                        nbJokerDispo--;
                    } else if (carteCoul[1][getIndice(c)] == 0){
                        nbJokerDispo -= 2;
                    }
                    if (carteCoul[3][getIndice(c)]+nbJokerDispo >= 2){
                        ListeDispos.addAll(ListeBateauAncreParCouleur(c));
                        ListeDispos.addAll(ListeWagonParCouleur(c));
                    }
                }
            }
        }else{
            if (nbBateauPosés <2){
                ListeDispos.addAll(ListeBateauAncreParCouleur(Coul));
            }if (nbWagonPosés < 2) {
                ListeDispos.addAll(ListeWagonParCouleur(Coul));
            }
        }
        for (CarteTransport c : cartesTransport){
            if (c.getType().equals(TypeCarteTransport.JOKER)){
                ListeDispos.add(c);
            }
        }
        return ListeDispos;
    }
    public List<CarteTransport> ListeWagonParCouleur(Couleur coul){
        List<CarteTransport> ListWagon = new ArrayList<CarteTransport>();
        for (CarteTransport c : cartesTransport){
            if (c.getType().equals(TypeCarteTransport.WAGON)) {
                if (c.getAncre()) {
                    if (c.getCouleur().equals(coul)) {
                        ListWagon.add(c);
                    }
                }
            }
        }
        return ListWagon;
    }
    public List<CarteTransport> ListeBateauAncreParCouleur(Couleur coul){
        List<CarteTransport> ListBateau = new ArrayList<CarteTransport>();
        for (CarteTransport c : cartesTransport){
            if (c.getType().equals(TypeCarteTransport.BATEAU)) {
                if (c.getAncre()) {
                    if (c.getCouleur().equals(coul)) {
                        ListBateau.add(c);
                    }
                }
            }
        }
        return ListBateau;
    }
    public void  capturerPorts(Ville v){
        List<CarteTransport> listeCartesDispos = listeCartesDisposPort();
        List<String> listeNomCartesDispos=new ArrayList<>();
        String choix;
        Couleur coul = null;
        while(cartesTransportPosees.size() < 4) {
            for (CarteTransport c : listeCartesDispos) {
                    listeNomCartesDispos.add(c.getNom());
            }
                choix = choisir("quelle carte utiliser?", listeNomCartesDispos, null, false);
            for (CarteTransport c : listeCartesDispos) {
                if (c.getNom().equals(choix)) {
                        cartesTransportPosees.add(c);
                        cartesTransport.remove(c);
                }
            }

        }
        for (CarteTransport c : cartesTransportPosees) {
            jeu.defausser(c);
        }
        cartesTransportPosees.clear();
    }
    public void capturerRoute(Route r){
        routes.add(r);
        jeu.retirerRoute(r);
        score+=r.getScore();
        List<CarteTransport> listeCartesDispos=new ArrayList<>();
        List<String> listeNomCartesDispos;
        int longueur=r.getLongueur();
        if(r.estTerrestrePaire()){
            longueur*=2;
        }
        String choix;
        boolean fini;
        while(longueur>0){
            fini=false;
            if(r.estMaritime() && !r.getCouleur().equals(Couleur.GRIS)){
                listeCartesDispos=listeCartesDisposMaritime(longueur,r.getCouleur());
            }
            else if(r.estTerrestre() && !r.getCouleur().equals(Couleur.GRIS)){
                listeCartesDispos= listeCartesDisposTerrestre(r.getCouleur());
            }
            else if(!r.estTerrestrePaire() && r.getCouleur().equals(Couleur.GRIS)){
                listeCartesDispos= listeCartesDisposGrise(r,longueur);
            }
            else if(r.estTerrestrePaire()){

                listeCartesDispos=listeCartesDisposPaire(r,r.getLongueur());
            }
            listeNomCartesDispos=new ArrayList<>();
            for(CarteTransport c:listeCartesDispos){
                listeNomCartesDispos.add(c.getNom());
            }

            choix=choisir("quelle carte utiliser?",listeNomCartesDispos,null,false);
            for(CarteTransport c:listeCartesDispos){
                if(!fini && c.getNom().equals(choix)){
                    longueur-=c.getTaille();
                    cartesTransportPosees.add(c);
                    cartesTransport.remove(c);
                    fini=true;
                }
            }
        }
        for(CarteTransport c:cartesTransportPosees){
            jeu.defausser(c);
        }
        cartesTransportPosees.clear();
    }

    public List<CarteTransport> listeCartesDisposTerrestre(Couleur coul){
        List<CarteTransport> list=new ArrayList<>();
        for(CarteTransport c:cartesTransport){
            if(c.getType().equals(TypeCarteTransport.WAGON) && c.getCouleur().equals(coul)){
                list.add(c);
            }
            else if(c.getType().equals(TypeCarteTransport.JOKER)){
                list.add(c);
            }
        }
        return list;
    }
    public List<CarteTransport> listeCartesDisposMaritime(int longueur,Couleur coul){
        List<CarteTransport> list=new ArrayList<>();
        int[][] cartesParCouleur=getCarteParcouleur();
        for(CarteTransport c:cartesTransport) {
            if(c.getType().equals(TypeCarteTransport.JOKER)){
                if(longueur%2==1 || getnbJoker()>1 || cartesParCouleur[2][getIndice(coul)]!=0){
                    list.add(c);
                }
            }
            else if(c.getType().equals(TypeCarteTransport.BATEAU) && c.getCouleur().equals(coul)){
                if(c.estDouble()){
                    if(longueur%2==0){
                        list.add(c);
                    }
                    else {
                        if (longueur != 1 || nbcarteSimplePosée() % 2 == 0 || cartesParCouleur[3][getIndice(coul)] == 0) {
                            list.add(c);
                        }
                    }
                }
                else{
                    if(longueur%2==1){
                        list.add(c);
                    }
                    else{
                        if(getnbJoker()>0) {
                            list.add(c);
                        }
                        else if(cartesParCouleur[3][getIndice(c.getCouleur())]>=2){
                            list.add(c);
                        }

                    }
                }
            }
        }
        return list;
    }
    public List<CarteTransport> listeCartesDisposGrise(Route r,int longueur){
        for(CarteTransport c:cartesTransportPosees){
            if(!c.getType().equals(TypeCarteTransport.JOKER)){
                if(r.estTerrestre()){
                    return listeCartesDisposTerrestre(c.getCouleur());
                }
                else if(r.estMaritime()){
                    return listeCartesDisposMaritime(longueur,c.getCouleur());
                }
            }
        }
        List<CarteTransport> list=new ArrayList<>();
        int[][] carteParCouleur=getCarteParcouleur();
        for(Couleur coul:getCouleurs()){
            if(r.estMaritime()){
                if(carteParCouleur[2][getIndice(coul)]+getnbJoker()>=longueur){
                    list.addAll(listeCartesDisposMaritime(longueur,coul));
                }
            }
            else if(r.estTerrestre()){
                if(carteParCouleur[0][getIndice(coul)]+getnbJoker()>=longueur){
                    list.addAll(listeCartesDisposTerrestre(coul));
                }
            }
        }
        return list;
    }
    public List<CarteTransport> listeCartesDisposPaire(Route r,int longueur){
        List<CarteTransport> list=new ArrayList<>();
        List<Couleur> couleursPoséesSeules=new ArrayList<>();
        int[] WagonParCouleur=getCarteParcouleur()[0];
        int nbJokerPosé=0;
        int nbJokerDispo=getnbJoker();
        for(CarteTransport c:cartesTransportPosees){
            if(c.getType().equals(TypeCarteTransport.JOKER)){
                nbJokerPosé++;
            }
            else {
                if (!couleursPoséesSeules.contains(c.getCouleur())) {
                    couleursPoséesSeules.add(c.getCouleur());
                }
                else{
                    couleursPoséesSeules.remove(c.getCouleur());
                    longueur--;
                }
            }
        }
        for(Couleur coul:couleursPoséesSeules){
            list.addAll(listeCartesDisposTerrestre(coul));
            if(WagonParCouleur[getIndice(coul)]==0){
                nbJokerDispo--;
            }
        }
        if(couleursPoséesSeules.size()!=longueur){
            for (Couleur coul : getCouleurs()) {
                if(nbJokerPosé>0 || WagonParCouleur[getIndice(coul)]+nbJokerDispo>=2){
                    list.addAll(listeCartesDisposTerrestre(coul));
                }
            }
        }

        return list;
    }
    public int nbcarteSimplePosée(){
        int i=0;
        for(CarteTransport c:cartesTransportPosees){
            if(c.getType().equals(TypeCarteTransport.BATEAU) && !c.estDouble()){
                i++;
            }
        }
        return i;
    }

    public List<Couleur> getCouleurs(){
        return Arrays.asList(Couleur.ROUGE,Couleur.VIOLET,Couleur.JAUNE,Couleur.BLANC,Couleur.VERT,Couleur.NOIR);
    }

    public String choisir(
            String instruction,
            Collection<String> choix,
            Collection<Bouton> boutons,
            boolean peutPasser) {
        if (choix == null)
            choix = new ArrayList<>();
        if (boutons == null)
            boutons = new ArrayList<>();

        HashSet<String> choixDistincts = new HashSet<>(choix);
        choixDistincts.addAll(boutons.stream().map(Bouton::valeur).toList());
        if (peutPasser || choixDistincts.isEmpty()) {
            choixDistincts.add("");
        }

        String entree;
        // Lit l'entrée de l'utilisateur jusqu'à obtenir un choix valide
        while (true) {
            jeu.prompt(instruction, boutons, peutPasser);
            entree = jeu.lireLigne();
            // si une réponse valide est obtenue, elle est renvoyée
            if (choixDistincts.contains(entree)) {
                return entree;
            }
        }
    }

    /**
     * Affiche un message dans le log du jeu (visible sur l'interface graphique)
     *
     * @param message le message à afficher (peut contenir des balises html pour la
     *                mise en forme)
     */
    public void log(String message) {
        jeu.log(message);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("=== %s (%d pts) ===", nom, score));
        joiner.add(String.format("  Wagons: %d  Bateaux: %d", nbPionsWagon, nbPionsBateau));
        return joiner.toString();
    }

    /**
     * @return une chaîne de caractères contenant le nom du joueur, avec des balises
     * HTML pour être mis en forme dans le log
     */
    public String toLog() {
        return String.format("<span class=\"joueur\">%s</span>", nom);
    }

    boolean destinationEstComplete(Destination d) {
        return estJoignable(d);
    }

    private boolean estJoignable(Destination d) {
        boolean trouve=false;
        ArrayList<String> fils;
        ArrayList<String> villesVisitées=new ArrayList<>();
        villesVisitées.add(d.getVille(0));
        String v;
        int i=0;
        while(i<villesVisitées.size() && !trouve){
            v=villesVisitées.get(i);
            if(villesVisitées.containsAll(d.getVille())){
                trouve=true;
            }
            else {
                fils=getFils(v);
                for(String f:fils){
                    if(!villesVisitées.contains(f)){
                        villesVisitées.add(f);
                    }
                }

            }
            i++;
        }
        return trouve;
    }
    public ArrayList<String> getFils(String ville){
        ArrayList<String> list=new ArrayList<>();
        for(Route r:routes){
            if(r.getVille1().getNom().equals(ville)){
                list.add(r.getVille2().getNom());
            }
            else if (r.getVille2().getNom().equals(ville)){
                list.add(r.getVille1().getNom());
            }
        }
        return list;
    }


    public int calculerScoreFinal() {
        int bonus=0;
        int nbRouteConnectés = 0;
        for (Ville v: ports) {
            nbRouteConnectés = 0;
            for (Destination d : destinations) {
                if(destinationEstComplete(d)){
                    if (d.getVille().contains(v.getNom())) {
                        nbRouteConnectés++;
                    }
                }
            }
            if (nbRouteConnectés == 1) {
                score += 20;
            } else if (nbRouteConnectés == 2) {
                score += 30;
            } else if (nbRouteConnectés >= 3) {
                score += 50;
            }
        }
        bonus-= (3-ports.size())*4;
        for(Destination d:destinations){
            if(destinationEstComplete(d)){
                bonus+=d.getValeur();
            }
            else{
                bonus-=d.getMalus();
            }
        }
        return score+bonus;
    }

    /**
     * Renvoie une représentation du joueur sous la forme d'un dictionnaire de
     * valeurs sérialisables
     * (qui sera converti en JSON pour l'envoyer à l'interface graphique)
     */
    Map<String, Object> dataMap() {
        return Map.ofEntries(
                Map.entry("nom", nom),
                Map.entry("couleur", couleur),
                Map.entry("score", score),
                Map.entry("pionsWagon", nbPionsWagon),
                Map.entry("pionsWagonReserve", nbPionsWagonEnReserve),
                Map.entry("pionsBateau", nbPionsBateau),
                Map.entry("pionsBateauReserve", nbPionsBateauEnReserve),
                Map.entry("destinationsIncompletes",
                        destinations.stream().filter(d -> !destinationEstComplete(d)).toList()),
                Map.entry("destinationsCompletes", destinations.stream().filter(this::destinationEstComplete).toList()),
                Map.entry("main", cartesTransport.stream().sorted().toList()),
                Map.entry("inPlay", cartesTransportPosees.stream().sorted().toList()),
                Map.entry("ports", ports.stream().map(Ville::nom).toList()),
                Map.entry("routes", routes.stream().map(Route::getNom).toList()));
    }
}

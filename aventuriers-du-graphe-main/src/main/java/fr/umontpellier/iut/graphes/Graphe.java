package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;

import java.io.IOException;
import java.util.*;

/**
 * (Multi) Graphe non-orienté pondéré. Le poids de chaque arête correspond à la longueur de la route correspondante.
 * Pour une paire de sommets fixée {i,j}, il est possible d'avoir plusieurs arêtes
 * d'extrémités i et j et de longueur identique, du moment que leurs routes sont différentes.
 * Par exemple, il est possible d'avoir les deux arêtes suivantes dans le graphe :
 * Arete a1 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * et
 * Arete a2 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * Dans cet exemple (issus du jeu), a1 et a2 sont deux arêtes différentes, même si leurs routes sont très similaires
 * (seul l'attribut nom est différent).
 */
public class Graphe {

    /**
     * Liste d'incidences :
     * mapAretes.get(1) donne l'ensemble d'arêtes incidentes au sommet dont l'identifiant est 1
     * Si mapAretes.get(u) contient l'arête {u,v} alors, mapAretes.get(v) contient aussi cette arête
     */
    private Map<Integer, HashSet<Arete>> mapAretes;


    /**
     * Construit un graphe à n sommets 0..n-1 sans arêtes
     */
    public Graphe(int n) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Construit un graphe vide
     */
    public Graphe() {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Construit un graphe à partir d'une collection d'arêtes.
     *
     * @param aretes la collection d'arêtes
     */
    public Graphe(Collection<Arete> aretes) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * À partir d'un graphe donné, construit un sous-graphe induit
     * par un ensemble de sommets, sans modifier le graphe donné
     *
     * @param graphe le graphe à partir duquel on construit le sous-graphe
     * @param X      l'ensemble de sommets qui définissent le sous-graphe
     *               prérequis : X inclus dans l'ensemble des sommets du graphe
     */
    public Graphe(Graphe graphe, Set<Integer> X) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return l'ensemble de sommets du graphe
     */
    public Set<Integer> ensembleSommets() {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return l'ordre du graphe (le nombre de sommets)
     */
    public int nbSommets() {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return le nombre d'arêtes du graphe (ne pas oublier que this est un multigraphe : si plusieurs arêtes sont présentes entre un même coupe de sommets {i,j}, il faut
     * toutes les compter)
     */
    public int nbAretes() {
        throw new RuntimeException("Méthode non implémentée");
    }


    public boolean contientSommet(Integer v) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Ajoute un sommet au graphe s'il n'est pas déjà présent
     *
     * @param v le sommet à ajouter
     */
    public void ajouterSommet(Integer v) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Ajoute une arête au graphe si elle n'est pas déjà présente
     *
     * @param a l'arête à ajouter. Si les 2 sommets {i,j} de a ne sont pas dans l'ensemble,
     *          alors les sommets sont automatiquement ajoutés à l'ensemble de sommets du graphe
     */
    public void ajouterArete(Arete a) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Supprime une arête du graphe si elle est présente, sinon ne fait rien
     *
     * @param a arête à supprimer
     */
    public void supprimerArete(Arete a) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @param a l'arête dont on veut tester l'existence
     * @return true si a est présente dans le graphe
     */
    public boolean existeArete(Arete a) {
        throw new RuntimeException("Méthode non implémentée");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer v : mapAretes.keySet()) {
            sb.append("sommet").append(v).append(" : ").append(mapAretes.get(v)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Retourne l'ensemble des sommets voisins d'un sommet donné.
     * Si le sommet n'existe pas, l'ensemble retourné est vide.
     *
     * @param v l'identifiant du sommet dont on veut le voisinage
     */
    public Set<Integer> getVoisins(int v) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Supprime un sommet du graphe, ainsi que toutes les arêtes incidentes à ce sommet
     *
     * @param v le sommet à supprimer
     */
    public void supprimerSommet(int v) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return le degré d'un sommet donné
     * prérequis : le sommet doit exister dans le graphe
     */
    public int degre(int v) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return le degré max, et Integer.Min_VALUE si le graphe est vide
     */
    public int degreMax() {
        throw new RuntimeException("Méthode non implémentée");
    }

    public boolean estSimple() {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return ture ssi pour tous sommets i,j de this avec (i!=j), alors this contient une arête {i,j}
     */
    public boolean estComplet() {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return true ssi this est une chaîne. Attention, être une chaîne
     * implique en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs de la chaîne. On considère que le graphe vide est une chaîne.
     */
    public boolean estUneChaine() {
        throw new RuntimeException("Méthode non implémentée");
    }


    /**
     * @return true ssi this est un cycle. Attention, être un cycle implique
     * en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs du cycle.
     * On considère que dans le cas où G n'a que 2 sommets {i,j}, et 2 arêtes parallèles {i,j}, alors G n'est PAS un cycle.
     * On considère que le graphe vide est un cycle.
     */
    public boolean estUnCycle() {
        throw new RuntimeException("Méthode non implémentée");
    }


    public boolean estUneForet() {
        throw new RuntimeException("Méthode non implémentée");
    }

    public Set<Integer> getClasseConnexite(int v) {
        throw new RuntimeException("Méthode non implémentée");
    }

    public Set<Set<Integer>> getEnsembleClassesConnexite() {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return true si et seulement si l'arête passée en paramètre est un isthme dans le graphe.
     */
    public boolean estUnIsthme(Arete a) {
        throw new RuntimeException("Méthode non implémentée");
    }

    public boolean sontAdjacents(int i, int j) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Fusionne les deux sommets passés en paramètre.
     * Toutes les arêtes reliant i à j doivent être supprimées (pas de création de boucle).
     * L'entier correspondant au sommet nouvellement créé sera le min{i,j}. Le voisinage du nouveau sommet
     * est l'union des voisinages des deux sommets fusionnés.
     * Si un des sommets n'est pas présent dans le graphe, alors cette fonction ne fait rien.
     */
    public void fusionnerSommets(int i, int j) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe simple valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sequenceEstGraphe(List<Integer> sequence) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return true si les deux graphes passés en paramètre sont isomorphes.
     * pré-requis : les deux graphes sont des graphes simples.
     */
    public static boolean sontIsomorphes(Graphe g1, Graphe g2) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Retourne un plus court chemin entre 2 sommets.
     *
     * @param depart  le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     * @return une liste d'entiers correspondant aux sommets du chemin dans l'ordre : l'élément en position 0 de la liste
     * est le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, boolean pondere) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Retourne un chemin entre 2 sommets sans répétition de sommets et sans dépasser
     * le nombre de bateaux et wagons disponibles. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @param depart    le sommet de départ
     * @param arrivee   le sommet d'arrivée
     * @param nbBateaux le nombre de bateaux disponibles
     * @param nbWagons  le nombre de wagons disponibles
     * @return une liste d'entiers correspondant aux sommets du chemin, où l'élément en position 0 de la liste
     * et le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     * Pré-requis le graphe `this` est un graphe avec des routes (les objets routes ne sont pas null).
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, int nbWagons, int nbBateaux) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Retourne un chemin passant une et une seule fois par tous les sommets d'une liste donnée.
     * Les éléments de la liste en paramètres doivent apparaître dans le même ordre dans la liste de sortie.
     *
     * @param listeSommets la liste de sommets à visiter sans répétition ;
     *                     pré-requis : c'est une sous-liste de la liste retournée
     * @return une liste d'entiers correspondant aux sommets du chemin.
     * Si le chemin n'existe pas, retourne une liste vide.
     */
    public List<Integer> parcoursSansRepetition(List<Integer> listeSommets) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Retourne un plus petit ensemble bloquant de routes entre deux villes. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @return un ensemble de route.
     * Remarque : l'ensemble retourné doit être le plus petit en nombre de routes (et PAS en somme de leurs longueurs).
     * Remarque : il se peut qu'il y ait plusieurs ensemble de cardinalité minimum.
     * Un seul est à retourner (au choix).
     */
    public Set<Route> ensembleBloquant(int ville1, int ville2) {
        throw new RuntimeException("Méthode non implémentée");
    }
}
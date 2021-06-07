package com.company;

/*
	Inventaire
	auteur : Alexandre Forget
	contact : alexandreqc26@gmail.com
	07/06/2021
	Système d'inventaire en ligne de commande. Programmation procédural avec ajout de fonctions. Deuxième travaille d'école.
 */

import java.text.DecimalFormat;
import java.util.*;
import java.io.File;
import iofichiertab.*;

public class Main {

    static Scanner clavier = new Scanner(System.in);            //Pour permettre à l'utilisateur d'utiliser le clavier
    static int ctr = 0;										    //compteur pour les indices des tableaux des vêtements
    static int dimInventaire = 25;								//Pour déterminer la grandeur de l'ensemble des tableaux des vêtements

    static double[] prixRevient = new double[dimInventaire]; 	//Tableau contenant les prix de revient. soit prix d'achat ou de fabrication
    static double[] prixVente = new double[dimInventaire]; 		//Tableau contenant les prix de vente
    static boolean[] statut = new boolean[dimInventaire]; 		//Tableau contenant les statut des vêtements 0=actif  1=inactif
    static String[] genre = new String[dimInventaire];			//Tableau contenant les genres des vêtements. Homme, femme ou unisex
    static String[] description = new String[dimInventaire]; 	//Tableau contenant les descriptions des vêtements
    static int[] quantite = new int[dimInventaire];		  		//Tableau contenant les quantité des vêtements en inventaire
    static int[] numInv = new int[dimInventaire];				//Tableau contenant les numéros d'inventaire d'un vêtement
    static int[] qteMin = new int[dimInventaire]; 				//Tableau contenant les quantité minimum à maintenir en inventaire pour un vêtement
    static int[] codeFournisseur = new int[dimInventaire];		//Tableau contenant les numéros de fournisseurs dans les tableaux des vêtements

    static int ctrFourn = 0;                                    //compteur pour les indices des tableaux des fournisseurs
    static int dimFourn = 10;                                   //Pour déterminer la grandeur de l'ensemble des tableaux des fournisseurs

    static String[] nomFourn = new String[dimFourn];            //Tableau pour le nom des fournisseurs
    static String[] telephoneFourn = new String[dimFourn];      //Tableau pour les numéros de téléphones des fournisseurs
    static int[] numeroFourn = new int[dimFourn];               //Tableau pour les numéros de fournisseurs des les tableau des vêtements
    static final String erreurSelection = "Sélection " +        //Constante pour le message d'erreur quand l'utilisateur fait un choix invalide
            "invalide, choisissez de nouveau : ";                   // dans les menus


    public static void main(String[] args) {
        boolean finProg = false;								//Booléen pour mettre fin au programme
        String fichierFourn = "dataFourn.txt";                  //nom du fichier de donnée des fournisseurs
        String fichier = "dataTableaux.txt";                    //nom du fichier de donnée des tableaux de vêtements
        String input = "";                                  //Pour valider le choix de l'utilisateur dans le menu principal
        final String SEPARATEUR = ";";                          //Séparateur des champs de le fichier

        // Lecture des tableaux des données d'inventaire et des fournisseurs enregistrés
        if (new File(fichier).exists()){
            ctr = IOFichierTab.lireFichier(fichier, SEPARATEUR, numInv, quantite, qteMin, codeFournisseur, prixRevient, prixVente, statut, genre,
                    description);
        }
        if (new File(fichierFourn).exists()){
            ctrFourn = IOFichierTab.lireFichier(fichierFourn, SEPARATEUR, numeroFourn, nomFourn, telephoneFourn);
        }


        System.out.println("\n\n\n\n\n\n===============================================================================");
        System.out.println("*********************** SYSTÈME DE GESTION D'INVENTAIRE ***********************");
        System.out.println("===============================================================================");

        // La method main consiste en l'affichage du menu principal et la sélections dans ce menu
        while (!finProg) {
            System.out.println("\nChoisissez l'opération désirée : " +
                    "\n1. Ajouter un vêtement " +
                    "\n2. Modifier les attributs d'un vêtements " +
                    "\n3. Supprimer / rendre inactif un vêtement " +
                    "\n4. Afficher les listes d'inventaire " +
                    "\n5. Valeur de l'inventaire " +
                    "\n6. Fournisseurs " +
                    "\n0. Fermer le programme ");
            input = clavier.nextLine();
            switch (validerSaisieVide(input)) {
                case "1":
                    ajouterItem();
                    break;
                case "2":
                    modifierItem();
                    break;
                case "3":
                    supprimerItem();
                    break;
                case "4":
                    menuListeInvenaire();
                    break;
                case "5":
                    menuValeurInventaire();
                    break;
                case "6":
                    menuFournisseurs();
                    break;
                case "0":
                    finProg = true;
                    break;
                default:
                    System.out.println(erreurSelection);
            }
        }
        // Enregistrement des tableaux des données d'inventaire et de fournisseurs enregistrés
        IOFichierTab.ecrireFichier(fichier, SEPARATEUR, ctr, numInv, quantite, qteMin, codeFournisseur, prixRevient, prixVente, statut, genre,
                description);

        IOFichierTab.ecrireFichier(fichierFourn, SEPARATEUR, ctrFourn, numeroFourn, nomFourn, telephoneFourn);

    } //fin methode main




    //----------------------------------------------------------------
    // Méthode en lien avec ajouter des items  --01--
    //----------------------------------------------------------------


    // Procédure pour l'ajout d'un nouveau vêtement à l'inventaire
    static public void ajouterItem() {
        boolean retourMenuPrin = false;
        boolean validationNumInv = false;
        boolean validationFournisseur = false;
        boolean validationSaisi = false;
        String input = "";
        int rechercheNumInv;
        int choix = 0;


        // Boucle "while" permettant à l'utilisateur de valider la saisie de donnée
        while (!retourMenuPrin) {
            // Boucle "do" pour vérifier la disponibilité du numéro d'inventaire
            do {
                System.out.println("Entrer le numéro d'inventaire : ");
                input = clavier.nextLine();
                rechercheNumInv = validerInt(input);

                if (ValiderSiItemExiste(rechercheNumInv)) {
                    System.out.println("Le numéro d'inventaire est déjà utilisé. \n");
                } else {
                    numInv[ctr] = rechercheNumInv;
                    validationNumInv = true;
                }
                //Fin boucle "do" pour vérifier la disponibilité du numéro d'inventaire
            } while (!validationNumInv);

            //Remet le boolean "validationNumInv" à false pour pouvoir entrer de nouveau dans la boucle si le numéro est déjà utilisé
            validationNumInv = false;

            //<editor-fold desc=" Section ou sont saisie les caractéristique du vêtement avec l'appel des fonctions pour valider les saisies ">
            System.out.println("Entrer la quantité en inventaire : ");
            input = clavier.nextLine();
            quantite[ctr] = validerInt(input);

            System.out.println("Entrer la quantité minimum à maintenir en inventaire : ");
            input = clavier.nextLine();
            qteMin[ctr] = validerInt(input);

            listerFournisseurs();
            validationFournisseur = false;
            while (!validationFournisseur) {
                System.out.println("\n1. Choisir un fournisseur déjà existant \n2. Créer un nouveau fournisseur");
                input = clavier.nextLine();
                input = validerSaisieVide(input);
                choix = validerInt(input);

                // Switch donnant le chois de créer un nouveau fournisseur pendant l'ajout d'un nouveau vêtement
                switch (choix) {
                    case 1:
                        System.out.println("Choisissez le numéro du fournisseur : ");
                        codeFournisseur[ctr] = numeroFourn[localiserFourn()];
                        validationFournisseur = true;
                        break;
                    case 2:
                        ajouterFournisseur();
                        codeFournisseur[ctr] = numeroFourn[ctrFourn - 1];
                        validationFournisseur = true;
                        break;
                    default:
                        System.out.println(erreurSelection);
                        break;
                }
            }
            System.out.println("Entrer le prix de revient : ");
            input = clavier.nextLine();
            prixRevient[ctr] = validerPrix(input);

            System.out.println("Entrer le prix de vente : ");
            input = clavier.nextLine();
            prixVente[ctr] = validerPrix(input);

            genre[ctr] = choisirGenre();

            System.out.println("Entrer la description du vêtement : ");
            description[ctr] = validerDescription();

            statut[ctr] = true;

            // Affiche les caractéristiques saisies et demande à l'utilisateur de valider.
            affichageEntete();
            affichageTab(ctr);
            System.out.println("\nEst-ce que toutes les informations sont Exact? \n1. Oui, ajouter le vêtement \n2. Non, entrer" +
                    " de nouveaux les informations \n0. Non, retourner au menu principal ");

            while (!validationSaisi) {
                // Switch permettant de valider la saisie et de recommencer ou retourner au menu principal
                input = clavier.nextLine();
                switch (validerSaisieVide(input)) {
                    case "1":
                        retourMenuPrin = true;
                        ctr++;
                        validationSaisi = true;
                        break;
                    case "2":
                        ctrFourn--;
                        validationSaisi = true;
                        break;
                    case "0":
                        retourMenuPrin = true;
                        ctrFourn--;
                        validationSaisi = true;
                        break;
                    default:
                        System.out.println(erreurSelection);
                }
            }
            //</editor-fold>
        } // fin du while pour permettre à l'utilisateur de valider les données saisies
    }


    // Fonction pour demander le genre du vêtement et non une entrée de texte pour éliminer le risque d'erreur.
    // Retourne le choix à la procédure ajouterItem
    static public String choisirGenre(){
        boolean validation = false;
        String genre = "";
        String input = "";

        System.out.println("Choisissez le genre du vêtement : \n1. homme \n2. femme \n3. Unisexe ");
        while (!validation) {
            input = clavier.nextLine();
            switch (validerSaisieVide(input)) {
                case "1":
                    genre = "homme  ";
                    validation = true;
                    break;
                case "2":
                    genre = "femme  ";
                    validation = true;
                    break;
                case "3":
                    genre = "unisexe";
                    validation = true;
                    break;
                default:
                    System.out.println(erreurSelection);
            }
        }
        return genre;
    }


    // Fonction pour valider que le premier caractère de la description est une lettre
    static public String validerDescription(){
        boolean validation = false;
        String chaine = clavier.nextLine();

        while (!validation) {
            if (!Character.isLetter(chaine.charAt(0))) {
                System.out.println("Le premier caractère doit obligatoirement être une lettre");
                chaine = clavier.nextLine();
            } else {
                validation = true;
            }
        }
        return chaine;
    }




    //----------------------------------------------------------------
    // Méthode en lien avec modifier des items  --02--
    //----------------------------------------------------------------


    // Procédure pour la modification d'un vêtement
    static public void modifierItem(){
        int rechercheNumInv;
        int idx;
        String input = "";
        boolean selectionMenu = false;
        boolean retourMenuPrinc = false;

        // Boucle "do" pour donner le choix ou non de retourner au menu principal à la fin de la modification
        do{
            listerTout();
            System.out.println("\nEntrer le numéro d'inventaire du vêtement que vous voulez supprimer / rendre inactif :");
            input = clavier.nextLine();
            rechercheNumInv = validerInt(input);
            idx = localiserItem(rechercheNumInv);

            // Si le numéro d'inventaire existe, affiche les détails de l'item à l'utilisateur et lui demande de confirmer que c'est le bon item
            if (rechercheNumInv == numInv[idx]) {
                affichageEntete();
                affichageTab(idx);
                System.out.println("\nEst-ce le bon vêtement à modifier? \n1. Oui\n2. Non, entrer un nouveau numéro d'inventaire\n0. Non, retourner" +
                        " au menu principal");
                // Boucle "do" pour offrir à l'utilisateur de continuer avec cet item, en choisir un autre ou retourner au menu principal
                do {
                    retourMenuPrinc = true;
                    selectionMenu = true;
                    // Switch pour demander à l'utilisateur quelle caractéristique du vêtement il veut modifier
                    input = clavier.nextLine();
                    switch (validerSaisieVide(input)) {
                        case "1":
                            System.out.println("\nQue voulez-vous modifier : \n1. Quantité en inventaire \n2. Prix de revient \n3. Prix de vente \n4." +
                                    " Le statut\n5. Le fournisseurs");
                            do {
                                selectionMenu = true;
                                input = clavier.nextLine();
                                switch (validerSaisieVide(input)) {
                                    case "1":
                                        modifierQuantiteIventaire(idx);
                                        break;
                                    case "2":
                                        modifierPrixRevient(idx);
                                        break;
                                    case "3":
                                        modifierPrixVente(idx);
                                        break;
                                    case "4":
                                        activerItem(idx);
                                        break;
                                    case "5":
                                        modifierFournisseur(idx);
                                        break;
                                    default:
                                        System.out.println(erreurSelection);
                                        selectionMenu = false;
                                        break;
                                }
                            }while (!selectionMenu);
                            break;
                        case "2":
                            retourMenuPrinc = false;
                            break;
                        case "0":
                            break;
                        default:
                            System.out.println(erreurSelection);
                            selectionMenu = false;
                            retourMenuPrinc = false;
                            break;
                    }
                }while (!selectionMenu);  // Fin de la boucle "do" qui demande à l'utilisateur si il veut continuer avec cet item, en choisir un
                // nouveau ou retourner au menu principal
            }
        }while (!retourMenuPrinc); // Fin de la boucle "do" pour donner le choix ou non de retourner au menu principal à la fin de la modification

    }


    // Procédure pour modifier la quantité en inventaire
    static public void modifierQuantiteIventaire(int idx){
        String input = "";

        System.out.println("La quantité en inventaire actuelle est " + quantite[idx] + ", entrer la nouvelle quantité " +
                "en inventaire : ");
        input = clavier.nextLine();
        quantite[idx] = validerInt(input);
        System.out.println("\nVoici le vêtement numéro " + numInv[idx] + " avec la modification apporté :");
        affichageEntete();
        affichageTab(idx);
        modificationSupplementaire();
    }


    // Procédure pour modifier le prix de revient
    static public void modifierPrixRevient(int idx){
        String input = "";
        DecimalFormat prix = new DecimalFormat("0.00");

        System.out.println("Le prix de revient actuel est " + prix.format(prixRevient[idx]) + ". Entrer le nouveau prix de revient : ");
        input = clavier.nextLine();
        prixRevient[idx] = validerPrix(input);
        System.out.println("\nVoici le vêtement numéro " + numInv[idx] + " avec la modification apporté :");
        affichageEntete();
        affichageTab(idx);
        modificationSupplementaire();
    }


    // Procédure pour modifier le prix de vente
    static public void modifierPrixVente(int idx){
        String input = "";
        DecimalFormat prix = new DecimalFormat("0.00");

        System.out.println("Le prix de vente actuel est " + prix.format(prixVente[idx]) + ", entrer le nouveau prix de vente : ");
        input = clavier.nextLine();
        prixVente[idx] = validerPrix(input);
        System.out.println("\nVoici le vêtement numéro " + numInv[idx] + " avec la modification apporté :");
        affichageEntete();
        affichageTab(idx);
        modificationSupplementaire();
    }


    // Procédure pour activer un item et qui vérifie si l'item est déjà actif.
    // Dans un tel cas indique à l'utilisateur comment désactiver un item
    static public void activerItem(int idx){
        boolean selectionMenu;
        String input = "";

        if (statut[idx]) {
            System.out.println("Ce vêtement est déjà actif. Pour le rendre inactif sélectionner l'option 3. Supprimer " +
                    "/ rendre inactif un vêtement dans le menu principal");
        } else {
            do {
                selectionMenu = true;
                System.out.println("\nCe vêtement est actuellement inactif, voulez-vous l'activer? \n1. Oui \n2. Non");
                input = clavier.nextLine();
                switch (validerSaisieVide(input)) {
                    case "1":
                        statut[idx] = true;
                        break;
                    case "2":
                        System.out.println("La statut reste inactif.");
                        break;
                    default:
                        System.out.println(erreurSelection);
                        selectionMenu = false;
                        break;
                }
            } while (!selectionMenu);
        }
        modificationSupplementaire();
    }


    // Procédure pour modifier le fournisseur d'un vêtement
    static public void modifierFournisseur(int idx){
        int nouvNumeroFourn = 0;

        listerFournisseurs();
        System.out.println("\nLe fournisseur actuel de ce vêtement est : " + chercherFourn(codeFournisseur[idx]) + "\nSaisissez le numéro de " +
                "fournisseur qui le remplacera");
        nouvNumeroFourn = remplacerFourniseur();
        codeFournisseur[idx] = nouvNumeroFourn;
        modificationSupplementaire();
    }


    // Procédure pour demander à l'utilisateur si il veut faire une modification supplémentaire
    // à la suite d'une modification ou retourner au menu principal
    static public void modificationSupplementaire(){
        boolean selectionMenu;
        String input = "";

        System.out.println("\nVoulez-vous apporter une modification supplémentaire ?\n1. Oui\n0. Non, retourner au menu principal");
        do {
            selectionMenu = true;
            input = clavier.nextLine();
            switch (validerSaisieVide(input)) {
                case "1":
                    modifierItem();
                    break;
                case "0":
                    break;
                default:
                    System.out.println(erreurSelection);
                    selectionMenu = false;
                    break;
            }
        }while (!selectionMenu);
    }


    // Fonction pour mettre à jour le numéro de fournisseur dans les tableaux des vêtements
    static public int remplacerFourniseur(){
        boolean validaton = false;
        String numeroFourn;
        int numeroFournisseur = 0;
        int i = 0;
        int indice = 0;


        while (!validaton) {
            numeroFourn = clavier.nextLine();
            numeroFournisseur = validerInt(numeroFourn);

            for (i = 0; i < ctrFourn; i++) {
                if (numeroFournisseur == Main.numeroFourn[i]){
                    validaton = true;
                    indice = Main.numeroFourn[i];
                }
            }
            if (!validaton){
                System.out.println(erreurSelection);
            }
        }
        return indice;
    }




    //----------------------------------------------------------------
    // Méthode en lien avec supprimer des items  --03--
    //----------------------------------------------------------------


    // Procédure pour supprimer un vêtement. L'utilisateur choisi un numéro d'inventaire
    // et le programme valide si il existe et confirme avec
    // l'utilisateur que c'est le bon vêtement à modifier
    static public void supprimerItem(){
        int indiceVetement;
        int rechercheNumInv;
        String input;
        boolean selectionMenu = false;

        while (!selectionMenu) {
            listerTout();
            System.out.println("\nEntrer le numéro d'inventaire du vêtement que vous voulez supprimer / rendre inactif :");
            input = clavier.nextLine();
            rechercheNumInv = validerInt(input);
            indiceVetement = localiserItem(rechercheNumInv);

            // Si le # d'inventaire est bon présente le vêtement sélectionné et demande une confirmation du vêtement à l'utilisateur
            if (rechercheNumInv == numInv[indiceVetement]) {
                affichageEntete();
                affichageTab(indiceVetement);
                System.out.println("\nEst-ce le bon vêtement à supprimer / désactiver? \n1. Oui\n2. Non, choisir un nouveau numéro d'inventaire \n0" +
                        ". Non, retourner au menu principal");
                selectionMenu = true;
                selectionMenu = validerVetementSupprimer(indiceVetement, selectionMenu);
            }
        }
    }


    // Fonction qui demande a l'utilisateur ce qu'il veut faire avec le vêtement choisie. Supprimer, désactiver, annuler l'opération
    static public boolean validerVetementSupprimer(int indiceVetement, boolean validation){
        boolean selectionMenu = false;
        String input = "";

        do {
            selectionMenu = true;
            input = clavier.nextLine();
            switch (validerSaisieVide(input)) {
                case "1":
                    choisirSupprimerDesactiver(indiceVetement);
                    break;
                case "2":
                    validation = false;
                    break;
                case "0":
                    break;
                default:
                    System.out.println(erreurSelection);
                    selectionMenu = false;
                    break;
            }
        }while (!selectionMenu);
        return validation;
    }


    // Procédure qui demande à l'utilisateur si il veut définitivement supprimer l'item ou le désactiver
    static public void choisirSupprimerDesactiver(int ctrSup) {
        boolean selectionMenu = false;
        String input = "";

        do {
            System.out.println("\nQue voulez-vous faire : \n1. Supprimer le vêtements (Cette option le supprimera de l'inventaire " +
                    "de façons définitive)  \n2. Mettre le vêtement inactif \n0. Annuler l'opération et retourner au menu principal");
            selectionMenu = true;
            input = clavier.nextLine();
            switch (validerSaisieVide(input)) {
                case "1":
                    supprimerItem(ctrSup);
                    break;
                case "2":
                    desactiverItem(ctrSup);
                    break;
                case "0":
                    break;
                default:
                    System.out.println(erreurSelection);
                    selectionMenu = false;
                    break;
            }
        }while (!selectionMenu);
    }


    // Fonction qui recherche si le numéro d'inventaire existe déjà
    static public boolean ValiderSiItemExiste(int rechercheNumInv){
        int idx;
        boolean validation = false;

        for (idx = 0;idx < ctr ;idx++){
            if (rechercheNumInv == numInv[idx]){
                validation = true;
            }
        }
        return validation;
    }


    // Fonction qui recherche l'indice d'un numéro de vêtement pour le localiser dans les tableaux
    static public int localiserItem(int rechercheNumInv){
        int indiceVetement = 0;

        while ((rechercheNumInv != numInv[indiceVetement]) && (indiceVetement < ctr))
            indiceVetement++;
        return indiceVetement;
    }


    // Procédure qui efface un item des tableaux d'inventaire
    static public void supprimerItem(int ctrSup){
        System.out.println("Le vêtement numéro " + numInv[ctrSup] + " a définitivement été supprimé de " +
                "l'inventaire.");
        for (numInv[ctrSup] = ctrSup; ctrSup < ctr - 1; ctrSup++) {
            numInv[ctrSup] = numInv[ctrSup + 1];
            quantite[ctrSup] = quantite[ctrSup + 1];
            qteMin[ctrSup] = qteMin[ctrSup + 1];
            codeFournisseur[ctrSup] = codeFournisseur[ctrSup + 1];
            prixRevient[ctrSup] = prixRevient[ctrSup + 1];
            prixVente[ctrSup] = prixVente[ctrSup + 1];
            genre[ctrSup] = genre[ctrSup + 1];
            description[ctrSup] = description[ctrSup + 1];
            statut[ctrSup] = statut[ctrSup + 1];
        }
        ctr--;
    }


    // Procédure qui valide si l'item est déjà inactif et le désactive si il ne l'est pas
    static public void desactiverItem(int ctrSup){
        if (!statut[ctrSup]){
            System.out.println("Le vêtement numéro " + numInv[ctrSup] + " est déjà inactif. Si vous voulez l'activer choisissez l'option 2. " +
                    "Modifier un vêtement au menu principal.");
        }else {
            System.out.println("Le vêtement numéro " + numInv[ctrSup] + " est maintenant inactif.");
            statut[ctrSup] = false;
        }
    }




    //----------------------------------------------------------------
    // Method en lien avec les listes d'inventaire  --04--
    //----------------------------------------------------------------

    // Procédure qui affiche le sous menu des liste d'inventaire et valide le choix de liste à afficher
    static public void menuListeInvenaire(){
        boolean retourMenuPrin = false;
        int idx = 0;
        String input = "";

        while (!retourMenuPrin) {
            System.out.println("\nChoisir la liste d'inventaire à afficher : " +
                    "\n1. Liste des vêtements actifs " +
                    "\n2. Liste des vêtements inactifs " +
                    "\n3. Liste des vêtements pour hommes " +
                    "\n4. Liste des vêtements pour femmes " +
                    "\n5. Liste des vêtements unisexes " +
                    "\n6. Liste des vêtements à commander " +
                    "\n7. Liste des vêtements fabriqués " +
                    "\n8. Liste des vêtements provenant d'un fournisseurs " +
                    "\n9. Liste des vêtements par fournisseurs " +
                    "\n0. Retourner au menu principal ");
            input = clavier.nextLine();
            switch (validerSaisieVide(input)) {

                case "1":  // Vêtement actifs
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if (statut[idx])
                            affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "2": //  Vêtement inactif
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if (!statut[idx]) affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "3": //  Vêtement pour homme
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if ((genre[idx].equals("homme  ")) && (statut[idx])) affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "4":  // Vêtement pour femme
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if ((genre[idx].equals("femme  ")) && (statut[idx])) affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "5":  // Vêtement unisexe
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if ((genre[idx].equals("unisexe")) && (statut[idx])) affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "6":  // Vêtement à commander
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if ((quantite[idx] - qteMin[idx] <= 0) && (statut[idx])) affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "7":  // liste des vêtements fabriqués par l'entreprise
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if ((codeFournisseur[idx] == 0) && (statut[idx])) affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "8":  // liste des vêtements achetés chez un fournisseurs
                    affichageEntete();
                    for (idx = 0; idx < ctr; idx++)
                        if ((codeFournisseur[idx] > 0) && (statut[idx])) affichageTab(idx);
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;

                case "9":  // Liste d'inventaire par fournisseurs
                    System.out.println("\nChoisissez le fournisseurs dont vous voulez afficher la liste des vêtements ");
                    listerFournisseurs();
                    listerParFourn();
                    if (afficherMenuAutreListe().equals("0")) retourMenuPrin = true;
                    break;
                case "0":
                    retourMenuPrin = true;
                    break;
                default:
                    System.out.println(erreurSelection);
                    break;
            }
        }
    }


    // Procédure qui affiche l'entête des listes d'inventaire
    static public void affichageEntete(){
        trierListeInventaire();
        System.out.println(formatter("", 136, "-"));
        System.out.println(formatter("# Inv", 7, null) + " | "
                + formatter("Qte", 7, null) + " | "
                + formatter("Qte min", 7, null) + " | "
                + formatter("# Fourn", 7, null) + " | "
                + formatter("Fournisseur", 25, null) + " | "
                + formatter("Prix revient", 12, null) + " | "
                + formatter("Prix vente", 12, null) + " | "
                + formatter("Genre", 7, null) + " | "
                + formatter("Statut", 8, null) + " | "
                + formatter("Description", 15, null) + " | ");
        System.out.println(formatter("", 136, "-"));
    }


    // Procédure qui affiche une liste d'inventaire selon les conditions reçu
    static public void affichageTab(int idx){
        DecimalFormat prix = new DecimalFormat("#,###0.00");

        System.out.println(formatter(Integer.toString(numInv[idx]), 7, null) + " | "
                + formatter(Integer.toString(quantite[idx]), 7, null) + " | "
                + formatter(Integer.toString(qteMin[idx]), 7, null) + " | "
                + formatter(Integer.toString(codeFournisseur[idx]), 7, null) + " | "
                + formatter(chercherFourn(codeFournisseur[idx]), 25, null) + " | "
                + formatter((prix.format(prixRevient[idx])),11, null) + "$ | "
                + formatter((prix.format(prixVente[idx])),11, null) + "$ | "
                + formatter(genre[idx], 7, null) + " | "
                + formatter(statut[idx] ? "Actif" : "Inactif", 8, null) + " | "
                + formatter(description[idx], 15, null) + " | ");
    }


    // Procédure qui tri la liste des items dans l'ordre du numéro d'inventaire
    public static void trierListeInventaire(){
        int i;
        int j;
        int[] boolTempo = new int[ctr];   // tableau temporaire pour convertir un booleen en int pour le trier avec les autres tableaux
        int tmpInt;
        String tmpString;
        double tmpDouble;

        for (i = 0; i < ctr;i++){
            if (statut[i])
                boolTempo[i] = 1;
            else
                boolTempo[i] = 0;
        }

        // Tri à bulle pour l'ensemble des tableaux d'inventaire
        for (i = 0; i < (ctr - 1); i++) {
            for (j = i + 1; j < ctr; j++) {
                if (numInv[i] > numInv[j]) {
                    tmpInt = numInv[i];
                    numInv[i] = numInv[j];
                    numInv[j] = tmpInt;

                    tmpInt = quantite[i];
                    quantite[i] = quantite[j];
                    quantite[j] = tmpInt;

                    tmpInt = qteMin[i];
                    qteMin[i] = qteMin[j];
                    qteMin[j] = tmpInt;

                    tmpInt = codeFournisseur[i];
                    codeFournisseur[i] = codeFournisseur[j];
                    codeFournisseur[j] = tmpInt;

                    tmpDouble = prixRevient[i];
                    prixRevient[i] = prixRevient[j];
                    prixRevient[j] = tmpDouble;

                    tmpDouble = prixVente[i];
                    prixVente[i] = prixVente[j];
                    prixVente[j] = tmpDouble;

                    tmpString = genre[i];
                    genre[i] = genre[j];
                    genre[j] = tmpString;

                    tmpInt = boolTempo[i];
                    boolTempo[i] = boolTempo[j];
                    boolTempo[j] = tmpInt;

                    tmpString = description[i];
                    description[i] = description[j];
                    description[j] = tmpString;
                }
            }
        }
        for (i = 0; i < ctr;i++){
            if (boolTempo[i] == 1)
                statut[i] = true;
            else
                statut[i] = false;
        }
    }


    //Fonction qui contrôle la longueur d'un String pour l'affichage des tableaux
    public static String formatter(String chaine, int longueur, String pad) {
        String chaineFixe = "";
        boolean fin = false;
        int i = 0;

        if (pad == null) {
            pad = " ";
        }

        while (i < longueur) {
            if (i == chaine.length()) {
                fin = true;
            }
            if (!fin) {
                chaineFixe = chaineFixe + chaine.charAt(i);
            } else {
                chaineFixe = chaineFixe + pad;
            }
            i++;
        }
        return chaineFixe;
    }


    // Fonction pour donner le choix à l'utilisateur d'afficher une autre liste d'inventaire ou retourner au menu principal
    static public String afficherMenuAutreListe() {
        String input;
        boolean choixInvalide = false;

        System.out.println("\n1. Afficher une autre liste d'inventaire \n0. Retourner au menu principal");
        input = clavier.nextLine();
        while (!choixInvalide) {
            if (!input.equals("0") && !input.equals("1")) {
                System.out.println(erreurSelection);
                input = clavier.nextLine();
            }else{
                choixInvalide = true;
            }
        }
        return input;
    }


    // Procédure pour choisir la liste des vêtements de quel fournisseurs afficher
    static public void listerParFourn(){
        int choixFourn = 0;
        String input = "";

        System.out.println("\nChoisissez le numéro du fournisseur dont vous voulez afficher la liste des vêtements :");
        input = clavier.nextLine();
        choixFourn = validerInt(input);

        // boucle while pour valider que le numéro de fournisseur existe
        while (fourneExiste(choixFourn)){
            System.out.println("\nLe numéro de fournisseur saisie n'est pas valide.\nChoisissez un autre numéro de fournisseur : ");
            input = clavier.nextLine();
            choixFourn = validerInt(input);
        }
        affichageEntete();
        for (int idx = 0;idx < ctr; idx++){
            if (codeFournisseur[idx] == choixFourn){
                affichageTab(idx);
            }
        }

    }


    // Procédure pour afficher l'ensemble des vêtements, autant actif que inactif
    static public void listerTout() {
        affichageEntete();
        for (int idx = 0; idx < ctr; idx++) {
                affichageTab(idx);
        }
    }




    //----------------------------------------------------------------
    // Méthode en lien avec le menu de la valeur de inventaire  --05--
    //----------------------------------------------------------------

    // Procédure pour obtenir la valeur de l'inventaire selon le prix de revient ou de vente
    // selon le choix de l'utilisateur
    static public void menuValeurInventaire() {
        boolean retourMenuPrin = false;
        double valeurTotal;
        String input = "";
        DecimalFormat prix = new DecimalFormat("#,##0.00");

        while (!retourMenuPrin) {
            System.out.println("\nEn fonction de quoi voulez-vous consulter la valeur de l'inventaire : " +
                    "\n1. Le prix de revient " +
                    "\n2. Le prix de vente " +
                    "\n0. Retour au menu principal");

            input = clavier.nextLine();
            switch (validerSaisieVide(input)) {
                case "1":
                    valeurTotal = 0;
                    for (int idx = 0; idx < ctr; idx++) {
                        valeurTotal = valeurTotal + (prixRevient[idx] * quantite[idx]);
                    }
                    System.out.print("\nLa valeur de l'inventaire selon le prix de revient est de : " + prix.format(valeurTotal) + "$\n");
                    break;

                case "2":
                    valeurTotal = 0;
                    for (int idx = 0; idx < ctr; idx++) {
                        valeurTotal = valeurTotal + (prixVente[idx] * quantite[idx]);
                    }
                    System.out.print("\nLa valeur de l'inventaire selon le prix de vente est de : " + prix.format(valeurTotal) + "$\n");
                    break;

                case "0":
                    retourMenuPrin = true;
                    break;
                default:
                    System.out.println(erreurSelection);
            }
        }
    }




    //----------------------------------------------------------------
    // Méthode en lien avec le menu des fournisseurs  --06--
    //----------------------------------------------------------------

    // Procédure pour afficher le sous menu des fournisseurs
    static public void menuFournisseurs(){
        boolean selectionMenu = false;
        boolean retourMenuPrinc = false;
        int indiceFournisseur = 0;
        String input = "";

        while (!retourMenuPrinc) {
            System.out.println("\n1. Ajouter un fournisseur " +
                    "\n2. Modifier le numéro d'un fournisseurs " +
                    "\n3. Modifier le nom d'un fournisseurs " +
                    "\n4. Modifier le numéro de téléphone d'un fournisseur " +
                    "\n5. Lister les fournisseurs " +
                    "\n6. Supprimer un fournisseur " +
                    "\n0. retourner au menu principal");
            do {
                selectionMenu = false;
                input = clavier.nextLine();
                switch (validerSaisieVide(input)) {
                    case "1":
                        ajouterFournisseur();
                        break;
                    case "2":
                        modifierNumFourn();
                        break;
                    case "3":
                        modifierNomFourn();
                        break;
                    case "4":
                        modifierTelephoneFourn();
                        break;
                    case "5":
                        listerFournisseurs();
                        break;
                    case "6":
                        listerFournisseurs();
                        System.out.println("\nChoisissez le numéro du fournisseur que vous voulez supprimer.");
                        indiceFournisseur = localiserFourn();
                        supprimerFourn(indiceFournisseur);
                        break;
                    case "0":
                        retourMenuPrinc = true;
                        break;
                    default:
                        System.out.println(erreurSelection);
                        selectionMenu = true;
                        break;
                }
            } while (selectionMenu);
        }
    }


    // Procédure pour ajouter un nouveau fournisseur
    static public void ajouterFournisseur(){
        String input = "";
        int numFourn;

        System.out.println("Saisissez le numéro de fournisseur :");
        input = clavier.nextLine();
        numFourn = validerInt(input);
        // boucle while qui vérifie si le numéro de fournisseur est déjà attribué
        while (!fourneExiste(numFourn)){
            System.out.println("Ce numéro de fournisseurs est déjà utilisé.\nChoisissez un autre numéro : ");
            input = clavier.nextLine();
            numFourn = validerInt(input);
        }
        numeroFourn[ctrFourn] = numFourn;

        System.out.println("\nSaisissez le nom du fournisseur :");
        input = clavier.nextLine();
        input = input.toUpperCase();
        if (input.equalsIgnoreCase("Alain"))
            System.out.println("Je me doutais bien que tu rêvais de fabriquer des vêtements!  ;) ");
        nomFourn[ctrFourn] = input;

        System.out.println("\nSaisissez le numéro de téléphone du fournisseur, entrer seulement les dix numéros sans espaces ni caractères spéciaux");
        input = clavier.next();
        input = validerNumTelephone(input);
        telephoneFourn[ctrFourn] = input;

        ctrFourn++;
    }


    // Procédure pour trier les fournisseur en ordre de numéro de fournisseur
    static public void trierFourn(){
        int tmpInt;
        int i;
        String tmpString;

        // tri à bulle des tableaux fournisseurs
        for (i = 0; i < (ctrFourn - 1); i++) {
            for (int j = i + 1; j < ctrFourn; j++) {
                if (numeroFourn[i] > numeroFourn[j]) {
                    tmpInt = numeroFourn[i];
                    numeroFourn[i] = numeroFourn[j];
                    numeroFourn[j] = tmpInt;

                    tmpString = nomFourn[i];
                    nomFourn[i] = nomFourn[j];
                    nomFourn[j] = tmpString;

                    tmpString = telephoneFourn[i];
                    telephoneFourn[i] = telephoneFourn[j];
                    telephoneFourn[j] = tmpString;
                }
            }
        }
    }


    // Procédure pour modifier le numéro de fournisseur
    static public void modifierNumFourn(){
        int i;
        int indiceFournisseur = 0;
        int nouvNumeroFourn = 0;
        int ancientNumeroFourn = 0;
        String input = "";

        listerFournisseurs();
        System.out.println("\nChoisissez le numéro du fournisseur dont vous voulez modifier le numéro");
        input = clavier.nextLine();
        ancientNumeroFourn = validerInt(input);
        indiceFournisseur = localiserFourn();

        // boucle while qui empêche de modifier le numéro du fournisseur "0". Les vêtements fabriqué maison
        while (indiceFournisseur == 0){
            System.out.println("Le numéro du fournisseur " + nomFourn[0] + " ne peu pas être modifié. \nChoisissez un autre fournisseur :");
            input = clavier.nextLine();
            ancientNumeroFourn = validerInt(input);
            indiceFournisseur = localiserFourn();
        }
        System.out.println("\nSaisissez le nouveau numéro du fournisseur :");
        input = clavier.nextLine();
        nouvNumeroFourn = validerInt(input);

        // boucle while qui vérifie la disponibilité du numéro de fournisseur choisi
        while (!fourneExiste(nouvNumeroFourn)){
            System.out.println("Ce numéro de fournisseurs est déjà utilisé.\nChoisissez un autre numéro : ");
            input = clavier.nextLine();
            nouvNumeroFourn = validerInt(input);
        }
        numeroFourn[indiceFournisseur] = nouvNumeroFourn;

        // boucle for qui met à jour le nouveau numéro de fournisseur
        // dans les tableaux des vêtements
        for (i = 0;i < ctr;i++){
            if (codeFournisseur[i] == ancientNumeroFourn)
                codeFournisseur[i] = nouvNumeroFourn;
        }
    }


    // Procédure pour modifier le nom d'un fournisseur
    static public void modifierNomFourn(){
        int indice = 0;
        String input = "";

        listerFournisseurs();
        System.out.println("\nChoisissez le numéro du fournisseur dont vous voulez modifier le nom");
        indice = localiserFourn();
        System.out.println("\nSaisissez le nouveau nom du fournisseur :");
        input = clavier.nextLine();
        nomFourn[indice] = input.toUpperCase();
        System.out.println("\nLe nom du fournisseur # " + numeroFourn[indice] + " est maintenant " + nomFourn[indice]);
    }


    // Procédure pour modifier le numéro de téléphone d'un fournisseur
    static public void modifierTelephoneFourn(){
        int indice = 0;
        String input = "";

        listerFournisseurs();
        System.out.println("\nChoisissez le numéro du fournisseur dont vous voulez modifier le numéro de téléphone");
        indice = localiserFourn();
        System.out.println("\nSaisissez le nouveau numéro de téléphone du fournisseur, entrer seulement les dix numéros sans espaces ni caractères " +
                "spéciaux");
        input = clavier.nextLine();
        input = validerNumTelephone(input);
        telephoneFourn[indice] = input;
        System.out.println("\nLe numéro de téléphone du fournisseur # " + numeroFourn[indice] + " est maintenant " + telephoneFourn[indice]);
    }


    // Procédure pour supprimer un fournisseur
    static public void supprimerFourn(int indiceFourn){
        int ctrSup = indiceFourn;

        // Pour empêcher la suppression du fournisseur "0". Vêtement fabriqué maison
        if (indiceFourn == 0){
            System.out.println("Le fournisseurs " + nomFourn[ctrSup] + " ne peut pas être supprimé");
        }else {
            // if qui empêche la suppression d'un fournisseur qui a des vêtements présents dans l'inventaire
            if (validerFournActif(indiceFourn)){
                System.out.println("Ce fournisseur ne peut pas être supprimé car il y a des vêtements présent dans l'inventaire qui lui sont " +
                        "associés. \nSupprimer les vêtements associés à ce fournisseur avant de le supprimer");
            }else {
                System.out.println("Le fournisseur " + nomFourn[ctrSup] + " (# " + numeroFourn[ctrSup] + ") a été supprimé");

                for (numeroFourn[ctrSup] = indiceFourn; ctrSup < ctrFourn - 1; ctrSup++) {
                    numeroFourn[ctrSup] = numeroFourn[ctrSup + 1];
                    nomFourn[ctrSup] = nomFourn[ctrSup + 1];
                    telephoneFourn[ctrSup] = telephoneFourn[ctrSup];
                }
                ctrFourn--;
            }
        }
    }


    // Fonction pour vérifier si un fournisseur existe
    static public boolean fourneExiste(int verifier){
        boolean validation = true;
        int i;

        for (i = 0;i < ctrFourn;i++){
            if (verifier == numeroFourn[i]){
                validation = false;
            }
        }
        return validation;
    }


    // Fonction pour valider la saisie de l'utilisateur d'un numéro de téléphone
    static public String validerNumTelephone(String input){
        boolean validation = false;
        int idx;
        int ctr;

        while (!validation) {
            ctr = 0;
            // boucle for qui vérifie que les caractère sont des numéros
            for (idx = 0; idx < input.length(); idx++) {
                if (Character.isDigit(input.charAt(idx))) {
                    ctr++;
                }
            }
            // if qui vérifie que la longeur de la chaine est de 10 et tous des chiffres
            if (input.length() == 10 && (ctr == input.length())) {
                validation = true;
            }else {
                System.out.println("Le numéro saisi n'est pas valide. Veillez entrer seulement les dix numéros sans espaces ni caractères spéciaux");
                input = clavier.nextLine();
            }
        }
        input = "(" + input.substring(0,3) + ") " + input.substring(3,6) + "-" + input.substring(6,10);

        return input;
    }


    // Fonction qui cherche un fournisseur pour faire le lien entre les tableaux des vêtements et les tableaux des fournisseurs
    static public String chercherFourn (int numFourn){
        String nom = "";
        int idx;

        for (idx = 0;idx < ctrFourn;idx++){
            if (numFourn == numeroFourn[idx]){
                nom = nomFourn[idx];
            }
        }
        return nom;
    }


    // Procédure pour afficher un entête à la liste des fournisseurs
    static public void enteteFournisseurs(){
        trierFourn();
        System.out.println(formatter("", 55, "-"));
        System.out.println(formatter("# Fourn", 7, null) + " | "
                + formatter("Nom du fournisseur", 25, null) + " | "
                + formatter("# de téléphone", 15, null) + " | ");
        System.out.println(formatter("", 55, "-"));
    }


    // Procédure pour afficher la liste des fournisseurs
    static public void listerFournisseurs(){
        enteteFournisseurs();
        for (int idx = 0; idx < ctrFourn; idx++) {
            System.out.println(formatter(Integer.toString(numeroFourn[idx]), 7, null) + " | "
                    + formatter(nomFourn[idx], 25, null ) + " | "
                    + formatter(telephoneFourn[idx], 15, null) + " | ");
        }
    }


    // Fonction pour localiser l'indice de tableau d'un fournisseur
    static public int localiserFourn(){
        boolean validaton = false;
        String input;
        int numeroFournisseur = 0;
        int i = 0;
        int indice = 0;


        while (!validaton) {
            input = clavier.nextLine();
            numeroFournisseur = validerInt(input);

            for (i = 0; i < ctrFourn; i++) {
                if (numeroFournisseur == numeroFourn[i]){
                    validaton = true;
                    indice = i;
                }
            }
            if (!validaton){
                System.out.println(erreurSelection);
            }
        }
        return indice;
    }


    // Fonction pour valiser si le fournisseur à effacer a des vêtements en inventaire.
    static public boolean validerFournActif (int indiceFourn){
        int idx;
        boolean validation = false;

        for (idx = 0;idx < ctr; idx++){
            if (numeroFourn[indiceFourn] == codeFournisseur[idx])
                validation = true;
        }
        return validation;
    }




    //----------------------------------------------------------------
    //Method de validation
    //----------------------------------------------------------------

    // Fonction pour valider la saisie d'un int
    static public String validerSaisieVide(String input){
        boolean validation = false;

        while (!validation) {
            if (input.length() == 0) {
                System.out.println(erreurSelection);
                input = clavier.nextLine();
            }else{
                validation = true;
            }
        }
        return input;
    }


    // Fonction pour valider la saisie d'un int
    static public int validerInt(String validInt){
        int ctr;
        int idx;
        int valide;
        boolean validation = false;

        while (!validation){
            ctr = 0;
            // boucle for qui valide que tous les caractère de la chaine sont des chiffres
            for(idx = 0;idx < validInt.length();idx++){
                if (Character.isDigit(validInt.charAt(idx))){
                    ctr++;
                }
            }
            // if qui contrôle la longeur de la chaine pour éviter de dépasser la valeur maximal permise dans un int
            if (ctr == validInt.length() && !validInt.equals("") && validInt.length() <= 10) {
                validation = true;
            }
            else {
                System.out.println("Vous devez entrer un entier sans espace, qui ne doit pas être négatif et d'un maximum de 10 caractère :");
                validInt = clavier.nextLine();
            }
        }
        valide = Integer.parseInt(validInt);
        return valide;
    }


    // Fonction pour valider la saisie d'un double
    static public double validerPrix(String validDouble){
        int ctr;
        int idx;
        double valide = 0;
        boolean validationMontant = false;
        boolean validationFormat = false;
        String erreurSaisie = "Le montant dois-être d'une valeur maximal de 1 000, positif et les décimales doivent être séparé par un '.' ou ','. ";

        // deux boucles while. L'un pour valider le format du nombre et l'autre le montant permis
        while (!validationMontant) {
            while (!validationFormat) {

                // Section pour construire le format du chiffre avec deux nombres après la virgule
                validDouble = validDouble + "00";
                idx = validDouble.indexOf('.');
                if (idx > 0) {
                    validDouble = validDouble.substring(0, idx) + "." + validDouble.substring(idx + 1, idx + 3);
                }else {
                    validDouble = validDouble.substring(0, validDouble.length() - 2);
                }
                ctr = 0;
                for (idx = 0; idx < validDouble.length(); idx++) {
                    if (Character.isDigit(validDouble.charAt(idx)) ||  validDouble.charAt(idx) == 46  ||  validDouble.charAt(idx) == 44) {
                        ctr++;
                    }
                }
                if (ctr == validDouble.length() && validDouble.length() != 0) {
                    validationFormat = true;
                } else {
                    System.out.println(erreurSaisie);
                    validDouble = clavier.nextLine();
                }
            }

            valide = Float.parseFloat(validDouble);
            if (valide < 1 || valide > 1000){
                System.out.println(erreurSaisie);
                validDouble = clavier.nextLine();
                validationFormat = false;
            }else {
                validationMontant = true;
            }
        }
        return valide;
    }


} //fin class main


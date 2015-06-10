package clientlourdnesti;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stagiaire
 */
public class Accueil extends javax.swing.JFrame {
    BaseDeDonnees bdd;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Accueil acc = new Accueil();
        acc.accueil();
    }
    
    /**
     * Creates new form Interface
     */
    public void accueil(){
        initComponents();
        
        //Appel des données de la base
        bdd = new BaseDeDonnees();
        
        //Affichage dans la fenêtre Test
        JListTest();
        // Fermeture de la requête
        bdd.finRequeteBdd();
        
        //liste Echantillon
        JListEchantillon();
        bdd.finRequeteBdd();
        
        //liste Degustateur
        JListDegustateur();
        bdd.finRequeteBdd();

        //liste Organisateur        
        JListOrganisateur();
        bdd.finRequeteBdd();
        
        //liste Salle
        JListSalle();
        bdd.finRequeteBdd();
        
        setTitle("Menu");
        setVisible(true);
    }
    
    class NomEtId
    {
        String nom;
        int id;
        
        //surcharge de toString de l'objet pour avoir le "nom" à la place de "type+appel mémoire"
        //prend effet quand la Jlist appel toString pour l'affichage
        public String toString()
        {
            return nom;     
        }
    }
    
    class Identite
    {
        String nom;
        String prenom;
        int id;
        
        //surcharge de toString de l'objet pour avoir l'identité complète "nom + prenom" à la place de "type+appel mémoire"
        //prend effet quand la Jlist appel toString pour l'affichage
        public String toString()
        {
            return nom +" "+ prenom;     
        }
    }
    public void RemplirJListTest(){
        //Récupération de la liste
        ResultSet rs = bdd.getListe("Test");
        
        //Remplisage de la liste
        DefaultListModel modelTest = new DefaultListModel();
        
        try{
            //Tant qu'il y a une ligne suivante
            while(rs.next()){
                NomEtId nomEtId = new NomEtId();
                //récupération pour chaque ligne du champ nom du tableau et de l'id correspondant
                nomEtId.nom = rs.getString("Nom");
                nomEtId.id = rs.getInt("idTest");
                                
                modelTest.addElement(nomEtId);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        listeTest.setModel(modelTest);
    }
    
    public void JListTest(){
        RemplirJListTest();
        
        //Remplissage des champs voisins à la liste
        //Création d'un listener pour pouvoir récupérer l'évènement
        ListSelectionListener listener = new ListSelectionListener() {

            @Override
            //Besoin de valueChanged pour modifier l'évènement
            public void valueChanged(ListSelectionEvent e) {
                NomEtId nei = (NomEtId) listeTest.getSelectedValue();
                //Si rien n'est sélectionné
                if(nei == null){
                    return;
                }
                //récupération de l'id correspondant au nom du test
                int idTest = nei.id;
                //Récupération de la ligne correspondante à ce test dans la table
                bdd = new BaseDeDonnees();
                ResultSet ligneTest = bdd.getLigneTest(idTest);
                
                
                //Récupération des informations qu'on souhaite afficher
                String nom = "";
                String date = "";
                
                int idEchantillon = 0;
                ResultSet ligneEchantillon;
                String nomEchantillon = "";
                
                int idOrganisateur = 0;
                ResultSet ligneOrganisateur;
                String nomOrganisateur = "";
                
                int idSalle = 0;
                ResultSet ligneSalle;
                String nomSalle = "";
                
                int idDegustateur = 0;
                ResultSet ligneDegustateur;
                String identiteDegustateur = "";
                ResultSet ligneDegustateurParticipeTest = bdd.getligneDegustateurParticipeTest(idTest);
                DefaultListModel modelTestD = new DefaultListModel();
                
                float aspect = 0;
                float gout = 0;
                float odeur = 0;
                float texture = 0;
                float syntheseN = 0;
                ResultSet listeNotes;
                
                String resultat = "";
                
                try{
                    //Accès à la ligne de la table
                    ligneTest.next();
                    nom = ligneTest.getString("Nom");
                    date = ligneTest.getString("DateTest");
                    
                    idEchantillon = ligneTest.getInt("Echantillon_idEchantillon");
                    ligneEchantillon = bdd.getLigneEchantillon(idEchantillon);
                    if(ligneEchantillon.next()){
                        nomEchantillon = ligneEchantillon.getString("Nom") +" "+ ligneEchantillon.getString("Marque");
                    }
                    
                    idOrganisateur = ligneTest.getInt("Organisateur_idOrganisateur");        
                    ligneOrganisateur = bdd.getLigneOrganisateur(idOrganisateur);
                    if(ligneOrganisateur.next()){
                        nomOrganisateur = ligneOrganisateur.getString("Prenom") +" "+ ligneOrganisateur.getString("Nom");
                    }
                    idSalle = ligneTest.getInt("Salle_idSalle");   
                    ligneSalle = bdd.getLigneSalle(idSalle);
                    if (ligneSalle.next()){
                        nomSalle = ligneSalle.getString("Numero");
                    }
                    
                    //tant qu'un dégustateur est associé au test
                    while(ligneDegustateurParticipeTest.next()){
                        //on récupère son id
                        idDegustateur = ligneDegustateurParticipeTest.getInt("idDegustateur");
                        
                        //on récupère la ligne correspondante dans la table Degustateur
                        ligneDegustateur = bdd.getLigneDegustateur(idDegustateur);
                        ligneDegustateur.next();
                        identiteDegustateur = ligneDegustateur.getString("Prenom") +" "+ ligneDegustateur.getString("Nom");

                        modelTestD.addElement(identiteDegustateur);
                    }
                    
                    listeNotes = bdd.getNotesTest(idTest);
                    int nombreLignes = 0;
                                        
                    while(listeNotes.next()){
                        aspect = (aspect + listeNotes.getFloat("Aspect"));
                        gout = gout + listeNotes.getFloat("Gout");
                        odeur = odeur + listeNotes.getFloat("Odeur");
                        texture = texture + listeNotes.getFloat("Texture");
                        nombreLignes += 1;
                    }
                    aspect = aspect/nombreLignes;
                    gout = gout/nombreLignes;
                    odeur = odeur/nombreLignes;
                    texture = texture/nombreLignes;
                    syntheseN =(aspect + gout + odeur + texture)/4;
                    
                    if(syntheseN >= 2.5){
                        resultat = "Article conservé";
                    }
                    else{
                        resultat = "Article rejeté";
                    }
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
                //Affichage des informations
                testSelectionne.setText(nom);
                dateTest.setText(date);
                echantillonTest.setText(nomEchantillon);
                organisateurTest.setText(nomOrganisateur);
                salleTest.setText(nomSalle);
                listeDegustateursTest.setModel(modelTestD);
                syntheseNotes.setText(String.valueOf(syntheseN));
                resultatTest.setText(resultat);
            }
        };
        listeTest.addListSelectionListener(listener);
    }
    
    public void RemplirJListEchantillon(){
        //Récupération de la liste
        ResultSet rs = bdd.getListe("Echantillon");
        
        //Remplisage de la liste
        DefaultListModel modelEchantillons = new DefaultListModel();
        
        //variable pour stocker le résultat
        try{
            while(rs.next()){
                NomEtId nomEtId = new NomEtId();
                //récupération pour chaque ligne du champ nom du tableau                
                nomEtId.nom = rs.getString("Nom");
                nomEtId.id = rs.getInt("idEchantillon");
                
                modelEchantillons.addElement(nomEtId);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        listeEchantillon.setModel(modelEchantillons);
    }
    public void JListEchantillon(){
        RemplirJListEchantillon();
        
        //Remplissage des champs voisins à la liste
        //Création d'un listener pour pouvoir récupérer l'évènement
        ListSelectionListener listener = new ListSelectionListener(){
            @Override
            //Besoin de valueChanged pour modifier l'évènement
            public void valueChanged(ListSelectionEvent e) {
                NomEtId nei = (NomEtId) listeEchantillon.getSelectedValue();
                //Si rien n'est sélectionné
                if(nei == null){
                    return;
                }
                
                //récupération de l'id correspondant au nom de l'échantillon
                int idEchantillon = nei.id;
                
                //Récupération de la ligne correspondante à ce test dans la table
                bdd = new BaseDeDonnees();
                ResultSet ligneEchantillon = bdd.getLigneEchantillon(idEchantillon);
                
                String echantillon = "";
                String marque = "";
                int quantite = 0;
                
                ResultSet listeTest;
                ResultSet ligneTestSelectionne;
                String TestSelectionne = "";
                DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
                
                try{
                    //Accès à la ligne de la table
                    ligneEchantillon.next();
                    echantillon = ligneEchantillon.getString("Nom");
                    marque = ligneEchantillon.getString("Marque");
                    quantite = ligneEchantillon.getInt("Quantite");
                    
                    //récupération  de la table test pour remplir la combobox
                    listeTest = bdd.getTest();
                    while(listeTest.next()){
                       NomEtId neiTest = new NomEtId();
                       neiTest.nom = listeTest.getString("Nom");
                       neiTest.id = listeTest.getInt("idTest");
                       comboModel.addElement(neiTest);
                    }
                    //Si un échantillon est déjà associé à un test, le sélectionner dans la combobox
                    ligneTestSelectionne = bdd.getLigneTestEchantillon(idEchantillon);
                    if(ligneTestSelectionne.next()){
                        TestSelectionne = ligneTestSelectionne.getString("Nom");
                        comboModel.setSelectedItem(TestSelectionne);
                    }
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
                echantillonSelectionne.setText(echantillon);
                marqueEchantillon.setText(marque);
                quantiteEchantillon.setText(String.valueOf(quantite));
                testAssocieEchantillon.setModel(comboModel);
            }
        };
        listeEchantillon.addListSelectionListener(listener);
    }
    
    public void RemplirJListDegustateur(){
        //Récupération de la liste
        ResultSet rs = bdd.getListe("Degustateur");
        
        DefaultListModel modelDegustateur = new DefaultListModel();
        
        //variable pour stocker le résultat
        try{
            while(rs.next()){
                Identite identite = new Identite();
                //récupération pour chaque des champs nom et prenom du tableau et de l'id correspondant              
                identite.nom = rs.getString("Nom");
                identite.prenom = rs.getString("Prenom");
                identite.id = rs.getInt("idDegustateur");
               
                modelDegustateur.addElement(identite);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        listeDegustateur.setModel(modelDegustateur);
    }
    public void JListDegustateur(){
        RemplirJListDegustateur();
        
        //Remplissage des champs voisins à la liste
        //Création d'un listener pour pouvoir récupérer l'évènement
        ListSelectionListener listener = new ListSelectionListener(){
            @Override
            //Besoin de valueChanged pour modifier l'évènement
            public void valueChanged(ListSelectionEvent e){
            Identite ident = (Identite) listeDegustateur.getSelectedValue();
                //Si rien n'est sélectionné
                if(ident == null){
                    return;
                }
                //récupération de l'id correspondant au nom de l'échantillon
                int idDegustateur = ident.id;
                
                //Récupération de la ligne correspondante à ce test dans la table
                bdd = new BaseDeDonnees();
                ResultSet ligneDegustateur = bdd.getLigneDegustateur(idDegustateur);
                
                String identite = "";
                String nom = "";
                String prenom = "";
                String adresse = "";
                String telephone = "";
                
                ResultSet listeTest;
                ResultSet ligneTestSelectionne;
                String TestSelectionne = "";
                String nomTest = "";
                DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
                
                try{
                    //Accès à la ligne de la table
                    ligneDegustateur.next();
                    identite = ligneDegustateur.getString("Prenom") +" "+ ligneDegustateur.getString("Nom");
                    nom = ligneDegustateur.getString("Nom");
                    prenom = ligneDegustateur.getString("Prenom");
                    adresse = ligneDegustateur.getString("Adresse");
                    telephone =  ligneDegustateur.getString("Telephone");
                    
                    //récupération  de la table test pour remplir la combobox
                    listeTest = bdd.getTest();
                    while(listeTest.next()){
                        nomTest = listeTest.getString("Nom");
                        comboModel.addElement(nomTest);
                    }
                    //Si un échantillon est déjà associé à un test, le sélectionner dans la combobox
                    ligneTestSelectionne = bdd.getLigneTestDegustateur(idDegustateur);
                    while(ligneTestSelectionne.next()){
                        TestSelectionne = ligneTestSelectionne.getString("Nom");
                        comboModel.setSelectedItem(TestSelectionne);
                    }
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
                degustateurSelectionne.setText(identite);
                nomDegustateur.setText(nom);
                prenomDegustateur.setText(prenom);
                adresseDegustateur.setText(adresse);
                telephoneDegustateur.setText(telephone);
                testAssocieDegustateur.setModel(comboModel);
            }
        };
        listeDegustateur.addListSelectionListener(listener);
    }
    
    public void RemplirJListOrganisateur(){
        //Récupération de la liste
        ResultSet rs = bdd.getListe("Organisateur");
        
        DefaultListModel modelOrganisateur = new DefaultListModel();
        
        //variable pour stocker le résultat
        try{
            while(rs.next()){
                Identite identite = new Identite();
                //récupération pour chaque des champs nom et prenom du tableau et de l'id correspondant              
                identite.nom = rs.getString("Nom");
                identite.prenom = rs.getString("Prenom");
                identite.id = rs.getInt("idOrganisateur");
                
                modelOrganisateur.addElement(identite);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        listeOrganisateur.setModel(modelOrganisateur);
    }
    public void JListOrganisateur(){
        RemplirJListOrganisateur();
                
        //Remplissage des champs voisins à la liste
        //Création d'un listener pour pouvoir récupérer l'évènement
        ListSelectionListener listener = new ListSelectionListener(){
            @Override
            //Besoin de valueChanged pour modifier l'évènement
            public void valueChanged(ListSelectionEvent e){
            Identite ident = (Identite) listeOrganisateur.getSelectedValue();
                //Si rien n'est sélectionné
                if(ident == null){
                    return;
                }
                //récupération de l'id correspondant au nom de l'échantillon
                int idOrganisateur = ident.id;
                
                //Récupération de la ligne correspondante à ce test dans la table
                bdd = new BaseDeDonnees();
                ResultSet ligneOrganisateur = bdd.getLigneOrganisateur(idOrganisateur);
                
                String identite = "";
                String nom = "";
                String prenom = "";
                String adresse = "";
                String telephone = "";
                
                ResultSet listeTest;
                ResultSet ligneTestSelectionne;
                String TestSelectionne = "";
                String nomTest = "";
                DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
                
                try{
                    //Accès à la ligne de la table
                    ligneOrganisateur.next();
                    identite = ligneOrganisateur.getString("Prenom") +" "+ ligneOrganisateur.getString("Nom");
                    nom = ligneOrganisateur.getString("Nom");
                    prenom = ligneOrganisateur.getString("Prenom");
                    adresse = ligneOrganisateur.getString("Adresse");
                    telephone =  ligneOrganisateur.getString("Telephone");
                    
                    //récupération  de la table test pour remplir la combobox
                    listeTest = bdd.getTest();
                    while(listeTest.next()){
                        nomTest = listeTest.getString("Nom");
                        comboModel.addElement(nomTest);
                    }
                    //Si un échantillon est déjà associé à un test, le sélectionner dans la combobox
                    ligneTestSelectionne = bdd.getLigneTestOrganisateur(idOrganisateur);
                    while(ligneTestSelectionne.next()){
                        TestSelectionne = ligneTestSelectionne.getString("Nom");
                        comboModel.setSelectedItem(TestSelectionne);
                    }
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
                organisateurSelectionne.setText(identite);
                nomOrganisateur.setText(nom);
                prenomOrganisateur.setText(prenom);
                adresseOrganisateur.setText(adresse);
                telephoneOrganisateur.setText(telephone);
                testAssocieOrganisateur.setModel(comboModel);
            }
        };
        listeOrganisateur.addListSelectionListener(listener);
    }
    
    public void RemplirJListSalle(){
        //Récupération de la liste
        ResultSet rs = bdd.getListe("Salle");
        
        DefaultListModel modelSalle = new DefaultListModel();
        
        //variable pour stocker le résultat
        try{
            while(rs.next()){
                NomEtId nomEtId = new NomEtId();
                //récupération pour chaque ligne du champ nom du tableau                
                nomEtId.nom = rs.getString("Numero");
                nomEtId.id = rs.getInt("idSalle");
                
                modelSalle.addElement(nomEtId);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        listeSalle.setModel(modelSalle);
    }
    public void JListSalle(){
        RemplirJListSalle();
        
        //Remplissage des champs voisins à la liste
        //Création d'un listener pour pouvoir récupérer l'évènement
        ListSelectionListener listener = new ListSelectionListener(){
            @Override
            //Besoin de valueChanged pour modifier l'évènement
            public void valueChanged(ListSelectionEvent e) {
                NomEtId nei = (NomEtId) listeSalle.getSelectedValue();
                //Si rien n'est sélectionné
                if(nei == null){
                    return;
                }
                //récupération de l'id correspondant au nom de l'échantillon
                int idSalle = nei.id;
                
                //Récupération de la ligne correspondante à ce test dans la table
                bdd = new BaseDeDonnees();
                ResultSet ligneSalle = bdd.getLigneSalle(idSalle);
                
                String numero = "";
                String batiment = "";
                String dateOccupation = "";
                int capaciteAccueil = 0;
                
                ResultSet listeTest;
                ResultSet ligneTestSelectionne;
                String TestSelectionne = "";
                String nomTest = "";
                DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
                
                try{
                    //Accès à la ligne de la table
                    ligneSalle.next();
                    numero = ligneSalle.getString("Numero");
                    batiment = ligneSalle.getString("Batiment");
                    dateOccupation = ligneSalle.getString("DateOccupation");
                    capaciteAccueil = ligneSalle.getInt("CapaciteAccueil");
                    
                    //récupération  de la table test pour remplir la combobox
                    listeTest = bdd.getTest();
                    while(listeTest.next()){
                        nomTest = listeTest.getString("Nom");
                        comboModel.addElement(nomTest);
                    }
                    //Si un échantillon est déjà associé à un test, le sélectionner dans la combobox
                    ligneTestSelectionne = bdd.getLigneTestSalle(idSalle);
                    while(ligneTestSelectionne.next()){
                        TestSelectionne = ligneTestSelectionne.getString("Nom");
                        comboModel.setSelectedItem(TestSelectionne);
                    }
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
                salleSelectionnee.setText(numero);
                numeroSalle.setText(numero);
                batimentSalle.setText(batiment);
                dateSalle.setText(dateOccupation);
                capaciteAccueilSalle.setText(String.valueOf(capaciteAccueil));
                testAssocieSalle.setModel(comboModel);
            }
        };
        listeSalle.addListSelectionListener(listener);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        fenetre = new javax.swing.JTabbedPane();
        ongletTest = new javax.swing.JPanel();
        cadreListeTest = new javax.swing.JScrollPane();
        listeTest = new javax.swing.JList();
        testSelectionne = new javax.swing.JTextField();
        dateTest = new javax.swing.JTextField();
        echantillonTest = new javax.swing.JTextField();
        organisateurTest = new javax.swing.JTextField();
        salleTest = new javax.swing.JTextField();
        syntheseNotes = new javax.swing.JTextField();
        resultatTest = new javax.swing.JTextField();
        enregistrerTest = new javax.swing.JButton();
        supprimerTest = new javax.swing.JButton();
        nouveauTest = new javax.swing.JButton();
        jLabelResultatTest = new javax.swing.JLabel();
        jLabelListeTest = new javax.swing.JLabel();
        jLabelTestSelectionne = new javax.swing.JLabel();
        jLabelSalleTest = new javax.swing.JLabel();
        jLabelEchantillonTeste = new javax.swing.JLabel();
        jLabelDateTest = new javax.swing.JLabel();
        jLabelOrganisateurTest = new javax.swing.JLabel();
        jLabelDegustateurTest1 = new javax.swing.JLabel();
        jLabelSyntheseNoteTest = new javax.swing.JLabel();
        cadreListeDegustateursTest = new javax.swing.JScrollPane();
        listeDegustateursTest = new javax.swing.JList();
        ongletIngredient = new javax.swing.JPanel();
        cadreListeEchantillon = new javax.swing.JScrollPane();
        listeEchantillon = new javax.swing.JList();
        echantillonSelectionne = new javax.swing.JTextField();
        marqueEchantillon = new javax.swing.JTextField();
        quantiteEchantillon = new javax.swing.JTextField();
        testAssocieEchantillon = new javax.swing.JComboBox();
        nouveauEchantillon = new javax.swing.JButton();
        enregistrerEchantillon = new javax.swing.JButton();
        supprimerEchantillon = new javax.swing.JButton();
        jLabelEchantillonTestAssocie = new javax.swing.JLabel();
        jLabelListeEchantillon = new javax.swing.JLabel();
        jLabelQuantiteEchantillon = new javax.swing.JLabel();
        jLabelMarqueEchantillon1 = new javax.swing.JLabel();
        jLabelEchantillonSelectionne2 = new javax.swing.JLabel();
        ongletDegustateur = new javax.swing.JPanel();
        cadreListeDegustateur = new javax.swing.JScrollPane();
        listeDegustateur = new javax.swing.JList();
        degustateurSelectionne = new javax.swing.JTextField();
        nomDegustateur = new javax.swing.JTextField();
        prenomDegustateur = new javax.swing.JTextField();
        adresseDegustateur = new javax.swing.JTextField();
        telephoneDegustateur = new javax.swing.JTextField();
        nouveauDegustateur = new javax.swing.JButton();
        enregistrerDegustateur = new javax.swing.JButton();
        supprimerDegustateur = new javax.swing.JButton();
        testAssocieDegustateur = new javax.swing.JComboBox();
        jLabelListeDegustateur = new javax.swing.JLabel();
        jLabelDegustateurSelectionne = new javax.swing.JLabel();
        jLabelTelephoneDegustateur = new javax.swing.JLabel();
        jLabelNomDegustateur = new javax.swing.JLabel();
        jLabelPrenomDegustateur = new javax.swing.JLabel();
        jLabelAdresseDegustateur = new javax.swing.JLabel();
        ongletOrganisateur = new javax.swing.JPanel();
        cadreListeOrganisateur = new javax.swing.JScrollPane();
        listeOrganisateur = new javax.swing.JList();
        organisateurSelectionne = new javax.swing.JTextField();
        nouveauOrganisateur = new javax.swing.JButton();
        enregistrerOrganisateur = new javax.swing.JButton();
        supprimerOrganisateur = new javax.swing.JButton();
        nomOrganisateur = new javax.swing.JTextField();
        prenomOrganisateur = new javax.swing.JTextField();
        adresseOrganisateur = new javax.swing.JTextField();
        telephoneOrganisateur = new javax.swing.JTextField();
        testAssocieOrganisateur = new javax.swing.JComboBox();
        jLabelListeOrganisateur = new javax.swing.JLabel();
        jLabelOrganisateurSelectionne = new javax.swing.JLabel();
        jLabelNomOrganisateur = new javax.swing.JLabel();
        jLabelPrenomOrganisateur = new javax.swing.JLabel();
        jLabelAdresseOrganisateur = new javax.swing.JLabel();
        jLabelTelephoneOrganisateur = new javax.swing.JLabel();
        ongletSalle = new javax.swing.JPanel();
        cadreListeSalle = new javax.swing.JScrollPane();
        listeSalle = new javax.swing.JList();
        salleSelectionnee = new javax.swing.JTextField();
        numeroSalle = new javax.swing.JTextField();
        batimentSalle = new javax.swing.JTextField();
        dateSalle = new javax.swing.JTextField();
        capaciteAccueilSalle = new javax.swing.JTextField();
        nouveauSalle = new javax.swing.JButton();
        enregistrerSalle = new javax.swing.JButton();
        supprimerSalle = new javax.swing.JButton();
        testAssocieSalle = new javax.swing.JComboBox();
        jLabelListeSalle = new javax.swing.JLabel();
        jLabelSalleSelectionnee = new javax.swing.JLabel();
        jLabelNumeroSalle = new javax.swing.JLabel();
        jLabelDateSalle = new javax.swing.JLabel();
        jLabelNumeroSalle1 = new javax.swing.JLabel();
        jLabelNumeroSalle2 = new javax.swing.JLabel();
        ongletNotes = new javax.swing.JPanel();
        apparenceNotes = new javax.swing.JTextField();
        odeurNotes = new javax.swing.JTextField();
        goutNotes = new javax.swing.JTextField();
        textureNotes = new javax.swing.JTextField();
        commentaireNotes = new javax.swing.JTextField();
        resultatFinalNotes = new javax.swing.JTextField();
        nouveauNotes = new javax.swing.JButton();
        enregistrerNotes = new javax.swing.JButton();
        supprimerNotes = new javax.swing.JButton();
        testAssocieNotes = new javax.swing.JComboBox();
        degustateurSelectionneNotes = new javax.swing.JComboBox();
        ingredientAssocieNotes = new javax.swing.JComboBox();
        jLabelNumeroSalle3 = new javax.swing.JLabel();
        jLabelNumeroSalle4 = new javax.swing.JLabel();
        jLabelNumeroSalle5 = new javax.swing.JLabel();
        jLabelNumeroSalle6 = new javax.swing.JLabel();
        jLabelNumeroSalle7 = new javax.swing.JLabel();
        jLabelNumeroSalle8 = new javax.swing.JLabel();
        titreBienvenue = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fenetre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ongletTest.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listeTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cadreListeTest.setViewportView(listeTest);

        ongletTest.add(cadreListeTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 510));

        testSelectionne.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        testSelectionne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testSelectionneActionPerformed(evt);
            }
        });
        ongletTest.add(testSelectionne, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 39, 537, 28));

        dateTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dateTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateTestActionPerformed(evt);
            }
        });
        ongletTest.add(dateTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 87, 490, 28));

        echantillonTest.setEditable(false);
        echantillonTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        echantillonTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                echantillonTestActionPerformed(evt);
            }
        });
        ongletTest.add(echantillonTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 121, 430, 28));

        organisateurTest.setEditable(false);
        organisateurTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        organisateurTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                organisateurTestActionPerformed(evt);
            }
        });
        ongletTest.add(organisateurTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(257, 155, 450, 28));

        salleTest.setEditable(false);
        salleTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        salleTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salleTestActionPerformed(evt);
            }
        });
        ongletTest.add(salleTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 189, 500, 28));

        syntheseNotes.setEditable(false);
        syntheseNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletTest.add(syntheseNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 410, 413, 28));

        resultatTest.setEditable(false);
        resultatTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletTest.add(resultatTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 444, 435, 28));

        enregistrerTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        enregistrerTest.setText("Enregistrer");
        enregistrerTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerTestActionPerformed(evt);
            }
        });
        ongletTest.add(enregistrerTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 100, 30));

        supprimerTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supprimerTest.setText("Supprimer");
        supprimerTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerTestActionPerformed(evt);
            }
        });
        ongletTest.add(supprimerTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 500, 100, 30));

        nouveauTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nouveauTest.setText("Nouveau");
        nouveauTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauTestActionPerformed(evt);
            }
        });
        ongletTest.add(nouveauTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 500, 100, 30));

        jLabelResultatTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelResultatTest.setText("Résultat du test :");
        ongletTest.add(jLabelResultatTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 444, -1, 28));

        jLabelListeTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelListeTest.setText("Liste des tests : ");
        ongletTest.add(jLabelListeTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 30));

        jLabelTestSelectionne.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelTestSelectionne.setText("Test sélectionné : ");
        ongletTest.add(jLabelTestSelectionne, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 130, 28));

        jLabelSalleTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelSalleTest.setText("Salle :");
        ongletTest.add(jLabelSalleTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 189, -1, 28));

        jLabelEchantillonTeste.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelEchantillonTeste.setText("Echantillon testé :");
        ongletTest.add(jLabelEchantillonTeste, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 121, 100, 28));

        jLabelDateTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelDateTest.setText("Date :");
        ongletTest.add(jLabelDateTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 87, 40, 28));

        jLabelOrganisateurTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelOrganisateurTest.setText("Organisateur :");
        ongletTest.add(jLabelOrganisateurTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 155, 80, 28));

        jLabelDegustateurTest1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelDegustateurTest1.setText("Dégustateur :");
        ongletTest.add(jLabelDegustateurTest1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 223, 90, 28));

        jLabelSyntheseNoteTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelSyntheseNoteTest.setText("Synthèse des notes :");
        ongletTest.add(jLabelSyntheseNoteTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 410, 118, 28));

        listeDegustateursTest.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cadreListeDegustateursTest.setViewportView(listeDegustateursTest);

        ongletTest.add(cadreListeDegustateursTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(257, 223, 450, 180));

        fenetre.addTab("Test", ongletTest);

        ongletIngredient.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listeEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        listeEchantillon.setModel(new DefaultListModel()
        );
        cadreListeEchantillon.setViewportView(listeEchantillon);

        ongletIngredient.add(cadreListeEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 510));

        echantillonSelectionne.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        echantillonSelectionne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                echantillonSelectionneActionPerformed(evt);
            }
        });
        ongletIngredient.add(echantillonSelectionne, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 39, 537, 28));

        marqueEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        marqueEchantillon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marqueEchantillonActionPerformed(evt);
            }
        });
        ongletIngredient.add(marqueEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 87, 481, 28));

        quantiteEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletIngredient.add(quantiteEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 131, 469, 28));

        testAssocieEchantillon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testAssocieEchantillonActionPerformed(evt);
            }
        });
        ongletIngredient.add(testAssocieEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 349, 537, 28));

        nouveauEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nouveauEchantillon.setText("Nouveau");
        nouveauEchantillon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauEchantillonActionPerformed(evt);
            }
        });
        ongletIngredient.add(nouveauEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 500, 100, 30));

        enregistrerEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        enregistrerEchantillon.setText("Enregistrer");
        enregistrerEchantillon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerEchantillonActionPerformed(evt);
            }
        });
        ongletIngredient.add(enregistrerEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 100, 30));

        supprimerEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supprimerEchantillon.setText("Supprimer");
        supprimerEchantillon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerEchantillonActionPerformed(evt);
            }
        });
        ongletIngredient.add(supprimerEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 500, 100, 30));

        jLabelEchantillonTestAssocie.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelEchantillonTestAssocie.setText("Test associé : ");
        ongletIngredient.add(jLabelEchantillonTestAssocie, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 315, 80, 28));

        jLabelListeEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelListeEchantillon.setText("Liste des échantillons : ");
        ongletIngredient.add(jLabelListeEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 30));

        jLabelQuantiteEchantillon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelQuantiteEchantillon.setText("Quantité : ");
        ongletIngredient.add(jLabelQuantiteEchantillon, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 131, 60, 28));

        jLabelMarqueEchantillon1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelMarqueEchantillon1.setText("Marque :");
        ongletIngredient.add(jLabelMarqueEchantillon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 87, 50, 28));

        jLabelEchantillonSelectionne2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelEchantillonSelectionne2.setText("Echantillon sélectionné : ");
        ongletIngredient.add(jLabelEchantillonSelectionne2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 150, 28));

        fenetre.addTab("Echantillon", ongletIngredient);

        ongletDegustateur.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listeDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cadreListeDegustateur.setViewportView(listeDegustateur);

        ongletDegustateur.add(cadreListeDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 510));

        degustateurSelectionne.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        degustateurSelectionne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                degustateurSelectionneActionPerformed(evt);
            }
        });
        ongletDegustateur.add(degustateurSelectionne, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 39, 537, 28));

        nomDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nomDegustateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomDegustateurActionPerformed(evt);
            }
        });
        ongletDegustateur.add(nomDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 97, 500, 28));

        prenomDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletDegustateur.add(prenomDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(227, 131, 480, 28));

        adresseDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletDegustateur.add(adresseDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(227, 165, 480, 28));

        telephoneDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletDegustateur.add(telephoneDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 199, 460, 28));

        nouveauDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nouveauDegustateur.setText("Nouveau");
        nouveauDegustateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauDegustateurActionPerformed(evt);
            }
        });
        ongletDegustateur.add(nouveauDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 500, 100, 30));

        enregistrerDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        enregistrerDegustateur.setText("Enregistrer");
        ongletDegustateur.add(enregistrerDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 100, 30));

        supprimerDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supprimerDegustateur.setText("Supprimer");
        ongletDegustateur.add(supprimerDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 500, 100, 30));

        ongletDegustateur.add(testAssocieDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 349, 537, 28));

        jLabelListeDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelListeDegustateur.setText("Liste des dégustateurs : ");
        ongletDegustateur.add(jLabelListeDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 30));

        jLabelDegustateurSelectionne.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelDegustateurSelectionne.setText("Dégustateur sélectionné : ");
        ongletDegustateur.add(jLabelDegustateurSelectionne, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 150, 28));

        jLabelTelephoneDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelTelephoneDegustateur.setText("Téléphone :");
        ongletDegustateur.add(jLabelTelephoneDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 199, 70, 28));

        jLabelNomDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNomDegustateur.setText("Nom :");
        ongletDegustateur.add(jLabelNomDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 97, 40, 28));

        jLabelPrenomDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelPrenomDegustateur.setText("Prénom :");
        ongletDegustateur.add(jLabelPrenomDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 131, 60, 28));

        jLabelAdresseDegustateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelAdresseDegustateur.setText("Adresse :");
        ongletDegustateur.add(jLabelAdresseDegustateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 165, 60, 28));

        fenetre.addTab("Dégustateur", ongletDegustateur);

        ongletOrganisateur.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listeOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cadreListeOrganisateur.setViewportView(listeOrganisateur);

        ongletOrganisateur.add(cadreListeOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 510));

        organisateurSelectionne.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        organisateurSelectionne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                organisateurSelectionneActionPerformed(evt);
            }
        });
        ongletOrganisateur.add(organisateurSelectionne, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 39, 537, 28));

        nouveauOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nouveauOrganisateur.setText("Nouveau");
        nouveauOrganisateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauOrganisateurActionPerformed(evt);
            }
        });
        ongletOrganisateur.add(nouveauOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 500, 100, 30));

        enregistrerOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        enregistrerOrganisateur.setText("Enregistrer");
        ongletOrganisateur.add(enregistrerOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 100, 30));

        supprimerOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supprimerOrganisateur.setText("Supprimer");
        ongletOrganisateur.add(supprimerOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 500, 100, 30));

        nomOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nomOrganisateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomOrganisateurActionPerformed(evt);
            }
        });
        ongletOrganisateur.add(nomOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 97, 500, 28));

        prenomOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletOrganisateur.add(prenomOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(227, 131, 480, 28));

        adresseOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletOrganisateur.add(adresseOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(227, 165, 480, 28));

        telephoneOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletOrganisateur.add(telephoneOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 199, 460, 28));

        ongletOrganisateur.add(testAssocieOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 349, 537, 28));

        jLabelListeOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelListeOrganisateur.setText("Liste des organisateurs : ");
        ongletOrganisateur.add(jLabelListeOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 30));

        jLabelOrganisateurSelectionne.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelOrganisateurSelectionne.setText("Organisateur sélectionné : ");
        ongletOrganisateur.add(jLabelOrganisateurSelectionne, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 150, 28));

        jLabelNomOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNomOrganisateur.setText("Nom :");
        ongletOrganisateur.add(jLabelNomOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 97, 40, 28));

        jLabelPrenomOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelPrenomOrganisateur.setText("Prénom :");
        ongletOrganisateur.add(jLabelPrenomOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 131, 60, 28));

        jLabelAdresseOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelAdresseOrganisateur.setText("Adresse :");
        ongletOrganisateur.add(jLabelAdresseOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 165, 60, 28));

        jLabelTelephoneOrganisateur.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelTelephoneOrganisateur.setText("Téléphone :");
        ongletOrganisateur.add(jLabelTelephoneOrganisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 199, 70, 28));

        fenetre.addTab("Organisateur", ongletOrganisateur);

        ongletSalle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listeSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cadreListeSalle.setViewportView(listeSalle);

        ongletSalle.add(cadreListeSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 510));

        salleSelectionnee.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        salleSelectionnee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salleSelectionneeActionPerformed(evt);
            }
        });
        ongletSalle.add(salleSelectionnee, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 39, 537, 28));

        numeroSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        numeroSalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numeroSalleActionPerformed(evt);
            }
        });
        ongletSalle.add(numeroSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(227, 97, 480, 28));

        batimentSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletSalle.add(batimentSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 131, 473, 28));

        dateSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletSalle.add(dateSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 165, 497, 28));

        capaciteAccueilSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletSalle.add(capaciteAccueilSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 199, 427, 28));

        nouveauSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nouveauSalle.setText("Nouveau");
        nouveauSalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauSalleActionPerformed(evt);
            }
        });
        ongletSalle.add(nouveauSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 500, 100, 30));

        enregistrerSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        enregistrerSalle.setText("Enregistrer");
        ongletSalle.add(enregistrerSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 500, 100, 30));

        supprimerSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supprimerSalle.setText("Supprimer");
        ongletSalle.add(supprimerSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 500, 100, 30));

        ongletSalle.add(testAssocieSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 349, 537, 28));

        jLabelListeSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelListeSalle.setText("Liste des salles : ");
        ongletSalle.add(jLabelListeSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 30));

        jLabelSalleSelectionnee.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelSalleSelectionnee.setText("Salle sélectionnée : ");
        ongletSalle.add(jLabelSalleSelectionnee, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 150, 28));

        jLabelNumeroSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle.setText("Capacité d'accueil :");
        ongletSalle.add(jLabelNumeroSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 199, 110, 28));

        jLabelDateSalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelDateSalle.setText("Date :");
        ongletSalle.add(jLabelDateSalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 165, 40, 28));

        jLabelNumeroSalle1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle1.setText("Numéro :");
        ongletSalle.add(jLabelNumeroSalle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 97, 60, 28));

        jLabelNumeroSalle2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle2.setText("Bâtiment :");
        ongletSalle.add(jLabelNumeroSalle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 131, 60, 28));

        fenetre.addTab("Salle", ongletSalle);

        ongletNotes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        apparenceNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ongletNotes.add(apparenceNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(167, 165, 510, 28));

        odeurNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        odeurNotes.setText("odeur");
        ongletNotes.add(odeurNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 199, 537, 28));

        goutNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        goutNotes.setText("goût");
        ongletNotes.add(goutNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 233, 543, 28));

        textureNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        textureNotes.setText("texture");
        textureNotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textureNotesActionPerformed(evt);
            }
        });
        ongletNotes.add(textureNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 267, 527, 28));

        commentaireNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        commentaireNotes.setText("commentaire");
        ongletNotes.add(commentaireNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 301, 499, 28));

        resultatFinalNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        resultatFinalNotes.setText("résultat final");
        ongletNotes.add(resultatFinalNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 359, 500, 28));

        nouveauNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nouveauNotes.setText("Nouveau");
        nouveauNotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauNotesActionPerformed(evt);
            }
        });
        ongletNotes.add(nouveauNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 100, 30));

        enregistrerNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        enregistrerNotes.setText("Enregistrer");
        ongletNotes.add(enregistrerNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 450, 100, 30));

        supprimerNotes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supprimerNotes.setText("Supprimer");
        ongletNotes.add(supprimerNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 450, 100, 30));

        testAssocieNotes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ongletNotes.add(testAssocieNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 93, 577, 28));

        degustateurSelectionneNotes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ongletNotes.add(degustateurSelectionneNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 25, 577, 28));

        ingredientAssocieNotes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ongletNotes.add(ingredientAssocieNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 59, 577, 28));

        jLabelNumeroSalle3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle3.setText("Goût :");
        ongletNotes.add(jLabelNumeroSalle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 233, 40, 28));

        jLabelNumeroSalle4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle4.setText("Résultat Final :");
        ongletNotes.add(jLabelNumeroSalle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 359, 80, 28));

        jLabelNumeroSalle5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle5.setText("Texture :");
        ongletNotes.add(jLabelNumeroSalle5, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 267, 60, 28));

        jLabelNumeroSalle6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle6.setText("Odeur :");
        ongletNotes.add(jLabelNumeroSalle6, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 199, 50, 28));

        jLabelNumeroSalle7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle7.setText("Apparence :");
        ongletNotes.add(jLabelNumeroSalle7, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 165, 70, 28));

        jLabelNumeroSalle8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNumeroSalle8.setText("Commentaire :");
        ongletNotes.add(jLabelNumeroSalle8, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 301, 80, 28));

        fenetre.addTab("Notes", ongletNotes);

        getContentPane().add(fenetre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 760, 580));

        titreBienvenue.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        titreBienvenue.setForeground(new java.awt.Color(51, 0, 0));
        titreBienvenue.setText("Application d'évaluation des ingrédients");
        getContentPane().add(titreBienvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, 47));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/clientlourdnesti/LogoNesti2.png"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(678, 281));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jLabel1, org.jdesktop.beansbinding.ELProperty.create("${icon}"), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("icon"), "LogoNesti");
        binding.setSourceNullValue(null);
        bindingGroup.addBinding(binding);

        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 120, 50));

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void testSelectionneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testSelectionneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_testSelectionneActionPerformed

    private void dateTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateTestActionPerformed

    private void echantillonSelectionneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_echantillonSelectionneActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_echantillonSelectionneActionPerformed

    private void marqueEchantillonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marqueEchantillonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_marqueEchantillonActionPerformed

    private void degustateurSelectionneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_degustateurSelectionneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_degustateurSelectionneActionPerformed

    private void nomDegustateurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomDegustateurActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomDegustateurActionPerformed

    private void organisateurSelectionneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_organisateurSelectionneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_organisateurSelectionneActionPerformed

    private void salleSelectionneeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salleSelectionneeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salleSelectionneeActionPerformed

    private void numeroSalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numeroSalleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numeroSalleActionPerformed

    private void textureNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textureNotesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textureNotesActionPerformed

    private void nouveauTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauTestActionPerformed
        listeTest.clearSelection();
        testSelectionne.setText("");
        dateTest.setText("");
        echantillonTest.setText("");
        organisateurTest.setText("");
        salleTest.setText("");
        listeDegustateursTest.setListData(new Object[0]);
        syntheseNotes.setText("");
        resultatTest.setText("");
    }//GEN-LAST:event_nouveauTestActionPerformed

    private void nouveauEchantillonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauEchantillonActionPerformed
        listeEchantillon.clearSelection();
        echantillonSelectionne.setText("");
        marqueEchantillon.setText("");
        quantiteEchantillon.setText("");
        testAssocieEchantillon.setSelectedIndex(-1);
    }//GEN-LAST:event_nouveauEchantillonActionPerformed

    private void nouveauDegustateurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauDegustateurActionPerformed
        listeDegustateur.clearSelection();
        degustateurSelectionne.setText("");
        nomDegustateur.setText("");
        prenomDegustateur.setText("");
        adresseDegustateur.setText("");
        telephoneDegustateur.setText("");
        testAssocieDegustateur.setSelectedIndex(-1);
    }//GEN-LAST:event_nouveauDegustateurActionPerformed

    private void nouveauOrganisateurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauOrganisateurActionPerformed
        listeOrganisateur.clearSelection();
        organisateurSelectionne.setText("");
        nomOrganisateur.setText("");
        prenomOrganisateur.setText("");
        adresseOrganisateur.setText("");
        telephoneOrganisateur.setText("");
        testAssocieOrganisateur.setSelectedIndex(-1);
    }//GEN-LAST:event_nouveauOrganisateurActionPerformed

    private void nouveauSalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauSalleActionPerformed
        listeSalle.clearSelection();
        salleSelectionnee.setText("");
        numeroSalle.setText("");
        batimentSalle.setText("");
        dateSalle.setText("");
        capaciteAccueilSalle.setText("");
        testAssocieSalle.setSelectedIndex(-1);
    }//GEN-LAST:event_nouveauSalleActionPerformed

    private void nouveauNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauNotesActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_nouveauNotesActionPerformed

    private void nomOrganisateurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomOrganisateurActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomOrganisateurActionPerformed

    private void echantillonTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_echantillonTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_echantillonTestActionPerformed

    private void salleTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salleTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salleTestActionPerformed

    private void organisateurTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_organisateurTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_organisateurTestActionPerformed

    private void testAssocieEchantillonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testAssocieEchantillonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_testAssocieEchantillonActionPerformed

    private void enregistrerTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerTestActionPerformed
        //récupération des données des champs
        String nom = testSelectionne.getText();
        String date = dateTest.getText();
        
        //Envoi de la bonne requête suivant le cas
        bdd = new BaseDeDonnees();
        ResultSet Test;
        
        if(listeTest.isSelectionEmpty()){
            Test = bdd.setLigneNouveauTest(nom, date);
        }
        else{
            //récupération de l'id pour un test sélectionné
            int index = listeTest.getSelectedIndex();
            NomEtId nei = new NomEtId();
            nei = (NomEtId) listeTest.getModel().getElementAt(index);
            int id = nei.id;
        
            bdd.setLigneModifierTest(id, nom, date);
        }
        RemplirJListTest();
        
    }//GEN-LAST:event_enregistrerTestActionPerformed

    private void supprimerTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerTestActionPerformed
        //récupération de l'id pour un test sélectionné
        ResultSet Test; 
        if(listeTest.isSelectionEmpty() == false){    
            int index = listeTest.getSelectedIndex();
            NomEtId nei = new NomEtId();
            nei = (NomEtId) listeTest.getModel().getElementAt(index);
            int id = nei.id;
        
            bdd.supprimerTest(id);
        }
        RemplirJListTest();
    }//GEN-LAST:event_supprimerTestActionPerformed

    private void enregistrerEchantillonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enregistrerEchantillonActionPerformed
        // TODO add your handling code here:
        //récupération des données des champs
        String nom = echantillonSelectionne.getText();
        String marque = marqueEchantillon.getText();
        int quantite = Integer.valueOf(quantiteEchantillon.getText());
        
        //récupération de l'id du test sélectionné
        int idTest = 0;
        int index = testAssocieEchantillon.getSelectedIndex();
        if(testAssocieEchantillon.getSelectedIndex() != -1){
            NomEtId nei = (NomEtId) testAssocieEchantillon.getItemAt(index);
            idTest = nei.id;
        }
        
        //récupération de l'id de l'échantillon sélectionné
        int indexEchantillon = listeEchantillon.getSelectedIndex();
        int idEchantillon = 0;
        if(listeEchantillon.getSelectedIndex() != -1){
            NomEtId neiEchantillon = (NomEtId) listeEchantillon.getModel().getElementAt(indexEchantillon);
            idEchantillon = neiEchantillon.id;
        }
        //Envoi de la bonne requête suivant le cas
        bdd = new BaseDeDonnees();
        int echantillon;
        ResultSet association;
        
        if(listeEchantillon.isSelectionEmpty()){
            //création de l'échantillon
            idEchantillon = bdd.setLigneNouveauEchantillon(nom,marque,quantite);
            
            //si un test est sélectionné
            if(testAssocieEchantillon.getSelectedIndex() != -1){
                bdd.associeTestEchantillon(idTest,idEchantillon);
                bdd.finRequeteBdd();
            }
        }
        else{
            bdd.setLigneModifierEchantillon(idEchantillon,nom,marque,quantite);
            //si un test est sélectionné
            if(testAssocieEchantillon.getSelectedIndex() != -1){
                bdd.associeTestEchantillon(idTest,idEchantillon);
                bdd.finRequeteBdd();
            }
        }
        RemplirJListEchantillon();
    }//GEN-LAST:event_enregistrerEchantillonActionPerformed

    private void supprimerEchantillonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerEchantillonActionPerformed
        //récupération de l'id pour un Echantillon sélectionné
        ResultSet Echantillon; 
        //Si un échantillon est sélectionné
        if(listeEchantillon.isSelectionEmpty() == false){    
            int idTest = 0;
            int index = testAssocieEchantillon.getSelectedIndex();
           
            //récupération de l'id de l'échantillon sélectionné
            int indexE = listeEchantillon.getSelectedIndex();
            NomEtId nei = new NomEtId();
            nei = (NomEtId) listeEchantillon.getModel().getElementAt(indexE);
            int idEchantillon = nei.id;
        
            bdd.supprimerEchantillon(idEchantillon);
        }
        RemplirJListEchantillon();
    }//GEN-LAST:event_supprimerEchantillonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adresseDegustateur;
    private javax.swing.JTextField adresseOrganisateur;
    private javax.swing.JTextField apparenceNotes;
    private javax.swing.JTextField batimentSalle;
    private javax.swing.JScrollPane cadreListeDegustateur;
    private javax.swing.JScrollPane cadreListeDegustateursTest;
    private javax.swing.JScrollPane cadreListeEchantillon;
    private javax.swing.JScrollPane cadreListeOrganisateur;
    private javax.swing.JScrollPane cadreListeSalle;
    private javax.swing.JScrollPane cadreListeTest;
    private javax.swing.JTextField capaciteAccueilSalle;
    private javax.swing.JTextField commentaireNotes;
    private javax.swing.JTextField dateSalle;
    private javax.swing.JTextField dateTest;
    private javax.swing.JTextField degustateurSelectionne;
    private javax.swing.JComboBox degustateurSelectionneNotes;
    private javax.swing.JTextField echantillonSelectionne;
    private javax.swing.JTextField echantillonTest;
    private javax.swing.JButton enregistrerDegustateur;
    private javax.swing.JButton enregistrerEchantillon;
    private javax.swing.JButton enregistrerNotes;
    private javax.swing.JButton enregistrerOrganisateur;
    private javax.swing.JButton enregistrerSalle;
    private javax.swing.JButton enregistrerTest;
    private javax.swing.JTabbedPane fenetre;
    private javax.swing.JTextField goutNotes;
    private javax.swing.JComboBox ingredientAssocieNotes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelAdresseDegustateur;
    private javax.swing.JLabel jLabelAdresseOrganisateur;
    private javax.swing.JLabel jLabelDateSalle;
    private javax.swing.JLabel jLabelDateTest;
    private javax.swing.JLabel jLabelDegustateurSelectionne;
    private javax.swing.JLabel jLabelDegustateurTest1;
    private javax.swing.JLabel jLabelEchantillonSelectionne2;
    private javax.swing.JLabel jLabelEchantillonTestAssocie;
    private javax.swing.JLabel jLabelEchantillonTeste;
    private javax.swing.JLabel jLabelListeDegustateur;
    private javax.swing.JLabel jLabelListeEchantillon;
    private javax.swing.JLabel jLabelListeOrganisateur;
    private javax.swing.JLabel jLabelListeSalle;
    private javax.swing.JLabel jLabelListeTest;
    private javax.swing.JLabel jLabelMarqueEchantillon1;
    private javax.swing.JLabel jLabelNomDegustateur;
    private javax.swing.JLabel jLabelNomOrganisateur;
    private javax.swing.JLabel jLabelNumeroSalle;
    private javax.swing.JLabel jLabelNumeroSalle1;
    private javax.swing.JLabel jLabelNumeroSalle2;
    private javax.swing.JLabel jLabelNumeroSalle3;
    private javax.swing.JLabel jLabelNumeroSalle4;
    private javax.swing.JLabel jLabelNumeroSalle5;
    private javax.swing.JLabel jLabelNumeroSalle6;
    private javax.swing.JLabel jLabelNumeroSalle7;
    private javax.swing.JLabel jLabelNumeroSalle8;
    private javax.swing.JLabel jLabelOrganisateurSelectionne;
    private javax.swing.JLabel jLabelOrganisateurTest;
    private javax.swing.JLabel jLabelPrenomDegustateur;
    private javax.swing.JLabel jLabelPrenomOrganisateur;
    private javax.swing.JLabel jLabelQuantiteEchantillon;
    private javax.swing.JLabel jLabelResultatTest;
    private javax.swing.JLabel jLabelSalleSelectionnee;
    private javax.swing.JLabel jLabelSalleTest;
    private javax.swing.JLabel jLabelSyntheseNoteTest;
    private javax.swing.JLabel jLabelTelephoneDegustateur;
    private javax.swing.JLabel jLabelTelephoneOrganisateur;
    private javax.swing.JLabel jLabelTestSelectionne;
    private javax.swing.JList listeDegustateur;
    private javax.swing.JList listeDegustateursTest;
    private javax.swing.JList listeEchantillon;
    private javax.swing.JList listeOrganisateur;
    private javax.swing.JList listeSalle;
    private javax.swing.JList listeTest;
    private javax.swing.JTextField marqueEchantillon;
    private javax.swing.JTextField nomDegustateur;
    private javax.swing.JTextField nomOrganisateur;
    private javax.swing.JButton nouveauDegustateur;
    private javax.swing.JButton nouveauEchantillon;
    private javax.swing.JButton nouveauNotes;
    private javax.swing.JButton nouveauOrganisateur;
    private javax.swing.JButton nouveauSalle;
    private javax.swing.JButton nouveauTest;
    private javax.swing.JTextField numeroSalle;
    private javax.swing.JTextField odeurNotes;
    private javax.swing.JPanel ongletDegustateur;
    private javax.swing.JPanel ongletIngredient;
    private javax.swing.JPanel ongletNotes;
    private javax.swing.JPanel ongletOrganisateur;
    private javax.swing.JPanel ongletSalle;
    private javax.swing.JPanel ongletTest;
    private javax.swing.JTextField organisateurSelectionne;
    private javax.swing.JTextField organisateurTest;
    private javax.swing.JTextField prenomDegustateur;
    private javax.swing.JTextField prenomOrganisateur;
    private javax.swing.JTextField quantiteEchantillon;
    private javax.swing.JTextField resultatFinalNotes;
    private javax.swing.JTextField resultatTest;
    private javax.swing.JTextField salleSelectionnee;
    private javax.swing.JTextField salleTest;
    private javax.swing.JButton supprimerDegustateur;
    private javax.swing.JButton supprimerEchantillon;
    private javax.swing.JButton supprimerNotes;
    private javax.swing.JButton supprimerOrganisateur;
    private javax.swing.JButton supprimerSalle;
    private javax.swing.JButton supprimerTest;
    private javax.swing.JTextField syntheseNotes;
    private javax.swing.JTextField telephoneDegustateur;
    private javax.swing.JTextField telephoneOrganisateur;
    private javax.swing.JComboBox testAssocieDegustateur;
    private javax.swing.JComboBox testAssocieEchantillon;
    private javax.swing.JComboBox testAssocieNotes;
    private javax.swing.JComboBox testAssocieOrganisateur;
    private javax.swing.JComboBox testAssocieSalle;
    private javax.swing.JTextField testSelectionne;
    private javax.swing.JTextField textureNotes;
    private javax.swing.JLabel titreBienvenue;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlourdnesti;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 *
 * @author stagiaire
 */
public class BaseDeDonnees {
    Connexion connexion;
    
    BaseDeDonnees(){
        connexion = new Connexion();
        connexion.connecte();
    }
    public void finRequeteBdd(){
        connexion.finRequete();
    }
    
    //Sert à fermer automatiquement la connexion sans appel nécessaire
    public void finalize(){
        connexion.deconnecte();
    }
    
    public ResultSet getListe(String table){
        String sql = "SELECT * FROM "+table;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);

        return resSet;
    }
    
    public ResultSet getLigneTest(int id){
        String sql = "SELECT * FROM test WHERE idTest = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getLigneEchantillon(int id){
        String sql = "SELECT * FROM echantillon WHERE idEchantillon = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
            
    public ResultSet getLigneSalle(int id){
        String sql = "SELECT * FROM salle WHERE idSalle = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getLigneOrganisateur(int id){
        String sql = "SELECT * FROM organisateur WHERE idOrganisateur = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getLigneDegustateur(int id){
        String sql = "SELECT * FROM degustateur WHERE idDegustateur = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getligneDegustateurParticipeTest(int id){
        String sql = "SELECT * FROM degustateur, participe "
        + "WHERE idDegustateur = Degustateur_idDegustateur and Test_idTest = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getNotesTest(int id){
        String sql = "SELECT note.Aspect, note.Gout, note.Odeur, note.Texture " +
                    "FROM note " +
                    "INNER JOIN test " +
                    "WHERE note.Echantillon_idEchantillon = test.Echantillon_idEchantillon " +
                    "AND idTest = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getTest(){
        String sql = "SELECT * FROM test";
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getLigneTestEchantillon(int id){
        String sql = "SELECT * FROM test WHERE Echantillon_idEchantillon = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getLigneTestDegustateur(int id){
        String sql = "SELECT test.idTest, test.Nom FROM test, participe "
                + "WHERE Test_idTest = idTest AND Degustateur_idDegustateur = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getLigneTestOrganisateur(int id){
        String sql = "SELECT * FROM test WHERE Organisateur_idOrganisateur = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public ResultSet getLigneTestSalle(int id){
        String sql = "SELECT * FROM test WHERE Salle_idSalle = "+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        
        return resSet;
    }
    
    public int setLigneNouveauTest(String nom, String date){
        String sql = "INSERT INTO test (Nom,DateTest)"
                + " VALUES ('"+nom+"','"+date+"')";
        
        //Envoi et exécution de la requête
        connexion.envoiRequete(sql);
        
        ResultSet resSet = connexion.envoiRequete("SELECT LAST_INSERT_ID()");
        
        try{
            resSet.next();
            return resSet.getInt(1);
        }catch(Exception e)
        {
            return 0;
        }
    }
    
    public void setLigneModifierTest(int id,String nom, String date){
        String sql = "UPDATE test "
                + " SET Nom='"+nom+"',DateTest='"+date+"'"
                + " WHERE idTest="+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
    
    public void supprimerTest(int id){
        String sql = "Delete FROM test WHERE idTest="+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
    
    public int setLigneNouveauEchantillon(String nom, String marque, int quantite){
        String sql = "INSERT INTO echantillon (Nom,Marque,Quantite)"
                + " VALUES ('"+nom+"','"+marque+"',"+quantite+")";
        String sql2 = "SELECT LAST_INSERT_ID()";

        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        ResultSet resSet2 = connexion.envoiRequete(sql2);
        
        
        try{
            resSet2.next();
            return resSet2.getInt(1);
        }catch(Exception e)
        {
            return 0;
        }
    }
    
    public void setLigneModifierEchantillon(int id, String nom, String marque, int quantite){
        String sql = "UPDATE echantillon "
                + " SET Nom='"+nom+"',Marque='"+marque+"',Quantite="+quantite
                + " WHERE idEchantillon="+id;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
    
    public void associeTestEchantillon(int idTest, int idEchantillon){
        String sql = "UPDATE test "
                + " SET Echantillon_idEchantillon="+idEchantillon
                + " WHERE idTest="+idTest;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
    
    public void supprimerEchantillon(int idEchantillon){
        //Supression du lien entre Echantillon et Test
        String sqlTest = "UPDATE test "
                + " SET Echantillon_idEchantillon=NULL"
                + " WHERE Echantillon_idEchantillon="+idEchantillon;
        
        //Envoi et exécution de la requête
        ResultSet resSetT = connexion.envoiRequete(sqlTest);

        //Suppression de l'échantillon dans la table Echantillon
        String sqlEchantillon = "DELETE FROM echantillon WHERE idEchantillon="+idEchantillon;
        
        //Envoi et exécution de la requête
        ResultSet resSetE = connexion.envoiRequete(sqlEchantillon);
    }
    public int setLigneNouveauDegustateur(String nom, String prenom, String adresse, String telephone){
        String sql = "INSERT INTO degustateur (Nom,Prenom,Adresse,Telephone)"
                + " VALUES ('"+nom+"','"+prenom+"','"+adresse+"','"+telephone+"')";
        String sql2 = "SELECT LAST_INSERT_ID()";

        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        ResultSet resSet2 = connexion.envoiRequete(sql2);
        
        try{
            resSet2.next();
            return resSet2.getInt(1);
        }catch(Exception e)
        {
            return 0;
        }
    }
    public void setLigneModifierDegustateur(int idDegustateur, String nom, String prenom, String adresse, String telephone){
        String sql = "UPDATE degustateur "
                + " SET Nom='"+nom+"',Prenom='"+prenom+"',Adresse='"+adresse+"',Telephone='"+telephone+"'"
                + " WHERE idDegustateur="+idDegustateur;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
    public void associeTestDegustateur(int idTest, int idDegustateur){
        String sql = "INSERT IGNORE INTO participe (Degustateur_idDegustateur,Test_idTest)"
                + " VALUES ("+idDegustateur+","+idTest+")";
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
    
    public void supprimerDegustateur(int idDegustateur){
        //Supression du lien entre Echantillon et Test
        String sqlParticipe = "DELETE FROM participe"
                + " WHERE Degustateur_idDegustateur="+idDegustateur;
        
        //Envoi et exécution de la requête
        ResultSet resSetT = connexion.envoiRequete(sqlParticipe);

        //Suppression de l'échantillon dans la table Echantillon
        String sqlDegustateur = "DELETE FROM degustateur WHERE idDegustateur="+idDegustateur;
        
        //Envoi et exécution de la requête
        ResultSet resSetE = connexion.envoiRequete(sqlDegustateur);
    }
    
    public int setLigneNouveauOrganisateur(String nom,String prenom,String adresse,String telephone){
        String sql = "INSERT INTO organisateur (Nom,Prenom,Adresse,Telephone)"
                + " VALUES ('"+nom+"','"+prenom+"','"+adresse+"','"+telephone+"')";
        String sql2 = "SELECT LAST_INSERT_ID()";

        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        ResultSet resSet2 = connexion.envoiRequete(sql2);

        try{
            resSet2.next();
            return resSet2.getInt(1);
        }catch(Exception e)
        {
            return 0;
        }
    }
    public void setLigneModifierOrganisateur(int currentID,String nom,String prenom,String adresse,String telephone){
        String sql = "UPDATE organisateur "
                + " SET Nom='"+nom+"',Prenom='"+prenom+"',Adresse='"+adresse+"',Telephone='"+telephone+"'"
                + " WHERE idOrganisateur="+currentID;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
    
    public void associeTestOrganisateur(int idTest, int idOrganisateur){
        String sql = "UPDATE test "
                + " SET Organisateur_idOrganisateur="+idOrganisateur
                + " WHERE idTest="+idTest;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
            
    public void supprimerOrganisateur(int idOrganisateur){
        //Supression du lien entre Organisateur et Test
        String sqlTest = "UPDATE test "
                + " SET Organisateur_idOrganisateur=NULL"
                + " WHERE Organisateur_idOrganisateur="+idOrganisateur;
        
        //Envoi et exécution de la requête
        ResultSet resSetT = connexion.envoiRequete(sqlTest);

        //Suppression de l'Organisateur dans la table Organisateur
        String sqlOrganisateur = "DELETE FROM Organisateur WHERE idOrganisateur="+idOrganisateur;
        
        //Envoi et exécution de la requête
        ResultSet resSetE = connexion.envoiRequete(sqlOrganisateur);
    }
    public void supprimerSalle(int idSalle){
        //Supression du lien entre Organisateur et Test
        String sqlTest = "UPDATE test "
                + " SET Salle_idSalle=NULL"
                + " WHERE Salle_idSalle="+idSalle;
        
        //Envoi et exécution de la requête
        ResultSet resSetT = connexion.envoiRequete(sqlTest);

        //Suppression de l'Organisateur dans la table Organisateur
        String sqlSalle = "DELETE FROM Salle WHERE idSalle="+idSalle;
        
        //Envoi et exécution de la requête
        ResultSet resSetE = connexion.envoiRequete(sqlSalle);
    }
    public int setLigneNouveauSalle(String numero,String batiment,String date,int capaciteDAccueil){
        String sql = "INSERT INTO salle (Numero,Batiment,DateOccupation,CapaciteAccueil)"
                + " VALUES ('"+numero+"','"+batiment+"','"+date+"',"+capaciteDAccueil+")";
        String sql2 = "SELECT LAST_INSERT_ID()";

        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
        ResultSet resSet2 = connexion.envoiRequete(sql2);

        try{
            resSet2.next();
            return resSet2.getInt(1);
        }catch(Exception e)
        {
            return 0;
        }
    }
    
    public void setLigneModifierSalle(int currentID,String numero,String batiment,String date,int capaciteDAccueil){
        String sql = "UPDATE salle"
                + " SET Numero='"+numero+"',Batiment='"+batiment+"',DateOccupation='"+date+"',CapaciteAccueil="+capaciteDAccueil
                + " WHERE idSalle="+currentID;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }

    public void associeTestSalle(int idTest, int idSalle){
        String sql = "UPDATE test "
                + " SET Salle_idSalle="+idSalle
                + " WHERE idTest="+idTest;
        
        //Envoi et exécution de la requête
        ResultSet resSet = connexion.envoiRequete(sql);
    }
}

    

// pas de réel programme au début et pas respecté
// points techniques importants POO traités en 1 semaine mais plus d'1 mois sur html
// cours très souvent repris sur internet, pas testés pas corrigés
// exercices rares, pas corrigés à l'oral, corrigés contiennent systématiquement des erreurs
// absence de fil conducteur, de pédagogie
// pas de présence en cours (derrière son pc en permanence)
// tentative de faire évoluer la pédagogie de lui en parler -> n'a rien entendu
// pas vraiment d'aide en cas de gros blocage
// rares informations et systématiquement erronnées
// pas de formation à l'utilisation d'outils pro, méthodologie de travail, pas de réel apprentissage sur les langages, pas de préparation correcte aux épreuves
// progrès réalisés essentiellement par les projets personnels 
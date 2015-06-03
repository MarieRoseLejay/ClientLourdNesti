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
    Connection cn = null;
    Statement st = null;
            
    public void connecte(){
        try {
            //Chargement du driver
            Class.forName("com.mysql.jdbc.Driver");
    
        //Connection à la base de données
        String url = "jdbc:mysql://localhost/evaluationingredient";
        String login = "root";
        String motdepasse = "";
        
        //Récupération de la connexion
        cn = DriverManager.getConnection(url, login, motdepasse);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }   

    public void deconnecte(){
        try{
            //fermeture de la connexion à la base
            cn.close();  
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public ResultSet envoiRequete(String texte){
        ResultSet resultat = null;
        
        try{
            //Création d'un statement pour passer une requête
            st = cn.createStatement();
            
            //execution de la requête
            st.executeQuery(texte);
            resultat = st.getResultSet();
        }
        catch(SQLException e){
            e.printStackTrace();
        } 
        return resultat;
    }
    
    public void finRequete(){
         try{
             st.close();
         }
         catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    /*public String getDonneeEchantillon(){
        //String sql = "SELECT 'donnee' FROM 'table' WHERE 'id' = 'valeur'";
        String sql = "SELECT nom FROM echantillon WHERE idEchantillon = 1";
        
        //Envoi et exécution de la requête
        ResultSet resSet = envoiRequete(sql);
        
        String retour = null;
        try{
            //récupération de la première ligne du tableau
            resSet.next();
            retour = resSet.getString("nom");
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return retour;
    }*/
    
    public ResultSet getListe(String table){
        String sql = "SELECT * FROM "+table+"";
        
        //Envoi et exécution de la requête
        ResultSet resSet = envoiRequete(sql);

        return resSet;
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
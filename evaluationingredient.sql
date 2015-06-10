-- phpMyAdmin SQL Dump
-- version 4.1.4
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Jeu 04 Juin 2015 à 15:15
-- Version du serveur :  5.6.15-log
-- Version de PHP :  5.5.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `evaluationingredient`
--

-- --------------------------------------------------------

--
-- Structure de la table `degustateur`
--

CREATE TABLE IF NOT EXISTS `degustateur` (
  `idDegustateur` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nom` varchar(45) NOT NULL,
  `Prenom` varchar(45) NOT NULL,
  `Adresse` varchar(100) NOT NULL,
  `Telephone` varchar(45) NOT NULL,
  PRIMARY KEY (`idDegustateur`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `degustateur`
--

INSERT INTO `degustateur` (`idDegustateur`, `Nom`, `Prenom`, `Adresse`, `Telephone`) VALUES
(1, 'Batigne', 'Romain', '5 rue du bleu mouton 34000 Montpellier', '04 26 15 48 89'),
(2, 'Langlois', 'Fabien', '9 rue des multipliants 34000 Montpellier', '04 12 45 78 89'),
(3, 'Dumuzois', 'Elyse', '21 rue  de Reims 34000 Montpellier', '04 05 14 25 36'),
(4, 'Hubert', 'Mathilde', '46 rue des Cerisiers 34000 Montpellier', '04 58 69 78 65'),
(5, 'Martin', 'Anne', '9 rue des platanes 34000 Montpellier', '04 33 65 89 78'),
(6, 'Dupont', 'Pierre', '13 rue de la brique 34000 Montpellier', '04 01 25 36 47');

-- --------------------------------------------------------

--
-- Structure de la table `echantillon`
--

CREATE TABLE IF NOT EXISTS `echantillon` (
  `idEchantillon` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nom` varchar(45) NOT NULL,
  `Marque` varchar(45) NOT NULL,
  `Quantite` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idEchantillon`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `echantillon`
--

INSERT INTO `echantillon` (`idEchantillon`, `Nom`, `Marque`, `Quantite`) VALUES
(1, 'ChocolatNoir', 'Cote d''Os', 10),
(2, 'Chocolat blanc', 'Mont bleu', 5),
(3, 'Café', 'Grand maman', 0),
(4, 'thé vert', 'Détonne', 0),
(5, 'glace à la fraise', 'Magnone', 0),
(6, 'Biscuit', 'LUI', 0);

-- --------------------------------------------------------

--
-- Structure de la table `note`
--

CREATE TABLE IF NOT EXISTS `note` (
  `Echantillon_idEchantillon` int(10) unsigned NOT NULL,
  `Degustateur_idDegustateur` int(10) unsigned NOT NULL,
  `Aspect` int(10) unsigned NOT NULL,
  `Gout` int(10) unsigned NOT NULL,
  `Odeur` int(10) unsigned NOT NULL,
  `Texture` int(10) unsigned NOT NULL,
  `Commentaire` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`Echantillon_idEchantillon`,`Degustateur_idDegustateur`),
  KEY `fk_Echantillon_has_Degustateur_Degustateur1_idx` (`Degustateur_idDegustateur`),
  KEY `fk_Echantillon_has_Degustateur_Echantillon1_idx` (`Echantillon_idEchantillon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `note`
--

INSERT INTO `note` (`Echantillon_idEchantillon`, `Degustateur_idDegustateur`, `Aspect`, `Gout`, `Odeur`, `Texture`, `Commentaire`) VALUES
(1, 2, 5, 2, 3, 4, 'trop dure à croquer');

-- --------------------------------------------------------

--
-- Structure de la table `organisateur`
--

CREATE TABLE IF NOT EXISTS `organisateur` (
  `idOrganisateur` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nom` varchar(45) NOT NULL,
  `Prenom` varchar(45) NOT NULL,
  `Adresse` varchar(100) NOT NULL,
  `Telephone` varchar(45) NOT NULL,
  PRIMARY KEY (`idOrganisateur`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `organisateur`
--

INSERT INTO `organisateur` (`idOrganisateur`, `Nom`, `Prenom`, `Adresse`, `Telephone`) VALUES
(1, 'Chatton', 'Marina', '13 rue des chats bossus 59000 Montpellier', '04 23 56 89 78'),
(2, 'Dupond', 'Jean', '21 rue de la garenne 34000 Montpellier', '04 01 36 14 69'),
(3, 'Durost', 'Martine', '52 rue des trois épées 34000 Montpellier', '04 78 89 52 63'),
(4, 'Delmas', 'Sophie', '25 boulevard du temps qui passe 34000 Montpellier', '04 89 56 12 45'),
(5, 'DeFrance', 'Marianne', '21 avenue de l''eau qui dort 34000 Montpellier', '04 32 98 78 54'),
(6, 'Dujardin', 'André', '14 rue de la pierre sèche 34000 Montpellier', '04 85 52 36 96');

-- --------------------------------------------------------

--
-- Structure de la table `participe`
--

CREATE TABLE IF NOT EXISTS `participe` (
  `Degustateur_idDegustateur` int(10) unsigned NOT NULL,
  `Test_idTest` int(10) unsigned NOT NULL,
  PRIMARY KEY (`Degustateur_idDegustateur`,`Test_idTest`),
  KEY `fk_Degustateur_has_Test_Test1_idx` (`Test_idTest`),
  KEY `fk_Degustateur_has_Test_Degustateur1_idx` (`Degustateur_idDegustateur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `participe`
--

INSERT INTO `participe` (`Degustateur_idDegustateur`, `Test_idTest`) VALUES
(1, 1),
(2, 1);

-- --------------------------------------------------------

--
-- Structure de la table `salle`
--

CREATE TABLE IF NOT EXISTS `salle` (
  `idSalle` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Numero` varchar(45) NOT NULL,
  `Batiment` varchar(45) NOT NULL,
  `DateOccupation` date NOT NULL,
  `CapaciteAccueil` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idSalle`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `salle`
--

INSERT INTO `salle` (`idSalle`, `Numero`, `Batiment`, `DateOccupation`, `CapaciteAccueil`) VALUES
(1, '42', 'Pomme', '2015-06-10', 5),
(2, '404', 'Raisin', '2015-05-18', 10),
(3, '500', 'Fraise', '2015-06-01', 8),
(4, '403', 'Groseille', '2015-06-16', 6),
(5, '31', 'Fraise', '2015-06-19', 10),
(6, '13', 'Pomme', '2015-06-27', 6);

-- --------------------------------------------------------

--
-- Structure de la table `test`
--

CREATE TABLE IF NOT EXISTS `test` (
  `idTest` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nom` varchar(45) NOT NULL,
  `DateTest` date NOT NULL,
  `Echantillon_idEchantillon` int(10) unsigned NOT NULL,
  `Salle_idSalle` int(10) unsigned NOT NULL,
  `Organisateur_idOrganisateur` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idTest`),
  KEY `fk_Test_Echantillon1_idx` (`Echantillon_idEchantillon`),
  KEY `fk_Test_Salle1_idx` (`Salle_idSalle`),
  KEY `fk_Test_Preparateur1_idx` (`Organisateur_idOrganisateur`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `test`
--

INSERT INTO `test` (`idTest`, `Nom`, `DateTest`, `Echantillon_idEchantillon`, `Salle_idSalle`, `Organisateur_idOrganisateur`) VALUES
(1, 'TestChocolatNoir', '2015-05-12', 1, 1, 1);

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `note`
--
ALTER TABLE `note`
  ADD CONSTRAINT `fk_Echantillon_has_Degustateur_Degustateur1` FOREIGN KEY (`Degustateur_idDegustateur`) REFERENCES `degustateur` (`idDegustateur`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Echantillon_has_Degustateur_Echantillon1` FOREIGN KEY (`Echantillon_idEchantillon`) REFERENCES `echantillon` (`idEchantillon`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `participe`
--
ALTER TABLE `participe`
  ADD CONSTRAINT `fk_Degustateur_has_Test_Degustateur1` FOREIGN KEY (`Degustateur_idDegustateur`) REFERENCES `degustateur` (`idDegustateur`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Degustateur_has_Test_Test1` FOREIGN KEY (`Test_idTest`) REFERENCES `test` (`idTest`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `test`
--
ALTER TABLE `test`
  ADD CONSTRAINT `fk_Test_Echantillon1` FOREIGN KEY (`Echantillon_idEchantillon`) REFERENCES `echantillon` (`idEchantillon`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Test_Preparateur1` FOREIGN KEY (`Organisateur_idOrganisateur`) REFERENCES `organisateur` (`idOrganisateur`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Test_Salle1` FOREIGN KEY (`Salle_idSalle`) REFERENCES `salle` (`idSalle`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

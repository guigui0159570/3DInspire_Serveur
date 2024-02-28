Feature: Créer un compte
  En tant qu'utilisateur, je souhaite pouvoir créer un compte sur l'application.

  Scenario Outline: L'utilisateur arrive à s'inscrire
    Given L'utilisateur se connecte sur la page d'inscription
    And L'utilisateur entre son email "<email>", son pseudo "<pseudo>" et un mot de passe "<password>"
    When L'utilisateur envoie ses informations
    Then L'utilisateur est inscrit

    Examples:
    | email | pseudo | password |
    | nicodupont@gmail.com | nicodupont | nicoDupont59@ |
    | mariedupont@hotmail.fr | mariedupont | maridup59@abc |

  Scenario: L'utilisateur n'arrive pas à s'inscrire car son email est déjà utilisé
    Given Un utilisateur "nico59" est inscrit avec l'email "nicodupont@gmail.com"
    And L'utilisateur se connecte sur la page d'inscription
    And L'utilisateur entre son email "nicodupont@gmail.com", son pseudo "nicodupont" et un mot de passe "nicoDupont59@"
    When L'utilisateur envoie ses informations
    Then L'utilisateur n'est pas inscrit

  Scenario: L'utilisateur n'arrive pas à s'inscrire car son email n'est pas valide
    Given L'utilisateur se connecte sur la page d'inscription
    And L'utilisateur entre son email "nicodupontgmail.com", son pseudo "nicodupont" et un mot de passe "nicoDupont59@"
    When L'utilisateur envoie ses informations
    Then L'utilisateur n'est pas inscrit
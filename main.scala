// importation des modules/packages
import scala.io.StdIn._
import math._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.io.{File, PrintWriter}





object Main{

   var comptesclients = ArrayBuffer[Compte]()
   var compte = new Compte()
   var id = 0
   var etat = true
   var code_pin = ""

   class Compte( pinclient : String = "INTRO1234", 
                 soldeclient : Double = 1200.0) {

      var pin = pinclient
      var solde = soldeclient

   // Opération de dépôt
   def depot() : Unit = {
      var montant_depot : Int = 0 
      var devise = 0
      var nom_devise = ""

      devise = readLine("\nIndiquez la devise du dépôt : 1) CHF ; 2) EUR > ").toInt
      do{ 
         montant_depot = readLine("Indiquez le montant du dépôt > ").toInt

         if((montant_depot % 10 != 0) || (montant_depot < 10)) println("Le montant doit être un multiple de 10.")
      }while((montant_depot % 10 != 0) || (montant_depot < 10))
      // tant que le montant de dépôt n'est pas un multiple de 10, on redemande à l'utilisateur de rentrer à nouveau un montant

      if(devise == 2){
         nom_devise = "EUR"
         // prise en compte du taux de change si l'utilisateur a fait un dépôt en EUR
         //montant_disponible += (montant_depot * 0.95)
         solde += (montant_depot * 0.95)
      }
      else{
         nom_devise = "CHF"
         //montant_disponible += montant_depot
         solde += (montant_depot)
      }
      printf("\nVotre dépôt a été pris en compte, le nouveau montant disponible sur votre compte est de : %.2f CHF\n\n",solde)

      enregistrement(comptesclients)

   }




   // Opération de retrait      
   def retrait() : Unit = {
      var montant_retrait : Int = 0
      var billet = 0
      var choix_coupure = 0
      var montant_restant : Int = 0
      var nb_billets = 0
      var choix = " "
      var choix_int = 0
      var nb_billets_500 = 0
      var nb_billets_200 = 0
      var nb_billets_100 = 0
      var nb_billets_50 = 0
      var nb_billets_20 = 0
      var nb_billets_10 = 0
      var montant_rendu = 0
      var devise = 0
      var nom_devise = ""

      var montant_retrait_autorise = 0.1*solde 
      do{
         devise = readLine("\nIndiquez la devise: 1) CHF ; 2) EUR > ").toInt
      }while((devise!=1) && (devise!=2))
      //tant que l'utilisateur choisit un nombre différent de 1(CHF) ou 2(EUR), on lui redemande de rentrer à nouveau le numéro de devise
      if(devise == 2){
         nom_devise = "EUR"
         // avec le taux de change, l'utilisateur peut retirer plus d'EUR que de CHF
         montant_retrait_autorise *= 1.05
      }
      else nom_devise = "CHF"
      do{
         montant_retrait = readLine("Indiquez le montant du retrait > ").toInt
         if((montant_retrait%10 != 0) || (montant_retrait < 10)) println("Le montant doit être un multiple de 10.")
         else if(montant_retrait > montant_retrait_autorise){
            printf("Votre plafond de retrait autorisé est de: %.2f ", montant_retrait_autorise)
            print(nom_devise + "\n")
         } 

      }while((montant_retrait%10 != 0) || (montant_retrait > montant_retrait_autorise) || (montant_retrait < 10)) // tant que le montant de retrait n'est pas un multiple de 10 ou qu'il est supérieur au montant de retrait autorisé, on redemande à l'utilisateur de rentrer un nouveau un montant


      //si le montant de retrait est supérieur ou égal à 200 et que la devise est en CHF, alors l'utilisateur peut choisir entre les grandes et petites coupures    
      if((montant_retrait >= 200) && (devise == 1)){
         do{
         choix_coupure = readLine("En 1) grosses coupures, 2) petites coupures > ").toInt
         }while((choix_coupure != 1) && (choix_coupure != 2))
      } //tant que l'utilisateur choisit un nombre différent de 1(grosses coupures) ou 2(petites coupures), on lui redemande de rentrer à nouveau la taille des coupures

      // sinon le montant se fera forcément en petites coupures et cela ne sera pas demandé à l'utilisateur
      else choix_coupure = 2

      // le montant qu'il reste à retirer est égal au montant de retrait au début
      montant_restant = montant_retrait 

      // si les coupures sont grandes, on commence par les billets de 500
      if(choix_coupure == 1) billet = 500

      // si les coupures sont petites, on commence par les billets de 100
      else if(choix_coupure == 2) billet = 100
      while (billet != 10){

         //si la division du montant restant à retirer par le billet donne un résultat plus grand ou égal à 1, alors on prend ce résultat et on l'arrondit à l'entier inférieur
         if((montant_restant / billet) >= 1){
            nb_billets = (montant_restant / billet).toInt
            println(f"\nIl reste $montant_restant $nom_devise à distribuer ")
            println(f"Vous pouvez obtenir au maximum $nb_billets billet(s) de $billet $nom_devise")
            // l'utilisateur peut choisir de retirer le nombre de billets proposé par l'algorithme en tapant 'o' ou bien choisir un nombre de billets inférieur à celui proposé
            choix = readLine("Tapez o pour ok ou une autre valeur inférieure à celle proposée > ")

            if(choix == "o"){
               montant_rendu = (billet * nb_billets)
               montant_restant -= montant_rendu
            } 
            else{ 
               // Si l'utilisateur n'a pas rentré 'o', alors par hypothèse, il a forcément rentré une valeur inférieure au nombre de billets proposés sous forme de String. On convertit la valeur en Int pour pouvoir faire des calculs
               choix_int = choix.toInt
               if(choix_int < nb_billets){
                  nb_billets = choix_int
                  montant_rendu = (billet * nb_billets)
                  montant_restant -= montant_rendu
               }
            }
         }
         // On utilise des différentes variables qui stockent la valeur de la variable nb_billets à différents moments
         if (billet == 500) {
            if (nb_billets * billet == montant_rendu) nb_billets_500 += nb_billets
            billet = 200
         }   
         else if (billet == 200){
            if (nb_billets * billet == montant_rendu) nb_billets_200 += nb_billets
            billet = 100
         } 
         else if(billet == 100){
            if (nb_billets * billet == montant_rendu) nb_billets_100 += nb_billets
            billet = 50
         } 
         else if(billet == 50){
            if (nb_billets * billet == montant_rendu) nb_billets_50 += nb_billets
            billet = 20
         } 
         else if (billet == 20){
            if (nb_billets * billet == montant_rendu) nb_billets_20 += nb_billets
            billet = 10

         }

      }
      // il n' y pas de billets plus petits que 10, donc on suppose que si l'utilisateur a refusé les possibilités avec les autres billets qui lui étaient disponibles, c'est que c'est qu'il voulait avoir des billets de 10
      if((billet == 10) && (montant_restant >= 10)) {
         nb_billets_10 = (montant_restant / billet).toInt
         montant_restant -= (billet * nb_billets)
      }
      println("\nVeuillez retirer la somme demandée :")
      // Pour chaque type de billet, si sa quantité à retirer est plus grande que 0, alors un message affiche le nombre de billets à retirer
      if (nb_billets_500 > 0) println(f"$nb_billets_500 billet(s) de 500 $nom_devise")
      if (nb_billets_200 > 0) println(f"$nb_billets_200 billet(s) de 200 $nom_devise")
      if (nb_billets_100 > 0) println(f"$nb_billets_100 billet(s) de 100 $nom_devise")
      if (nb_billets_50 > 0) println(f"$nb_billets_50 billet(s) de 50 $nom_devise")
      if (nb_billets_20 > 0) println(f"$nb_billets_20 billet(s) de 20 $nom_devise")
      if (nb_billets_10 > 0) println(f"$nb_billets_10 billet(s) de 10 $nom_devise")

      // prise en compte du taux de change si l'utilisateur a fait un retrait en EUR
      if(devise == 2) solde -= (montant_retrait * 0.95) //montant_disponible -= (montant_retrait * 0.95)
      else solde -= montant_retrait //montant_disponible -= montant_retrait

      printf("Votre retrait a été pris en compte, le nouveau montant disponible sur votre compte est de : %.2f CHF\n\n", solde)

      enregistrement(comptesclients)
   }






   // Opération de changement de code pin
   def changepin() : Unit = {
      // On demande le nouveau code pin
      do{
         pin = readLine("Saisissez votre nouveau code pin (il doit contenir au moins 8 caractères) > ")
         if (pin.length < 8) println("Votre code pin ne contient pas au moins 8 caractères")
      }while(pin.length < 8)
      enregistrement(comptesclients)
   }


   def consultation() : Unit = {
      printf("\nLe montant disponible sur votre compte est de : %.2f CHF\n\n", solde)
   }   

   def mdp() : Unit = { 
      code_pin = pin
   }

}



   def lecture() : Unit = {
      try{
         val fr = Source.fromFile("clients.csv")
         val lignefr = fr.reset().getLines()

         while(!lignefr.isEmpty){
            var ligne = lignefr.next()
            var comptes = ligne.split(",")
            compte = new Compte(comptes(0).toString, comptes(1).toDouble)
            comptesclients += compte
            }
            fr.close
      }
      catch{
         case ex : java.io.FileNotFoundException => println("Chargement des données des comptes clients impossible. Bancomat temporairement hors service.")
         etat = false
      }   
   }


   def enregistrement(clients: ArrayBuffer[Compte]) : Unit = {
      val pw = new PrintWriter("clients.csv")
      clients.foreach(compte => pw.println(s"${compte.pin},${compte.solde}"))
      pw.close()
   }





   def main(args: Array[String]): Unit = {

      lecture()

      var operation = 5 
      var tentative = 0
      var code_entered = ""

      // boucle principale qui va faire tourner le programme en boucle 
      while(etat == true){

        tentative = 3

        if(operation ==5){
           id = readLine("\nSaisissez votre code identifiant > ").toInt
           if((id >= comptesclients.length) || (id<0)){
              println("Cet identifiant n’est pas valable.")
              etat = false
           }    


            if(etat == true){

               comptesclients(id).mdp()

                  // 3 essais pour rentrer le bon code pin
                  do{
                     if(tentative != 3) println(f"\nCode pin erroné, il vous reste $tentative tentatives >")
                     code_entered = readLine("Saisissez votre code pin > ") 
                     // à chaque fois que l'utilisateur rentre un code, on lui retire une tentative en moins


                     if(code_pin != code_entered) tentative -= 1

                     //s'il reste 0 tentatives, on demande à nouveau le id
                     if(tentative == 0){
                        println("Trop d’erreurs, abandon de l’identification \n")
                     }
                 }while((code_pin != code_entered) && (tentative > 0))
            }
      }         
            if(etat == true){
               if(tentative != 0){

                  // le "\n" permet de passer à la ligne. Il est équivalent à println("")
                  println("Choisissez votre opération : \n   1) Dépôt \n   2) Retrait \n   3) Consultation du compte \n   4) Changement du code pin \n   5) Quitter")
                  operation = readLine("Votre choix : ").toInt
            }

               if(operation == 1) comptesclients(id).depot()

               if(operation == 2) comptesclients(id).retrait()

               if(operation == 3) comptesclients(id).consultation()

               if(operation == 4) comptesclients(id).changepin()

               if((operation == 5) && (tentative != 0)) println("Fin des opérations, n’oubliez pas de récupérer votre carte. \n")
            }   
      }
   }
}



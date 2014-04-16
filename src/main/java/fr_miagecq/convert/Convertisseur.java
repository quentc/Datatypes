package fr_miagecq.convert;

import java.math.BigDecimal;
import java.util.Scanner;

//Principal class of the Convertisseur
public class Convertisseur {
       static Scanner scanner = new Scanner(System.in);
       static Conversion uneConversion = new Conversion();
	//main of the program
	   public static void main (String[] args){	
	       menuprincipal();
	   }
           //main menu
	   public static void menuprincipal(){
	       int opt = 0;
           //Test to go out of the menu only if the chosen user a valid option
           while(opt !=5)
           {
        	System.out.println("*********************************************"); 
	        System.out.println("Choisissez une option :\n");
	        System.out.println("1) Visualiser les conversions");
	        System.out.println("2) Effectuer une conversion");
	        System.out.println("3) Ajouter une conversion");
	        System.out.println("4) Modifier coefficient");
	        System.out.println("5) Quitter");
	        System.out.println("*********************************************");
	        opt = scanner.nextInt();
                       
           
           //According to the chosen obtion call the corresponding method
            switch (opt)
            {
            	case 1 : uneConversion.afficherConversion();
            	break;
                case 2 : uneConversion.effectuerConversion();
                break;
                case 3 : uneConversion.ajouterConversion();
                break;
                case 4 : uneConversion.coefficientupdate();
                break;
                
                
            }         
	        }

           }
	  
	   }       
	  
	        
		    
		  

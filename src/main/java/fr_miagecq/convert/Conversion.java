package fr_miagecq.convert;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

//Enumeration containing the various possible types of conversion
enum TYPECONVERSION{Base, Avancee, Devise}

public class Conversion {
    //Definition of the global variables
    static Scanner scanner = new Scanner(System.in);
	public double coefficient;
	public TYPECONVERSION typeConversion;
	String etalon = "USD";
        //Localization of the files of conversion
	static String ficDevises = "src/conversionDevises.properties";
	static String ficBase = "src/conversionBase.properties";
	static String ficAvance="src/conversionAvancee.properties";
        //Board containing the standards
	String [] tabEtalon = {"g","m","l","degre","USD"};
	
	   //*************************************************************************
           //               Function of configuration for a conversion
           // The user indicates his parameters to make a conversion
           //*************************************************************************
	      public static void effectuerConversion()
          {
              String uniteEntree, uniteSortie;
              String nomfic= new String();
              String valeur;
              int typeMesure;
              int parcourstab = 1;
              int compteur =0;
              TYPEESPECES[] tabTypeEspece = TYPEESPECES.values();
              String [] tabTypesM= new String [tabTypeEspece.length];
              
              //Choice of a measure in those available in the enumeration TYPEESPECES
              System.out.println("Choisissez une mesure :");
              for (TYPEESPECES unTypeM : TYPEESPECES.values())
              {
                  // Display of the various types of possible conversions
                  System.out.println(parcourstab + ") " + unTypeM);
                  tabTypesM[compteur] = unTypeM.toString();    
                  parcourstab++;
                  compteur++;
              }
              
              typeMesure = scanner.nextInt();
             //Creation of a new object
              Conversion uneConversion = new Conversion();
              //According to the choice of type of utlisateur, indicates the file where are present the data
                if(typeMesure == 5)
                  {
                	nomfic=ficAvance;
                }
                else if(typeMesure == 6)
                {
                	   nomfic = ficDevises;
                }	
                else {
                    nomfic = ficBase;
                }
                 
                int typeMesurebis =typeMesure-1;
                String type = tabTypesM[typeMesurebis];
                // Function of recovery of the possible conversions for the type chosen 
                String liste =lecturerecuplisteconversion(nomfic,type);
                
                //test to ask user which entry unity he wants
                if (nomfic.equals(ficBase) || nomfic.equals(ficAvance)){
                	 System.out.println("Unité d'entrée ? (Unites possibles : " + liste +")");
                }
                else if (nomfic.equals(ficDevises)) {
                	System.out.println("Devise d'entrée ? (Devises possibles : " + liste +")");
                }
               
                
                 uniteEntree = scanner.nextLine();
                 uniteEntree = scanner.nextLine();
                //while the user type an incorrect entry unity, ask him to retype it
                while (!controle(uniteEntree,liste,nomfic)){
                	System.out.println("Entree invalide , veuillez recommencer");
                	  if (nomfic.equals(ficBase) || nomfic.equals(ficAvance)){
                     	 System.out.println("Unité d'entrée ? (Unites possibles : " + liste +")");
                     }
                     else if (nomfic.equals(ficDevises)) {
                     	System.out.println("Devise d'entrée ? (Devises possibles : " + liste +")");
                     }
                    uniteEntree = scanner.nextLine();

                	
                }
                      //test to ask user which out unity he wants
                if (nomfic.equals(ficBase) || nomfic.equals(ficAvance)){
               	 System.out.println("Unité de sortie ? (Unites possibles : " + liste +")");
               }
               else if (nomfic.equals(ficDevises)) {
               	System.out.println("Devise de sortie ? (Unites possibles : " + liste +")");
               }
           
                 //while the user type an incorrect out unity, ask him to retype it
                uniteSortie = scanner.nextLine();
                while (!controle(uniteSortie,liste,nomfic)){
                	System.out.println("Sortie invalide , veuillez recommencer");
                	 if (nomfic.equals(ficBase) || nomfic.equals(ficAvance)){
                       	 System.out.println("Unité de sortie ? (Unites possibles : " + liste +")");
                       }
                       else if (nomfic.equals(ficDevises)) {
                       	System.out.println("Devise de sortie? ? (Unites possibles : " + liste +")");
                       }
                   
                    uniteSortie = scanner.nextLine();

                	
                }
                
                if (nomfic.equals(ficAvance)){
                	if(!controleAvanceeFinal(uniteEntree,uniteSortie,liste,nomfic)){
                		System.out.println("Erreur de saisie");
                		effectuerConversion();
                	};
                }
         //ask to the user the value to convert
    	 System.out.println("Valeur à convertir ?");
         valeur = scanner.nextLine();
  
         //use the convertions functions, conversionBase or ConversionAvancee with good parameters
         if(nomfic.equals(ficDevises) || nomfic.equals(ficBase))
         {
        	 
            double valeurFinale = uneConversion.conversionBase(uniteEntree, uniteSortie, valeur, nomfic, typeMesure);
             System.out.println("Valeur finale : " + valeurFinale); 
         }
        
         else
         {
              Object valeurFinale = uneConversion.conversionAvancee(uniteEntree, uniteSortie, valeur);
              System.out.println("Valeur finale : " + valeurFinale.toString()); 
             }
        }
     
              
          //*************************************************************************
          //               Function of controle for basic conversion
          // Try to find the entry/out unity the user typed in the list of basic unity of the type he chosed
          //*************************************************************************      
          
          public static boolean controle(String unite,String liste,String nomfic)
          {
        	  
        	  String tabunite[]=liste.split(",");
        	  int compteur = 0;
        	  
        	  boolean trouve = false;

        	  while (compteur <tabunite.length)
        	  {
        	     String inter = tabunite[compteur];
        		  if (nomfic!=ficAvance)
        		  {
        			  //fichiers de bases
                             
        			  if (inter.equals(unite))
        			  	{
        				  trouve = true;
        			  	}
        		  }
        		  else 
        		  {       int postiret = inter.indexOf("-");
                                  int postirerbis = inter.indexOf("-")+1;
                                  String chainetiret = inter.substring(0, postiret);
                                  String chainetiretbis = inter.substring(postirerbis, inter.length());
                                  
        			  if(chainetiret.equals(unite) || chainetiretbis.equals(unite))
        			  {
        				  trouve = true;
        			  }
        		  }
        		 
        		  compteur++;
        	
        	  
        	  }
			return trouve;
          }
          
           //*************************************************************************
           //               Function of controle for Advanced Conversion
           // Try to find the entry/out unity the user typed in the list of advanced unity of the type he chosed
           //*************************************************************************
          public static boolean controleAvanceeFinal(String uniteE,String uniteS,String liste,String nomfic){
        	  String tabunite[]=liste.split(",");
        	  int compteur = 0;
        	  
        	  boolean trouve = false;
        			  
        	  while (compteur <tabunite.length)
        	  {
        	
        		  if (nomfic.equals(ficAvance))
        		  {
        			  //fichier Avancé
                                  String inter = tabunite[compteur];
                                  String chaineunite = uniteE+"-"+uniteS;
        			  if (inter.equals(chaineunite))
        			  	{
        				  trouve = true;
        			  	}
        		  }
        		  compteur++;
        	  }
        	  
          return trouve;
          }
	  
           //*************************************************************************
           //               Add Conversion to the file of basic,currency or advanced conversion
           //
           //*************************************************************************
          public static void ajouterConversion()
          {
             TYPEESPECES []tabValue = TYPEESPECES.values();
             String [] tabTypes= new String [tabValue.length];
             int compteur = 1;
             int typeMesure=0;
             //the user chose the type of conversion he want to add
             System.out.println("Choisissez un type de conversion :");
              for (TYPECONVERSION unType : TYPECONVERSION.values())
              {
                  System.out.println(compteur + ") " + unType);
                  compteur++;
              }
              int typeConversion = scanner.nextInt();
              if(typeConversion == 1)
              {
                  //save the type of the unity that the user wants to add
                  int parcourstab = 1;
                  System.out.println("Conversion pour quelle type de mesure ?");
                  for (TYPEESPECES unType : TYPEESPECES.values())
                  {
                      if(unType.toString() != "Devises")
                      {
                      System.out.println(parcourstab + ") " + unType);
                      tabTypes[parcourstab] = unType.toString();                      
                      parcourstab++;
                      }
                  }
                  typeMesure = scanner.nextInt();                   
              }    
              String mesure = tabTypes[typeMesure];
              switch (typeConversion)
              {
              //call function to add the new unity to the files
              case 1 : ajouterConversionBase(mesure); break;
              case 2 : ajouterConversionAvancee(); break;
              case 3 : ajouterConversionDevise(); break;
                  
              }                
          }
          
          //*************************************************************************
          //           Basic Conversion
          //Parameters :
          //uniteE : entry unity ; UniteS : out unity ; valeur : value to convert
          //nomfic : file where the data are ; choix : choice of type by the user
          //*************************************************************************
          public double conversionBase (String uniteE, String uniteS,String valeur, String nomfic, int choix)
           {
        
             double coefficientEtalon;
             double coefficientEntree;
             double coefficientSortie;
             Properties properties = lecture(nomfic);
             double valeurFinale;
             double value ;
             String valEtalon ; 
             String etalon;
             int indice;
             
             //find the right standard of the unity
             if(choix==6){
                 indice = choix-1;
                 valEtalon = tabEtalon[indice];
            	 etalon=valEtalon;
             }
             else {
                 indice=choix-2;
                 valEtalon = tabEtalon[indice];
                 etalon=valEtalon;}
             
             
             String entree=properties.getProperty(uniteE);
             String sortie = properties.getProperty(uniteS);
             
             String unepropEtalon = properties.getProperty(etalon);
             String unepropEntree= properties.getProperty(etalon);
             String unepropSortie = properties.getProperty(etalon);
             if (choix!=6){
                   int choixtab = choix-1;
                   String choixetalon= tabEtalon[choixtab];
                   String propEtalon = properties.getProperty(choixetalon);
                   String propEntree = properties.getProperty(uniteE);
                   String propSortie= properties.getProperty(uniteS);
                   int posespace =propEtalon.indexOf(" ");
                   int posespaceE = propEntree.indexOf(" ");
                   int posespaceS = propSortie.indexOf(" ");
                   //recup all the coefficient in the file for the calcul
                   coefficientEtalon = Double.parseDouble(propEtalon.substring(0,posespace)); 
                   coefficientEntree = Double.parseDouble(propEntree.substring(0,posespaceE));                 
                   coefficientSortie = Double.parseDouble(propSortie.substring(0,posespaceS));
                 
             }
                 else{
              coefficientEtalon = Double.parseDouble(unepropEtalon);
              coefficientEntree = Double.parseDouble(unepropEntree);                 
              coefficientSortie = Double.parseDouble(unepropSortie); 
                 }
             //replace the , typed by the user by a .
             if(valeur.indexOf(",") > 0){
            	 valeur=valeur.replace(",", ".");
             }
             
             value= Double.parseDouble(valeur);
             
             //final calcul to find the converted value in the right unity
             valeurFinale = value * (coefficientEtalon/coefficientEntree)*coefficientSortie; 
             
             return (valeurFinale);
           }
          //*************************************************************************
          //               AdvancedConversion
          //Parameters :
          //UniteE : entry unity ; uniteS : out unity ; valeur : value to convert
          //*************************************************************************
             public Object conversionAvancee (String uniteE, String uniteS, String valeur)
              {
                 Properties properties = lecture("src/conversionAvancee.properties");
                 	  Double value = Double.parseDouble(valeur);
                      Object res = null;
                      ScriptEngineManager mgr = new ScriptEngineManager();
                      ScriptEngine engine = mgr.getEngineByName("JavaScript");
                      //recup the formula in the file, with the good uniteE and uniteS 
                      String formule = properties.getProperty(uniteE + "-" + uniteS);
                      String valeur2 = String.valueOf(value);
                     //replace the X in the formula by the value that the user wants to convert
                      String foo = formule.replace("X", valeur2);
                      
                      try {
                          res = engine.eval(foo);
                      } catch (ScriptException e) {
                          // TODO Auto-generated catch block
                          e.printStackTrace();
                      }
                  return res;
              }   
             
             
             //*************************************************************************
             // 		Function of Coefficientupdate
             //*************************************************************************
                
            public static void coefficientupdate()
            {	
            	Scanner scanner2 = new Scanner(System.in);
            	String nameproper="";
            	int typeMesure=0;
            	int boucletab=1,secboucletab = 0;
                
                TYPEESPECES []tabValue = TYPEESPECES.values();
            	String [] tabTypesM= new String [tabValue.length];
                String nomfic= "";
                //the user chose the type of the unity
                System.out.println("Choisissez une mesure :");
                for (TYPEESPECES unTypeM : TYPEESPECES.values())
                {
                    String untype = unTypeM.toString();
                	if (!untype.equals("Temperature")){
                    System.out.println(boucletab + ") " + unTypeM);
                    tabTypesM[secboucletab] = unTypeM.toString();    
                    boucletab++;
                	}
                   
                    secboucletab++;
                }
               
                
                typeMesure = scanner2.nextInt();

                 
                  if(typeMesure == 5)
                  {
                  	   nomfic = ficDevises;
                  }	
                  else {
                      nomfic = ficBase;
                  }
                   
                  int majTypeMesure = typeMesure-1;
                  String type = tabTypesM[majTypeMesure];
                  //while the user type an incorrect entry unity, ask him to retype it
                  String liste =lecturerecuplisteconversion(nomfic,type);
                  if (nomfic.equals(ficBase) || nomfic.equals(ficAvance)){
                 	 System.out.println("Unité d'entrée ? (Unites possibles : " + liste +")");
                 }
                 else if (nomfic.equals(ficDevises)) {
                 	System.out.println("Devise d'entrée ? (Devises possibles : " + liste +")");
                 }

                  nameproper = scanner.nextLine();
                  
                 while (!controle(nameproper,liste,nomfic)){
                 	System.out.println("Entree invalide , veuillez recommencer");
                 	  if (nomfic.equals(ficBase) || nomfic.equals(ficAvance)){
                      	 System.out.println("Unité d'entrée ? (Unites possibles : " + liste +")");
                      }
                      else if (nomfic.equals(ficDevises)) {
                      	System.out.println("Devise d'entrée ? (Devises possibles : " + liste +")");
                      }
                     nameproper = scanner.nextLine();

                 	
                 }
                 
           	 //function to update fhe property file
           	 updatefile(nomfic,nameproper);
           	 
            
            }
          //*************************************************************************
          // 			Function tu update the file
          //*************************************************************************
               
            public static void updatefile(String namefile,String nameproper)
            {
            Float newvalue;
            
        
            Properties properties = new Properties();
                
                try {
                	properties.load(new FileInputStream(namefile));
             
   			} catch (FileNotFoundException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			} catch (IOException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
                
                String properencours = properties.getProperty(nameproper);
                int posparenthese = properencours.indexOf("(");
                int longueur = properencours.length();
                System.out.println("Ancien coefficient : "+properencours.substring(0, posparenthese));
                //the user type the new coefficient for the property
                System.out.println("Nouveau coefficient pour l'unité :" + nameproper);
                FileOutputStream oStream = null;
                
                try{
                newvalue = scanner.nextFloat();
               
                double result;
                String finalvalue=newvalue.toString() + properencours.substring(posparenthese,longueur);
                properties.setProperty(nameproper, finalvalue);
                oStream = new FileOutputStream(new File(namefile));
                 //try the new coefficient with a basic formula, jsut to be sure he typed a number
            	result = newvalue * 2;
                	
                }
            	 catch (Exception e) {
            		System.out.println("A problem has been occured with the coefficient, please try again");
            	
            		coefficientupdate();
            	 }
                  try {
                    properties.store(oStream,"") ;
                } catch (IOException e) {
                	System.out.println("A problem has been occured with the coefficient, please try again");
               
                }
                
                
            }
          //*************************************************************************
          // 			Function to see all the possible conversions
          //*************************************************************************
             
         public static void afficherConversion()
         {
        	
        	 int compteur =0;
        	 boolean valid = false;
        	 String choix ="";
        	 String chainemetrique="";
        	 String chaineimperial="";
        	 String chaineautre="";
        	 String chaineencours="";
             Properties properties = new Properties();
        
             try {
				properties.load(new FileInputStream(ficBase));
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   
             Set<Object> tabproper = properties.keySet();
             Iterator<Object> boucleproperties = tabproper.iterator();
       
        	//the user can chose between 2 type, by System or by Type of unity
        	 System.out.println("Quel type d'affichage souhaitez vous?");
        	 System.out.println("1) Affichage par système (Métrique, Impérial, Autre)");
        	 System.out.println("2) Affichage par type de mesure");
        	 choix= scanner.nextLine();
        
        	 	if (choix.equals("1") || choix.equals("2"))
        	 	{
        	 		valid=true;
        	 	}
        
                 //if the user chosed by System
        	 if (choix.equals("1"))
        	 {
        		 
                 while (boucleproperties.hasNext())
                 {
                     String nextclef = (String)boucleproperties.next();
                     String valeur = properties.getProperty(nextclef);
              
               
                     valeur = valeur.substring(valeur.indexOf("-")+1, (valeur.indexOf(")")));
                                //on each property, extract the type of the System and build the string of each System
                     		if(valeur.equals("M")){
                     			chainemetrique= chainemetrique + "," +nextclef;

                     		}
                     		else if(valeur.equals("I")){
                     			chaineimperial= chaineimperial + "," +nextclef;
                     			}
                     		else if(valeur.equals("A")){
                     			chaineautre= chaineautre + "," +nextclef;
                     		}
                     		}
                 	chainemetrique=chainemetrique.substring(1,chainemetrique.length());
                 	chaineimperial=chaineimperial.substring(1,chaineimperial.length());
                 	chaineautre=chaineautre.substring(1,chaineautre.length());
                 	
                 	System.out.println("*********************************************");
                    System.out.println("Unités metriques :"+ chainemetrique);
                    System.out.println("Unités imperial :"+ chaineimperial);
                    System.out.println("Unités autre :"+ chaineautre);
        	 }
         //if the user chosed 2
         else if (choix.equals("2")){
        	 //loading types
        	 TYPEESPECES [] tabEspece = TYPEESPECES.values();
        	 String [] tabTypesM= new String [tabEspece.length];
             
                 for (TYPEESPECES unTypeM : TYPEESPECES.values())
             {
                 String unType = unTypeM.toString();
            	 if (!unType.equals("Temperature") && (!unType.equals("Devises"))){
            	 tabTypesM[compteur] = unTypeM.toString();   
                 compteur++;
            	 }
             }

        
                 compteur=0;
                 while (tabTypesM[compteur]!=null)
                 {		
               
                	   Properties propertries = new Properties();
                       
                       try {
                    	   propertries.load(new FileInputStream(ficBase));
          				
          			} catch (FileNotFoundException e) {
          				// TODO Auto-generated catch block
          				e.printStackTrace();
          			} catch (IOException e) {
          				// TODO Auto-generated catch block
          				e.printStackTrace();
          			}
             
                     Set<Object> tabproperbis = propertries.keySet();
                     Iterator<Object> secboucleproper = tabproperbis.iterator();
                	 
                	 chaineencours="";
                	 while (secboucleproper.hasNext())
                     {
                         String key = (String)secboucleproper.next();
                         String valeur = propertries.getProperty(key);
                       //value = type of the conversion

                		valeur = valeur.substring(valeur.indexOf("(")+1, (valeur.indexOf("-")));
                                String valeurTab = (String)tabTypesM[compteur];
                 		if(valeur.equals(valeurTab))
                 		{
                 			chaineencours= chaineencours + "," +key;
                 			

                 		}
                 		//return the current stirng
                     }
                         
                String valeurType = tabTypesM[compteur];
                int longchaine =chaineencours.length();
                System.out.println(valeurType+ " : " + chaineencours.substring(1,longchaine));
                compteur++;
                }
         	}	
         }
             
             
          //*************************************************************************
          //                    Function to add a new Basic conversion
          //*************************************************************************
          public static void ajouterConversionBase(String typeMesure)
          {
              Properties properties = lecture("src/conversionBase.properties");
              String systeme="";
              //the user type the name of the unity
              System.out.println("Nouvelle unité :");
              String nouvelleUnite= scanner.nextLine();
              nouvelleUnite= scanner.nextLine();
              //variable to check the system
              boolean estcorrect = false;
              //the user ype the system he wants
              while (estcorrect==false){
            	  System.out.println("Systèmé métrique, impérial ou autre? (Saisissez M, I ou A)");
            	  systeme= scanner.nextLine();
            	  if (systeme.equals("M") || systeme.equals("I") || systeme.equals("A"))
            	  {
            		  estcorrect=true;
            	  }
              }
              //the user type the coefficient of the unity, compared to the standard of the type
              System.out.println("Coefficient :");
              String coefficient= scanner.nextLine();
              
              //set the good property in the file
              properties.setProperty(nouvelleUnite,coefficient.toString() + " ("+typeMesure+"-"+ systeme+")") ;
              
              FileOutputStream oStream = null;
            try {
                oStream = new FileOutputStream(new File("src/conversionBase.properties"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
              try {
                properties.store(oStream,"") ;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
              
 
          }
           //*************************************************************************
          //                    Function to add a new currency
          //*************************************************************************
          public static void ajouterConversionDevise()
          {
              Properties properties = lecture("src/conversionDevises.properties");
              //the user type the new devise
              System.out.println("Nouvelle devise :");
              String nouvelleDevise= scanner.next();
              //fix the coefficient in compararison of the Dollar
              System.out.println("Taux par rapport au Dollar :");
              String taux=scanner.nextLine();
              taux=scanner.nextLine();
              //create the new property
              properties.setProperty(nouvelleDevise,taux.toString());
              
              FileOutputStream oStream = null;
            try {
                oStream = new FileOutputStream(new File("src/conversionDevises.properties"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
              try {
                properties.store(oStream,"") ;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
               
 
          }
          //*************************************************************************
          //                    Function to add a new Advanced conversion
          //*************************************************************************
          public static void ajouterConversionAvancee ()
          {
             Properties properties = lecture("src/conversionAvancee.properties");
             
          
                  ScriptEngineManager mgr = new ScriptEngineManager();
                  ScriptEngine engine = mgr.getEngineByName("JavaScript");
                  //The new entry unity
                  System.out.println("Entrez votre unité d'entrée pour la nouvelle conversion");
                  String nouvelleUniteE= scanner.next();
                  //The new out unity
                  System.out.println("Entrez votre valeur de sortie pour la nouvelle conversion");
                  String nouvelleUniteS= scanner.next();
                  
                  String conversion = properties.getProperty(nouvelleUniteE + "-" + nouvelleUniteS);
                 
                  String formule="0";
                  //build the formula for the conversion
                  while (!formule.contains("X") && !formule.equals("Q")) {
                      System.out.println("Entrez la formule de conversion");
                      System.out.println("Tapez X pour la future valeur à convertir à l'aide de la formule");
                      System.out.println("Tapez Q pour quitter");
                      formule= scanner.next();                 
                  }
                  //if the user chose to quit
                  if (formule.equals("Q")){
                         Convertisseur home = new Convertisseur();
                         home.menuprincipal();
                  }
                //build the new property
                 properties.setProperty(nouvelleUniteE + "-" + nouvelleUniteS,formule) ;
             
               

                  String res;
                  Object result ;
                 
                 
                  FileOutputStream oStream = null;
                try {
                    //test the formula typed by the user with a simple example
                    res = advancedconversiontest(formule);
                    result = engine.eval(res);
                    oStream = new FileOutputStream(new File("src/conversionAvancee.properties"));
                    properties.store(oStream,"") ;
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                	System.out.println("A problem has been occured, please try again");
                } catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("A problem has been occured, please try again");
				}
                catch (ScriptException e) {
                    // TODO Auto-generated catch block
                  	System.out.println("A problem has been occured, please try again");
                  	ajouterConversionAvancee();
                  }
               
              
                  
              
          }   
          //*************************************************************************
          //                    Function to test the formule the user types when he wants to add a new Advanced conversion
          //*************************************************************************
      
          public static String advancedconversiontest(String formula)
          {
        	  {
                   //test the formule : replace the X by 2 and return the string
                   String finalformula = formula.replace("X", "2");
                       
                       
                   return finalformula;
               }   
          }
           //*************************************************************************
          //                    Function to recup the list of the possible conversion for a type
          // Parameters : 
          // nomfic : name of the file ; choix : type of unity
          //*************************************************************************
          public static String lecturerecuplisteconversion(String nomfic,String choix)
          {
        	  String chaineconversion="";
 
              final String UNITES_FILE = nomfic;
              Properties properties = new Properties();

              try
              {
            	
                  properties.load(new FileInputStream(UNITES_FILE));
                  Set<Object> tabProper =  properties.keySet();
                  Iterator<Object> boucleproperties = tabProper.iterator();
                  while (boucleproperties.hasNext())
                  {
          
                      String key = (String)boucleproperties.next();
                      String valeur = properties.getProperty(key);
                      
                      if (nomfic.equals(ficBase)) {
                      //recup the name of the unity
                      valeur = valeur.substring(valeur.indexOf("(")+1, (valeur.indexOf("-")));
                      		if(valeur.equals(choix)){
                      			chaineconversion= chaineconversion + "," +key;

                      		}
                      }
                      else {
                    	  chaineconversion=chaineconversion + "," + key;
                      }
                      
                      
                      
                      }
                  }

              catch (Exception e)
              {
                  System.out.println(e.toString());
              }
  
            	  chaineconversion= chaineconversion.substring(1);
              
              
  
              return(chaineconversion);
          }
          
     //*************************************************************************
     //                    Function to ready the property file
     //*************************************************************************
          public static Properties lecture(String nomfic)
          {
              final String UNITES_FILE = nomfic;
              Properties properties=null;
              InputStream stream = null;
              if (properties == null) {
                  try {
                      properties = new Properties();
                      properties.load(new FileInputStream(UNITES_FILE));

                  } catch (final Exception exp) {

                  } finally {
                      try {
                          if (stream != null) {
                              stream.close();
                          }
                      } catch (final IOException e) {

                      }
                  }
              }
              return(properties);
          }
}
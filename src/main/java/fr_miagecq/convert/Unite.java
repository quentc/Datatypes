package fr_miagecq.convert;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

enum SYSTEME{Metrique, Imperial}
enum TYPEESPECES{Poids, Longueur, Volume, Angle, Temperature, Devises}
public class Unite {

	public String nom;
	public String valeur;
	public SYSTEME systeme;
	public TYPEESPECES espece;	
	
}

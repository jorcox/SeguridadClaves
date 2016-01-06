package firmaSimetrica;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ClaveSimetricaEmisor {
		public static void main(String[] args) {
			
			try{
				long beKey = System.currentTimeMillis();
			    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
			    SecretKey clavePrivada = keygenerator.generateKey();
			    long afKey = System.currentTimeMillis();			    
			    System.out.println("Tiempo de generacion de claves de 56 bytes -> " + (afKey - beKey) );
			    
			    beKey = System.currentTimeMillis();
			    keygenerator = KeyGenerator.getInstance("DES");
			    SecretKey clavePrivada56 = keygenerator.generateKey();
			    afKey = System.currentTimeMillis();			    
			    System.out.println("Tiempo de generacion de claves de 56 bytes -> " + (afKey - beKey) );
			    
			    beKey = System.currentTimeMillis();
			    keygenerator = KeyGenerator.getInstance("AES");
			    SecretKey clavePrivada128 = keygenerator.generateKey();
			    afKey = System.currentTimeMillis();			    
			    System.out.println("Tiempo de generacion de claves de 128 bytes -> " + (afKey - beKey) );
			    
			    beKey = System.currentTimeMillis();
			    keygenerator = KeyGenerator.getInstance("HmacSHA256");
			    SecretKey clavePrivada256 = keygenerator.generateKey();
			    afKey = System.currentTimeMillis();			    
			    System.out.println("Tiempo de generacion de claves de 256 bytes -> " + (afKey - beKey) );
			    
			    Cipher desCipher;

			    // Create the cipher 
			    desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			    
			    // Initialize the cipher for encryption
			    desCipher.init(Cipher.ENCRYPT_MODE, clavePrivada);
			    
			    Scanner listaFicheros = new Scanner(new File(args[0]));
			    int cuenta = 0;
			    while(listaFicheros.hasNextLine()){
			    	FileInputStream inputText = new FileInputStream(listaFicheros.nextLine());
			    	
			    	cuenta++;
			    	
			    	byte[] text = new byte[inputText.available()];
				    //sensitive information
				    //byte[] text = "Nadie puede verme. Soy Nicolas Maduro.".getBytes();
			    	
			    	inputText.read(text);
			    	inputText.close();
	
				    System.out.println("Texto " + cuenta + " en bytes : " + text);
				    System.out.println("Texto " + cuenta + " : " + new String(text));
				   
				    long beEnc = System.currentTimeMillis();
				    // Encrypt the text
				    byte[] textEncrypted = desCipher.doFinal(text);
				    long afEnc = System.currentTimeMillis();
				    
				    System.out.println("Tiempo de encriptacion del texto " + cuenta + " -> " + (afEnc - beEnc) );
	
				    System.out.println("Texto " + cuenta + " encriptado : " + textEncrypted);
				    
		        	/*
		        	 * Guardando el fichero encriptado
		        	 */
		        	FileOutputStream sigfos = new FileOutputStream("textoEncriptado" + cuenta);
		        	sigfos.write(textEncrypted);
		        	sigfos.close();

				}
			    /*
			     * Guardo la clave secreta en un fichero
			     */
			    byte[] key = clavePrivada.getEncoded();
	        	FileOutputStream keyfos = new FileOutputStream("privada");
	        	keyfos.write(key);
	        	keyfos.close();
	        	
	        	

			    

			    
			}catch(NoSuchAlgorithmException e){
				e.printStackTrace();
			}catch(NoSuchPaddingException e){
				e.printStackTrace();
			}catch(InvalidKeyException e){
				e.printStackTrace();
			}catch(IllegalBlockSizeException e){
				e.printStackTrace();
			}catch(BadPaddingException e){
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		   
		}
	}


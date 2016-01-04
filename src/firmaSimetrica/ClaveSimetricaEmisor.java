package firmaSimetrica;


import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ClaveSimetricaEmisor {
		public static void main(String[] argv) {
			
			try{

			    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
			    SecretKey clavePrivada = keygenerator.generateKey();
			    
			    Cipher desCipher;

			    // Create the cipher 
			    desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			    
			    // Initialize the cipher for encryption
			    desCipher.init(Cipher.ENCRYPT_MODE, clavePrivada);

			    //sensitive information
			    byte[] text = "Nadie puede verme. Soy Nicolas Maduro.".getBytes();

			    System.out.println("Texto en bytes : " + text);
			    System.out.println("Texto : " + new String(text));
			   
			    // Encrypt the text
			    byte[] textEncrypted = desCipher.doFinal(text);

			    System.out.println("Texto encriptado : " + textEncrypted);

			    
			    /*
			     * Guardo la clave secreta en un fichero
			     */
			    byte[] key = clavePrivada.getEncoded();
	        	FileOutputStream keyfos = new FileOutputStream("privada");
	        	keyfos.write(key);
	        	keyfos.close();
	        	
	        	
	        	/*
	        	 * Guardando el fichero encriptado
	        	 */
	        	FileOutputStream sigfos = new FileOutputStream("textoEncriptado");
	        	sigfos.write(textEncrypted);
	        	sigfos.close();
			    

			    
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


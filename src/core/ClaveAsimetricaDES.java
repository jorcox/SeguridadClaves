package core;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ClaveAsimetricaDES {
	public static void main(String[] argv) {
		
		try{

		    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
		    keyGen.initialize(1024);
		    KeyPair keyPair = keyGen.genKeyPair();
		    PublicKey clavePublica = keyPair.getPublic();
		    PrivateKey clavePrivada = keyPair.getPrivate();
		    
		    Signature dsa = Signature.getInstance("DSA");
		    
		    dsa.initSign(clavePrivada);
		    
		    //sensitive information
		    byte[] text = "Nadie puede verme. Soy Nicolas Maduro.".getBytes();
		    
		    dsa.update(text);
		    
		    byte[] textEncrypted = dsa.sign();
		    
		    dsa.initVerify(clavePublica);
		    
		    dsa.update(textEncrypted);
		    
		    boolean ver = dsa.verify(textEncrypted);
		    System.out.println("Resultado -> " + ver);
		    
		    
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}catch (SignatureException e) {
			e.printStackTrace();
		} 
	   
	}
}

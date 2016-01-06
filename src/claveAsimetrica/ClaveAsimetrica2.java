package claveAsimetrica;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ClaveAsimetrica2 {
	
	public static void main(String[] args) {
		/* 100 mensajes aleatorios */
		int[] keySizes = {1024, 2048, 4096};
		for (int i=0; i<keySizes.length; i++) {
			cifrarYDescifrar(keySizes[i]);
		}
	}
	
	private static void cifrarYDescifrar(int size) {
		try {
			/* Calcula el maximo de bytes que se pueden cifrar con estas claves */
			int maxSize = size / 8 - 11;
			System.out.println("Bytes maximos que se pueden cifrar con claves de " + size + " bytes: " + 
					(maxSize));
			
			/* Genera las claves RSA publica y privada */
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(size);
			long t1 = new Date().getTime();
			KeyPair keyPair = keyGen.genKeyPair();
			long t2 = new Date().getTime();
			System.out.println("Tiempo empleado en crear claves de " + size + " bytes: " + (t2-t1) + " ms");
			
		    PublicKey clavePublica = keyPair.getPublic();
		    PrivateKey clavePrivada = keyPair.getPrivate();
		    
		    /* Obtiene 100 mensajes aleatorios */
		    Random gen = new Random();
		    long acumCifrado = 0;
		    long acumDescifrado = 0;
		    for (int i=0; i<100; i++) {
		    	/* Crea el mensaje aleatorio */
		    	byte[] bytesMensaje = new byte[maxSize];
		    	gen.nextBytes(bytesMensaje);
		    	
		    	/* Cifra el mensaje aleatorio con la clave publica */
			    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			    cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
			    t1 = new Date().getTime();
			    byte[] bytesCifrado = cipher.doFinal(bytesMensaje);
			    t2 = new Date().getTime();
			    acumCifrado += (t2-t1);
			    
			    /* Descifra el mensaje aleatorio con la clave privada */
				Cipher desCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				desCipher.init(Cipher.DECRYPT_MODE, clavePrivada);
				t1 = new Date().getTime();
				desCipher.doFinal(bytesCifrado);
				t2 = new Date().getTime();
				acumDescifrado += (t2-t1);
		    }
		    System.out.println("Tiempo empleado en cifrar los mensajes: " + acumCifrado + " ms");
		    System.out.println("Tiempo empleado en descifrar los mensajes: " + acumDescifrado + "ms\n");
        	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
	}
}

package claveAsimetrica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ClaveAsimetrica1 {
	
	public static void main(String[] args) {
		/* 25 documentos */
		int[] keySizes = {1024, 2048, 4096};
		for (int i=0; i<keySizes.length; i++) {
			cifrar(keySizes[i]);
			descifrar(keySizes[i]);
		}
	}
	
	private static void cifrar(int size) {
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
		    
		    /* Obtiene cada fichero del directorio */
		    File dir = new File("ficheros");
		    String[] files = dir.list();
		    long acum = 0;
		    for (int i=0; i<files.length; i++) {
		    	/* Obtiene el mensaje del fichero */
		    	BufferedReader reader = new BufferedReader(new FileReader(new File(dir+"/"+files[i])));
		    	String content = "";
		    	String line = reader.readLine();
		    	while (line != null) {
		    		content += line;
		    		line = reader.readLine();
		    	}
		    	reader.close();
		    	byte[] bytesMensaje = content.getBytes("UTF8");
		    	
		    	/* Cifra el mensaje del fichero con la clave publica */
			    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			    cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
			    t1 = new Date().getTime();
			    byte[] bytesCifrado = cipher.doFinal(bytesMensaje);
			    t2 = new Date().getTime();
			    acum += (t2-t1);
				
				/* Guarda el mensaje */
	        	FileOutputStream outputMensaje = new FileOutputStream("asimetrica1/cifrados/"+size+"/"+files[i]);
	        	outputMensaje.write(bytesCifrado);
	        	outputMensaje.close();
		    }
		    System.out.println("Tiempo empleado en cifrar los mensajes: " + acum + " ms");
			
			/* Guarda la clave publica */
		    byte[] bytesPublica = clavePublica.getEncoded();
        	FileOutputStream outputPublica = new FileOutputStream("asimetrica1/publica"+size);
        	outputPublica.write(bytesPublica);
        	outputPublica.close();
			
			/* Guarda la clave privada */
		    byte[] bytesPrivada = clavePrivada.getEncoded();
        	FileOutputStream outputPrivada = new FileOutputStream("asimetrica1/privada"+size);
        	outputPrivada.write(bytesPrivada);
        	outputPrivada.close();
        	
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void descifrar(int size) {
		try {
			/* Recupera la clave privada */
			FileInputStream inputPrivada = new FileInputStream("asimetrica1/privada" + size);
			byte[] bytesPrivada = new byte[inputPrivada.available()];
			inputPrivada.read(bytesPrivada);
			inputPrivada.close();
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey clavePrivada = kf.generatePrivate(new PKCS8EncodedKeySpec(bytesPrivada));
			
			/* Obtiene cada fichero del directorio */
			File dir = new File("asimetrica1/cifrados/"+size);
		    String[] files = dir.list();
		    long acum = 0;
		    for (int i=0; i<files.length; i++) {
		    	/* Lee el mensaje del fichero cifrado */
		    	FileInputStream inputMensaje = new FileInputStream("asimetrica1/cifrados/"+size+"/"+files[i]);
				byte[] bytesMensaje = new byte[inputMensaje.available()];
				inputMensaje.read(bytesMensaje);
				inputMensaje.close();

				/* Descifra el mensaje */
				Cipher desCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				long t1 = new Date().getTime();
				desCipher.init(Cipher.DECRYPT_MODE, clavePrivada);
				long t2 = new Date().getTime();
				acum += (t2-t1);
				byte[] mensajeOriginal = desCipher.doFinal(bytesMensaje);
				
				/* Almacena el mensaje descifrado */
	        	FileOutputStream outputMensaje = new FileOutputStream("asimetrica1/descifrados/"+size+"/"+files[i]);
	        	outputMensaje.write(mensajeOriginal);
	        	outputMensaje.close();
		    }
		    System.out.println("Tiempo empleado en descifrar los mensajes: " + acum + " ms\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
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

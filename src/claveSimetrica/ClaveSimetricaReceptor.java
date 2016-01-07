package claveSimetrica;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public class ClaveSimetricaReceptor {

	public static void main(String[] args) {
		
		if(args.length<1) 
			System.out.println("Uso -> Arg1 = listaFicherosDesencriptar Arg2 = ClavePrivada ");
		
		/*
		 * Utilizamos algoritmo DES para generar la keyFactory 
		 * y leer la clave privada 56 b
		 * 
		 * Para descifrar usamos DES/ECB/PKCS5Padding, es decir, 
		 * algoritmo DES con el modo ElectronicCodeBook con relleno PCKS5.
		 * 
		 */
			
		try {
			/*
			 * Lectura de la clave secreta
			 */
			FileInputStream keyfis = new FileInputStream(args[1]);
			byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			keyfis.close();
			

			SecretKeySpec secKeySpec = new SecretKeySpec(encKey, "DES");

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			SecretKey claveSecreta = keyFactory.generateSecret(secKeySpec);

			Cipher desCipher;

			// Create the cipher
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Initialize the same cipher for decryption
			desCipher.init(Cipher.DECRYPT_MODE, claveSecreta);
			
			Scanner listaFicheros = new Scanner(new File(args[0]));
		    int cuenta = 0;
		    while(listaFicheros.hasNextLine()){
		    	FileInputStream inputText = new FileInputStream(listaFicheros.nextLine());
		    	cuenta++;

				/*
				 * Lectura del fichero encriptado
				 */
				byte[] textoEncriptado = new byte[inputText.available()];
				inputText.read(textoEncriptado);
				inputText.close();
	
				/*
				 * Desencriptacion del texto
				 */
				long beDeEnc = System.currentTimeMillis();
				byte[] textDecrypted = desCipher.doFinal(textoEncriptado);
				long adDeEnc = System.currentTimeMillis();
				
				System.out.println("Tiempo de desencriptacion del texto " + cuenta + " -> " + (adDeEnc - beDeEnc) );
	
				System.out.println("Texto " + cuenta + " desencriptado : " + new String(textDecrypted));
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

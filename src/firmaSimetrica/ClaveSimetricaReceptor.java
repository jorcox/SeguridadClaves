package firmaSimetrica;

import java.io.FileInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public class ClaveSimetricaReceptor {

	public static void main(String[] args) {
		try {
			/*
			 * Lectura de la clave secreta
			 */
			FileInputStream keyfis = new FileInputStream(args[0]);
			byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			keyfis.close();

			/*
			 * Lectura del fichero encriptado
			 */
			FileInputStream ficheroTextoEncriptado = new FileInputStream(args[1]);
			byte[] textoEncriptado = new byte[ficheroTextoEncriptado.available()];
			ficheroTextoEncriptado.read(textoEncriptado);
			ficheroTextoEncriptado.close();

			SecretKeySpec secKeySpec = new SecretKeySpec(encKey, "DES");

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			SecretKey claveSecreta = keyFactory.generateSecret(secKeySpec);

			Cipher desCipher;

			// Create the cipher
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Initialize the same cipher for decryption
			desCipher.init(Cipher.DECRYPT_MODE, claveSecreta);

			/*
			 * Desencriptacion del texto
			 */
			byte[] textDecrypted = desCipher.doFinal(textoEncriptado);

			System.out.println("Texto desencriptado : " + new String(textDecrypted));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

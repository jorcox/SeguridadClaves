package firmaDigital;

import java.io.*;
import java.security.*;

class GenSig {

    public static void main(String[] args) {

        /* Generate a DSA signature */

        if (args.length != 1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {

        	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        	/*
        	 * Fuente de aleatoriedad
        	 */
        	SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        	/*
        	 * 1024 es el tamaño de la clave
        	 */
        	keyGen.initialize(1024, random);
        	
        	Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 

        	KeyPair pair = keyGen.generateKeyPair();
        	PrivateKey priv = pair.getPrivate();
        	PublicKey pub = pair.getPublic();
        	
        	dsa.initSign(priv);
        	
        	/*
        	 * Firmando el fichero
        	 */
        	FileInputStream fis = new FileInputStream(args[0]);
        	BufferedInputStream bufin = new BufferedInputStream(fis);
        	byte[] buffer = new byte[1024];
        	int len;
        	while ((len = bufin.read(buffer)) >= 0) {
        	    dsa.update(buffer, 0, len);
        	};
        	bufin.close();
        	
        	/*
        	 * Firma final
        	 */        	
        	byte[] realSig = dsa.sign();
        	
        	/*
        	 * Guardando la firma final en fichero
        	 */
        	FileOutputStream sigfos = new FileOutputStream("firma");
        	sigfos.write(realSig);
        	sigfos.close();
        	
        	/*
        	 * Guardando la clave publica 
        	 */
        	byte[] key = pub.getEncoded();
        	FileOutputStream keyfos = new FileOutputStream("publica");
        	keyfos.write(key);
        	keyfos.close();
        	
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}
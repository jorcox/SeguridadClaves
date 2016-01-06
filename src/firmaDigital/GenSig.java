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
        	        	
        	Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 
        	
        	long beKey = System.currentTimeMillis();
        	keyGen.initialize(512, random);
        	KeyPair pair = keyGen.generateKeyPair();
        	PrivateKey priv = pair.getPrivate();
        	PublicKey pub = pair.getPublic();
        	long afKey = System.currentTimeMillis();
        	
        	System.out.println("Tiempo de generacion de claves de 512 bytes -> " + (afKey - beKey) );
        	
        	beKey = System.currentTimeMillis();
        	keyGen.initialize(1024, random);
        	pair = keyGen.generateKeyPair();
        	PrivateKey priv1024 = pair.getPrivate();
        	PublicKey pub1024 = pair.getPublic();
        	afKey = System.currentTimeMillis();
        	
        	System.out.println("Tiempo de generacion de claves de 1024 bytes -> " + (afKey - beKey) );
        	
        	beKey = System.currentTimeMillis();
        	keyGen.initialize(2048, random);
        	pair = keyGen.generateKeyPair();
        	PrivateKey priv2048 = pair.getPrivate();
        	PublicKey pub2048 = pair.getPublic();
        	afKey = System.currentTimeMillis();
        	
        	System.out.println("Tiempo de generacion de claves de 2048 bytes -> " + (afKey - beKey) );
        	
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
        	long beSign = System.currentTimeMillis();
        	byte[] realSig = dsa.sign();
        	long afSign = System.currentTimeMillis();
        	
        	System.out.println("Tiempo de firma -> " + (afSign - beSign) );
        	
        	
        	
        	
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
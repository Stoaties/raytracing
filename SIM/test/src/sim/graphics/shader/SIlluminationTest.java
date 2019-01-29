/**
 * 
 */
package sim.graphics.shader;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.graphics.SColor;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SIllumination</b>.
 * 
 * @author Simon Vézina
 * @since 2017-02-16
 * @version 2017-08-19
 */
public class SIlluminationTest {

	/**
	 * Test de l'illumination ambiante avec une source de lumière blanche et noire.
	 */
	@Test
	public void ambientReflexionTest1()
	{
		try{
			
		SColor black_light = SColor.BLACK;
		SColor white_light = SColor.WHITE;
		
		SColor random_material = new SColor(0.6, 0.5, 0.2);
		
		// Test de la couleur ambiante blanche
		Assert.assertEquals(random_material, SIllumination.ambientReflexion(white_light, random_material));
		
		// Test de la couleur ambiant noire
		Assert.assertEquals(black_light, SIllumination.ambientReflexion(black_light, random_material));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void ambientReflexionTest1()");
		}
	}

	/**
	 * Test de l'illumination ambiante avec une source de lumière quelconque.
	 */
	@Test
	public void ambientReflexionTest2()
	{
		try{
			
		SColor light = new SColor(0.2, 0.3, 0.4);
		SColor material = new SColor(0.9, 0.8, 0.7);
		
		Assert.assertEquals(new SColor(0.18, 0.24, 0.28), SIllumination.ambientReflexion(light, material));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void ambientReflexionTest2()");
		}
	}
	
	/**
	 * Test de l'illumination diffuse avec une source de lumière blanche et noire avec des orientations parallèles.
	 */
	@Test
	public void lambertianReflexionTest1()
	{
		try{
			
		SColor black_light = SColor.BLACK;
		SColor white_light = SColor.WHITE;
		
		SVector3d normal = new SVector3d(0.0, 0.0, 1.0).normalize();
		SVector3d light_direction = new SVector3d(0.0, 0.0, -1.0).normalize();
		
		SColor random_material = new SColor(0.6, 0.5, 0.2);
		
		// Test de la couleur ambiante blanche
		Assert.assertEquals(random_material, SIllumination.lambertianReflexion(white_light, random_material, normal, light_direction));
		
		// Test de la couleur ambiant noire
		Assert.assertEquals(black_light, SIllumination.lambertianReflexion(black_light, random_material, normal, light_direction));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void lambertianReflexionTest1()");
		}
	}
	
	/**
	 * Test de l'illumination diffuse avec une source de lumière quelconque et des orientations quelconques, mais sens opposé.
	 * Il y aura de l'illumination.
	 */
	@Test
	public void lambertianReflexionTest2()
	{
		try{
			
		SColor light = new SColor(0.2, 0.3, 0.4);
		SColor material = new SColor(0.9, 0.8, 0.7);
		
		SVector3d normal = new SVector3d(2.3, 4.5, 8.7).normalize();
		SVector3d light_direction = new SVector3d(-1.2, -5.6, -2.4).normalize();
		
		Assert.assertEquals(new SColor(0.1407100214802341, 0.18761336197364545, 0.21888225563591968), SIllumination.lambertianReflexion(light, material, normal, light_direction));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void lambertianReflexionTest2()");
		}
	}
	
	/**
	 * Test de l'illumination diffuse avec une source de lumière quelconque et des orientations quelconques, mais de même sens.
	 * Il n'y aura pas d'illumination
	 */
	@Test
	public void lambertianReflexionTest3()
	{
		try{
			
		SColor light = new SColor(0.2, 0.3, 0.4);
		SColor material = new SColor(0.9, 0.8, 0.7);
		
		SVector3d normal = new SVector3d(2.3, 4.5, 8.7).normalize();
		SVector3d light_direction = new SVector3d(1.2, 5.6, 2.4).normalize();
		
		// Test de la couleur ambiante blanche
		Assert.assertEquals(new SColor(0.0, 0.0, 0.0), SIllumination.lambertianReflexion(light, material, normal, light_direction));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void lambertianReflexionTest3()");
		}
	}
	
	/**
	 * Test de l'illumination spéculaire de Blinn avec une source de lumière blanche et noire avec des orientations parallèles.
	 */
	@Test
	public void blinnSpecularReflexionTest1()
	{
		try{
			
		SColor black_light = SColor.BLACK;
		SColor white_light = SColor.WHITE;
		
		SVector3d normal = new SVector3d(0.0, 0.0, 1.0).normalize();
		SVector3d light_direction = new SVector3d(0.0, 0.0, -1.0).normalize();
		SVector3d ray_direction = new SVector3d(0.0, 0.0, -1.0).normalize();
		double random_n = 6.9;	// valeur aléatoire
		
		SColor random_material = new SColor(0.6, 0.5, 0.2);
		
		// Test de la couleur ambiante blanche
		Assert.assertEquals(random_material, SIllumination.blinnSpecularReflexion(white_light, random_material, normal, ray_direction, light_direction, random_n));
		
		// Test de la couleur ambiant noire
		Assert.assertEquals(black_light, SIllumination.blinnSpecularReflexion(black_light, random_material, normal, ray_direction, light_direction, random_n));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void blinnSpecularReflexionTest1()");
		}
	}
	
	/**
	 * Test de l'illumination spéculaire de Blinn avec une source de lumière quelconque et des orientations quelconques, mais sens opposé.
	 * Il y aura de l'illumination.
	 */
	@Test
	public void blinnSpecularReflexionTest2()
	{
		try{
			
		SColor light = new SColor(0.2, 0.3, 0.4);
		SColor material = new SColor(0.9, 0.8, 0.7);
		
		SVector3d normal = new SVector3d(2.3, 4.5, 8.7).normalize();
		SVector3d light_direction = new SVector3d(-1.2, -5.6, -2.4).normalize();
		SVector3d ray_direction = new SVector3d(4.5, -8.4, -3.0).normalize();
		double n = 3.2;	
		
		Assert.assertEquals(new SColor(0.05624968851560261, 0.07499958468747013, 0.08749951546871515), SIllumination.blinnSpecularReflexion(light, material, normal, ray_direction, light_direction, n));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void blinnSpecularReflexionTest2()");
		}
	}
	
	/**
	 * Test de l'illumination spéculaire de Blinn avec une source de lumière quelconque et des orientations quelconques, mais de même sens.
	 * Il n'y aura pas d'illumination
	 */
	@Test
	public void blinnSpecularReflexionTest3()
	{
		try{
			
		SColor light = new SColor(0.2, 0.3, 0.4);
		SColor material = new SColor(0.9, 0.8, 0.7);
		
		SVector3d normal = new SVector3d(2.3, 4.5, 8.7).normalize();
		SVector3d light_direction = new SVector3d(1.2, 5.6, 2.4).normalize();
		SVector3d ray_direction = new SVector3d(4.5, -8.4, -3.0).normalize();
		double n = 3.2;	
		
		// Test de la couleur ambiante blanche
		Assert.assertEquals(new SColor(0.0, 0.0, 0.0), SIllumination.blinnSpecularReflexion(light, material, normal, ray_direction, light_direction, n));
		
		}catch(SNoImplementationException e){
			SLog.logWriteLine("SIlluminationTest ---> Test non effectué : public void blinnSpecularReflexionTest3()");
		}
	}
	
}//fin de la classe SIlluminationTest

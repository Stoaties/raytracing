/**
 * 
 */
package sim.readwrite.object;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * @author Simon
 * @since 2017-11-16
 * @version 2017-11-16
 */
public class SSceneRW extends SAbstractReadableWriteable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond à un tableau contenant l'ensemble des mots clés 
   * à utiliser reconnus lors de la définition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_CAMERA, SKeyWordDecoder.KW_VIEWPORT, SKeyWordDecoder.KW_RAYTRACER,
    SKeyWordDecoder.KW_PRIMITIVE, SKeyWordDecoder.KW_BLINN_MATERIAL, SKeyWordDecoder.KW_TEXTURE_MATERIAL, 
    SKeyWordDecoder.KW_AMBIENT_LIGHT, SKeyWordDecoder.KW_DIRECTIONAL_LIGHT, SKeyWordDecoder.KW_POINT_LIGHT,
    SKeyWordDecoder.KW_LINEAR_APERTURE_LIGHT, SKeyWordDecoder.KW_RECTANGULAR_APERTURE_LIGHT,
    SKeyWordDecoder.KW_ELLIPTICAL_APERTURE_LIGHT,SKeyWordDecoder.KW_APERTURE_MASK_LIGHT,
    SKeyWordDecoder.KW_MODEL     
  };
  
  
  // Ensemble des éléments pouvant être lu dans une scène
  private final List<SCameraRW> camera_list;
  
  
  
  /**
   * 
   */
  public SSceneRW()
  {
    camera_list = new ArrayList<SCameraRW>();  
  }

  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line)throws SReadingException, IOException
  {
    try{
      switch(code)
      {
        case SKeyWordDecoder.CODE_CAMERA :                camera_list.add(new SCameraRW(sbr)); return true;
                    
        /*
        case SKeyWordDecoder.CODE_VIEWPORT :              viewport = new SViewport(sbr); return true;
                        
        case SKeyWordDecoder.CODE_RAYTRACER :             raytracer_builder = new SRaytracerBuilder(sbr); return true;
        
        case SKeyWordDecoder.CODE_PRIMITIVE :             primitive_list.add(new SPrimitive(sbr)); return true;
                                
        case SKeyWordDecoder.CODE_BLINN_MATERIAL:         material_list.add(new SBlinnMaterial(sbr)); return true;
        
        case SKeyWordDecoder.CODE_TEXTURE_MATERIAL :      material_list.add(new SBlinnTextureMaterial(sbr)); return true;
        
        case SKeyWordDecoder.CODE_AMBIENT_LIGHT :         light_list.add(new SAmbientLight(sbr)); return true;
                                
        case SKeyWordDecoder.CODE_DIRECTIONAL_LIGHT :     light_list.add(new SDirectionalLight(sbr)); return true;
                                
        case SKeyWordDecoder.CODE_POINT_LIGHT :           light_list.add(new SPointLight(sbr)); return true;
        
        case SKeyWordDecoder.CODE_LINEAR_APERTURE_LIGHT : light_list.add(new SLinearApertureLight(sbr)); return true;
        
        case SKeyWordDecoder.CODE_RECTANGULAR_APERTURE_LIGHT : light_list.add(new SRectangularApertureLight(sbr)); return true;
        
        case SKeyWordDecoder.CODE_ELLIPTICAL_APERTURE_LIGHT : light_list.add(new SEllipticalApertureLight(sbr)); return true;
        
        case SKeyWordDecoder.CODE_APERTURE_MASK_LIGHT : light_list.add(new SApertureMaskLight(sbr)); return true;
        
        
        case SKeyWordDecoder.CODE_MODEL :   SModelReader m_reader = new SModelReader(sbr);
        
                                            if(m_reader.asRead())
                                              model_list.add((SModel)m_reader.getValue());
                                            
                                            return true;
        */
        
        default : return false;
      }
    }catch(SConstructorException e){
      throw new SReadingException("Erreur SSceneRW 004 : La construction d'un élément de la scène est impossible.", e);
    }
  }

  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    writeComment(bw, "Paramètres du rendu");
    
    for(SCameraRW c : camera_list)
      c.write(bw);
    
  }
  
  @Override
  protected void readingInitialization() throws SInitializationException
  {
    
  }
  
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_SCENE;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
    
}// fin de la classe SSceneRW

/**
 * 
 */
package sim.geometry.space;

import java.util.List;
import java.util.Map;

import sim.geometry.SGeometry;

/**
 * La classe <b>SVoxelSpaceEntry</b> est une cellule regroupant des informations en lien avec un <b>SVoxelSpace</b>.
 * 
 * @author Simon V�zina
 * @since 2016-01-31
 * @version 2016-02-04
 */
public class SVoxelSpaceEntry {

  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>voxel_map</b> correspond � la carte des voxels de cette cellule.
   */
  private final Map<SVoxel, List<SGeometry>> voxel_map;
  
  /**
   * La variable <b>voxel_builder</b> correspond au constructeur de voxels de cette cellule.
   */
  private final SVoxelBuilder voxel_builder;
  
  /**
   * La variable <b>absolute_extremum_voxel</b> correspond au voxel d'extr�me en lien avec la carte de voxel <b>voxel_map</b> de cette cellule. 
   */
  private final SVoxel absolute_extremum_voxel;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une cellule d'une carte de voxels.
   * 
   * @param voxel_map - La carte des voxels.
   * @param voxel_builder - Le constructeur de voxel attitr� � la carte.
   * @param absolute_extremum_voxel - Le voxel d'extr�me attitr� � la carte.
   */
  public SVoxelSpaceEntry(Map<SVoxel, List<SGeometry>> voxel_map, SVoxelBuilder voxel_builder, SVoxel absolute_extremum_voxel)
  {
    this.voxel_map = voxel_map;
    this.voxel_builder = voxel_builder;
    this.absolute_extremum_voxel = absolute_extremum_voxel;
  }
  
  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir la carte des voxels de la cellule.
   * 
   * @return La carte des voxels.
   */
  public Map<SVoxel, List<SGeometry>> getVoxelMap()
  {
    return voxel_map;
  }
  
  /**
   * M�thode pour obtenir le constructeur de voxels attitr� � la carte de voxels de la cellule.
   * 
   * @return Le constructeur de voxels.
   */
  public SVoxelBuilder getVoxelBuilder()
  {
    return voxel_builder;
  }
  
  /**
   * M�thode pour obtenir le voxel d'extr�me attitr� � la carte de voxels de la cellule.
   * 
   * @return Le voxel d'extr�me.
   */
  public SVoxel getAbsoluteExtremumVoxel()
  {
    return absolute_extremum_voxel;
  }
  
}//fin de la classe SVoxelSpaceEntry

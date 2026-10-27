package cn.mcmod.corn_delight.legacy.compat;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
public final class Delight {
 public static DelightIntegration integration;
 private Delight() {}
 public static void select() {
  ModContainer mod=Loader.instance().getIndexedModList().get("farmersdelight");
  String entry=mod==null || mod.getMod()==null ? "" : mod.getMod().getClass().getName();
  String adapter;
  if(entry.equals("vectorwing.farmersdelight.legacy.Foundation")) adapter="MaintainedDelight";
  else if(entry.equals("com.wdcftgg.farmersdelightlegacy.FarmersDelightLegacy")) adapter="AlternativeDelight";
  else throw new IllegalStateException("Unsupported Farmer's Delight implementation: "+entry);
  if(adapter.equals("AlternativeDelight") && new org.apache.maven.artifact.versioning.ComparableVersion(mod.getVersion()).compareTo(new org.apache.maven.artifact.versioning.ComparableVersion("1.1.7"))<0)
   throw new IllegalStateException("This addon requires ukmojb Farmer's Delight Legacy 1.1.7 or newer");
  try {
   integration=(DelightIntegration)Class.forName(Delight.class.getPackage().getName()+"."+adapter).newInstance();
  } catch(ReflectiveOperationException | LinkageError failure) {
   throw new IllegalStateException("Cannot initialize "+adapter+"; install a supported Farmer's Delight version",failure);
  }
  org.apache.logging.log4j.LogManager.getLogger("corn_delight").info("Selected Farmer's Delight integration: {}",adapter);
 }
}

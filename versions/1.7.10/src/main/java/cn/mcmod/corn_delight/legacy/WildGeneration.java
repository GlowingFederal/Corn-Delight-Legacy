package cn.mcmod.corn_delight.legacy;
public final class WildGeneration implements cpw.mods.fml.common.IWorldGenerator {
 public void generate(java.util.Random random,int chunkX,int chunkZ,net.minecraft.world.World world,net.minecraft.world.chunk.IChunkProvider generator,net.minecraft.world.chunk.IChunkProvider provider) {
  if(!CornDelight.generate || world.provider.dimensionId!=0 || random.nextInt(CornDelight.chance)!=0) return;
  int x=chunkX*16+2+random.nextInt(12),z=chunkZ*16+2+random.nextInt(12);
  float temperature=world.getBiomeGenForCoords(x,z).temperature;
  if(temperature<=0 || temperature>1) return;
  for(int i=0;i<64;i++) {
   int px=x+random.nextInt(5)-2,pz=z+random.nextInt(5)-2;
   int y=world.getHeightValue(px,pz);if(y<1 || y>=255) continue; if(world.getBlock(px,y-1,pz)==net.minecraft.init.Blocks.grass && world.isAirBlock(px,y,pz)) world.setBlock(px,y,pz,CornDelight.wild,0,2);
  }
 }
}

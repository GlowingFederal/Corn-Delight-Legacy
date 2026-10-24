package cn.mcmod.corn_delight.legacy;
public final class WildGeneration implements net.minecraftforge.fml.common.IWorldGenerator {
 public void generate(java.util.Random random,int chunkX,int chunkZ,net.minecraft.world.World world,net.minecraft.world.gen.IChunkGenerator generator,net.minecraft.world.chunk.IChunkProvider provider) {
  if(!CornDelight.generate || world.provider.getDimension()!=0 || random.nextInt(CornDelight.chance)!=0) return;
  int x=chunkX*16+2+random.nextInt(12),z=chunkZ*16+2+random.nextInt(12);
  float temperature=world.getBiome(new net.minecraft.util.math.BlockPos(x,64,z)).getTemperature(new net.minecraft.util.math.BlockPos(x,64,z));
  if(temperature<=0 || temperature>1) return;
  for(int i=0;i<64;i++) {
   int px=x+random.nextInt(5)-2,pz=z+random.nextInt(5)-2;
   net.minecraft.util.math.BlockPos pos=world.getHeight(new net.minecraft.util.math.BlockPos(px,0,pz));if(pos.getY()<1 || pos.getY()>=255) continue;if(world.getBlockState(pos.down()).getBlock()==net.minecraft.init.Blocks.GRASS && world.isAirBlock(pos)) world.setBlockState(pos,CornDelight.wild.getDefaultState(),2);
  }
 }
}

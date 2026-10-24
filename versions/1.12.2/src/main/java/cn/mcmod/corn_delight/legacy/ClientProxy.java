package cn.mcmod.corn_delight.legacy;
public final class ClientProxy extends CommonProxy {
 public void preInit() { net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this); }
 @net.minecraftforge.fml.common.eventhandler.SubscribeEvent public void models(net.minecraftforge.client.event.ModelRegistryEvent event) {
 for(java.util.Map.Entry<String,net.minecraft.item.Item> entry:CornDelight.ITEMS.entrySet())
 net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(entry.getValue(),0,new net.minecraft.client.renderer.block.model.ModelResourceLocation("corn_delight:"+entry.getKey(),"inventory")); }
}
